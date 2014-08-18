package lt.kanaporis.thesis.tree;

import java.util.*;

/**
 * Immutable forest of ordered rooted trees.
 */
public class Forest {

    public static final Forest EMPTY = new Forest();

    private final List<Tree> trees;

    // ---- ctor ---------------------------------

    public Forest(Tree... trees) {
        this.trees = Collections.unmodifiableList(Arrays.asList(trees));
    }

    public Forest(List<Tree> trees) {
        this.trees = Collections.unmodifiableList(trees);
    }

    // --------------------------------------------

    public List<Tree> trees() {
        return trees;
    }

    public Tree tree(int i) {
        return trees.get(i);
    }

    public Forest add(Forest that) {
        return add(that.trees());
    }

    public Forest add(List<Tree> newTrees) {
        List<Tree> trees = new ArrayList<>(this.trees.size() + newTrees.size());
        trees.addAll(this.trees);
        trees.addAll(newTrees);
        return new Forest(trees);
    }

    public boolean empty() {
        return trees.size() == 0;
    }

    public Tree rightmostTree() {
        if (empty()) {
            return null;
        }
        return trees.get(lastTreeIndex());
    }

    public Forest removeRightmostTree() {
        if (empty()) {
            return EMPTY;
        }
        return new Forest(trees.subList(0, lastTreeIndex()));
    }

    public Forest removeRightmostRoot() {
        if (empty()) {
            return EMPTY;
        }
        List<Tree> trees = new ArrayList<>();
        trees.addAll(this.trees.subList(0, lastTreeIndex()));
        trees.addAll(this.trees.get(lastTreeIndex()).children());
        return new Forest(trees);
    }

    private int lastTreeIndex() {
        return trees.size() - 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tree t : trees) {
            sb.append(t.toString());
        }
        return sb.toString();
    }
}
