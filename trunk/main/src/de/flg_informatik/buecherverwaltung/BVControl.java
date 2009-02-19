package de.flg_informatik.buecherverwaltung;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.Version;

public class BVControl implements Runnable,BVSelectedEventListener {
	private final String defaultfilename="buchverwaltung.default.xml";
	private final String significantstring=".BuchverwaltungV01";
	Properties app_settings;
	Version version=new Version(new int[]{0,5},"09-02-02");
	BVGUI gui;
	BVStorage bvs;
	Connection connection;
	BVScanAdapter scanner;

	public BVControl(){
		// TODO: save properties after prog terminated
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

		BVUtils.setParams(this,connection);
		bvs=new BVStorage(this,connection);
		gui=new BVGUI(this);
		scanner=new BVScanAdapter(this);
			
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
		System.out.println(BVControl.class+": "+ obj);
	}
	
	/**
	 * @param args
	 * BVSelectetEvent forwarded by any view which doesn't know what to do
	 * with that particular BVSelectedEvent,
	 * should change tab on top and forward event to it.
	 */

	public void thingSelected(BVSelectedEvent e) {
		// TODO Auto-generated method stub
		
	}
}
