package de.flg_informatik.buecherverwaltung;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.flg_informatik.buecherverwaltung.BVBookTypeView.State;
import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;

public class BVSelector extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean debug=false;
	public enum Orientation{
		VERTICAL,
		HORZONTAL;
	}
	private SelectedEventType[] events;
	private JCheckBox[] checkbox;
	public BVSelector(){//dummy
		this.add(new Label("BVSelector()"));
	}
	
	public BVSelector(ActionListener listener, Vector<String> names, Orientation o) {
		new BVD(true,names.size());
		switch(o){
		case VERTICAL: 
			this.setLayout(new GridLayout(20,(names.size()/20+1)));
			break;
		case HORZONTAL:
			this.setLayout(new GridLayout((names.size()/20+1),20));
			break;
		default:
			new BVE();
		}
		validate();
		checkbox=new JCheckBox[names.size()];
		for (int i=0; i< names.size(); i++){
			checkbox[i]=new JCheckBox(names.get(i).toString());
			checkbox[i].setActionCommand(Integer.toString(i));
			add(checkbox[i]);
			checkbox[i].addActionListener(listener);
		}
		
		invalidate();
		
	}

	
	public BVSelector(ActionListener listener, String[] names, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.length,1));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(1,names.length));
			break;
		}
		checkbox=new JCheckBox[names.length];
		
		for (int i=0; i< names.length; i++){
			checkbox[i]=new JCheckBox(names[i]);
			checkbox[i].setActionCommand(Integer.toString(i));
			add(checkbox[i]);
			checkbox[i].addActionListener(listener);
			
		}
		validate();
	}

	public void clickOn(State state){
		checkbox[state.ordinal()].doClick();
	}
	public void clickOn(int index){
		if (index < 0 ){
			clearSelection();
		}else{
		 checkbox[index].doClick();
		}
	}
	public void actionPerformed(ActionEvent arg0) {
		BVSelectedEvent.makeEvent(this,events[Integer.parseInt(arg0.getActionCommand())]);
		// TODO Auto-generated method stub
		
	}
	public Vector<String> getSelected(){
		Vector<String> ret=new Vector<String>();
		for (int i=0 ; i< checkbox.length; i++ ){
			if (checkbox[i].isSelected()){
				ret.add(checkbox[i].getText());
			}
			
		}
		return ret;
	}
	public int[] getSelectedIndex(){
		int j=0;
		for (int i=0 ; i< checkbox.length; i++ ){
			if (checkbox[i].isSelected()){
				j++;
			}
		}
		int[] ret=new int[j];
		j=0;
		for (int i=0 ;i< checkbox.length; i++ ){
			if (checkbox[i].isSelected()){
				ret[j]= i;
				j++;
			}
		}
		return ret;
	
	}
	public void clearSelection(){
		for (int i=0 ; i< checkbox.length; i++ ){
			if (checkbox[i].isSelected()){
				checkbox[i].setSelected(false);
			}
			
		}
		validate();
	}
	public Vector<String> getNames (){
		Vector<String> ret = new Vector<String>();
			for (JCheckBox rb:checkbox){
				ret.add(rb.getText());
			}
		return ret;
	}
	
	public void removeActionListener(ActionListener al) {
		for(JCheckBox rb:checkbox){
			rb.removeActionListener(al);
		}
	}
	public void removeAllActionListener() {
		for(JCheckBox rb:checkbox){
			for (ActionListener al:rb.getActionListeners()){
				rb.removeActionListener(al);
			}
		}
	}
	

}
