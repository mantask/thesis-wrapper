package lt.kanaporis.tortilla.dom;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public final class Utils {

	/**
	 * @return Builds DOM from HTML or returns null on failure
	 */
	public static Document parseHtml(String html) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(html.getBytes()));
		} catch (Exception e) {
			return null;
		}
	}
	
}
