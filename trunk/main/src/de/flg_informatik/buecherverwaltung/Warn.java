package de.flg_informatik.buecherverwaltung;

import javax.swing.JOptionPane;

public class Warn extends Mess implements BVConstants{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int messagetype=JOptionPane.WARNING_MESSAGE;
	Warn(){
		
		printOut("Warnung!\n", this,messagetype);
	}
	Warn(Throwable e){
		text="Es ist ein Fehler (\"runtime in JVM\") aufgetreten: "+e.getMessage();
		throwable=e;
		printOut(text, this,messagetype);
	}

	Warn(String text){
		printOut(text,this,messagetype);
	}
	Warn(String text,Throwable e){
		throwable = e;
		printOut(text,this,messagetype);
	}
	

	
}
	


