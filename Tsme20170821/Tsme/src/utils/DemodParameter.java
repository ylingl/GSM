package utils;

import java.util.ArrayList;
import java.util.List;

import mvc.device.DemodResultParam;

public class DemodParameter {
	public enum demod_status_enum {
		start, transfer, stop, stopped;
	}
	
	public enum si_type_enum {
		first(1), seconed(2), third(3), fourth(4), fifth(5);
		
	    private int index;
	    
	    si_type_enum(int idx) {
	        this.index = idx;
	    }
	    
	    public int getIndex() {
	        return index;
	    }
	}
	
	public enum mode_type_enum {
		ONCE(0), //At the start of measurement，自动解调切只解调一次
		ON_CMD(1), //At the start of measurement，非自动解调
		REPETITION(2), //At the start of measurement，反复解调与interval_ms联合使用
		BTS(3), //During measurement
		BTS_FORCE(4), //During measurement
		BTS_RESET(5), //During measurement
		CHANNEL_RESET(6), //During measurement
		BTS_OLD(7), //During measurement
		BTS_OLD_FORCE(8); //During measurement
		
	    private int index;
	    
	    mode_type_enum(int idx) {
	        this.index = idx;
	    }
	    
	    public int getIndex() {
	        return index;
	    }
	}
	
	private boolean training = false;
	
	private boolean warning = false;
	
	private int overTime_m; //超时分钟数，当主监控超过这个时间不请求数据，就通知设备，不用上报数据了
	
	private String templateId;
	
	private String accountId;//用于标示主操作帐户
	
	private demod_status_enum status;
	
	private si_type_enum si_type;
	
	private mode_type_enum mode_type;//至关重要，关系到扫频仪的工作模式，和interval_ms联合使用
	
	private int interval_ms = 100;//下一次解调前的等待时间，默认值为100ms
	
	private int measure_rate = 1000;//数据刷新频率，多少次/1000s
	
	private long lastPopDataTime; //主监控客户端，最后一次获取数据的时间，如果超时了，则停止监控
	
	//序号,frequencyBand,频点,频点ID
	private List<DemodResultParam> demodResultParamList = new ArrayList<DemodResultParam>();

	public boolean isTraining() {
		return training;
	}

	public void setTraining(boolean training) {
		this.training = training;
	}

	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	public si_type_enum getSi_type() {
		return si_type;
	}

	public void setSi_type(si_type_enum si_type) {
		this.si_type = si_type;
	}

	public mode_type_enum getMode_type() {
		return mode_type;
	}

	public void setMode_type(mode_type_enum mode_type) {
		this.mode_type = mode_type;
	}

	public int getMeasure_rate() {
		return measure_rate;
	}

	public void setMeasure_rate(int measure_rate) {
		this.measure_rate = measure_rate;
	}

	public int getInterval_ms() {
		return interval_ms;
	}

	public void setInterval_ms(int interval_ms) {
		this.interval_ms = interval_ms;
	}

	public demod_status_enum getStatus() {
		return status;
	}

	public void setStatus(demod_status_enum status) {
		this.status = status;
	}

	public long getLastPopDataTime() {
		return lastPopDataTime;
	}

	public void setLastPopDataTime(long lastPopDataTime) {
		this.lastPopDataTime = lastPopDataTime;
	}

	//判断主监控客户端，请求数据是否超时，距离上次获取数据的时间超过1分钟，则判断为已经断开，则停止获取数据
	public boolean isMainClientOverTime(){
		long now = System.currentTimeMillis();
		if((now - getLastPopDataTime()) > getOverTime_m() * 60 * 1000){
			return true;
		}
		return false;
	}

	public int getOverTime_m() {
		return overTime_m;
	}

	public void setOverTime_m(int overTime_m) {
		this.overTime_m = overTime_m;
	}

	public List<DemodResultParam> getDemodResultParamList() {
		return demodResultParamList;
	}

	public void setDemodResultParamList(List<DemodResultParam> demodResultParamList) {
		this.demodResultParamList = demodResultParamList;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
}
