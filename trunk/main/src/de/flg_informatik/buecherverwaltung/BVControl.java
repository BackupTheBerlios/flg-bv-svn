package de.flg_informatik.buecherverwaltung;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

import de.flg_informatik.utils.Version;

public class BVControl implements Runnable {
	private final String defaultfilename="buchverwaltung.default.xml";
	private final String significantstring=".BuchverwaltungV01";
	
	Version version=new Version(new int[]{0,5},"09-02-02");
	BVGUI gui;
	BVStorage bvs;
	Connection connection;
	BVScanAdapter scanner;

	public BVControl(){
		// TODO: save properties after prog terminated
		java.util.Properties app_settings = loadPropertiesFromXML("buchverwaltung.xml", new File(defaultfilename), significantstring);
		
		// BVCEventObjects.listener=this;
		if (!BVDataBase.getDataBankDrivers(app_settings))
			javax.swing.JOptionPane.showMessageDialog(null, "'sun.jdbc.odbc.JdbcOdbcDriver' or 'com.mysql.jdbc.Driver' nicht gefunden.\nDas Programm wird jetzt beendet");
		
		do {
			connection=BVDataBase.getConnection(app_settings);
			if (connection == null) {
				javax.swing.JOptionPane.showMessageDialog(null, "Konnte nicht mit Datenbank verbinden, bitte Einstellungen überprüfen!");
				PropertiesDialog.showSettingsDialog(app_settings);
			}
		} while (connection == null);

		BVUtils.setParams(this,connection);
		bvs=new BVStorage(this,connection);
		gui=new BVGUI(this);
		scanner=new BVScanAdapter(this);
			
	}
	
	private Properties loadPropertiesFromXML(String infilename, File defaultfile, String significantkey){
		switch(de.flg_informatik.utils.shell.OSShell.getOS()){
			case linux:
				infilename=System.getProperty("user.home")+System.getProperty("file.separator")+"."+infilename;
				break;
			default:
				// infilename=infilename;
		}
		File infile = new File(infilename);
		File getfile = new File(infilename);
		Properties props = new Properties();
		while(!props.containsKey(significantkey)){
			try{FileInputStream fis =new FileInputStream(getfile);
				props.loadFromXML(fis);
			}catch(java.io.FileNotFoundException e){
				javax.swing.JOptionPane.showMessageDialog(null, "Eine Einstellungsdatei wurde nicht in ihrem Benutzerverzeichnis gefunden. Es werden die Standarteinstellungen geladen.");
				/* switch(DecisionBox.getLabel(this,"Einstellungsdatei",
							new String[]{"In Ihrem Benutzerverzeichnis gibt es keine",
								"Einstellungsdatei: (\""+infilename+"\")",
								"Was wollen Sie: Die Einstellungsdatei..."},
							new String[]{"... aus default neu erstellen?","...suchen?"})){
						case 1:{ */
							getfile=defaultfile;
							debug("loading defaults");
							break;
						/* }
						case 2:{
							JFileChooser jfc = new javax.swing.JFileChooser();
							if(jfc.showOpenDialog(this)==javax.swing.JFileChooser.APPROVE_OPTION){
								getfile=jfc.getSelectedFile();
								infile=getfile;
							}
							break;
						}
					} */
			}catch(java.util.InvalidPropertiesFormatException e){
				debug("Format of props not valid!");
			}
			catch(java.io.IOException e){
				e.printStackTrace();
			}
			File outfile=new File(infile.getAbsolutePath());
			defaultfile=infile;
			
			
		}
		
		return props;
	}
	
	private boolean savePropertiesToXML(File file, Properties props, String comment){
		if (file!=null){
			try{
				java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
				props.storeToXML(fos , comment);
				fos.close();
				return true;
			}catch(Exception e){
				e.printStackTrace(); // debug("exception");
			}
			debug(file);
			
			return false;
		}	
		return false;
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
	 */
}
