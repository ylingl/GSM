package tsme.table.authorizedDevice.bean;

import java.util.Date;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class AUTHORIZEDDEVICE  extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4899399076830536665L;
	
	private String id;
	
	private String device_num;
	
	private String creator_id;
	
	private Date valid_date;
	
	private Date expiry_date;
	
	private boolean active;
	
	private long create_time;
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getDevice_num() {
		return device_num;
	}

	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public Date getValid_date() {
		return valid_date;
	}

	public void setValid_date(Date valid_date) {
		this.valid_date = valid_date;
	}

	public Date getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	
}
