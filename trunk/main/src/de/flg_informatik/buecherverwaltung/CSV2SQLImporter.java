package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.utils.FLGFrame;

public class CSV2SQLImporter extends JPanel {
	//TODO
	
	private String tablename="Customers";
	private Container container=null;
	private static String stablename="Customers";
	private static Container scontainer=null;
	private static final boolean debug=false;
	private static final String unknownfield="@unknown";
	private static final String nullfield="@setnull";
	private static final String ignorefield="@ignore";
	private static Pattern delimited=Pattern.compile("(\\A\".*\"\\z)");
	private boolean isdelimited;
	private Vector <String> sqlfields;
	private Vector <FileColumn> filefields;
	private Vector <JCheckBox> emptynull = new Vector<JCheckBox>();
	private Vector <FieldSelector> filecombo= new Vector <FieldSelector>();
	private File infile=null;
	private static FileColumn unknowncolumn;
	private static FileColumn nullcolumn;
	private static FileColumn ignorecolumn;
	private static final String fieldseparator =",";
	private JButton OK = new JButton("OK");
	private JButton load = new JButton("File Importieren");
	private char[] trenner;
	private FileReader inreader = null;
	private NorthPanel np=null;
	private SouthPanel sp=null;
	private JScrollPane cp=null;
	
	public CSV2SQLImporter(String tablename, Container container){
		setLayout(new BorderLayout());
		stablename=tablename;
		scontainer=container;
		preinit();
		init();
	}
	public CSV2SQLImporter(){
		setLayout(new BorderLayout());
		
	}
	public boolean init(){
		if ( scontainer == null | stablename==null){
			throw new java.lang.InternalError("Initialisierungsfehler in: "+this.getClass());
		}
		container=scontainer;
		tablename=stablename;
		add(np=new NorthPanel(),BorderLayout.NORTH);
		add(cp=new JScrollPane(),BorderLayout.CENTER);// Dummy
		add(sp=new SouthPanel(),BorderLayout.SOUTH);
		OK.addActionListener(sp);
		validate();
		setVisible(true);
		return true;
	}
	public static void setTablename(String tablename){
		stablename=tablename;
	}
	public static void setContainer(Container container){
		scontainer=container;
	}
	public void preinit(){
		
	}
	public void postinit(){
		
	}
	private boolean openFile(){
		if (!debug){
			if  ((infile=FileDialog.getFileOpen((Frame)container))==null){
				return false;
			}
		}else{
			infile=new File("/home/notkers/test.csv");
			
		}
		if (infile.canRead()){
			return true;
		}else{
			return false;
		}
	}
	
	private class MySQLcolumns extends JPanel{
		private FieldSelector fs=null;
		private JCheckBox en=null;
		Vector<Boolean> notNullColumns = new Vector<Boolean>(); 
		public MySQLcolumns() {
			sqlfields=BVUtils.getColumnHeaders(tablename);
			notNullColumns=BVUtils.getNotNullColumns(tablename);
			setLayout(new GridLayout(sqlfields.size(),3));
			for (int i= 0; i<sqlfields.size();i++){
				fs=new FieldSelector(sqlfields.get(i));
				filecombo.add(fs);
				add(fs); 
				add(new JLabel(" => "+sqlfields.get(i)));
				new BVD(debug,sqlfields.get(i));
				en=new JCheckBox("wenn leer, dann MYSQL-NULL",!notNullColumns.get(i));
				emptynull.add(en);
				add(en);
				
			}
			setVisible(true);
		}
	
		
		
	}
	private class FieldSelector extends javax.swing.JComboBox{
		FieldSelector(String msqlc){
			super(filefields);
			getModel().setSelectedItem(getGuess(msqlc));
			
						
		}
		
	}

	private class NorthPanel extends JPanel {
		JTextField fsep= new JTextField(fieldseparator,0){{setBorder(null);setBackground(Color.WHITE);this.setFont(new java.awt.Font(this.getFont().toString(),java.awt.Font.BOLD,(int)(this.getFont().getSize()*1.5)));}};
		ButtonGroup bg =new ButtonGroup();
		JRadioButton CO = new JRadioButton("\",\"");
		JRadioButton SC = new JRadioButton("\";\"");
		JRadioButton FS = new JRadioButton("=>");
		JCheckBox CR = new JCheckBox("CR=\"\\r\"");
		JCheckBox LF = new JCheckBox("LF=\"\\n\"");
		JCheckBox QM = new JCheckBox();
		
