package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.data.bean.BeanCoordinate;

public class MonitorParameter {
	public enum monitor_status_enum {
		start, transfer, stop, stopped
	}
	
	//public enum purpose_enum {
	//	train, monitor
	//}
	
	private boolean training = false;
	
	private boolean warning = false;

	//private purpose_enum purpose;//用途

	private String templateId; //模板id，标示当前模板，用于支持多客户端访问
	
	//private int originalDataFileOrdno = 0; //模板文件序号
	
	private int overTime_m; //超时分钟数，当主监控超过这个时间不请求数据，就通知设备，不用上报数据了
	
	//private String deviceAddress;
	
	private String accountId;//用于标示主操作帐户

	private monitor_status_enum status;

	private int interval = 1; //刷新频率，毫秒数，默认值为1s

	private List<Float> frequencyList = new ArrayList<Float>();//奇数为起始频率，偶数为终止频率

	private int maxMeans;// 最大监测频率

	private int bandWidth;// 带宽

	private int fftSize;// ftt 步长
	
	private Map<String, String> frequencyBandMap = new HashMap<String, String>();//前频段，后ID
	
	private Map<String, List<List<Float>>> warningLineMap = new HashMap<String, List<List<Float>>>();
	
	private Map<String, List<BeanCoordinate>> warningPointMap = new HashMap<String, List<BeanCoordinate>>();
	
	private long lastPopDataTime; //主监控客户端，最后一次获取数据的时间，如果超时了，则停止监控
	
	//private Map<String, ArrayList<WarningData>> warnDataMap = new HashMap<String, ArrayList<WarningData>>();

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		if(interval > 0)
			this.interval = interval;
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
		if((now - getLastPopDataTime()) > getOverTime_m() * 60 *1000){
			return true;
		}
		return false;
	}

	public int getMaxMeans() {
		return maxMeans;
	}

	public void setMaxMeans(int maxMeans) {
		this.maxMeans = maxMeans;
	}

	public int getFftSize() {
		return fftSize;
	}

	public void setFftSize(int fftSize) {
		this.fftSize = fftSize;
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public List<Float> getFrequencyList() {
		return frequencyList;
	}

	public void setFrequencyList(List<Float> frequencyList) {
		this.frequencyList = frequencyList;
	}

	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		this.bandWidth = bandWidth;
	}

	/*public Map<String, ArrayList<WarningData>> getWarnDataMap() {
		return warnDataMap;
	}

	public void setWarnDataMap(Map<String, ArrayList<WarningData>> warnDataMap) {
		this.warnDataMap = warnDataMap;
	}*/

	public Map<String, List<List<Float>>> getWarningLineMap() {
		return warningLineMap;
	}

	public void setWarningLineMap(Map<String, List<List<Float>>> warningLineMap) {
		this.warningLineMap = warningLineMap;
	}

	public Map<String, List<BeanCoordinate>> getWarningPointMap() {
		return warningPointMap;
	}

	public void setWarningPointMap(Map<String, List<BeanCoordinate>> warningPointMap) {
		this.warningPointMap = warningPointMap;
	}

	public Map<String, String> getFrequencyBandMap() {
		return frequencyBandMap;
	}

	public void setFrequencyBandMap(Map<String, String> frequencyBandMap) {
		this.frequencyBandMap = frequencyBandMap;
	}

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

	public monitor_status_enum getStatus() {
		return status;
	}

	public void setStatus(monitor_status_enum status) {
		this.status = status;
	}

	public int getOverTime_m() {
		return overTime_m;
	}

	public void setOverTime_m(int overTime_m) {
		this.overTime_m = overTime_m;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
