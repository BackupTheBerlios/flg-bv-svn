package de.flg_informatik.buecherverwaltung;

import javax.swing.JOptionPane;

public class Mess extends Throwable implements BVConstants{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Mess(String text){	
		JOptionPane.showMessageDialog(Control.getControl().mainGUI,text,"FLGBV "+Control.getControl().version.toString()+"",JOptionPane.INFORMATION_MESSAGE);
		new Deb(debug,text);
	}
	
}
	


