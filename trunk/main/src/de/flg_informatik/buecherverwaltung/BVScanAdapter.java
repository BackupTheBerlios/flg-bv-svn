/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.io.File;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.scanner.ScanFile;

/**
 * @author notkers
 *
 */
public class BVScanAdapter {
	File file1=new File("C:\\temp\\temp.ll"); //TODO should be an enumeration of /dev/tty files
		
	BVScanAdapter(BVControl control){
		initScanner();
		
	}
	
	public void eanScanned(String eanstring){
		if (eanstring.startsWith("978")||eanstring.startsWith("979")){ // Ean-Bookland -> ISBN
			//if ( isInBooktypes(eanstring)){
				
			//}
			BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.ISBNSelected, new Ean(eanstring));
		}
		// Ean.checkEan(new Ean(eanstring));
			
	}
	
	private boolean initScanner(){
		if (ScanFile.getScan1(this, file1)==null){
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
