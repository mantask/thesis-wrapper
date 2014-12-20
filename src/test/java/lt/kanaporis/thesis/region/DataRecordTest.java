package lt.kanaporis.thesis.region;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Forest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DataRecordTest {

    @Test
    public void testIsCovered() throws Exception {
        DataRecord parentRecord = new DataRecord();
        for (int i = 1; i <= 3; i++) {
            parentRecord.add(new Forest(Fixture.movieHtml.child(i))); // tr td[1-3]
        }

        DataRecord childRecord = new DataRecord();
        childRecord.add(Fixture.movieHtml.child(1).subforest()); // tr td[1] *

        assertTrue(childRecord.isCoveredBy(parentRecord));
    }
}
