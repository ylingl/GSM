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

	//private purpose_enum purpose;//��;

	private String templateId; //ģ��id����ʾ��ǰģ�壬����֧�ֶ�ͻ��˷���
	
	//private int originalDataFileOrdno = 0; //ģ���ļ����
	
	private int overTime_m; //��ʱ��������������س������ʱ�䲻�������ݣ���֪ͨ�豸�������ϱ�������
	
	//private String deviceAddress;
	
	private String accountId;//���ڱ�ʾ�������ʻ�

	private monitor_status_enum status;

	private int interval = 1; //ˢ��Ƶ�ʣ���������Ĭ��ֵΪ1s

	private List<Float> frequencyList = new ArrayList<Float>();//����Ϊ��ʼƵ�ʣ�ż��Ϊ��ֹƵ��

	private int maxMeans;// �����Ƶ��

	private int bandWidth;// ����

	private int fftSize;// ftt ����
	
	private Map<String, String> frequencyBandMap = new HashMap<String, String>();//ǰƵ�Σ���ID
	
	private Map<String, List<List<Float>>> warningLineMap = new HashMap<String, List<List<Float>>>();
	
	private Map<String, List<BeanCoordinate>> warningPointMap = new HashMap<String, List<BeanCoordinate>>();
	
	private long lastPopDataTime; //����ؿͻ��ˣ����һ�λ�ȡ���ݵ�ʱ�䣬�����ʱ�ˣ���ֹͣ���
	
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
	
	//�ж�����ؿͻ��ˣ����������Ƿ�ʱ�������ϴλ�ȡ���ݵ�ʱ�䳬��1���ӣ����ж�Ϊ�Ѿ��Ͽ�����ֹͣ��ȡ����
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
