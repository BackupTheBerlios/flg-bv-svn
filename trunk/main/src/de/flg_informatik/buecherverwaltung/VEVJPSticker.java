package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;

public class VEVJPSticker extends JPanel implements ActionListener{
	JButton save = new JButton("Einstellungen speichern");
	FLGProperties props;
	VEVJPSticker(){
		setLayout(new BorderLayout());
		add(new JPanel(){{
			setLayout(new GridLayout(1,1));
			add(props=new FLGProperties(null,Control.getControl().propertyfilenameett,new File(Control.getControl().defaultfilenameett),Control.getControl().significantstringett));			
		}},BorderLayout.CENTER);
		add(new JPanel(){{add(save);}},BorderLayout.SOUTH);
		save.addActionListener(this);
		
	}


	
	@Override
	public void actionPerformed(ActionEvent e) {
		props.setProperties();
		props.saveProperties();
		
	}

}
