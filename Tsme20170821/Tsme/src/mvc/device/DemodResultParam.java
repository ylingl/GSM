package mvc.device;

public class DemodResultParam extends DemodPoint {
	
	private String frequencyBand;
	
	private String demodulationPointId;

	public String getDemodulationPointId() {
		return demodulationPointId;
	}

	public void setDemodulationPointId(String demodulationPointId) {
		this.demodulationPointId = demodulationPointId;
	}

	public String getFrequencyBand() {
		return frequencyBand;
	}

	public void setFrequencyBand(String frequencyBand) {
		this.frequencyBand = frequencyBand;
	}

}
