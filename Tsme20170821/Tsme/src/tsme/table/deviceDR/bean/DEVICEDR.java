package tsme.table.deviceDR.bean;

import tsme.bean.mainBeanPractice.TsmeMainBeanPractice;
import tsme.bean.mainBeanPractice.TsmeMainBeanPracticeImpl;

public class DEVICEDR extends TsmeMainBeanPracticeImpl implements TsmeMainBeanPractice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1512832286178008823L;

	private String id;
	
	private String originalDemod_id;
	
	private int groupNum;
	
	private String pduType;
	
	private String indicatorOfSCHInfo;
	
	private float avgCTI;
	
	private String CIValue;
	
	private long CICount;
	
	private String mobileCountryCode;
	
	private String mobileNetworkCode;
	
	private String locationAreaCode;
	
	private String RAColour;
	
	private String SI13Position;
	
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

	public String getPduType() {
		return pduType;
	}

	public void setPduType(String pduType) {
		this.pduType = pduType;
	}

	public String getCIValue() {
		return CIValue;
	}

	public void setCIValue(String cIValue) {
		CIValue = cIValue;
	}

	public String getMobileCountryCode() {
		return mobileCountryCode;
	}

	public void setMobileCountryCode(String mobileCountryCode) {
		this.mobileCountryCode = mobileCountryCode;
	}

	public String getMobileNetworkCode() {
		return mobileNetworkCode;
	}

	public void setMobileNetworkCode(String mobileNetworkCode) {
		this.mobileNetworkCode = mobileNetworkCode;
	}

	public String getLocationAreaCode() {
		return locationAreaCode;
	}

	public void setLocationAreaCode(String locationAreaCode) {
		this.locationAreaCode = locationAreaCode;
	}

	public String getRAColour() {
		return RAColour;
	}

	public void setRAColour(String rAColour) {
		RAColour = rAColour;
	}

	public String getSI13Position() {
		return SI13Position;
	}

	public void setSI13Position(String sI13Position) {
		SI13Position = sI13Position;
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

	public long getCICount() {
		return CICount;
	}

	public void setCICount(long cICount) {
		CICount = cICount;
	}

	public String getIndicatorOfSCHInfo() {
		return indicatorOfSCHInfo;
	}

	public void setIndicatorOfSCHInfo(String indicatorOfSCHInfo) {
		this.indicatorOfSCHInfo = indicatorOfSCHInfo;
	}

	public float getAvgCTI() {
		return avgCTI;
	}

	public void setAvgCTI(float avgCTI) {
		this.avgCTI = avgCTI;
	}

	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public String getOriginalDemod_id() {
		return originalDemod_id;
	}

	public void setOriginalDemod_id(String originalDemod_id) {
		this.originalDemod_id = originalDemod_id;
	}

}
