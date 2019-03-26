package mvc.device;

import java.util.ArrayList;
import java.util.List;

public class DemodResultStatic extends DemodResultParam{
	
	private String indicatorOfSCHInfo;
	
	private String pduType;
	
	private String CIValue;
	
	private String mobileCountryCode;
	
	private String mobileNetworkCode;
	
	private String locationAreaCode;
	
	private String RAColour;
	
	private String SI13Position;
	
	private long count;

	private List<Float> carrierToInterferenceList = new ArrayList<Float>();
	
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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getPduType() {
		return pduType;
	}

	public void setPduType(String pduType) {
		this.pduType = pduType;
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

	public String getIndicatorOfSCHInfo() {
		return indicatorOfSCHInfo;
	}

	public void setIndicatorOfSCHInfo(String indicatorOfSCHInfo) {
		this.indicatorOfSCHInfo = indicatorOfSCHInfo;
	}

	public List<Float> getCarrierToInterferenceList() {
		return carrierToInterferenceList;
	}

	public void setCarrierToInterferenceList(List<Float> carrierToInterferenceList) {
		this.carrierToInterferenceList = carrierToInterferenceList;
	}
	
}
