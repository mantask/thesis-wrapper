package lt.kanaporis.thesis.tree;

import org.junit.Test;

import static lt.kanaporis.thesis.tree.Node.elem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ForestTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutability() throws Exception {
        new Forest(TreeFixture.html).trees().add(TreeFixture.html);
    }

    @Test
    public void testLastTree() throws Exception {
        Forest f = new Forest(new Tree(elem("h1")),
                new Tree(elem("body")),
                new Tree(elem("strong")));
        assertEquals("{strong}", f.lastTree().toString());
    }

    @Test
    public void testRemoveLastTree() throws Exception {
        Forest f = new Forest(new Tree(elem("h1")),
                new Tree(elem("body")),
                new Tree(elem("strong")));
        assertEquals("{h1}{body}", f.removeLastTree().toString());
    }

    @Test
    public void testRemoveLastTreeBecomesEmpty() throws Exception {
        Forest f = new Forest(new Tree(elem("h1")));
        assertEquals("", f.removeLastTree().toString());
    }

    @Test
    public void testRemoveLastTreeForEmptyTree() throws Exception {
        Forest f = new Forest();
        assertEquals("", f.removeLastTree().toString());
    }

    @Test
    public void testRemoveLastTreeNode() throws Exception {
        assertEquals("{head}{body{h1{TEXT=\"Hello, World!\"}}{p{TEXT=\"Body text goes\"}{strong{TEXT=\"here\"}}{TEXT=\"!\"}}}",
                new Forest(TreeFixture.html).removeLastTreeNode().toString());
    }

    @Test
    public void testRemoveLastTreeNodeForEmptyForest() throws Exception {
        assertTrue(new Forest().removeLastTreeNode().empty());
    }

    @Test
    public void testRemoveLastTreeNodeForSmallTree() throws Exception {
        Forest f = new Forest(new Tree(elem("h1")));
        assertTrue(f.removeLastTreeNode().empty());
    }
}