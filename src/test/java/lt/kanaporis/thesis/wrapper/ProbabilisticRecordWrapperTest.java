package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Tree;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ProbabilisticRecordWrapperTest {

    @Test
    public void testMajorTableTransformation() throws Exception {
        ProbabilisticRecordLevelWrapper wrapper = new ProbabilisticRecordLevelWrapper(
                Fixture.TABLE_WITH_HEAD_BODY_FOOT,
                Fixture.TABLE_WITH_HEAD_BODY_FOOT.child(1).child(0), // table → tbody → td
                Fixture.htmlChangeModel);
        Set<Tree> trees = wrapper.wrap(Fixture.TABLE_WITH_JUST_BODY);

        assertEquals(3, trees.size());

        Iterator<Tree> i = trees.iterator();
        assertEquals("Name: First", i.next().text());
        assertEquals("Name: Second", i.next().text());
        assertEquals("Name: Third", i.next().text());
    }
}
