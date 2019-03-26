package tsme.table.rescPath.bean;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.controlProperty.bean.CONTROLPROPERTY;

public class RESCPATH extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2312651269744722110L;
	
	/**
	 * 
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	/**
	 * 
	 */
	@NotEmpty
	@Length(max=500)
	private String uri;
	
	/**
	 * 
	 */
	@NotEmpty
	@Length(max=100)
	private String function;
	
	/**
	 * 
	 */
	@NotNull
	private boolean active = true;
	
	/**
	 * 
	 */
	@NotNull
	private long create_time;
	
	private List<CONTROLPROPERTY> controlPropertyList = new LinkedList<CONTROLPROPERTY>();
	
	public String getId(){
		return this.id;
	}

	public void setId(String id){
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<CONTROLPROPERTY> getControlPropertyList() {
		return controlPropertyList;
	}

	public void setControlPropertyList(List<CONTROLPROPERTY> controlPropertyList) {
		this.controlPropertyList = controlPropertyList;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
