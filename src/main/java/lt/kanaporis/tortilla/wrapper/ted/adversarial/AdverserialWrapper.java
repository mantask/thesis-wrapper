package lt.kanaporis.tortilla.wrapper.ted.adversarial;

import java.util.List;

import lt.kanaporis.tortilla.dom.PostOrderElementNavigator;
import lt.kanaporis.tortilla.wrapper.ContentWrapper;
import lt.kanaporis.tortilla.wrapper.WrappingResult;
import lt.kanaporis.tortilla.wrapper.ted.RTEDMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import convenience.RTED;

// TODO might not be the best implementation to multiple element selection
// TODO might want to associate a different cost per different edit. esp based on the context.
public class AdverserialWrapper implements ContentWrapper {
	
	private final String originalTree;
	private final int originalPostOrderIndex;

	public AdverserialWrapper(String originalHtml, String selector) {
		Document originalDoc = Jsoup.parse(originalHtml);
		PostOrderElementNavigator navigator = new PostOrderElementNavigator(originalDoc);
		this.originalPostOrderIndex = navigator.getElementIndex(selector);
		this.originalTree = RTEDMapper.map(originalDoc);
	}

	public WrappingResult wrap(String html) {
		Document doc = Jsoup.parse(html);
		String tree = RTEDMapper.map(doc);
		PostOrderElementNavigator navigator = new PostOrderElementNavigator(doc);
		
		// TODO calculate cost and min dist that breaks wrapper
		double distance = RTED.computeDistance(originalTree, tree);
		List<int[]> mappings = RTED.computeMapping(originalTree, tree);
		
		for (int[] mapping : mappings) {
			if (mapping[0] == originalPostOrderIndex) {
				String text = navigator.getElementValue(mapping[1]);
				return new WrappingResult(text, distance);
			}
		}
		return null;
	}

}
