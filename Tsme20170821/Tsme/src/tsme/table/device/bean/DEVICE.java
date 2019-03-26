package tsme.table.device.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

/**
 * @date 20150922
 * @author lmq
 * 设备信息表
 */
public class DEVICE extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4101232592497863939L;

	/**
	 * VARCHAR2(50 BYTE) 主键
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	/**
	 * VARCHAR2(50 BYTE)
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String device_num;
	
	/**
	 * VARCHAR2(50 BYTE)
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String device_type;
	
	private float LNG;
	
	private float LAT;
	
	private int azimuth;
	
	private double distance;
	
	private String ip;
	
	/**
	 * 非空
	 */
	@NotEmpty
	private boolean active;
	
	private long create_time;
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getDevice_num() {
		return device_num;
	}

	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public float getLNG() {
		return LNG;
	}

	public void setLNG(float LNG) {
		this.LNG = LNG;
	}

	public float getLAT() {
		return LAT;
	}

	public void setLAT(float LAT) {
		this.LAT = LAT;
	}

	public int getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(int azimuth) {
		this.azimuth = azimuth;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
