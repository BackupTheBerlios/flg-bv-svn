package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.flg_informatik.utils.FLGProperties;

public class VEVJPSticker extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton save = new JButton("Einstellungen speichern");
	FLGProperties props;
	@SuppressWarnings("serial")
	VEVJPSticker(){
		setLayout(new BorderLayout());
		add(new JPanel(){{
			setLayout(new GridLayout(1,1));
			add(props=new FLGProperties(null,Control.getControl().propertyfilenameett,new File(Control.getControl().defaultfilenameett),Control.getControl().significantstringett));			
		}},BorderLayout.CENTER);
		add(new JPanel(){{add(save);}},BorderLayout.SOUTH);
		save.addActionListener(this);
		
	}


	
	//@Override
	public void actionPerformed(ActionEvent e) {
		props.setProperties();
		props.saveProperties();
		
	}

}
