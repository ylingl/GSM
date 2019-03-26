package mvc.history;

import java.util.ArrayList;
import java.util.List;

import tsme.table.deviceFB.bean.DEVICEFB;
import tsme.table.deviceWT.bean.DEVICEWT;

public class TemplateDeviceFB {

	private DEVICEWT deviceWT;
	
	private List<DEVICEFB> deviceFBList = new ArrayList<DEVICEFB>();

	public DEVICEWT getDeviceWT() {
		return deviceWT;
	}

	public void setDeviceWT(DEVICEWT deviceWT) {
		this.deviceWT = deviceWT;
	}

	public List<DEVICEFB> getDeviceFBList() {
		return deviceFBList;
	}

	public void setDeviceFBList(List<DEVICEFB> deviceFBList) {
		this.deviceFBList = deviceFBList;
	}
	
}
