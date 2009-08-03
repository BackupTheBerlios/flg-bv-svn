package de.flg_informatik.buecherverwaltung;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import de.flg_informatik.ean13.Ean;

/**
 * @author notkers
 *
 */
public class BVBookUse{
	private static boolean debug=true;
		
	/**
	 *  
	 * @return Vector of subjects from set-definition in database; 
	 * 
	 */
	public static Vector<String> getSubjects(){
		subjects=BVUtils.getSetTokens(tablename, col_subname);
		new BVD(debug,subjects);
		return (subjects);
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
		for (BVBookUse bu:BVBookUse.getUsesOf(ISBN)){
			new BVD(debug,bu);
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
		try {
			rs=BVUtils.doQuery("SELECT BCID FROM " + tablename + " WHERE ISBN = "
					+ ISBN + " AND BCID <> 0");
			rs.beforeFirst();
			while (rs.next()){	
				return rs.getInt(col_bc);
			}
		} catch (SQLException sqe) {
			
		} catch (NullPointerException np){
			//no aequivalence yet
		}
		return 0;
	}
	/**
	 * get equivalent ISBN
	 * @param ISBN
	 * @return
	 */
	
	public static Vector<String> getEquisOfString(Ean ISBN){
		ResultSet rs;
		Vector<String> ret=new Vector<String>();
		for (BVBookUse bv:getBVOf(getBCIDOf(ISBN), false)){
			new BVD(debug,bv.isbn);
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
		new BVD(debug,"making "+ean1+" equivalent to "+ean2);
		ResultSet rs;
		if (ean1.toString().equals(ean2.toString())){
			new BVW("Kann keine Äquivalenz mit sich selbst einrichten!\n Ignoriere Anweisung.");
			return;
		}
		int bcid=0;
		Vector<BVBookUse> uses = new Vector<BVBookUse>();
		// Collect uses first and delete them
		uses.addAll(getUsesOf(ean1));
		uses.addAll(getUsesOf(ean2));
		for (BVBookUse bv:uses){
			bv.delUse();
		}
		
		// is first of ean1 and ean2
		if (getBCIDOf(ean1)+getBCIDOf(ean2)==0){
				
			//Find free bcid (maximum of bcid + 1)
			try {
				bcid=0;
				do{	
					bcid++;
					rs=BVUtils.doQuery("SELECT COUNT(BCID) FROM " + tablename + " WHERE BCID =  "
							+ bcid);
					new BVD(debug,rs.first());
				}while (rs.getInt(1)!=0);
			} catch (SQLException sqe) {
				new BVD(debug,sqe);
				
			}
			new BVD(debug,"bcid: "+bcid);
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
		for (BVBookUse bv:uses){
			bv.bcid=bcid;
			bv.isbn=null; // not needed
			if (!isExisting(bv.bcid, bv.grade, bv.subject)){
				bv.addUse();
			}
		}
		//Find uses 
		
		
	}
	public static void delEqui(Ean ean1){
		Vector<BVBookUse> uses = new Vector<BVBookUse>();
		Vector<BVBookUse> equis = new Vector<BVBookUse>();
		// Collect uses first and delete them
		new BVD(debug,"deleting"+ean1);
		uses.addAll(getUsesOf(ean1));
		new BVD(debug,uses);
		
		equis=getBVOf(getBCIDOf(ean1),false);

		for (BVBookUse bv:equis){ // delete myselve
			new BVD(debug,bv.isbn.toString()+"=="+ean1.toString());
			if (bv.isbn.toString().equals(ean1.toString())){
			
				bv.delUse();
				equis.remove(bv);
			}
		}
		
		if (equis.size()<=1){
			new BVD(debug,uses);
			try{
				equis.firstElement().delUse();
				for (BVBookUse bv:uses){
					// not needed
					
					if (!isExisting(equis.firstElement().isbn, bv.grade, bv.subject)){
						makeBookUse(equis.firstElement().isbn, bv.grade, bv.subject);
					}
				}	
				
			}catch(NullPointerException npe){
				new BVW("Datenbankinkosistenz repariert!\n" +
						" (Fehlendes Äquivalent!) ");
				
			}
		
		
		}	
		
		for (BVBookUse bv:uses){
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
				new BVW("Keine vollständige Zuweisung; \n Anweisung ignoriert!");
				return;
			}
			new BVD(5,grade); new BVD(debug,subject);
			if (!isExisting(isbn,grade,subject)){
				new BVBookUse(isbn,grade,subject).addUse();
			}else{
				new BVW("Diese Zuordnung existiert bereits; \n Anweisung ignoriert!");
			}
		}else{ //equivalence
			makeBookUse(getBCIDOf(isbn), grade, subject);
			
		}
		
	}
	public static void makeBookUse(int bcid, String grade, String subject){
		if (grade == null | subject == null){
			new BVW("Keine vollständige Zuweisung; \n Anweisung ignoriert!");
			return;
		}
		if (!isExisting(bcid,grade,subject)){
			new BVBookUse(bcid,grade,subject).addUse();
		}else{
			new BVW("Diese Zuordnung existiert bereits; \n Anweisung ignoriert!");
		}
		
	}
	

	/**
	 * initializes class
	 */
	public static void init(){
		new BVBookUse();
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
	private BVBookUse(int bcid, String grade, String subject ){ // new Bookuse from equi
		this.bcid=bcid;
		this.grade=grade;
		this.subject=subject;
		
	}
	
	private BVBookUse(int buid, int bcid, String isbn, String grade, String subject ){ // new Bookuse created, same ISBN
		if (isbn != null){
			this.isbn=new Ean(isbn);
		}
		this.buid=buid;
		this.bcid=bcid;
		this.grade=grade;
		this.subject=subject;
		
	}
	boolean delUse(){
		new BVD(debug,"deleting"+this);
			
			return(BVUtils.doDelete("DELETE FROM "+tablename+" WHERE "
					+ col_pk + " = " + buid )!=0);
			}

	private static Vector<BVBookUse> getBVOf(Ean ISBN){
		Vector<BVBookUse> ret = new Vector<BVBookUse>();
		ResultSet rs;
		try{
			rs=BVUtils.doQuery("SELECT  * FROM " + tablename + " WHERE ISBN = "
					+ ISBN);
			rs.beforeFirst();
			while (rs.next()){	
				ret.add(new BVBookUse(rs.getInt(col_pk),rs.getInt(col_bc),rs.getString(col_isbn),rs.getString(col_grades),rs.getString(col_subname)));
			}	
		} catch (SQLException sqe) {
			new BVE(sqe.toString());
			
		} catch (NullPointerException np){
			//null in null out;
		}
		return ret;
		
	}
	private static Vector<BVBookUse> getBVOf(int bcid, boolean isbnnull) {
		Vector<BVBookUse> ret = new Vector<BVBookUse>();
		ResultSet rs;
		
		try {
			if (isbnnull){
		
				rs=BVUtils.doQuery("SELECT * FROM " + tablename + " WHERE ( BCID = "
				+ bcid + ") " + " AND ("  + col_isbn + " IS NULL )");
				rs.beforeFirst();
				while (rs.next()){	
					ret.add(new BVBookUse(rs.getInt(col_pk),rs.getInt(col_bc),null,rs.getString(col_grades),rs.getString(col_subname)));
				}
			} else {
				rs=BVUtils.doQuery("SELECT * FROM " + tablename + " WHERE ( BCID = "
						+ bcid + ") " + " AND ("  + col_isbn + " IS NOT NULL )");
				rs.beforeFirst();
				while (rs.next()){	
					ret.add(new BVBookUse(rs.getInt(col_pk),rs.getInt(col_bc),rs.getString(col_isbn),rs.getString(col_grades),rs.getString(col_subname)));
				}
			}
			
		} catch (SQLException sqe) {
		new BVE(sqe.toString());
		
		} catch (NullPointerException np){
		//no use yet
		}
		return ret;
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
	private static boolean isExisting(int bcid, String grade,
			String subject) {
		 if (BVUtils.doCount("SELECT COUNT(*) FROM "+tablename+" WHERE " +
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
			return(BVUtils.doInsert("INSERT INTO "+tablename+" SET "
					+ "ISBN  = null ," 
					+ col_bc + " = " + bcid + ","
					+ col_grades + " = '" + grade + "',"
					+ col_subname + " = '" + subject +
					 "'" )!=0);
		}else{
			return(BVUtils.doInsert("INSERT INTO "+tablename+" SET "
					+ "ISBN" + " = " + isbn + ","
					+ col_bc + " = " + 0 + ","
					+ col_grades + " = '" + grade + "',"
					+ col_subname + " = '" + subject +
					 "'" )!=0);
		}
	}
	private static boolean setBCIDOf(Ean isbn, int bcid){
		return(BVUtils.doUpdate("INSERT INTO "+tablename+" SET "
				+ "ISBN" + " = " + isbn + ","
				+ col_bc + " = " + bcid + "," 
				+ col_grades + " = " +null + ","
				+ col_subname + " = " + null )!=0);
		}

	public static Vector<Ean>  getISBN(String subject, String grade) {
		Vector<Ean> ret= new Vector<Ean>();
		Vector<Integer> equi = new Vector<Integer>();
		ResultSet rs;
		try {
			rs=BVUtils.doQuery("SELECT  ISBN, BCID FROM "+tablename+" WHERE " +
			 		" ( " + col_grades + " = '" + grade + "') AND ("
			 		+ col_subname + " = '" + subject +"')");
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
		}
		if (equi.size()>0){ // there are Equivalences
			for (int bcid:equi){
				for (BVBookUse bvbu:BVBookUse.getBVOf(bcid, false)){
					ret.add(bvbu.isbn);
				}
			}
		}
		
		
		
		return ret;
	}

	

	
	
	
	
	
	

}
