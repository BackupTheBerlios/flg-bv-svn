package de.flg_informatik.utils;

public class Version implements Comparable<Version>{
	int[] version={1,1};
	String date = "2009-03-01";
	public Version(){
		System.out.println(this);
	}
	public Version(int[] version, String date){
		this.version=version;
		this.date=date;
	}
	
	public String toString(){
		StringBuffer ret=new StringBuffer();
		for (int i=0;i< version.length;i++){
			if (i>0) ret.append(".");
			ret.append(version[i]);
		}
		ret.append(" of ");
		ret.append(date);
		return new String(ret);
	}
	
	
	public int[] getVersion(){
		return version;
	}
	public int compareTo(Version vers){
		for (int i=0; i<Math.min(this.version.length, vers.version.length);i++){
			if (this.version[i]>vers.version[i]) return 1;
			if (this.version[i]<vers.version[i]) return -1;
		}
		if (this.version.length>vers.version.length)return 1;
		if (this.version.length<vers.version.length)return -1;
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Version();
		// TODO Auto-generated method stub

	}
	
}
