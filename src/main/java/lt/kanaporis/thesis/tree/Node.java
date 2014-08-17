package lt.kanaporis.thesis.tree;

public class Node {

    private final NodeType type;
    private final String label;
    private final String value;

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
        this.label = (type != NodeType.TEXT) ? label.toLowerCase() : label;
        this.value = (value != null) ? value.trim() : null;
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
