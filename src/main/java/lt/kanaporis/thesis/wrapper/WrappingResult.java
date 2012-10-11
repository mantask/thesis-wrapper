package lt.kanaporis.thesis.wrapper;

public class WrappingResult {
	private final String value;
	private final double confidence;
	
	// --- INITIALIZE --------------------------------------
	
	public WrappingResult(String value, double confidence) {
		this.confidence = confidence;
		this.value = value;
	}

	// --- PROFILE -----------------------------------------
	
	public double getConfidence() {
		return confidence;
	}

	public String getValue() {
		return value;
	}
}
