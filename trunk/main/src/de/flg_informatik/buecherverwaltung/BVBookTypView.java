package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVBookTypView extends BVView implements ActionListener {
	final int[] columnwidth={13,20,50,50};
	final boolean[] columnresizable={false,true,true,true}; 
	private BVBookTypeDatamodell mymodell;
	private BVBookTypView me;
	private int lastselected;
	private BVChooser bvchooser;
	private Vector<String> booktyp=null;
	private BookTypeWhat booktypewhat = null;
	private BVControl bvc;
	enum State{
		info,
		edit,
		neu,
		remove;
	}
	State state;
	
	public BVBookTypView(BVControl bvc,Connection connection){
		me = this;
		this.bvc=bvc;
		setLayout(new BorderLayout());
		mymodell=new BVBookTypeDatamodell(bvc,connection,this);
		add(new BVJPanel(me,mymodell),BorderLayout.CENTER);
		add(makeChooser(this),BorderLayout.WEST);
		booktypewhat = new BookTypeWhat(state); //adds itself
		BVSelectedEvent.addBVSelectedEventListener(this);
		validateTree();
	}
	
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(1280,1024);
	}
	
	public void thingSelected(BVSelectedEvent e) {
		if (e.getId()==SelectedEventType.ISBNSelected){
			debug("selected");
			if (e.getEan()!=null){
				booktyp=mymodell.getBookType(e.getEan());
			}
			if (booktyp==null){
				booktyp=new Vector<String>();
				if (e.getEan()==null){
					booktyp.add("");
				}else{
					booktyp.add(e.getEan().toString());
				}
				for (int i=1; i<mymodell.numofcolumns;i++ ){
					booktyp.add("");
				};
				if (state!=State.neu){ // neue ISBNs nur in "neu" Zustand
					stateChanged(State.neu);
					return; 
				}
				debug("neu: "+e.getEan());
			}
			booktypewhat.reMakePanel();
				
		}
		
	}
	public synchronized void validateTree(){
		super.validateTree();
	}
	private JPanel makeChooser(ActionListener listener){
		JPanel ret =new JPanel(new FlowLayout());
		ret.add(bvchooser=new BVChooser(listener,new String[]{"info","edit","neu"}));
		state=State.info;
	return ret;
	}
	void stateChanged(State state){
		bvchooser.clickOn(state);
	}
	
	private Vector<String> newBooktype(String ean){
		Vector<String> ret=new Vector<String>();
		ret.add(ean);
		for (int i=1; i< mymodell.headers.size();i++){
			ret.add(null);
			
		}
		return ret;
		
	}
		
	
	private class BookTypeWhat extends JPanel implements ActionListener{
		BookTypeWhat mebtw;
		TextField[] editfields;
		JButton save;
		JButton remove;
		
		public BookTypeWhat(State state) {
			mebtw=this;
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
				if (state==State.neu){
					switch (me.mymodell.setNewBooktype(newvec)){
						case ok:
							lastselected = me.mymodell.getRowCount()-1;
							me.stateChanged(State.info);
							
					}
				}
				if (state==State.edit){
					me.mymodell.setBookType(newvec);
				}
			}
		}
		synchronized void reMakePanel(){
			this.removeAll();
			JPanel up=new JPanel(new FlowLayout());
			
				editfields = new TextField[mymodell.numofcolumns];
				for (int i=0; i< mymodell.numofcolumns;i++){
//					up.add(new Minipanel(mymodell.getColumnName(i),editfields[i]=new TextField(booktyp.get(Math.min(i,booktyp.size())),columnwidth[i])));
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
							if (booktyp.firstElement()==""){ // ISBN manuell eingeben
								editfields[0].setEditable(true);
								editfields[0].setFocusable(true);
							}
							break;
					}	
				
				}
			
			this.add(up);
			switch(state){
			case info:
				up.add(new Minipanel("Bestand",new Label(Integer.toString(bvc.gui.bvbv.getBookCount(booktyp.get(0).toString())[0]))));
			
			case edit:
				up.add(new Minipanel(null,save));
				break;
			case neu:
				up.add(new Minipanel(null,save));
				break;
				
			}
			booktyp=null;
			me.add(this,BorderLayout.SOUTH);
			me.validateTree();	
		}
		private class Minipanel extends JPanel{
			Minipanel(String c1, Component c2){
				setLayout(new GridLayout(2,1));
				add(new Label(c1));
				add(c2);
			}
		
		}
		private class Editpanel extends JPanel{
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
		switch (Integer.parseInt(e.getActionCommand())){
		case 0: state=State.info;
			BVSelectedEvent.makeEvent(lastselected, SelectedEventType.ISBNSelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case 1: state=State.edit;
			BVSelectedEvent.makeEvent(lastselected, SelectedEventType.ISBNSelected, new Ean(((BVBookTypeDatamodell)mymodell).tablecells.get(lastselected).get(0).toString()));
			break;
		case 2: state=State.neu;
			if (booktyp==null){	
				BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.ISBNSelected, null);
			}else{
				BVSelectedEvent.makeEvent(this, BVSelectedEvent.SelectedEventType.ISBNSelected, new Ean(booktyp.firstElement()));
			}
			break;
		}
		
		
	}
	static private void debug(Object obj){
		System.out.println(BVBookTypView.class+": "+ obj);
	}
	
	
}
