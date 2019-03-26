package tsme.table.originalDemod.bean;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.deviceDR.bean.DEVICEDR;

/**
 * @date 20150922
 * @author lmq
 * 数据分析原始数据表
 */
public class ORIGINALDEMOD extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5339809176897630965L;

	/**
	 * VARCHAR2(50 BYTE) 主键
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	private String originalData_id;
	
	private float frequencyPoint;
	
	private String file_path;

	private boolean active;
	
	private long create_time;
	
	private List<DEVICEDR> deviceDRList = new ArrayList<DEVICEDR>();

	public String getFile_path() {
		return file_path;
	}

	public String getId() {
		return id;
	}
	
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	@Override
	public void setId(String id) {
		this.id = id;
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

	public String getOriginalData_id() {
		return originalData_id;
	}

	public void setOriginalData_id(String originalData_id) {
		this.originalData_id = originalData_id;
	}

	public float getFrequencyPoint() {
		return frequencyPoint;
	}

	public void setFrequencyPoint(float frequencyPoint) {
		this.frequencyPoint = frequencyPoint;
	}

	public List<DEVICEDR> getDeviceDRList() {
		return deviceDRList;
	}

	public void setDeviceDRList(List<DEVICEDR> deviceDRList) {
		this.deviceDRList = deviceDRList;
	}
}
