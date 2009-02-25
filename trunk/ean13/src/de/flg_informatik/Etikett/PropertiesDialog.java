package de.flg_informatik.Etikett;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JDialog;

import de.flg_informatik.utils.FLGProperties;

public class PropertiesDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private FLGProperties propertiespanel;
	private static Properties properties=new Properties();
	javax.swing.JButton cmdOk;
	
	public PropertiesDialog (Properties properties, String infilename, File defaultfile, String significantkey) {
		super ((java.awt.Frame) null, "Etikettdruckeinstellungen", true);
		this.setLayout(new java.awt.BorderLayout ());
		propertiespanel = new FLGProperties(properties,infilename, defaultfile, significantkey);
		cmdOk = new javax.swing.JButton ("Speichern");
		cmdOk.addActionListener(this);
		
		this.add(propertiespanel, java.awt.BorderLayout.CENTER);
		this.add(cmdOk, java.awt.BorderLayout.SOUTH);
		this.pack();
	}
	
	public void actionPerformed(ActionEvent evnt) {
		if (evnt.getSource() == cmdOk) {
			
			propertiespanel.saveProperties();
			PropertiesDialog.properties=this.propertiespanel.getProperties();
			this.setVisible(false);
		}
	}
	
	public static Properties showSettingsDialog (Properties properties, String infilename, File defaultfile, String significantkey) {
		PropertiesDialog dlg = new PropertiesDialog (properties, infilename,defaultfile,significantkey);
		dlg.setVisible(true);
		return PropertiesDialog.properties;
	}

	
}
