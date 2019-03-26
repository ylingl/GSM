package tsme.table.cellGeneralTemplate.bean;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class CELLGENERALTEMPLATE extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5411659985026562202L;

	private String id;
	
	private String cell_id;
	
	private String template_id;
	
	private String frequency_range;
	
	private int RxLev;
	
	private int C_I;
	
	private int frequency;
	
	private String BCCH;
	
	private String cell;
	
	private String precision;
	
	private String TRX;
	
	private long create_time;
	
	private boolean visible;
	
	private String element;
	
	private String value;
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getCell_id() {
		return cell_id;
	}

	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getFrequency_range() {
		return frequency_range;
	}

	public void setFrequency_range(String frequency_range) {
		this.frequency_range = frequency_range;
	}

	public int getRxLev() {
		return RxLev;
	}

	public void setRxLev(int rxLev) {
		RxLev = rxLev;
	}

	public int getC_I() {
		return C_I;
	}

	public void setC_I(int c_I) {
		C_I = c_I;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getBCCH() {
		return BCCH;
	}

	public void setBCCH(String bCCH) {
		BCCH = bCCH;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getTRX() {
		return TRX;
	}

	public void setTRX(String tRX) {
		TRX = tRX;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}
}
