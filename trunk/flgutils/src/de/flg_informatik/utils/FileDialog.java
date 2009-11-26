/**
 * 
 */
package de.flg_informatik.utils;

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
public class FileDialog extends JFileChooser {
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
	static public File getFileOpen(Window w){
		
		fc=new JFileChooser();
		dlg = new Dialog((Frame) null, "Open File", true);
		// dlg = new Dialog(w, "Open File",Dialog.DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
		if (fc.showOpenDialog(dlg)==JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}else{
			return null;
		}
	}
	static public File getFileOpen(Frame f){
		fc=new JFileChooser();
		dlg = new Dialog((Frame) null, "Open File", true);
		// dlg = new Dialog(f, "Open File",Dialog.DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
		if (fc.showOpenDialog(dlg)==JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}else{
			return null;
		}
	}
	static public File getFileSave(Frame f){
		fc=new JFileChooser();
		dlg = new Dialog((Frame) null, "Save File", true);
		// dlg = new Dialog(f, "Save File",Dialog.DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
		if (fc.showSaveDialog(dlg)==JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}else{
			return null;
		}
	}
	static public File getFileSave(Window w){
		fc=new JFileChooser();
		dlg = new Dialog((Frame) null, "Save File", true);
		// dlg = new Dialog(w, "Save File",Dialog.DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
		if (fc.showSaveDialog(dlg)==JFileChooser.APPROVE_OPTION){
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
