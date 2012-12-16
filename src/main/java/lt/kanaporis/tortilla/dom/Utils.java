package lt.kanaporis.tortilla.dom;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class Utils {

	/**
	 * @return Builds DOM from HTML or returns null on failure
	 */
	public static Document parseHtml(String html) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(html.getBytes()));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			return null;
		}
	}
	
}
