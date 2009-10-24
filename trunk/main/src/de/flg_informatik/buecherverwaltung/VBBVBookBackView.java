package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.ean13.Ean;

public class VBBVBookBackView extends JPanel implements UCCase {
	

	/**
	 * Zunï¿½chst nur Rückgabe des Buches,
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
	private static boolean debug=true;
	private Integer condition=0;
	//private OBook book=null;
	private TextField idf = new TextField("",13);
	private TextField titlef = new TextField("",30);
	private JLabel conditionf = new JLabel("w");
	private OBook lastbook=null;
	private NorthPanel np;
	private JButton end;
	private JButton cancel;
	private NorthPanel.ConditionSwitcher condswitch;
	public VBBVBookBackView(){
		this.setLayout(new BorderLayout());
		add(np=new NorthPanel(),BorderLayout.NORTH);
		
		this.setVisible(true);
	}
	private class NorthPanel extends JPanel implements ActionListener, MouseListener {
		JButton[] condbut=new JButton[6];
		
		
		
		NorthPanel(){
			idf.setEditable(false);
			titlef.setEditable(false);
			conditionf.setFont(new Font(null,	Font.BOLD, 96 ));
			add(idf);
			idf.addMouseListener(VBBVBookBackView.NorthPanel.this);
			add(titlef);
			add(conditionf);
			add(condswitch=new ConditionSwitcher());
			add(end=new JButton("Abschließen"){{
				addActionListener(VBBVBookBackView.NorthPanel.this);
			}});
			add(cancel=new JButton("Abbrechen"){{
				addActionListener(VBBVBookBackView.NorthPanel.this);
			}});
		}
		private class ConditionSwitcher extends JPanel{
			
			ConditionSwitcher(){
				super (new GridLayout(6,1));
				for (int i=0;i<6;i++){
					condbut[i]=new JButton(" "+(i+1)+" ");
					condbut[i].setActionCommand((i+1)+"");
					condbut[i].setBackground(colorOfCondition(i+1));
					condbut[i].addActionListener(VBBVBookBackView.NorthPanel.this);
					add(condbut[i]);
				}
			}
		}

		// @Override
		public void actionPerformed(ActionEvent e) {
			new Deb(e.getActionCommand());
			
			if (lastbook!=null){
				for (int i=0; i<6; i++ ){
					if(e.getActionCommand().equals((i+1)+"")){
						lastbook.Scoring_of_condition=i+1;
						publish(lastbook);
						
					}
				}
			}
			if (e.getSource().equals(end)){
					toBackground(); 
					publish(lastbook);
			}
			if (e.getSource().equals(cancel)){
				Control.log("ABBRUCH der Rückgabe: (" + lastbook.ID + ", " + OBTBookType.getTitle(new Ean(lastbook.ISBN))+", "+lastbook.Scoring_of_condition+")" );
				lastbook=null;
				publish(lastbook);
			}
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (lastbook==null){
				new Warn("Bitte ID eingeben,\n ohne 20...");
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
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
						incCondition(lastbook);
					}else{
						commit(lastbook);
						lastbook = new OBook(e.getEan());
					}	
				}else{
					lastbook = new OBook(e.getEan());
					
				}
				publish(lastbook);
				break;
			default: // do nothing
			}
			
		
	}
	
	private synchronized void commit(OBook book){
		new Deb (debug,"commit");
		if (USQLQuery.doUpdate("UPDATE Books SET Location=1, " +
				"Scoring_of_Condition="+book.Scoring_of_condition+" WHERE ID="+book.ID)==1){
				
			Control.log("Rückgabe von Buch: " + book.ID + " " + OBTBookType.getTitle(new Ean(book.ISBN))+" Zustand: "+book.Scoring_of_condition );
		}
	
	}
	
	
	
	private synchronized boolean publish(OBook book){
		np.setVisible(false);
		if (book==null){
			idf.setText("");
			titlef.setText("");
			conditionf.setText("-");
			np.setBackground(new Color(150,150,150));
			condswitch.setVisible(false);
			end.setVisible(false);

		}else{
			idf.setText(OBook.makeBookEan(book.ID).toString());
			titlef.setText(OBTBookType.getTitle(new Ean(book.ISBN)));
			conditionf.setText(book.Scoring_of_condition+"");
			np.setBackground(colorOfCondition(book.Scoring_of_condition));
			condswitch.setVisible(true);
			end.setVisible(true);
		}
		
		np.setVisible(true);
		MainGUI.val();
		return true;
		
		
	}
	private Color colorOfCondition(int cond){
		if (cond>6){
			cond=6;
		}
		switch(cond){
			case 1:
				return new Color(0,230,0);
			case 2:
				return new Color(144,230,0);
			case 3:
				return Color.YELLOW;
			case 4:
				return Color.ORANGE;
			case 5:
				return new Color(255,128,0);
			case 6:
				return new Color(200,0,0);
				
		}
		return new Color((int)Math.min((cond-1)*70,255),(int)Math.min((6-(cond))*70,255),0-0*Math.abs(cond-3));
	}
	
	private void incCondition(OBook book){
		book.Scoring_of_condition++;
		
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
		publish(null);
		// TODO Auto-generated method stub
		
	}
	
}
