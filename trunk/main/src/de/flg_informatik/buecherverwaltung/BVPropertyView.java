package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;

public class BVPropertyView extends JPanel implements BVView{
	FireButton save = new FireButton("save properties to file");
	BVPropertyView(){
		//setLayout(new FlowLayout());
		//add(new Panel(){{
			setLayout(new GridLayout(1,1));
			add(BVControl.getControl().app_settings_pane);
			
		//}});
		
	}


	public void itemSelected(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}
	

	public void thingSelected(BVSelectedEvent e) {
		// TODO Auto-generated method stub

	}

	public void toBackground() {
		BVControl.getControl().app_settings_pane.focusLost(null);
		
		/*if (javax.swing.JOptionPane.showConfirmDialog(BVControl.getControl().gui,
				"Sollen die Einstellungen übernommen werden?",
				"Einstellungen",
				javax.swing.JOptionPane.YES_NO_OPTION
				)==javax.swing.JOptionPane.YES_OPTION){
			props.saveProperties();
			
		}
		*/
		// TODO Does'nt work yet
		
	}

	public void toClose() {
		// TODO Auto-generated method stub
		
	}


	public Vector<SelectedEventType> getConsumedEvents() {
		// TODO Auto-generated method stub
		return null;
	}


	public void toFront() {
		// TODO Auto-generated method stub
		
	}

}
