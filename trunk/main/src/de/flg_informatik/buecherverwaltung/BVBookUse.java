package de.flg_informatik.buecherverwaltung;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import de.flg_informatik.ean13.Ean;

/**
 * @author notkers
 *
 */
public class BVBookUse {
		
	/**
	 *  
	 * @return Vector of subjects from set-definition in database; 
	 * 
	 */
	public static Vector<String> getSubjects(){
		
		return (subjects=BVUtils.getSetTokens(tablename, col_subname));
	}
	
	/**
	 * @return Vector of grades from set-definition in database 
	 */
	public static Vector<String> getGrades(){
		return (grades=BVUtils.getSetTokens(tablename, col_grades));
	}
	
	/**
	 * <p>
	 * Has to be called again when BookUse was created or deleted! 
	 * 
	 * @return Vector of all BookUses of ISBN, if empty returns null;
	 *
	 */
	public static Vector<BVBookUse> getUsesOf(Ean ISBN){
		Vector<BVBookUse> ret = new Vector<BVBookUse>();
		ResultSet rs;
		try {
			rs=BVUtils.doQuery("SELECT  * FROM " + tablename + " WHERE ISBN = "
					+ ISBN);
			rs.beforeFirst();
			while (rs.next()){	
				ret.add(new BVBookUse(rs.getInt(col_pk),rs.getInt(col_bc),rs.getString(col_isbn),rs.getString(col_grades),rs.getString(col_subname)));
			}
		} catch (SQLException sqe) {
			
		} catch (NullPointerException np){
			//no use yet
		}
		return ret;
	}
	
	/**
	 * convienience method of {@link getUsesOf()} , nice Strings {@code grade: subject}.
	 * 
	 * Has to be called again when BookUse was created or deleted!
	 */
	public static Vector<String> getUsesOfString(Ean ISBN){
		Vector<String> ret=new Vector<String>();
		for (BVBookUse bu:BVBookUse.getUsesOf(ISBN)){
			ret.add(bu.grade+": "+bu.subject);
		}
		return ret;
	}
	
	public Vector<BVBookUse> getAequis(){
		Vector<BVBookUse> ret = new Vector<BVBookUse>();
		ResultSet rs;
		if (bcid!=0){
			try {
				rs=BVUtils.doQuery("SELECT  * FROM " + tablename + " WHERE BCID = "
						+ bcid);
				rs.beforeFirst();
				while (rs.next()){	
					ret.add(new BVBookUse(rs.getInt(col_pk),rs.getInt(col_bc),rs.getString(col_isbn),rs.getString(col_grades),rs.getString(col_subname)));
				}
			} catch (SQLException sqe) {
				
			} catch (NullPointerException np){
				//no use yet
			}
		}else{
			
		}
		
		return ret;
	}
	
	public Vector<String> getAequiString(Ean ISBN){
		Vector<String> ret=new Vector<String>();
		for (BVBookUse bu:getAequis()){
			ret.add(bu.buid+"");
		}
		return ret;
	}
	
	public Ean getIsbn(){
		return isbn;
	}
	
	/**
	 * Makes and saves new BookUse.
	 * 
	 * 
	 */
	public static void makeBookUse(Ean isbn, String grade, String subject){
		if (grade == null | subject == null){
			return;
		}
		if (!isExisting(isbn,grade,subject)){
			new BVBookUse(isbn,grade,subject).addUse();
		}
		
	}
	
	

	/**
	 * initializes class
	 */
	public static void init(){
		new BVBookUse();
	}
	
	
	boolean delUseOf(){
		
		return(BVUtils.doDelete("DELETE FROM "+tablename+" WHERE "
				+ col_pk + " = " + buid )!=0);
		}
	
	/**
	 *  Implementation
	 */
	private static final String tablename="BookUses";
	private static final String col_pk="BUID";
	private static final String col_bc="BCID";	
	private static final String col_isbn="ISBN";
	private static final String col_subname="Subjects";
	private static final String col_grades="Grades";
	private static Vector<String> subjects;
	private static Vector<String> grades;
	private String subject=null;
	private String grade=null;
	private Ean isbn;
	private int buid=0;
	private int bcid=0;	
	
	private BVBookUse(){ // only for init
		
	}
		
	private BVBookUse(Ean isbn, String grade, String subject ){ // new Bookuse from database
		this.isbn=isbn;
		this.grade=grade;
		this.subject=subject;
		
	}
	
	private BVBookUse(int buid, int bcid, String isbn, String grade, String subject ){ // new Bookuse created, same ISBN
		this.isbn=new Ean(isbn);
		this.buid=buid;
		this.bcid=bcid;
		this.grade=grade;
		this.subject=subject;
		
	}
	private static boolean isExisting(Ean isbn, String grade,
			String subject) {
		 if (BVUtils.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE " +
		 		" ( ISBN" + " = " + isbn + ")AND ("
		 		+ col_grades + " = '" + grade + "') AND ("
		 		+ col_subname + " = '" + subject +"')"
		 		)==0){
 			 return false;
 		 }else{
 			 return true;
 		 }
	}
	
	private boolean addUse(){
		return(BVUtils.doInsert("INSERT INTO "+tablename+" SET "
				+ "ISBN" + " = " + isbn + ","
				+ col_bc + " = " + bcid + ","
				+ col_grades + " = '" + grade + "',"
				+ col_subname + " = '" + subject +
				 "'" )!=0);
		}
	
	private boolean setBookUseClass(){
		
		return(BVUtils.doUpdate("UPDATE "+tablename + " SET " 
				+ col_bc + " = " + bcid + ","
				+ col_subname + " = '" + null + "',"
				+ col_grades + " = '" + null + "'" 
				+ " WHERE " + col_pk + " = " + buid)!=0);
		}
	

	
	
	
	static private void debug(Object obj){ // for debugging
		System.out.println(BVBookUse.class+": "+ obj);
	}
	
	

}
