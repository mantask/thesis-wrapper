package lt.kanaporis.thesis.region;

public class DataRegion {
    public static final DataRegion EMPTY = new DataRegion(0, 0, 0);

    private final int tagNodesPerGenNode;
    private final int startPosition;
    private final int tagNodesTotalCount;

    public DataRegion(int tagNodesPerGenNode, int startPosition, int tagNodesTotalCount) {
        this.tagNodesPerGenNode = tagNodesPerGenNode;
        this.startPosition = startPosition;
        this.tagNodesTotalCount = tagNodesTotalCount;
    }

    public int tagNodesPerGenNode() {
        return tagNodesPerGenNode;
    }

    public int startPosition() {
        return startPosition;
    }

    public int tagNodesTotalCount() {
        return tagNodesTotalCount;
    }

    public int lastPosition() {
        return startPosition + tagNodesTotalCount - 1;
    }

    public DataRegion expandByOne() {
        return new DataRegion(
                tagNodesPerGenNode,
                startPosition,
                tagNodesTotalCount + tagNodesPerGenNode);
    }

    public boolean empty() {
        return tagNodesTotalCount == 0;
    }
}
