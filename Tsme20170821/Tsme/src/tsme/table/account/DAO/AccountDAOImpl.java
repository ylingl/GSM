package tsme.table.account.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import security.MyUserDetails.MyUserDetails;
import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.account.bean.ACCOUNT;
import tsme.table.accountProperty.bean.ACCOUNTPROPERTY;

@Repository("accountDAO")
public class AccountDAOImpl extends TsmeMainDAOPracticeImpl<ACCOUNT> implements AccountDAO{

	@Override
	public MyUserDetails findAccountByUsername(String username){
		String sql = "SELECT * FROM account WHERE username='" + username + "' AND active=1";
		
		return (MyUserDetails) findByQuery(sql, MyUserDetails.class).get(0);
	}
	
	@Override
	public boolean isUserNameExisted(String userName) {
		String sql = "SELECT id FROM account WHERE userName='" + userName + "'";
		
		return !findByQueryForList(sql, String.class).isEmpty();
	}
	
	@Override
	public boolean saveAccountWithUsernameAndPassword(String username, String password){
		ACCOUNT account = new ACCOUNT();
		
		Date date = new Date();
		account.setUsername(username);
		account.setPassword(password);
		if(username.contains("@"))
			account.setEmail(username);
		account.setActive(true);
		account.setCreate_time(date.getTime());
		
		List<ACCOUNTPROPERTY> accountPropertyList = new ArrayList<ACCOUNTPROPERTY>();
		ACCOUNTPROPERTY accountProperty = new ACCOUNTPROPERTY();
		accountProperty.setActive(true);
		accountProperty.setCreate_time(date.getTime());
		accountProperty.setAccount_id(account.getId());
		accountProperty.setRole_code("ROLE_TESTER");
		accountPropertyList.add(accountProperty);
		account.setAccountPropertyList(accountPropertyList);
		
		return this.cascadedSave(account);
	}
	
	@SuppressWarnings("unchecked")
	public ACCOUNT findByUsername(String username){
		String sql = "SELECT * FROM account WHERE username='" + username + "'";
		
		List<ACCOUNT> account = (List<ACCOUNT>) this.findByQuery(sql, ACCOUNT.class);
		
		return account.size()==0 ? null : account.get(0);
	}
	
	public boolean saveAccountWithApplyId(String applyId, String password){
		ACCOUNT account = new ACCOUNT();
		
		Date date = new Date();
		account.setUsername(applyId);
		account.setPassword(password);
		
		if(applyId.contains("@"))
			account.setEmail(applyId);
		else
			account.setPhone_num(applyId);
		
		account.setActive(true);
		account.setCreate_time(date.getTime());
		
		List<ACCOUNTPROPERTY> accountPropertyList = new ArrayList<ACCOUNTPROPERTY>();
		ACCOUNTPROPERTY accountProperty = new ACCOUNTPROPERTY();
		accountProperty.setActive(true);
		accountProperty.setCreate_time(date.getTime());
		accountProperty.setAccount_id(account.getId());
		accountProperty.setRole_code("ROLE_USER");
		accountPropertyList.add(accountProperty);
		account.setAccountPropertyList(accountPropertyList);	
		
		return this.cascadedSave(account);
	}
}
