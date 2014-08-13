package lt.kanaporis.thesis.tree;

import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mantas on 8/1/14.
 */
public class Node {

    private final NodeType type;
    private final String label;
    private final String value;
    private final Set<Node> children = new HashSet<>();
    private Node parent;

    // --- ctor --------------------------------------------

    public static Node text(String value) {
        return new Node(NodeType.TEXT, "TEXT", value);
    }

    public static Node attr(String label, String value) {
        return new Node(NodeType.ATTRIBUTE, label, value);
    }

    public static Node elem(String label) {
        return new Node(NodeType.ELEMENT, label, null);
    }

    private Node(NodeType type, String label, String value) {
        this.type = type;
        this.label = label;
        this.value = value;
    }

    // -----------------------------------------------------

    public Node addChild(Node child) {
        Validate.isTrue(type == NodeType.ELEMENT);
        children.add(child);
        child.parent = this;
        return this;
    }

    public Node addChild(Forest forest) {
        Validate.isTrue(type == NodeType.ELEMENT);
        for (Node tree : forest.getTrees()) {
            addChild(tree);
        }
        return this;
    }

    public Set<Node> getChildren() {
        return children;
    }

    public Forest getSubForest() {
        return new Forest(getChildren());
    }

    public Node getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent != null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('{');
        if (type == NodeType.ELEMENT) {
            sb.append(label);
            for (Node child : children) {
                sb.append(child.toString());
            }
        } else {
            sb.append(label).append("=\"").append(value).append('"');
        }
        sb.append('}');
        return sb.toString();
    }

    public boolean hasChild(Node node) {
        for (Node child : children) {
            if (child == node || child.hasChild(node)) {
                return true;
            }
        }
        return false;
    }

    public Forest getTail(Node distinguishedNode) {
        // TODO
        return null;
    }

    public String getText() {
        StringBuffer sb = new StringBuffer();
        if (type == NodeType.ELEMENT) {
            for (Node child : children) {
                sb.append(child.getText()).append(' ');
            }
        }
        return sb.toString().trim();
    }

    /**
     ELEMENT_NODE
     ATTRIBUTE_NODE
     TEXT_NODE
     CDATA_SECTION_NODE
     ENTITY_REFERENCE_NODE
     ENTITY_NODE
     PROCESSING_INSTRUCTION_NODE
     COMMENT_NODE
     DOCUMENT_NODE
     DOCUMENT_TYPE_NODE
     DOCUMENT_FRAGMENT_NODE
     NOTATION_NODE
     */
    public enum NodeType {
        ELEMENT,
        ATTRIBUTE,
        TEXT,
    }

}
