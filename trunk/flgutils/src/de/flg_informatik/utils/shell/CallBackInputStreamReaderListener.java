package de.flg_informatik.utils.shell;

public interface CallBackInputStreamReaderListener {
	static String lbr=System.getProperty("line.separator");
	void  inputMatch(String string, int which);
	void inputReadln(String string);
	boolean isReady();

}
