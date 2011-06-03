package de.flg_informatik.buecherverwaltung;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.buecherverwaltung.VBTVBookTypeView.State;

public class JPChooser extends JPanel implements ActionListener,BVConstants{
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
	public JPChooser(){//dummy
		this.add(new Label("JPChooser()"));
	}
	public JPChooser(ActionListener listener, ArrayList<String> names, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.size(),1));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(1,names.size()));
			break;
		}
		
		radiobutton=new JRadioButton[names.size()];
		
		for (int i=0; i< names.size(); i++){
			radiobutton[i]=new JRadioButton(names.get(i));
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			add(radiobutton[i]);
			radiobutton[i].addActionListener(listener);
		}
		invalidate();
	}
	public JPChooser(ActionListener listener, Object[] enu, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(enu.length,1));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(1,enu.length));
			break;
		}
		
		radiobutton=new JRadioButton[enu.length];
		
		for (int i=0; i< enu.length; i++){
			radiobutton[i]=new JRadioButton(enu[i].toString());
			radiobutton[i].setActionCommand(enu[i].toString());
			bgr.add(radiobutton[i]);
			add(radiobutton[i]);
			radiobutton[i].addActionListener(listener);
		}
		invalidate();
	}
	public JPChooser(ActionListener listener, Vector<String> names, Orientation o) {
		new Deb(debug,names.size());
		switch(o){
		case VERTICAL: 
			this.setLayout(new GridLayout(names.size()/(names.size()/15+1)+1,(names.size()/15+1)));
			break;
		case HORZONTAL:
			this.setLayout(new GridLayout((names.size()/15+1),15));
			break;
		default:
			new Err();
		}
		validate();
		radiobutton=new JRadioButton[names.size()];
		
		
		for (int i=0; i< names.size(); i++){
			new Deb(debug, names.get(i).toString());
			radiobutton[i]=new JRadioButton(names.get(i).toString());
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			add(radiobutton[i]);
		
			radiobutton[i].addActionListener(listener);
			
		}
		
		invalidate();
		
	}
	public JPChooser(Vector<Object> names, Orientation o, ActionListener listener) {
		new Deb(debug,names.size());
		switch(o){
		case VERTICAL: 
			this.setLayout(new GridLayout(names.size()/(names.size()/20+1)+1,(names.size()/20+1)));
			break;
		case HORZONTAL:
			this.setLayout(new GridLayout((names.size()/20+1),20));
			break;
		default:
			new Err();
		}
		validate();
		radiobutton=new JRadioButton[names.size()];
		
		
		for (int i=0; i< names.size(); i++){
			radiobutton[i]=new JRadioButton(names.get(i).toString());
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			add(radiobutton[i]);
		
			radiobutton[i].addActionListener(listener);
			
		}
		
		invalidate();
		
	}
	
	public JPChooser(ActionListener listener, String[] names, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.length,1));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(1,names.length));
			break;
		}
		radiobutton=new JRadioButton[names.length];
		
		for (int i=0; i< names.length; i++){
			radiobutton[i]=new JRadioButton(names[i]);
			new Deb(debug,i+" "+radiobutton[i]);
			radiobutton[i].setActionCommand(Integer.toString(i));
			new Deb(debug,radiobutton[i].getActionCommand());
			bgr.add(radiobutton[i]);
			add(radiobutton[i]);
			radiobutton[i].addActionListener(listener);
			
		}
		validate();
	}
	public JPChooser(String[] names, SelectedEventType[] events, Orientation o) {
		switch(o){
		case VERTICAL: 
			setLayout(new GridLayout(names.length,1));
			break;
		case HORZONTAL:
			setLayout(new GridLayout(1,names.length));
			break;
		}
		
	
		this.events=events;
		radiobutton=new JRadioButton[names.length];
		for (int i=0; i< names.length; i++){
			radiobutton[i]=new JRadioButton(names[i]);
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			add(radiobutton[i]);
			radiobutton[i].addActionListener(this);
			
		}
		validate();
		
		
	}
	public void clickOn(State state){
		radiobutton[state.ordinal()].doClick();
	}
	public void clickOn(String actionCommand) throws java.lang.IllegalArgumentException, NullPointerException{
		new Deb(debug,radiobutton.length);
		new Deb(debug,radiobutton[0].getText()+"="+actionCommand);
	for (int i=0; i<radiobutton.length;i++){
		if (radiobutton[i].getText().equals(actionCommand)){
			
			clickOn(i);
			return;
		}
	}
	if (radiobutton.length>0){
			throw new IllegalArgumentException("No such actionCommand");
		}
	}
	public void clickOn(int index){
		new Deb(debug,index);
		radiobutton[index].doClick();
	}
	public void actionPerformed(ActionEvent arg0) {
		SelectedEvent.makeEvent(this,events[Integer.parseInt(arg0.getActionCommand())]);
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
		bgr.clearSelection(); //java >= 1.6
		
	}
	public Vector<String> getNames (){
		Vector<String> ret = new Vector<String>();
			for (JRadioButton rb:radiobutton){
				ret.add(rb.getText());
			}
		return ret;
	}
	public void removeActionListener(ActionListener al) {
		for(JRadioButton rb:radiobutton){
			rb.removeActionListener(al);
		}
	}
	
	public void removeAllActionListener() {
		for(JRadioButton rb:radiobutton){
			for (ActionListener al:rb.getActionListeners()){
				rb.removeActionListener(al);
			}
		}
	}
	
}
