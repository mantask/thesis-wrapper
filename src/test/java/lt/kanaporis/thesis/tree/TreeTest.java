package lt.kanaporis.thesis.tree;

import lt.kanaporis.thesis.Fixture;
import org.junit.Ignore;
import org.junit.Test;

import static lt.kanaporis.thesis.tree.Node.*;
import static org.junit.Assert.*;

public class TreeTest {
    
    @Test
    public void testNoChildrenForEmptyNodes() throws Exception {
        assertTrue(new Tree(elem("h1")).children().isEmpty());
        assertTrue(new Tree(text("Hello, World!")).children().isEmpty());
        assertTrue(new Tree(attr("style", "border: 1px solid gray;")).children().isEmpty());
    }

    @Test
    public void testChildrenAreOrdered() throws Exception {
        Tree t = new Tree(elem("body"),
                new Tree(elem("h1")),
                new Tree(elem("h2")),
                new Tree(elem("h3")),
                new Tree(elem("h4")));
        assertEquals("h1", t.child(0).root().label());
        assertEquals("h2", t.child(1).root().label());
        assertEquals("h3", t.child(2).root().label());
        assertEquals("h4", t.child(3).root().label());
    }

    @Test
    public void testChildWithinBounds() throws Exception {
        assertEquals("body", Fixture.origHtml.child(1).root().toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildForEmpty() throws Exception {
        Tree t = new Tree(elem("h1"));
        t.child(0);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("{html}", new Tree(elem("HTML")).toString());
        assertEquals("{style=\"color:red;\"}", new Tree(attr("Style", "color:red;")).toString());
        assertEquals("{html{head}{body{h1{TEXT=\"Hello, World!\"}}{p{TEXT=\"Body text goes\"}{strong{TEXT=\"here\"}}{TEXT=\"!\"}}}}", Fixture.origHtml.toString());
    }

    @Test
    public void testHasChild() throws Exception {
        Node h1 = elem("H1");
        Tree tree = new Tree(elem("body"),
                new Tree(h1,
                    new Tree(text("Header goes here"))),
                new Tree(elem("p"),
                    new Tree(text("Content goes here"))));
        assertTrue(tree.contains(h1));
        assertFalse(tree.contains(elem("H1")));
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals("", new Tree(elem("body")).text());
        assertEquals("", new Tree(attr("style", "color:red;")).text());
        assertEquals("Hello, World!", new Tree(text(" Hello, World! ")).text());
        assertEquals("Hello, World! Body text goes here !", Fixture.origHtml.text());
    }

    @Test(expected = RuntimeException.class)
    public void testTailWhenNoDistinquishedNode() throws Exception {
        Fixture.origHtml.tail(elem("h1"));
    }

    @Test
    public void testTailForRoot() throws Exception {
        assertNull(Fixture.origHtml.tail(Fixture.origHtml.root()));
    }

    @Test
    public void testTailWhenOnRight() throws Exception {
        Node p = Fixture.origHtml.child(1).child(1).root();
        assertEquals("{html{body}}", Fixture.origHtml.tail(p).toString());
    }

    @Test
    public void testTailWhenOnLeft() throws Exception {
        Node h1 = Fixture.origHtml.child(1).child(0).root();
        assertEquals("{html{body{p{TEXT=\"Body text goes\"}{strong{TEXT=\"here\"}}{TEXT=\"!\"}}}}",
                Fixture.origHtml.tail(h1).toString());
    }

    @Test(expected = RuntimeException.class)
    public void testPrefixWhenTreeWithoutDistNode() throws Exception {
        Fixture.origHtml.prefix(elem("h1"));
    }

    @Test
    public void testPrefixWhenInMiddle() throws Exception {
        Node h1 = Fixture.origHtml.child(1).child(0).root();
        assertEquals("{head}", Fixture.origHtml.prefix(h1).toString());
    }

    @Test
    public void testPrefixWhenOnRight() throws Exception {
        Node p = Fixture.origHtml.child(1).child(1).root();
        assertEquals("{head}{h1{TEXT=\"Hello, World!\"}}", Fixture.origHtml.prefix(p).toString());
    }

    @Test
    public void testPrefixWhenOnLeft() throws Exception {
        Node head = Fixture.origHtml.child(0).root();
        assertEquals("", Fixture.origHtml.prefix(head).toString());
    }

    @Test
    public void testSubforest() throws Exception {
        Forest f = Fixture.origHtml.subforest();
        assertEquals(2, f.trees().size());
        assertEquals("head", f.tree(0).root().label());
        assertEquals("body", f.tree(1).root().label());
    }

    @Test
    public void testEqualsTrue() throws Exception {
        assertTrue(TreeUtils.areEqual(
                Fixture.origHtml,
                new Tree(elem("html"),
                        new Tree(elem("head")),
                        new Tree(elem("body"),
                                new Tree(elem("h1"),
                                        new Tree(text("Hello, World!"))),
                                new Tree(elem("p"),
                                        new Tree(text("Body text goes ")),
                                        new Tree(elem("strong"),
                                                new Tree(text("here"))),
                                        new Tree(text("!")))))));
    }

    @Test
    public void testEqualsFalseWithNodeTextMissing() throws Exception {
        assertFalse(TreeUtils.areEqual(
                Fixture.origHtml,
                new Tree(elem("html"),
                        new Tree(elem("head")),
                        new Tree(elem("body"),
                                new Tree(elem("h1")), // text missing
                                new Tree(elem("p"),
                                        new Tree(text("Body text goes ")),
                                        new Tree(elem("strong"),
                                                new Tree(text("here"))),
                                        new Tree(text("!")))))));
    }

    @Test
    public void testEqualsFalseWithTextNodeRenamed() throws Exception {
        assertFalse(TreeUtils.areEqual(
                Fixture.origHtml,
                new Tree(elem("html"),
                    new Tree(elem("head")),
                    new Tree(elem("body"),
                            new Tree(elem("h2"), // h1 → h2
                                    new Tree(text("Hello, World!"))),
                            new Tree(elem("p"),
                                    new Tree(text("Body text goes ")),
                                    new Tree(elem("strong"),
                                            new Tree(text("here"))),
                                    new Tree(text("!")))))));
    }

    @Test
    public void testNodeCount() throws Exception {
        assertEquals(1, new Tree(elem("h1")).nodeCount());
        assertEquals(5, new Tree(elem("body"),
            new Tree(elem("h1")),
            new Tree(elem("h2")),
            new Tree(elem("h3")),
            new Tree(elem("h4"))).nodeCount());
        assertEquals(10, Fixture.origHtml.nodeCount());
    }

    @Test
    public void testSubstantialDifference() throws Exception {
        assertTrue(Fixture.origHtml.substantiallyDifferentFrom(new Tree(elem("h1"))));
        assertTrue(new Tree(elem("h1")).substantiallyDifferentFrom(Fixture.origHtml));
    }

    @Test
    public void testSubtree() throws Exception {
        Tree body = new Tree(elem("body"),
                new Tree(elem("h1")),
                new Tree(elem("h2")),
                new Tree(elem("h3")),
                new Tree(elem("h4")));
        assertEquals("{body{h1}{h2}{h3}{h4}}", body.subtree(0, 4).toString());
        assertEquals("{body}", body.subtree(0, 0).toString());
        assertEquals("{body{h1}}", body.subtree(0, 1).toString());
        assertEquals("{body{h4}}", body.subtree(3, 4).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtreeOutOfRange() throws Exception {
        new Tree(elem("body"),
                new Tree(elem("h1")),
                new Tree(elem("h2")),
                new Tree(elem("h3")),
                new Tree(elem("h4"))).subtree(4, 5);
    }

}
