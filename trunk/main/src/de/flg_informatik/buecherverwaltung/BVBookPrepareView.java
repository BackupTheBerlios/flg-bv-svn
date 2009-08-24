package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVBookPrepareView extends BVTableView implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean debug=true;
	final int[] columnwidth={13,20,50,50};
	boolean[] columnresizable={false,true,true,true}; 
	private BVPrepareDatamodell mymodell;
	private BVBookPrepareView me;
	private int lastselected=-1;
	private BVJPanel bvjp;
	private BVWestClass wp;
	private long pretime=0;
	private long echotime=10;
	private int preselected;
	
	public BVBookPrepareView(){
		me = this;
		mymodell=new BVPrepareDatamodell(me, null);
		bvjp=new BVJPanel(me,mymodell);
		setLayout(new BorderLayout());
		add(wp=new BVWestClass(),BorderLayout.WEST);
		add(bvjp,BorderLayout.CENTER);
		validate();
	}
	
	
	@Override
	/*public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(1280,1024);
	}
	*/
	
	public void thingSelected(BVSelectedEvent e) {
		switch (e.getId()){
		case BookFreeSelected:
				// TODO Ausleihe verbuchen
			break;
		default:	
					
		}
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
	
		if (preselected==(preselected=lastselected)){ // catch echos
			if (pretime-(pretime=System.currentTimeMillis())<echotime){
				return; //was a Echo
			}
		}
	
	
	}
		

	public void actionPerformed(ActionEvent e) { //BVChooser
		
		
		
	}
	BVPrepareDatamodell getModell(){
		return mymodell;
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
		add(BVSelectedEvent.SelectedEventType.BLClassSelected);
		}});
	}


	public void toFront() {
				
	}
	
	
}
