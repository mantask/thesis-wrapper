package lt.kanaporis.tortilla.wrapper.ted.adversarial;

import java.util.List;

import lt.kanaporis.tortilla.dom.PostOrderNavigator;
import lt.kanaporis.tortilla.dom.Utils;
import lt.kanaporis.tortilla.wrapper.ContentWrapper;
import lt.kanaporis.tortilla.wrapper.WrappingResult;
import lt.kanaporis.tortilla.wrapper.ted.RTEDMapper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import convenience.RTED;

// TODO might not be the best implementation to multiple element selection
// TODO might want to associate a different cost per different edit. esp based on the context.
// TODO currently deals with fixed XML structure. Not ready for invalid HTML trees. move to http://jsoup.org/apidocs/
public class AdverserialWrapper implements ContentWrapper {
	
	private final String originalTree;
	private final int originalPostOrderIndex;

	public AdverserialWrapper(String originalHtml, String xPath) {
		Document originalDom = Utils.parseHtml(originalHtml);
		PostOrderNavigator navigator = new PostOrderNavigator(originalDom);
		this.originalPostOrderIndex = navigator.index(xPath);
		this.originalTree = RTEDMapper.map(originalDom);
	}

	public WrappingResult wrap(String html) {
		Document dom = Utils.parseHtml(html);
		String tree = RTEDMapper.map(dom);
		PostOrderNavigator navigator = new PostOrderNavigator(dom);
		
		// TODO calculate cost and min dist that breaks wrapper
		double distance = RTED.computeDistance(originalTree, tree);
		
		List<int[]> mappings = RTED.computeMapping(originalTree, tree);
		for (int[] mapping : mappings) {
			if (mapping[0] == originalPostOrderIndex) {
				Node node = navigator.get(mapping[1]);
				return new WrappingResult(node.getNodeValue(), distance);
			}
		}

		return null;
	}

}
