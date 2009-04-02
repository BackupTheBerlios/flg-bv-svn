package de.flg_informatik.buecherverwaltung;

import java.io.File;
import java.sql.Connection;
import java.util.Properties;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.Version;

public class BVControl implements Runnable {
	/**
	 * controller for flg-bv,
	 * get properties, start all components in order
	 * 
	 * doesn't implement BVSelectedEventListener but
	 * has a static(!) thingSelected(BVSelectedEvent e)
	 * to make usecase switch if a selected component (on top)
	 * can't consume a (Scanner-)Event
	 * 
	 * 
	 * 
	 */
	private final String defaultfilename="main/buchverwaltung.default.xml";
	private final String significantstring=".BuchverwaltungV01";
	private static BVControl thecontrol;
	Properties app_settings;
	Version version=new Version(new int[]{0,5},"09-02-02");
	BVGUI gui;
	BVStorage bvs;
	Connection connection;
	BVScanAdapter scanner;
	
	public BVControl(){
		app_settings=new FLGProperties(app_settings,"buchverwaltung.xml", new File(defaultfilename), significantstring).getProperties();
		debug(app_settings);
		// BVCEventObjects.listener=this;
		if (!BVDataBase.getDataBankDrivers(app_settings))
			javax.swing.JOptionPane.showMessageDialog(null, "'sun.jdbc.odbc.JdbcOdbcDriver' or 'com.mysql.jdbc.Driver' nicht gefunden.\nDas Programm wird jetzt beendet");
		
		do {
			
			connection=BVDataBase.getConnection(app_settings);
			if (connection == null) {
				javax.swing.JOptionPane.showMessageDialog(null, "Konnte nicht mit Datenbank verbinden, bitte Einstellungen überprüfen!");
				PropertiesDialog.showSettingsDialog(app_settings,"buchverwaltung.xml", new File(defaultfilename), significantstring);
			}
		} while (connection == null);
		BVControl.thecontrol=this;
		BVUtils.setParams(this,connection);
		bvs=new BVStorage(this,connection);
		gui=new BVGUI(this);
		scanner=new BVScanAdapter(this);
			
	}
	public static BVControl getControl(){
		return thecontrol;
	}
	
	
	
	private void shutDown(){
		BVDataBase.CloseConnection(connection);
		// TODO: uncomment, implement
		// savePropertiesToXML(file, props, comment);
		System.exit(0);
	}

	public void run(){
		
	}
	
	static private void debug(Object obj){
		//System.out.println(BVControl.class+": "+ obj);
	}
	
	/**
	 * @param BVSelectetEvent: forwarded by selected (on top) view which
	 * doesn't know what to do with that particular BVSelectedEvent.
	 * 
	 * typically triggered by view's BVSelectedEventListener: 
	 * 		switch(e.getId()) 
	 * 			...default: if (BVGUI.isSelected(this)){
	 * 							//maybe some cleaning up ...
	 * 							BVControl.thingSelected(e);
	 * 						}
	 * 
	 * 
	 */

	public static void thingSelected(BVSelectedEvent e) {
		switch (e.getId()){
			case ISBNUnknownSelected:
				BVGUI.selectView(BVUsecases.Buchtypen);
				break;
			case BookLeasedSelected:
				BVGUI.selectView(BVUsecases.StapelRückgabe);
				break;	
		}
	}
}
