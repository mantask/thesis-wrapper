package lt.kanaporis.thesis.region;

import convenience.RTED;
import lt.kanaporis.thesis.Config;
import lt.kanaporis.thesis.tree.RtedMapper;
import lt.kanaporis.thesis.tree.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DataRegionLocator {

    /**
     * Locate data regions inside direct children of boundary node.
     */
    public static List<Tree> locate(Tree boundary) {
        DiffsToNextGeneralizedNode dist = childCombinations(boundary);
        Set<DataRegion> regions = dataRegions(0, boundary, dist);
        return mapTrees(boundary, regions);
    }

    /**
     * Map data regions to subtrees.
     */
    private static List<Tree> mapTrees(Tree boundary, Set<DataRegion> regions) {
        List<Tree> trees = new ArrayList<>();
        for (DataRegion region : regions) {
            for (int i = region.startPosition(); i <= region.lastPosition(); i += region.tagNodesPerGenNode()) {
                trees.add(boundary.subtree(i, i + region.tagNodesPerGenNode()));
            }
        }
        return trees;
    }

    /**
     * Identifying data regions below a node.
     */
    @SuppressWarnings("unchecked")
    private static Set<DataRegion> dataRegions(int firstGenNode, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRegion maxDR = maxDataRegion(firstGenNode, boundary, dist);
        if (!maxDR.empty() && maxDR.lastPosition() != boundary.children().size()) {
            Set<DataRegion> regions = Collections.singleton(maxDR);
            regions.addAll(dataRegions(maxDR.lastPosition() + 1, boundary, dist));
            return regions;
        }
        return Collections.EMPTY_SET;
    }

    private static DataRegion maxDataRegion(int firstGenNode, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRegion maxDR = DataRegion.EMPTY;
        for (int genNodeLength = 1; genNodeLength <= Config.MAX_NUMBER_OF_TAGS_PER_GENERALIZED_NODE; genNodeLength++) {
            for (int genNodeStart = firstGenNode; genNodeStart < genNodeLength; genNodeStart++) {
                DataRegion currDR = maxDataRegionFrom(genNodeStart, genNodeLength, boundary, dist);
                if (maxDR.tagNodesTotalCount() < currDR.tagNodesTotalCount() &&
                        (maxDR.empty() || currDR.startPosition() <= maxDR.startPosition())) {
                    maxDR = currDR;
                }
            }
        }
        return maxDR;
    }

    private static DataRegion maxDataRegionFrom(int genNodeStart, int genNodeLength, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRegion currDR = DataRegion.EMPTY;
        for (int nextGenNodeStart = genNodeStart + genNodeLength;
             nextGenNodeStart + genNodeLength <= boundary.children().size();
             nextGenNodeStart += genNodeLength) {
            if (dist.from(nextGenNodeStart - genNodeLength, genNodeLength) <= Config.EDIT_DISTANCE_THRESHOLD) {
                if (currDR.empty()) {
                    // data region of two gen. nodes
                    currDR = new DataRegion(genNodeLength, nextGenNodeStart - genNodeLength, 2 * genNodeLength);
                } else {
                    currDR = currDR.expandByOne();
                }
            } else if (!currDR.empty()) {
                break;
            }
        }
        return currDR;
    }

    /**
     * Compares different length combination similarity of direct children regions.
     * ref. CombComp(NodeList, K), fig. 6, [Liu'03]
     */
    private static DiffsToNextGeneralizedNode childCombinations(Tree boundary) {

        DiffsToNextGeneralizedNode editDists = new DiffsToNextGeneralizedNode(
                boundary.children().size(),
                Config.MAX_NUMBER_OF_TAGS_PER_GENERALIZED_NODE);

        // different generalized node start indexes
        for (int firstGenNodeStart = 0;
                firstGenNodeStart < Config.MAX_NUMBER_OF_TAGS_PER_GENERALIZED_NODE &&
                        firstGenNodeStart < boundary.children().size();
                firstGenNodeStart++) {

            // different generalized node lengths to cover all pairs
            for (int genNodeLength = firstGenNodeStart + 1;
                    genNodeLength <= Config.MAX_NUMBER_OF_TAGS_PER_GENERALIZED_NODE &&
                            // search for a data region of min two generalized nodes
                            containsRegion(boundary, firstGenNodeStart + genNodeLength, genNodeLength);
                    genNodeLength++) {

                Tree currGenNode = boundary.subtree(firstGenNodeStart, firstGenNodeStart + genNodeLength);
                String mappedCurrGenNode = RtedMapper.map(currGenNode);

                // sliding window for subsequent generalized nodes
                for (int nextGenNodeStart = firstGenNodeStart + genNodeLength;
                        containsRegion(boundary, nextGenNodeStart, genNodeLength);
                        nextGenNodeStart += genNodeLength) {

                    Tree nextGenNode = boundary.subtree(nextGenNodeStart, nextGenNodeStart + genNodeLength);
                    String mappedNextGenNode = RtedMapper.map(nextGenNode);

                    // TODO could compare String edit-dist instead
                    // TODO how about probabilistic change model and edit distance?
                    double editDist = !currGenNode.substantiallyDifferentFrom(nextGenNode)
                            ? RTED.computeDistance(mappedCurrGenNode, mappedNextGenNode) : 0.0;
                    editDists.from(nextGenNodeStart - genNodeLength, genNodeLength, editDist);

                    currGenNode = nextGenNode;
                    mappedCurrGenNode = mappedNextGenNode;
                }
            }
        }
        return editDists;
    }

    private static boolean containsRegion(Tree root, int start, int length) {
        return start + length <= root.children().size();
    }
}
