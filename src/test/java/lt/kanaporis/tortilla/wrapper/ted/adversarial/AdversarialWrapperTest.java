package lt.kanaporis.tortilla.wrapper.ted.adversarial;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import lt.kanaporis.tortilla.wrapper.WrappingResult;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class AdversarialWrapperTest {

	@Test
	public void testWrapText() {
		String html1 = "<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>";
		String html2 = "<html><head></head><body><h1>Hello there!</h1><div>yyy</div></body></html>";
		String xpath = "/html/body/p/text()";
		AdverserialWrapper wrapper = new AdverserialWrapper(html1, xpath);
		WrappingResult result = wrapper.wrap(html2);
		assertEquals("yyy", result.getValue());
	}

	@Test
	public void testWrapAttribute() {
		String html1 = "<html><head></head><body><h1>Hello there!</h1><p id=\"node\">xxx</p></body></html>";
		String html2 = "<html><head></head><body><h1>Hello there!</h1><div id=\"node\">yyy</div></body></html>";
		String xpath = "//*[@id='node']/text()";
		AdverserialWrapper wrapper = new AdverserialWrapper(html1, xpath);
		WrappingResult result = wrapper.wrap(html2);
		assertEquals("yyy", result.getValue());
	}
	
	@Test
	public void testWrapAmazon() throws Exception {
		String html1 = FileUtils.readFileToString(new File(getClass().getResource("/amazon20110105034652.html").toURI()));
		String html2 = FileUtils.readFileToString(new File(getClass().getResource("/amazon20110727020908.html").toURI()));
		String xpath = "//*[@id='navFooter']/table/tbody/tr[1]/td/table/tbody/tr/td[4]/div";
		AdverserialWrapper wrapper = new AdverserialWrapper(html1, xpath);
		WrappingResult result = wrapper.wrap(html2);
		assertEquals("Make Money with Us", result.getValue());
	}
	
	

}
