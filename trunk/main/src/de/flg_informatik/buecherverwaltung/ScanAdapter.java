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
import de.flg_informatik.scanner.IScanAdapter;
import de.flg_informatik.utils.FLGProperties;

/**
 * @author notkers
 *
 */
public class ScanAdapter implements IScanAdapter, Runnable{
	private static boolean debug=true;
	private static Control control;
	private Ean ean;
	
	ScanAdapter(Control control){
		ScanAdapter.control=control;
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
		new Deb(debug,file1);
		initScanner(file1);
		
	}
	ScanAdapter(Ean ean){
		this.ean=ean;
		new Deb(debug,"eanScanned");
		(new Thread(this)).start();
	}
	
	public synchronized void eanScanned(String eanstring){
		Ean ean=new Ean(eanstring);
		new ScanAdapter(ean);
		
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
		// TODO may be tested against getSubTypes
		switch (EEANType.getType(ean)){
		case ISBN: // Ean-Bookland -> ISBN
			
			if ( OBTBookType.isKnownISBN(ean)){
				new Deb(debug,"isKownISBN()");
				control.newEvent(this, SelectedEvent.SelectedEventType.ISBNSelected, ean);
			}else{
				new Deb(debug,"isUnknownISBN()");
				control.newEvent(this, SelectedEvent.SelectedEventType.ISBNUnknownSelected, ean);
			}
			break;
		case Book:
				new Deb(debug,"isBookEan()");
				OBook book = new OBook(ean);
				new Deb(debug,"wasBookEan()");
				if (book.ISBN==OBTBookType.ISBNNullEan.getEan()){
						new Deb(debug,"isISBNNullEan");
						control.newEvent(this, SelectedEvent.SelectedEventType.BookUnknownSelected, ean);
					}else{
						new Deb(book.Location);
						if (book.Location.equals(new BigInteger("1"))){
							new Deb(debug,"isFreeBook");
							control.newEvent(this, SelectedEvent.SelectedEventType.BookFreeSelected, ean);
						}else{
							new Deb(debug,"isLeasedBook");
							control.newEvent(this, SelectedEvent.SelectedEventType.BookLeasedSelected, ean);
							
							
						}
					}
				break;
		case BVClass:
			// TODO
			break;
		
		}	
		
	}
	
}
