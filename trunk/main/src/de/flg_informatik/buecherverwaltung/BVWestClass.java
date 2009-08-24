package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class BVWestClass extends JPanel implements ActionListener{
	private BVChooser years;
	private BVChooser classes;
	private static boolean debug=true;
	private Vector<BVClass> classesvect = null;
	private BVWestClass np;
		public BVWestClass() {
			setLayout(new GridLayout(0,1));
			np=this;
			years=new BVChooser(np,BVClass.getYears(),BVChooser.Orientation.VERTICAL);
			add(years);
			try {
				years.clickOn(BVControl.getControl().app_settings.getProperty("aktuellesSchuljahr", "2009"));
				makeVisible();
			} catch (IllegalArgumentException iae) {
				// nothing to do 
			}
			
			
		}
		
		public void makeVisible(){
			/*if (np.getComponentCount()>1) {
				remove(classes);
			}else
			np.add(classes=new BVChooser(this,BVClass.getClassNames(classesvect),BVChooser.Orientation.VERTICAL));
			*/
			revalidate();
		}
		

		public void actionPerformed(ActionEvent e) {
			new BVD(debug,e);
			
			if (((Container)(e.getSource())).getParent().equals(years)){
				if (np.getComponentCount()>1) remove(classes);
				classesvect=BVClass.getClasses(((JRadioButton)(e.getSource())).getText());
				new BVD(debug,classesvect);
				np.add(classes=new BVChooser(this,BVClass.getClassNames(classesvect),BVChooser.Orientation.VERTICAL));
				makeVisible();
								
			}
			if (((Container)(e.getSource())).getParent().equals(classes)){
				BVSelectedEvent.makeEvent(getSelectedClass(),BVSelectedEvent.SelectedEventType.BLClassSelected);
				makeVisible();
			}
			
		}
		/*public Vector<BVClass> getClassVector(){
			return (Vector<BVClass>)classesvect.clone();
		}
		*/
		public BVClass getSelectedClass(){
			try {
				new BVD(debug,classes.getSelectedIndex());
				return classesvect.get(classes.getSelectedIndex());
			} catch (NullPointerException e) {
				// no classes yet
				return null;
			}
		}
		public String getSelectedYear(){
			return years.getSelected();
		}
	
}
