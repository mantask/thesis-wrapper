package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;
import lt.kanaporis.thesis.tree.TreeUtils;
import org.junit.Test;

import static lt.kanaporis.thesis.tree.Node.elem;
import static lt.kanaporis.thesis.tree.Node.text;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProbabilisticPageWrapperTest {

    @Test
    public void testNoChangeProb() throws Exception {
        Tree a = new Tree(Node.elem("a"));
        ProbabilisticPageLevelWrapper wrapper = new ProbabilisticPageLevelWrapper(a, a, Fixture.abcChangeModel);
        assertTrue(TreeUtils.areEqual(a, wrapper.wrap(new Tree(Node.elem("a")))));
    }

    @Test
    public void testInsertChild() throws Exception {
        Tree a = new Tree(Node.elem("a"));
        Tree ba = new Tree(Node.elem("b"), a);
        ProbabilisticPageLevelWrapper wrapper = new ProbabilisticPageLevelWrapper(a, a, Fixture.abcChangeModel);
        assertTrue(TreeUtils.areEqual(a, wrapper.wrap(ba)));
    }

    @Test
    public void testDeleteChild() throws Exception {
        Tree a = new Tree(Node.elem("a"));
        Tree ba = new Tree(Node.elem("b"), a);
        ProbabilisticPageLevelWrapper wrapper = new ProbabilisticPageLevelWrapper(ba, a, Fixture.abcChangeModel);
        assertTrue(TreeUtils.areEqual(a, wrapper.wrap(ba)));
    }

    @Test
    public void testReplacedParent() throws Exception {
        Tree transHtml = new Tree(elem("html"),
                new Tree(elem("head")),
                new Tree(elem("body"),
                        new Tree(elem("h1"),
                                new Tree(text("Hello, World!"))),
                        new Tree(elem("div"), // p→div
                                new Tree(text("Body text goes ")),
                                new Tree(elem("strong"),
                                        new Tree(text("here"))),
                                new Tree(text("!")))));

        ProbabilisticPageLevelWrapper wrapper = new ProbabilisticPageLevelWrapper(
                Fixture.ORIG_HTML,
                Fixture.ORIG_HTML.child(1).child(1).child(1), // html→body→p→strong
                Fixture.htmlChangeModel);

        assertEquals("here", wrapper.wrap(transHtml).text());
    }

}
