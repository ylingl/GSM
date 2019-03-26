package mvc.spectra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpectraPrePara {
	
	private String templateName;
	
	private boolean myself = false;
	
	private int maxMeans;// �����Ƶ��

	private int bandWidth;// ����

	private int fftSize;// ftt ����
	
	private float longitude;
	
	private float latitude;
	
	private float altitude;
	
	private List<Float> frequencyList = new ArrayList<Float>();
	
	private Map<String, List<List<Float>>> warningLineMap = new HashMap<String, List<List<Float>>>();

	private Map<String, List<List<Float>>> demodulationPointMap = new HashMap<String, List<List<Float>>>();
	
	public int getMaxMeans() {
		return maxMeans;
	}

	public void setMaxMeans(int maxMeans) {
		this.maxMeans = maxMeans;
	}

	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		this.bandWidth = bandWidth;
	}

	public int getFftSize() {
		return fftSize;
	}

	public void setFftSize(int fftSize) {
		this.fftSize = fftSize;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<Float> getFrequencyList() {
		return frequencyList;
	}

	public void setFrequencyList(List<Float> frequencyList) {
		this.frequencyList = frequencyList;
	}

	public Map<String, List<List<Float>>> getWarningLineMap() {
		return warningLineMap;
	}

	public void setWarningLineMap(Map<String, List<List<Float>>> warningLineMap) {
		this.warningLineMap = warningLineMap;
	}

	public boolean isMyself() {
		return myself;
	}

	public void setMyself(boolean myself) {
		this.myself = myself;
	}

	public Map<String, List<List<Float>>> getDemodulationPointMap() {
		return demodulationPointMap;
	}

	public void setDemodulationPointMap(Map<String, List<List<Float>>> demodulationPointMap) {
		this.demodulationPointMap = demodulationPointMap;
	}

}