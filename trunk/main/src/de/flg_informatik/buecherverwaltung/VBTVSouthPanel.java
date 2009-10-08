/**
 * 
 */
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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.flg_informatik.Etikett.EtikettDruck;
import de.flg_informatik.ean13.*;

import de.flg_informatik.buecherverwaltung.VBTVBookTypeView.State;


/**
 * @author notkers
 *
 */
class VBTVSouthPanel extends JPanel implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static boolean debug=true;
		VBTVSouthPanel mebtp;
		VBTVBookTypeView myview;
		TextField[] editfields;
		JButton save;
		JButton directprint;
		JButton remove;
		JPanel up;
		State state;
		
		public VBTVSouthPanel(State state, VBTVBookTypeView myview) {
			mebtp=this;
			this.myview=myview;
			this.state=state;
			up=new JPanel();
			save=new JButton("Speichern");
			save.setActionCommand("save");
			save.addActionListener(mebtp);
			directprint=new JButton("sofort drucken");
			directprint.setActionCommand("directprint");
			directprint.addActionListener(mebtp);
			remove=new JButton("Buchtyp löschen");
			remove.setActionCommand("remonve");
			remove.addActionListener(mebtp);
			this.setLayout(new GridLayout(0,1));
			myview.add(this,BorderLayout.SOUTH);
			myview.validate();
			
			
		}
		public void actionPerformed(ActionEvent e) {
			Vector<String> newvec = new Vector<String>();
			for (int i=0; i< editfields.length;i++){
				newvec.add(editfields[i].getText());
			}
			if (e.getActionCommand().equals("save")){
				new Deb(debug,"saving"+state);
				switch (state){
					case neu:
						switch (myview.getModell().setNewBooktype(newvec)){
							case ok:
								myview.selectLastBookType();
								myview.stateChanged(State.info);
								break;
							case unknown:
								// Datenbankfehler
								break;
						}		
						break;
					case edit:
						myview.getModell().setBookType(newvec);
						myview.setBookType(newvec);
						myview.stateChanged(State.info);
						break;
					
					case druck:
						// TODO there should be a nice fifo for storing Etiketten to print
						// maybe in UStorage and a nice frontend to print it
					
						
						
						
						
					
					
				}
			}
			if (e.getActionCommand().equals("directprint")){
				new Deb(debug,"printing"+state);
				int howmany;
				if ((howmany=Integer.parseInt(editfields[2].getText()))>0){
					EtikettDruck.etikettenDruck(OBook.makeNewBooks(howmany, editfields[0].getText()),Integer.parseInt(editfields[3].getText()));
					myview.stateChanged(State.info);
				}
			}	
		}
		synchronized void reMakePanel(State state, Vector<String> booktyp){
			myview.remove(this);
			this.removeAll();
			up.removeAll();
			
			switch (state){
				case info:
				case edit:
				case neu:	
					if (booktyp!=null){
						editfields = new TextField[myview.getModell().numofcolumns];
						for (int i=0; i< myview.getModell().numofcolumns;i++){
							up.add(new Minipanel(myview.getModell().getColumnName(i),editfields[i]=new TextField(booktyp.get(i),myview.columnwidth[i])));
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
						
						
						switch(state){ // Hier kommt der Rest zur Buchtyp-Zeile dazu.
						case info:
							up.add(new Minipanel("Bestand",new Label(Integer.toString(myview.getModell().getBookCount(booktyp.get(0))))));
							up.add(new Minipanel("Im Lager",new Label(Integer.toString(myview.getModell().getFreeBookCount(booktyp.get(0))))));
							break;
						case edit:
							up.add(new Minipanel(null,save));
							add(new OBUPanel(new Ean(editfields[0].getText())));
							break;
						case neu:
							up.add(new Minipanel(null,save));
							break;
							
						}
						this.add(up);
						booktyp=null;
						
					
						
					}
					break;
				case druck:
					if (booktyp!=null){
						editfields = new TextField[4];
						for (int i=0; i< 2;i++){
							up.add(new Minipanel(myview.getModell().getColumnName(i),editfields[i]=new TextField(booktyp.get(i),myview.columnwidth[i])));
							editfields[i].setEditable(false);
						}
						up.add(new Minipanel("Anzahl neu",editfields[2]=new TextField("0",5)));
						up.add(new Minipanel("Etiketten Offset",editfields[3]=new TextField("0",5)));
					}
					up.add(new Minipanel(null,directprint));
					this.add(up);
					break;
				case register:
					if (booktyp!=null){
						
					}else{
						add(new JLabel("Bitte zugehörigen Buchtyp anklicken/scannen!"));
					}
					
					
			}
			up.invalidate();
			invalidate();
			myview.add(this,BorderLayout.SOUTH);
			myview.validate();
			
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
		private class VerticalPanel extends JPanel{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			VerticalPanel(){
				setLayout(new GridLayout(0,1));
				
			}
		public Dimension getSize(){
				return getParent().getSize();
			}
		}
		
	
	
}
