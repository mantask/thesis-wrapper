package lt.kanaporis.thesis.wrapper.chrome_dev_tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import lt.kanaporis.thesis.wrapper.ContentWrapper;
import lt.kanaporis.thesis.wrapper.WrappingResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author mantas
 *
 */
public class ChromeDevToolsWrapper implements ContentWrapper {
	private final XPathExpression optimizedQueryExpr;

	// --- INITIALIZE --------------------------------------
	
	public ChromeDevToolsWrapper(String originalDoc, String query) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new ByteArrayInputStream(originalDoc.getBytes()));

		XPath xpath = XPathFactory.newInstance().newXPath();
		Node node = (Node) xpath.evaluate(query, document, XPathConstants.NODE);
		
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			optimizedQueryExpr = xpath.compile("/");
			return;
		}
		
		List<XPathStep> steps = new ArrayList<XPathStep>();
		Node contextNode = node;
		
		while (contextNode != null) {
			XPathStep step = xPathValue(contextNode);
			if (step == null) {
				throw new RuntimeException("Error - bail out early.");
			}
			steps.add(step);
			if (step.isOptimized()) {
				break;
			}
			contextNode = contextNode.getParentNode();
		}
		
		String optimizedQuery = steps.size() > 0 && steps.get(steps.size() - 1).isOptimized() ? "" : "/";
		for (int i = steps.size() - 1; i >= 0; i--) {
			optimizedQuery += steps.get(i) + (i > 0 ? "/" : "");
		}
		optimizedQueryExpr = xpath.compile(optimizedQuery);
	}
	
	// --- CONTENT_WRAPPER --------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public WrappingResult wrap(String doc) {
		try {
			NodeList nodes = (NodeList) optimizedQueryExpr.evaluate(doc, XPathConstants.NODESET);
			if (nodes.getLength() == 0) {
				return null;
			}
			Node node = nodes.item(0);
			return new WrappingResult(node.getNodeValue(), 1.0);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	// ----------------------------------------------------------------------
	
	private XPathStep xPathValue(Node node) {
		String ownValue = "";
		int ownIndex = xPathIndex(node);
		if (ownIndex == -1) {
			return null; // Error
		}
		
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			if (node.getAttributes().getNamedItem("id") != null) {
				return new XPathStep("//*[@id=\"" + node.getAttributes().getNamedItem("id").getTextContent() + "\"]", true);
			}
			ownValue = node.getNodeName();
			break;
		case Node.ATTRIBUTE_NODE:
			ownValue = "@" + node.getNodeName();
			break;
		case Node.TEXT_NODE:
		case Node.CDATA_SECTION_NODE:
			ownValue = "text()";
			break;
       case Node.PROCESSING_INSTRUCTION_NODE:
           ownValue = "processing-instruction()";
           break;
       case Node.COMMENT_NODE:
           ownValue = "comment()";
           break;
       case Node.DOCUMENT_NODE:
           ownValue = "";
           break;
       default:
           ownValue = "";
           break;
		}
		
		if (ownIndex > 0) {
			ownValue += "[" + ownIndex + "]";
		}
		return new XPathStep(ownValue, node.getNodeType() == Node.DOCUMENT_NODE);
	}

	private int xPathIndex(Node node) {
		NodeList siblings = node.getParentNode() != null ? node.getParentNode().getChildNodes() : null;
		if (siblings == null || siblings.getLength() == 0) {
			return 0; // Root node - no siblings.
		}
		boolean hasSameNamedElements = false;
		for (int i = 0; i < siblings.getLength(); i++) {
			if (areNodesSimilar(node, siblings.item(i)) && siblings.item(i) != node) {
				hasSameNamedElements = true;
				break;
			}
		}
		if (!hasSameNamedElements) {
			return 0;
		}
		int ownIndex = 1; // XPath indices start with 1.
    	for (int i = 0; i < siblings.getLength(); ++i) {
    		if (areNodesSimilar(node, siblings.item(i))) {
				if (siblings.item(i) == node) {
					return ownIndex;
    			}
				++ownIndex;
    		}
    	}
        return -1; // An error occurred: |this| not found in parent's children.
	}
	
	/**
	 * Returns -1 in case of error, 0 if no siblings matching the same expression, <XPath index among the same expression-matching sibling nodes> otherwise.
	 */
	private boolean areNodesSimilar(Node left, Node right) {
		if (left.isSameNode(right)) { // TODO or .isEqualNode(right) ?
			return true;
		}
		
		if (left.getNodeType() == Node.ELEMENT_NODE && right.getNodeType() == Node.ELEMENT_NODE) {
			return left.getNodeName() == right.getNodeName();
		}
		if (left.getNodeType() == right.getNodeType()) {
			return true;
		}
		
		// XPath treats CDATA as text nodes.
		short leftType = left.getNodeType() == Node.CDATA_SECTION_NODE ? Node.TEXT_NODE : left.getNodeType();
		short rightType = right.getNodeType() == Node.CDATA_SECTION_NODE ? Node.TEXT_NODE : right.getNodeType();
		return leftType == rightType;
	}
}
