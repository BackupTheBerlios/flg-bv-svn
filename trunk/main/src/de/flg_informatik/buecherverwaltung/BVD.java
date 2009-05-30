package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BVD extends Throwable{
	String who="";
	int mydebuglevel=1;
	/*
	 * public BVD(String text){
		this(true,text);
	}
	*/
	private BVD(boolean debug, String text){	
		if (debug){
			
			
			if (mydebuglevel>0){
				System.out.println(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
			
				for (int i=0; i< Math.min(this.getStackTrace().length, mydebuglevel);i++){
					who = who+"\n"+this.getStackTrace()[i].toString();
				}
			}
		}

	
	}
	/*public BVD(Object o){
		this(o.toString());
	
	}
	*/
	public BVD(boolean debug, Object o){
		String text;
		if (o!=null){
			if (debug){
				text=o.toString();
				
				if (mydebuglevel>0){
					System.out.println(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
				
					for (int i=0; i< Math.min(this.getStackTrace().length, mydebuglevel);i++){
						who = who+"\n"+this.getStackTrace()[i].toString();
					}
				}
			}
		}else{
			if (debug){
				text="null-Object";
				
				if (mydebuglevel>0){
					System.out.println(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
				
					for (int i=0; i< Math.min(this.getStackTrace().length, mydebuglevel);i++){
						who = who+"\n"+this.getStackTrace()[i].toString();
					}
				}
			}
		}
	
	}
}
	


