package lt.kanaporis.tortilla.wrapper.ted.adversarial;

import static org.junit.Assert.assertEquals;

import java.io.File;

import lt.kanaporis.tortilla.wrapper.WrappingResult;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class AdversarialWrapperTest {

	@Test
	public void testWrapText() {
		String html1 = "<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>";
		String html2 = "<html><head></head><body><h1>Hello there!</h1><div>yyy</div></body></html>";
		String selector = "p";
		AdverserialWrapper wrapper = new AdverserialWrapper(html1, selector);
		WrappingResult result = wrapper.wrap(html2);
		assertEquals("yyy", result.getValue());
	}

	@Test
	public void testWrapAttribute() {
		String html1 = "<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>";
		String html2 = "<html><head></head><body><h1>Hello there!</h1><div id=\"node\">yyy</div></body></html>";
		String selector = "[id=node]";
		AdverserialWrapper wrapper = new AdverserialWrapper(html1, selector);
		WrappingResult result = wrapper.wrap(html2);
		assertEquals("yyy", result.getValue());
	}
	
	@Test
	public void testWrapAmazon() throws Exception {
		String html1 = FileUtils.readFileToString(new File(getClass().getResource("/amazon20110105034652.html").toURI()));
		String html2 = FileUtils.readFileToString(new File(getClass().getResource("/amazon20110727020908.html").toURI()));
		String selector = "[id=navFooter] table tbody tr:eq(0) td table tbody tr td:eq(3) div";
		AdverserialWrapper wrapper = new AdverserialWrapper(html1, selector);
		WrappingResult result = wrapper.wrap(html2);
		assertEquals("Make Money with Us", result.getValue());
	}
	
	

}
