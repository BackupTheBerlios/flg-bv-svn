package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;

public class VPVPropertyView extends JPanel implements UCCase, ActionListener{
	JButton save = new JButton("Einstellungen speichern");
	VPVPropertyView(){
		setLayout(new BorderLayout());
		add(new JPanel(){{
		
			setLayout(new GridLayout(1,1));
			add(Control.getControl().app_settings_pane,BorderLayout.CENTER);
			
		}});
		add(new JPanel(){{add(save);}},BorderLayout.SOUTH);
		save.addActionListener(this);
		
	}


	public void itemSelected(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}
	

	public void thingSelected(SelectedEvent e) {
		// TODO Auto-generated method stub

	}

	public void toBackground() {
		Control.getControl().app_settings_pane.focusLost(null);
		
		/*if (javax.swing.JOptionPane.showConfirmDialog(Control.getControl().gui,
				"Sollen die Extras �bernommen werden?",
				"Extras",
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


	//@Override
	public void actionPerformed(ActionEvent e) {
		Control.getControl().app_settings_pane.setProperties();
		Control.getControl().app_settings_pane.saveProperties();
		
	}

}
