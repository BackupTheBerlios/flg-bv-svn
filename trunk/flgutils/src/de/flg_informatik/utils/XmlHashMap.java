/**
 * 
 */
package de.flg_informatik.utils;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




/**
 * @author notkers
 *
 */
public class XmlHashMap {
	File linuxdefault=new File("ovpnsetup.xml"); //localize
	File windefault=new File("default.xml");
	static XmlHashMap xmlp=null;
	HashMap<String, String> properties;
	Document doc;
	
	private XmlHashMap(File file) {
		this.doc=readXMLFile(file); 
		this.fillMap();
			
		
	}
	
	public static XmlHashMap getXmlHashMap(File file){
		xmlp=new XmlHashMap(file);
		
		return xmlp;
		
	}
	
	private File getFile(){
		
		return linuxdefault;
	}
	public  Document readXMLFile(File file){
		Document document;
		try {
			document=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getFile());
			return document;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	
	}
	private boolean fillMap(){
		doc.normalizeDocument();
		recurseNode(doc,"");
		writeXmlFile(doc, "ovpnsetup2.xml");
		

		return true;
		
		
		
		
	}
	 public void writeXmlFile(Document doc, String filename) {
	        try {
	            // Prepare the DOM document for writing
	            Source source = new DOMSource(doc);
	    
	            // Prepare the output file
	            File file = new File(filename);
	            Result result = new StreamResult(file);
	    
	            // Write the DOM document to the file
	            Transformer xformer = TransformerFactory.newInstance().newTransformer();
	            xformer.transform(source, result);
	        } catch (TransformerConfigurationException e) {
	        } catch (TransformerException e) {
	        }
	    }
	private void recurseNode(Node node, String pfad){
		NodeList list=null;
		node.normalize();
		System.out.println("down");
		if (node.getAttributes()!=null){
			parseAttrib(node);
		}

		list=getChilds(node);
		try{
			Thread.sleep(100);
		}catch(Exception e){
			
		}
		if (list.getLength()>0){
			for (int i=0; i< list.getLength();i++){
				//System.out.println("List: "+list.item(i).getNodeName()+": "+list.getLength()+"/"+i);
				if (node.getNodeType()!=Node.TEXT_NODE){
					recurseNode(list.item(i),pfad+"."+node.getNodeName());
				}
			}
		}else{
			if (node.getTextContent().trim().length()!=0){
				System.out.println(pfad+": "+node.getTextContent());
			}else{
				System.out.println(pfad+": null");
			}
			
			
		}
		System.out.println("up");
		return;
	}
	private void parseAttrib(Node node){
		
	}
	private synchronized NodeList getChilds(Node node){
		
	return 	node.getChildNodes();
	}
	public static void main(String[] args){
		getXmlHashMap(new File("ovpnsetup.xml"));
		
	}

}
