package de.flg_informatik.utils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class CopyOfFLGProperties extends  Panel implements TextListener,FocusListener{
	/**
	 * 
	 */
	private Properties properties;
	private static final long serialVersionUID = 767010307826685780L;
	private JTabbedPane jtp =new JTabbedPane();
	private CopyOfFLGProperties me;
	private String infilename;
	private File defaultfile;
	private String significantkey;
	private ArrayList<String> groups;
	private ArrayList<ArrayList<String>> keys;
	private ArrayList<TabPanel.LinePanel> changelist = new ArrayList<TabPanel.LinePanel>();
	private boolean changed=false;
	/**
	 * @param (Properties properties) Properties als Objekt für die Rückgabe, wenn null, dann wird das Propertyfile (infile bzw defaultfile) geladen.
	 * @param (String infilename) Filename des Propertyfiles, das im Userverzeichnis (unix mit .dot) abgelegt ist/wird
	 * @param (File defaultfile) File, das, falls kein Propertyfile gefunden wird, als Template verwendet wird (muss mitgeliefert werden)
	 * @param (String significantkey) Propertykey an dem das File erkannt werden kann, dient zur Verifizierung
	 * 
	 * ansonsten gibt es die Methoden readProperties(), saveProperties() speichert unter infile (s.u.),boolean savePropertiesAs(String filename);	 
	 * */

	public CopyOfFLGProperties(java.util.Properties properties, String infilename, File defaultfile, String significantkey){
		super();
		this.infilename = infilename; 
		this.defaultfile = defaultfile;
		this.significantkey = significantkey;
		if (properties==null){
			switch(de.flg_informatik.utils.shell.OSShell.getOS()){
				case linux:
					this.infilename=System.getProperty("user.home")+System.getProperty("file.separator")+"."+this.infilename;
				break;
				default:
				break;	
			// infilename=infilename;
			}
			properties=loadPropertiesFromXML(this.infilename,defaultfile,significantkey);
		}
		debug(properties);
		this.properties=properties;
		debug(this.properties);
		this.me=this;
		this.setLayout(new BorderLayout(5,5));
		makeTabs(properties);
	}

	public Properties getProperties(){
		return properties;
	}
	
	public Properties readProperties(){
		return loadPropertiesFromXML(infilename, defaultfile, significantkey);
	}
	
	public boolean saveProperties(){
		
		return !(changed=!savePropertiesToXML(new File(infilename), properties, ""));
	}
	
	public boolean savePropertiesAs(String newfilename){
		return savePropertiesToXML(new File(newfilename), properties, "");
	}
	
	private void getGroups(Properties props){
		String keystring;
		String keykey;
		String groupstring;
		groups=new ArrayList<String>();
		keys=new ArrayList<ArrayList<String>>();
		groups.add("Allgemein");
		keys.add(new ArrayList<String>());
		for (Object obj: props.keySet()){
			keystring=obj.toString();
			keykey=keystring;
			if (keystring.indexOf(".")==0){
				keykey=keystring.substring(1, keystring.length());
			}
			if (keystring.indexOf("*")==0){
				keykey=keystring.substring(1, keystring.length());
			}
			if (!(keykey.indexOf(".")<0)&&(keykey.indexOf(".")<keykey.length())){
					groupstring=(keykey.substring(0, keykey.indexOf(".")));
					if (!groups.contains(groupstring)){
						groups.add(groupstring);
						keys.add(new ArrayList<String>());
						debug(keystring);
					}
					keys.get(groups.indexOf(groupstring)).add(keystring);
			}else{	
				keys.get(0).add(keystring);
			}
		}

		
	}
	private Properties loadPropertiesFromXML(String infilename, File defaultfile, String significantkey){
		File infile = new File(infilename);
		File getfile = new File(infilename);
		Properties props = new Properties();
		// Try infile
		try{
			try{
				FileInputStream fis =new FileInputStream(getfile);
				props.loadFromXML(fis);
				if (!props.containsKey(significantkey)){ // infile not valid
					javax.swing.JOptionPane.showMessageDialog(null, "Die gefundene Einstellungsdatei hat nicht die richtige Kennung (key:\""+significantkey+"\"!");
					throw new java.io.FileNotFoundException(); // like infile missing
				}
			}catch(java.io.FileNotFoundException e){
				// infile file missing
				javax.swing.JOptionPane.showMessageDialog(null, "Eine gültige Einstellungsdatei wurde nicht in ihrem Benutzerverzeichnis gefunden. Es werden die Standardeinstellungen geladen.");
				getfile=defaultfile;
				debug("loading defaults from: "+getfile.getAbsolutePath());
				try{
					FileInputStream fis =new FileInputStream(getfile);
					props.loadFromXML(fis);
				}catch(java.io.FileNotFoundException fnfe2){
					javax.swing.JOptionPane.showMessageDialog(null, "Konnte die Standardeinstellungen nicht laden, näheres in der Fehlerkonsole!");
				}
				if (!props.containsKey(significantkey)){
					javax.swing.JOptionPane.showMessageDialog(null, "Die Default-Einstellungsdatei hat nicht die richtige Kennung (key:\""+significantkey+"\"! Breche das Programm ab!");
					System.exit(1);
				}
				/* switch(DecisionBox.getLabel(this,"Einstellungsdatei",
				new String[]{"In Ihrem Benutzerverzeichnis gibt es keine",
					"Einstellungsdatei: (\""+infilename+"\")",
					"Was wollen Sie: Die Einstellungsdatei..."},
				new String[]{"... aus default neu erstellen?","...suchen?"})){
			case 1:{ */
						/* }
						case 2:{
							JFileChooser jfc = new javax.swing.JFileChooser();
							if(jfc.showOpenDialog(this)==javax.swing.JFileChooser.APPROVE_OPTION){
								getfile=jfc.getSelectedFile();
								infile=getfile;
							}
							break;
						}
					} */
		}
		}catch(java.util.InvalidPropertiesFormatException e){
			debug("Format of props not valid!");
		}
		catch(java.io.IOException e){
			e.printStackTrace();
		}
				
		if (getfile.getAbsolutePath().equals(defaultfile.getAbsolutePath())){
			// this is a very dirty hack for having a valid property set to save
			this.savePropertiesToXML(infile, props, "Saved as copy of "+ defaultfile.getAbsolutePath()+", "+new java.util.Date().toString());
		}
		
		return props;
	}
	
	private boolean savePropertiesToXML(File file, Properties props, String comment){
		if (file!=null){
			try{
				java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
				props.storeToXML(fos , comment);
				fos.close();
				return true;
			}catch(Exception e){
				e.printStackTrace(); // debug("exception");
			}
			debug(file);
			
			return false;
		}	
		return false;
	}
	
	private void makeTabs(Properties props){
		
		clearTabs();
		getGroups(props);
		for (String string: groups){
			jtp.addTab(string, new TabPanel(keys.get(groups.indexOf(string)).toArray(new String[keys.get(groups.indexOf(string)).size()])));
			jtp.addFocusListener(this);
			
		}
		this.add(jtp,BorderLayout.CENTER);
		
	}
	
	private void clearTabs(){
		jtp.removeAll();
	}
	

	public void setProperties(){
		if (!changelist.isEmpty()){
			for (TabPanel.LinePanel lpl : changelist){
				properties.setProperty(lpl.lbl.getText(), lpl.tfld.getText().trim());
			}
			changed=true;
			changelist.clear();
			javax.swing.JOptionPane.showMessageDialog(null, "Änderungen übernommen!", "Einstellungen",javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}
	}

	static private void debug(Object obj){
		System.out.println(CopyOfFLGProperties.class+": "+ obj);
	}
	
	class TabPanel extends Panel{
		/**
		 * Creates the list of Label-TextField pairs
		 * TextFields of dotted propkeys are set "setEditable(false)"
		 * and grayed out
		 * 
		 * TextFields of *propkeys are displayed as ****
		 */
		private static final long serialVersionUID = -4718591306709206094L;

		class LinePanel extends Panel{
			private static final long serialVersionUID = 4689741209659160762L;
			Label lbl;
			TextField tfld;
		}
		
		
		TabPanel(String[] propkeys){
			Panel inner=new Panel(new GridLayout(propkeys.length,1));
			this.setLayout(new FlowLayout());
			for (int i=0 ; i< propkeys.length; i++){
					//pnl.setSize(200,50);
					LinePanel pnl = new LinePanel();
					pnl.lbl = new Label(propkeys[i]);
					pnl.tfld = new TextField(properties.getProperty(propkeys[i]),30);
					if (propkeys[i].codePointAt(0)=='.'){
						pnl.tfld.setEditable(false);
						pnl.tfld.setBackground(java.awt.Color.LIGHT_GRAY);
					}
					if (propkeys[i].codePointAt(0)=='*'){
						pnl.tfld.setEchoChar('*');
						pnl.tfld.setBackground(java.awt.Color.GRAY);
					}
					pnl.add(pnl.lbl);
					pnl.add(pnl.tfld);
					pnl.tfld.addTextListener(me);
					inner.add(pnl);
					
			}
			JScrollPane	jsp = new JScrollPane(inner,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.add(jsp);
			this.validateTree();
		}
	}
	
	public boolean isChanged(){
		return changed;
	}

	
	
	/**
	 * EventListeners from Interfaces
	 * a) implemented
	 */
	
	public void textValueChanged(TextEvent e) {
		debug("textChange");
		if (!changelist.contains(((TabPanel.LinePanel)((TextField)e.getSource()).getParent()))){
			changelist.add((TabPanel.LinePanel)((TextField)e.getSource()).getParent());
		}
	}
	
	public void focusLost(FocusEvent e) {
		setProperties();
		
	}
	
	/**
	 * EventListeners from Interfaces
	 * b) Stubs
	 */
	

	public void focusGained(FocusEvent e) {
		// nothing to do
		
	}
	
}
