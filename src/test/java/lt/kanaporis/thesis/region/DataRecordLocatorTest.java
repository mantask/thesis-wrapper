package lt.kanaporis.thesis.region;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Tree;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class DataRecordLocatorTest {

    @Test
    public void testLocateBasicRegions() throws Exception {
        Set<DataRecord> records = DataRecordLocator.locate(Fixture.tableHtml.child(0));
        assertEquals(1, records.size());

        DataRecord record = records.iterator().next();
        assertEquals(6, record.generalizedNodes().size());

        int i = 1;
        for (Forest generalizedNode : record.generalizedNodes()) {
            assertEquals("{tr{td{TEXT=\"" + i + "\"}}}", generalizedNode.toString());
            i++;
        }
    }

    @Test
    public void testLocateNonExistentRegions() throws Exception {
        Set<DataRecord> records = DataRecordLocator.locate(Fixture.origHtml.child(1));
        assertEquals(0, records.size());
    }

    @Test
    public void testLocateMoreComplexTable() throws Exception {
        Set<DataRecord> records = DataRecordLocator.locate(Fixture.movieHtml);
        assertEquals(1, records.size());

        DataRecord record = records.iterator().next();
        assertEquals(3, record.generalizedNodes().size());

        Iterator<Forest> genNodes = record.generalizedNodes().iterator();

        assertEquals("{tr{td{strong{TEXT=\"1. Guardians of the Galaxy (2014)\"}}{br}{TEXT=\"Weekend: $16.3M\"}}}", genNodes.next().toString());
        assertEquals("{tr{td{strong{TEXT=\"2. Teenage Mutant Ninja Turtles (2014)\"}}{br}{TEXT=\"Weekend: $11.8M\"}}}", genNodes.next().toString());
        assertEquals("{tr{td{strong{TEXT=\"3. If I Stay (2014)\"}}{br}{TEXT=\"Weekend: $9.3M\"}}}", genNodes.next().toString());
    }
}
