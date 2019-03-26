package ws.history;

import java.util.ArrayList;
import java.util.List;

import mvc.spectra.WarningData;

public class WsHistoryServer {
	
	public static final int server_code_initial = 800;//初始链接
	public static final int server_code_normal = 600;//正常
	
	private String timestample;
	
	private boolean serial;
	
	private int index;
	
	private int server_code;
	
	private String LNG;
	
	private String LAT;
	
	private String create_time;
	
	private List<Double> spDataList = new ArrayList<Double>();
	
	private List<WarningData> warnDataList = new ArrayList<WarningData>();

	public String getTimestample() {
		return timestample;
	}

	public void setTimestample(String timestample) {
		this.timestample = timestample;
	}

	public int getServer_code() {
		return server_code;
	}

	public void setServer_code(int server_code) {
		this.server_code = server_code;
	}

	public List<WarningData> getWarnDataList() {
		return warnDataList;
	}

	public void setWarnDataList(List<WarningData> warnDataList) {
		this.warnDataList = warnDataList;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getLNG() {
		return LNG;
	}

	public void setLNG(String lNG) {
		LNG = lNG;
	}

	public String getLAT() {
		return LAT;
	}

	public void setLAT(String lAT) {
		LAT = lAT;
	}

	public List<Double> getSpDataList() {
		return spDataList;
	}

	public void setSpDataList(List<Double> spDataList) {
		this.spDataList = spDataList;
	}

	public boolean isSerial() {
		return serial;
	}

	public void setSerial(boolean serial) {
		this.serial = serial;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
