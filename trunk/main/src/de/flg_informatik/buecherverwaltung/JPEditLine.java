package de.flg_informatik.buecherverwaltung;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JPEditLine extends JPanel {
	public JLabel label;
	public JTextField textfield;
	public JPEditLine(String labeltext, String text, String longas, int textwidth) {
		add(label=new JLabel(labeltext));
		label.setSize(Math.max(label.getFontMetrics(label.getFont()).stringWidth(longas),label.getSize().width), label.getSize().height);
		add(textfield=new JTextField(text,textwidth));
		
		
	}
	public JPEditLine(String labeltext, int textwidth) {
		this(labeltext, "",labeltext,textwidth);
	}
	public JPEditLine(String labeltext, String text) {
		this(labeltext, text, labeltext,text.length());
	}
	public String getText(){
		return textfield.getText();
	}
}
