package mvc.device;

import java.util.ArrayList;
import java.util.List;

public class ResponseDemodParam {
	
	private int si_type;
	
	private int mode_type;
	
	private int interval_ms = 100;//下一次解调前的等待时间，默认值为100ms
	
	private int measure_rate = 1000;//数据刷新频率，多少次/1000s
	
	//序号-frequencyBand-频点
	private List<Float> demodPointList = new ArrayList<Float>();

	public int getSi_type() {
		return si_type;
	}

	public void setSi_type(int si_type) {
		this.si_type = si_type;
	}

	public int getMode_type() {
		return mode_type;
	}

	public void setMode_type(int mode_type) {
		this.mode_type = mode_type;
	}

	public int getInterval_ms() {
		return interval_ms;
	}

	public void setInterval_ms(int interval_ms) {
		this.interval_ms = interval_ms;
	}

	public int getMeasure_rate() {
		return measure_rate;
	}

	public void setMeasure_rate(int measure_rate) {
		this.measure_rate = measure_rate;
	}

	public List<Float> getDemodPointList() {
		return demodPointList;
	}

	public void setDemodPointList(List<Float> demodPointList) {
		this.demodPointList = demodPointList;
	}

}
