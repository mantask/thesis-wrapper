package lt.kanaporis.thesis.tree;

import static lt.kanaporis.thesis.tree.Node.elem;
import static lt.kanaporis.thesis.tree.Node.text;

public class TreeFixture {

    public Node htmlTree = elem("html")
            .addChild(elem("head"))
            .addChild(elem("body")
                .addChild(elem("h1")
                    .addChild(text("Hello, world!")))
                .addChild(elem("p")
                    .addChild(text("Body text goes "))
                    .addChild(elem("strong")
                        .addChild(text("here")))
                    .addChild(text("!"))));

}
