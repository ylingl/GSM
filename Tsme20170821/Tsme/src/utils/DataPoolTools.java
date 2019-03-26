package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import logic.data.bean.BeanCoordinate;
import mvc.device.DemodResultStatic;
import mvc.device.ReportStatus;
import mvc.history.WarningHistory;
import mvc.spectra.SpectraData;
import mvc.spectra.SpectraPrePara;
import mvc.spectra.WarningData;
import utils.DemodParameter.demod_status_enum;
import utils.MonitorParameter.monitor_status_enum;

/**
 * ���ݻ����
 * 
 * @author kitty
 */
public class DataPoolTools {
	
	public final static String root_path = "D:";
	
	//��Ȩ�豸�б�
	public static Set<String> authorizedDeviceNumSet = new HashSet<String>();
	
	//httpSessionId-SpetrumList
	public static Map<String, List<ArrayList<Double>>> httpSessionIdSpetrumMap = new HashMap<String, List<ArrayList<Double>>>();
	
	//httpSessionId-WarningDataList
	public static Map<String, List<WarningData>> httpSessionIdWarningDataMap = new HashMap<String, List<WarningData>>();
	
	//httpSessionId-centerFrequency�����ڽ���澯
	public static Map<String, Float> httpSessionIdCenterFrequencyMap = new HashMap<String, Float>();
	
	//httpSessionId-Index
	public static Map<String, Integer> httpSessionIdIndexMap = new HashMap<String, Integer>();
	
	//httpSessionId-WarningDataList
	public static Map<String, List<BeanCoordinate>> httpSessionIdWarningPointMap = new HashMap<String, List<BeanCoordinate>>();
	
	//httpSessionId-WarningHistoryList, ����ͳ����ʽ����ɾ��
	public static Map<String, List<WarningHistory>> httpSessionIdWarningHistoryMap = new HashMap<String, List<WarningHistory>>();
	
	//�豸��������
	public static Map<String, MonitorParameter> deviceMonitorParameterMap = new HashMap<String, MonitorParameter>();
	
	//�����������
	public static Map<String, DemodParameter> deviceDemodParameterMap = new HashMap<String, DemodParameter>();

	//deviceId-ReportStatus
	public static Map<String, ReportStatus> monitorReportStatusMap = new HashMap<String, ReportStatus>();
	
	//deviceId-ReportStatus
	public static Map<String, ReportStatus> offlineReportStatusMap = new HashMap<String, ReportStatus>();
		
	//�ͻ����ϴ���ʵʱ������ݻ����
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> monitorDataPool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();
	
	//�ͻ����ϴ���ʵʱ������ݻ����,����дԭʼ����
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> monitorDataToFilePool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();

	//��Ҫд�ļ������ݻ����
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> trainingDataPool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();
	
	//������ʾѵ�������е�Ƶ������,ÿ�����30��
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> showDataPool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();
	
	//���ڴӿͻ�����ʾ
	public static Map<String, SpectraPrePara> spectraPreParaCopyMap = new HashMap<String, SpectraPrePara>();
	
	//deviceId-(frequencyBand-Ԥ������)SpectraData��ÿ������һ��ͳ�����ݣ�һ��Ƶ��һ��ͳ�ƽ��
	public static Map<String, SpectraData> spectraDataMap = new HashMap<String, SpectraData>();
	
	//frequencyBand-Ԥ�����ݶ���
	public static Map<String, HashMap<String, ArrayList<WarningHistory>>> warningHistoryMapToFile = new HashMap<String, HashMap<String, ArrayList<WarningHistory>>>();
	
	//deviceId-frequencyBand-frequency-��������
	public static Map<String, HashMap<String, HashMap<Float, Queue<String[]>>>> demodulateDataPool = new HashMap<String, HashMap<String, HashMap<Float, Queue<String[]>>>>();
	
	//deviceId-frequencyBand-frequency-��������
	public static Map<String, HashMap<String, HashMap<Float, Queue<String>>>> demodulateDataToFilePool = new HashMap<String, HashMap<String, HashMap<Float, Queue<String>>>>();
	
	//deviceId-frequencyBand-frequency-��������
	//public static Map<String, HashMap<String, HashMap<Float, Queue<String>>>> demodulateDataToMonitorFilePool = new HashMap<String, HashMap<String, HashMap<Float, Queue<String>>>>();
	
	//deviceId-frequencyBand-���ͳ�ƽ���У�ÿ��Ƶ������һ�黥����ͬ��ͳ������
	public static Map<String, HashMap<String, ArrayList<DemodResultStatic>>> demodResultStaticMap = new HashMap<String, HashMap<String, ArrayList<DemodResultStatic>>>();
	
	//deviceId-frequencyBand-���ͳ�ƽ���У�ÿ��Ƶ������һ�黥����ͬ��ͳ������
	public static Map<String, HashMap<String, ArrayList<DemodResultStatic>>> demodResultStaticMapToDB = new HashMap<String, HashMap<String, ArrayList<DemodResultStatic>>>();
	
	//�������һ��ʱ��û�л�ȡ���ݵļ�أ�����ʱ����ֹͣ��أ����һ���ֵ����Ϊ�յ�ʱ����Ƴ��豸
	public static void clearOverTimeMonitor() {
		Collection<String> deviceNums = deviceMonitorParameterMap.keySet();
		for (String deviceNum : deviceNums) {
			if(deviceMonitorParameterMap.get(deviceNum).isMainClientOverTime() || 
				deviceMonitorParameterMap.get(deviceNum).getStatus().equals(monitor_status_enum.stopped)){
				
				/*if(!monitorDataToFilePool.containsKey(deviceNum) 
					|| (monitorDataToFilePool.containsKey(deviceNum) && monitorDataToFilePool.get(deviceNum).size() == 0)) {
					
					deviceMonitorParameterMap.remove(deviceNum);
					monitorDataPool.remove(deviceNum);
					monitorDataToFilePool.remove(deviceNum);
				}
				
				if(!trainingDataPool.containsKey(deviceNum) 
					|| (trainingDataPool.containsKey(deviceNum) && trainingDataPool.get(deviceNum).size() == 0)) {
						
					trainingDataPool.remove(deviceNum);
				}*/
				
			}
			
			if(deviceDemodParameterMap.containsKey(deviceNum) && ( 
				deviceDemodParameterMap.get(deviceNum).isMainClientOverTime() || 
				deviceDemodParameterMap.get(deviceNum).getStatus().equals(demod_status_enum.stopped) )){
				
				/*if(!demodulateDataToFilePool.containsKey(deviceNum) 
					|| (demodulateDataToFilePool.containsKey(deviceNum) && demodulateDataToFilePool.get(deviceNum).size() == 0)) {
					
					deviceDemodParameterMap.remove(deviceNum);
					demodulateDataPool.remove(deviceNum);
					demodulateDataToFilePool.remove(deviceNum);
				}*/
			}
		}
	}
	
}
