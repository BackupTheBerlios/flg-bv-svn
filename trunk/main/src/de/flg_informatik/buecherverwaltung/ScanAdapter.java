/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.io.File;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.ean13.WrongCheckDigitException;
import de.flg_informatik.ean13.WrongLengthException;
import de.flg_informatik.scanner.IScanAdapter;
import de.flg_informatik.scanner.ScanEmulator;
import de.flg_informatik.scanner.ScanFile;
import de.flg_informatik.scanner.ScanKeyStrokes;
import de.flg_informatik.utils.FLGProperties;

/**
 * @author notkers
 * kind of janus-headed, polymorphistic constructor
 *  
 *
 */
public class ScanAdapter implements IScanAdapter, Runnable, BVConstants{
	private static Control control;
	private static Ean ISBNNullEan; 
	private Ean ean;
	private int debug=1;
	
	ScanAdapter(Control control){ // thats the true Constructor 
		ScanAdapter.control=control;
		try {
			ISBNNullEan = new Ean(ISBNNull12);
		} catch (Exception e) {
			throw new InternalError();
		}

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
		ScanKeyStrokes.getScanner(this);
	}
	ScanAdapter(Ean ean){ // Kind of kick starter for thread of a scan
		this.ean=ean;
		new Deb(debug,"eanScanned"+ean);
		(new Thread(this)).start();
	}
	
	public synchronized void eanScanned(String eanstring){
		
		try{
			Ean ean=new Ean(eanstring);
			new ScanAdapter(ean);
		}catch(WrongCheckDigitException e){
			new Warn(e.getMessage());
		}catch (Exception e) {
			new InternalError(e.getMessage());
		}
		
		
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
				if (book.ISBN==ISBNNullEan.getEan()){
						new Deb(debug,"isISBNNullEan");
						control.newEvent(this, SelectedEvent.SelectedEventType.BookUnknownSelected, ean);
					}else{
						if (book.isInStore()){
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
