package de.flg_informatik.buecherverwaltung;

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

public class BVBookUsePanel extends JPanel  implements ActionListener{
	private Ean isbn=null;
	private BVBookUsePanel me;
	private FireButton add = new FireButton("hinzufügen");
	private FireButton cancel = new FireButton("abbrechen");
	
	
	
	public BVBookUsePanel(Ean ISBN){
		me=this;
		this.isbn=ISBN;
		add.addActionListener(this);
		cancel.addActionListener(this);
		this.add(new showPanel());
		this.add(new aequiPanel());
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==add){
			this.removeAll();
			this.add(new showPanel());
			this.add(new aequiPanel());
			this.add(new addPanel());
			
		}
		if (e.getSource()==cancel){
			this.removeAll();
			this.add(new showPanel());
			this.add(new aequiPanel());
		}
		this.validate();
		
	}
	class addPanel extends JPanel implements ActionListener{
		BVChooser sc;
		BVChooser gc;
		private FireButton save = new FireButton("save");
		
		
		addPanel(){
			
;			setLayout(new GridLayout(0,1));
			add(sc=new BVChooser(null,BVBookUse.getSubjects(), Orientation.HORZONTAL));
			add(gc=new BVChooser(null,BVBookUse.getGrades(), Orientation.HORZONTAL));
			
			add(new JPanel(){{add(cancel);add(save);}});
			save.addActionListener(this);
			validate();
		}

		public void actionPerformed(ActionEvent e) {
			
			debug("clicked"+ e);
			debug(sc.getSelected());
			debug(gc.getSelected());
			BVBookUse.makeBookUse(isbn,gc.getSelected(),sc.getSelected());
			add.fire();
			
			
		}
		
	}
	class showPanel extends JPanel implements ActionListener{
		private FireButton remove = new FireButton("löschen");
		BVChooser bu;
		showPanel(){
			
			setLayout(new FlowLayout());
			add(new JLabel("Verwendbar in: Klassenstufe: Fach"));
			if (BVBookUse.getUsesOfString(isbn).size()>0){ // not empty
				add(bu=new BVChooser(null,BVBookUse.getUsesOfString(isbn), Orientation.HORZONTAL));
				remove.addActionListener(this);
				add(remove);	
			}
			add(add);
		}
		public void actionPerformed(ActionEvent e) {
			(BVBookUse.getUsesOf(isbn).get(bu.getSelectedIndex())).delUseOf();
			cancel.fire();
		}
		
	}
	class aequiPanel extends JPanel implements ActionListener{
		private FireButton remove = new FireButton("löschen");
		private FireButton add = new FireButton("setzen");
		BVChooser bu;
		aequiPanel(){
			
			setLayout(new FlowLayout());
			if (BVBookUse.getUsesOf(isbn).size()>0){
				if (BVBookUse.getUsesOf(isbn).firstElement().getAequis().size()>0){
					add(new JLabel("Äquivalent zu:"));
					for (BVBookUse bv:BVBookUse.getUsesOf(isbn).firstElement().getAequis()){ 
						debug(bv);
						add(new JLabel(bv.getIsbn().toString()));
					}
				}else{
					add.setLabel("Äquvalenz setzen"); // two times
				}	
			}else{
				add.setLabel("Äquvalenz setzen");	// two times
			}
			
			add(add);
		}
		public void actionPerformed(ActionEvent e) {
			
			cancel.fire();
		}
		
	}
	public static void main(String[] args) {
		FLGFrame f=new FLGFrame();
		BVGUI.main(null);
		f.add(new BVBookUsePanel(new Ean("379000000000")));
		f.setVisible(true);
		f.pack();
	}
	static private void debug(Object obj){
		System.out.println(BVBookUsePanel.class+": "+ obj);
	}
}
