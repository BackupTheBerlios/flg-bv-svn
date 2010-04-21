package de.flg_informatik.scanner;
import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

import de.flg_informatik.buecherverwaltung.Deb;
import de.flg_informatik.buecherverwaltung.ScanAdapter;
import de.flg_informatik.utils.FLGFrame;


public class ScanKeyStrokes extends Scanner implements AWTEventListener{
	final private static int debug=0;
	ScanAdapter adapter;
	static ScanKeyStrokes scanFile=null; 
	StringBuffer io=new StringBuffer();
	
	private ScanKeyStrokes (ScanAdapter adapter){
		this.adapter=adapter;
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(this, java.awt.AWTEvent.KEY_EVENT_MASK);
		scanFile=this;
		new Deb(debug,"new");
		
	}	
	
	void sendString(String io){
		if (adapter != null) {
			adapter.eanScanned(io);
		}else{
			new Deb(debug,io);
		}
		
		
	}
	
	public static ScanKeyStrokes getScanner(ScanAdapter adapter) {
		new Deb(debug,"try");
		if (scanFile==null){
			new ScanKeyStrokes(adapter);
			new Deb(debug,scanFile+" "+adapter);
		}	
		return scanFile;
		
	}
	public void closeScanner() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void eventDispatched(AWTEvent event) {
		new Deb(debug,event);
		char ch;
		if (event.getID()==KeyEvent.KEY_TYPED){
				switch (ch=((KeyEvent)(event)).getKeyChar()){
					case '\n':
						if (io.length()==13){
							sendString(io.toString());
						}
						io.setLength(0);
					break;
					default:
						if ((ch>= '0' ) & (ch <= '9' )){
							io.append(ch);
						}else{
							io.setLength(0);
						}
				}
		
		}
		
	}
	/**
	 *  Testing only
	 */
	public static void main(String[] args) {
		//getScan1(null, new File("C:\\temp\\temp.ll") );
		FLGFrame testframe=new FLGFrame();
		testframe.add(new JPanel());
	}
	
}
