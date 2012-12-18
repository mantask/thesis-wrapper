package lt.kanaporis.tortilla.wrapper;

public class WrappingResult {
	private final String value;
	private final Double confidence;
	
	// --- INITIALIZE --------------------------------------
	
	public WrappingResult(String value, double confidence) {
		this.confidence = confidence;
		this.value = value;
	}

	public WrappingResult(String value) {
		this.confidence = null;
		this.value = value;
	}

	// --- PROFILE -----------------------------------------
	
	/**
	 * @return 0..1 value of confidence
	 */
	public Double getConfidence() {
		return confidence;
	}

	public String getValue() {
		return value;
	}
	
	// ------------------------------------------------------
	
	@Override
	public String toString() {
		return value + " : " + confidence;
	}
}
