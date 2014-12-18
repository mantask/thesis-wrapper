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
public class DataRecordLocator {

    /**
     * Locate data records inside the tree with distinquished node in it.
     */
    public static Set<DataRecord> locate(Tree tree) {
        // TODO Node → Tree ?
        Map<Node, DiffsToNextGeneralizedNode> diffs = calcCombinations(tree);
        Map<Node, Set<DataRecord>> dataRegions = findDataRegions(tree, diffs);
        return identifyDataRecords(dataRegions.get(tree.root()));
    }

    private static Map<Node, DiffsToNextGeneralizedNode> calcCombinations(Tree tree) {
        Map<Node, DiffsToNextGeneralizedNode> diffs = new HashMap<>();
        calcRecursiveCombinations(tree, diffs);
        return diffs;
    }

    private static void calcRecursiveCombinations(Tree boundary, Map<Node, DiffsToNextGeneralizedNode> diffs) {
        diffs.put(boundary.root(), calcDirectChildCombinations(boundary));
        for (Tree child : boundary.children()) {
            calcRecursiveCombinations(child, diffs);
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

    private static Map<Node, Set<DataRecord>> findDataRegions(Tree tree, Map<Node, DiffsToNextGeneralizedNode> diffs) {
        Map<Node, Set<DataRecord>> dataRegions = new HashMap<>();
        findRecursiveDataRegions(tree, diffs, dataRegions);
        return dataRegions;
    }

    private static void findRecursiveDataRegions(Tree tree, Map<Node, DiffsToNextGeneralizedNode> diffs,
            Map<Node, Set<DataRecord>> dataRegions) {
        dataRegions.put(tree.root(), identifyDataRegions(0, tree, diffs.get(tree.root())));
        Set<DataRecord> tempDataRecords = Collections.EMPTY_SET;
        for (Tree child : tree.children()) {
            findRecursiveDataRegions(child, diffs, dataRegions);
            tempDataRecords.addAll(uncoveredDataRegions(dataRegions.get(tree.root()), child, dataRegions));
        }
        dataRegions.get(tree.root()).addAll(tempDataRecords);
    }

    /**
     * Identifying data regions below a node.
     */
    @SuppressWarnings("unchecked")
    private static Set<DataRecord> identifyDataRegions(int firstGenNode, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRecord maxDR = maxDataRegion(firstGenNode, boundary, dist);
        if (maxDR.isEmpty() || maxDR.lastPosition() == boundary.children().size()) {
            return Collections.EMPTY_SET;
        }
        Set<DataRecord> regions = Collections.singleton(maxDR);
        regions.addAll(identifyDataRegions(maxDR.lastPosition() + 1, boundary, dist));
        return regions;
    }

    private static DataRecord maxDataRegion(int firstGenNode, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRecord maxDR = DataRecord.EMPTY;
        for (int genNodeLength = 1; genNodeLength <= Config.MAX_NUMBER_OF_TAGS_PER_GENERALIZED_NODE; genNodeLength++) {
            for (int genNodeStart = firstGenNode; genNodeStart < genNodeLength; genNodeStart++) {
                DataRecord currDR = maxDataRegionFrom(genNodeStart, genNodeLength, boundary, dist);
                if (maxDR.tagNodeCount() < currDR.tagNodeCount() &&
                        (maxDR.isEmpty() || currDR.startPosition() <= maxDR.startPosition())) {
                    maxDR = currDR;
                }
            }
        }
        return maxDR;
    }

    private static DataRecord maxDataRegionFrom(int genNodeStart, int genNodeLength, Tree boundary, DiffsToNextGeneralizedNode dist) {
        DataRecord currDR = DataRecord.EMPTY;
        for (int nextGenNodeStart = genNodeStart + genNodeLength;
             nextGenNodeStart + genNodeLength <= boundary.children().size();
             nextGenNodeStart += genNodeLength) {
            if (dist.from(nextGenNodeStart - genNodeLength, genNodeLength) <= Config.EDIT_DISTANCE_THRESHOLD) {
                if (currDR.isEmpty()) {
                    // data region of two gen. nodes
                    currDR = new DataRecord(nextGenNodeStart - genNodeLength).add(boundary.subforest(nextGenNodeStart - genNodeLength, nextGenNodeStart + genNodeLength - 1));
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
    private static Set<DataRecord> uncoveredDataRegions(Set<DataRecord> parentDataRecords,
            Tree child, Map<Node, Set<DataRecord>> dataRegions) {
        Set<DataRecord> diffDataRecords = new HashSet<>();
        for (DataRecord childDataRecord : dataRegions.get(child)) {
            if (!isCoveredBy(childDataRecord, parentDataRecords)) {
                diffDataRecords.add(childDataRecord);
            }
        }
        return diffDataRecords;
    }

    private static boolean isCoveredBy(DataRecord childDataRecord, Set<DataRecord> parentDataRecords) {
        for (DataRecord parentDataRecord : parentDataRecords) {
            if (childDataRecord.isCoveredBy(parentDataRecord)) {
                return true;
            }
        }
        return false;
    }

    private static Set<DataRecord> identifyDataRecords(Set<DataRecord> dataRegion) {
        Set<DataRecord> dataRecords = new HashSet<>();
        for (DataRecord dataRecord : dataRegion) {
            dataRecords.add(findRecord(dataRecord));
        }
        return dataRecords;
    }

    private static DataRecord findRecord(DataRecord dataRecord) {
        if (dataRecord.tagNodesPerGeneralizedNode() == 1) {
            return findRecords1(dataRecord);
        } else {
            return findRecordsN(dataRecord);
        }
    }

    /**
     * Each generalized node G in DR consists of only one tag node (or component)
     * in the tag tree.
     */
    private static DataRecord findRecords1(DataRecord dataRecord) {
        Set<Tree> childTrees = dataRecord.childTagNodes();
        if (similar(childTrees) && !areTableRows(childTrees)) {
            return eachAsDataRecord(childTrees);
        } else {
            return dataRecord;
        }
    }

    private static DataRecord eachAsDataRecord(Set<Tree> childTrees) {
        DataRecord record = new DataRecord(-1);
        for (Tree tree : childTrees) {
            record.add(new Forest(tree));
        }
        return record;
    }

    private static boolean similar(Set<Tree> tagNodes) {
        // TODO
        return false;
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
    private static DataRecord findRecordsN(DataRecord dataRecord) {
        Set<Tree> childTagNodes = dataRecord.childTagNodes();
        if (similar(childTagNodes) && sameNumberOfChildren(dataRecord.childTagNodes())) {
            return groupCorrespondingChildrenOfEveryTagNode(dataRecord);
        } else {
            return dataRecord;
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

    private static DataRecord groupCorrespondingChildrenOfEveryTagNode(DataRecord oldRecord) {
        DataRecord newRecord = new DataRecord();
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