		NorthPanel(){
			super(new GridLayout(1,4));
			bg.add(CO);
			bg.add(SC);
			bg.add(FS);
			add(new JPanel(){{add(new JLabel("Feldtrenner:"));add(CO);add(SC);add(FS);add(fsep);}});
			add(new JPanel(){{add(new JLabel("Zeilentrenner:")); add(CR);add(LF);}});
			add(new JPanel(){{add(new JLabel("\" \" als opt. Textauszeichner:")); add(QM);;}});
			CO.setSelected(true);
			if(System.getProperty("line.separator").equals("\n")){
				CR.setSelected(false);
				LF.setSelected(true);
			}
			if(System.getProperty("line.separator").equals("\r\n")){
				CR.setSelected(true);
				LF.setSelected(true);
			}
			if(System.getProperty("line.separator").equals("\r")){
				CR.setSelected(true);
				LF.setSelected(false);
			}
			QM.setSelected(true);
			trenner=getChars();
			isdelimited=QM.isSelected();
			
			add(new JPanel(){{add(OK);}});
			OK.addActionListener(sp);
			
			
			//
		}
		private char[] getChars(){
			char[] ret = new char[3];
			if (CO.isSelected()){
				ret[0]=',';
			}
			if (SC.isSelected()){
				ret[0]=';';
			}
			if (FS.isSelected()){
				ret[0]=fsep.getText().charAt(0);
			}
			if (CR.isSelected()){
				ret[1]='\r';
				if (LF.isSelected()){
					ret[2]='\n';
				}else{
					ret[2]=0;
				}
			}else{
				if (LF.isSelected()){
					ret[1]='\n';
				}else{
					LF.doClick();
					new BVW("Kein Zeilentrenner gewählt\n es wird LF (\\n) verwendet!" );
					
				}
				ret[2]=0;
			}
			isdelimited=QM.isSelected();
			return ret;
		
		}

		
			
		
		
	}
	private class CenterPanel extends JScrollPane{
		public CenterPanel() {
			
			
			setViewportView(new JPanel(){{add(new MySQLcolumns());}});
	}
		public Dimension getPreferredSize() {
			return new Dimension(500,500);
		}
		
	
	}
	/* 
	 * Mainly acting as Controller
	 */ 
	
	private class SouthPanel extends JPanel implements ActionListener,ChangeListener{
		SouthPanel(){
		
			
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(OK)){
				trenner=np.getChars();
				isdelimited=np.QM.isSelected();
				openFile();
				filefields=readFileColumns();
				getParent().remove(cp);
				getParent().add(cp=new CenterPanel(),BorderLayout.CENTER);
				cp.invalidate();
				this.removeAll();
				this.add(load);
				load.addActionListener(this);
				getParent().validate();
			}
			if (e.getSource().equals(load)){
				dataImport();
			}
			
		}

