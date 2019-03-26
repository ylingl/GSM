package logic.map;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mvc.map.GeoMsg;
import tsme.table.baseStation.DAO.BaseStationDAO;
import tsme.table.device.DAO.DeviceDAO;
import utils.DataPoolTools;

@Service("mapService")
public class MapServiceImpl implements MapService {

	@Autowired
	@Qualifier("baseStationDAO")
	BaseStationDAO baseStationDAO;

	@Autowired
	@Qualifier("deviceDAO")
	DeviceDAO deviceDAO;

	@SuppressWarnings("unchecked")
	public List<GeoMsg> getBSGeoMsg() {
		String sql = "SELECT baseStation.NAME, baseStation.BSIC, baseStation.model, baseStation.introduction , "
				+ "bsLocation.LAT, bsLocation.LNG, bsLocation.km_stone FROM bsLocation, baseStation WHERE "
				+ "bsLocation.baseStation_id=baseStation.id AND baseStation.active=1 AND bsLocation.active=1";
		
		List<GeoMsg> geoMsgList = (List<GeoMsg>) baseStationDAO.findByQuery(sql, GeoMsg.class);
		
		for (GeoMsg temp : geoMsgList) {		
			temp.setPoint_type("BS");
			temp.setState("normal");
		}

		return geoMsgList;
	}

	@SuppressWarnings("unchecked")
	public List<GeoMsg> getDeviceGeoMsg() {
		String sql = "SELECT device_num, device_type, LNG, LAT, ip FROM device WHERE active=1";
		
		List<GeoMsg> geoMsgList = (List<GeoMsg>) deviceDAO.findByQuery(sql, GeoMsg.class);
		
		for (GeoMsg temp : geoMsgList) {
			temp.setIntroduction("TSME2000");
			temp.setPoint_type("DS");
			
			if(DataPoolTools.monitorReportStatusMap.containsKey(temp.getDevice_num())){
				temp.setLAT(DataPoolTools.monitorReportStatusMap.get(temp.getDevice_num()).getLatitude());
				temp.setLNG(DataPoolTools.monitorReportStatusMap.get(temp.getDevice_num()).getLongitude());
				temp.setState("normal");
			} else {
				temp.setState("offline");
			}
		}
		
		return geoMsgList;
	}
	
}
