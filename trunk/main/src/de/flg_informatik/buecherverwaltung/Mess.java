package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Mess extends Throwable{
	boolean debug=true;
	
	public Mess(String text){	
		JOptionPane.showMessageDialog(Control.getControl().mainGUI,text,"FLGBV "+Control.getControl().version.toString()+"",JOptionPane.INFORMATION_MESSAGE);
		new Deb(debug,text);
	}
	
}
	


