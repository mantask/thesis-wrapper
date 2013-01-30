package lt.kanaporis.tortilla.wrapper.ted;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class RTEDMapperTest {

	@Test
	public void testMapPlainTags() {
		Document doc = Jsoup.parse("<html><head></head><body><h1></h1><p></p></body></html>");
		String result = RTEDMapper.map(doc);
		assertEquals("{body{h1}{p}}", result);
	}
	
	@Test
	public void testMapTagsWithText() {
		Document doc = Jsoup.parse("<html><head></head><body><h1>Hello there!</h1><p>xxx</p></body></html>");
		String result = RTEDMapper.map(doc);
		assertEquals("{body{h1}{p}}", result);
	}
	
	@Test
	public void testMapTagsWithAttributes() {
		Document doc = Jsoup.parse("<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>");
		String result = RTEDMapper.map(doc);
		assertEquals("{body{h1}{p}}", result);
	}
	
}
