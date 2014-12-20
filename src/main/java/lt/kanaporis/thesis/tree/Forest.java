package lt.kanaporis.thesis.tree;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Immutable forest of ordered rooted trees.
 */
public class Forest {

    public static final Forest EMPTY = new Forest();

    private final List<Tree> trees;

    // ---- ctor ---------------------------------

    public Forest(Tree... trees) {
        this(Arrays.asList(trees));
    }

    public Forest(List<Tree> trees) {
        if (trees == null) {
            this.trees = Collections.EMPTY_LIST;
        } else {
            this.trees = ImmutableList.copyOf(Iterables.filter(trees, Predicates.notNull()));
        }
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

    public Forest add(Tree... trees) {
        return add(Arrays.asList(trees));
    }

    public int size() {
        return trees.size();
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

    public boolean isCoveredBy(Forest parentForest) {
        for (Tree child : this.trees()) {
            for (Tree parent : parentForest.trees()) {
                if (child.isCoveredBy(parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Generalizes two forests into one.
     */
    public Forest merge(Forest that) {
        // TODO implement
        return null;
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
