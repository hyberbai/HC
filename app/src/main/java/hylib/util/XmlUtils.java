package hylib.util;

import hylib.toolkits.type;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtils {
    
    public static Document GetXMLDoc(String xmlStr) throws Exception {
		StringReader sr = new StringReader(xmlStr); 
		InputSource is = new InputSource(sr); 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder=factory.newDocumentBuilder(); 
		Document doc = builder.parse(is);
		return doc;
	}
    
    public static Element SelectDocElement(Document doc, String xpath) throws Exception {
    	String[] pathItems = xpath.split("\\\\|/");
    	NodeList list = doc.getElementsByTagName(pathItems[0]);
    	for (int i = 0; i < list.getLength(); i++) {
    		Element e = type.as(list.item(i), Element.class);
			if(e == null) continue;
			if(pathItems.length == 1) return e;
			e = SelectSubElement(e, pathItems, 1);
			if(e != null) return e;
		}
    	return null;
	}
    
    public static Element SelectSubElement(Element e, String[] pathItems, int pathItemIndex) throws Exception {
    	NodeList list = e.getChildNodes();
    	for (int i = 0; i < list.getLength(); i++) {
    		Element sub = type.as(list.item(i), Element.class);
			if(sub.getNodeName().equals(pathItems[pathItemIndex])) 
				if(++pathItemIndex == pathItems.length) 
					return sub;
				else
				{
					Element eFound = SelectSubElement(sub, pathItems, pathItemIndex);
					if(eFound != null) return eFound;
				}
		}
    	return null;
	}

    public static Element FindSubElement(Element e, String name) {
    	NodeList list = e.getChildNodes();
    	for (int i = 0; i < list.getLength(); i++) {
    		Element sub = type.as(list.item(i), Element.class);
			if(sub.getNodeName().equals(name)) return sub;
		}
    	return null;
    }

    public static String GetSubElementValue(Element e, String name) 
	{
    	e = FindSubElement(e, name);
    	if(e == null) return "";
    	return e.getTextContent();
	}
//    public static Node XMLDocSelect(NodeList list, String[] pathItems) throws Exception {
//    	for (String item : pathItems) {
//			Node
//		}
//    	
//    	NodeList list = doc.getElementsByTagName("A");
//    	return list.item(0);
//	}
}
