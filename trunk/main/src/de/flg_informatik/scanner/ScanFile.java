package de.flg_informatik.scanner;
import java.io.File;
import java.io.FileReader;

import de.flg_informatik.buecherverwaltung.BVD;
import de.flg_informatik.buecherverwaltung.BVScanAdapter;


public class ScanFile extends Scanner implements Runnable{
	private static boolean debug=false;
	public FileReader rf=null;
	BVScanAdapter adapter;
	static ScanFile scanFile=null; 
	
	private ScanFile (BVScanAdapter adapter, File file){
		this.adapter=adapter;
		scanFile=this;
		new BVD(debug,"new");
		try{
			rf= new FileReader(file);
		
		Thread thread=new Thread(this);
		thread.start();
		}catch(Exception e){
			new BVD(debug,"No File: "+file);
			scanFile=null;
			//e.printStackTrace();
		}
		
	}
	/*public static void main(String[] args) {
		new ScanFile();
		/*Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		System.out.println(portList.nextElement().toString());
		gnu.io.RXTXCommDriver io = new gnu.io.RXTXCommDriver();
		io.initialize();
		
		
		
	}*/
	public void run() {
		char ch;
		StringBuffer io=new StringBuffer();
		
		try{
		while (true){
			while(!rf.ready()){
				Thread.sleep(10);
			}
			ch = (char)(rf.read());
			if (ch == '\n'){
				if (io.length()==13){
					sendString(io.toString());
				}
				io.setLength(0);
			}else{
				io.append(ch);
			}
		}
		}catch(Exception ioe){
			ioe.printStackTrace();
		}
	}
	void sendString(String io){
		// new BVD(debug,io);
		if (adapter != null) {
			adapter.eanScanned(io);
		}else{
			new BVD(debug,io);
		}
		
		
	}
	
	/**
	 *  Testing only
	 */
	public static void main(String[] args) {
		//getScan1(null, new File("C:\\temp\\temp.ll") );
	getScanner(null, new File("/dev/ttyUSB0"));
	}
	public static Scanner getScanner(BVScanAdapter adapter, Object file) {
		new BVD(debug,"try");
		if (scanFile==null){
			new ScanFile(adapter, (File)file);
			new BVD(debug,scanFile);
		}	
		return scanFile;
		
	}
	public void closeScanner() {
		// TODO Auto-generated method stub
		
	}
	
}
