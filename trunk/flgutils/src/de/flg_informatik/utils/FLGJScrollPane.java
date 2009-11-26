package de.flg_informatik.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FLGJScrollPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FLGJScrollPane(Component scrollable) {
		setLayout(new BorderLayout());
		JPanel inner =new JPanel(new GridLayout(0,1));
		JPanel outer=new JPanel();
		inner.add(scrollable);
		outer.add(inner);
		add(new JScrollPane(outer),BorderLayout.CENTER);
	}

}
