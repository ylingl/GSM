package tsme.table.baseStation.bean;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.bsLocation.bean.BSLOCATION;

/**
 * @date 2010922
 * @author lmq
 * ��վ��Ϣ��һ���ӱ�
 */
public class BASESTATION extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9013425786867550830L;
	
	/**
	 * VARCHAR2(50 BYTE) ����
	 * �ǿ�
	 */
	@NotEmpty
	@Length(max=50)
	private String id;
	
	/**
	 * VARCHAR2(50 BYTE)���
	 * �ǿ�
	 */
	@NotEmpty
	@Length(max=50)
	private String cell_id;
	
	/**
	 * ��վ����
	 */
	private String name;
	
	/**
	 * ��վʶ����
	 */
	private String BSIC;
	
	/**
	 * ��վվ��
	 */
	private String model;
	
	/**
	 * ɨ��뾶
	 */
	private float radius;
	
	/**
	 * ��վ���
	 */
	private String introduction;
	
	/**
	 * �Ƿ�ɼ�
	 */
	private boolean active = true;
	

	private long create_time;
	
	
	private List<BSLOCATION> bsLocationList = new ArrayList<BSLOCATION>();
	

	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public String getBSIC() {
		return BSIC;
	}

	public void setBSIC(String bSIC) {
		BSIC = bSIC;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCell_id() {
		return cell_id;
	}

	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}

	public List<BSLOCATION> getBsLocationList() {
		return bsLocationList;
	}

	public void setBsLocationList(List<BSLOCATION> bsLocationList) {
		this.bsLocationList = bsLocationList;
	}
}

