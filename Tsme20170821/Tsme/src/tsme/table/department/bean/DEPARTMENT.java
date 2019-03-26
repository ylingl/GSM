package tsme.table.department.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

/**
 * @date 20150922
 * @author lmq
 * ������Ϣ��
 */
public class DEPARTMENT extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6665007986413229152L;

	/**
	 * VARCHAR2(50 BYTE) ����
	 * �ǿ�
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	/**
	 * ��������
	 */
	private String name;
	
	/**
	 * ���ű���
	 */
	private String depart_num;
	
	private boolean active;
	
	private long create_time;

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getDepart_num() {
		return depart_num;
	}

	public void setDepart_num(String depart_num) {
		this.depart_num = depart_num;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