		public void stateChanged(ChangeEvent e) {
			OK.doClick();
			
		}

		
		
	}
	private class FileColumn{
		String name; int index; 
		FileColumn(String name, int index){
			this.name=name; this.index=index;
		}
		public String toString(){
			return this.name;
		}
	}
	private void dataImport() {
		String line[];
		String linestring;
		FileColumn fc;
		Vector <String> sets=new Vector <String>();
		
		while ((line=getLine())!=null){
			sets.removeAllElements();
			for (int i=0; i<sqlfields.size();i++){
				fc=(FileColumn)(filecombo.get(i).getSelectedItem());
				if(fc.index==-1){
					new BVW("Bitte allen Feldern einen Wert <> \""+unknownfield+ "\" zuordnen!\n"
							+ "\"" + ignorefield + "\" ändert nichts (UPDATE) / setzt SQL-default (INSERT)\n"
							+ "\"" + nullfield + "\" weist den Wert SQL-NULL zu \n" 
							+ "... dies kann je nach Tabellen-Definition zu einem SQL-Error führen!" );
					return; 
				}
				if(fc.index!=-2){
					linestring=line[fc.index];
					if (fc.index==-3| (linestring.equals("")&emptynull.get(i).isSelected())){
						sets.add(sqlfields.get(i).toString()+" = NULL");
					}else{
						sets.add(sqlfields.get(i).toString()+" = '" + line[fc.index]+"'");
					}
					
				}	
			}
			sqlWrite(sets);
		}

	}
	private synchronized boolean sqlWrite(Vector<String> sets){
			StringBuffer strb=new StringBuffer();
			if (BVUtils.doCount("SELECT ALL COUNT(*) FROM "+ tablename +" WHERE "+sets.firstElement())>0){
				strb.append("UPDATE "+ tablename +" SET ");
				for (int i= 1; i< sets.size()-1;i++){
					strb.append(sets.get(i)+", ");
				}
				strb.append(sets.lastElement()+ " WHERE "+sets.firstElement());
				BVUtils.doUpdate(strb.toString());
			}else{
				strb.append("INSERT  INTO "+ tablename +" SET ");
				for (int i= 0; i< sets.size()-1;i++){
					strb.append(sets.get(i)+", ");
				}
				strb.append(sets.lastElement());
				BVUtils.doInsert(strb.toString());
			}
		return true;
	}
	
	
		
	
	private FileColumn getGuess(String msqlc) { //TODO better heuristic wanted
		new BVD(debug,filefields);
		for (FileColumn str:filefields){
			if (!(str.index<0)){
				//new BVD(debug,str);
				String matchstring1=new String("(\\A"+str.name+"\\z)");
				Pattern p1=Pattern.compile(matchstring1);
				String matchstring2=new String("(\\A"+str.name.subSequence(0, 1)+".*"+str.name.subSequence(str.name.length()-1, str.name.length())+"\\z)");
				Pattern p2=Pattern.compile(matchstring2);
				String matchstring3=new String("("+str.name.subSequence(0, 2)+")");
				Pattern p3=Pattern.compile(matchstring1);
				if (p1.matcher((CharSequence)(msqlc)).matches()){
					new BVD(debug,"M1");
					return(str);
				}
				if (p2.matcher((CharSequence)(msqlc)).matches()){
					new BVD(debug,"M2");
					return(str);
					
				}
				if (p3.matcher((CharSequence)(msqlc)).matches()){
					new BVD(debug,"M3");
					return(str);
					
				}
			}
		
		}
		return(unknowncolumn);
		
		
	}
	
	private Vector<FileColumn> readFileColumns(){
		Vector<FileColumn> ret = new Vector<FileColumn>();
		StringBuffer strb = new StringBuffer();
		int i;
			try {	
				if (inreader!=null){
					inreader.close();
				}
				inreader = new FileReader(infile);
				while (((i = inreader.read())!= -1) && (i != trenner[1])){
					strb.appendCodePoint(i);						
				}					
			} catch (FileNotFoundException fnfe) {
				// TODO: handle exception
			} catch (IOException ioe){
				
			}
			String[] string=(new String(strb)).split(Character.toString((trenner[0])));
			for (i=0;i<string.length;i++){
				ret.add(new FileColumn(doStrip(string[i]),i));
			}
			ret.add(unknowncolumn=new FileColumn(unknownfield,-1));
			ret.add(ignorecolumn=new FileColumn(ignorefield,-2));
			ret.add(nullcolumn=new FileColumn(nullfield,-3));
			new BVD(debug,ret.toString());
		return ret;
	}
	public void itemSelected(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void toBackground() {
		if (inreader!=null){
			try{
				inreader.close();
			}catch(IOException ioe){
				
			}
			inreader=null;
		}
		removeAll();
		add(np=new NorthPanel(),BorderLayout.NORTH);
		add(cp=new JScrollPane());// Dummy
		add(sp=new SouthPanel(),BorderLayout.SOUTH);
		OK.addActionListener(sp);
		validate();
		setVisible(true);
	}

	private synchronized String[] getLine() {
		String[] ret= new String[filefields.size()-3];
		String[] get= new String[filefields.size()-3];
		StringBuffer strb=new StringBuffer();
		int i;
		try {	
			while (((i = inreader.read())!= -1) && (i != trenner[1])){
				
				if (i!= trenner[2]){// ignore rest of /newline!
					strb.appendCodePoint(i);
				}	
			}	
			if (i==-1){ return null; }
		} catch (FileNotFoundException fnfe) {
			// TODO: handle exception
		} catch (IOException ioe){
			ioe.printStackTrace();
		
			
		}
		
		get=strb.toString().split(Character.toString(trenner[0]));
		for (int j=0; j< ret.length; j++){
			ret[j]=doStrip(get[j]);
		}
	return ret;
	}
	private synchronized String doStrip(String string){
		String ret=new String();
		if (isdelimited & delimited.matcher(string).matches()){
			ret = new String(string.substring(1,string.length()-1));
		}else{
			ret = string;
		}
		return ret;
	}
	public static void debug(Object o){
		if (debug){ 
			System.out.println(o);
		}
	}

}
