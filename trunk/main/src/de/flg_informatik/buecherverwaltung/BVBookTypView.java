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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVBookTypView extends BVView implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int[] columnwidth={13,20,50,50};
	private final String myname="Buchtypen";
	boolean[] columnresizable={false,true,true,true}; 
	private BVBookTypeDatamodell mymodell;
	private BVBookTypView me;
	private int lastselected=1;
	private BVChooser bvchooser;
	private Vector<String> booktyp=null;
	private BookTypeWhat booktypewhat = null;
	private BVControl bvc;
	private BVJPanel bvjp;
	enum State{
		info,
		edit,
		druck,
		neu;
	}
	State state;
	
	public BVBookTypView(BVControl bvc){
		me = this;
		mymodell=new BVBookTypeDatamodell(this);
		this.bvc=bvc;
		this.bvjp=new BVJPanel(me,mymodell);
		setLayout(new BorderLayout());
		add(bvjp,BorderLayout.CENTER);
		add(makeChooser(this),BorderLayout.WEST);
		booktypewhat = new BookTypeWhat(state); //adds itself
		BVSelectedEvent.addBVSelectedEventListener(this);
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
			booktyp=mymodell.getBookType(e.getEan());
			break;
		case ISBNUnknownSelected:
			booktyp=newBooktype();
			if (e.getEan()!=null){
				booktyp.set(0, e.getEan().toString());
			}
			break;
		case ISBNBuySelected:
			booktyp=mymodell.getBookType(e.getEan());
			
			break;
		case BookUnknownSelected:	
			
			
		}
		/* 
		 * Bin ich vorne?
		 * Dann gebe ich an den Controller ab
		 */	
		if (((JTabbedPane)(this.getParent())).indexOfTab(myname)==((JTabbedPane)(this.getParent())).getSelectedIndex()){
			
		}
			
			
		booktypewhat.reMakePanel();
		
		
	}
	/*public synchronized void validateTree(){
		super.validateTree();
	}*/
	
	private JPanel makeChooser(ActionListener listener){
		JPanel ret =new JPanel(new FlowLayout());
		ArrayList<String> chooses=new ArrayList<String>();
		for(State s : State.values()){
			chooses.add(s.name());
		}
		ret.add(bvchooser=new BVChooser(listener,chooses));
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
	
	private class BookTypeWhat extends JPanel implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		BookTypeWhat mebtw;
		TextField[] editfields;
		JButton save;
		JButton remove;
		JPanel up;
		
		public BookTypeWhat(State state) {
			mebtw=this;
			up=new JPanel(new FlowLayout());
			save=new JButton("Speichern");
			save.setActionCommand("save");
			save.addActionListener(mebtw);
			remove=new JButton("Buchtyp löschen");
			remove.setActionCommand("remonve");
			remove.addActionListener(mebtw);
			this.setLayout(new GridLayout(2,1));
			
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("save")){
				
				Vector<String> newvec = new Vector<String>();
				for (int i=0; i< editfields.length;i++){
					newvec.add(editfields[i].getText());
				}
				debug("saving"+state);
				switch (state){
					case neu:
						switch (me.mymodell.setNewBooktype(newvec)){
							case ok:
								lastselected = me.mymodell.getRowCount()-1;
								me.stateChanged(State.info);
								break;
							case unknown:
								// Datenbankfehler
								break;
						}		
						break;
					case edit:
						me.mymodell.setBookType(newvec);
						booktyp=newvec;
						me.stateChanged(State.info);
						break;
					
					case druck:
						int howmany;
						if ((howmany=Integer.parseInt(editfields[2].getText()))>0){
							BVBook.makeNewBooks(howmany, editfields[0].getText());
							me.stateChanged(State.info);
						}
						
						
						
						
					
					
				}
			}
		}
		synchronized void reMakePanel(){
			this.removeAll();
			up.removeAll();
			switch (state){
				case info:
				case edit:
				case neu:	
					if (booktyp!=null){
						editfields = new TextField[mymodell.numofcolumns];
						for (int i=0; i< mymodell.numofcolumns;i++){
							up.add(new Minipanel(mymodell.getColumnName(i),editfields[i]=new TextField(booktyp.get(i),columnwidth[i])));
							editfields[i].setEditable(false);
												
							switch (state){
								case info:
									editfields[i].setEditable(false);
									editfields[i].setFocusable(false);
									break;
								case edit: //man sollte jetzt die Datenbank locken
									if (i>0){
										editfields[i].setEditable(true);
										editfields[i].setFocusable(true);
									}else{
										editfields[i].setEditable(false);
										editfields[i].setFocusable(false);
									}
									break;
								case neu:
									
									if (i>0){
										editfields[i].setEditable(true);
										editfields[i].setFocusable(true);
									}else{
										editfields[i].setEditable(false);
										editfields[i].setFocusable(false);
									}
									if (booktyp.firstElement()==null){ // ISBN manuell eingeben
										editfields[0].setEditable(true);
										editfields[0].setFocusable(true);
									}
									break;
							}	
						
						}
						this.add(up);
						
						switch(state){ // Hier kommt der Rest zur Buchtyp-Zeile dazu.
						case info:
							up.add(new Minipanel("Bestand",new Label(Integer.toString(mymodell.getBookCount(booktyp.get(0))))));
							up.add(new Minipanel("Im Lager",new Label(Integer.toString(mymodell.getFreeBookCount(booktyp.get(0))))));
							break;
						case edit:
							up.add(new Minipanel(null,save));
							break;
						case neu:
							up.add(new Minipanel(null,save));
							break;
							
						}
						bvjp.table.repaint();
						booktyp=null;
						
					
						
					}
					break;
				case druck:
					if (booktyp!=null){
						editfields = new TextField[3];
						for (int i=0; i< 2;i++){
							up.add(new Minipanel(mymodell.getColumnName(i),editfields[i]=new TextField(booktyp.get(i),columnwidth[i])));
							editfields[i].setEditable(false);
						}
						up.add(new Minipanel("Anzahl neu",editfields[2]=new TextField(5)));
					}
					up.add(new Minipanel(null,save));
					this.add(up);
					break;
					
					
			}
			me.add(this,BorderLayout.SOUTH);
			me.validate();	
		}
		private class Minipanel extends JPanel{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			Minipanel(String c1, Component c2){
				setLayout(new GridLayout(2,1));
				add(new Label(c1));
				add(c2);
			}
		
		}
		private class Editpanel extends JPanel{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			Editpanel(){
				super(new BorderLayout());
				
			} 
		}
		
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
	stateChanged(state);

	}
		

	public void actionPerformed(ActionEvent e) {
		state=State.values()[Integer.parseInt(e.getActionCommand())];
		switch (state){
		case info:
			BVSelectedEvent.makeEvent(lastselected, SelectedEventType.ISBNSelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case edit:
			BVSelectedEvent.makeEvent(lastselected, SelectedEventType.ISBNSelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case druck:
			BVSelectedEvent.makeEvent(lastselected, SelectedEventType.ISBNBuySelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case neu:
			BVSelectedEvent.makeEvent(null, SelectedEventType.ISBNUnknownSelected);
			break;
		}
		
		
	}
	static private void debug(Object obj){
		System.out.println(BVBookTypView.class+": "+ obj);
	}
	
	
}
