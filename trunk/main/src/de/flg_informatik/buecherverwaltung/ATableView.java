package de.flg_informatik.buecherverwaltung;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

public abstract class ATableView extends JPanel implements SelectedEventListener, UCCase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3403823097446423001L;


	int getColumnwidth(int i){
		return 0;
	}
	boolean getColumnresizable(int i){
		return true;
	}

	
	public abstract void itemSelected(ListSelectionEvent e);
}
