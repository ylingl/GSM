package tsme.table.trainerAudit.bean;

import java.util.Date;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class TRAINERAUDIT extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8937636273619877368L;

	private String id;
	
	private String account_id;
	
	private String real_name;
	
	private String phone_num;
	
	private String state;
	
	private String auditor_id;
	
	private String auditor_name;
	
	private String audit_opinions;
	
	private Date audit_date;
	
	private Date create_date;
	
	private boolean active;
	
	private long create_time;
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAudit_opinions() {
		return audit_opinions;
	}

	public void setAudit_opinions(String audit_opinions) {
		this.audit_opinions = audit_opinions;
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

	public String getAuditor_name() {
		return auditor_name;
	}

	public void setAuditor_name(String auditor_name) {
		this.auditor_name = auditor_name;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getAuditor_id() {
		return auditor_id;
	}

	public void setAuditor_id(String auditor_id) {
		this.auditor_id = auditor_id;
	}

	public Date getAudit_date() {
		return audit_date;
	}

	public void setAudit_date(Date audit_date) {
		this.audit_date = audit_date;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
}
