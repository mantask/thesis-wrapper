package lt.kanaporis.thesis.wrapper.naive;

class XPathStep {
	private final String value;
	private final boolean optimized;
	
	// --- INITIALIZE --------------------------------------

	public XPathStep(String value, boolean optimized) {
		this.value = value;
		this.optimized = optimized;
	}
	
	// --- PROFILE -----------------------------------------
	
	public String getValue() {
		return value;
	}
	
	public boolean isOptimized() {
		return optimized;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
