package lt.kanaporis.tortilla.wrapper.ted;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.LblTree;

public class RTEDMapper {

	/**
	 * @param doc
	 *            XML document
	 * @return Tree representation for RTED input, e.g. {html{head}{body{h1{#text}}{p{#text}}
	 */
	public static String map(Document doc) {
		return walk(doc.getDocumentElement());
	}

	/**
	 * Map current DOM node and recursively walk through children
	 */
	private static String walk(Node node) {
		StringBuilder sb = new StringBuilder();
		sb.append(LblTree.OPEN_BRACKET);
		sb.append(toString(node));
		if (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.DOCUMENT_NODE) {
			
			// iterate attributes
			NamedNodeMap attributes = node.getAttributes();
			if (attributes != null) {
				for (int i = 0; i < attributes.getLength(); i++) {
					sb.append(walk(attributes.item(i)));
				}
			}
				
			// recursively handle child nodes
			NodeList nodes = node.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				sb.append(walk(nodes.item(i)));
			}

		}
		sb.append(LblTree.CLOSE_BRACKET);
		return sb.toString();
	}

	/**
	 * @return The string representation of node name
	 */
	public static String toString(final Node node) {
		if (node == null) {
			return null;
		}
		
		if (node.getNodeType() == Node.DOCUMENT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
			return node.getNodeName();
			
		} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			return "@" + node.getNodeName();
			
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			return "#text";
			
		} else {
			// unknown node type. see http://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Node.html
			return "?";
		}
	}
	
}
