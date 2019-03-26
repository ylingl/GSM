package mvc.spectra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpectraData {
	
	private Double LNG;
	
	private Double LAT;
	
	//frequencyBand-频谱队列
	private Map<String, ArrayList<Double>> spDataMap = new HashMap<String, ArrayList<Double>>();
	
	//frequencyBand-预警数据队列
	private Map<String, ArrayList<WarningData>> warnDataMap = new HashMap<String, ArrayList<WarningData>>();

	public Map<String, ArrayList<Double>> getSpDataMap() {
		return spDataMap;
	}

	public void setSpDataMap(Map<String, ArrayList<Double>> spDataMap) {
		this.spDataMap = spDataMap;
	}

	public Map<String, ArrayList<WarningData>> getWarnDataMap() {
		return warnDataMap;
	}

	public void setWarnDataMap(Map<String, ArrayList<WarningData>> warnDataMap) {
		this.warnDataMap = warnDataMap;
	}

	public Double getLNG() {
		return LNG;
	}

	public void setLNG(Double lNG) {
		LNG = lNG;
	}

	public Double getLAT() {
		return LAT;
	}

	public void setLAT(Double lAT) {
		LAT = lAT;
	}
	
}
