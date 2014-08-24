package lt.kanaporis.thesis.tree;

import java.util.ArrayList;
import java.util.List;

public class PostOrderNavigator {

    public static List<Tree> sort(Tree tree) {
        List<Tree> sorted = new ArrayList<>();
        if (tree != null) {
            sort(tree, sorted);
        }
        return sorted;
    }

    private static void sort(Tree curr, List<Tree> sorted) {
        for (Tree child : curr.children()) {
            sort(child, sorted);
        }
        sorted.add(curr);
    }

}
