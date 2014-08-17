package lt.kanaporis.thesis.tree;

import org.apache.commons.lang3.Validate;

import java.util.*;

public class PostOrderNavigator {

    public static List<Tree> sort(Tree tree) {
        Validate.notNull(tree);
        List<Tree> sorted = new ArrayList<>();
        sort(tree, sorted);
        return sorted;
    }

    public static void sort(Tree curr, List<Tree> sorted) {
        for (Tree child : curr.children()) {
            sort(child, sorted);
        }
        sorted.add(curr);
    }

}
