package de.flg_informatik.utils.shell;

public interface CallBackShellReaderListener extends CallBackInputStreamReaderListener{
	void errMatch(String string, int which);
	void errReadln(String string);
}
