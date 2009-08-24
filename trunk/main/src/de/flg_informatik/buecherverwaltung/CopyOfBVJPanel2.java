package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import de.flg_informatik.utils.FLGJScrollPane;
import de.flg_informatik.utils.FLGScrollPaneLayout;

public class CopyOfBVJPanel2 extends JPanel {
	

	/**
	 * Parent Panel class containing a scrollable table("mymodell")
	 * setting widths of columns by and forwarding ListSelection Events to "myparent"
	 */
	private static final long serialVersionUID = 1L;
	BTVTable table;
	CopyOfBVJPanel2 me; 
	AbstractTableModel memodell;
	BVTableView meparent;
		
	public CopyOfBVJPanel2(BVTableView myparent,AbstractTableModel mymodell){
		this.me=this;
		this.memodell=mymodell;
		this.meparent=myparent;
		this.table=new BTVTable();
		this.add(new FLGJScrollPane(table));
		invalidate();
		
		
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
			//Es k�nnte mehr Colums als Parameter in myparent.columnwidth bzw .columnresizable geben
			try{
				int maxtchar=this.getGraphics().getFontMetrics().charWidth('m');
				for (int i=0; i<this.getColumnCount();i++){
					this.getColumn(memodell.getColumnName(i)).setPreferredWidth(meparent.getColumnwidth(i)*maxtchar); 
					if (!(meparent).getColumnresizable(i)){
						this.getColumn(memodell.getColumnName(i)).setMinWidth(meparent.getColumnwidth(i)*maxtchar);
						this.getColumn(memodell.getColumnName(i)).setResizable(false);
					}
				}
			}catch(Exception e){
				
				
				e.printStackTrace();
				
				new BVE("Interner Fehler aufgetreten. \n F�r weitere Information sehen Sie bitte den Konsolenoutput ein.");
				
			}
			
		}
		/*public Dimension getPreferredScrollableViewportSize(){
			return new Dimension(1024,500);
		}
		*/	
		public void valueChanged(ListSelectionEvent e){
			super.valueChanged(e);
			// read the manual ListSelectionEvent
			meparent.itemSelected(e);
			this.validate();
		}
	}

	
}