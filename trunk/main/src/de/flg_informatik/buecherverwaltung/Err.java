package de.flg_informatik.buecherverwaltung;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Err extends Throwable implements BVConstants{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String who="";
	
	Err(){
		if (debug>0){
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
	Err(String text){
		if (debug>0){
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
			/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

			Error(String text,String who){
				
				
				JOptionPane.showMessageDialog(Control.getControl().mainGUI,text+who,"FLGBV "+Control.getControl().version.toString()+"",JOptionPane.ERROR_MESSAGE);
				
			}
			
	}		
}
	


