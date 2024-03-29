package lt.kanaporis.thesis;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;
import org.apache.commons.lang3.Validate;

import static lt.kanaporis.thesis.tree.Node.elem;
import static lt.kanaporis.thesis.tree.Node.text;

public class Fixture {

    public static final Tree ORIG_HTML =
            new Tree(elem("html"),
                new Tree(elem("head")),
                new Tree(elem("body"),
                    new Tree(elem("h1"),
                        new Tree(text("Hello, World!"))),
                    new Tree(elem("p"),
                        new Tree(text("Body text goes ")),
                        new Tree(elem("strong"),
                            new Tree(text("here"))),
                        new Tree(text("!")))));

    public static final Tree TABLE_HTML =
            new Tree(elem("table"),
                new Tree(elem("tr"),
                    new Tree(elem("td"), new Tree(text("1"))),
                    new Tree(elem("td"), new Tree(text("2"))),
                    new Tree(elem("td"), new Tree(text("3"))),
                    new Tree(elem("td"), new Tree(text("4"))),
                    new Tree(elem("td"), new Tree(text("5"))),
                    new Tree(elem("td"), new Tree(text("6")))));

    public static final Tree TABLE_WITH_HEAD_BODY_FOOT =
            new Tree(elem("table"),
                    new Tree(elem("thead"),
                            new Tree(text("Header"))),
                    new Tree(elem("tbody"),
                            new Tree(elem("td"),
                                    new Tree(elem("strong"),
                                            new Tree(text("Name:"))),
                                    new Tree(text("First"))),
                            new Tree(elem("td"),
                                    new Tree(elem("strong"),
                                            new Tree(text("Name:"))),
                                    new Tree(text("Second"))),
                            new Tree(elem("td"),
                                    new Tree(elem("strong"),
                                            new Tree(text("Name:"))),
                                    new Tree(text("Third")))),
                    new Tree(elem("tfoot"),
                            new Tree(text("Footer"))));

    public static final Tree TABLE_WITH_JUST_BODY =
            new Tree(elem("table"),
                    new Tree(elem("td"),
                            new Tree(text("Name: First"))),
                    new Tree(elem("td"),
                            new Tree(text("Name: Second"))),
                    new Tree(elem("td"),
                            new Tree(elem("strong"),
                                    new Tree(text("Name:"))),
                            new Tree(text("Third"))));

    public static final Tree MOVIE_HTML =
            new Tree(Node.elem("tr"),
                new Tree(Node.elem("th"),
                    new Tree(Node.text("Title of the movie"))),
                new Tree(Node.elem("td"),
                    new Tree(Node.elem("strong"),
                            new Tree(Node.text("1. Guardians of the Galaxy (2014)"))),
                    new Tree(Node.elem("br")),
                    new Tree(Node.text("Weekend: $16.3M"))),
                new Tree(Node.elem("td"),
                    new Tree(Node.elem("strong"),
                            new Tree(Node.text("2. Teenage Mutant Ninja Turtles (2014)"))),
                    new Tree(Node.elem("br")),
                    new Tree(Node.text("Weekend: $11.8M"))),
                new Tree(Node.elem("td"),
                    new Tree(Node.elem("strong"),
                            new Tree(Node.text("3. If I Stay (2014)"))),
                    new Tree(Node.elem("br")),
                    new Tree(Node.text("Weekend: $9.3M"))),
                new Tree(Node.elem("td"),
                    new Tree(Node.elem("a"),
                            new Tree(Node.text("See more box office results at BoxOfficeMojo.com")))));

    public static final ProbabilisticChangeModel abcChangeModel = new ProbabilisticChangeModel();
    static {
        abcChangeModel.stopProb(0.15);

        abcChangeModel.insProb("a", 0.2);
        abcChangeModel.insProb("b", 0.3);
        abcChangeModel.insProb("c", 0.5);

        abcChangeModel.delProb("a", 0.12);
        abcChangeModel.delProb("b", 0.34);
        abcChangeModel.delProb("c", 0.45);

        abcChangeModel.subProb("a", "b", 0.17);
        abcChangeModel.subProb("a", "c", 0.03);

        abcChangeModel.subProb("b", "a", 0.15);
        abcChangeModel.subProb("b", "c", 0.05);

        abcChangeModel.subProb("c", "a", 0.05);
        abcChangeModel.subProb("c", "b", 0.1);

        Validate.isTrue(abcChangeModel.valid());
    }

