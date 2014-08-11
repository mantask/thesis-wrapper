package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.HtmlParser;
import lt.kanaporis.thesis.tree.Node;

/**
 * Created by mantas on 8/1/14.
 */
public abstract class BaseWrapper implements Wrapper {

    protected final Forest oldTree;
    protected final Node distinguishedNode;

    private BaseWrapper(String oldHtml, String distinguishedNodeCssSelector) {
        // TODO
    }

    public static BaseWrapper instance(String oldHtml, String distinguishedNodeCssSelector) {
        return new BaseWrapper(oldHtml, distinguishedNodeCssSelector);
    }

    @Override
    public WrapperResult wrap(String newHtml) {
        return wrap(new HtmlParser().parse(newHtml));
    }

    protected abstract WrapperResult wrap(Forest newTree);

}
