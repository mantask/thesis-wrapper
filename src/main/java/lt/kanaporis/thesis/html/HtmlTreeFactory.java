package lt.kanaporis.thesis.html;

import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

/**
 * Builds internal tree structure from given HTML.
 */
public class HtmlTreeFactory {

    public static Tree buildFromDom(Document dom) {
        return walk(dom.body());
    }

    /**
     * Map current DOM node and recursively walk through children
     */
    private static Tree walk(final org.jsoup.nodes.Node elem) {
        Node node = map(elem);
        if (node != null) {
            // TODO
            /*
            List<Node>
            for (org.jsoup.nodes.Node childElem : elem.childTagNodes()) {
                Node childNode = walk(childElem);
                if (childNode != null) {
                    node.addChild(childNode);
                }
            }
            */
        }
        return new Tree(node);
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
