package lt.kanaporis.thesis.region;

import convenience.RTED;
import lt.kanaporis.thesis.Config;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.RtedMapper;
import lt.kanaporis.thesis.tree.Tree;

import java.util.*;

/**
 * Locates data records inside a tree. Based on [Liu'03] paper
 * "Mining Data Records in Web Pages".
 */
public class DataRegionLocator {

    /**
     * Locate data records inside the tree with distinquished node in it.
     */
    public static Set<DataRegion> locate(Tree tree) {
        Map<Tree, DiffsToNextGeneralizedNode> diffs = calcCombinations(tree);
        Map<Tree, Set<DataRegion>> dataRegions = findDataRegions(tree, diffs);
        return identifyDataRecords(dataRegions.get(tree));
    }

    private static Map<Tree, DiffsToNextGeneralizedNode> calcCombinations(Tree tree) {
        Map<Tree, DiffsToNextGeneralizedNode> diffs = new HashMap<>();
        calcRecursiveCombinations(tree, diffs);
        return diffs;
    }

    private static void calcRecursiveCombinations(Tree boundary, Map<Tree, DiffsToNextGeneralizedNode> diffs) {
        diffs.put(boundary, calcDirectChildCombinations(boundary));
        for (Tree child : boundary.children()) {
            if (child.children().size() > 0) {
                calcRecursiveCombinations(child, diffs);
            }
        }
    }

