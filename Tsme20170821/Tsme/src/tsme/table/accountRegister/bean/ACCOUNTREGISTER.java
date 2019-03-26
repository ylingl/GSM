package tsme.table.accountRegister.bean;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class ACCOUNTREGISTER extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5762184302673956462L;


	/**
	 * VARCHAR2(50 BYTE) 唯一标识
	 * 非空
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	@NotEmpty
	@Length(max=20)
	private String stamp;
	
	/**
	 * VARCHAR2(150 BYTE) 
	 * 非空
	 */
	@NotEmpty
	@Length(max=150)
	private String apply_id;
	
	/**
	 * 
	 */
	@NotNull
	private Date reg_date;
	
	/**
	 * 
	 */
	@NotNull
	private boolean reg_done;
	
	/**
	 * 
	 */
	private Date reg_done_date;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStamp(){
		return stamp;
	}
	
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}
	
	
	public String getApply_id(){
		return apply_id;
	}
	
	public void setApply_id(String apply_id){
		this.apply_id = apply_id;
	}
	
	public Date getReg_date(){
		return reg_date;
	}
	
	public void setReg_date(Date reg_date){
		this.reg_date = reg_date;
	}
	
	public Date getReg_done_date(){
		return reg_done_date;
	}
	
	public void setReg_done_date(Date reg_done_date){
		this.reg_done_date = reg_done_date;
	}

	public boolean isReg_done() {
		return reg_done;
	}

	public void setReg_done(boolean reg_done) {
		this.reg_done = reg_done;
	}
}
