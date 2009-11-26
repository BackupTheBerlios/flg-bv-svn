/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * @author Notker
 *
 */
public class VBVCSVIVFileDialog extends JFileChooser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static File file;
	static Dialog dlg;
	static JFileChooser fc;
	/**
	 * @param args
	 */
	static public File getFileOpen(Container container){
		new Deb(3,"");
		fc=new JFileChooser();
		if (Frame.class.isAssignableFrom(container.getClass())){
			dlg = new Dialog((Frame) container , "Open File", true);
		}else{
			if (Dialog.class.isAssignableFrom(container.getClass())){
				dlg = new Dialog((Dialog) container , "Open File", true);
			}else{
				if (Window.class.isAssignableFrom(container.getClass())){
					dlg = new Dialog((Window) container , "Open File", Dialog.DEFAULT_MODALITY_TYPE);
				}else{
					dlg = new Dialog((Frame) null, "Open File", true);
				}
				
			}
			
		}
		if (fc.showOpenDialog(dlg)==JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}else{
			return null;
		}
	}
	
	static public File getFileSave(Container container){
		final String title="save File";
		fc=new JFileChooser();
		if (Frame.class.isAssignableFrom(container.getClass())){
			dlg = new Dialog((Frame) container ,title , true);
		}else{
			if (Dialog.class.isAssignableFrom(container.getClass())){
				dlg = new Dialog((Dialog) container , title, true);
			}else{
				if (Window.class.isAssignableFrom(container.getClass())){
					dlg = new Dialog((Window) container , title, Dialog.DEFAULT_MODALITY_TYPE);
				}else{
					dlg = new Dialog((Frame) null, title, true);
				}
				
			}
			
		}
		if (fc.showOpenDialog(dlg)==JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}else{
			return null;
		}
	}
	
	
	public static void main(String[] args) {

	}
	public void approveSelection(){
		file=this.getSelectedFile();
		
		dlg.dispose();
		
	}

	public Dimension getPreferredSize(){
		return (new Dimension(400,400));
	}
	

}