    /**
     * Compares different length combination similarity of direct children regions.
     * ref. CombComp(NodeList, K), fig. 6, [Liu'03]
     */
    private static DiffsToNextGeneralizedNode calcDirectChildCombinations(Tree boundary) {

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

                    // TODO could compare String edit-dist instead. add a Config switch for this. Abstract away distance calculations.
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

    /**
     * Locate data regions inside direct children of boundary node.
     */
/*
    public static List<Tree> locate(Tree boundary) {
        DiffsToNextGeneralizedNode dist = calcDirectChildCombinations(boundary);
        Set<DataRegion> regions = dataRegions(0, boundary, dist);
        return mapTrees(boundary, regions);
    }
*/

    /**
     * Map data regions to subtrees.
     */
/*
    private static List<Tree> mapTrees(Tree boundary, Set<DataRegion> regions) {
        List<Tree> trees = new ArrayList<>();
        for (DataRegion region : regions) {
            for (int i = region.startPosition(); i <= region.lastPosition(); i += region.tagNodesPerGeneralizedNode()) {
                trees.add(boundary.subtree(i, i + region.tagNodesPerGeneralizedNode()));
            }
        }
        return trees;
    }
*/

    private static Map<Tree, Set<DataRegion>> findDataRegions(Tree tree, Map<Tree, DiffsToNextGeneralizedNode> diffs) {
        Map<Tree, Set<DataRegion>> dataRegions = new HashMap<>();
        findRecursiveDataRegions(tree, diffs, dataRegions);
        return dataRegions;
    }

    private static void findRecursiveDataRegions(Tree tree, Map<Tree, DiffsToNextGeneralizedNode> diffs,
            Map<Tree, Set<DataRegion>> dataRegions) {
        if (!tree.depthAtLeast(3)) {
            return;
        }
        dataRegions.put(tree, identifyDataRegions(0, tree, diffs.get(tree)));
        Set<DataRegion> tempDataRegions = new LinkedHashSet<>();
        for (Tree child : tree.children()) {
            findRecursiveDataRegions(child, diffs, dataRegions);
            tempDataRegions.addAll(uncoveredDataRegions(dataRegions.get(tree), child, dataRegions));
        }
        dataRegions.get(tree).addAll(tempDataRegions);
    }

    /**
     * Identifying data regions below a node.
     */
    @SuppressWarnings("unchecked")
    private static Set<DataRegion> identifyDataRegions(int firstGenNode, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRegion maxDR = maxDataRegion(firstGenNode, boundary, dist);
        //if (maxDR.isEmpty() || maxDR.lastPosition() == boundary.children().size()) { // TODO why do we check for LastPos = Count(Children)
        if (maxDR.isEmpty()) {
            return new HashSet<>();
        }
        Set<DataRegion> regions = new HashSet<>();
        regions.add(maxDR);
        regions.addAll(identifyDataRegions(maxDR.lastPosition() + 1, boundary, dist));
        return regions;
    }

    private static DataRegion maxDataRegion(int firstGenNode, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRegion maxDR = DataRegion.EMPTY;
        for (int genNodeLength = 1; genNodeLength <= Config.MAX_NUMBER_OF_TAGS_PER_GENERALIZED_NODE; genNodeLength++) {
            for (int genNodeStart = firstGenNode; genNodeStart < genNodeLength; genNodeStart++) {
                DataRegion currDR = maxDataRegionFrom(genNodeStart, genNodeLength, boundary, dist);
                if (maxDR.tagNodeCount() < currDR.tagNodeCount() &&
                        (maxDR.isEmpty() || currDR.startPosition() <= maxDR.startPosition())) {
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
                if (currDR.isEmpty()) {
                    currDR = new DataRegion(nextGenNodeStart - genNodeLength)
                            .add(boundary.subforest(nextGenNodeStart - genNodeLength, nextGenNodeStart - 1))
                            .add(boundary.subforest(nextGenNodeStart, nextGenNodeStart + genNodeLength - 1));
                } else {
                    currDR.add(boundary.subforest(nextGenNodeStart, nextGenNodeStart + genNodeLength - 1));
                }
            } else if (!currDR.isEmpty()) {
                break;
            }
        }
        return currDR;
    }

    /**
     * Return DataRegions of a child that are not covered by any DR of a parent.
     */
    private static Set<DataRegion> uncoveredDataRegions(Set<DataRegion> parentDataRegions,
            Tree child, Map<Tree, Set<DataRegion>> dataRegions) {
        Set<DataRegion> diffDataRegions = new HashSet<>();
        if (dataRegions.containsKey(child)) {
            for (DataRegion childDataRegion : dataRegions.get(child)) {
                if (!childDataRegion.isCoveredBy(parentDataRegions)) {
                    diffDataRegions.add(childDataRegion);
                }
            }
        }
        return diffDataRegions;
    }

    private static Set<DataRegion> identifyDataRecords(Set<DataRegion> dataRegion) {
        Set<DataRegion> dataRegions = new HashSet<>();
        for (DataRegion dataRecord : dataRegion) {
            dataRegions.add(findRecord(dataRecord));
        }
        return dataRegions;
    }

    private static DataRegion findRecord(DataRegion dataRegion) {
        if (dataRegion.tagNodesPerGeneralizedNode() == 1) {
            return findRecords1(dataRegion);
        } else {
            return findRecordsN(dataRegion);
        }
    }

    /**
     * Each generalized node G in DR consists of only one tag node (or component)
     * in the tag tree.
     */
    private static DataRegion findRecords1(DataRegion dataRegion) {
        Set<Tree> childTrees = dataRegion.childTagNodes();
        if (similar(childTrees) && !areTableRows(childTrees)) {
            return eachAsDataRecord(childTrees);
        } else {
            return dataRegion;
        }
    }

    private static DataRegion eachAsDataRecord(Set<Tree> childTrees) {
        DataRegion record = new DataRegion(-1);
        for (Tree tree : childTrees) {
            record.add(new Forest(tree));
        }
        return record;
    }

    private static boolean similar(Set<Tree> tagNodes) {
        if (tagNodes.size() > 1) {
            Tree prev = null;
            for (Tree curr : tagNodes) {
                if (prev != null && prev.substantiallyDifferentFrom(curr)) {
                    return false;
                }
                prev = curr;
            }
        }
        return true;
    }

    private static boolean areTableRows(Set<Tree> childTrees) {
        for (Tree tagNode : childTrees) {
            if (tagNode.root().type() != Node.NodeType.ELEMENT || tagNode.root().label() != "tr") {
                return false;
            }
        }
        return true;
    }

    /**
     * A generalized node G in DR consists of n tag nodes (n > 1) or components
     */
    private static DataRegion findRecordsN(DataRegion dataRegion) {
        Set<Tree> childTagNodes = dataRegion.childTagNodes();
        if (similar(childTagNodes) && sameNumberOfChildren(dataRegion.childTagNodes())) {
            return groupCorrespondingChildrenOfEveryTagNode(dataRegion);
        } else {
            return dataRegion;
        }
    }

    private static boolean sameNumberOfChildren(Set<Tree> tagNodes) {
        if (tagNodes.size() == 0) {
            return true;
        }
        int childCount = tagNodes.iterator().next().children().size();
        for (Tree tagNode : tagNodes) {
            if (tagNode.children().size() != childCount) {
                return false;
            }
        }
        return true;
    }

    private static DataRegion groupCorrespondingChildrenOfEveryTagNode(DataRegion oldRecord) {
        DataRegion newRecord = new DataRegion();
        for (Forest oldGenNode : oldRecord.generalizedNodes()) {
            for (int childPosition = 0; childPosition < oldGenNode.tree(0).children().size(); childPosition++) {
                Forest newGenNode = new Forest();
                for (Tree tagNode : oldGenNode.trees()) {
                    newGenNode = newGenNode.add(tagNode.child(childPosition));
                }
                newRecord.add(oldGenNode);
            }
        }
        return newRecord;
    }
}
