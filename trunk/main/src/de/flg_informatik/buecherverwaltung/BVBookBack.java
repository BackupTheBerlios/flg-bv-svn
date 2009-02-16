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
	private Integer condition;
	private BVBook book;
	private TextField idf = new TextField("",13);
	private TextField titlef = new TextField("",30);
	private JLabel conditionf = new JLabel("w");
	public BVBookBack(BVControl bvc,Connection connection){
		this.setLayout(new FlowLayout());
		idf.setEditable(false);
		titlef.setEditable(false);
		conditionf.setFont(new Font(null,	Font.BOLD, 96 ));
		this.bvc=bvc;
		this.add(idf);
		this.add(titlef);
		this.add(conditionf);
		this.setBackground(Color.BLUE);
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

	public void thingSelected(BVSelectedEvent e) {
		switch (e.getId()){
			case AgainSelected:
				incCondition(1); // same Book = Condition + 1
				break;
			case BookLeasedSelected:
				incCondition(0); // next Book = Condition unchanged
				bookBack(e.getEan().toString());
				break;
			default:
				bvc.thingSelected(e);
		// TODO Auto-generated method stub
		}
	}
		
	private synchronized boolean bookBack(String ean){
		
		try{
			ResultSet rs=BVUtils.doQuery("SELECT * FROM Book WHERE ID="+ean);
			rs.first();
			book=new BVBook(new BigInteger(rs.getString("ID")), rs.getString("Purchased"), 
					rs.getInt("scoring_of_condition"),new BigInteger(rs.getString("Location")), new BigInteger(rs.getString("ISBN")));
		}catch(SQLException sqle){
			sqle.printStackTrace();
			return false;
		}
		synchronized(condition) {
			condition=book.Scoring_of_condition;
			publish(book);
			
			try{
				condition.wait();
			}catch(InterruptedException ie){
				// write the old Condition
			}
		}
		try {
			BVUtils.doUpdate("UPDATE Books SET Location=0, Condition="+condition+" WHERE ID="+ean);
			// TODO: Close individual lease
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return true;
		
		
	}
	
	private void publish(BVBook book){
		idf.setText(book.ID.toString());
		titlef.setText(book.getTitle());
		conditionf.setText(book.Scoring_of_condition+"");
		
		
	}
	
	private void incCondition(int inc){
		synchronized(condition){
			condition=condition+inc;
			condition.notify();
		}
	
	
	}

}
