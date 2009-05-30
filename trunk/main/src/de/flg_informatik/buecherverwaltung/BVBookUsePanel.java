package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.flg_informatik.buecherverwaltung.BVChooser.Orientation;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.FireButton;

public class BVBookUsePanel extends JPanel  implements ActionListener {
	private static boolean debug=true;
	private Ean isbn=null;
	private BVBookUsePanel me;
	private FireButton add = new FireButton("hinzufügen");
	private FireButton cancel = new FireButton("abbrechen");
	private int mw=0;
	private int mh=0;
	
	
	public BVBookUsePanel(Ean ISBN){
		me=this;
		this.isbn=ISBN;
		add.addActionListener(this);
		cancel.addActionListener(this);
		add(new showPanel());
		validate();
					
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==add){
			this.removeAll();
			//add (new showPanel());
			//add(new aequiPanel());
			final addPanel ap=new addPanel();
			add(ap);
			add(new JPanel(new GridLayout(0,1)){{add(new JLabel("Verwendung hinzufügen:"));add(new JPanel(){{add(cancel);add(ap.save);}});;}});
			ap.save.addActionListener(ap);
			invalidate();
			
		}
		if (e.getSource()==cancel){
			this.removeAll();
			this.add(new showPanel());
			//this.add(new aequiPanel());
			invalidate();
			
		}
		validate();
		try{
			me.invalidate();
			me.getParent().invalidate();
			me.getParent().getParent().validate();
		}catch(NullPointerException ne){
			
		}
	}
	class addPanel extends JPanel implements ActionListener{
		BVChooser sc;
		BVChooser gc;
		private FireButton save = new FireButton("save");

		addPanel(){
			super(new GridLayout(0,1));
			add(sc=new BVChooser(null,BVBookUse.getSubjects(), Orientation.HORZONTAL));
			add(gc=new BVChooser(null,BVBookUse.getGrades(), Orientation.HORZONTAL));
			invalidate();
		}
	
		public void actionPerformed(ActionEvent e) {
			
			BVBookUse.makeBookUse(isbn,gc.getSelected(),sc.getSelected());
			add.fire();
			
		}
	}

	class showPanel extends JPanel implements ActionListener{
		private FireButton remove = new FireButton("entfernen");
		BVChooser bu;
		showPanel(){
			setLayout(new GridLayout(0,1));
			JPanel ap=new JPanel(){{
				add(new JLabel("Verwendbar in: Klassenstufe: Fach"));
				if (BVBookUse.getUsesOfString(isbn).size()>0){ // not empty
					add(bu=new BVChooser(null,BVBookUse.getUsesOfString(isbn), Orientation.HORZONTAL));
					add(remove);	
				}
				add(add);
			}};
			add(ap);
			remove.addActionListener(this);
			add (new aequiPanel());
			
		}
		public void actionPerformed(ActionEvent e) {
			(BVBookUse.getUsesOf(isbn).get(bu.getSelectedIndex())).delUse();
			cancel.fire();
			remove.removeActionListener(this);
		}
	}
	
	class aequiPanel extends JPanel implements ActionListener{
		private FireButton remove = new FireButton("löschen");
		private FireButton add = new FireButton("setzen");
		BVChooser bvc;
		aequiPanel(){
			
			setLayout(new FlowLayout());
			if (BVBookUse.getBCIDOf(isbn)>0){
				add(new JLabel("Äquivalent zu:"));
				bvc=new BVChooser(null,BVBookUse.getAequisOfString(isbn),BVChooser.Orientation.HORZONTAL);
				add(bvc);
				add(remove);
				remove.setLabel("Äquiv. entfernen");
				remove.addActionListener(this);
			}else{
				add(new JLabel("Keine Äquivalenzen"));
				// two times
			}	
			add.setLabel("Äquiv. hinzufügen"); 
			add(add);
			add.addActionListener(this);
			
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(add)){
				remove(add);
				add(cancel);
				add(new JLabel("Bitte äquivalente ISBN anklicken/scannen!"));
				invalidate();
				me.validate();
				BVSelectedEvent.addBVSelectedEventListener(new WaitingForAequi());
			}
			if(e.getSource().equals(remove)){
				new BVD(debug,bvc.getSelectedIndex());
				new BVD(debug,bvc.getSelected());
				if (bvc.getSelectedIndex()>-1){
					BVBookUse.delAequi(new Ean(BVBookUse.getAequisOfString(isbn).get(bvc.getSelectedIndex())));
				}else{
					new BVW("Keine Äquivalenz gewählt!\n Ignoriere Anweisung.");
				}
				cancel.fire();
			}
		
			
		}
	}

	class WaitingForAequi extends JPanel implements BVSelectedEventListener{
		public void thingSelected(BVSelectedEvent e) {
			if (e.getId()==BVSelectedEvent.SelectedEventType.ISBNSelected){
				BVBookUse.makeAequi(isbn,e.getEan());
				BVSelectedEvent.removeBVSelectedEventListener(this);
				new BVM(isbn+" & "+e.getEan() );
				BVSelectedEvent.makeEvent(this,BVSelectedEvent.SelectedEventType.ISBNSelected,isbn);
			}
			BVSelectedEvent.removeBVSelectedEventListener(this);
			cancel.fire();
		}
	}
}
	



