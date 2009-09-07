package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.management.MXBean;
import javax.security.auth.Subject;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;


import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGJScrollPane;

public class BVLeasePreView extends BVTableView implements BVView , ActionListener{
	private static boolean debug=true;
	private BVWestClass wp;
	private UsableBookPanel dp;
	private UsableBookPanel ep;
	private BVJPanel bvjp; 
	private DualPanel cp;
	private BVSelector bvs;
	private JDialog jd=null;
	private BVLeasePreView me;
	private int lastselected;
	private BVBookTypeDatamodell mymodell;
	
	
	public BVLeasePreView() {
		setLayout(new BorderLayout());
		me = this;
		mymodell=new BVBookTypeDatamodell(null);//BVPrepareDatamodell(null);
		add(wp=new BVWestClass(),BorderLayout.WEST);
		add(cp=new DualPanel(),BorderLayout.CENTER);
		add(new NorthPanel(),BorderLayout.NORTH);
	}
	private class NorthPanel extends JPanel implements ActionListener{
		JButton save = new JButton("Stundentafel speichern");
		NorthPanel(){
			super(new FlowLayout());
			bvs=new BVSelector((ActionListener)cp,BVBookUse.getSubjects(),BVSelector.Orientation.HORZONTAL);
			add(bvs);
			add(save);
			save.addActionListener(this);
			
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(save)){
				new BVD(debug,bvs.getSelected());
				BVClass.saveSubjects(wp.getSelectedClass().KID,bvs.getSelected());
				return;
			}
			
		}
	}
	private class DualPanel extends JSplitPane implements ActionListener{
		int dividerat=0;
		public DualPanel() {
			super(JSplitPane.HORIZONTAL_SPLIT,dp=new UsableBookPanel(),bvjp=new BVJPanel(me,mymodell));
					
		}

		public void makeVisible() {
			if (dividerat==0){ // start in the middle
				setDividerLocation(0.5);
			}
			dividerat=getDividerLocation();
			
			dp.makeVisible();
			//mymodell.remake(wp.getSelectedClass());
			bvjp.revalidate();
			
			setDividerLocation(dividerat);
			revalidate();
			
		}

		public void actionPerformed(ActionEvent e) {
			dp.actionPerformed(e);
			//ep.actionPerformed(e);
			
		}
	}
	
	private class UsableBookPanel extends JPanel implements ActionListener{
		Vector<BookTypLine> lines=new Vector<BookTypLine>();
		Subpanel sub;

		UsableBookPanel(){
			super(new BorderLayout());
		}
		
		public void makeVisible(){
			removeAll();
			bvs.clearSelection();
			add(new FLGJScrollPane(sub=new Subpanel()),BorderLayout.CENTER);
			try {
				new BVD(debug,wp.getSelectedClass());
				
				for(String s:BVClass.getSubjects((wp.getSelectedClass().KID))){
					bvs.clickOn(bvs.getNames().indexOf(s));
				}
			} catch (NullPointerException e) {
				new BVD(debug,e);
				// no Class selected
			}
			
			
		}
	
		class Subpanel extends JPanel{
			public Subpanel() {
				setLayout(new GridLayout(0,1,1,0));
				//removeAll();
				lines.removeAllElements();
				for (String subject:bvs.getSelected()){
					addItem(subject);
				}
			}
			void removeItem(String subject){
				for (Component c:getComponents()){
					new BVD(debug,c.getName());
					if (c.getName().equals(subject)){
						lines.remove(c);
						remove(c);
						break;
					}
					
				}
				
				
				
			}
			void addItem(String subject){
				for (Component c:getComponents()){
					new BVD(debug,c);
					if (c.getName().equals(subject)){
						return; // already here
					}
				}
				lines.add(new BookTypLine(subject));
				add(lines.lastElement(),subject);
				getComponent(getComponentCount()-1).setName(subject);
				return; 
			}	
		}
		public void actionPerformed(ActionEvent e) {
			if (((JCheckBox)(e.getSource())).isSelected()){
				sub.addItem((((JCheckBox)(e.getSource())).getText()));
			}else{
				sub.removeItem((((JCheckBox)(e.getSource())).getText()));
			}
			sub.invalidate();
		}		
			
		
	
		
	}
	private int maxwidth=10; // max width of BookTypLine._JLabel
	private class BookTypLine extends JPanel {
		private JDialog jd2=null;
		private Vector <BTColumn> btfields;
		BTColumn chooseline = new BTColumn((Ean) null, "Ein anderes Buch!");
		private class _JLabel extends JLabel { // to set kind of tabular
			public _JLabel(String subject){
				super (subject);
			}
			public Dimension getPreferredSize(){
				return new Dimension(maxwidth,getGraphics().getFontMetrics().getHeight());
			}
			
		}
		public BookTypLine(String subject) {
			super(new FlowLayout(FlowLayout.LEFT));
			if (BVBookUse.getSubjects().contains(subject)){
				_JLabel jlabel=new _JLabel(subject);
				add(jlabel);
				add(new BTSelector(subject,getBTVector(subject)));
				maxwidth=Math.max(maxwidth, getParent().getGraphics().getFontMetrics().stringWidth(subject));
			}

			
		}
		void getBooks(){
			
		}
		
		private class BTSelector extends javax.swing.JComboBox{
			String subject;
			BTSelector(String subject, Vector <BTColumn> btfields){
				
				super(btfields);
				
				addActionListener(this);
				this.subject=subject;
			}
			public void actionPerformed(ActionEvent e) {
				
				// super.actionPerformed(e);
				if (((BTSelector)(e.getSource())).getSelectedItem().equals(chooseline)){
					BVControl.getControl().newEvent(this, BVSelectedEvent.SelectedEventType.BookTypOnTop);
					jd2 = new JOptionPane("Bitte einen BuchTyp wählen!",JOptionPane.INFORMATION_MESSAGE).createDialog("Auswählen");
					jd2.setModal(false);
					jd2.setVisible(true);
					BVSelectedEvent.addBVSelectedEventListener(new WaitingForBT(subject, getGrade()));
					
					
				}
			}
			class WaitingForBT implements BVSelectedEventListener{
				String subject;
				String grade;
				
				
				public WaitingForBT(String subject, String grade) {
					this.subject=subject;
					this.grade=grade;
					//this.classindex=wp.classes.getSelectedIndex(); // must save for changing to BT
					//this.yearindex=wp.years.getSelectedIndex();
				}
				public void thingSelected(BVSelectedEvent e) {
					if (jd2!=null){
						jd2.dispose();
						jd2=null;
					}
					
					if (e.getId()==BVSelectedEvent.SelectedEventType.ISBNSelected){
						BVBookUse.makeBookUse(e.getEan(),grade,subject);
						BVControl.getControl().newEvent(this, BVSelectedEvent.SelectedEventType.BLISBNSelected);
						return;
					}
					BVSelectedEvent.removeBVSelectedEventListener(this);
					// while((years.getSelectedIndex()==-1)); // waiting for foreground TODO may be a hanger
					wp.makeVisible();
					//wp.classes.clickOn(classindex);
					invalidate();
					BVGUI.val();
					
					
				}
			}
			
		}
		private Vector <BTColumn> getBTVector(String subject){
			Vector <BTColumn> ret = new Vector <BTColumn>();
			Vector <Ean> eans = new Vector <Ean>();
			eans=BVBookUse.getISBN(subject, getGrade());
			
			for (Ean ean:eans){
				ret.add(new BTColumn(ean,BVBookType.getTitle(ean)));
			}
			ret.add(chooseline);
			return ret;
			
		}
		
		private class BTColumn{
			Ean isbn; String title; 
			BTColumn(Ean isbn, String title){
				this.isbn=isbn; this.title=title;
			}
			public String toString(){
				return this.title+" ("+isbn+" )";
			}
		}
	}

	
	public void itemSelected(ListSelectionEvent e) {
		// no List no selection
	}

	
	public String getGrade() {
			String ret=null;
			String cl=wp.getSelectedClass().Name;
			new BVD(debug,cl);
			if ((cl.equalsIgnoreCase("J1")) | cl.equals("12")) return "J1"; 
			if ((cl.equalsIgnoreCase("J2")) | cl.equals("13")) return "J2"; 
			if (cl.substring(0, 2).equalsIgnoreCase("11")) return "10";
			if (cl.substring(0, 2).equalsIgnoreCase("10")) return "10";
			try {
				ret = Integer.valueOf(cl.substring(0, 1)).toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
			new BVD(debug,ret);
		return ret;
	}


	public void toBackground() {
		
		if (jd!=null){
			jd.dispose();
			jd=null;
			new BVW("Ausleihe abgebrochen");
		}
	}

	public void thingSelected(BVSelectedEvent e) {
		if (e.getId().equals(BVSelectedEvent.SelectedEventType.BookFreeSelected)){
			
			if (wp.getSelectedClass()==null){
				BVSelectedEvent.addBVSelectedEventListener(new WaitingForBL(new BVBook(e.getEan())));
				jd = new JOptionPane("Bitte eine Klasse wählen (Jahrgang beachten)",JOptionPane.INFORMATION_MESSAGE).createDialog("Auswählen");
				jd.setModal(false);
				jd.setVisible(true);
			}else{
				makeLease(new BVBook(e.getEan()),wp.getSelectedClass());
				
			}
		}else{
			if (e.getId().equals(BVSelectedEvent.SelectedEventType.BLClassSelected)){
				cp.makeVisible();
			}
			
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
			add(BVSelectedEvent.SelectedEventType.BLISBNSelected);
		}});
	}


	public void actionPerformed(ActionEvent e) {
		
		
	}


	public void toFront() {
		if (lastselected==-1){ // no Selection yet
			bvjp.getTable().changeSelection(0, -1, false, false);
			bvjp.getTable().changeSelection(0, -1, true, false);
		}
		
		bvjp.getTable().invalidate();
		bvjp.validate();
		
				
	}

}
