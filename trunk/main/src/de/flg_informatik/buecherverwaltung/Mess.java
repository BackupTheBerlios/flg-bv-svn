package de.flg_informatik.buecherverwaltung;

import java.awt.Component;

import javax.swing.JOptionPane;

public class Mess extends Throwable implements BVConstants{
		
	/**
	 * 
	 */
	String who="";
	String text="";
	String title="";
	Throwable throwable=null; // if detail is an error/exception
	int depth=Math.max(10, BVConstants.debug);
	StackTraceElement[] mystacktrace;
	private static final long serialVersionUID = 1L;
	Mess(){
				mystacktrace=this.getStackTrace();
	};
	public Mess(String text){
		printOut(text, null, JOptionPane.INFORMATION_MESSAGE);
	}
	public Mess(String text, int messagetype){
		printOut(text, null, messagetype);
	}
	public Mess(String text, int messagetype, Throwable e){
		throwable=e;
		printOut(text, this, messagetype);
	}
	public Mess(String text, int messagetype, Mess details){
		if (details!=null){
			this.mystacktrace=details.mystacktrace;
		}
		printOut(text, details, messagetype);
	}
	
		
		
	
	

	void printOut(String text, Mess details, int messagetype){
		
	/* here are locals */
		Component parent=null;
		depth=Math.max(10, BVConstants.debug);
		title="FLGBV";
		String version=" (no controller)";
		String hasgui=" (no GUI)";
		try { // what do we have
			version=Mess.version.toString()+"";
			parent=Control.getControl().mainGUI;
			hasgui=""; // all set up
		} catch (NullPointerException e) {
			// something was missing
		}finally{
			title=title+version+hasgui;
		}
	/* end of locals*/
		if ( details != null){ // there are details
			if (JOPane.showScrollableOptionDialog(parent, text
				, title, 0, messagetype,null, new String[] {"OK", "Details"} , "OK")==1){// want details
			
				text+="\n";
				if (throwable!=null){
					for (int i= 0;i< throwable.getStackTrace().length; i++){
						text=text+throwable.getStackTrace()[i]+"\n";
					}
				}else{
					for (int i=0; i< Math.min(mystacktrace.length, depth);i++){
							text = text+"\n"+mystacktrace[i].toString();
						}
					
				}
				
				new Mess(text,messagetype,null);
			
			}
		}else{
			JOPane.showScrollableOptionDialog(parent, text
					, title, 0, messagetype,null,new String[] {"OK"} , "OK");
		}
		finished();
	}
	
	void finished(){
		
	}
	
	
		
}		
	

	


