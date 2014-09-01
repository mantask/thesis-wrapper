package lt.kanaporis.thesis.region;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataRegionLocatorTest {

    @Test
    public void testLocateBasicRegions() throws Exception {
        List<Tree> regionTrees = DataRegionLocator.locate(Fixture.tableHtml.child(0));
        assertEquals(6, regionTrees.size());
        for (int i = 0; i < 6; i++) {
            assertEquals("{tr{td{TEXT=\"" + (i + 1) + "\"}}}", regionTrees.get(i).toString());
        }
    }

    @Test
    public void testLocateNonExistentRegions() throws Exception {
        List<Tree> regionTrees = DataRegionLocator.locate(Fixture.origHtml.child(1));
        assertEquals(0, regionTrees.size());
    }

    @Test
    public void testLocateMoreComplexTable() throws Exception {
        List<Tree> regionTrees = DataRegionLocator.locate(Fixture.movieHtml);
        assertEquals(3, regionTrees.size());
        assertEquals("{tr{td{strong{TEXT=\"1. Guardians of the Galaxy (2014)\"}}{br}{TEXT=\"Weekend: $16.3M\"}}}", regionTrees.get(0).toString());
        assertEquals("{tr{td{strong{TEXT=\"2. Teenage Mutant Ninja Turtles (2014)\"}}{br}{TEXT=\"Weekend: $11.8M\"}}}", regionTrees.get(1).toString());
        assertEquals("{tr{td{strong{TEXT=\"3. If I Stay (2014)\"}}{br}{TEXT=\"Weekend: $9.3M\"}}}", regionTrees.get(2).toString());
    }
}