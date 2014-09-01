package lt.kanaporis.thesis;

public final class Config {

    /**
     * Probabilistic transducer optimization for similar trees (ref §4.1.1 Dalvi'09)
     */
    public static final boolean ENABLE_OPTIMIZATION_FOR_SIMILAR_TREES = true;

    /**
     * Max allowed tree difference (in percentage) between node count for significant difference.
     */
    public static final double MAX_ALLOWED_TREE_DIFFERENCE = 2.0 / 3;

    /**
     * Maximum number of tag nodes that a generalized node can have.
     */
    public static final int MAX_NUMBER_OF_TAGS_PER_GENERALIZED_NODE = 3;

    /**
     * Edit distance threshold is needed to decide whether two strings are similar.
     * A set of training pages is used to decide it.
     * TODO should take into consideration the probabilistic model
     */
    public static final double EDIT_DISTANCE_THRESHOLD = 2;

    // TODO public static final boolean ENABLE_CONTENT_MODEL = false;

}
