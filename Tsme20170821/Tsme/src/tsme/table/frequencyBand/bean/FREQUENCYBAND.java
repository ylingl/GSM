package tsme.table.frequencyBand.bean;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.avgData.bean.AVGDATA;
import tsme.table.avgExtreme.bean.AVGEXTREME;
import tsme.table.demodulationPoint.bean.DEMODULATIONPOINT;
import tsme.table.trainingData.bean.TRAININGDATA;
import tsme.table.warningLine.bean.WARNINGLINE;

/**
 * @date 20150922
 * @author lmq
 * 数据分析原始数据表
 */
public class FREQUENCYBAND extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2915171282825234175L;
	

	/**
	 * VARCHAR2(50 BYTE) 主键
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	private String warningTemplate_id;
	
	private float startFrequency;
	
	private float stopFrequency;

	private boolean active = true;
	
	private long create_time;
	
	private List<AVGDATA> avgDataList = new ArrayList<AVGDATA>();
	
	private List<AVGEXTREME> avgExtremeList = new ArrayList<AVGEXTREME>();
	
	private List<WARNINGLINE> warninglineList = new ArrayList<WARNINGLINE>();
	
	private List<TRAININGDATA> trainingDataList = new ArrayList<TRAININGDATA>();
	
	private List<DEMODULATIONPOINT> demodulationPointList = new ArrayList<DEMODULATIONPOINT>();

	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getWarningTemplate_id() {
		return warningTemplate_id;
	}

	public void setWarningTemplate_id(String warningTemplate_id) {
		this.warningTemplate_id = warningTemplate_id;
	}

	public float getStartFrequency() {
		return startFrequency;
	}

	public void setStartFrequency(float startFrequency) {
		this.startFrequency = startFrequency;
	}

	public float getStopFrequency() {
		return stopFrequency;
	}

	public void setStopFrequency(float stopFrequency) {
		this.stopFrequency = stopFrequency;
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

	public List<AVGDATA> getAvgDataList() {
		return avgDataList;
	}

	public void setAvgDataList(List<AVGDATA> avgDataList) {
		this.avgDataList = avgDataList;
	}

	public List<AVGEXTREME> getAvgExtremeList() {
		return avgExtremeList;
	}

	public void setAvgExtremeList(List<AVGEXTREME> avgExtremeList) {
		this.avgExtremeList = avgExtremeList;
	}

	public List<WARNINGLINE> getWarninglineList() {
		return warninglineList;
	}

	public void setWarninglineList(List<WARNINGLINE> warninglineList) {
		this.warninglineList = warninglineList;
	}

	public List<TRAININGDATA> getTrainingDataList() {
		return trainingDataList;
	}

	public void setTrainingDataList(List<TRAININGDATA> trainingDataList) {
		this.trainingDataList = trainingDataList;
	}

	public List<DEMODULATIONPOINT> getDemodulationPointList() {
		return demodulationPointList;
	}

	public void setDemodulationPointList(List<DEMODULATIONPOINT> demodulationPointList) {
		this.demodulationPointList = demodulationPointList;
	}
	
}
