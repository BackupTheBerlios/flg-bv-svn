package de.flg_informatik.buecherverwaltung;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.buecherverwaltung.VBTVBookTypeView.State;

public class JPSelector extends JPanel implements ActionListener,BVConstants{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Orientation{
		VERTICAL,
		HORZONTAL;
	}
	private SelectedEventType[] events;
	private JCheckBox[] checkbox;
	public JPSelector(){//dummy
		this.add(new Label("JPSelector()"));
	}
	
	public JPSelector(ActionListener listener, Vector<String> names, Orientation o) {
		new Deb(debug,names.size());
		switch(o){
		case VERTICAL: 
			this.setLayout(new GridLayout(20,(names.size()/20+1)));
			break;
		case HORZONTAL:
			this.setLayout(new GridLayout((names.size()/20+1),20));
			break;
		default:
			new Err();
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

	
	public JPSelector(ActionListener listener, String[] names, Orientation o) {
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
		SelectedEvent.makeEvent(this,events[Integer.parseInt(arg0.getActionCommand())]);
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
