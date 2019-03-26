package tsme.table.authorizedDevice.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.authorizedDevice.bean.AUTHORIZEDDEVICE;

@Repository("authorizedDeviceDAO")
public class AuthorizedDeviceDAOImpl extends TsmeMainDAOPracticeImpl<AUTHORIZEDDEVICE> implements AuthorizedDeviceDAO{

	@SuppressWarnings("unchecked")
	public List<String> getAuthorizedDeviceNumList(){
		String sql = "SELECT device_num FROM authorizedDevice WHERE active=1 " + 
					"AND TO_DAYS(expiry_date) - TO_DAYS(NOW()) >= 0 ORDER BY device_num ASC";
		
		return (List<String>) this.findByQueryForList(sql, String.class);
	}
	
}
