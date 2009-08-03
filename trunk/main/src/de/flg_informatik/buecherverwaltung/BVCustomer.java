package de.flg_informatik.buecherverwaltung;

public class BVCustomer {
	private final static String tablename="Customers";
	
	public static int getTotal(int KID){
		return BVUtils.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE KID = "+KID);
	}
	public static int getRev(int KID){
		return BVUtils.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE KID = "+KID+ "AND RE = Rev" );
	}
	public static int getNoFL3(int KID){
		return BVUtils.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE KID="+KID+" AND FL3 IS NULL");
	}
	public static int getFL2F(int KID){
		return BVUtils.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE KID="+KID+" AND ( FL1= 'F' OR FL2= 'F'");
	}
	public static int getFL2L(int KID){
		return BVUtils.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE KID="+KID+" AND FL2= 'F'");
	}
}
