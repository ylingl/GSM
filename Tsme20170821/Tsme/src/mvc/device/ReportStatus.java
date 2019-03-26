package mvc.device;

/**
 * 上报状态请求参数
 * @author kitty
 *
 */
public class ReportStatus extends Report {
	
	public static final int report_code_normal = 20;//表示常态,不请求数据
	public static final int report_code_req_monitorParam = 21;//表示设备请求用户设置的频谱监测参数
	public static final int report_code_req_demodParam = 22;//表示设备请求用户设置的解调参数

	private int monitor_code;
	
	private int demod_code;
	
	private String device_type;
	
	private String gpsTime;
	
	private float longitude;
	
	private float latitude;
	
	private float altitude;
	
	private String ipAddress;
	
	private long updateTime;

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
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

	public int getMonitor_code() {
		return monitor_code;
	}

	public void setMonitor_code(int monitor_code) {
		this.monitor_code = monitor_code;
	}

	public int getDemod_code() {
		return demod_code;
	}

	public void setDemod_code(int demod_code) {
		this.demod_code = demod_code;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
}
