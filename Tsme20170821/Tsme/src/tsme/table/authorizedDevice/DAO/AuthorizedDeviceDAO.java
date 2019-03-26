package tsme.table.authorizedDevice.DAO;

import java.util.List;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;
import tsme.table.authorizedDevice.bean.AUTHORIZEDDEVICE;

public interface AuthorizedDeviceDAO extends TsmeMainDAOPractice<AUTHORIZEDDEVICE> {

	public List<String> getAuthorizedDeviceNumList();
	
}
