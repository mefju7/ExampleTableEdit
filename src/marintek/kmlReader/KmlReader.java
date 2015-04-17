package marintek.kmlReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class KmlReader {

	public static void main(String[] args) {
		FileInputStream is = null;
		// TODO Auto-generated method stub
		System.out.println("program started 2015-04-17/2");
		File input = new File("C:\\Jobb\\testing\\2015-04-08\\Mine steder\\doc.kml");
		try {

			is = new FileInputStream(input);
			// SAXParserFactory sax = SAXParserFactory.newInstance();
			// SAXParser parser = sax.newSAXParser();
			//
			// DefaultHandler hb=null;
			// parser.parse(is, hb);
			DocumentBuilderFactory dbf;
			dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document dom = db.parse(is);
			Element doc = dom.getDocumentElement();
			NodeList plms = doc.getElementsByTagName("Placemark");
	
			for(int i=0;i<plms.getLength();++i)
			{
				Node el=plms.item(i);
		
			}
			System.out.printf("got %d placemarks\n", plms.getLength());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		System.out.println("program ended 2015-04-17/3");
	}

}
