/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.scanner.ScanEmulator;
import de.flg_informatik.scanner.ScanFile;
import de.flg_informatik.scanner.ScanAdapter;
import de.flg_informatik.utils.FLGProperties;

/**
 * @author notkers
 *
 */
public class BVScanAdapter implements ScanAdapter, Runnable{
	private static BVControl control;
	private Ean ean;
	
	BVScanAdapter(BVControl control){
		BVScanAdapter.control=control;
		File file1=null; //TODO should be an enumeration of /dev/tty files, by now of Property scanner.scanfile
		if (control.app_settings.getProperty("scanner.typ").equals("emulator")){
			if (System.getProperty("os.name").equals("Linux")){
				file1=new File(new FLGProperties(null,"buchverwaltung.xml", new File("buchverwaltung.default.xml"), ".BuchverwaltungV01").getProperties().getProperty("scanner.emulator.filename_linux"));
			}
			new ScanEmulator(file1);
		}
		if (control.app_settings.getProperty("scanner.typ").equals("file")){
			if (System.getProperty("os.name").equals("Linux")){
				file1=new File(new FLGProperties(null,"buchverwaltung.xml", new File("buchverwaltung.default.xml"), ".BuchverwaltungV01").getProperties().getProperty("scanner.file.filename_linux"));
				
			}
		}
		debug(file1);
		initScanner(file1);
		
	}
	BVScanAdapter(Ean ean){
		this.ean=ean;
		debug("eanScanned");
		(new Thread(this)).start();
	}
	
	public synchronized void eanScanned(String eanstring){
		Ean ean=new Ean(eanstring);
		new BVScanAdapter(ean);
		
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


	public void run() {
		if (BVBookType.isISBN(ean)){ // Ean-Bookland -> ISBN
			
			if ( BVBookType.isKnownISBN(ean)){
				debug("isKownISBN()");
				control.newEvent(this, BVSelectedEvent.SelectedEventType.ISBNSelected, ean);
			}else{
				debug("isUnknownISBN()");
				control.newEvent(this, BVSelectedEvent.SelectedEventType.ISBNUnknownSelected, ean);
			}
			
		}else{
			if (BVBook.isBookEan(ean)){
				debug("isBookEan()");
				BVBook book = new BVBook(ean);
				debug("wasBookEan()");
				if (book.ISBN==BVBookType.ISBNNullEan.getEan()){
						debug("isISBNNullEan");
						control.newEvent(this, BVSelectedEvent.SelectedEventType.BookUnknownSelected, ean);
					}else{
						if (book.Location.equals(new BigInteger("1"))){
							debug("isFreeBook");
							control.newEvent(this, BVSelectedEvent.SelectedEventType.BookFreeSelected, ean);
						}else{
							debug("isLeasedBook");
							control.newEvent(this, BVSelectedEvent.SelectedEventType.BookLeasedSelected, ean);
							
							
						}
					}
				
				
				
			}else{
				debug("unclear ean");// Ean.checkEan(new Ean(eanstring));
			}
		}
	}
	static private void debug(Object obj){
		// System.out.println(BVScanAdapter.class+": "+ obj);
	}
}
