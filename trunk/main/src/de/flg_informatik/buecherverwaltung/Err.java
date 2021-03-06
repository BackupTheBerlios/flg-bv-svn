package de.flg_informatik.buecherverwaltung;

import javax.swing.JOptionPane;

public class Err extends Mess{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static final int messagetype=JOptionPane.ERROR_MESSAGE;
	Err(){
		printOut("Es ist ein interner Fehler aufgetreten!\n",this,messagetype);
		
	}
	Err(Throwable e){
		throwable=e;
		text=e.getLocalizedMessage();
		printOut(text,this,messagetype);
		
	}

	Err(String text){
		printOut(text,this,messagetype);
		
	}
	Err(String text,Throwable e){
		text="Fehlermeldung der JVM: "+e.getMessage();
		throwable=e;
		printOut(text,this,messagetype);
	}
	@Override
	void finished() {
		// TODO Auto-generated method stub
		super.finished();
		if (debug==0){
			System.exit(1);
		}
	}
	
	
	
}
	


