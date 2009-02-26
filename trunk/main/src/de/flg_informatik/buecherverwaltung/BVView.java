package de.flg_informatik.buecherverwaltung;

import java.awt.event.TextListener;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

public abstract class BVView extends JPanel implements BVSelectedEventListener{
	int index; 
	BVView(int index){
		this.index=index;
	}
	int getColumnwidth(int i){
		return 0;
	}
	boolean getColumnresizable(int i){
		return true;
	}
	public abstract void itemSelected(ListSelectionEvent e);
}
