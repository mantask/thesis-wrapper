package lt.kanaporis.tortilla.dom;

import java.util.List;

public class TreeEditScript {

	private final String originalTree;
	private final List<int[]> mapping;
	
	public TreeEditScript(String originalTree, List<int[]> mapping) {
		this.originalTree = originalTree;
		this.mapping = mapping;
	}

	public int apply(int originalPostOrderIndex) {
		throw new RuntimeException("Not yet implemented");
	}

}
