package tsme.table.account.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.accountProperty.bean.ACCOUNTPROPERTY;

public class ACCOUNT extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2145918257123276922L;

	private String id;
	
	private String department_id;
	
	private String real_name;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String phone_num;
	
	private boolean active;
	
	private long create_time;
	
	private List<ACCOUNTPROPERTY> accountPropertyList = new ArrayList<ACCOUNTPROPERTY>();
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getDepartment_id() {
		return department_id;
	}
	
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	
	public long getCreate_time() {
		return create_time;
	}
	
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		if(username != null && !username.isEmpty())
			this.password = new Md5PasswordEncoder().encodePassword(password, username);
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ACCOUNTPROPERTY> getAccountPropertyList() {
		return accountPropertyList;
	}

	public void setAccountPropertyList(List<ACCOUNTPROPERTY> accountPropertyList) {
		this.accountPropertyList = accountPropertyList;
	}
	
}
