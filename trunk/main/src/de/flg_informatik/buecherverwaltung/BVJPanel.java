package de.flg_informatik.buecherverwaltung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVJPanel extends JPanel {
	

	private BTVTable table;
	BVJPanel me; 
	AbstractTableModel memodell;
	BVView meparent;
	
	public BVJPanel(BVView myparent,AbstractTableModel mymodell){
		this.me=this;
		this.memodell=mymodell;
		this.meparent=myparent;
		this.table=new BTVTable(meparent, memodell);
		this.add(new JScrollPane(this.table 
				,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS){
			public Dimension getPreferredSize() {
				return new Dimension(me.getSize().width-me.getInsets()
					.left-me.getInsets().right-10,me.getSize().height-me.getInsets()
					.top-me.getInsets().bottom-10);
			}
			
		}
		);
		
	}
	
	public BTVTable getTable(){
		return table;
	}
		
	class BTVTable extends JTable {
		BVView meparent;
		AbstractTableModel memodell;
		public BTVTable(BVView myparent, AbstractTableModel mymodell) {
			super(mymodell);
			this.memodell=mymodell;
			this.meparent=myparent;
			this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.setRowSelectionAllowed(true);
			this.setColumnSelectionAllowed(false);
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		}
		
		public synchronized void validate(){
				//Es könnte mehr Colums als Parameter in myparent.columnwidth bzw .columnresizable geben
			try{
				int maxtchar=this.getGraphics().getFontMetrics().charWidth('m');
				for (int i=0; i<this.getColumnCount();i++){
					this.getColumn(memodell.getColumnName(i)).setPreferredWidth(((BVBookTypView)meparent).columnwidth[i]*maxtchar);
					if (!((BVBookTypView)meparent).columnresizable[i]){
						this.getColumn(memodell.getColumnName(i)).setMinWidth(((BVBookTypView)meparent).columnwidth[i]*maxtchar);
						this.getColumn(memodell.getColumnName(i)).setResizable(false);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				
				javax.swing.JOptionPane.showMessageDialog (null, "Interner Fehler aufgetreten. Für weitere Information sehen Sie bitte den Konsolenoutput ein.");
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
			this.validateTree();
		}
	}

	
}
