/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.scanner.Scan1;

/**
 * @author notkers
 *
 */
public class BVScanAdapter {
	
	BVScanAdapter(BVControl control){
		initScanner();
		
	}
	
	public void eanScanned(String eanstring){
		// Ean.checkEan(new Ean(eanstring));
		BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.ISBNSelected, new Ean(eanstring));	
	}
	
	private boolean initScanner(){
		if (Scan1.getScan1(this)==null){
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
