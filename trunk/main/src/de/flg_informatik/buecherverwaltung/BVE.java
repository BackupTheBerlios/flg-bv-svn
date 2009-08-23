package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BVE extends Throwable{
	private final static boolean debug=false;
	String who="";
	int debuglevel=17;
	BVE(){
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
	BVE(String text){
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
				
				
				JOptionPane.showMessageDialog(BVControl.getControl().gui,text+who,"FLGBV "+BVControl.getControl().version.toString()+"",JOptionPane.ERROR_MESSAGE);
				
			}
			
	}		
}
	


