package mvc.device;

import java.util.ArrayList;
import java.util.List;

public class ResponseMonitorParam {

	private int interval; //刷新频率，毫秒数
	
	private List<Float> frequencyList = new ArrayList<Float>();// 开始频率
	
	private int maxMeans;// 最大监测频率
	
	private int bandWidth;// 带宽
	
	private int fftSize;// ftt 步长
	
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<Float> getFrequencyList() {
		return frequencyList;
	}

	public void setFrequencyList(List<Float> frequencyList) {
		this.frequencyList = frequencyList;
	}

	public int getMaxMeans() {
		return maxMeans;
	}

	public void setMaxMeans(int maxMeans) {
		this.maxMeans = maxMeans;
	}

	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		this.bandWidth = bandWidth;
	}

	public int getFftSize() {
		return fftSize;
	}

	public void setFftSize(int fftSize) {
		this.fftSize = fftSize;
	}

}
