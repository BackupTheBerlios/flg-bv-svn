package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

public class BVBookBack extends BVView {

	/**
	 * Zunächst nur Rückgabe des Buches,
	 * Rückmeldung über Zustand,
	 * <auf Knopfdruck Abfrage wieviele Bücher dieses Booktypes 
	 * aus dieser Location noch fehlen>
	 * Schülerkonto auf separatem Tab ...  
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public BVBookBack(BVControl bvc,Connection connection){
		this.bvc=bvc;
	}
	
	
	private BVControl bvc;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemSelected(ListSelectionEvent e) {
		// No List, no Selection nurh
		
	}

	public void thingSelected(BVSelectedEvent e) {
		switch (e.getId()){
			case BookLeasedSelected:
				
				break;
			default:
				bvc.thingSelected(e);
		// TODO Auto-generated method stub
		}
		
	}

}
