package lt.kanaporis.thesis.tree;

import lt.kanaporis.thesis.Config;
import org.apache.commons.lang3.Validate;

import java.util.*;

public class Tree {

    private final Node root;
    private final List<Tree> children;
    private final int nodeCount;

    public Tree(Node root, Tree... children) {
        Validate.notNull(root);
        this.root = root;
        this.nodeCount = countAllNodes(children);
        if (children.length > 0) {
            Validate.isTrue(root.type() == Node.NodeType.ELEMENT);
            this.children = Collections.unmodifiableList(Arrays.asList(children));
        } else {
            this.children = Collections.EMPTY_LIST;
        }
    }

    private int countAllNodes(Tree... children) {
        int nodeCount = 1;
        for (Tree child : children) {
            nodeCount += child.nodeCount();
        }
        return nodeCount;
    }

    public Tree(Node root, Collection<Tree> children) {
        this(root, children.toArray(new Tree[] {}));
    }

    public Tree(Node root, Forest forest) {
        this(root, forest.trees());
    }

    public Node root() {
        return root;
    }

    public List<Tree> children() {
        return children;
    }

    public Tree child(int i) {
        return children.get(i);
    }

    public String text() {
        StringBuffer sb = new StringBuffer();
        sb.append(root.text());
        for (Tree child : children) {
            if (lastCharNotWhiteSpace(sb)) {
                sb.append(' ');
            }
            sb.append(child.text());
        }
        return sb.toString().trim();
    }

    private boolean lastCharNotWhiteSpace(StringBuffer sb) {
        return sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ';
    }

    public Tree tail(Node distNode) {
        if (root == distNode) {
            return null;
        }
        for (int i = 0; i < children.size(); i++) {
            if (child(i).contains(distNode)) {
                List<Tree> tail = new ArrayList<>();

                // try adding subtree recursively
                Tree subtail = child(i).tail(distNode);
                if (subtail != null) {
                    tail.add(subtail);
                }

                // add sibling trees to the right
                if (i + 1 < children.size()) {
                    tail.addAll(children.subList(i + 1, children.size()));
                }

                return new Tree(root, tail);
            }
        }
        throw new RuntimeException("Distinquished node not found!");
    }

    public Forest prefix(Node distNode) {
        return prefix(distNode, new Forest());
    }

    private Forest prefix(Node distNode, Forest f) {
        if (root == distNode) { // can we use equals here?
            return f;
        }
        for (int i = 0; i < children.size(); i++) {
            if (child(i).contains(distNode)) {
                return children.get(i).prefix(distNode,
                        (i > 0) ? f.add(children.subList(0, i)) : f);
            }
        }
        throw new RuntimeException("Distinquished node not found!");
    }

    public boolean contains(Node node) {
        if (root == node) {
            return true;
        }
        for (Tree child : children) {
            if (child.contains(node)) {
                return true;
            }
        }
        return false;
    }

    public Forest subforest() {
        return new Forest(children.toArray(new Tree[] {}));
    }

    public int nodeCount() {
        return nodeCount;
    }

    /**
     * Heuristics for determining, if two trees are very different
     */
    public boolean substantiallyDifferentFrom(Tree that) {
        // TODO experiment with threshold
        double diff = Math.abs(this.nodeCount() - that.nodeCount());
        double max = Math.max(this.nodeCount(), that.nodeCount());
        return (diff / max) > Config.MAX_ALLOWED_TREE_DIFFERENCE;
    }

    // --- Object ---------------------------------------------

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('{').append(root.toString());
        for (Tree child : children) {
            sb.append(child.toString());
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        return (that instanceof Tree) && this.equals((Tree) that);
    }

    public boolean equals(Tree that) {
        if (that == null ||
                !this.root.equals(that.root) ||
                this.children.size() != that.children.size()) {
            return false;
        }
        for (int i = 0; i < children.size(); i++) {
            if (!this.child(i).equals(that.child(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = root.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }
}
