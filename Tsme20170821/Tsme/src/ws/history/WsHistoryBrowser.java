package ws.history;

public class WsHistoryBrowser {
	
	public static final int browser_code_normal = 900;//表示正常传输
	public static final int browser_code_close = 100;//表示关闭
	
	private int browser_code;
	
	private String timestample;
	
	private boolean serial;
	
	private String direction;
	
	private int index;

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

	public boolean isSerial() {
		return serial;
	}

	public void setSerial(boolean serial) {
		this.serial = serial;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
