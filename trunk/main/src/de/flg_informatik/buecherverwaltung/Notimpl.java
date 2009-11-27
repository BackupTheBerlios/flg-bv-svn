package de.flg_informatik.buecherverwaltung;

import javax.swing.JOptionPane;

public class Notimpl extends Mess implements BVConstants{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int messagetype=JOptionPane.ERROR_MESSAGE;
	Notimpl(){
		printOut("Diese Funktion/Aktion ist noch nicht implementiert!\n",this,messagetype);
		
	}
}
	


