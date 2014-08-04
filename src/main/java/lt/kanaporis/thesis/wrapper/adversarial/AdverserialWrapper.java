package lt.kanaporis.thesis.wrapper.adversarial;

import convenience.RTED;
import lt.kanaporis.thesis.tree.rted.PostOrderElementNavigator;
import lt.kanaporis.thesis.wrapper.Wrapper;
import lt.kanaporis.thesis.wrapper.WrapperResult;
import lt.kanaporis.thesis.tree.rted.RtedMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

// TODO might not be the best implementation to multiple element selection
// TODO might want to associate a different cost per different edit. esp based on the context.
public class AdverserialWrapper implements Wrapper {
	
	private final String originalTree;
	private final int originalPostOrderIndex;

	public AdverserialWrapper(String originalHtml, String cssSelector) {
		Document originalDoc = Jsoup.parse(originalHtml);
		PostOrderElementNavigator navigator = new PostOrderElementNavigator(originalDoc);
		this.originalPostOrderIndex = navigator.getElementIndex(cssSelector);
		this.originalTree = RtedMapper.map(originalDoc);
	}

	public WrapperResult wrap(String html) {
		Document doc = Jsoup.parse(html);
		String tree = RtedMapper.map(doc);
		PostOrderElementNavigator navigator = new PostOrderElementNavigator(doc);
		
		// TODO calculate cost and min dist that breaks wrapper
		double distance = RTED.computeDistance(originalTree, tree);
		List<int[]> mappings = RTED.computeMapping(originalTree, tree);
		
		for (int[] mapping : mappings)
            if (mapping[0] == originalPostOrderIndex) {
                String text = navigator.getElementValue(mapping[1]);
                return new WrapperResult(text, distance);
            }
		return null;
	}

}
