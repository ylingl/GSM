package task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import mvc.device.ReportStatus;
import tsme.table.authorizedDevice.DAO.AuthorizedDeviceDAO;
import tsme.table.device.DAO.DeviceDAO;
import tsme.table.device.bean.DEVICE;
import utils.DataPoolTools;

@Component
public class Tasks {
	
	@Autowired
	@Qualifier("deviceDAO")
	private DeviceDAO deviceDAO;
	
	@Autowired
	@Qualifier("authorizedDeviceDAO")
	private AuthorizedDeviceDAO authorizedDeviceDAO;
	
	@SuppressWarnings("unchecked")
	public void offlineJob(){
		List<String> offlineDeviceNum = new ArrayList<String>();
		
		long now = System.currentTimeMillis();
		for(Entry<String, ReportStatus> reportStatusEntry : DataPoolTools.monitorReportStatusMap.entrySet()){
			if((now - reportStatusEntry.getValue().getUpdateTime()) > 30 * 1000){
				offlineDeviceNum.add(reportStatusEntry.getKey());
			}
		}
		
		for(String deviceNum : offlineDeviceNum){
			ReportStatus reportStatus = DataPoolTools.monitorReportStatusMap.get(deviceNum);
			
			String sql = "SELECT * FROM device WHERE device_num = '" + deviceNum + "' AND active = 1";
			
			List<DEVICE> deviceList = (List<DEVICE>) deviceDAO.findByQuery(sql, DEVICE.class);
			DEVICE device = deviceList.get(0);
			
			device.setLAT(reportStatus.getLatitude());
			device.setLNG(reportStatus.getLongitude());
			device.setIp(reportStatus.getIpAddress());
			device.setCreate_time(System.currentTimeMillis());
			
			deviceDAO.update(device);
			
			DataPoolTools.monitorReportStatusMap.remove(deviceNum);
			DataPoolTools.offlineReportStatusMap.put(deviceNum, reportStatus);
		}
	}
	
	public void loadAuthorizedDeviceNumSet(){
		List<String> authorizedDeviceNumList = authorizedDeviceDAO.getAuthorizedDeviceNumList();
		for(String authorizedDeviceNum : DataPoolTools.authorizedDeviceNumSet){
			if(!authorizedDeviceNumList.contains(authorizedDeviceNum)){
				if(DataPoolTools.deviceDemodParameterMap.containsKey(authorizedDeviceNum)){
					DataPoolTools.deviceDemodParameterMap.get(authorizedDeviceNum).setTraining(false);
					DataPoolTools.deviceDemodParameterMap.get(authorizedDeviceNum).setWarning(false);
				}
			}
		}
		
		DataPoolTools.authorizedDeviceNumSet.clear();
		DataPoolTools.authorizedDeviceNumSet.addAll(authorizedDeviceNumList);
	}
}
