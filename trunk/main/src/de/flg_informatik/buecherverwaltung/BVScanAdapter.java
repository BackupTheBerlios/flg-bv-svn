/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.io.File;
import java.math.BigInteger;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.scanner.ScanFile;
import de.flg_informatik.scanner.ScanAdapter;
import de.flg_informatik.utils.FLGProperties;

/**
 * @author notkers
 *
 */
public class BVScanAdapter implements ScanAdapter{
	
	BVScanAdapter(BVControl control){
		File file1=null; //TODO should be an enumeration of /dev/tty files, by now of Property scanner.scanfile
		if (control.app_settings.getProperty("scanner.typ").equals("emulator")){
			if (System.getProperty("os.name").equals("Linux")){
				file1=new File(new FLGProperties(null,"buchverwaltung.xml", new File("buchverwaltung.default.xml"), ".BuchverwaltungV01").getProperties().getProperty("scanner.emulator.filename_linux"));
			}
		}
		if (control.app_settings.getProperty("scanner.typ").equals("file")){
			if (System.getProperty("os.name").equals("Linux")){
				file1=new File(new FLGProperties(null,"buchverwaltung.xml", new File("buchverwaltung.default.xml"), ".BuchverwaltungV01").getProperties().getProperty("scanner.file.filename_linux"));
			}
		}
		debug(file1);
		initScanner(file1);
		
	}
	
	public synchronized void eanScanned(String eanstring){
		Ean ean=new Ean(eanstring);
		if (BVBookType.isISBN(ean)){ // Ean-Bookland -> ISBN
			debug("isISBN()");
			if ( BVBookType.isKnownISBN(ean)){
				BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.ISBNSelected, ean);
			}else{
				BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.ISBNUnknownSelected, ean);
			}
			
		}
		if (BVBook.isBookEan(ean)){
			//debug("book: ;" + ean);
			BVBook book= new BVBook(ean);
			//debug("book");
				if (book.ISBN==BVBookType.ISBNNullEan.getEan()){
					BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.BookUnknownSelected, ean);
				}else{
					if (book.Location.equals(new BigInteger("1"))){
						BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.BookFreeSelected, ean);
					}else{
						
							BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.BookLeasedSelected, ean);
						
						
					}
				}
			
			
			
		}// Ean.checkEan(new Ean(eanstring));
			
	}
	
	/*
	 * TODO should find out whether Scanner is really connected to file
	 */
	
	private boolean initScanner(File file){
		if (ScanFile.getScanner(this, file)==null){
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
	static private void debug(Object obj){
		System.out.println(BVScanAdapter.class+": "+ obj);
	}

}
