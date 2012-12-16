package lt.kanaporis.tortilla.wrapper.ted;

import static org.junit.Assert.assertEquals;
import lt.kanaporis.tortilla.dom.Utils;

import org.junit.Test;
import org.w3c.dom.Document;

public class RTEDMapperTest {

	@Test
	public void testMapPlainTags() {
		Document doc = Utils.parseHtml("<html><head></head><body><h1></h1><p></p></body></html>");
		String result = RTEDMapper.map(doc);
		assertEquals("{html{head}{body{h1}{p}}}", result);
	}
	
	@Test
	public void testMapTagsWithText() {
		Document doc = Utils.parseHtml("<html><head></head><body><h1>Hello there!</h1><p>xxx</p></body></html>");
		String result = RTEDMapper.map(doc);
		assertEquals("{html{head}{body{h1{#text}}{p{#text}}}}", result);
	}
	
	@Test
	public void testMapTagsWithAttributes() {
		Document doc = Utils.parseHtml("<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>");
		String result = RTEDMapper.map(doc);
		assertEquals("{html{head}{body{h1{#text}}{p{@id}{#text}}}}", result);
	}
	
}
