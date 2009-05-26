package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;

public class BVPropertyView extends BVView implements ActionListener {
	FireButton save = new FireButton("save properties to file");
	FLGProperties props;
	BVPropertyView(){
		setLayout(new FlowLayout());
		add(new Panel(){{
			setLayout(new GridLayout(0,1));
			add(props=new FLGProperties(BVControl.getControl().app_settings,BVControl.getControl().propertyfilename,new File(BVControl.getControl().defaultfilename),BVControl.getControl().significantstring));
			add(new Panel(){{
				//setLayout(new GridLayout(0,3)); 
				//add(new Panel()); 
				//add(new Panel());
				//add(new Panel(){{
				//	setLayout(new FlowLayout());
					add(save);
				//}});
			}});
		}});
		save.addActionListener(this);
	}

	@Override
	public void itemSelected(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}
	

	public void thingSelected(BVSelectedEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(save)){
			props.saveProperties();
			
		}
		// TODO Does'nt work yet
		
	}

	@Override
	void toBackground() {
		if (javax.swing.JOptionPane.showConfirmDialog(BVControl.getControl().gui,
				"Sollen die Einstellungen übernommen werden?",
				"Einstellungen",
				javax.swing.JOptionPane.YES_NO_OPTION
				)==javax.swing.JOptionPane.YES_OPTION){
			props.saveProperties();
			
		}
		// TODO Does'nt work yet
		
	}

}
