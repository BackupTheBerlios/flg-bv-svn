package de.flg_informatik.scanner;
import java.io.FileReader;

import de.flg_informatik.buecherverwaltung.BVScanAdapter;

public class Scan1 implements Runnable{
	FileReader rf;
	BVScanAdapter adapter;
	static Scan1 scan1=null; 
	
	private Scan1 (BVScanAdapter adapter){
		this.adapter=adapter;
		scan1=this;
		debug("new");
		try{
			rf= new FileReader("/dev/ttyUSB0");
		
		Thread thread=new Thread(this);
		thread.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/*public static void main(String[] args) {
		new Scan1();
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
		debug(io);
		if (adapter != null) {
			adapter.eanScanned(io);
		}else{
			debug(io);
		}
		
		
	}
	static private void debug(Object obj){
		System.out.println(Scan1.class+": "+ obj);
	
	}
	public static void main(String[] args) {
		getScan1(null);
	}
	public static Scan1 getScan1(BVScanAdapter adapter) {
		debug("try");
		if (scan1==null){
			new Scan1(adapter);
		}	
		return scan1;
		
	}
}
