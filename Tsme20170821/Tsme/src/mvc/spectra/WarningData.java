package mvc.spectra;

import java.util.ArrayList;
import java.util.List;

public class WarningData {
	
	private float startFrequency = 0; 
	
	private float stopFrequency = 0;
	
	private float centerFrequency = 0;
	
	private String currentTime = "";
	
	private boolean visible = true;
	
	private long number = 1;

	private List<String> warningTimeList = new ArrayList<String>();
	
	public float getStartFrequency() {
		return startFrequency;
	}

	public void setStartFrequency(float startFrequency) {
		this.startFrequency = startFrequency;
	}

	public float getCenterFrequency() {
		return centerFrequency;
	}

	public void setCenterFrequency(float centerFrequency) {
		this.centerFrequency = centerFrequency;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public float getStopFrequency() {
		return stopFrequency;
	}

	public void setStopFrequency(float stopFrequency) {
		this.stopFrequency = stopFrequency;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public List<String> getWarningTimeList() {
		return warningTimeList;
	}

	public void setWarningTimeList(List<String> warningTimeList) {
		this.warningTimeList = warningTimeList;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
