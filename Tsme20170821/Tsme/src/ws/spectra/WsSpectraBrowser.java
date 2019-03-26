package ws.spectra;

public class WsSpectraBrowser {

	public static final int browser_code_normal = 900;//表示正常传输
	public static final int browser_code_close = 100;//表示关闭
	
	private int browser_code;
	
	private String deviceNum;
	
	private String timestample;
	
	private String business;

	public int getBrowser_code() {
		return browser_code;
	}

	public void setBrowser_code(int browser_code) {
		this.browser_code = browser_code;
	}

	public String getTimestample() {
		return timestample;
	}

	public void setTimestample(String timestample) {
		this.timestample = timestample;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	
}
