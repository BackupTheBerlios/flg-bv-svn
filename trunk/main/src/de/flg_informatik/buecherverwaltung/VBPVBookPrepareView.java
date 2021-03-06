package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.event.ListSelectionEvent;

public class VBPVBookPrepareView extends ATableView implements ActionListener, BVConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int[] columnwidth={13,20,50,50};
	boolean[] columnresizable={false,true,true,true}; 
	private VBPVDatamodell mymodell;
	private VBPVBookPrepareView me;
	private int lastselected=-1;
	private JPSrcollTable bvjp;
	private JPVYearChooser wp;
	private long pretime=0;
	private long echotime=10;
	private int preselected;
	
	public VBPVBookPrepareView(){
		me = this;
		mymodell=new VBPVDatamodell(null);
		bvjp=new JPSrcollTable(me,mymodell);
		setLayout(new BorderLayout());
		add(wp=new JPVYearChooser(),BorderLayout.WEST);
		add(bvjp,BorderLayout.CENTER);
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
	
		if (preselected==lastselected){ // catch echos
			if (pretime-(pretime=System.currentTimeMillis())<echotime){
				return; //was a Echo
			}
		}else{
			preselected=lastselected;
		}
	
	
	}
		

	public void actionPerformed(ActionEvent e) { //JPChooser
		
		
		
	}
	VBPVDatamodell getModell(){
		return mymodell;
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
		add(SelectedEvent.SelectedEventType.BLClassSelected);
		}});
	}


	public void toFront() {
				
	}
	
	
}
