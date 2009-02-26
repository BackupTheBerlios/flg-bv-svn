package de.flg_informatik.buecherverwaltung;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextField;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private Integer condition=0;
	private BVBook book=null;
	private TextField idf = new TextField("",13);
	private TextField titlef = new TextField("",30);
	private JLabel conditionf = new JLabel("w");
	public BVBookBack(BVControl bvc,int index){
		super(index);
		debug(this.index);
		BVSelectedEvent.addBVSelectedEventListener(this);
		this.setLayout(new FlowLayout());
		idf.setEditable(false);
		titlef.setEditable(false);
		conditionf.setFont(new Font(null,	Font.BOLD, 96 ));
		this.bvc=bvc;
		this.add(idf);
		this.add(titlef);
		this.add(conditionf);
		publish(book);
		this.setVisible(true);
		
		
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
		// No List, no Selection, nurh
		
	}
	public void toClose(){
		synchronized(condition){
			condition.notify();
			book=null;
			publish(book);
		}
	}

	public void thingSelected(BVSelectedEvent e) {
		debug("selected "+e.getEan());
		debug(e.getId());
		switch (e.getId()){
			case BookLeasedSelected:
				incCondition(BVBook.makeBookID(e.getEan())); // same Book = Condition + 1
				// next Book = Condition unchanged
				break;
				
			default:
				toClose();
				
		// TODO Auto-generated method stub
		}
	}
	
	
	private boolean bookBack(BigInteger id){
		
		
		synchronized(condition) {
			book=new BVBook(id);
			debug (1);
			//condition=Integer.valueOf(book.Scoring_of_condition);
			publish(book);
			
			try{
				condition.wait();
				
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
			BVUtils.doUpdate("UPDATE Books SET Location=1, Scoring_of_Condition="+book.Scoring_of_condition+" WHERE ID="+book.ID);
			debug("Rückbuchung: "+book.ID);
			
		}
		
			// TODO: Close individual lease
	
		return true;
		
		
	}
	
	private void publish(BVBook book){
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
		this.validate();
		
		
	}
	
	private void incCondition(BigInteger id){
		
		if (book!=null){
			debug("book.id: "+book.ID+" id: "+ id);
			if (book.ID.equals(id)){
				debug(2);
				book.Scoring_of_condition+=1;
				publish(book);
				
			}else{
				debug(3);
				synchronized(condition){
					condition.notify();
				}
				bookBack(id);
					
			}
		}else{
			debug("bookBack: "+id);
			bookBack(id);
			
		
		}
		
	
	}
	static private void debug(Object obj){
		System.out.println(BVBookBack.class+": "+ obj);
	}
}