    public static final ProbabilisticChangeModel htmlChangeModel = new ProbabilisticChangeModel();
    static {
        htmlChangeModel.stopProb(0.05);

        htmlChangeModel.insProb("html", 0.01);
        htmlChangeModel.insProb("body", 0.02);
        htmlChangeModel.insProb("div", 0.2);
        htmlChangeModel.insProb("h1", 0.07);
        htmlChangeModel.insProb("p", 0.1);
        htmlChangeModel.insProb("strong", 0.2);
        htmlChangeModel.insProb("text", 0.4);

        htmlChangeModel.delProb("html", 0.005);
        htmlChangeModel.delProb("body", 0.01);
        htmlChangeModel.delProb("div", 0.1);
        htmlChangeModel.delProb("h1", 0.035);
        htmlChangeModel.delProb("p", 0.05);
        htmlChangeModel.delProb("strong", 0.1);
        htmlChangeModel.delProb("text", 0.2);

        // 0.078
        htmlChangeModel.subProb("html", "html", 0.05);
        htmlChangeModel.subProb("html", "body", 0.013);
        htmlChangeModel.subProb("html", "div", 0.008);
        htmlChangeModel.subProb("html", "h1", 0.003);
        htmlChangeModel.subProb("html", "p", 0.002);
        htmlChangeModel.subProb("html", "strong", 0.001);
        htmlChangeModel.subProb("html", "text", 0.001);

        // 0.067
        htmlChangeModel.subProb("body", "html", 0.001);
        htmlChangeModel.subProb("body", "body", 0.05);
        htmlChangeModel.subProb("body", "div", 0.005);
        htmlChangeModel.subProb("body", "h1", 0.003);
        htmlChangeModel.subProb("body", "p", 0.005);
        htmlChangeModel.subProb("body", "strong", 0.002);
        htmlChangeModel.subProb("body", "text", 0.001);

        // 0.108
        htmlChangeModel.subProb("div", "html", 0.005);
        htmlChangeModel.subProb("div", "body", 0.005);
        htmlChangeModel.subProb("div", "div", 0.05);
        htmlChangeModel.subProb("div", "h1", 0.007);
        htmlChangeModel.subProb("div", "p", 0.019);
        htmlChangeModel.subProb("div", "strong", 0.005);
        htmlChangeModel.subProb("div", "text", 0.017);

        // 0.110
        htmlChangeModel.subProb("h1", "html", 0.001);
        htmlChangeModel.subProb("h1", "body", 0.002);
        htmlChangeModel.subProb("h1", "div", 0.015);
        htmlChangeModel.subProb("h1", "h1", 0.05);
        htmlChangeModel.subProb("h1", "p", 0.015);
        htmlChangeModel.subProb("h1", "strong", 0.017);
        htmlChangeModel.subProb("h1", "text", 0.01);

        // 0.127
        htmlChangeModel.subProb("p", "html", 0.001);
        htmlChangeModel.subProb("p", "body", 0.001);
        htmlChangeModel.subProb("p", "div", 0.030);
        htmlChangeModel.subProb("p", "h1", 0.015);
        htmlChangeModel.subProb("p", "p", 0.050);
        htmlChangeModel.subProb("p", "strong", 0.015);
        htmlChangeModel.subProb("p", "text", 0.015);

        // 0.228
        htmlChangeModel.subProb("strong", "html", 0.001);
        htmlChangeModel.subProb("strong", "body", 0.001);
        htmlChangeModel.subProb("strong", "div", 0.035);
        htmlChangeModel.subProb("strong", "h1", 0.047);
        htmlChangeModel.subProb("strong", "p", 0.055);
        htmlChangeModel.subProb("strong", "strong", 0.050);
        htmlChangeModel.subProb("strong", "text", 0.039);

        // 0.282
        htmlChangeModel.subProb("text", "html", 0.001);
        htmlChangeModel.subProb("text", "body", 0.001);
        htmlChangeModel.subProb("text", "div", 0.035);
        htmlChangeModel.subProb("text", "h1", 0.055);
        htmlChangeModel.subProb("text", "p", 0.055);
        htmlChangeModel.subProb("text", "strong", 0.085);
        htmlChangeModel.subProb("text", "text", 0.050);

        Validate.isTrue(htmlChangeModel.valid());
    }
}
