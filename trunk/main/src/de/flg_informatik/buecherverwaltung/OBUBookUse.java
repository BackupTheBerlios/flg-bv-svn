package de.flg_informatik.buecherverwaltung;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import de.flg_informatik.ean13.Ean;

/**
 * @author notkers
 *
 */
public class OBUBookUse implements BVConstants{
	
		
	/**
	 *  
	 * @return Vector of subjects from set-definition in database; 
	 * 
	 */
	public static Vector<String> getSubjects(){
		subjects=USQLQuery.getSetTokens(tablename, col_subname);
		new Deb(debug,subjects);
		return (subjects);
	}
	
	/**
	 * @return Vector of grades from set-definition in database 
	 */
	public static Vector<String> getGrades(){
		return (USQLQuery.getSetTokens(tablename, col_grades));
	}
	
	/**
	 * <p>
	 * Has to be called again when BookUse was created or deleted! 
	 * 
	 * @return Vector of all BookUses of ISBN, if empty returns null;
	 *
	 */
	public static Vector<OBUBookUse> getUsesOf(Ean ISBN){
	
		if (getBCIDOf(ISBN)==0){
			return getBVOf(ISBN);
		}else{
			return getBVOf(getBCIDOf(ISBN),true);
		}

	}
	
	

	/**
	 * convienience method of {@link getUsesOf()} , nice Strings {@code grade: subject}.
	 * 
	 * Has to be called again when BookUse was created or deleted!
	 */
	public static Vector<String> getUsesOfString(Ean ISBN){
		Vector<String> ret=new Vector<String>();
		for (OBUBookUse bu:OBUBookUse.getUsesOf(ISBN)){
			new Deb(debug,bu);
			ret.add(bu.grade+": "+bu.subject);
		}
		return ret;
	}
	/**
	 * <p>
	 * Has to be called again when BookUse was created or deleted! 
	 * 
	 * @return Vector of all BookUses of ISBN, if empty returns null;
	 *
	 */
	public static int getBCIDOf(Ean ISBN){
		ResultSet rs;
		Statement statement = Control.getControl().bvs.getStatement();
		try {
			rs=USQLQuery.doQuery("SELECT BCID FROM " + tablename + " WHERE ISBN = "
					+ ISBN + " AND BCID <> 0",statement);
			rs.beforeFirst();
			while (rs.next()){	
				return rs.getInt(col_bc);
			}
		} catch (SQLException sqe) {
			
		} catch (NullPointerException np){
			//no aequivalence yet
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return 0;
	}
	/**
	 * get equivalent ISBN
	 * @param ISBN
	 * @return
	 */
	
	public static Vector<String> getEquisOfString(Ean ISBN){
		Vector<String> ret=new Vector<String>();
		for (OBUBookUse bv:getBVOf(getBCIDOf(ISBN), false)){
			new Deb(debug,bv.isbn);
			if(!bv.isbn.toString().equals(ISBN.toString())){
				ret.add(bv.isbn.toString());
			}
		}
		return ret;
	}
	/**
	 * Set two ISBN or bcid as equivalent.
	 * Make a new BookClassID bcid. 
	 * Merge all uses and store it for bcid.  
	 * @param ean1
	 * @param ean2
	 */
	
	
	public static void makeEqui(Ean ean1, Ean ean2){
		new Deb(debug,"making "+ean1+" equivalent to "+ean2);
		ResultSet rs;
		if (ean1.toString().equals(ean2.toString())){
			new Warn("Kann keine Äquivalenz mit sich selbst einrichten!\n Ignoriere Anweisung.");
			return;
		}
		int bcid=0;
		Vector<OBUBookUse> uses = new Vector<OBUBookUse>();
		// Collect uses first and delete them
		uses.addAll(getUsesOf(ean1));
		uses.addAll(getUsesOf(ean2));
		for (OBUBookUse bv:uses){
			bv.delUse();
		}
		
		// is first of ean1 and ean2
		if (getBCIDOf(ean1)+getBCIDOf(ean2)==0){
				
			//Find free bcid (maximum of bcid + 1)
			Statement statement = Control.getControl().bvs.getStatement();
			try {
				bcid=0;
				do{	
					bcid++;
					rs=USQLQuery.doQuery("SELECT COUNT(BCID) FROM " + tablename + " WHERE BCID =  "
							+ bcid,statement);
					new Deb(debug,rs.first());
				}while (rs.getInt(1)!=0);
			} catch (SQLException sqe) {
				new Deb(debug,sqe);
				
			}finally{
				Control.getControl().bvs.releaseStatement(statement);
			}
			new Deb(debug,"bcid: "+bcid);
			setBCIDOf(ean1,bcid);
			setBCIDOf(ean2,bcid);
		}else{
			// first one equivalent
		
			if (getBCIDOf(ean1)!=0){
				bcid=getBCIDOf(ean1);
				if (getBCIDOf(ean2)!=0){ // much work to do
					for (String ean:getEquisOfString(ean2)){
						setBCIDOf(new Ean(ean),bcid);
					}
				}
				setBCIDOf(ean2,bcid);
			}else{ //must be only ean2!=0
					bcid=getBCIDOf(ean2);
					setBCIDOf(ean1, bcid);
				
			}
			
		
		}
		for (OBUBookUse bv:uses){
			bv.bcid=bcid;
			bv.isbn=null; // not needed
			if (!isExisting(bv.bcid, bv.grade, bv.subject)){
				bv.addUse();
			}
		}
		//Find uses 
		
		
	}
	public static void delEqui(Ean ean1){
		Vector<OBUBookUse> uses = new Vector<OBUBookUse>();
		Vector<OBUBookUse> equis = new Vector<OBUBookUse>();
		// Collect uses first and delete them
		new Deb(debug,"deleting"+ean1);
		uses.addAll(getUsesOf(ean1));
		new Deb(debug,uses);
		
		equis=getBVOf(getBCIDOf(ean1),false);

		for (OBUBookUse bv:equis){ // delete myselve
			new Deb(debug,bv.isbn.toString()+"=="+ean1.toString());
			if (bv.isbn.toString().equals(ean1.toString())){
			
				bv.delUse();
				equis.remove(bv);
			}
		}
		
		if (equis.size()<=1){
			new Deb(debug,uses);
			try{
				equis.firstElement().delUse();
				for (OBUBookUse bv:uses){
					// not needed
					
					if (!isExisting(equis.firstElement().isbn, bv.grade, bv.subject)){
						makeBookUse(equis.firstElement().isbn, bv.grade, bv.subject);
					}
				}	
				
			}catch(NullPointerException npe){
				new Warn("Datenbankinkosistenz repariert!\n" +
						" (Fehlendes Äquivalent!) ");
				
			}
		
		
		}	
		
		for (OBUBookUse bv:uses){
			// not needed
			if (!isExisting(ean1, bv.grade, bv.subject)){
				makeBookUse(ean1, bv.grade, bv.subject);
				bv.delUse();
			}
		}
	
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
		if (getBCIDOf(isbn)==0){ // no equivalence
			if (grade == null | subject == null){
				new Warn("Keine vollständige Zuweisung; \n Anweisung ignoriert!");
				return;
			}
			new Deb(5,grade); new Deb(debug,subject);
			if (!isExisting(isbn,grade,subject)){
				new OBUBookUse(isbn,grade,subject).addUse();
			}else{
				new Warn("Diese Zuordnung existiert bereits; \n Anweisung ignoriert!");
			}
		}else{ //equivalence
			makeBookUse(getBCIDOf(isbn), grade, subject);
			
		}
		
	}
	public static void makeBookUse(int bcid, String grade, String subject){
		if (grade == null | subject == null){
			new Warn("Keine vollständige Zuweisung; \n Anweisung ignoriert!");
			return;
		}
		if (!isExisting(bcid,grade,subject)){
			new OBUBookUse(bcid,grade,subject).addUse();
		}else{
			new Warn("Diese Zuordnung existiert bereits; \n Anweisung ignoriert!");
		}
		
	}
	

	/**
	 * initializes class
	 */
	public static void init(){
		new OBUBookUse();
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
	private String subject=null;
	private String grade=null;
	private Ean isbn;
	private int buid=0;
	private int bcid=0;	
	
	private OBUBookUse(){ // only for init
		
	}
		
	private OBUBookUse(Ean isbn, String grade, String subject ){ // new Bookuse from database
		this.isbn=isbn;
		this.grade=grade;
		this.subject=subject;
		
	}
	private OBUBookUse(int bcid, String grade, String subject ){ // new Bookuse from equi
		this.bcid=bcid;
		this.grade=grade;
		this.subject=subject;
		
	}
	
	private OBUBookUse(int buid, int bcid, String isbn, String grade, String subject ){ // new Bookuse created, same ISBN
		if (isbn != null){
			this.isbn=new Ean(isbn);
		}
		this.buid=buid;
		this.bcid=bcid;
		this.grade=grade;
		this.subject=subject;
		
	}
	boolean delUse(){
		new Deb(debug,"deleting"+this);
			
			return(USQLQuery.doDelete("DELETE FROM "+tablename+" WHERE "
					+ col_pk + " = " + buid )!=0);
			}

	private static Vector<OBUBookUse> getBVOf(Ean ISBN){
		Vector<OBUBookUse> ret = new Vector<OBUBookUse>();
		Statement statement = Control.getControl().bvs.getStatement();
		ResultSet rs;
		try{
			rs=USQLQuery.doQuery("SELECT  * FROM " + tablename + " WHERE ISBN = "
					+ ISBN,statement);
			rs.beforeFirst();
			while (rs.next()){	
				ret.add(new OBUBookUse(rs.getInt(col_pk),rs.getInt(col_bc),rs.getString(col_isbn),rs.getString(col_grades),rs.getString(col_subname)));
			}	
		} catch (SQLException sqe) {
			new Err(sqe.toString());
			
		} catch (NullPointerException np){
			//null in null out;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return ret;
		
	}
	private static Vector<OBUBookUse> getBVOf(int bcid, boolean isbnnull) {
		Vector<OBUBookUse> ret = new Vector<OBUBookUse>();
		ResultSet rs;
		Statement statement = Control.getControl().bvs.getStatement();
		
		try {
			if (isbnnull){
		
				rs=USQLQuery.doQuery("SELECT * FROM " + tablename + " WHERE ( BCID = "
				+ bcid + ") " + " AND ("  + col_isbn + " IS NULL )",statement);
				rs.beforeFirst();
				while (rs.next()){	
					ret.add(new OBUBookUse(rs.getInt(col_pk),rs.getInt(col_bc),null,rs.getString(col_grades),rs.getString(col_subname)));
				}
			} else {
				rs=USQLQuery.doQuery("SELECT * FROM " + tablename + " WHERE ( BCID = "
						+ bcid + ") " + " AND ("  + col_isbn + " IS NOT NULL )",statement);
				rs.beforeFirst();
				while (rs.next()){	
					ret.add(new OBUBookUse(rs.getInt(col_pk),rs.getInt(col_bc),rs.getString(col_isbn),rs.getString(col_grades),rs.getString(col_subname)));
				}
			}
			
		} catch (SQLException sqe) {
		new Err(sqe.toString());
		
		} catch (NullPointerException np){
		//no use yet
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return ret;
	}
	private static boolean isExisting(Ean isbn, String grade,
			String subject) {
		 if (USQLQuery.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE " +
		 		" ( ISBN" + " = " + isbn + ")AND ("
		 		+ col_grades + " = '" + grade + "') AND ("
		 		+ col_subname + " = '" + subject +"')"
		 		)==0){
 			 return false;
 		 }else{
 			 return true;
 		 }
	}
	private static boolean isExisting(int bcid, String grade,
			String subject) {
		 if (USQLQuery.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE " +
		 		" ( "+col_bc + " = " + bcid + ")AND ("
		 		+ col_grades + " = '" + grade + "') AND ("
		 		+ col_subname + " = '" + subject +"')"
		 		)==0){
 			 return false;
 		 }else{
 			 return true;
 		 }
	}
	

	private boolean addUse(){
		if (bcid>0){
			return(USQLQuery.doInsert("INSERT INTO "+tablename+" SET "
					+ "ISBN  = null ," 
					+ col_bc + " = " + bcid + ","
					+ col_grades + " = '" + grade + "',"
					+ col_subname + " = '" + subject +
					 "'" )!=0);
		}else{
			return(USQLQuery.doInsert("INSERT INTO "+tablename+" SET "
					+ "ISBN" + " = " + isbn + ","
					+ col_bc + " = " + 0 + ","
					+ col_grades + " = '" + grade + "',"
					+ col_subname + " = '" + subject +
					 "'" )!=0);
		}
	}
	private static boolean setBCIDOf(Ean isbn, int bcid){
		return(USQLQuery.doUpdate("INSERT INTO "+tablename+" SET "
				+ "ISBN" + " = " + isbn + ","
				+ col_bc + " = " + bcid + "," 
				+ col_grades + " = " +null + ","
				+ col_subname + " = " + null )!=0);
		}

	public static Vector<Ean>  getISBN(String subject, String grade) {
		Vector<Ean> ret= new Vector<Ean>();
		Vector<Integer> equi = new Vector<Integer>();
		Statement statement = Control.getControl().bvs.getStatement();
		ResultSet rs;
		try {
			rs=USQLQuery.doQuery("SELECT  ISBN, BCID FROM "+tablename+" WHERE " +
			 		" ( " + col_grades + " = '" + grade + "') AND ("
			 		+ col_subname + " = '" + subject +"')",statement);
			rs.beforeFirst();
			while (rs.next()){
				if (rs.getString(1)!=null){
					ret.add(new Ean(rs.getString(1)));
				}else{
					if (rs.getString(2)!=null){
						equi.add(rs.getInt(2));
					}
				}
			}
			
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		if (equi.size()>0){ // there are Equivalences
			for (int bcid:equi){
				for (OBUBookUse bvbu:OBUBookUse.getBVOf(bcid, false)){
					ret.add(bvbu.isbn);
				}
			}
		}
		
		
		
		return ret;
	}

	

	
	
	
	
	
	

}
