/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.io.File;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.scanner.ScanFile;
import de.flg_informatik.utils.FLGProperties;

/**
 * @author notkers
 *
 */
public class BVScanAdapter {
	enum types { // enumeration of ScannerInterfaces 
		emulator,
		file;
	}
		
	BVScanAdapter(BVControl control){
		File file1=null; //TODO should be an enumeration of /dev/tty files, by now of Property scanner.scanfile
		if (control.app_settings.getProperty("scanner.typ").equals("emulator")){
			if (System.getProperty("os.name").equals("Linux")){
				file1=new File(new FLGProperties(null,"buchverwaltung.xml", new File("buchverwaltung.default.xml"), ".BuchverwaltungV01").getProperties().getProperty("scanner.emulator.filename_linux"));
			}
		}
		initScanner(file1);
		
	}
	
	public void eanScanned(String eanstring){
		if (eanstring.startsWith("978")||eanstring.startsWith("979")){ // Ean-Bookland -> ISBN
			//if ( isInBooktypes(eanstring)){
				
			//}
			BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.ISBNSelected, new Ean(eanstring));
		}
		// Ean.checkEan(new Ean(eanstring));
			
	}
	
	/*
	 * TODO should find out whether Scanner is really connected to file
	 */
	
	private boolean initScanner(File file){
		if (ScanFile.getScan1(this, file)==null){
			return false;
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BVScanAdapter(null);

	}

}
