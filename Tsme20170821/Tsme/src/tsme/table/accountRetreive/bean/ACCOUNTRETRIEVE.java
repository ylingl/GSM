package tsme.table.accountRetreive.bean;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class ACCOUNTRETRIEVE extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{
	
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
	private Date retrieve_date;

	/**
	 * 
	 */
	@NotNull
	private boolean retrieve_done;
	
	/**
	 * 
	 */
	private Date retrieve_done_date;
	
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
	
	public Date getRetrieve_date(){
		return retrieve_date;
	}
	
	public void setRetrieve_date(Date retrieve_date){
		this.retrieve_date = retrieve_date;
	}
	
	public Date getRetrieve_done_date(){
		return retrieve_done_date;
	}
	
	public void setRetrieve_done_date(Date retrieve_done_date){
		this.retrieve_done_date = retrieve_done_date;
	}

	public boolean isRetrieve_done() {
		return retrieve_done;
	}

	public void setRetrieve_done(boolean retrieve_done) {
		this.retrieve_done = retrieve_done;
	}
}
