package de.flg_informatik.buecherverwaltung;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Notimpl extends Throwable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String who="";
	int debuglevel=1;
	Notimpl(){
	
		if (debuglevel>0){
		who="Stacktrace:";	
		
			for (int i=0; i< Math.min(this.getStackTrace().length, debuglevel);i++){
				who = who+"\n"+this.getStackTrace()[i].toString();
			}
		}
		new Error("Not Implemented yet \n please contact nurh@flg-informatik.de",who);
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
	


