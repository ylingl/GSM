package tsme.table.originalAlarm.bean;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class ORIGINALALARM extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8232607217421731241L;
	
	private String id;
	
	private String originalData_id;
	
	private String file_path;

	private boolean active;
	
	private long create_time;
	
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getOriginalData_id() {
		return originalData_id;
	}

	public void setOriginalData_id(String originalData_id) {
		this.originalData_id = originalData_id;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
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
}
