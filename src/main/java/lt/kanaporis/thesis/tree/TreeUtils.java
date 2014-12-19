package lt.kanaporis.thesis.tree;

public class TreeUtils {

    public static boolean areEqual(Tree tree1, Tree tree2) {
        if (tree1 == null || tree2 == null ||
                !tree1.root().equals(tree2.root()) ||
                tree1.children().size() != tree2.children().size()) {
            return false;
        }
        for (int i = 0; i < tree1.children().size(); i++) {
            if (!areEqual(tree1.child(i), tree2.child(i))) {
                return false;
            }
        }
        return true;
    }
}
