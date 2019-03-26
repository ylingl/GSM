package tsme.table.accountRetreive.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import tsme.table.accountRetreive.bean.ACCOUNTRETRIEVE;
import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;

@Repository("accountRetrieveDAO")
public class AccountRetrieveDAOImpl  extends TsmeMainDAOPracticeImpl<ACCOUNTRETRIEVE> implements AccountRetrieveDAO{
	
	@SuppressWarnings("unchecked")
	public ACCOUNTRETRIEVE findAccountRetrieveByStamp(String stamp){
		List<ACCOUNTRETRIEVE> accountRetrieve =(List<ACCOUNTRETRIEVE>) this.findByQuery("SELECT * FROM accountRetrieve WHERE stamp = '" + stamp +"'", ACCOUNTRETRIEVE.class);
		
		if (accountRetrieve.isEmpty())
			return null;
		else
			return accountRetrieve.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public ACCOUNTRETRIEVE findAccountRetrieveByStampAndApplyId(String stamp, String applyId){
		List<ACCOUNTRETRIEVE> accountRetrieve =(List<ACCOUNTRETRIEVE>) this.findByQuery("SELECT * FROM accountRetrieve WHERE stamp = '" + stamp +"' and apply_id = '" + applyId + "'", ACCOUNTRETRIEVE .class);
		
		if (accountRetrieve.isEmpty())
			return null;
		else
			return accountRetrieve.get(0);
	}
}
