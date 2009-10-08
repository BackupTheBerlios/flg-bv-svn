package de.flg_informatik.buecherverwaltung;

import java.util.Vector;

import javax.swing.event.ListSelectionEvent;
interface UCCase extends  SelectedEventListener{
	
	
	/** 
	 * normally implemented by JPanel
	 * kind of multiple inherit 
	 */
	public String getName();
	
	/** 
	 * normally implemented by JPanel
	 * kind of multiple inherit 
	 */
	public void setName(String string);
	
	/**
	 * needed for UseCaseManagement
	 * @return 
	 */
	public Vector<SelectedEvent.SelectedEventType> getConsumedEvents();
	
	public void toFront();
	
	public void toBackground();
	
	public void toClose();
	
	public abstract void itemSelected(ListSelectionEvent e);
	
	}
