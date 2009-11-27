package de.flg_informatik.buecherverwaltung;

import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import de.flg_informatik.utils.FLGJScrollPane;

public class JOPane extends JOptionPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	static int showScrollableOptionDialog(Component
			 parentComponent, Object message //TODO implement others
			 , String title, int optionType, int 
			 messageType, Icon icon, Object[] options, Object initialValue) throws 
			 HeadlessException{
		Object scrollableMessage;
			if (String.class.isAssignableFrom(message.getClass())){
				scrollableMessage = new FLGJScrollPane(new FLGJScrollPane(new JTextArea((String)message){{
						setBackground(new JOptionPane().getBackground());
						setFont(getFont().deriveFont(Font.BOLD));
					}}){{ // must be twice, seems to be a bug
							setBorder(new EmptyBorder(0,0,0,0));
					}});
					((FLGJScrollPane)scrollableMessage).setBorder(new EmptyBorder(0,0,0,0));
			}else{
				scrollableMessage = message;
			}
			
		return JOptionPane.showOptionDialog(parentComponent, scrollableMessage, 
				title, optionType, messageType, icon, options, initialValue);
	}

}
