package mvc.history;

import logic.data.bean.BeanCoordinate;

public class WarningGroup {

	private BeanCoordinate beginPoint = new BeanCoordinate();
	
	private BeanCoordinate endPoint = new BeanCoordinate();
	
	private float centerFrequency = 0;

	public BeanCoordinate getBeginPoint() {
		return beginPoint;
	}

	public void setBeginPoint(BeanCoordinate beginPoint) {
		this.beginPoint = beginPoint;
	}

	public BeanCoordinate getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(BeanCoordinate endPoint) {
		this.endPoint = endPoint;
	}

	public float getCenterFrequency() {
		return centerFrequency;
	}

	public void setCenterFrequency(float centerFrequency) {
		this.centerFrequency = centerFrequency;
	}
	
}
