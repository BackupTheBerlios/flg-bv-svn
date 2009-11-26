/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author notkers
 *
 */
public class JPConditionSwitcher extends JPanel{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		JButton[] condbut=new JButton[6];
		private JLabel conditionf = new JLabel("0");
		private JPanel switcher   = new JPanel(new GridLayout(6,1));
		private int condition;
		private static final Color[] colorOfCondition = new Color[]{
			new Color(200,200,200),
			new Color(0,230,0),
			new Color(144,230,0),
			Color.YELLOW,
			Color.ORANGE,
			new Color(255,128,0),
			new Color(200,0,0)
		};
			
		
		JPConditionSwitcher(ActionListener listener,int cond){
			super (new GridLayout(1,2));
			condition=cond;
			conditionf.setFont(new Font(null,Font.BOLD, 96 ));
			setCondition(condition);
			add(conditionf);
			for (int i=0;i<6;i++){
					condbut[i]=new JButton(" "+(i+1)+" ");
					condbut[i].setActionCommand((i+1)+"");
					condbut[i].setBackground(colorOfCondition(i+1));
					condbut[i].addActionListener(listener);
					switcher.add(condbut[i]);
			};
			add(switcher); 
		}
		Color setCondition(int cond){
			condition=cond;
			setBackground(colorOfCondition[condition]);
			conditionf.setText(" "+condition+" ");
			return colorOfCondition(condition);
			
		}
		
		public static Color colorOfCondition(int cond){
			if (cond>6){
				cond=6;
			}		
		return colorOfCondition[cond];
	}

}
