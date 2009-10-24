package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.print.attribute.standard.MediaSize.Other;
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


import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGJScrollPane;

public class VBLVBookLeaseView extends JPanel implements UCCase , ActionListener{
	private static boolean debug=true;
	private JPVYearChooser wp;
	private OClass leaseclass;
	private CenterPanel cp;
	//private JPSelector bvs;
	private JDialog jd=null;
	JButton otherleaser = new JButton("Ausleihvorgang beenden");
	
	public VBLVBookLeaseView() {
		super(new BorderLayout());
		add(wp=new JPVYearChooser(),BorderLayout.WEST);
		add(cp=new CenterPanel(), BorderLayout.CENTER);
		add(new NorthPanel(),BorderLayout.NORTH);
	}
	private class NorthPanel extends JPanel implements ActionListener{
		
		NorthPanel(){
			super(new FlowLayout());
			add(otherleaser);
			otherleaser.addActionListener(this);
			otherleaser.setVisible(false);
			
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(otherleaser)){
				wp.unselectClass();
				otherleaser.setVisible(false);
				
				
				return;
			}
			
		}
	}

	private class CenterPanel extends JPanel{
	
		JPanel inner =new JPanel(new GridLayout(0,1));
		JPanel outer=new JPanel();

		CenterPanel(){
			super(new BorderLayout());
		}
		
		public void makeVisible(){
			//removeAll();
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
		wp.unselectClass();
		otherleaser.setVisible(false);
	}

	public void thingSelected(SelectedEvent e) {
		new Deb("thing selected"+e.getId());
		if (e.getId().equals(SelectedEvent.SelectedEventType.BookFreeSelected)){
			if (MainGUI.isSelectedView(this)){ // only active when on top
				if ( jd !=null){
			
					new Warn("Vorige Ausleihe abgebrochen\n keine Klasse gewählt!");
					jd=null;				
				}
				if (wp.getSelectedClass()==null){
					jd = new JOptionPane("Bitte eine Klasse wählen (Jahrgang beachten)",JOptionPane.INFORMATION_MESSAGE).createDialog("Auswählen");
					jd.setModal(false);
					jd.setVisible(true);
					SelectedEvent.addBVSelectedEventListener(new WaitingForBL(new OBook(e.getEan())));
				}else{
					makeLease(new OBook(e.getEan()),wp.getSelectedClass());
					
				}
			}
		}else{
			if (e.getId().equals(SelectedEvent.SelectedEventType.BLClassSelected)){
				cp.makeVisible();
				otherleaser.setVisible(true);
			}
			
		}

	}
	class WaitingForBL implements SelectedEventListener{
		final OBook book;
		public WaitingForBL(OBook book) {
			this.book=book;
		}
		public void thingSelected(SelectedEvent e) {
			SelectedEvent.removeBVSelectedEventListener(this);
			if (jd!=null){
				jd.dispose();
				jd=null;
			}
			if (e.getId()==SelectedEvent.SelectedEventType.BLClassSelected){
				makeLease(book,((OClass)(e.getSource())));
				
			}else{
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
		}});
	}


	public void actionPerformed(ActionEvent e) {
		
		
	}


	public void toFront() {
			cp.makeVisible();
			
		
		
		
		
	}


}
