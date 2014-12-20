package lt.kanaporis.thesis.tree;

public class Node {

    private final NodeType type;
    private final String label;
    private final String value;

    // --- factory methods ---------------------------------

    public static Node text(String value) {
        return new Node(NodeType.TEXT, "TEXT", value);
    }

    public static Node attr(String label, String value) {
        return new Node(NodeType.ATTRIBUTE, label, value);
    }

    public static Node elem(String label) {
        return new Node(NodeType.ELEMENT, label, null);
    }

    // --- ctor --------------------------------------------

    private Node(NodeType type, String label, String value) {
        this.type = type;
        this.label = (type != NodeType.TEXT) ? label.trim().toLowerCase() : label;
        this.value = value;
    }

    // -----------------------------------------------------

    public String label() {
        return label;
    }

    public NodeType type() {
        return type;
    }

    public String value() {
        return value;
    }

    public String text() {
        return (type == NodeType.TEXT) ? value : "";
    }

    // --- Object ------------------------------------------

    @Override
    public String toString() {
        if (type == NodeType.ELEMENT) {
            return label;
        } else {
            return label + "=\"" + value + "\"";
        }
    }

    @Override
    public boolean equals(Object that) {
        return (that instanceof Node) && this.equals((Node) that);
    }

    public  boolean equals(Node that) {
        return (this == that) ||
                (that != null) &&
                (this.type == that.type) &&
                (this.label == null && that.label == null || this.label.equals(that.label)) &&
                (this.value == null && that.value == null || this.value.equals(that.value));
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + label.hashCode();
        result = value != null ? (31 * result + value.hashCode()) : 0;
        return result;
    }

    // ------------------------------------------------------

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
