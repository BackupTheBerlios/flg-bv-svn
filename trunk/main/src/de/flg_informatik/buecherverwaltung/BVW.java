package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BVW extends Throwable{
	String who="";
	int debuglevel=0;
	BVW(String text){
		
		if (debuglevel>0){
		who="Stacktrace:";	
		
			for (int i=0; i< Math.min(this.getStackTrace().length, debuglevel);i++){
				who = who+"\n"+this.getStackTrace()[i].toString();
			}
		}
		new warning(text,who);
	}

	class warning extends JDialog{
			warning(String text,String who){
				
				
				JOptionPane.showMessageDialog(BVControl.getControl().gui,text+who,"FLGBV "+BVControl.getControl().version.toString(),JOptionPane.WARNING_MESSAGE);
				
			}
			
	}		
}
	


