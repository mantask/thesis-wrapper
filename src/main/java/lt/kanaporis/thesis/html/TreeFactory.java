package lt.kanaporis.thesis.html;

import lt.kanaporis.thesis.tree.Node;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class TreeFactory {

    public static Node buildFromDom(Document dom) {
        return walk(dom.body());
    }

    /**
     * Map current DOM node and recursively walk through children
     */
    private static Node walk(final org.jsoup.nodes.Node elem) {
        Node node = map(elem);
        if (node != null) {
            for (org.jsoup.nodes.Node childElem : elem.childNodes()) {
                Node childNode = walk(childElem);
                if (childNode != null) {
                    node.addChild(childNode);
                }
            }
        }
        return node;
    }

    private static Node map(final org.jsoup.nodes.Node el) {
        if (el instanceof Element) {
            return Node.elem(((Element) el).tagName());
        } else if (el instanceof TextNode) {
            return Node.text(((TextNode) el).text());
        } else {
            return null;
        }
        // TODO Comment, DataNode, DocumentType, Element, TextNode, XmlDeclaration
    }

}
