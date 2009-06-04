package de.flg_informatik.buecherverwaltung;

import java.awt.event.FocusEvent;
import java.awt.event.TextListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

interface BVView extends  BVSelectedEventListener{
	
	
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
	public Vector<BVSelectedEvent.SelectedEventType> getConsumedEvents();
	
	public void toFront();
	
	public void toBackground();
	
	public void toClose();
	
	public abstract void itemSelected(ListSelectionEvent e);
	
	}
