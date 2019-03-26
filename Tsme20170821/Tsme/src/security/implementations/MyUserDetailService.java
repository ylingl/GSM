package security.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import security.MyUserDetails.MyProperty;
import security.MyUserDetails.MyUserDetails;
import tsme.table.account.DAO.AccountDAO;
import tsme.table.accountProperty.DAO.AccountPropertyDAO;
import tsme.table.accountProperty.bean.ACCOUNTPROPERTY;

public class MyUserDetailService implements UserDetailsService{

	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
	@Autowired
	@Qualifier("accountPropertyDAO")
	private AccountPropertyDAO accountPropertyDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MyUserDetails myUserDetails = new MyUserDetails();
		
		myUserDetails = accountDAO.findAccountByUsername(username);

		List<ACCOUNTPROPERTY> accountPropertyList = accountPropertyDAO.findByForeignKey(myUserDetails.getId());
		List<MyProperty> myPropertyList = new ArrayList<MyProperty>();
		for(ACCOUNTPROPERTY accountProperty : accountPropertyList){
			MyProperty myProperty = new MyProperty();
			myProperty.setRole(accountProperty.getRole_code());
			myPropertyList.add(myProperty);
		}
		
		myUserDetails.setMyPropertyList(myPropertyList);
		
		return myUserDetails;
	}
	
}
