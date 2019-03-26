package mvc.device;

import java.util.ArrayList;
import java.util.List;

public class ResponseMonitorParam {

	private int interval; //ˢ��Ƶ�ʣ�������
	
	private List<Float> frequencyList = new ArrayList<Float>();// ��ʼƵ��
	
	private int maxMeans;// �����Ƶ��
	
	private int bandWidth;// ����
	
	private int fftSize;// ftt ����
	
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
