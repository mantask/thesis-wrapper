package lt.kanaporis.thesis;

public final class Config {

    /**
     * Probabilistic transducer optimization for similar trees (ref §4.1.1 Dalvi'09)
     */
    public static final boolean ENABLE_OPTIMIZATION_FOR_SIMILAR_TREES = true;

    /**
     * Max allowed tree difference (in percentage) between node count.
     */
    public static final double MAX_ALLOWED_TREE_DIFFERENCE = 2.0 / 3;

    // TODO public static final boolean ENABLE_CONTENT_MODEL = false;

}
