package logic.map;

import java.util.List;

import mvc.map.GeoMsg;

public interface MapService {
	
	public List<GeoMsg> getBSGeoMsg();
	
	public List<GeoMsg> getDeviceGeoMsg();
	
}
