package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class VBLVBookLeaseView extends JPanel implements UCCase , ActionListener, BVConstants{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPVYearChooser wp;
	private JPBookPresenter np;
	@SuppressWarnings("unused")
	private CenterPanel cp;
	private JOPWaitFor jd=null;
	private OBook lastbook=null;
	private OBook book=null;
	public VBLVBookLeaseView() {
		super(new BorderLayout());
		add(np=new JPBookPresenter(lastbook,new JPConditionSwitcher(this,0),this),BorderLayout.NORTH);
		add(wp=new JPVYearChooser(),BorderLayout.WEST);
		add(cp=new CenterPanel(), BorderLayout.CENTER);
		
	}
	
	private class CenterPanel extends JPanel{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JPanel inner =new JPanel(new GridLayout(0,1));
		JPanel outer=new JPanel();

		CenterPanel(){
			super(new BorderLayout());
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
		
		if (book!=null){
			makeLease(book);
		}
		book=null;
		wp.unselectClass();
	}

	public void thingSelected(SelectedEvent e) {
		if (e.getId().equals(SelectedEvent.SelectedEventType.BookFreeSelected)){
			if (MainGUI.isSelectedView(this)){ // only active when on top
				book=new OBook(e.getEan()); 
				if (book.equals(lastbook)){ // do nothing but increment
					book.incCondition();
				}else{
					if (wp.getSelectedClass()==null){
						if ( jd ==null){ // there should be a Class selected
							jd = new JOPWaitFor("Bitte eine Klasse wählen (Jahrgang beachten)",JOptionPane.INFORMATION_MESSAGE,SelectedEvent.SelectedEventType.BLClassSelected);
						}
						
					}else{
						jd=null;
						makeLease(lastbook);
					}
				}
				
				lastbook=book;
				np.publish(lastbook);
			}

		}
	}	
	
	
	public void toClose() {
		
	}


	public void makeLease(OBook book) {
		
		if(jd!=null){
			jd.dispose();
		}
		if (wp.getSelectedClass()==null){
			new Warn("Vorige Ausleihe abgebrochen!\n keine Klasse gewählt!");
			Control.logln("ABBRUCH der Ausleihe: (" + lastbook.ID + ", " + OBTBookType.getTitle(Ean.getEan(lastbook.ISBN))+", "+lastbook.Scoring_of_condition+"): No class chosen") ;
		}else{
			if (book!=null){
				Ean kean=Ean.getEan(new BigInteger(wp.getSelectedClass().KID).add(OClass.Class12));
				Control.log("LEIHE: B"+book.ID + " -> K" +kean.toString().substring(8, 12) + " ("+ OBTBookType.getTitle(Ean.getEan(book.ISBN))+" an " + OClass.getBVClass(kean).Name +") Zustand: "+book.Scoring_of_condition );
				if(book.makeLease(kean)){
					Control.logln(" OK!");
				}else{
					Control.logln(" Fehler!");
					
				}
			}else{
				wp.unselectClass();
			}
		}
		lastbook=null;
		
		
	}


	@SuppressWarnings("serial")
	public Vector<SelectedEventType> getConsumedEvents() {
		return (new Vector<SelectedEvent.SelectedEventType>(){{
			add(SelectedEvent.SelectedEventType.BookFreeSelected);
			add(SelectedEvent.SelectedEventType.BLClassSelected);
		}});
	}


	public void actionPerformed(ActionEvent e) {
		if (lastbook!=null){
			for (int i=0; i<6; i++ ){
				if(e.getActionCommand().equals((i+1)+"")){
					lastbook.Scoring_of_condition=i+1;
					np.publish(lastbook);
				}
			}
		}
		if (e.getActionCommand().equals("end")){
				toBackground(); 
				np.publish(lastbook);
		}
		if (e.getActionCommand().equals("cancel")){
			if (lastbook!=null){
				Control.logln("ABBRUCH der Ausleihe: (" + lastbook.ID + ", " + OBTBookType.getTitle(Ean.getEan(lastbook.ISBN))+", "+lastbook.Scoring_of_condition+"): Benutzeraktion") ;
				lastbook=null;
			}
			book=null;
			wp.unselectClass();
			np.publish(book);
		}
	}


	public void toFront() {
			np.publish(null);
		
			
		
		
		
		
	}


}
