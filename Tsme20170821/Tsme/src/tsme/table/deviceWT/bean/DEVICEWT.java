package tsme.table.deviceWT.bean;

import java.util.ArrayList;
import java.util.List;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.deviceFB.bean.DEVICEFB;

/**
 * 存放 设备监测参数指标
 * @author LMQ
 *
 */
public class DEVICEWT extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8715633179639543794L;
	
	private String id;
	
	private String device_id;
	
	private String template_id;
	
	private String template_name;
	
	private double LNG;
	
	private double LAT;
	
	private int fftSize;
	
	private int bandWidth;
	
	private int maxMeans;
	
	private boolean active = true;
	
	private long create_time;
	
	private List<DEVICEFB> deviceFBList = new ArrayList<DEVICEFB>();

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	
	public String getId() {
		return id;
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

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getTemplate_name() {
		return template_name;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
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

	public int getFftSize() {
		return fftSize;
	}

	public void setFftSize(int fftSize) {
		this.fftSize = fftSize;
	}

	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		this.bandWidth = bandWidth;
	}

	public int getMaxMeans() {
		return maxMeans;
	}

	public void setMaxMeans(int maxMeans) {
		this.maxMeans = maxMeans;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<DEVICEFB> getDeviceFBList() {
		return deviceFBList;
	}

	public void setDeviceFBList(List<DEVICEFB> deviceFBList) {
		this.deviceFBList = deviceFBList;
	}
	
}
