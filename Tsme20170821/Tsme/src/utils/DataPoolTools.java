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
 * 数据缓冲池
 * 
 * @author kitty
 */
public class DataPoolTools {
	
	public final static String root_path = "D:";
	
	//授权设备列表
	public static Set<String> authorizedDeviceNumSet = new HashSet<String>();
	
	//httpSessionId-SpetrumList
	public static Map<String, List<ArrayList<Double>>> httpSessionIdSpetrumMap = new HashMap<String, List<ArrayList<Double>>>();
	
	//httpSessionId-WarningDataList
	public static Map<String, List<WarningData>> httpSessionIdWarningDataMap = new HashMap<String, List<WarningData>>();
	
	//httpSessionId-centerFrequency，用于解除告警
	public static Map<String, Float> httpSessionIdCenterFrequencyMap = new HashMap<String, Float>();
	
	//httpSessionId-Index
	public static Map<String, Integer> httpSessionIdIndexMap = new HashMap<String, Integer>();
	
	//httpSessionId-WarningDataList
	public static Map<String, List<BeanCoordinate>> httpSessionIdWarningPointMap = new HashMap<String, List<BeanCoordinate>>();
	
	//httpSessionId-WarningHistoryList, 用于统计正式版需删除
	public static Map<String, List<WarningHistory>> httpSessionIdWarningHistoryMap = new HashMap<String, List<WarningHistory>>();
	
	//设备参数设置
	public static Map<String, MonitorParameter> deviceMonitorParameterMap = new HashMap<String, MonitorParameter>();
	
	//解调参数设置
	public static Map<String, DemodParameter> deviceDemodParameterMap = new HashMap<String, DemodParameter>();

	//deviceId-ReportStatus
	public static Map<String, ReportStatus> monitorReportStatusMap = new HashMap<String, ReportStatus>();
	
	//deviceId-ReportStatus
	public static Map<String, ReportStatus> offlineReportStatusMap = new HashMap<String, ReportStatus>();
		
	//客户端上传的实时监控数据缓冲池
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> monitorDataPool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();
	
	//客户端上传的实时监控数据缓冲池,用于写原始数据
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> monitorDataToFilePool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();

	//需要写文件的数据缓冲池
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> trainingDataPool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();
	
	//用于显示训练过程中的频谱数据,每组深度30组
	public static Map<String, HashMap<String, Queue<ArrayList<Double>>>> showDataPool = new HashMap<String, HashMap<String, Queue<ArrayList<Double>>>>();
	
	//用于从客户端显示
	public static Map<String, SpectraPrePara> spectraPreParaCopyMap = new HashMap<String, SpectraPrePara>();
	
	//deviceId-(frequencyBand-预警数据)SpectraData，每组仅存放一组统计数据，一组频谱一组统计结果
	public static Map<String, SpectraData> spectraDataMap = new HashMap<String, SpectraData>();
	
	//frequencyBand-预警数据队列
	public static Map<String, HashMap<String, ArrayList<WarningHistory>>> warningHistoryMapToFile = new HashMap<String, HashMap<String, ArrayList<WarningHistory>>>();
	
	//deviceId-frequencyBand-frequency-解调结果列
	public static Map<String, HashMap<String, HashMap<Float, Queue<String[]>>>> demodulateDataPool = new HashMap<String, HashMap<String, HashMap<Float, Queue<String[]>>>>();
	
	//deviceId-frequencyBand-frequency-解调结果列
	public static Map<String, HashMap<String, HashMap<Float, Queue<String>>>> demodulateDataToFilePool = new HashMap<String, HashMap<String, HashMap<Float, Queue<String>>>>();
	
	//deviceId-frequencyBand-frequency-解调结果列
	//public static Map<String, HashMap<String, HashMap<Float, Queue<String>>>> demodulateDataToMonitorFilePool = new HashMap<String, HashMap<String, HashMap<Float, Queue<String>>>>();
	
	//deviceId-frequencyBand-解调统计结果列，每个频点仅存放一组互不相同的统计数据
	public static Map<String, HashMap<String, ArrayList<DemodResultStatic>>> demodResultStaticMap = new HashMap<String, HashMap<String, ArrayList<DemodResultStatic>>>();
	
	//deviceId-frequencyBand-解调统计结果列，每个频点仅存放一组互不相同的统计数据
	public static Map<String, HashMap<String, ArrayList<DemodResultStatic>>> demodResultStaticMapToDB = new HashMap<String, HashMap<String, ArrayList<DemodResultStatic>>>();
	
	//清除超过一定时间没有获取数据的监控，当超时或者停止监控，并且缓冲值数据为空的时候才移除设备
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
