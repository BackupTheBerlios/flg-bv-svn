package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BVM extends Throwable{
	boolean debug=true;
	
	public BVM(String text){	
		JOptionPane.showMessageDialog(BVControl.getControl().gui,text,"FLGBV "+BVControl.getControl().version.toString()+"",JOptionPane.INFORMATION_MESSAGE);
		new BVD(debug,text);
	}
	
}
	


