package lt.kanaporis.thesis.tree;

import lt.kanaporis.thesis.Fixture;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RtedMapperTest {

    @Test
    public void testMapping() throws Exception {
        assertEquals("{h1}", RtedMapper.map(new Tree(Node.elem("h1"))));
        assertEquals("{style}", RtedMapper.map(new Tree(Node.attr("style", "color: red;"))));
        assertEquals("{TEXT}", RtedMapper.map(new Tree(Node.text("Hello, World!"))));
        assertEquals("{html{head}{body{h1{TEXT}}{p{TEXT}{strong{TEXT}}{TEXT}}}}", RtedMapper.map(Fixture.origHtml));
    }
}
