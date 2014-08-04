package lt.kanaporis.thesis.tree.rted;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import util.LblTree;

public class RtedMapper {

	/**
	 * @param doc
	 *            XML document
	 * @return Tree representation for RTED input, e.g. {html{head}{body{h1}{p}}}
	 */
	public static String map(Document doc) {
		return walk(doc.body());
	}

	/**
	 * Map current DOM node and recursively walk through children
	 */
	private static String walk(final Element element) {
		StringBuilder sb = new StringBuilder();
		sb.append(LblTree.OPEN_BRACKET);
		sb.append(element.tagName());
		for (Element child : element.children()) {
			sb.append(walk(child));
		}
		sb.append(LblTree.CLOSE_BRACKET);
		return sb.toString();
	}

}
