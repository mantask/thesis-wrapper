package lt.kanaporis.thesis.region;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Forest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DataRegionTest {

    @Test
    public void testIsCovered() throws Exception {
        DataRegion parentRecord = new DataRegion();
        for (int i = 1; i <= 3; i++) {
            parentRecord.add(new Forest(Fixture.MOVIE_HTML.child(i))); // tr td[1-3]
        }

        DataRegion childRecord = new DataRegion();
        childRecord.add(Fixture.MOVIE_HTML.child(1).subforest()); // tr td[1] *

        assertTrue(childRecord.isCoveredBy(parentRecord));
    }
}
