package logic.security.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import tsme.table.account.DAO.AccountDAO;

@Service("loginService")
public class LoginServiceImpl implements LoginService{

	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean verifyUsernameAndPasswordForWeb(String username, String password){
		
		String sql = "SELECT password FROM account WHERE username='" + username + "'";
		
		List<String> passwordList = (List<String>) accountDAO.findByQueryForList(sql, String.class);
		if(passwordList != null && !passwordList.isEmpty()){
			
			String realPassword = new Md5PasswordEncoder().encodePassword(password, username);
			
			if(realPassword.equalsIgnoreCase(passwordList.get(0))) {
				return true;
			}
			else 
				return false;
		}
		else
			return false;		
	}

}
