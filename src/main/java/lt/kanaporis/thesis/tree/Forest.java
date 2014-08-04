package lt.kanaporis.thesis.tree;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Immutable forest of ordered rooted trees.
 *
 * Created by mantas on 8/1/14.
 */
// TODO make it functional (immutable) data structure
public class Forest {

    public final Set<Node> trees;

    // ---- ctor ---------------------------------

    public Forest() {
        trees = Collections.EMPTY_SET;
    }

    public Forest(Node... trees) {
        Set<Node> newForestTrees = new LinkedHashSet<>();
        for (Node tree : trees) {
            newForestTrees.add(tree);
        }
        this.trees = Collections.unmodifiableSet(newForestTrees);
    }

    public Forest(Forest... forests) {
        Set<Node> newForestTrees = new LinkedHashSet<>();
        for (Forest forest : forests) {
            newForestTrees.addAll(forest.getTrees());
        }
        this.trees = Collections.unmodifiableSet(newForestTrees);
    }

    public Forest(Set<Node> nodes) {
        Set<Node> newForestTrees = new LinkedHashSet<>();
        newForestTrees.addAll(nodes);
        this.trees = Collections.unmodifiableSet(newForestTrees);
    }

    // --------------------------------------------

    public Forest add(Node... trees) {
        Set<Node> newForestTrees = new LinkedHashSet<>();
        newForestTrees.addAll(this.trees);
        for (Node tree : trees) {
            newForestTrees.add(tree);
        }
        return new Forest(newForestTrees);
    }

    public Forest add(Forest... forests) {
        Set<Node> newForestTrees = new LinkedHashSet<>();
        newForestTrees.addAll(this.trees);
        for (Forest forest : forests) {
            newForestTrees.addAll(forest.getTrees());
        }
        return new Forest(newForestTrees);
    }

    public Forest removeNode(Node node) {
        // TODO
        return null;
    }

    public Forest removeTree(Node tree) {
        // TODO
        return null;
    }

    public boolean isTree() {
        return trees.size() == 1;
    }

    public Node getLastTree() {
        return (Node) trees.toArray()[trees.size() - 1];
    }

    public Forest getPrefix(Node u) {
        // TODO
        return null;
    }

    public Forest getTail(Node u) {
        // TODO
        return null;
    }

    public Set<Node> getTrees() {
        return trees;
    }

    public boolean isEmpty() {
        return trees.size() == 0;
    }
}
