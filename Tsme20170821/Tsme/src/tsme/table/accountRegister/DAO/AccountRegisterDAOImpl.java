package tsme.table.accountRegister.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import tsme.table.accountRegister.bean.ACCOUNTREGISTER;
import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;

@Repository("accountRegisterDAO")
public class AccountRegisterDAOImpl extends TsmeMainDAOPracticeImpl<ACCOUNTREGISTER> implements AccountRegisterDAO{
	
	@SuppressWarnings("unchecked")
	public ACCOUNTREGISTER findAccountRegisterByStamp(String stamp){
		List<ACCOUNTREGISTER> accountRegister =(List<ACCOUNTREGISTER>) this.findByQuery("SELECT * FROM accountRegister WHERE stamp = '" + stamp +"'", ACCOUNTREGISTER.class);
		
		if (accountRegister.isEmpty())
			return null;
		else
			return accountRegister.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public ACCOUNTREGISTER findAccountRegisterByStampAndApplyId(String stamp, String applyId){
		List<ACCOUNTREGISTER> accountRegister = (List<ACCOUNTREGISTER>) this.findByQuery("SELECT * FROM accountRegister WHERE stamp = '" + stamp +"' and apply_id = '" + applyId + "'", ACCOUNTREGISTER.class);
		
		if (accountRegister.isEmpty())
			return null;
		else
			return accountRegister.get(0);
	}
}
