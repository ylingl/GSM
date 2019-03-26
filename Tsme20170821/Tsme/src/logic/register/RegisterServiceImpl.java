package logic.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tsme.table.account.DAO.AccountDAO;

@Service("registerService")
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
	@Override
	public boolean register(String stamp, String userName, String password){
		
		if (accountDAO.isUserNameExisted(userName))
			return false;
		
		return accountDAO.saveAccountWithUsernameAndPassword(userName, password);
	}

}
