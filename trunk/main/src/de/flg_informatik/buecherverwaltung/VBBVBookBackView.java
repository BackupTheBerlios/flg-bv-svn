package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.ean13.Ean;

public class VBBVBookBackView extends JPanel implements UCCase, ActionListener, BVConstants {
	

	/**
	 * Zunächst nur Rückgabe des Buches,
	 * Rückmeldung üer Zustand,
	 * <auf Knopfdruck Abfrage wieviele Bücher dieses Booktypes 
	 * aus dieser Location noch fehlen>
	 * Schülerkonto auf separatem Tab ...  
	 * 
	 * 
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private OBook lastbook=null;
	private OBook book=null;
	private JPBookPresenter np;
	public VBBVBookBackView(){
		this.setLayout(new BorderLayout());
		add(np=new JPBookPresenter(lastbook,new JPConditionSwitcher(this,0),this),BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
		public void actionPerformed(ActionEvent e) {
	
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
				if (lastbook!= null){
					Control.logln("ABBRUCH der Rückgabe: (" + lastbook.ID + ", " + OBTBookType.getTitle(Ean.getEan(lastbook.ISBN))+", "+lastbook.Scoring_of_condition+"), Benutzeraktion" );
					lastbook=null;
					np.publish(lastbook);
				}
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
				if (MainGUI.isSelectedView(this)){
					book=new OBook(e.getEan());
					if (lastbook != null){
						if (book.equals(lastbook)){
							lastbook.incCondition();
						}else{
							commit(lastbook);
							lastbook = book;
						}	
					}else{
						lastbook = book;
						
					}
					np.publish(lastbook);
					break;
				}
			default: // do nothing
			}
			
		
	}
	
	private synchronized void commit(OBook book){
		new Deb (debug,"commit");
		String classname=""
			;
		String classid="";
		Ean leaser=book.getLeaser();
		switch (EEANType.getType(leaser)){
			case BVClass:
				if(OClass.getBVClass(leaser)!=null ){
					classname=OClass.getBVClass(leaser).Name;
				}else{
					classname="000";
				}
				classid="K"+leaser.toString().substring(8, 12);
				
			break;
		}	
		Control.log("ZURÜCK: B"+book.ID + " <- "+classid + " ("+ OBTBookType.getTitle(Ean.getEan(book.ISBN))+" von " + classname +") Zustand: "+book.Scoring_of_condition );
		
		if (book.endLease()){
			Control.logln(" OK!");
		}else{
			Control.logln(" Fehler!");
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
		
		return (new Vector<SelectedEvent.SelectedEventType>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add(SelectedEvent.SelectedEventType.BookLeasedSelected);
		}});
	}
	public void toFront() {
		np.publish(null);
		// TODO Auto-generated method stub
		
	}
	
}
