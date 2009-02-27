package de.flg_informatik.paedmlovpn;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Test {
	Document doc;
	public Test(){
		try {
			doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("ovpnsetup.xml");
		} catch (Exception e) {
			e.printStackTrace();
			
			// TODO: handle exception
		}
		System.out.println();
	}
	public static void main(String[] args){
		new Test();
	}

}
