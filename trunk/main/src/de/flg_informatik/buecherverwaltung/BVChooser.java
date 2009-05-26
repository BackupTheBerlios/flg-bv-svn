package de.flg_informatik.buecherverwaltung;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.flg_informatik.buecherverwaltung.BVBookTypeView.State;
import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;

public class BVChooser extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum Orientation{
		VERTICAL,
		HORZONTAL;
	}
	private SelectedEventType[] events;
	private JRadioButton[] radiobutton;
	private ButtonGroup bgr=new ButtonGroup();
	public BVChooser(ActionListener listener, ArrayList<String> names, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.size(),0));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(0,names.size()));
			break;
		}
		radiobutton=new JRadioButton[names.size()];
		
		for (int i=0; i< names.size(); i++){
			radiobutton[i]=new JRadioButton(names.get(i));
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			this.add(radiobutton[i]);
			radiobutton[i].addActionListener(listener);
		}
	}
	public BVChooser(ActionListener listener, Vector<String> names, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.size(),0));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(0,names.size()));
			break;
		}
		radiobutton=new JRadioButton[names.size()];
		
		for (int i=0; i< names.size(); i++){
			radiobutton[i]=new JRadioButton(names.get(i));
			debug(i+" "+radiobutton[i]);
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			this.add(radiobutton[i]);
			radiobutton[i].addActionListener(listener);
			
		}
		
	}
	public BVChooser(ActionListener listener, String[] names, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.length,0));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(0,names.length));
			break;
		}
		radiobutton=new JRadioButton[names.length];
		
		for (int i=0; i< names.length; i++){
			radiobutton[i]=new JRadioButton(names[i]);
			debug(i+" "+radiobutton[i]);
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			this.add(radiobutton[i]);
			radiobutton[i].addActionListener(listener);
			
		}
	}
	public BVChooser(String[] names, SelectedEventType[] events, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.length,0));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(0,names.length));
			break;
		}
		
	
		this.events=events;
		radiobutton=new JRadioButton[names.length];
		for (int i=0; i< names.length; i++){
			radiobutton[i]=new JRadioButton(names[i]);
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			this.add(radiobutton[i]);
			radiobutton[i].addActionListener(this);
			
		}
		
		// TODO Auto-generated constructor stub
	}
	public void clickOn(State state){
		radiobutton[state.ordinal()].doClick();
	}

	public void actionPerformed(ActionEvent arg0) {
		BVSelectedEvent.makeEvent(this,events[Integer.parseInt(arg0.getActionCommand())]);
		// TODO Auto-generated method stub
		
	}
	public String getSelected(){
		for (int i=0 ; i< bgr.getButtonCount(); i++ ){
			if (radiobutton[i].isSelected()){
				return radiobutton[i].getText();
			}
			
		}
		return null;
	}
	public int getSelectedIndex(){
		for (int i=0 ; i< bgr.getButtonCount(); i++ ){
			if (radiobutton[i].isSelected()){
				return i;
			}
			
		}
		return -1;
	}
	public void clearSelection(){
		// bgr.clearSelection(); java > 1.6
		for (int i=0 ; i< bgr.getButtonCount(); i++ ){
			if (radiobutton[i].isSelected()){
				radiobutton[i].setSelected(false);
			}
			
		}
	}
	
	static private void debug(Object obj){
		//System.out.println(BVChooser.class+": "+ obj);
	}

}
