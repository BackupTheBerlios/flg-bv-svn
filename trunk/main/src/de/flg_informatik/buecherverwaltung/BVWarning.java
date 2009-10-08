package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BVWarning extends Throwable  {
	
	public BVWarning(String s){
		
		new Warning(s);
		
		
	}
	class Warning extends JDialog{
		Warning(String text){
			JOptionPane.showMessageDialog(Control.getControl().gui,text,Control.getControl().version.toString(),JOptionPane.WARNING_MESSAGE);
			
		}
		
	}
	

}
