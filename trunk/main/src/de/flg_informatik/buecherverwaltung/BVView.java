package de.flg_informatik.buecherverwaltung;

import java.awt.event.FocusEvent;
import java.awt.event.TextListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

public abstract class BVView extends JPanel implements BVSelectedEventListener{
	java.util.Vector <BVSelectedEvent.SelectedEventType> ConsumedEvents=new java.util.Vector <BVSelectedEvent.SelectedEventType>();
	
	int getIndex(){
		return BVUsecases.valueOf(this.getName()).ordinal();
	}
	int getColumnwidth(int i){
		return 0;
	}
	boolean getColumnresizable(int i){
		return true;
	}
	
	abstract void toBackground();
	
	void toClose(){
		
	}
	public abstract void itemSelected(ListSelectionEvent e);
}
