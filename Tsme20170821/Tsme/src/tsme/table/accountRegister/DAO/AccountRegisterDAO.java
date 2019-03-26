package tsme.table.accountRegister.DAO;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;
import tsme.table.accountRegister.bean.ACCOUNTREGISTER;

public interface AccountRegisterDAO extends TsmeMainDAOPractice<ACCOUNTREGISTER>{

	public ACCOUNTREGISTER findAccountRegisterByStamp(String stamp);
	
	public ACCOUNTREGISTER findAccountRegisterByStampAndApplyId(String stamp, String applyId);
	
}
