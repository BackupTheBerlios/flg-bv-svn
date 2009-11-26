package de.flg_informatik.buecherverwaltung;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Warn extends Throwable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String who="";
	int debuglevel=0;
	Warn(String text){
		
		if (debuglevel>0){
		who="Stacktrace:";	
		
			for (int i=0; i< Math.min(this.getStackTrace().length, debuglevel);i++){
				who = who+"\n"+this.getStackTrace()[i].toString();
			}
		}
		new warning(text,who);
	}

	@SuppressWarnings("serial")
	class warning extends JDialog{
			warning(String text,String who){
				
				
				JOptionPane.showMessageDialog(Control.getControl().mainGUI,text+who,"FLGBV "+Control.getControl().version.toString(),JOptionPane.WARNING_MESSAGE);
				
			}
			
	}		
}
	


