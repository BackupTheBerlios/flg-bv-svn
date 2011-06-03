package de.flg_informatik.buecherverwaltung;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class JPVYearChooser extends JPanel implements ActionListener,BVConstants{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPChooser years;
	private JPChooser classes;
	private Vector<OClass> classesvect = null;
	private JPVYearChooser np;
		public JPVYearChooser() {
			setLayout(new GridLayout(0,1));
			np=this;
			years=new JPChooser(np,OClass.getYears(),JPChooser.Orientation.VERTICAL);
			add(years);
			try {
				years.clickOn(Control.getControl().app_settings.getProperty("aktuellesSchuljahr", "2011"));
				
			} catch (IllegalArgumentException iae) {
				new Warn("keine Schuljahre verfügbar, sind die Klassendaten importiert?");
			}catch (NullPointerException e){
				// years not created yet
			}
			makeVisible();
			
			
		}
		
		public void makeVisible(){
			/*if (np.getComponentCount()>1) {
				remove(classes);
			}else
			np.add(classes=new JPChooser(this,OClass.getClassNames(classesvect),JPChooser.Orientation.VERTICAL));
			*/
			revalidate();
		}
		

		public void actionPerformed(ActionEvent e) {
			new Deb(debug,e);
			
			if (((Container)(e.getSource())).getParent().equals(years)){
				if (np.getComponentCount()>1) remove(classes);
				classesvect=OClass.getClasses(((JRadioButton)(e.getSource())).getText());
				new Deb(debug,classesvect);
				np.add(classes=new JPChooser(this,OClass.getClassNames(classesvect),JPChooser.Orientation.VERTICAL));
				makeVisible();
								
			}
			if (((Container)(e.getSource())).getParent().equals(classes)){
				new Deb(e.getActionCommand());
				Control.getControl().newEvent(getSelectedClass(),SelectedEvent.SelectedEventType.BLClassSelected);
				makeVisible();
			}
			
		}
		/*public Vector<OClass> getClassVector(){
			return (Vector<OClass>)classesvect.clone();
		}
		*/
		public OClass getSelectedClass(){
			try {
				return classesvect.get(classes.getSelectedIndex());
			} catch (NullPointerException e) {
				// no classes yet
				return null;
			} catch (ArrayIndexOutOfBoundsException e) {
				// no class selected any more
				return null;
			}
		}
		public void unselectClass(){
			
			try {
				classes.clearSelection();
				revalidate();
			} catch (NullPointerException e) {
				// no classes yet
			}
		}
		public String getSelectedYear(){
			return years.getSelected();
		}
	
}
