package lt.kanaporis.thesis.html;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;
import lt.kanaporis.thesis.wrapper.ProbabilisticRecordLevelWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

// TODO work work work!
public class HtmlRecordWrapper {

    private final ProbabilisticRecordLevelWrapper wrapper;
    private final ProbabilisticChangeModel probModel = new ProbabilisticChangeModel(); // FIXME init

    public HtmlRecordWrapper(String html, String selector) {
        Document dom = Jsoup.parse(html);
        Tree tree = HtmlTreeFactory.buildFromDom(dom);
        Elements distinquishedElems = dom.select(selector);
        if (distinquishedElems.size() != 1) {
            throw new IllegalStateException("Selector cannot locate a unique distinquished node.");
        }
        Tree distinquishedNode = locate(dom, tree, distinquishedElems);
        wrapper = new ProbabilisticRecordLevelWrapper(tree, distinquishedNode, probModel);
    }

    public List<String> wrap(String html) {
        Document dom = Jsoup.parse(html);
        Tree tree = HtmlTreeFactory.buildFromDom(dom);
        List<Node> nodes = wrapper.wrap(tree);
        return toString(nodes);
    }

    public List<String> toString(List<Node> nodes) {
        List<String> nodesStr = new ArrayList<>();
        for (Node node : nodes) {
            nodesStr.add(node.text());
        }
        return nodesStr;
    }

    private Tree locate(Document dom, Tree origTree, Elements distinquishedElems) {
        // TODO
        return null;
    }

}
