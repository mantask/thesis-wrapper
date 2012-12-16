package lt.kanaporis.tortilla.wrapper.ted.adversarial;

import java.util.List;

import lt.kanaporis.tortilla.dom.PostOrderNavigator;
import lt.kanaporis.tortilla.dom.TreeEditScript;
import lt.kanaporis.tortilla.dom.Utils;
import lt.kanaporis.tortilla.wrapper.ContentWrapper;
import lt.kanaporis.tortilla.wrapper.WrappingResult;
import lt.kanaporis.tortilla.wrapper.ted.RTEDMapper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import convenience.RTED;

// TODO might not be the best implementation to multiple element selection
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
		
		List<int[]> mapping = RTED.computeMapping(originalTree, tree);
		TreeEditScript script = new TreeEditScript(originalTree, mapping);
		int postOrderIndex = script.apply(originalPostOrderIndex);
		PostOrderNavigator navigator = new PostOrderNavigator(dom);
		Node node = navigator.get(postOrderIndex);

		double distance = RTED.computeDistance(originalTree, tree);
		// TODO calculate cost and min dist that breaks wrapper
		
		return new WrappingResult(node.toString(), distance);
	}

}
