package de.flg_informatik.buecherverwaltung;

import java.awt.event.TextListener;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

public abstract class BVView extends JPanel implements BVSelectedEventListener{
	int[] columnwidth;
	boolean[] columnresizable; 
	public abstract void itemSelected(ListSelectionEvent e);
}
