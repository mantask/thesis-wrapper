package lt.kanaporis.thesis.region;

import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Tree;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class DataRegion {

    public static final DataRegion EMPTY = new DataRegion(0);

    private final int startPosition;
    private final Set<Forest> generalizedNodes = new LinkedHashSet<>();

    public DataRegion() {
        this(-1);
    }

    public DataRegion(int startPosition) {
        this.startPosition = startPosition;
    }

    public DataRegion add(Forest genNode) {
        Validate.isTrue(generalizedNodes.isEmpty() || genNode.size() == tagNodesPerGeneralizedNode());
        generalizedNodes.add(genNode);
        return this;
    }

    public boolean isEmpty() {
        return generalizedNodes.isEmpty();
    }

    public boolean isCoveredBy(Set<DataRegion> parentDataRegions) {
        for (DataRegion parentDataRegion : parentDataRegions) {
            if (isCoveredBy(parentDataRegion)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCoveredBy(DataRegion that) {
        for (Forest thisGenNode : this.generalizedNodes) {
            for (Forest thatGenNode : that.generalizedNodes) {
                if (thisGenNode.isCoveredBy(thatGenNode)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Integer tagNodesPerGeneralizedNode() {
        if (generalizedNodes.isEmpty()) {
            return 0;
        }
        Forest lastGenNode = generalizedNodes.iterator().next();
        return (lastGenNode != null) ? lastGenNode.size() : 0;
    }

    public int tagNodeCount() {
        return tagNodesPerGeneralizedNode() * generalizedNodes.size();
    }

    public Set<Forest> generalizedNodes() {
        return Collections.unmodifiableSet(generalizedNodes);
    }

    public int startPosition() {
        return startPosition;
    }

    public int lastPosition() {
        return startPosition() + tagNodeCount();
    }

    public Set<Tree> childTagNodes() {
        Set<Tree> childTrees = new LinkedHashSet<>();
        for (Forest genNode : generalizedNodes) {
            childTrees.addAll(genNode.trees());
        }
        return childTrees;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Forest genNode : generalizedNodes) {
            if (sb.length() > 1) {
                sb.append(',');
            }
            sb.append(genNode.toString());
        }
        sb.append(')');
        return sb.toString();
    }
}
