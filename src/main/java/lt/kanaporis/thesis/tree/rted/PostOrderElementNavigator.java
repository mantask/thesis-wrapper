package lt.kanaporis.thesis.tree.rted;

import javax.xml.xpath.XPathExpressionException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * Navigates DOM tree in a post-order (left, right, root) fashion.
 * Text nodes are not counted. Attribute nodes are included. 
 * 
 * @author mantas
 *
 */
public class PostOrderElementNavigator {

	private final Element body;
	private Element lookingElement;
	private int lookingIndex;
	private int curIndex = 0;
	
	public PostOrderElementNavigator(Document dom) {
		this.body = dom.body();
	}
	
	/**
	 * @return a rted element value by post order index. starts at 1.
	 */
	public String getElementValue(int lookingIndex) {
		this.lookingIndex = lookingIndex;
		return getElementValue(body);
	}
	
	/**
	 * @return a post order index of the element, identified by XPath.
	 * @throws XPathExpressionException 
	 */
	public Integer getElementIndex(String selector) {
		this.lookingElement = body.select(selector).first();
		return getElementIndex(body);
	}
	
	// ----------------------------------------------------
		
	private String getElementValue(final Element element) {
		
		// recursively handle child elements
		for (Element child : element.children()) {
			String result = getElementValue(child);
			if (result != null) {
				return result;
			}
		}
		
		curIndex++;
		
		// check if this is the one
		if (curIndex == lookingIndex) {
			return element.text();
		}

		return null;
	}
		
	private Integer getElementIndex(final Element element) {
		
		// recursively handle child elements
		for (Element child : element.children()) {
			Integer result = getElementIndex(child);
			if (result != null) {
				return result;
			}
		}
		
		curIndex++;
		
		// check if this is the one
		if (lookingElement == element) {
			return curIndex;
		}

		return null;
	}
		
}
