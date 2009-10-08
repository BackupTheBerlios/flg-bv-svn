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


import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGJScrollPane;

public class VBLPVBookLeasePreView extends ATableView implements UCCase , ActionListener{
	private static boolean debug=true;
	private JPVYearChooser wp;
	private UsableBookPanel dp;
	private UsableBookPanel ep;
	private JPSrcollTable bvjp; 
	private DualPanel cp;
	private JPSelector bvs;
	private JDialog jd=null;
	private VBLPVBookLeasePreView me;
	private int lastselected;
	private VBTVDatamodell mymodell;
	
	
	public VBLPVBookLeasePreView() {
		setLayout(new BorderLayout());
		me = this;
		mymodell=new VBTVDatamodell(null);//VBPVDatamodell(null);
		add(wp=new JPVYearChooser(),BorderLayout.WEST);
		add(cp=new DualPanel(),BorderLayout.CENTER);
		add(new NorthPanel(),BorderLayout.NORTH);
	}
	private class NorthPanel extends JPanel implements ActionListener{
		JButton save = new JButton("Stundentafel speichern");
		NorthPanel(){
			super(new FlowLayout());
			bvs=new JPSelector((ActionListener)cp,OBUBookUse.getSubjects(),JPSelector.Orientation.HORZONTAL);
			add(bvs);
			add(save);
			save.addActionListener(this);
			
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(save)){
				new Deb(debug,bvs.getSelected());
				OClass.saveSubjects(wp.getSelectedClass().KID,bvs.getSelected());
				return;
			}
			
		}
	}
	private class DualPanel extends JSplitPane implements ActionListener{
		int dividerat=0;
		public DualPanel() {
			super(JSplitPane.HORIZONTAL_SPLIT,dp=new UsableBookPanel(),bvjp=new JPSrcollTable(me,mymodell));
					
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
				new Deb(debug,wp.getSelectedClass());
				
				for(String s:OClass.getSubjects((wp.getSelectedClass().KID))){
					bvs.clickOn(bvs.getNames().indexOf(s));
				}
			} catch (NullPointerException e) {
				new Deb(debug,e);
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
					new Deb(debug,c.getName());
					if (c.getName().equals(subject)){
						lines.remove(c);
						remove(c);
						break;
					}
					
				}
				
				
				
			}
			void addItem(String subject){
				for (Component c:getComponents()){
					new Deb(debug,c);
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
			if (OBUBookUse.getSubjects().contains(subject)){
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
					Control.getControl().newEvent(this, SelectedEvent.SelectedEventType.BookTypOnTop);
					jd2 = new JOptionPane("Bitte einen BuchTyp wählen!",JOptionPane.INFORMATION_MESSAGE).createDialog("Auswählen");
					jd2.setModal(false);
					jd2.setVisible(true);
					SelectedEvent.addBVSelectedEventListener(new WaitingForBT(subject, getGrade()));
					
					
				}
			}
			class WaitingForBT implements SelectedEventListener{
				String subject;
				String grade;
				
				
				public WaitingForBT(String subject, String grade) {
					this.subject=subject;
					this.grade=grade;
					//this.classindex=wp.classes.getSelectedIndex(); // must save for changing to BT
					//this.yearindex=wp.years.getSelectedIndex();
				}
				public void thingSelected(SelectedEvent e) {
					if (jd2!=null){
						jd2.dispose();
						jd2=null;
					}
					
					if (e.getId()==SelectedEvent.SelectedEventType.ISBNSelected){
						OBUBookUse.makeBookUse(e.getEan(),grade,subject);
						Control.getControl().newEvent(this, SelectedEvent.SelectedEventType.BLISBNSelected);
						return;
					}
					SelectedEvent.removeBVSelectedEventListener(this);
					// while((years.getSelectedIndex()==-1)); // waiting for foreground TODO may be a hanger
					wp.makeVisible();
					//wp.classes.clickOn(classindex);
					invalidate();
					MainGUI.val();
					
					
				}
			}
			
		}
		private Vector <BTColumn> getBTVector(String subject){
			Vector <BTColumn> ret = new Vector <BTColumn>();
			Vector <Ean> eans = new Vector <Ean>();
			eans=OBUBookUse.getISBN(subject, getGrade());
			
			for (Ean ean:eans){
				ret.add(new BTColumn(ean,OBTBookType.getTitle(ean)));
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
			new Deb(debug,cl);
			if ((cl.equalsIgnoreCase("J1")) | cl.equals("12")) return "J1"; 
			if ((cl.equalsIgnoreCase("J2")) | cl.equals("13")) return "J2"; 
			if (cl.substring(0, 2).equalsIgnoreCase("11")) return "10";
			if (cl.substring(0, 2).equalsIgnoreCase("10")) return "10";
			try {
				ret = Integer.valueOf(cl.substring(0, 1)).toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
			new Deb(debug,ret);
		return ret;
	}


	public void toBackground() {
		
		if (jd!=null){
			jd.dispose();
			jd=null;
			new Warn("Ausleihe abgebrochen");
		}
	}

	public void thingSelected(SelectedEvent e) {
		if (e.getId().equals(SelectedEvent.SelectedEventType.BookFreeSelected)){
			if (MainGUI.isSelectedView(this)){ // only active when on top
				if (wp.getSelectedClass()==null){
					SelectedEvent.addBVSelectedEventListener(new WaitingForBL(new OBook(e.getEan())));
					jd = new JOptionPane("Bitte eine Klasse wählen (Jahrgang beachten)",JOptionPane.INFORMATION_MESSAGE).createDialog("Auswählen");
					jd.setModal(false);
					jd.setVisible(true);
				}else{
					makeLease(new OBook(e.getEan()),wp.getSelectedClass());
					
				}
			}
		}else{
			if (e.getId().equals(SelectedEvent.SelectedEventType.BLClassSelected)){
				cp.makeVisible();
			}
			
		}

	}
	class WaitingForBL implements SelectedEventListener{
		final OBook book;
		public WaitingForBL(OBook book) {
			this.book=book;
		}
		public void thingSelected(SelectedEvent e) {
			if (jd!=null){
				jd.dispose();
				jd=null;
			}
			if (e.getId()==SelectedEvent.SelectedEventType.BLClassSelected){
				makeLease(book,((OClass)(e.getSource())));
				SelectedEvent.removeBVSelectedEventListener(this);
			}else{
				SelectedEvent.removeBVSelectedEventListener(this);
				new Warn("Ausleihe abgebrochen");
				
			}
			
		}
	}

	public void toClose() {
		// TODO Auto-generated method stub
		
	}


	public void makeLease(OBook book, OClass class1) {
		new OBLBookLease(book,class1);
		
	}


	public Vector<SelectedEventType> getConsumedEvents() {
		return (new Vector<SelectedEvent.SelectedEventType>(){{
			add(SelectedEvent.SelectedEventType.BookFreeSelected);
			add(SelectedEvent.SelectedEventType.BLClassSelected);
			add(SelectedEvent.SelectedEventType.BLISBNSelected);
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
