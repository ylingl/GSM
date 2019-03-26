package tsme.table.account.DAO;

import security.MyUserDetails.MyUserDetails;
import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;
import tsme.table.account.bean.ACCOUNT;

public interface AccountDAO extends TsmeMainDAOPractice<ACCOUNT>{
	
	public MyUserDetails findAccountByUsername(String username);
	
	public boolean isUserNameExisted(String userName);
	
	public boolean saveAccountWithUsernameAndPassword(String userName, String password);
	
	public ACCOUNT findByUsername(String username);
	
	public boolean saveAccountWithApplyId(String applyId, String password);
	
}
