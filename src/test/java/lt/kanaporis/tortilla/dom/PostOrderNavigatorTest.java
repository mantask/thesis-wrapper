package lt.kanaporis.tortilla.dom;

import static org.junit.Assert.assertEquals;
import lt.kanaporis.tortilla.wrapper.ted.RTEDMapper;

import org.junit.Test;
import org.w3c.dom.Document;

public class PostOrderNavigatorTest {

	private final static String HTML = "<html><head></head><body><h1>Hello there!</h1><p class=\"first second\">xxx</p></body></html>";
	
	@Test
	public void testGet() {
		Document doc = Utils.parseHtml(HTML);
		PostOrderNavigator nav = new PostOrderNavigator(doc);
		assertEquals(null, nav.get(0));
		assertEquals("head", RTEDMapper.toString(nav.get(1)));
		assertEquals("#text", RTEDMapper.toString(nav.get(2)));
		assertEquals("h1", RTEDMapper.toString(nav.get(3)));
		assertEquals("@class", RTEDMapper.toString(nav.get(4)));
		assertEquals("#text", RTEDMapper.toString(nav.get(5)));
		assertEquals("p", RTEDMapper.toString(nav.get(6)));
		assertEquals("body", RTEDMapper.toString(nav.get(7)));
		assertEquals("html", RTEDMapper.toString(nav.get(8)));
		assertEquals(null, nav.get(9));
	}

	@Test
	public void testIndexAttribute() {
		Document doc = Utils.parseHtml(HTML);
		PostOrderNavigator nav = new PostOrderNavigator(doc);
		assertEquals(4, nav.index("/html/body/p/@class"));
		assertEquals(8, nav.index("/html"));
		assertEquals(3, nav.index("/html/body/h1"));
	}

}
