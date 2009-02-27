package de.flg_informatik.paedmlovpn;

import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Label;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Button;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

// import de.flg_informatik.utils.DecissionBox;
import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.shell.OSShell;
import de.flg_informatik.utils.shell.Osses;
// import de.flg_informatik.paedmlovpn.TwoButtonBox;
public class CopyOfStartVPNSetup090125 extends Panel implements ActionListener{
	/**
	 * 
	 */
	ArrayList a;
	private static final long serialVersionUID = 1L;
	private java.util.LinkedList[] mountdirs;
	private String username;
	private String host;
	private File infile=null;
	private File outfile=new File("ovpnproperties.xml");
	private File defaultfile=new File("ovpnproperties.xml");
	private Properties properties;
	private String[] osses;
	private CardLayout mclo = new CardLayout(20,20);
		
	static String setupfile = "StartVPNSetup.data";
	static StartVPNControl svpnctrl;
	CheckboxGroup oscbg = new CheckboxGroup();
	Checkbox clinux = new Checkbox(Osses.linux.osname,oscbg,false);
	Checkbox cwinnt = new Checkbox(Osses.winnt.osname,oscbg,false);
	Panel subpanel = new Panel();
	Panel buttons = new Panel(new GridLayout(0,4));
	Button save = new Button("speichern");
	Panel os= new Panel();
	static CopyOfStartVPNSetup090125 svpns=null;
	CopyOfStartVPNSetup090125(){
		this.properties=this.loadPropertiesFromXML("ovpnproperties.xml", defaultfile, "hostname");
		this.osses=properties.getProperty("osses").split(",");
		this.makeDisplay(properties);
		
	}
	
	private void makeDisplay(Properties props)
	{
		
		this.setLayout(mclo);
		Panel[] ospanel = new Panel[osses.length];
		for (int i=0; i< osses.length; i++)
			{ 
				this.add(ospanel[i]=new Panel(),Osses.valueOf(osses[i]).osname);
				ospanel[i].add(new Label(Osses.valueOf(osses[i]).osname));
				debug(Osses.valueOf(osses[i]).osname);
			}
		os.add(new Label("Betriebssystem"));
		os.add(cwinnt);
		os.add(clinux);
		os.setBackground(java.awt.Color.RED);
		os.add(buttons);
		switch(OSShell.getOS()){
			case linux:
				clinux.setState(true);
				break;
			case winnt:	
				cwinnt.setState(true);
				break;
		}
		
		this.add(os, "Allgemein");
		mclo.layoutContainer(this);
		mclo.show(this,"Allgemein");
		
		
		
		buttons.add(new Panel());
		buttons.add(save);
		buttons.add(new Panel());
		buttons.add(new Panel());
		save.addActionListener(this);

			
	}
	
	

	public void actionPerformed(ActionEvent e) {
		
		savePropertiesToXML(new File(properties.getProperty("propertyfilename")), properties, "paedmlovpn by NURH", false);
		//svpnctrl.svpn.mainCard();
	}

	public String getHost() {
		return host;
	}

	
	public String getUsername() {
		return username;
	}
	
	public java.util.LinkedList getMountDirs(Osses os){
		
		switch(os){
			case linux:
				String mtpath = properties.getProperty("linux.mountpath");
				String[] mtdirs = properties.getProperty("linux.mountpoints").split(",");
				java.util.LinkedList<String> ret=new java.util.LinkedList<String>();
				for (int i=0;i< mtdirs.length; i++){ret.add(mtpath+"/"+mtdirs[i]);}
				return ret;
				
			case winnt:
				break;
		}
		return new java.util.LinkedList();
	}
	
	public java.util.LinkedList getMountdirs(int osshell) {
		
		return mountdirs[osshell];
	}
	
	static private void debug(Object obj){
		System.out.println(CopyOfStartVPNSetup090125.class+": "+ obj);
	}
	static CopyOfStartVPNSetup090125 getEinstellungen(StartVPNControl svpnctrl){
		debug("getEinstellung");

		if (svpns == null){
			
			svpns=new CopyOfStartVPNSetup090125();
			}
			CopyOfStartVPNSetup090125.svpnctrl=svpnctrl;
	
		return svpns; 
	}
	public static void main(String[] args){
		FLGFrame frame = new FLGFrame();
		frame.add(new CopyOfStartVPNSetup090125());
		frame.setVisible(true);
		frame.validate();
		frame.pack();
	}
	private Properties loadPropertiesFromXML(String infilename, File defaultfile, String significantkey){
		switch(de.flg_informatik.utils.shell.OSShell.getOS()){
			case linux: {
				infilename=System.getProperty("user.home")+System.getProperty("file.separator")+"."+infilename;
				break;
			}
			default: {
				infilename=infilename;
			}
		}
		File infile = new File(infilename);
		File getfile = new File(infilename);
		Properties props = new Properties();
		while(!props.containsKey(significantkey)){
			try{FileInputStream fis =new FileInputStream(getfile);
				props.loadFromXML(fis);
			}catch(java.io.FileNotFoundException e){
				switch(DecissionBox.getLabel(this,"Einstellungsdatei",
							new String[]{"In Ihrem Benutzerverzeichnis gibt es keine",
								"Einstellungsdatei: (\""+infilename+"\")",
								"Was wollen Sie: Die Einstellungsdatei..."},
							new String[]{"... aus default neu erstellen?","...suchen?"})){
						case 1:{
							getfile=defaultfile;
							debug("loading defaults");
							break;
						}
						case 2:{
							JFileChooser jfc = new javax.swing.JFileChooser();
							if(jfc.showOpenDialog(this)==javax.swing.JFileChooser.APPROVE_OPTION){
								getfile=jfc.getSelectedFile();
								infile=getfile;
							}
							break;
						}
					}
			}catch(java.util.InvalidPropertiesFormatException e){
				debug("Format of props not valid!");
			}
			catch(java.io.IOException e){
				e.printStackTrace();
			}
			props.setProperty("propertyfilename", infile.getAbsolutePath());
			defaultfile=infile;
			
			
		}
		
		return props;
	}
	private boolean savePropertiesToXML(File file, Properties props, String comment, boolean askforfile){
		if (!askforfile && file!=null){
			try{
				java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
				props.storeToXML(fos , comment);
				fos.close();
			}catch(Exception e){
				e.printStackTrace(); // debug("exception");
			}
			debug(file);
			return true;
		}	
		return false;
	}

}
