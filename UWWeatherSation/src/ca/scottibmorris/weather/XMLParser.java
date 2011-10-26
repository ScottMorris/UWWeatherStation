package ca.scottibmorris.weather;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLParser {
	InputStream xmlDocument;
	
	public XMLParser(InputStream xmlDocument) {
		this.xmlDocument = xmlDocument;
	}
	
	public HashMap<String, String> parse() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        HashMap<String, String> xmlItems = new HashMap<String, String>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(xmlDocument);
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("current_observation");
            for (int i=0; i<items.getLength(); i++){
                Node item = items.item(i);
                xmlItems.put(item.getNodeName(), item.getNodeValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        return xmlItems;
    }
}
