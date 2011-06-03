package de.flg_informatik.buecherverwaltung;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.flg_informatik.buecherverwaltung.JPChooser.Orientation;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FireButton;

public class OBUPanel extends JPanel  implements ActionListener, BVConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Ean isbn=null;
	private OBUPanel me;
	private JButton add = new JButton("hinzufügen");
	private JButton cancel = new JButton("abbrechen");

	
	public OBUPanel(Ean ISBN){
		me=this;
		this.isbn=ISBN;
		add.addActionListener(this);
		cancel.addActionListener(this);
		add(new showPanel());
		validate();
					
	}
	
	@SuppressWarnings("serial")
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==add){
			this.removeAll();
			//add (new showPanel());
			//add(new aequiPanel());
			final addPanel ap=new addPanel();
			add(ap);
			add(new JPanel(new GridLayout(0,1)){{add(new JLabel("Verwendung hinzufügen:"));add(new JPanel(){{add(cancel);
					}});;}});
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
	@SuppressWarnings("serial")
	class addPanel extends JPanel implements ActionListener{
		JPChooser sc;
		JPChooser gc;
		//private FireButton save = new FireButton("save");

		addPanel(){
			super(new GridLayout(0,1));
			add(sc=new JPChooser(this,OBUBookUse.getSubjects(), Orientation.HORZONTAL));
			add(gc=new JPChooser(this,OBUBookUse.getGrades(), Orientation.HORZONTAL));
			invalidate();
		}
	
		public void actionPerformed(ActionEvent e) {
			
			if (gc.getSelected()!=null & sc.getSelected()!= null){
				OBUBookUse.makeBookUse(isbn,gc.getSelected(),sc.getSelected());
				add.doClick();
			}
			
		}
	}

	@SuppressWarnings("serial")
	class showPanel extends JPanel implements ActionListener{
		private FireButton remove = new FireButton("entfernen");
		JPChooser bu;
		showPanel(){
			setLayout(new GridLayout(0,1));
			JPanel ap=new JPanel(){{
				add(new JLabel("Verwendbar in: Klassenstufe: Fach"));
				if (OBUBookUse.getUsesOfString(isbn).size()>0){ // not empty
					add(bu=new JPChooser(null,OBUBookUse.getUsesOfString(isbn), Orientation.HORZONTAL));
					add(remove);	
				}
				add(add);
			}};
			add(ap);
			remove.addActionListener(this);
			add (new aequiPanel());
			
		}
		public void actionPerformed(ActionEvent e) {
			(OBUBookUse.getUsesOf(isbn).get(bu.getSelectedIndex())).delUse();
			cancel.doClick();
			remove.removeActionListener(this);
		}
	}
	
	class aequiPanel extends JPanel implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private FireButton remove = new FireButton("löschen");
		private FireButton add = new FireButton("setzen");
		
		JPChooser bvc;
		public JDialog jd;
		aequiPanel(){
			
			setLayout(new FlowLayout());
			if (OBUBookUse.getBCIDOf(isbn)>0){
				add(new JLabel("Äquivalent zu:"));
				bvc=new JPChooser(null,OBUBookUse.getEquisOfString(isbn),JPChooser.Orientation.HORZONTAL);
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
				invalidate();
				me.validate();
				SelectedEvent.addBVSelectedEventListener(new WaitingForAequi());
				jd = new JOptionPane("Bitte äquivalente ISBN anklicken/scannen!",JOptionPane.INFORMATION_MESSAGE).createDialog("Auswählen");
				jd.setModal(false);
				jd.setVisible(true);
			}
			if(e.getSource().equals(remove)){
				new Deb(debug,bvc.getSelectedIndex());
				new Deb(debug,bvc.getSelected());
				if (bvc.getSelectedIndex()>-1){
					OBUBookUse.delEqui(new Ean(OBUBookUse.getEquisOfString(isbn).get(bvc.getSelectedIndex())));
				}else{
					new Warn("Keine Äquivalenz gewählt!\n Ignoriere Anweisung.");
				}
				cancel.doClick();
			}
		
			
		}
		class WaitingForAequi extends JPanel implements SelectedEventListener{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void thingSelected(SelectedEvent e) {
				if (jd!=null){
					jd.dispose();
					jd=null;
				}
				
				if (e.getId()==SelectedEvent.SelectedEventType.ISBNSelected){
					OBUBookUse.makeEqui(isbn,e.getEan());
					SelectedEvent.removeBVSelectedEventListener(this);
					new Mess(isbn+" & "+e.getEan() );
					SelectedEvent.makeEvent(this,SelectedEvent.SelectedEventType.ISBNSelected,isbn);
				}
				SelectedEvent.removeBVSelectedEventListener(this);
				cancel.doClick();
			}
		}
	}

	
}
	



