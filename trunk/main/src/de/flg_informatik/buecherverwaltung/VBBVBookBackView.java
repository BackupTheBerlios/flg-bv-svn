package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.ean13.Ean;

public class VBBVBookBackView extends JPanel implements UCCase, ActionListener {
	

	/**
	 * Zun�chst nur R�ckgabe des Buches,
	 * R�ckmeldung �er Zustand,
	 * <auf Knopfdruck Abfrage wieviele B�cher dieses Booktypes 
	 * aus dieser Location noch fehlen>
	 * Sch�lerkonto auf separatem Tab ...  
	 * 
	 * 
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private static boolean debug=true;
	private OBook lastbook=null;
	private JPBookPresenter np;
	public VBBVBookBackView(){
		this.setLayout(new BorderLayout());
		add(np=new JPBookPresenter(lastbook,new JPConditionSwitcher(this,0),this),BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
		public void actionPerformed(ActionEvent e) {
			new Deb(e.getActionCommand());
			
			if (lastbook!=null){
				for (int i=0; i<6; i++ ){
					if(e.getActionCommand().equals((i+1)+"")){
						lastbook.Scoring_of_condition=i+1;
						np.publish(lastbook);
						
					}
				}
			}
			if (e.getActionCommand().equals("end")){
					toBackground(); 
					np.publish(lastbook);
			}
			if (e.getActionCommand().equals("cancel")){
				Control.log("ABBRUCH der R�ckgabe: (" + lastbook.ID + ", " + OBTBookType.getTitle(new Ean(lastbook.ISBN))+", "+lastbook.Scoring_of_condition+")" );
				lastbook=null;
				np.publish(lastbook);
			}
			
		}

		public boolean postinit(){
		return true;
	}
	public void itemSelected(ListSelectionEvent e) {
		// No List, no Selection, nurh
		
	}
	
	public synchronized void thingSelected(SelectedEvent e) {
		switch (e.getId()){
		
			case BookLeasedSelected:// we stay on top
				if (lastbook != null){
					if (OBook.makeBookID(e.getEan()).equals(lastbook.ID)){
						lastbook.incCondition();
					}else{
						commit(lastbook);
						lastbook = new OBook(e.getEan());
					}	
				}else{
					lastbook = new OBook(e.getEan());
					
				}
				np.publish(lastbook);
				break;
			default: // do nothing
			}
			
		
	}
	
	private synchronized void commit(OBook book){
		new Deb (debug,"commit");
		if (USQLQuery.doUpdate("UPDATE Books SET Location=1, " +
				"Scoring_of_Condition="+book.Scoring_of_condition+" WHERE ID="+book.ID)==1){
				
			Control.log("R�ckgabe von Buch: " + book.ID + " " + OBTBookType.getTitle(new Ean(book.ISBN))+" Zustand: "+book.Scoring_of_condition );
		}
	
	}

	public void toBackground() {
		if (lastbook!=null){ // we have been in business
			commit(lastbook); // close Transaction
			lastbook=null;
		}else{
			// nothing to do
		}
		
		
	}
	public void toClose() {
		// TODO Auto-generated method stub
		
	}
	public Vector<SelectedEvent.SelectedEventType> getConsumedEvents() {
		
		// TODO Auto-generated method stub
		return (new Vector<SelectedEvent.SelectedEventType>(){{
			add(SelectedEvent.SelectedEventType.BookLeasedSelected);
		}});
	}
	public void toFront() {
		np.publish(null);
		// TODO Auto-generated method stub
		
	}
	
}
