package lt.kanaporis.tortilla.wrapper.chrome_dev_tools;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import lt.kanaporis.tortilla.wrapper.ContentWrapper;
import lt.kanaporis.tortilla.wrapper.WrappingResult;

import org.junit.Test;
import org.xml.sax.SAXException;

public class ChromeDevToolsWrapperTest {

	@Test
	public void testWrapById() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		String doc = "<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>";
		String query = "//html/body/p";
		ContentWrapper wrapper = new ChromeDevToolsWrapper(doc, query);
		WrappingResult result = wrapper.wrap(doc);
		assertEquals("xxx", result.getValue());
		assertNull(result.getConfidence());
	}

	@Test
	public void testWrapByClass() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		String doc = "<html><head></head><body><h1>Hello there!</h1><p class=\"node\">xxx</p></body></html>";
		String query = "//html/body/p";
		ContentWrapper wrapper = new ChromeDevToolsWrapper(doc, query);
		WrappingResult result = wrapper.wrap(doc);
		assertEquals("xxx", result.getValue());
		assertNull(result.getConfidence());
	}

}
