package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.Etikett.EtikettDruck;
import de.flg_informatik.buecherverwaltung.BVBookUsePanel.WaitingForAequi;
import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVBookTypeView extends BVTableView implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean debug=true;
	final int[] columnwidth={13,20,50,50};
	boolean[] columnresizable={false,true,true,true}; 
	private BVBookTypeDatamodell mymodell;
	private BVBookTypeView me;
	private int lastselected=1;
	private BVChooser bvchooser;
	private Vector<String> booktyp=null;
	private BTSouthPanel booktypepanel = null;
	private BVJPanel bvjp;
	enum State{
		info,
		edit,
		druck,
		register,
		neu;
	}
	State state;
	
	public BVBookTypeView(){
		me = this;
		mymodell=new BVBookTypeDatamodell(this);
		this.bvjp=new BVJPanel(me,mymodell);
		this.booktypepanel=new BTSouthPanel(state,this);
		setLayout(new BorderLayout());
		add(bvjp,BorderLayout.CENTER);
		add(makeChooser(this),BorderLayout.WEST);
		add(booktypepanel,BorderLayout.SOUTH); 
		validate();
	}
	
	
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(1280,1024);
	}
	
	public void thingSelected(BVSelectedEvent e) {
		switch (e.getId()){
		case ISBNSelected:
			if (state==State.neu){
				stateChanged(State.info);
				
			}
			booktyp=mymodell.getBookType(e.getEan());
			new BVD(debug,booktyp);
			break;
		case ISBNUnknownSelected:
			booktyp=newBooktype();
			if (e.getEan()!=null){
				booktyp.set(0, e.getEan().toString());
				stateChanged(State.neu);
			}
			
			
			
			break;
		case ISBNBuySelected:
			booktyp=mymodell.getBookType(e.getEan());
			
			break;
		case BookUnknownSelected:
			if (state!=State.register){
				stateChanged(State.register);
				BVSelectedEvent.addBVSelectedEventListener(new WaitingForBT(new BVBook(e.getEan())));
				invalidate();
				me.validate();
			}else{
				new BVBook(e.getEan()).setBookType(new Ean(booktyp.firstElement()));
			}
		default:	
					
		}
		
			
			
		booktypepanel.reMakePanel(state, booktyp);
		
		
	}
	
	
	private JPanel makeChooser(ActionListener listener){
		JPanel ret =new JPanel(new FlowLayout());
		ArrayList<String> chooses=new ArrayList<String>();
		for(State s : State.values()){
			chooses.add(s.name());
		}
		ret.add(bvchooser=new BVChooser(listener,chooses,BVChooser.Orientation.VERTICAL));
		state=State.info;
	return ret;
	}
	
	void stateChanged(State state){
		bvchooser.clickOn(state);
	}
	
	private Vector<String> newBooktype(){
		Vector<String> ret=new Vector<String>();
		for (int i=0; i< mymodell.headers.size();i++){
			ret.add(null);
			
		}
		return ret;
		
	}
	
	int getColumnwidth(int i){
		return columnwidth[i];
	}
	boolean getColumresizable(int i){
		return columnresizable[i];
	}	
	
	
	public void itemSelected(ListSelectionEvent e){

		for (int i=e.getFirstIndex(); i<= e.getLastIndex();i++){
			// read the manual ListSelectionEvent
			if (((javax.swing.DefaultListSelectionModel)(e.getSource())).isSelectedIndex(i)){
				lastselected=i;
				
			}
		}
	if (state==State.neu){
		state=State.info;
	}
	if (state==State.register){ //we need another (UnknownBook)-Selection, must not make new event!
		return;
	}
	stateChanged(state);

	}
		

	public void actionPerformed(ActionEvent e) { //BVChooser
		state=State.values()[Integer.parseInt(e.getActionCommand())];
		switch (state){
		case info:
			BVSelectedEvent.makeEvent(this, SelectedEventType.ISBNSelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case edit:
			BVSelectedEvent.makeEvent(this, SelectedEventType.ISBNSelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case druck:
			BVSelectedEvent.makeEvent(this, SelectedEventType.ISBNBuySelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case register:
			booktyp=null;
			new BVW("Bitte den Buchtyp für die zu \n registrierenden Bücher wählen!");
			break;
		case neu:
			if(booktyp==null){ // new selected manually
				BVSelectedEvent.makeEvent(this, SelectedEventType.ISBNUnknownSelected);
				break;
			}else{
				if(BVBookType.isKnownISBN(new Ean(booktyp.get(0)))){
					BVSelectedEvent.makeEvent(this, SelectedEventType.ISBNUnknownSelected);
					break;
				}
			}
			// selected by scan nothing to do!
			
			
			break;
		}
		
		
	}
	BVBookTypeDatamodell getModell(){
		return mymodell;
	}
	class WaitingForBT implements BVSelectedEventListener{
		final BVBook book;
		public WaitingForBT(BVBook book) {
			this.book=book;
		}
		public void thingSelected(BVSelectedEvent e) {
			if (e.getId()==BVSelectedEvent.SelectedEventType.ISBNSelected){
				book.setBookType(e.getEan());
				BVSelectedEvent.removeBVSelectedEventListener(this);
			}else{
				BVSelectedEvent.removeBVSelectedEventListener(this);
			}
			
		}
	}
	void selectLastBookType(){
		lastselected = mymodell.getRowCount()-1;
	}
	void setBookType(Vector<String> booktyp){
		this.booktyp = booktyp;
	}
	

	public void toBackground() {
		new BVD(debug,"retireing");
		
		
	}
	public void validate() {
		super.validate();
		bvjp.repaint();
		
		
	}

	public void toClose() {
		// TODO Auto-generated method stub
		
	}
	public Vector<SelectedEventType> getConsumedEvents() {
		return (new Vector<BVSelectedEvent.SelectedEventType>(){{
			add(BVSelectedEvent.SelectedEventType.ISBNSelected);
			add(BVSelectedEvent.SelectedEventType.ISBNUnknownSelected);
			add(BVSelectedEvent.SelectedEventType.ISBNBuySelected);
			add(BVSelectedEvent.SelectedEventType.BookUnknownSelected);
			add(BVSelectedEvent.SelectedEventType.BTedit);
			add(BVSelectedEvent.SelectedEventType.BTinfo);
			add(BVSelectedEvent.SelectedEventType.BTnew);
	}});
	}


	public void toFront() {
		// TODO Auto-generated method stub
		
	}
	
	
}
