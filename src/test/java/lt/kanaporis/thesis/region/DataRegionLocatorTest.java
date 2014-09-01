package lt.kanaporis.thesis.region;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Tree;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataRegionLocatorTest {

    @Test
    public void testLocateRegions() throws Exception {
        List<Tree> regionTrees = DataRegionLocator.locate(Fixture.tableHtml.child(0));
        assertEquals(6, regionTrees.size());
        for (int i = 0; i < 6; i++) {
            assertEquals("{tr{td{TEXT=\"" + (i + 1)g + "\"}}}", regionTrees.get(i).toString());
        }
    }
}
