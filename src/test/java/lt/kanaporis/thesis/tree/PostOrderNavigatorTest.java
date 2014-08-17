package lt.kanaporis.thesis.tree;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PostOrderNavigatorTest {

    @Test
    public void testSortForNonEmpty() throws Exception {
        List<Tree> nodes = PostOrderNavigator.sort(TreeFixture.html);
        assertEquals(10, nodes.size());
        assertEquals("head", nodes.get(0).root().label());
        assertEquals("TEXT", nodes.get(1).root().label());
        assertEquals("h1", nodes.get(2).root().label());
        assertEquals("TEXT", nodes.get(3).root().label());
        assertEquals("TEXT", nodes.get(4).root().label());
        assertEquals("strong", nodes.get(5).root().label());
        assertEquals("TEXT", nodes.get(6).root().label());
        assertEquals("p", nodes.get(7).root().label());
        assertEquals("body", nodes.get(8).root().label());
        assertEquals("html", nodes.get(9).root().label());
    }
}