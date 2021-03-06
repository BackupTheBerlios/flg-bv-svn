package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.flg_informatik.scanner.ScanKeyStrokes;

public class MainGUI extends JFrame implements BVConstants{

	/*
	 * Hauptklasse, jeder usecase bekommt ein Panel im JTabbedPane
	 * definiert in UCUseCases
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private static MainGUI thegui;
	// final private static int debug=8;
	public JButton cancelbutton = new JButton("Abbrechen");
	public JButton closebutton = new JButton("Beenden");
	Control control;
	JTabbedPane centerpane;
	LogPane lp;
	public MainGUI(Control control){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Deb(debug,"setting Variables");
		this.setVisible(false);
		thegui=this;
		this.control=control;
		new Deb(debug,"configuring FLGFrame");
		//this.setQuitsystem(false);
		//this.setClosewindow(false);
		new Deb(debug,"setting Layout");
		this.setLayout(new BorderLayout());
		new Deb(debug,"making Buttonfield");
		this.add(makeButtonfield(),BorderLayout.SOUTH);
		new Deb(debug,"making CardField");
		this.add(makeCardField(),BorderLayout.CENTER);
		new Deb(debug,"finishing");
		this.setExtendedState(MAXIMIZED_BOTH);
		this.pack();
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
		new Deb(debug,"finished");
		
		
		
	}
	
	public static boolean isSelectedView(UCCase view){
		if (view==null){
			return false;
		}
		new Deb(debug,"isSelectedView() from: "+((JPanel)view).getName());
		if(thegui.centerpane.getSelectedComponent()==null){
			return false;
		}else{
			return ((JPanel)view).getName().equals(thegui.centerpane.getSelectedComponent().getName());
		}
		
	}

	public static UCCase getSelectedView(){
		if(thegui.centerpane.getSelectedComponent()==null){
			return null;
		}else{
			return (UCCase) thegui.centerpane.getSelectedComponent();
		}
		
	}
	
	private JTabbedPane makeCardField(){
		centerpane=new JTabbedPane();
		for (UCUseCases usecase:UCUseCases.values()){
			new Deb(debug,"centerpane: "+ usecase.view.getName());
			centerpane.addTab(((JPanel)(usecase.view)).getName(),((JPanel)(usecase.view)));
			SelectedEvent.addBVSelectedEventListener(usecase.view);
		}
		centerpane.addChangeListener(Control.getControl());
		
		
		
		
		
		
		return centerpane;
	}
	
	
	
	
	@SuppressWarnings("serial")
	private JPanel makeButtonfield(){
		JPanel retpan=new JPanel();
		retpan.add(lp=new LogPane());
		retpan.add(new JPanel(new GridLayout(0,1)){
			{
				add(new JLabel("Buchverwaltung Version: "+MainGUI.version));
				add(new JLabel("(C) C.HOFF&NURH (@flg-informatik.de)"));
				add(new JPanel(new FlowLayout()){{
				this.add(cancelbutton);
				cancelbutton.addActionListener(control);
				this.add(closebutton);
				closebutton.addActionListener(control);
				
			}});
		}});
		invalidate();
		return retpan;
	}
	class LogPane extends java.awt.TextArea{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			
		
		LogPane(){
			this.setEditable(false);
		}
		public java.awt.Dimension getPreferredSize(){
			if (Control.getControl().mainGUI==null){
				return(new java.awt.Dimension(600,100));
			}
			return (new java.awt.Dimension(Control.getControl().mainGUI.getSize().width/3*2,Control.getControl().mainGUI.getSize().height/4));
			
		}
		public java.awt.Dimension getMinimumSize(){
			
				return(new java.awt.Dimension(600,100));
			
		}
		synchronized public void append(String string){
			super.append(string);
			invalidate();
			getParent().validate();
			
			
		}
	}

	public void toClose(){
		closebutton.doClick();
	}
	
	public static void selectView(UCUseCases usecase){
		new Deb(0,"selectView: "+ usecase);
		thegui.centerpane.setSelectedIndex(usecase.ordinal());
		thegui.centerpane.invalidate();
		thegui.validate();
	}
	public static Point getCentering(Component comp) {
		Point ret=new Point();
		ret.x=thegui.getBounds().x+(thegui.getBounds().width-comp.getWidth())/2;
		ret.y=thegui.getBounds().y+(thegui.getBounds().height-comp.getHeight())/2;
		return ret;
	}
	public static void main(String[] args) {
		new Control();
		
	}
	
	public synchronized void validate(){
		super.validate();
		
	}
	public static void val(){
		thegui.validate();
		
	}
}
