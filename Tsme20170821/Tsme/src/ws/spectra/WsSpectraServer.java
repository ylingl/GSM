package ws.spectra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mvc.spectra.SpectraData;

public class WsSpectraServer {

	public static final int server_code_initial = 800;//初始链接
	public static final int server_code_normal = 600;//正常
	
	private int server_code;
	
	private String timestample;
	
	private SpectraData spectraData;
	
	private String business;
	
	private Map<String, List<List<Float>>> demodulationPointMapForChart = new HashMap<String, List<List<Float>>>();

	private Map<String, ArrayList<Double>> spectrumMap = new HashMap<String, ArrayList<Double>>();
	
	public int getServer_code() {
		return server_code;
	}

	public void setServer_code(int server_code) {
		this.server_code = server_code;
	}

	public String getTimestample() {
		return timestample;
	}

	public void setTimestample(String timestample) {
		this.timestample = timestample;
	}

	public SpectraData getSpectraData() {
		return spectraData;
	}

	public void setSpectraData(SpectraData spectraData) {
		this.spectraData = spectraData;
	}

	public Map<String, List<List<Float>>> getDemodulationPointMapForChart() {
		return demodulationPointMapForChart;
	}

	public void setDemodulationPointMapForChart(Map<String, List<List<Float>>> demodulationPointMapForChart) {
		this.demodulationPointMapForChart = demodulationPointMapForChart;
	}

	public Map<String, ArrayList<Double>> getSpectrumMap() {
		return spectrumMap;
	}

	public void setSpectrumMap(Map<String, ArrayList<Double>> spectrumMap) {
		this.spectrumMap = spectrumMap;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}
	
}
