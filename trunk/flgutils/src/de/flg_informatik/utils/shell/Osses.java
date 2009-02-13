package de.flg_informatik.utils.shell;

public enum Osses { 
	linux(new String[]{"Linux","Linux"}),  
	winnt (new String[]{"Windows 2000/XP","Windows XP"});
	private Osses(String[] names){
		this.osname=names[0];
		this.ostype=names[1];
	}
	public String osname;
	public String ostype;
//public static final String[] osname = {"Linux","Windows 2000/XP"};
//public static final String[] ostype = {"Linux","Windows XP"};
}