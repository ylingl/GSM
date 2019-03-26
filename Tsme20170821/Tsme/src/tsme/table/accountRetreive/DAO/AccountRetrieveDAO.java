package tsme.table.accountRetreive.DAO;

import tsme.table.accountRetreive.bean.ACCOUNTRETRIEVE;
import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;

public interface AccountRetrieveDAO extends TsmeMainDAOPractice<ACCOUNTRETRIEVE>{

	public ACCOUNTRETRIEVE findAccountRetrieveByStamp(String stamp);
	
	public ACCOUNTRETRIEVE findAccountRetrieveByStampAndApplyId(String stamp, String applyId);
	
}
