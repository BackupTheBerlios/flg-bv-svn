package de.flg_informatik.utils;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FLGUE extends Throwable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static boolean debug=false;
	String who="";
	int debuglevel=1;
	FLGUE(){
		if (debug){
			this.printStackTrace();
		}
	
		if (debuglevel>0){
		who="\nStacktrace:";	
		
			for (int i=0; i< Math.min(this.getStackTrace().length, debuglevel);i++){
				who = who+"\n"+this.getStackTrace()[i].toString();
			}
		}
		new Error("Interner Fehler",who);
	}
	FLGUE(String text){
		if (debug){
			this.printStackTrace();
		}
		
		if (debuglevel>0){
		who="Stacktrace:";	
		
			for (int i=0; i< Math.min(this.getStackTrace().length, debuglevel);i++){
				who = who+"\n"+this.getStackTrace()[i].toString();
			}
		}
		new Error(text,who);
	}

	class Error extends JDialog{
			Error(String text,String who){
				
				
				JOptionPane.showMessageDialog((JFrame)null,text+who,"de.flg-informatik.utils "+new Version().toString()+"",JOptionPane.ERROR_MESSAGE);
				
			}
			
	}		
}
	

