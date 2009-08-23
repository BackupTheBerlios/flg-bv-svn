package de.flg_informatik.buecherverwaltung;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BVD extends Throwable{
	String who="";
	int mydebuglevel=1;

	public BVD(boolean debug, Object o){
		String text;
		if (o!=null){
			if (debug){
				text=o.toString();
				System.out.println(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
				if (mydebuglevel>0){
					for (int i=0; i< Math.min(this.getStackTrace().length, mydebuglevel);i++){
						who = who+"\n"+this.getStackTrace()[i].toString();
					}
					System.out.println(who);
				}
			}
		}else{
			if (debug){
				text="null-Object";
				System.out.println(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
				if (mydebuglevel>0){
					for (int i=0; i< Math.min(this.getStackTrace().length, mydebuglevel);i++){
						who = who+"\n"+this.getStackTrace()[i].toString();
					}
					System.out.println(who);
				}
				
			}
		}
	
	}
	public BVD(int debuglevel, Object o){
		String text;
		mydebuglevel=debuglevel;
		if (o!=null){
		
			text=o.toString();
			System.out.println(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
			if (mydebuglevel>0){
				
			
				for (int i=0; i< Math.min(this.getStackTrace().length, mydebuglevel);i++){
					who = who+"\n"+this.getStackTrace()[i].toString();
				}
				System.out.println(who);
			}
		
		}else{
		
			text="null-Object";
			System.out.println(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
			if (mydebuglevel>0){
				for (int i=0; i< Math.min(this.getStackTrace().length, mydebuglevel);i++){
					who = who+"\n"+this.getStackTrace()[i].toString();
				}
				System.out.println(who);
			}
			
		}
	
	}
}

	


