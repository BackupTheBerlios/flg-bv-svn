package de.flg_informatik.buecherverwaltung;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.util.Properties;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.flg_informatik.Etikett.EtikettDruck;
import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.Version;

public class Control implements Runnable,ActionListener,ChangeListener {
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
	final String defaultfilenameett="main/buchverwaltungetikett.default.xml";
	final String propertyfilenameett="buchverwaltungetikett.xml";
	final String significantstringett=".BuchverwaltungEtikettV01";
	final private static boolean debug=true;
	private static Control thecontrol;
	private UCCase viewontop=null;
	FLGProperties app_settings_pane;
	Properties app_settings;
	Version version=new Version(new int[]{0,95},"09-10-25");
	UCUseCases.Selected2Usecases  switchusecases;
	MainGUI mainGUI;
	UStorage bvs;
	Connection connection;
	ScanAdapter scanner;
	
	public Control(){
		app_settings_pane=new FLGProperties(app_settings,propertyfilename, new File(defaultfilename), significantstring);
		app_settings=app_settings_pane.getProperties();
		new Deb(debug,"fetched properties");
		if (!UDataBaseConnect.getDataBankDrivers(app_settings))
			javax.swing.JOptionPane.showMessageDialog(null, "'sun.jdbc.odbc.JdbcOdbcDriver' or 'com.mysql.jdbc.Driver' nicht gefunden.\nDas Programm wird jetzt beendet");
		
		do {
			
			connection=UDataBaseConnect.getConnection(app_settings);
			if (connection == null) {
				javax.swing.JOptionPane.showMessageDialog(null, "Konnte nicht mit Datenbank verbinden, bitte Extras überprüfen!");
				VPVDialog.showSettingsDialog(app_settings_pane);
			}
		new Deb(debug,"connecting");	
		} while (connection == null);
		new Deb(debug,"connected");
		thecontrol=this;
		USQLQuery.setParams(this,connection);
		new Deb(debug,"initialized UtilsParams");
		bvs=new UStorage(this,connection);
		new Deb(debug,"initialized bvs");
		OBUBookUse.init();
		new Deb(debug,"initialized OBUBuukUse");
		EtikettDruck.setPropertyFileStrings(propertyfilenameett, defaultfilenameett, significantstringett);
		new Deb(debug,"set EtikettDruck.setPropertyFileStrings");
		mainGUI=new MainGUI(this);
		new Deb(debug,"initialized mainGUI");
		((VBVCSVImporterView)(UCUseCases.Datenimport.view)).init(mainGUI);
		new Deb(debug,"initialized BVCSV");
		switchusecases = UCUseCases.getSelected2Usecases();
		new Deb(debug,"initialized switchusecases");
		scanner=new ScanAdapter(this);
		new Deb(debug,"initialized SCANNER");
		MainGUI.selectView(UCUseCases.Datenimport);
		MainGUI.selectView(UCUseCases.Ausleihe);//default usecase
		new Deb(debug,"End of Constructor of Control");
		
		
		
		
			
	}
	public static Control getControl(){
		return thecontrol;
	}
	
	
	
	private void shutDown(){
		// inform every usecase
		UCUseCases.toClose();
		// shutdown data connection
		UDataBaseConnect.CloseConnection(connection);
		new Deb(debug,"closing");
		System.exit(0);
	}

	public void run(){
		
	}
	
	
	
	/**
	 * Central Dispatcher of interaction (i.e. Eans or Clicks)
	 * Gets the present topView,
	 * changes it according to switch-matrix created in UCUseCases.Selected2Usecase,
	 * 	in case of switch calls the views toBackground() method
	 * 		which can call a modal-box (to save, commit ...)
	 * 
	 * At last makes the event by calling
	 * 	SelectedEvent.makeEvent(source, id, ean, wildcards);
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
		UCCase activeview=MainGUI.getSelectedView();
		new Deb(debug,activeview.getName()+"->"+id+"->"+switchusecases.get(id));
		new Deb(debug,activeview.getName()+": "+UCUseCases.getUsecase(activeview).ConsumedEvents);
		if (!(UCUseCases.getUsecase(activeview).ConsumedEvents==null)){
			if (!UCUseCases.getUsecase(activeview).ConsumedEvents.contains(id)){
				//activeview.toBackground();
				MainGUI.selectView(switchusecases.get(id));
			}
		}
		SelectedEvent.makeEvent(source, id, ean, wildcards);
		
	}

	public void actionPerformed(ActionEvent e) {
		shutDown();
		
		
	}
	public static void logln(String string){
		thecontrol.mainGUI.lp.append(string+"\n");
		thecontrol.mainGUI.validate();
	}
	public static void log(String string){
		thecontrol.mainGUI.lp.append(string);
		thecontrol.mainGUI.validate();
	}
	/** 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 * 
	 * de-selected view retires to Background
	 */
	public void stateChanged(ChangeEvent e) {
		
		if (viewontop!=null & !MainGUI.isSelectedView(viewontop)){
			viewontop.toBackground();
			new Deb(true,viewontop.getName()+"<>"+MainGUI.getSelectedView().getName());
		}
		viewontop=MainGUI.getSelectedView();
		viewontop.toFront();
		
	}
	
}
