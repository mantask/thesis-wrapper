package lt.kanaporis.thesis.wrapper.chrome_dev_tools;

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
