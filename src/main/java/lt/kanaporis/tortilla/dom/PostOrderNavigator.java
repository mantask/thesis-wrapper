package lt.kanaporis.tortilla.dom;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Navigates DOM tree in a post-order (left, right, root) fashion.
 * Text nodes are not counted. Attribute nodes are included. 
 * 
 * @author mantas
 *
 */
public class PostOrderNavigator {

	private final Document dom;
	
	public PostOrderNavigator(Document dom) {
		this.dom = dom;
	}
	
	/**
	 * @return a dom element by post order index. starts at 1.
	 */
	public Node get(int i) {
		return new PostOrderNavigatorByIndex(i).get();
	}
	
	/**
	 * @return a post order index of the element, identified by XPath.
	 * @throws XPathExpressionException 
	 */
	public Integer index(String query) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node node = (Node) xpath.evaluate(query, dom, XPathConstants.NODE);
			return new PostOrderNavigatorByNode(node).get();
		} catch (XPathExpressionException e) {
			return null;
		}
	}
	
	// -----------------------------------
	
	/**
	 * A separate class for navigation.
	 */
	private final class PostOrderNavigatorByIndex {
		
		private final int lookingIndex;
		private int curIndex = 0;
		
		public PostOrderNavigatorByIndex(final int lookingIndex) {
			this.lookingIndex = lookingIndex;
		}
		
		public Node get() {
			return get(dom.getDocumentElement());
		}
		
		private Node get(final Node node) {
			if (node.getNodeType() == Node.DOCUMENT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
				
				// iterate attributes
				NamedNodeMap attributes = node.getAttributes();
				if (attributes != null) {
					for (int i = 0; i < attributes.getLength(); i++) {
						Node result = get(attributes.item(i));
						if (result != null) {
							return result;
						}
					}
				}
			
				// recursively handle child nodes
				NodeList nodes = node.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					Node result = get(nodes.item(i));
					if (result != null) {
						return result;
					}
				}
			}
			
			curIndex++;
			if (curIndex == lookingIndex) {
				return node;
			}

			return null;
		}
		
	}
	
	// -----------------------------------
	
	/**
	 * A separate class for navigation.
	 */
	private final class PostOrderNavigatorByNode {
		
		private final Node lookingNode;
		private int curIndex = 0;
		
		public PostOrderNavigatorByNode(final Node lookingNode) {
			this.lookingNode = lookingNode;
		}
		
		public Integer get() {
			return get(dom.getDocumentElement());
		}
		
		private Integer get(final Node node) {
			if (node.getNodeType() == Node.DOCUMENT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
				
				// iterate attributes
				NamedNodeMap attributes = node.getAttributes();
				if (attributes != null) {
					for (int i = 0; i < attributes.getLength(); i++) {
						Integer result = get(attributes.item(i));
						if (result != null) {
							return result;
						}
					}
				}
			
				// recursively handle child nodes
				NodeList nodes = node.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					Integer result = get(nodes.item(i));
					if (result != null) {
						return result;
					}
				}
			}
			
			curIndex++;
			if (node.isSameNode(lookingNode)) {
				return curIndex;
			}

			return null;
		}
		
	}
	
}
