package tsme.table.warningTemplate.bean;

import java.util.ArrayList;
import java.util.List;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.frequencyBand.bean.FREQUENCYBAND;

public class WARNINGTEMPLATE extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8785829159098252159L;

	private String id;
	
	private String device_id;
	
	private String creator_id;
	
	private String template_name;
	
	private boolean active = true;
	
	private long create_time;
	
	private double LNG;
	
	private double LAT;
	
	private int fftSize;
	
	private int bandWidth;
	
	private int maxMeans;
	
	private List<FREQUENCYBAND> frequencyBandList = new ArrayList<FREQUENCYBAND>();
	
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public double getLNG() {
		return LNG;
	}

	public void setLNG(double lNG) {
		LNG = lNG;
	}

	public double getLAT() {
		return LAT;
	}

	public void setLAT(double lAT) {
		LAT = lAT;
	}

	public String getTemplate_name() {
		return template_name;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	public int getFftSize() {
		return fftSize;
	}

	public void setFftSize(int fftSize) {
		this.fftSize = fftSize;
	}
	
	public int getMaxMeans() {
		return maxMeans;
	}

	public void setMaxMeans(int maxMeans) {
		this.maxMeans = maxMeans;
	}

	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		this.bandWidth = bandWidth;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<FREQUENCYBAND> getFrequencyBandList() {
		return frequencyBandList;
	}

	public void setFrequencyBandList(List<FREQUENCYBAND> frequencyBandList) {
		this.frequencyBandList = frequencyBandList;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}
}
