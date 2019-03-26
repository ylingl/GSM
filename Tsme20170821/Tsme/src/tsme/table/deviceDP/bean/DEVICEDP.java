package tsme.table.deviceDP.bean;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class DEVICEDP extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7954173308994739373L;

	private String id;
	
	private String deviceFB_id;
	
	private float x;
	
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public String getDeviceFB_id() {
		return deviceFB_id;
	}

	public void setDeviceFB_id(String deviceFB_id) {
		this.deviceFB_id = deviceFB_id;
	}
	
}
