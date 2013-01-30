package lt.kanaporis.tortilla.dom;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class PostOrderNavigatorTest {

	private final static String HTML = "<html><head></head><body><h1>Hello there!</h1><p class=\"first second\">xxx</p></body></html>";
	
	@Test
	public void testGet() {
		Document doc = Jsoup.parse(HTML);
		PostOrderElementNavigator nav = new PostOrderElementNavigator(doc);
		assertEquals(null, nav.getElementValue(0));
		assertEquals("Hello there!", nav.getElementValue(1));
		assertEquals("xxx", nav.getElementValue(2));
		assertEquals("", nav.getElementValue(3));
		assertEquals(null, nav.getElementValue(4));
	}

	@Test
	public void testIndexAttribute() {
		Document doc = Jsoup.parse(HTML);
		PostOrderElementNavigator nav = new PostOrderElementNavigator(doc);
		assertEquals(1, nav.getElementIndex("h1"));
		assertEquals(2, nav.getElementIndex("p"));
		assertEquals(3, nav.getElementIndex("body"));
	}

}
