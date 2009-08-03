package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;



import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVLeasePreView extends JPanel implements BVView , ActionListener{
	private static boolean debug=true;
	private Vector<BVClass> classesvect = null;  
	private BVChooser years=new BVChooser();
	private BVChooser classes=new BVChooser();
	private NorthPanel np;
	private CenterPanel cp;
	private JPanel sp;
	private JDialog jd=null;
	public BVLeasePreView() {
		setLayout(new BorderLayout());
		add(np=new NorthPanel(),BorderLayout.NORTH);
		add(cp=new CenterPanelPre(), BorderLayout.CENTER);
		add(sp=new HFill(), BorderLayout.SOUTH);
		setVisible(true);
	}
	private class NorthPanel extends JPanel implements ActionListener{
		
		JPanel sub=new JPanel();
		NorthPanel(){
			add(years);
			add(classes);
			
			validate();
		}

		public void actionPerformed(ActionEvent e) {
			new BVD(debug,e);
			
			if (((Container)(e.getSource())).getParent().equals(years)){
				classesvect=BVClass.getClasses(((JRadioButton)(e.getSource())).getText());
				np.remove(classes);
				np.add(classes=new BVChooser(this,BVClass.getClassNames(classesvect),BVChooser.Orientation.HORZONTAL));
				np.invalidate();
				
			}
			if (((Container)(e.getSource())).getParent().equals(classes)){
				new BVD(true,classesvect.get(classes.getSelectedIndex()));
				BVSelectedEvent.makeEvent(classesvect.get(classes.getSelectedIndex()),BVSelectedEvent.SelectedEventType.BLClassSelected);
				cp.makeVisible();
				cp.invalidate();
				BVGUI.val();
			}
			
		}
		
	}
	private class CenterPanel extends JPanel{
		void makeVisible(){};
	}
	private class CenterPanelPre extends CenterPanel implements ActionListener{
		Vector<String> subjects=BVBookUse.getSubjects();
		Vector<BookTypLine> lines=new Vector<BookTypLine>();
		JButton save = new JButton("Stundentafel speichern");
		BVSelector bvs;
		JScrollPane sp=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED){
		};
		JScrollPane sp1=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel container=new JPanel(new GridLayout(1,2)){
		/*	public Dimension getPreferredSize() {
				Container me=this.getParent();
				if (me==null){
					return new Dimension(10,10);
				}
				return new Dimension(me.getSize().width-me.getInsets()
					.left-me.getInsets().right-10,me.getSize().height-me.getInsets()
					.top-me.getInsets().bottom-10);
			}*/
		};
		
		Subpanel sub;
		Subpanel sub1;
		CenterPanelPre(){
			save.addActionListener(this);
		}
		public void makeVisible(){
			removeAll();
			add(bvs=new BVSelector(this,subjects,BVSelector.Orientation.HORZONTAL));
			add(new HFill());
			add(new HFill());
			add(save);
			add(new HFill());
			sp.setViewportView(sub=new Subpanel());
			sp1.setViewportView(sub1=new Subpanel());
			for(String s:BVClass.getSubjects((BVClass.getClasses(years.getSelected()).get(classes.getSelectedIndex()).KID))){
				new BVD(debug,s);
				bvs.clickOn(bvs.getNames().indexOf(s));
			}
			container.add(sp);
			container.add(sp1);
			add(container);
			container.invalidate();
			invalidate();
			BVGUI.val();
			
			
			
		}
		class Subpanel extends JPanel{
			public Subpanel() {
				//setLayout(new GridLayout(0,1,20,5));
				//removeAll();
				lines.removeAllElements();
				for (String subject:bvs.getSelected()){
					addItem(subject); add(new HFill());
				}
				setLayout(new GridLayout(bvs.getSelected().size(),1,20,5));
				invalidate();
				cp.invalidate();
				BVLeasePreView.this.revalidate();
			}
			/* public Dimension getPreferredSize() {
				Container me=this.getParent();
				if (me==null){
					return new Dimension(10,10);
				}
				return new Dimension(me.getSize().width-me.getInsets()
					.left-me.getInsets().right-10,me.getSize().height-me.getInsets()
					.top-me.getInsets().bottom-10);
			}*/
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
				BookTypLine btl=new BookTypLine(subject);
				lines.add(btl);
				add(btl,subject);
				getComponent(getComponentCount()-1).setName(subject);
				return; 
			}	
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(save)){
				new BVD(debug,bvs.getSelected());
				BVClass.saveSubjects(BVClass.getClasses(years.getSelected()).get(classes.getSelectedIndex()).KID,bvs.getSelected());
				return;
			}
			if (((JCheckBox)(e.getSource())).isSelected()){
				sub.addItem((((JCheckBox)(e.getSource())).getText()));
			}else{
				sub.removeItem((((JCheckBox)(e.getSource())).getText()));
			}
			sub.invalidate();
			validate();
		}		
			
		
	
		
	}
	private class BookTypLine extends JPanel {
		private JDialog jd2=null;
		private Vector <BTColumn> btfields;
		BTColumn chooseline = new BTColumn((Ean) null, "Ein anderes Buch!");
		public BookTypLine(String subject) {
			super();
			if (BVBookUse.getSubjects().contains(subject)){
				add(new JLabel(subject));
				add(new BTSelector(subject,getBTVector(subject)));
				
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
				int classindex;
				int yearindex;
				
				public WaitingForBT(String subject, String grade) {
					this.subject=subject;
					this.grade=grade;
					this.classindex=classes.getSelectedIndex(); // must save for changing to BT
					this.yearindex=years.getSelectedIndex();
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
					years.clickOn(yearindex);
					classes.clickOn(classindex);
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
			String cl=classesvect.get(classes.getSelectedIndex()).Name;
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
		classes.clearSelection();
		if (jd!=null){
			jd.dispose();
			jd=null;
			new BVW("Ausleihe abgebrochen");
		}
	}

	public void thingSelected(BVSelectedEvent e) {
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
			add(BVSelectedEvent.SelectedEventType.BLISBNSelected);
		}});
	}


	public void actionPerformed(ActionEvent e) {
		
		
	}


	public void toFront() {
		new BVD(debug,"Hallo");
		new BVD(debug,years);
		cp.removeAll();
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
