package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.flg_informatik.ean13.Ean;

public class BVClass {
	public static final BigInteger Class12=new BigInteger("210000000000");
	final static String tablename="Classes";
	final static private boolean debug=true;
	String KID;
	String Name;
	Short Abijahr; 
	Short Jahr; 
	int Location;
	
	BVClass(){
	}
	BVClass(String KID, String Name, Short Abijahr, Short Jahr, int Location) {
		this.KID=KID;
		this.Name=Name;
		this.Abijahr=Abijahr;
		this.Jahr=Jahr;
		this.Location=Location;
	}
	public static BVClass getBVClass(Ean ean) {
		//TODO not tested
		int id=Integer.parseInt(ean.toString().substring(4,12)); 
		return getBVClass(id);
	}
	
	public static BVClass getBVClass(int id) {
		BVClass result=null;
		//TODO not tested
		try{
			ResultSet rs=BVUtils.doQuery("SELECT * FROM " + tablename + " WHERE KID = " + id );
			rs.beforeFirst();
			while(rs.next()){	
				result=(new BVClass(rs.getString("KID"), rs.getString("KName"), rs.getShort("COY"), rs.getShort("Year"), rs.getInt("LID")));
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			
		}
		return result;
	}
	

	public static String[] getYears(){
		String[] ret;
		ResultSet rs;
		int i=0;
		try {
			
			rs=BVUtils.doQuery("SELECT Year FROM "+ tablename + " GROUP BY Year ORDER BY Year ");
			rs.beforeFirst();
			while (rs.next()){
				if (rs.getInt("Year")>2008){
					i++;
				}
			}
			ret=new String[i];
			rs.first();
			for (i=ret.length-1; i>=0;i--){
				rs.next();
				ret[i]=rs.getString("Year");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret=null;
		}
		return ret;
		
		
	}
	public static void saveSubjects(String KID, Vector<String> subjects) {
		StringBuffer buf=new StringBuffer();
		for (String s:subjects){
			buf.append(s+",");
		}
		String res=buf.substring(0, buf.length()-1);//must trunc
		new BVD(debug,res);
		if (BVUtils.doUpdate("UPDATE "+tablename+" SET Subjects = '" + res +
				"' WHERE KID = "+ KID )==1){
			BVControl.log("Klasse "+ getBVClass(Integer.valueOf(KID)).Name +" setze Stundentafel " + subjects);
		}
	}
	public static Vector<String> getSubjects(String KID) {
		Vector<String> result=new Vector<String>();
		String[] res;
		try{
			ResultSet rs=BVUtils.doQuery("SELECT Subjects FROM " + tablename + " WHERE KID = " + KID );
			rs.first();
			res=rs.getString(1).split(new String(","));
			
			for (int i=0; i< res.length;i++){ // this is strange! it starts with a ""
					result.add(res[i]);
					new BVD(debug,res[i]);
			}	
		}catch(SQLException sqle){
			sqle.printStackTrace();
			
		}
		return result;
	}
	public static Vector <BVClass> getClasses(String year){
		Vector<BVClass> result=new Vector<BVClass>();
		try{
			ResultSet rs=BVUtils.doQuery("SELECT * FROM " + tablename + " WHERE Year = '" + year +"' ORDER BY KName" );
			rs.beforeFirst();
			while(rs.next()){	
				result.add(new BVClass(rs.getString("KID"), rs.getString("KName"), rs.getShort("COY"), rs.getShort("Year"), rs.getInt("LID")));
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			result = null;
		}
		return result;
		
	}
	public static Vector <String> getClassNames(Vector<BVClass> vec){
		Vector<String> result=new Vector<String>();
		for (BVClass bcl:vec){
			result.add(bcl.Name.toString());
		}
		return result;
		
	}
	public static Ean getEan(int id){
		//TODO not tested
		return new Ean(Class12.add(new java.math.BigInteger(id+"")));
	}
	
	public static Ean getEan(BVClass cl){
		//TODO not tested
		return new Ean(Class12.add(new java.math.BigInteger(cl.KID+"")));
	}
	public static boolean isClassEan(Ean ean){
		//TODO not tested
		return (getBVClass(ean)!=null);
	}
}
