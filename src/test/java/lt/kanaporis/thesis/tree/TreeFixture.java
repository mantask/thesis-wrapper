package lt.kanaporis.thesis.tree;

import static lt.kanaporis.thesis.tree.Node.elem;
import static lt.kanaporis.thesis.tree.Node.text;

public class TreeFixture {

    public static Tree html = new Tree(elem("html"),
            new Tree(elem("head")),
            new Tree(elem("body"),
                new Tree(elem("h1"),
                    new Tree(text("Hello, World!"))),
                new Tree(elem("p"),
                    new Tree(text("Body text goes ")),
                    new Tree(elem("strong"),
                        new Tree(text("here"))),
                    new Tree(text("!")))));

}
