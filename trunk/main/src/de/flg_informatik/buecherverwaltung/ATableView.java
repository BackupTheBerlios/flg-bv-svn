package de.flg_informatik.buecherverwaltung;

import java.awt.event.FocusEvent;
import java.awt.event.TextListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

public abstract class ATableView extends JPanel implements SelectedEventListener, UCCase{
	
	int getColumnwidth(int i){
		return 0;
	}
	boolean getColumnresizable(int i){
		return true;
	}

	
	public abstract void itemSelected(ListSelectionEvent e);
}
