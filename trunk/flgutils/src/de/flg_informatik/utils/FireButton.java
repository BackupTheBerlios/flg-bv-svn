package de.flg_informatik.utils;

import java.awt.Button;

public class FireButton extends Button{
	/**
 * 
 */
private static final long serialVersionUID = 1L;
	public FireButton(String title){
		super(title);
		
	}
	java.awt.Button.AccessibleAWTButton cont = (java.awt.Button.AccessibleAWTButton)this.getAccessibleContext();
public	void fire(){
		cont.doAccessibleAction(0);
	}
}
