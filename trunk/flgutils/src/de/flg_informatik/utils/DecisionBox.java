package de.flg_informatik.utils;

import java.awt.*;

public class DecisionBox extends Dialog implements java.awt.event.ActionListener{
	private static final long serialVersionUID = 1L;
	
	public static final int UNDEFINED=-1;
	public static final int CANCEL=0;
	public int back=UNDEFINED;
	private static String cancel="Abbrechen";
	private String[] buttons;


	public static int getLabel(Container owner, String title, String labels, String[] buttons, boolean mitcancel){
		return getLabel(owner,title,new String[] {labels},buttons, mitcancel);
	}
	public static int getLabel(Container owner, String title, String labels, String[] buttons){
		return getLabel(owner,title,new String[] {labels},buttons, false);
	}
	public static int getLabel(Container owner, String title, String[] labels, String[] buttons){
		return getLabel(owner,title,labels,buttons, false);
	}
	
	public static int getLabel(Container owner, String title, String[] labels, String[] button, boolean mitcancel){
		String[] buttons;
		if (mitcancel){
			buttons=new String[button.length+1];
			buttons[0]=cancel;
			for (int i=0;i<button.length;i++){
				buttons[i+1]=button[i];
			}	
		}else{
			buttons=button;
		}
		DecisionBox tbb = getTbb(owner, title);
		tbb.setLayout(new GridLayout(2,1));
		tbb.add(tbb.labels(labels));
		tbb.buttons=buttons;
		tbb.add(tbb.buttons());
		tbb.pack();
		tbb.dispose();
		tbb.pack();
		tbb.setVisible(true);
		while(tbb.back==UNDEFINED);
		return tbb.back;		
		
	}
	
	private DecisionBox(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	private DecisionBox(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getActionCommand()==cancel){
			back=0;
			this.setVisible(false);
		}else{
			for (int i=0;i<buttons.length;i++){
				if (e.getActionCommand()==buttons[i]){
					back=i+1;
					this.setVisible(false);
					break;
				}
			}		
		}
		
		
		
	}

	

	
	private Panel labels(String[] labels){
		Panel pan = new Panel();
		GridLayout gla = new GridLayout(labels.length,1);
		gla.setVgap(-3);
		pan.setLayout(gla);		
		for (int i=0;i<labels.length;i++){
			pan.add(new Label(labels[i]));
		}
		return pan;
		
	}
	private Panel buttons(){
		Panel pan=new Panel();
		Button but;
		for (int i=0;i<buttons.length;i++){
			pan.add(but=new Button(buttons[i]));
			but.addActionListener(this);
		}
		return pan;
	}

	private static DecisionBox getTbb(Container owner, String title){
		DecisionBox tbb;
		if (owner.getClass().isInstance(Frame.class)){
			tbb=new DecisionBox((Frame) owner,title,true);
		}else{
			if (owner.getClass().isInstance(Frame.class)){
				tbb=new DecisionBox((Dialog) owner,title,true);
			}else{
				tbb=new DecisionBox((Frame) null,title,true);
			}
			
		}
		return tbb;
	}
	
	public static void main(String[] args){
		Frame frame = new Frame();
			System.out.println(DecisionBox.getLabel(frame,"Einstellungsdatei",new String[]{"In Ihrem Benutzerverzeichnis gibt es keine","Einstellungsdatei. Was wollen Sie: Die Datei..."},new String[]{"...neu erstellen?","...suchen?"}));
	}
			

}
