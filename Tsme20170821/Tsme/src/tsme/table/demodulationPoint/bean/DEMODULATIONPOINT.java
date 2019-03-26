package tsme.table.demodulationPoint.bean;

import java.util.ArrayList;
import java.util.List;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;
import tsme.table.demodulationResult.bean.DEMODULATIONRESULT;

public class DEMODULATIONPOINT extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8880164560845767257L;

	private String id;
	
	private String frequencyBand_id;
	
	private int row;
	
	private float x;
	
	private boolean active;
	
	private long create_time;
	
	private List<DEMODULATIONRESULT> demodulationPointResultList = new ArrayList<DEMODULATIONRESULT>();
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getFrequencyBand_id() {
		return frequencyBand_id;
	}

	public void setFrequencyBand_id(String frequencyBand_id) {
		this.frequencyBand_id = frequencyBand_id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public List<DEMODULATIONRESULT> getDemodulationPointResultList() {
		return demodulationPointResultList;
	}

	public void setDemodulationPointResultList(List<DEMODULATIONRESULT> demodulationPointResultList) {
		this.demodulationPointResultList = demodulationPointResultList;
	}
	
}
