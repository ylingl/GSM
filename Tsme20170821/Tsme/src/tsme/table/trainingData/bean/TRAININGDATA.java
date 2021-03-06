package tsme.table.trainingData.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

/**
 * @date 20150922
 * @author lmq
 * 数据分析原始数据表
 */
public class TRAININGDATA extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3954746905352613145L;

	/**
	 * VARCHAR2(50 BYTE) 主键
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	private String frequencyBand_id;
	
	private String file_path;

	private boolean active;
	
	private long create_time;

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

	public String getFrequencyBand_id() {
		return frequencyBand_id;
	}

	public void setFrequencyBand_id(String frequencyBand_id) {
		this.frequencyBand_id = frequencyBand_id;
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
