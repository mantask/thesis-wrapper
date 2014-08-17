package lt.kanaporis.thesis.tree;

import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testValue() throws Exception {
        assertEquals("Hello, World!", Node.text("Hello, World!").value());
        assertEquals("color: red;", Node.attr("style", "color: red;").value());
        assertNull(Node.elem("h1").value());
    }

    @Test
    public void testType() throws Exception {
        assertEquals(Node.NodeType.ELEMENT, Node.elem("h1").type());
        assertEquals(Node.NodeType.ATTRIBUTE, Node.attr("style", "color: red;").type());
        assertEquals(Node.NodeType.TEXT, Node.text("Hello, World!").type());
    }

    @Test
    public void testLabel() throws Exception {
        assertEquals("h1", Node.elem("H1").label());
        assertEquals("style", Node.attr("Style", "color: red;").label());
        assertEquals("TEXT", Node.text("Hello, World!").label());
    }

    @Test
    public void testText() throws Exception {
        assertEquals("", Node.elem("H1").text());
        assertEquals("", Node.attr("Style", "color: red;").text());
        assertEquals("Hello, World!", Node.text("Hello, World!").text());
    }
}
