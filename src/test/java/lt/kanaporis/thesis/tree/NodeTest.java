package lt.kanaporis.thesis.tree;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testTextNodeCreation() throws Exception {
        Node h1 = Node.elem("h1");
        assertEquals("h1", h1.getLabel());
        assertNull(h1.getParent());
        assertTrue(h1.getChildren().isEmpty());
        assertEquals("", h1.getText());
    }
}
