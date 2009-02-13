package de.flg_informatik.buecherverwaltung;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.flg_informatik.buecherverwaltung.BVBookTypView.State;
import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;

public class BVChooser extends JPanel implements ActionListener{
	private SelectedEventType[] events;
	private JRadioButton[] radiobutton;
	private ButtonGroup bgr=new ButtonGroup();
	public BVChooser(ActionListener listener, String[] names) {
		super(new GridLayout(0,1));
		radiobutton=new JRadioButton[names.length];
		for (int i=0;i< names.length;i++){
			radiobutton[i]=new JRadioButton();
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			this.add(radiobutton[i]);
			this.add(new Label(names[i]));
			this.add(new Label());
			radiobutton[i].addActionListener(listener);
			
		}
		radiobutton[0].doClick();
	}
	public BVChooser(String[] names, SelectedEventType[] events) {
		super(new GridLayout(0,1));
		this.events=events;
		radiobutton=new JRadioButton[names.length];
		for (int i=0;i< names.length;i++){
			radiobutton[i]=new JRadioButton();
			radiobutton[i].setActionCommand(Integer.toString(i));
			bgr.add(radiobutton[i]);
			this.add(radiobutton[i]);
			this.add(new Label(names[i]));
			this.add(new Label());
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

}
