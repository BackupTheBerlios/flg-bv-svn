package de.flg_informatik.utils.templates;

import javax.swing.JOptionPane;

import de.flg_informatik.fehlzeiten.Constants;

public class Warn extends Mess implements Constants{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int messagetype=JOptionPane.WARNING_MESSAGE;
	public Warn(){
		
		printOut("Warnung!\n", this,messagetype);
	}
	public Warn(Throwable e){
		text="Es ist ein Fehler (\"runtime in JVM\") aufgetreten: "+e.getMessage();
		throwable=e;
		printOut(text, this,messagetype);
	}

	public Warn(String text){
		printOut(text,this,messagetype);
	}
	public Warn(String text,Throwable e){
		throwable = e;
		printOut(text,this,messagetype);
	}
	

	
}
	


