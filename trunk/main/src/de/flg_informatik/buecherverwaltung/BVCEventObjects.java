package de.flg_informatik.buecherverwaltung;

import java.awt.Button;
import java.awt.event.ActionListener;
import de.flg_informatik.utils.FireButton;

public enum BVCEventObjects{
		stop (new FireButton("Beenden")),
		cancel (new FireButton("Abbrechen"));
		;
	public Object ev;
	public static ActionListener listener=null;
	
	private BVCEventObjects(Object ev) {
		this.ev = ev;
	}
	public void register(){
		if (Button.class.isAssignableFrom(this.ev.getClass())){
			((Button)(this.ev)).addActionListener(listener);
		}
	}
}
