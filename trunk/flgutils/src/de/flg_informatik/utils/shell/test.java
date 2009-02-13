package de.flg_informatik.utils.shell;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] cmd = new String[1];
		cmd[0] = "c:\\windows\\system32\\net.exe use t: \\\\10.4.0.1\\tausch /USER:hr 87NN17 /PERSIST:no";
		try{
		Runtime.getRuntime().exec(cmd[0]);
		}catch(Exception e){
			System.out.println(e);
		}
		// TODO Auto-generated method stub

	}

}
