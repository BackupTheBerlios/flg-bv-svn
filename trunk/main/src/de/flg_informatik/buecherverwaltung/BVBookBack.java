package de.flg_informatik.buecherverwaltung;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextField;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.ean13.Ean;

public class BVBookBack extends BVView {

	/**
	 * Zunächst nur Rückgabe des Buches,
	 * Rückmeldung über Zustand,
	 * <auf Knopfdruck Abfrage wieviele Bücher dieses Booktypes 
	 * aus dieser Location noch fehlen>
	 * Schülerkonto auf separatem Tab ...  
	 * 
	 * 
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private static boolean debug=true;
	private Integer condition=0;
	private BVBook book=null;
	private TextField idf = new TextField("",13);
	private TextField titlef = new TextField("",30);
	private JLabel conditionf = new JLabel("w");
	private BVBook lastbook=null;
	public BVBookBack(){
		ConsumedEvents.addElement(
				BVSelectedEvent.SelectedEventType.BookLeasedSelected
				);
		BVSelectedEvent.addBVSelectedEventListener(this);
		this.setLayout(new FlowLayout());
		idf.setEditable(false);
		titlef.setEditable(false);
		conditionf.setFont(new Font(null,	Font.BOLD, 96 ));
		this.add(idf);
		this.add(titlef);
		this.add(conditionf);
		this.setVisible(true);
		
		
	}
	public void itemSelected(ListSelectionEvent e) {
		// No List, no Selection, nurh
		
	}
	
	public synchronized void thingSelected(BVSelectedEvent e) {
		new BVD(debug,e.getEan());
		switch (e.getId()){
		
			case BookLeasedSelected:// we stay on top
				if (lastbook != null){
					if (BVBook.makeBookID(e.getEan()).equals(lastbook.ID)){
						incCondition(lastbook);
					}else{
						commit(lastbook);
						lastbook = new BVBook(e.getEan());
					}	
				}else{
					lastbook = new BVBook(e.getEan());
				}
				new BVD(debug,publish(lastbook));
				break;
			default: // do nothing
			}
			
		
	}
	
	private synchronized void commit(BVBook book){
		new BVD (debug,"commit");
		BVUtils.doUpdate("UPDATE Books SET Location=1, Scoring_of_Condition="+book.Scoring_of_condition+" WHERE ID="+book.ID);
	
	}
	
	
	
	private boolean publish(BVBook book){
		if (book==null){
			idf.setText("");
			titlef.setText("");
			conditionf.setText("-");
			this.setBackground(new Color(150,150,150));

		}else{
			idf.setText(BVBook.makeBookEan(book.ID).toString());
			titlef.setText(BVBookType.getTitle(new Ean(book.ISBN)));
			conditionf.setText(book.Scoring_of_condition+"");
		
			this.setBackground(new Color((int)Math.min((book.Scoring_of_condition-1)*60,255),(int)Math.min((6-(book.Scoring_of_condition))*60,255),0));
		}
		
		BVGUI.val();
		return true;
		
		
	}
	
	private void incCondition(BVBook book){
		book.Scoring_of_condition++;
	}

	
	@Override
	void toBackground() {
		if (lastbook!=null){ // we have been in business
			commit(lastbook); // close Transaction
			lastbook=null;
			new BVD(debug,publish(lastbook));
		}else{
			// nothing to do
		}
		
		
	}
	
}
