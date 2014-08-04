package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.tree.Node;

public class WrapperResult {
	private final Node node;
	private final Double confidence;
	
	// --- INITIALIZE --------------------------------------
	
	public WrapperResult(Node node, double confidence) {
		this.confidence = confidence;
		this.node = node;
	}

	public WrapperResult(Node node) {
        this(node, 1.0);
	}

	// --- PROFILE -----------------------------------------
	
	/**
	 * @return 0..1 value of confidence
	 */
	public Double getConfidence() {
		return confidence;
	}

    public Node getNode() {
        return node;
    }

    // ------------------------------------------------------
	
	@Override
	public String toString() {
		return node.toString() + " : " + confidence;
	}
}
