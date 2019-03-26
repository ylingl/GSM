package logic.config;

import java.util.List;

import tsme.table.authorizedDevice.bean.AUTHORIZEDDEVICE;

public interface ConfigService {
	
	public List<AUTHORIZEDDEVICE> findAllAuthorizedDevice(boolean active);
	
	public int saveAuthorizedDevice(AUTHORIZEDDEVICE authorizedDevice, String creator_id);
	
	public boolean deleteAuthorizedDevice(String deviceNum);
	
	public boolean updateAuthorizedDeviceExpiryDate(String deviceNum, String expiryDate);
	
}
