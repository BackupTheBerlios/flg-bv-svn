package de.flg_informatik.utils;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.TextEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class FLGProperties extends  JPanel implements FocusListener{
	/**
	 * 
	 */
	private Properties properties;
	private static final long serialVersionUID = 767010307826685781L;
	private JTabbedPane jtp =new JTabbedPane();
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

	public FLGProperties(java.util.Properties properties, String infilename, File defaultfile, String significantkey){
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
					this.infilename=System.getProperty("user.home")+this.infilename;
				break;	
			}
			properties=loadPropertiesFromXML(this.infilename,defaultfile,significantkey);
		}
		debug(properties);
		this.properties=properties;
		this.addFocusListener(this);
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
		//setProps();
		return savePropertiesToXML(new File(infilename), properties, "");
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
		while(!props.containsKey(significantkey)){
			try{FileInputStream fis =new FileInputStream(getfile);
				props.loadFromXML(fis);
				if(!props.containsKey(significantkey)){
					 new FileInputStream("");
				}
			}catch(java.io.FileNotFoundException e){
				switch (javax.swing.JOptionPane.showOptionDialog(null, "Eine gültige Einstellungsdatei wurde nicht in \nden Anwendungseinstellungen/ihrem Benutzerverzeichnis gefunden.\n Sollen die Standardeinstellungen geladen werden?",
                       "Extras laden",
                        javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                        javax.swing.JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Ja","Extras suchen", "Programm abbrechen"},
                        null)){
                    case javax.swing.JOptionPane.YES_OPTION:
                    	getfile=defaultfile;
                    	break;
                    case javax.swing.JOptionPane.NO_OPTION:
                    	javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
                    	jfc.setFileHidingEnabled(false);
   	                   	if(jfc.showOpenDialog(this)==javax.swing.JFileChooser.APPROVE_OPTION){
							getfile=jfc.getSelectedFile();
							
							if (javax.swing.JOptionPane.showConfirmDialog(getParent(), 
									"Soll die Parameterdatei am Standardplatz in \nden Anwendungseinstellungen/ihrem Benutzerverzeichnis gespeichert werden?",
									"Lokalisierung", 
									javax.swing.JOptionPane.YES_NO_OPTION)==javax.swing.JOptionPane.NO_OPTION){
								infile=getfile;
								this.infilename=infile.getAbsolutePath();
							}
						}else{
							new FLGUE("Abbruch durch Benutzer!\n");
							System.exit(2);
						}
						break;
                    case javax.swing.JOptionPane.CANCEL_OPTION:
                    	new FLGUE("Abbruch durch Benutzer!\n");
						System.exit(2);
          	 
					}
              
				debug("loading defaults from: "+getfile.getAbsolutePath());
				try{
					FileInputStream fis =new FileInputStream(getfile);
					props.loadFromXML(fis);
				}catch(java.io.FileNotFoundException fnfe2){
					new FLGUE("Defaultfile: "+getfile.getAbsolutePath()+" not found!\n");
					System.exit(1);
				} catch (InvalidPropertiesFormatException ipfe) {
					new FLGUE("Defaultfile: "+getfile.getAbsolutePath()+" has wrong format!\n");
					System.exit(1);
				} catch (IOException ioe) {
					new FLGUE("Defaultfile: "+getfile.getAbsolutePath()+" I/O error!\n");
					System.exit(1);
				}
				if (!props.containsKey(significantkey)){
					javax.swing.JOptionPane.showMessageDialog(null, "Die Default-Einstellungsdatei hat nicht die richtige Kennung (key:\""+significantkey+"\"! Breche das Programm ab!");
					System.exit(1);
				}
			}catch(java.util.InvalidPropertiesFormatException e){
				debug("Format of props not valid!");
			}
			catch(java.io.IOException e){
				e.printStackTrace();
			}
		}
		if (getfile.getAbsolutePath().equals(defaultfile.getAbsolutePath())){
			// localize default file
			this.savePropertiesToXML(infile, props, "Saved as copy of "+ defaultfile.getAbsolutePath()+", "
					+new java.util.Date().toString());
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
		JPanel panel;
		clearTabs();
		getGroups(props);
		for (String string: groups){
			panel=new TabPanel(keys.get(groups.indexOf(string)).toArray(new String[keys.get(groups.indexOf(string)).size()]));
			jtp.addTab(string, panel);
			
			
		}
		jtp.addFocusListener(this);
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
			javax.swing.JOptionPane.showMessageDialog(null, "Änderungen übernommen!", "Extras",javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}
	}

	static private void debug(Object obj){
		//System.out.println(FLGProperties.class+": "+ obj);
	}
	
	class TabPanel extends JPanel{
		/**
		 * Creates the list of JLabel-JTextField pairs
		 * JTextFields of dotted propkeys are set "setEditable(false)"
		 * and grayed out
		 * 
		 * TextFields of *propkeys are displayed as ****
		 */
	
		
		private static final long serialVersionUID = -4718591306709206094L;
		
	
		/* 
		 * A small hack to get handling easy
		 * calls(!) local void textValueChanged(TextEvent e)
		 * 
		 */
		class CBJTextField extends JTextField implements DocumentListener{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			CBJTextField(String title){
				super(title);
				getDocument().addDocumentListener(this);
			}

			public void changedUpdate(DocumentEvent e) {
				textValueChanged(new TextEvent(this,TextEvent.TEXT_VALUE_CHANGED));
				
			}

			public void insertUpdate(DocumentEvent e) {
				textValueChanged(new TextEvent(this,TextEvent.TEXT_VALUE_CHANGED));
				
			}
			
			public void removeUpdate(DocumentEvent e) {
				textValueChanged(new TextEvent(this,TextEvent.TEXT_VALUE_CHANGED));
				
			}
			
		}
		class CBJPasswordField extends JPasswordField implements DocumentListener{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			CBJPasswordField(String title){
				super(title);
				getDocument().addDocumentListener(this);
			}

			public void changedUpdate(DocumentEvent e) {
				textValueChanged(new TextEvent(this,TextEvent.TEXT_VALUE_CHANGED));
				
			}

			public void insertUpdate(DocumentEvent e) {
				textValueChanged(new TextEvent(this,TextEvent.TEXT_VALUE_CHANGED));
				
			}
			public void removeUpdate(DocumentEvent e) {
				textValueChanged(new TextEvent(this,TextEvent.TEXT_VALUE_CHANGED));
				
			}
		}
	

		class LinePanel extends JPanel{
			private static final long serialVersionUID = 4689741209659160762L;
			JLabel lbl;
			JTextField tfld;
		}
		
		
		TabPanel(String[] propkeys){
			JPanel inner =new JPanel(new GridLayout(0,1));
			JPanel outer=new JPanel();
			// JScrollPane	jsp = new JScrollPane(outer);
			setLayout(new BorderLayout());
			outer.add(inner);
			this.add(new JScrollPane(outer),BorderLayout.CENTER);
			for (int i=0 ; i< propkeys.length; i++){
					LinePanel pnl = new LinePanel();
					pnl.setLayout(new GridLayout(1,2,8,2));
					pnl.lbl = new JLabel(propkeys[i]);
					
					if (propkeys[i].codePointAt(0)=='*'){
						pnl.tfld = new CBJPasswordField(properties.getProperty(propkeys[i]));
						pnl.tfld.setBackground(java.awt.Color.GRAY);
					}else{
						pnl.tfld = new CBJTextField(properties.getProperty(propkeys[i]));
					}
					
					if (propkeys[i].codePointAt(0)=='.'){
						pnl.tfld.setEditable(false);
						pnl.tfld.setBackground(java.awt.Color.LIGHT_GRAY);
						pnl.tfld.setToolTipText("This can only be changed in the *.xml file");
						
					}
					
					pnl.add(pnl.lbl);
					pnl.add(pnl.tfld);
					inner.add(pnl);
					
			}
			
			
		}
	}
	
	private void textValueChanged(TextEvent e) {
		debug("textChange");
		if (!changelist.contains(((TabPanel.LinePanel)((JTextField)e.getSource()).getParent()))){
			changelist.add((TabPanel.LinePanel)((JTextField)e.getSource()).getParent());
		}
		changed=true;
	}
	
	
	public boolean isChanged(){
		return changed;
	}

		
	/**
	 * EventListeners from Interfaces
	 * a) implemented
	 */
	
		
	public void focusLost(FocusEvent e) {
		setProperties();
	}
	
	/**
	 * EventListeners from Interfaces
	 * b) Stubs
	 */
	

	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * For testing, modifying,
	 * can be used to generate a modified localized file
	 * or for debugging
	 * @param (String infilename) file to be read, if not existing localized file to be written to
	 * @param (String defaultfilename) file containing default(starter-)file, must exist if infilename doesn't
	 * @param (String significantkey) key for verifying correct file, just one existing key
	 */
	public static void main(String[] args){
		FLGProperties properties;
		properties= new FLGProperties(null,args[0],new File(args[1]),args[2]);
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.getContentPane().add(properties);
		jf.setVisible(true);
		jf.pack();
		}
	class mfc extends javax.swing.JFileChooser
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void test(){
			//this.;
			
		}
		
	}

  

	
	
}