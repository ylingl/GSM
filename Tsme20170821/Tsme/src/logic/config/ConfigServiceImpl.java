package logic.config;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tsme.table.account.DAO.AccountDAO;
import tsme.table.authorizedDevice.DAO.AuthorizedDeviceDAO;
import tsme.table.authorizedDevice.bean.AUTHORIZEDDEVICE;
import tsme.table.device.DAO.DeviceDAO;

@Service("configService")
public class ConfigServiceImpl implements ConfigService{
	
	@Autowired
	@Qualifier("authorizedDeviceDAO")
	private AuthorizedDeviceDAO authorizedDeviceDAO;
	
	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
	@Autowired
	@Qualifier("deviceDAO")
	private DeviceDAO devcieDAO;

	@SuppressWarnings("unchecked")
	public List<AUTHORIZEDDEVICE> findAllAuthorizedDevice(boolean active){
		String sql = "SELECT * FROM authorizedDevice WHERE active=" + (active ? "1" : "0") + " ORDER BY device_num ASC";
		List<AUTHORIZEDDEVICE> authorizedDeviceList = (List<AUTHORIZEDDEVICE>) authorizedDeviceDAO.findByQuery(sql, AUTHORIZEDDEVICE.class);
		
		HashMap<String, String> idNameMap = new HashMap<String, String>();
		for(AUTHORIZEDDEVICE temp : authorizedDeviceList) {
			//注意！！仅用于显示做的小trick
			if(idNameMap.containsKey(temp.getCreator_id())){
				temp.setCreator_id(idNameMap.get(temp.getCreator_id()));
			} else {
				sql = "SELECT real_name FROM account WHERE id='" + temp.getCreator_id() + "' AND active=1";
				String realName = (String) accountDAO.findByQueryForList(sql, String.class).get(0);
				idNameMap.put(temp.getCreator_id(), realName);
				temp.setCreator_id(realName);
			}
		}
		
		return authorizedDeviceList;
	}
	
	@SuppressWarnings("unchecked")
	public int saveAuthorizedDevice(AUTHORIZEDDEVICE authorizedDevice, String creator_id){
		String sql = "SELECT * FROM authorizedDevice WHERE device_num='" + authorizedDevice.getDevice_num() + "'";
		List<AUTHORIZEDDEVICE> authorizedDeviceList = (List<AUTHORIZEDDEVICE>) authorizedDeviceDAO.findByQuery(sql, AUTHORIZEDDEVICE.class);
		if(authorizedDeviceList.isEmpty()){
			return authorizedDeviceDAO.save(authorizedDevice);
		} else {
			if(authorizedDeviceList.get(0).isActive()){
				return -1;
			} else {
				if(authorizedDeviceList.get(0).getCreator_id().equalsIgnoreCase(creator_id)){
					authorizedDevice.setId(authorizedDeviceList.get(0).getId());
					return authorizedDeviceDAO.update(authorizedDevice);
				} else {
					return -1;
				}
			}
		}
	}
	
	public boolean deleteAuthorizedDevice(String deviceNum){
		String sql = "SELECT deviceWT.id FROM device, deviceWT WHERE device.device_num='" + deviceNum + "' AND device.id=deviceWT.device_id";
		if(devcieDAO.findByQueryForList(sql, String.class).isEmpty()){
			sql = "DELETE FROM authorizedDevice WHERE device_num='" + deviceNum + "'";
			authorizedDeviceDAO.executeBySql(sql);
			return true;
		} else {
			sql = "UPDATE authorizedDevice SET active=0 WHERE device_num='" + deviceNum + "'";
			authorizedDeviceDAO.executeBySql(sql);
			return false;
		}
		
	}
	
	public boolean updateAuthorizedDeviceExpiryDate(String deviceNum, String expiryDate){
		String sql = "UPDATE authorizedDevice SET expiry_date='" + expiryDate + "' WHERE device_num='" + deviceNum + "'";
		authorizedDeviceDAO.executeBySql(sql);
		return true;
	}
}
