package tsme.table.originalData.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.originalAlarm.bean.ORIGINALALARM;
import tsme.table.originalDemod.bean.ORIGINALDEMOD;

/**
 * @date 20150922
 * @author lmq
 * 数据分析原始数据表
 */
public class ORIGINALDATA extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

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
	
	private String deviceFB_id;
	
	private String creator_id;
	
	private String file_path;
	
	private Date date;

	private boolean active;
	
	private long create_time;
	
	private List<ORIGINALDEMOD> originalDemodList = new ArrayList<ORIGINALDEMOD>();
	
	private List<ORIGINALALARM> originalAlarmList = new ArrayList<ORIGINALALARM>();

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

	public String getDeviceFB_id() {
		return deviceFB_id;
	}

	public void setDeviceFB_id(String deviceFB_id) {
		this.deviceFB_id = deviceFB_id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<ORIGINALDEMOD> getOriginalDemodList() {
		return originalDemodList;
	}

	public void setOriginalDemodList(List<ORIGINALDEMOD> originalDemodList) {
		this.originalDemodList = originalDemodList;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public List<ORIGINALALARM> getOriginalAlarmList() {
		return originalAlarmList;
	}

	public void setOriginalAlarmList(List<ORIGINALALARM> originalAlarmList) {
		this.originalAlarmList = originalAlarmList;
	}
	
}
