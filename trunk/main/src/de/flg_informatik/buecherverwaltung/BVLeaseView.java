package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVLeaseView extends JPanel implements BVView , ActionListener{
	private static boolean debug=true;
	private Vector<BVClass> classesvect = null;  
	private BVChooser years=new BVChooser();
	private BVChooser classes=new BVChooser();
	private NorthPanel np;
	private JDialog jd=null;
	public BVLeaseView() {
		setLayout(new BorderLayout());
		add(np=new NorthPanel(),BorderLayout.NORTH);
		setVisible(true);
	}
	private class NorthPanel extends JPanel implements ActionListener{
		NorthPanel(){
			add(years);
			add(classes);
			
		}

		public void actionPerformed(ActionEvent e) {
			new BVD(debug,e);
			
			if (((Container)(e.getSource())).getParent().equals(years)){
				classesvect=BVClass.getClasses(((JRadioButton)(e.getSource())).getText());
				np.remove(classes);
				np.add(classes=new BVChooser(this,BVClass.getClassNames(classesvect),BVChooser.Orientation.HORZONTAL));
				invalidate();
			}
			if (((Container)(e.getSource())).getParent().equals(classes)){
				new BVD(true,classesvect.get(classes.getSelectedIndex()));
				BVSelectedEvent.makeEvent(classesvect.get(classes.getSelectedIndex()),BVSelectedEvent.SelectedEventType.BLClassSelected);
			}
		}
		
	}
	

	
	public void itemSelected(ListSelectionEvent e) {
		// no List no selection
	}

	
	public void toBackground() {
		classes.clearSelection();
		if (jd!=null){
			jd.dispose();
			jd=null;
			new BVW("Ausleihe abgebrochen");
		}
	}

	public void thingSelected(BVSelectedEvent e) {
		new BVD(debug,classes.getSelectedIndex());
		
		if (e.getId().equals(BVSelectedEvent.SelectedEventType.BookFreeSelected)){
			
			if (classes.getSelectedIndex()==-1){
				BVSelectedEvent.addBVSelectedEventListener(new WaitingForBL(new BVBook(e.getEan())));
				jd = new JOptionPane("Bitte eine Klasse wählen (Jahrgang beachten)",JOptionPane.INFORMATION_MESSAGE).createDialog("Auswählen");
				jd.setModal(false);
				jd.setVisible(true);
			}else{
				makeLease(new BVBook(e.getEan()),classesvect.get((classes.getSelectedIndex())));
				
			}
		}else{
			
		}

	}
	class WaitingForBL implements BVSelectedEventListener{
		final BVBook book;
		public WaitingForBL(BVBook book) {
			this.book=book;
		}
		public void thingSelected(BVSelectedEvent e) {
			if (jd!=null){
				jd.dispose();
				jd=null;
			}
			if (e.getId()==BVSelectedEvent.SelectedEventType.BLClassSelected){
				makeLease(book,((BVClass)(e.getSource())));
				BVSelectedEvent.removeBVSelectedEventListener(this);
			}else{
				BVSelectedEvent.removeBVSelectedEventListener(this);
				new BVW("Ausleihe abgebrochen");
				
			}
			
		}
	}

	public void toClose() {
		// TODO Auto-generated method stub
		
	}


	public void makeLease(BVBook book, BVClass class1) {
		new BVBookLease(book,class1);
		
	}


	public Vector<SelectedEventType> getConsumedEvents() {
		return (new Vector<BVSelectedEvent.SelectedEventType>(){{
			add(BVSelectedEvent.SelectedEventType.BookFreeSelected);
			add(BVSelectedEvent.SelectedEventType.BLClassSelected);
		}});
	}


	public void actionPerformed(ActionEvent e) {
		
		
	}


	public void toFront() {
		new BVD(debug,"Hallo");
		new BVD(debug,years);
		np.removeAll();
		np.add(years = new BVChooser(np,BVClass.getYears(),BVChooser.Orientation.HORZONTAL));
		new BVD(debug,years);
		years.clickOn(0);
		years.invalidate();
		np.invalidate();
		invalidate();
		BVControl.getControl().gui.validate();
		// TODO Auto-generated method stub
		
	}


}
