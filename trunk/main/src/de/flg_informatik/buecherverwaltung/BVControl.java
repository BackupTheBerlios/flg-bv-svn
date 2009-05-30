package de.flg_informatik.buecherverwaltung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.util.Properties;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.Version;

public class BVControl implements Runnable,ActionListener,ChangeListener {
	/**
	 * controller for flg-bv,
	 * get properties, start all components in order
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	final String defaultfilename="main/buchverwaltung.default.xml";
	final String propertyfilename="buchverwaltung.xml";
	final String significantstring=".BuchverwaltungV01";
	java.util.Hashtable<BVSelectedEvent.SelectedEventType, BVUsecases> SwitchEvents;
	private static BVControl thecontrol;
	private BVView viewontop=null;
	Properties app_settings;
	Version version=new Version(new int[]{0,5},"09-02-02");
	BVSwitchUsecases switchusecases;
	BVGUI gui;
	BVStorage bvs;
	Connection connection;
	BVScanAdapter scanner;
	
	public BVControl(){
		
		app_settings=new FLGProperties(app_settings,propertyfilename, new File(defaultfilename), significantstring).getProperties();
		// new BVD("fetched properties");
		if (!BVDataBase.getDataBankDrivers(app_settings))
			javax.swing.JOptionPane.showMessageDialog(null, "'sun.jdbc.odbc.JdbcOdbcDriver' or 'com.mysql.jdbc.Driver' nicht gefunden.\nDas Programm wird jetzt beendet");
		
		do {
			
			connection=BVDataBase.getConnection(app_settings);
			if (connection == null) {
				javax.swing.JOptionPane.showMessageDialog(null, "Konnte nicht mit Datenbank verbinden, bitte Einstellungen überprüfen!");
				PropertiesDialog.showSettingsDialog(app_settings,propertyfilename, new File(defaultfilename), significantstring);
			}
		// new BVD("connecting");	
		} while (connection == null);
		// new BVD("connected");
		BVControl.thecontrol=this;
		BVUtils.setParams(this,connection);
		// new BVD("initialized UtilsParams");
		bvs=new BVStorage(this,connection);
		// new BVD("initialized bvs");
		BVBookUse.init();
		gui=new BVGUI(this);
		// new BVD("initialized gui");
		switchusecases = new BVSwitchUsecases();
		// new BVD("initialized switchusecases");
		SwitchEvents=new java.util.Hashtable<BVSelectedEvent.SelectedEventType, BVUsecases>();
		// new BVD("initialized SwitchEvents");
		scanner=new BVScanAdapter(this);
		// new BVD("initialized SCANNER");
		BVGUI.selectView(BVUsecases.Buchtypen); // default usecase
		// new BVD("End of Constructor of BVControl");
		
		
		
		
			
	}
	public static BVControl getControl(){
		return thecontrol;
	}
	
	
	
	private void shutDown(){
		BVDataBase.CloseConnection(connection);
		// new BVD("closing");
		// TODO: uncomment, implement
		// savePropertiesToXML(file, props, comment);
		System.exit(0);
	}

	public void run(){
		
	}
	
	
	
	/**
	 * Central Dispatcher of interaction (i.e. Eans or Clicks)
	 * Gets the present topView,
	 * changes it according to switch-matrix,
	 * 	in case of switch calls the views toBackground() method
	 * 		which can call a modal-box (to save, commit ...)
	 * 
	 * At last makes the event by calling
	 * 	BVSelectedEvent.makeEvent(source, id, ean, wildcards);
	 * 		maybe with some nulls 
	 * 
	 * @param Source: Object which caught the Ean or Click)
	 * 
	 * @param BVSelectetEventType: Type of interaction
	 * 
	 * 
	 * 
	 * 
	 */
	
	public void newEvent(Object source, SelectedEventType id){
		newEvent(source, id, null, 0);
	}
	public void newEvent(Object source, SelectedEventType id, Ean ean){
		newEvent(source, id, ean, 0);
	}
	public synchronized void newEvent(Object source, SelectedEventType id, Ean ean, int wildcards){
		BVView activeview=BVGUI.getSelectedView();
		// new BVD(activeview.getName()+"->"+id+"->"+switchusecases.get(id));
		
		if (!activeview.ConsumedEvents.contains(id)){
			activeview.toBackground();
			BVGUI.selectView(switchusecases.get(id));
		}
		BVSelectedEvent.makeEvent(source, id, ean, wildcards);
		
	}

	public void actionPerformed(ActionEvent e) {
		shutDown();
		
		
	}
	/** 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 * 
	 * de-selected view retires to Background
	 */
	public void stateChanged(ChangeEvent e) {
		if (viewontop!=null & !BVGUI.isSelectedView(viewontop)){
			viewontop.toBackground();
		}
		viewontop=BVGUI.getSelectedView();
		
	}
}
