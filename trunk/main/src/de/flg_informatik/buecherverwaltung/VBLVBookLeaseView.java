package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;

public class VBLVBookLeaseView extends JPanel implements UCCase , ActionListener{
	private static boolean debug=true;
	private JPVYearChooser wp;
	private JPBookPresenter np;
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
							jd = new JOPWaitFor("Bitte eine Klasse w�hlen (Jahrgang beachten)",JOptionPane.INFORMATION_MESSAGE,SelectedEvent.SelectedEventType.BLClassSelected);
						}
						
					}else{
						jd=null;
						makeLease(lastbook);
					}
				}
				
				lastbook=book;
				np.publish(lastbook);
			}else{
				if (e.getId().equals(SelectedEvent.SelectedEventType.BLClassSelected)){
					
				}
				
			}

		}
	}	
	
	private void cancelled(String text){
		new Warn(text);
		jd=null;
		lastbook=null;
	}

	public void toClose() {
		
	}


	public void makeLease(OBook book) {
		
		if(jd!=null){
			jd.dispose();
		}
		if (wp.getSelectedClass()==null){
			cancelled("Vorige Ausleihe abgebrochen!\n keine Klasse gew�hlt!");
			lastbook=null;
			return;
		}
		new OBLBookLease(book,wp.getSelectedClass());
		lastbook=null;
	}


	public Vector<SelectedEventType> getConsumedEvents() {
		return (new Vector<SelectedEvent.SelectedEventType>(){{
			add(SelectedEvent.SelectedEventType.BookFreeSelected);
			add(SelectedEvent.SelectedEventType.BLClassSelected);
		}});
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")){
			wp.unselectClass();
			return;
		}
		
	}


	public void toFront() {
			np.publish(null);
		
			
		
		
		
		
	}


}
