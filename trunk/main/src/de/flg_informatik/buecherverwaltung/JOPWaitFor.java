/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;


/**
 * @author notkers
 *
 */
public class JOPWaitFor extends JOptionPane implements SelectedEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SelectedEventType setype;
	private JDialog jd; 

	/**
	 * @param message
	 * @param messageType
	 * @param selectedEventType 
	 */
	public JOPWaitFor(Object message, int messageType, SelectedEventType selectedEventType) {
		super(message, messageType);
		setype=selectedEventType;
		SelectedEvent.addBVSelectedEventListener(this);
		jd=createDialog("Bitte Auswählen");
		jd.setModal(false);
		jd.setVisible(true);
	}


	public void thingSelected(SelectedEvent e) {
		SelectedEvent.removeBVSelectedEventListener(this);
		switch (setype){
			case BLClassSelected:
					jd.dispose();
				break;
			default:
				
			
		}
		
	}
	void dispose(){
		if (jd!=null){
			jd.dispose();
		}
	}
	

}
