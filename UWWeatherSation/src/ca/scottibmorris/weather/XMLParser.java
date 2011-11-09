package ca.scottibmorris.weather;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.widget.TextView;


public class XMLParser {
	InputStream xmlDocument;
	TextView tv;
	
	public XMLParser(InputStream xmlDocument, TextView tv) {
		this.xmlDocument = xmlDocument;
		this.tv = tv;
	}
	
	public HashMap<String, String> parse() {
		XmlPullParserFactory factory;
	    String tagName = "";
	    String text = "";
	    HashMap<String, String> xmlItems = new HashMap<String, String>();
	    
	    //XML PushPullParser - Thanks to Geekswordsman (on StackOverflow)
	    //http://stackoverflow.com/questions/8070923/how-to-do-a-simple-xml-parser-with-android/8071019#8071019
	    try {
	        factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
	        xpp.setInput(new InputStreamReader(xmlDocument));
	        int eventType = xpp.getEventType();

	        while (eventType != XmlPullParser.END_DOCUMENT) {
	            if (eventType == XmlPullParser.TEXT) {
	                text = xpp.getText(); //Pulling out node text
	            } else if (eventType == XmlPullParser.END_TAG) {
	                tagName = xpp.getName();

	                xmlItems.put(tagName, text.trim());

	                text = ""; //Reset text for the next node
	            }
	            eventType = xpp.next();
	        }
	    /*}  catch (XmlPullParserException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();*/
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    	//Log.d("Exception attribute", e + "+" + tagName);
	    }
	    
	    return xmlItems;
	}
        /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        HashMap<String, String> xmlItems = new HashMap<String, String>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(xmlDocument);
            Element root = dom.getDocumentElement();
            NodeList items = root.getChildNodes();//.getElementsByTagName("current_observation");
            //Node rootElement = items.item(0);
            //items = rootElement.getChildNodes();
            tv.append("\nParser, # of Items: " + String.valueOf(items.getLength()));
            for (int i = 0; i < items.getLength(); i++){
            	Node item = items.item(i);
                xmlItems.put(item.getNodeName(), item.getNodeValue());
                tv.append("\nNM: " + item.getNodeName() + " NV: " + item.getNodeValue() + " i: " + i);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        return xmlItems;
    }*/
}
