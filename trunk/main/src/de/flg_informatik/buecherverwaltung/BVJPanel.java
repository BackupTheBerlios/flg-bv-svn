package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import de.flg_informatik.utils.FLGJScrollPane;

public class BVJPanel extends JPanel {
	

	/**
	 * Parent Panel class containing a scrollable table("mymodell")
	 * setting widths of columns by and forwarding ListSelection Events to "myparent"
	 */
	private static final long serialVersionUID = 1L;
	BTVTable table;
	BVJPanel me; 
	AbstractTableModel memodell;
	BVTableView meparent;
	int prefwidth=0;
		
	public BVJPanel(BVTableView myparent,AbstractTableModel mymodell){
		this.me=this;
		this.memodell=mymodell;
		this.meparent=myparent;
		this.table=new BTVTable();
		JPanel inner =new JPanel(new GridLayout(0,1));
		JPanel outer=new JPanel();
		inner.add(table);
		setLayout(new BorderLayout());
		outer.add(inner);
		this.add(new JScrollPane(outer),BorderLayout.CENTER);		
	}
	
	public BTVTable getTable(){
	
	 return table;
	}
	
		
	class BTVTable extends JTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public BTVTable() {
			super();
			this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.setRowSelectionAllowed(true);
			this.setColumnSelectionAllowed(false);
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
			this.setModel(memodell);
					
		}
		
		public synchronized void validate(){
			
			//Es könnte mehr Colums als Parameter in myparent.columnwidth bzw .columnresizable geben
			try{
				prefwidth=0;
				int maxtchar=this.getGraphics().getFontMetrics().charWidth('m');
				for (int i=0; i<this.getColumnCount();i++){
					this.getColumn(memodell.getColumnName(i)).setPreferredWidth(meparent.getColumnwidth(i)*maxtchar);
					prefwidth+=meparent.getColumnwidth(i)*maxtchar;
					
					if (!(meparent).getColumnresizable(i)){
						this.getColumn(memodell.getColumnName(i)).setMinWidth(meparent.getColumnwidth(i)*maxtchar);
						this.getColumn(memodell.getColumnName(i)).setResizable(false);
					}
				}
			}catch(Exception e){
				
				
				e.printStackTrace();
				
				new BVE("Interner Fehler aufgetreten. \n Für weitere Information sehen Sie bitte den Konsolenoutput ein.");
				
			}
			table.setSize(prefwidth, 100);
			super.validateTree();
			
		}
		public Dimension getPreferredScrollableViewportSize(){
			return new Dimension(prefwidth,0);
		}
		public Dimension getSize(){
			return new Dimension(prefwidth,0);
		}
		
		public void valueChanged(ListSelectionEvent e){
			super.valueChanged(e);
			// read the manual ListSelectionEvent
			meparent.itemSelected(e);
			this.validate();
			
		}
	}

	
}
