package tsme.table.warningLine.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

/**
 * @date 20150922
 * @author lmq
 * 数据分析原始数据表
 */
public class WARNINGLINE extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

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

	private String frequencyBand_id;
	
	private int baseRow;
	
	private int peakRow;
	
	private float x;
	
	private float y; 
	
	private boolean active;
	
	private long create_time;
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

	public int getBaseRow() {
		return baseRow;
	}

	public void setBaseRow(int baseRow) {
		this.baseRow = baseRow;
	}

	public int getPeakRow() {
		return peakRow;
	}

	public void setPeakRow(int peakRow) {
		this.peakRow = peakRow;
	}

	public String getId() {
		return id;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
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
