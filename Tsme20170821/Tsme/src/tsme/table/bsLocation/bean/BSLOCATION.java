package tsme.table.bsLocation.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

/**
 * @date 20150922
 * @author lmq
 * 基站地理信息表
 */
public class BSLOCATION extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5500786720171512123L;
	
	/**
	 * VARCHAR2(50 BYTE) 主键
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	/**
	 * VARCHAR2(50 BYTE) 外键，基站表主键
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String baseStation_id;
	
	/**
	 * 经度
	 */
	private float LNG;
	
	/**
	 * 纬度
	 */
	private float LAT;
	
	/**
	 * 方向角
	 */
	private int azimuth;
	
	/**
	 * 到达小区中心距离
	 */
	private float distance;
	
	/**
	 * 公里标
	 */
	private String km_stone;
	
	
	private boolean active;
	
	
	private long create_time;

	
	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	
	public float getLNG() {
		return LNG;
	}

	public void setLNG(float lNG) {
		LNG = lNG;
	}

	public float getLAT() {
		return LAT;
	}

	public void setLAT(float lAT) {
		LAT = lAT;
	}

	public int getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(int azimuth) {
		this.azimuth = azimuth;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getKm_stone() {
		return km_stone;
	}

	public void setKm_stone(String km_stone) {
		this.km_stone = km_stone;
	}

	public String getBaseStation_id() {
		return baseStation_id;
	}

	public void setBaseStation_id(String baseStation_id) {
		this.baseStation_id = baseStation_id;
	}

}
