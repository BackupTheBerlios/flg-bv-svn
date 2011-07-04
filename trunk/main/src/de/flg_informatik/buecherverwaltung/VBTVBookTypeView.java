package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.ean13.Ean;

public class VBTVBookTypeView extends ATableView implements ActionListener, BVConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int[] columnwidth={13,20,50,50};
	boolean[] columnresizable={false,true,true,true}; 
	private VBTVDatamodell mymodell;
	private VBTVBookTypeView me;
	private int lastselected=-1;
	private JPChooser bvchooser;
	private Vector<String> booktyp=null;
	private VBTVSouthPanel booktypepanel = null;
	private JPSrcollTable bvjp;
	private long pretime=0;
	private long echotime=10;
	private int preselected;
	enum State{
		info,
		edit,
		zukauf,
		register,
		neu;
	}
	State state;
	
	public VBTVBookTypeView(){
		me = this;
		mymodell=new VBTVDatamodell(this);
		this.bvjp=new JPSrcollTable(me,mymodell);
		this.booktypepanel=new VBTVSouthPanel(state,this);
		setLayout(new BorderLayout());
		add(bvjp,BorderLayout.CENTER);
		add(makeChooser(this),BorderLayout.WEST);
		add(booktypepanel,BorderLayout.SOUTH);
		validate();
	}
	
	
	//@Override
	/*public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(1280,1024);
	}
	*/
	
	public void thingSelected(SelectedEvent e) {
		switch (e.getId()){
		case ISBNSelected:
			if (state==State.neu){
				stateChanged(State.info);
				
			}
			booktyp=mymodell.getBookType(e.getEan());
			new Deb(debug,booktyp);
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
				SelectedEvent.addBVSelectedEventListener(new WaitingForBT(new OBook(e.getEan())));
				invalidate();
				me.validate();
			}else{
				new OBook(e.getEan()).setBookType(Ean.getEan(booktyp.firstElement()));
			}
		case BookTypOnTop:
			if (state!=State.info){
				stateChanged(State.info);
			}
			invalidate();
			me.validate();
			break;
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
		ret.add(bvchooser=new JPChooser(listener,chooses,JPChooser.Orientation.VERTICAL));
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
	
		if (preselected==lastselected){ // catch echos
			if (pretime-(pretime=System.currentTimeMillis())<echotime){
				return; //was a Echo
			}
		}else{
			preselected=lastselected;
			pretime=System.currentTimeMillis();
		}
	if (state==State.neu){
		state=State.info;
	}
	if (state==State.register){ //we need another (UnknownBook)-Selection, must not make new event!
		return;
	}
	
	stateChanged(state);
	
	}
		

	public void actionPerformed(ActionEvent e) { //JPChooser
		state=State.values()[Integer.parseInt(e.getActionCommand())];
		switch (state){
		case info:
			SelectedEvent.makeEvent(this, SelectedEvent.SelectedEventType.ISBNSelected, Ean.getEan(((VBTVDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case edit:
			SelectedEvent.makeEvent(this, SelectedEvent.SelectedEventType.ISBNSelected, Ean.getEan(((VBTVDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case zukauf:
			SelectedEvent.makeEvent(this, SelectedEvent.SelectedEventType.ISBNBuySelected, Ean.getEan(((VBTVDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case register:
			booktyp=null;
			new Warn("Bitte den Buchtyp für die zu \n registrierenden Bücher wählen!");
			break;
		case neu:
			if(booktyp==null){ // new selected manually
				SelectedEvent.makeEvent(this, SelectedEvent.SelectedEventType.ISBNUnknownSelected);
				break;
			}else{
				if(OBTBookType.isKnownISBN(Ean.getEan(booktyp.get(0)))){
					SelectedEvent.makeEvent(this, SelectedEvent.SelectedEventType.ISBNUnknownSelected);
					break;
				}
			}
			// selected by scan nothing to do!
			
			
			break;
		}
		
		
	}
	VBTVDatamodell getModell(){
		return mymodell;
	}
	class WaitingForBT implements SelectedEventListener{
		final OBook book;
		public WaitingForBT(OBook book) {
			this.book=book;
		}
		public void thingSelected(SelectedEvent e) {
			if (e.getId()==SelectedEvent.SelectedEventType.ISBNSelected){
				book.setBookType(e.getEan());
				SelectedEvent.removeBVSelectedEventListener(this);
			}else{
				SelectedEvent.removeBVSelectedEventListener(this);
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
		new Deb(debug,"retireing");
		
		
	}
	public void validate() {
		super.validate();
		bvjp.repaint();
		
		
	}

	public void toClose() {
		// TODO Auto-generated method stub
		
	}
	@SuppressWarnings("serial")
	public Vector<SelectedEvent.SelectedEventType> getConsumedEvents() {
		return (new Vector<SelectedEvent.SelectedEventType>(){{
			add(SelectedEvent.SelectedEventType.ISBNSelected);
			add(SelectedEvent.SelectedEventType.ISBNUnknownSelected);
			add(SelectedEvent.SelectedEventType.ISBNBuySelected);
			add(SelectedEvent.SelectedEventType.BookUnknownSelected);
			add(SelectedEvent.SelectedEventType.BTedit);
			add(SelectedEvent.SelectedEventType.BTinfo);
			add(SelectedEvent.SelectedEventType.BTnew);
			add(SelectedEvent.SelectedEventType.BookTypOnTop);
	}});
	}


	public void toFront() {
		if (lastselected==-1){ // no Selection yet
			bvjp.getTable().changeSelection(0, -1, false, false);
			bvjp.getTable().changeSelection(0, -1, true, false);
		}
		
		bvjp.getTable().invalidate();
		bvjp.validate();
		
				
	}
	
	
}
