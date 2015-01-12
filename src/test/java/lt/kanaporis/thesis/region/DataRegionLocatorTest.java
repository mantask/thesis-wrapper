package lt.kanaporis.thesis.region;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Forest;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class DataRegionLocatorTest {

    @Test
    public void testLocateBasicRegions() throws Exception {
        Set<DataRegion> records = DataRegionLocator.locate(Fixture.TABLE_HTML.child(0));
        assertEquals(1, records.size());

        DataRegion record = records.iterator().next();
        assertEquals(6, record.generalizedNodes().size());

        int i = 1;
        for (Forest generalizedNode : record.generalizedNodes()) {
            assertEquals("{td{TEXT=\"" + i + "\"}}", generalizedNode.toString());
            i++;
        }
    }

    @Test
    public void testLocateTextRegions() throws Exception {
        Set<DataRegion> records = DataRegionLocator.locate(Fixture.ORIG_HTML.child(1));
        assertEquals(1, records.size());

        DataRegion record = records.iterator().next();
        assertEquals(3, record.generalizedNodes().size());

        Iterator<Forest> genNodes = record.generalizedNodes().iterator();

        assertEquals("{TEXT=\"Body text goes \"}", genNodes.next().toString());
        assertEquals("{strong{TEXT=\"here\"}}", genNodes.next().toString());
        assertEquals("{TEXT=\"!\"}", genNodes.next().toString());
    }

    @Test
    public void testLocateMoreComplexTable() throws Exception {
        Set<DataRegion> records = DataRegionLocator.locate(Fixture.MOVIE_HTML);
        assertEquals(1, records.size());

        DataRegion record = records.iterator().next();
        assertEquals(3, record.generalizedNodes().size());

        Iterator<Forest> genNodes = record.generalizedNodes().iterator();

        assertEquals("{td{strong{TEXT=\"1. Guardians of the Galaxy (2014)\"}}{br}{TEXT=\"Weekend: $16.3M\"}}", genNodes.next().toString());
        assertEquals("{td{strong{TEXT=\"2. Teenage Mutant Ninja Turtles (2014)\"}}{br}{TEXT=\"Weekend: $11.8M\"}}", genNodes.next().toString());
        assertEquals("{td{strong{TEXT=\"3. If I Stay (2014)\"}}{br}{TEXT=\"Weekend: $9.3M\"}}", genNodes.next().toString());
    }

    @Test
    public void testLocateTds() throws Exception {
        Set<DataRegion> records = DataRegionLocator.locate(Fixture.TABLE_WITH_HEAD_BODY_FOOT);
        assertEquals(1, records.size());

        DataRegion record = records.iterator().next();
        assertEquals(3, record.generalizedNodes().size());

        Iterator<Forest> genNodes = record.generalizedNodes().iterator();

        Iterator<Forest> i = genNodes.iterator();
        assertEquals("Name: First", i.next().toString());
        assertEquals("Name: Second", i.next().toString());
        assertEquals("Name: Third", i.next().toString());
    }
}
