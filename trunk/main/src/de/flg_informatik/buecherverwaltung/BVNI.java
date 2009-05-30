package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BVNI extends Throwable{
	String who="";
	int debuglevel=1;
	BVNI(){
	
		if (debuglevel>0){
		who="Stacktrace:";	
		
			for (int i=0; i< Math.min(this.getStackTrace().length, debuglevel);i++){
				who = who+"\n"+this.getStackTrace()[i].toString();
			}
		}
		new Error("Not Implemented yet \n please contact nurh@flg-informatik.de",who);
	}
	

	class Error extends JDialog{
			Error(String text,String who){
				
				
				JOptionPane.showMessageDialog(BVControl.getControl().gui,text+who,"FLGBV "+BVControl.getControl().version.toString()+"",JOptionPane.ERROR_MESSAGE);
				
			}
			
	}		
}
	


