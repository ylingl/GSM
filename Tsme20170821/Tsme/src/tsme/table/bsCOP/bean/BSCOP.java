package tsme.table.bsCOP.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

/**
 * @date 20150922
 * @author lmq
 * ��վ������Ϣ��
 */
public class BSCOP extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6106142927127516395L;
	
	/**
	 * VARCHAR2(50 BYTE) ����
	 * �ǿ�
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	/**
	 * VARCHAR2(50 BYTE) �������վ������
	 * �ǿ�
	 */
	@NotEmpty
	@Length(max=50)
	private String baseStation_id;
	
	private long create_time;
	
	/**
	 * ״̬
	 */
	private String status;
	
	/**
	 * �����¹�ԭ��
	 */
	private String reason;
	
	/**
	 * �Ƿ��ѷ����¹�֪ͨ
	 */
	private boolean notify_flag;

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getBase_station_id() {
		return base_station_id;
	}

	public void setBase_station_id(String base_station_id) {
		this.base_station_id = base_station_id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isNotify_flag() {
		return notify_flag;
	}

	public void setNotify_flag(boolean notify_flag) {
		this.notify_flag = notify_flag;
	}

	public String getId() {
		return id;
	}
}
