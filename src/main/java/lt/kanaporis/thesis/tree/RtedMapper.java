package lt.kanaporis.thesis.tree;

public class RtedMapper {

    public static String map(final Tree tree) {
        StringBuffer sb = new StringBuffer();
        sb.append('{').append(tree.root().label());
        for (Tree child : tree.children()) {
            sb.append(map(child));
        }
        sb.append('}');
        return sb.toString();
    }

}
