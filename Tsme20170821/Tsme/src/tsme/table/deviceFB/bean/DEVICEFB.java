package tsme.table.deviceFB.bean;

import java.util.ArrayList;
import java.util.List;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.deviceDP.bean.DEVICEDP;
import tsme.table.deviceWL.bean.DEVICEWL;
import tsme.table.originalData.bean.ORIGINALDATA;

public class DEVICEFB extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5650683550013762013L;

	private String id;
	
	private String deviceWT_id;
	
	private float startFrequency;
	
	private float stopFrequency;
	
	private boolean active = true;
	
	private long create_time;
	
	private List<DEVICEWL> deviceWLList = new ArrayList<DEVICEWL>();
	
	private List<ORIGINALDATA> originalDataList = new ArrayList<ORIGINALDATA>();
	
	private List<DEVICEDP> deviceDPList = new ArrayList<DEVICEDP>();
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDeviceWT_id() {
		return deviceWT_id;
	}

	public void setDeviceWT_id(String deviceWT_id) {
		this.deviceWT_id = deviceWT_id;
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

	public List<DEVICEWL> getDeviceWLList() {
		return deviceWLList;
	}

	public void setDeviceWLList(List<DEVICEWL> deviceWLList) {
		this.deviceWLList = deviceWLList;
	}

	public List<ORIGINALDATA> getOriginalDataList() {
		return originalDataList;
	}

	public void setOriginalDataList(List<ORIGINALDATA> originalDataList) {
		this.originalDataList = originalDataList;
	}

	public List<DEVICEDP> getDeviceDPList() {
		return deviceDPList;
	}

	public void setDeviceDPList(List<DEVICEDP> deviceDPList) {
		this.deviceDPList = deviceDPList;
	}
}
