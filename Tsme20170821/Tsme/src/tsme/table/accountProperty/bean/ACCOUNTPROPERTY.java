package tsme.table.accountProperty.bean;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class ACCOUNTPROPERTY extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1422136537648975208L;
	
	/**
	 * VARCHAR2(50 BYTE) 唯一标识
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	/**
	 * VARCHAR2(50 BYTE) 外键
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String account_id;
	
	/**
	 * 
	 */
	private String role_code;
	
	/**
	 * 
	 */
	@NotNull
	private boolean active;
	
	/**
	 * 
	 */
	@NotNull
	private long create_time;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;	
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getRole_code() {
		return role_code;
	}

	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}

}
