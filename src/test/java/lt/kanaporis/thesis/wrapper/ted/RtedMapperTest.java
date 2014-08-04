package lt.kanaporis.thesis.wrapper.ted;

import static org.junit.Assert.assertEquals;

import lt.kanaporis.thesis.tree.rted.RtedMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class RtedMapperTest {

	@Test
	public void testMapPlainTags() {
		Document doc = Jsoup.parse("<html><head></head><body><h1></h1><p></p></body></html>");
		String result = RtedMapper.map(doc);
		assertEquals("{body{h1}{p}}", result);
	}
	
	@Test
	public void testMapTagsWithText() {
		Document doc = Jsoup.parse("<html><head></head><body><h1>Hello there!</h1><p>xxx</p></body></html>");
		String result = RtedMapper.map(doc);
		assertEquals("{body{h1}{p}}", result);
	}
	
	@Test
	public void testMapTagsWithAttributes() {
		Document doc = Jsoup.parse("<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>");
		String result = RtedMapper.map(doc);
		assertEquals("{body{h1}{p}}", result);
	}
	
}
