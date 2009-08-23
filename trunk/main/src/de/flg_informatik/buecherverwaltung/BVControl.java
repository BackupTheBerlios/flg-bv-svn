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
	final private static boolean debug=true;
	private static BVControl thecontrol;
	private BVView viewontop=null;
	FLGProperties app_settings_pane;
	Properties app_settings;
	Version version=new Version(new int[]{0,6},"09-08-12");
	BVUsecases.Selected2Usecases  switchusecases;
	BVGUI gui;
	BVStorage bvs;
	Connection connection;
	BVScanAdapter scanner;
	
	public BVControl(){
		app_settings_pane=new FLGProperties(app_settings,propertyfilename, new File(defaultfilename), significantstring);
		app_settings=app_settings_pane.getProperties();
		new BVD(debug,"fetched properties");
		if (!BVDataBase.getDataBankDrivers(app_settings))
			javax.swing.JOptionPane.showMessageDialog(null, "'sun.jdbc.odbc.JdbcOdbcDriver' or 'com.mysql.jdbc.Driver' nicht gefunden.\nDas Programm wird jetzt beendet");
		
		do {
			
			connection=BVDataBase.getConnection(app_settings);
			if (connection == null) {
				javax.swing.JOptionPane.showMessageDialog(null, "Konnte nicht mit Datenbank verbinden, bitte Einstellungen überprüfen!");
				PropertiesDialog.showSettingsDialog(app_settings,propertyfilename, new File(defaultfilename), significantstring);
			}
		new BVD(debug,"connecting");	
		} while (connection == null);
		new BVD(debug,"connected");
		thecontrol=this;
		BVUtils.setParams(this,connection);
		new BVD(debug,"initialized UtilsParams");
		bvs=new BVStorage(this,connection);
		new BVD(debug,"initialized bvs");
		BVBookUse.init();
		gui=new BVGUI(this);
		new BVD(debug,"initialized gui");
		((BVCSVImporter)(BVUsecases.Datenimport.view)).init(gui);
		new BVD(debug,"initialized BVCSV");
		switchusecases = BVUsecases.getSelected2Usecases();
		new BVD(debug,"initialized switchusecases");
		scanner=new BVScanAdapter(this);
		new BVD(debug,"initialized SCANNER");
		BVGUI.selectView(BVUsecases.Datenimport);
		BVGUI.selectView(BVUsecases.Ausleihe);//default usecase
		new BVD(debug,"End of Constructor of BVControl");
		
		
		
		
			
	}
	public static BVControl getControl(){
		return thecontrol;
	}
	
	
	
	private void shutDown(){
		BVDataBase.CloseConnection(connection);
		new BVD(debug,"closing");
		// TODO: uncomment, implement
		// savePropertiesToXML(file, props, comment);
		System.exit(0);
	}

	public void run(){
		
	}
	
	
	
	/**
	 * Central Dispatcher of interaction (i.e. Eans or Clicks)
	 * Gets the present topView,
	 * changes it according to switch-matrix created in BVUsecases.Selected2Usecase,
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
		new BVD(debug,activeview.getName()+"->"+id+"->"+switchusecases.get(id));
		new BVD(debug,activeview.getName()+": "+BVUsecases.getUsecase(activeview).ConsumedEvents);
		if (!(BVUsecases.getUsecase(activeview).ConsumedEvents==null)){
			if (!BVUsecases.getUsecase(activeview).ConsumedEvents.contains(id)){
				//activeview.toBackground();
				BVGUI.selectView(switchusecases.get(id));
			}
		}
		BVSelectedEvent.makeEvent(source, id, ean, wildcards);
		
	}

	public void actionPerformed(ActionEvent e) {
		shutDown();
		
		
	}
	public static void log(String string){
		thecontrol.gui.lp.append(string+"\n");
	}
	/** 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 * 
	 * de-selected view retires to Background
	 */
	public void stateChanged(ChangeEvent e) {
		
		if (viewontop!=null & !BVGUI.isSelectedView(viewontop)){
			viewontop.toBackground();
			new BVD(true,viewontop.getName()+"<>"+BVGUI.getSelectedView().getName());
		}
		viewontop=BVGUI.getSelectedView();
		viewontop.toFront();
		
	}
}
