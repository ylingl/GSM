package mvc.spectra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import logic.data.DataAnalysisService;
import logic.data.bean.BeanCoordinate;
import logic.spectra.SpectraService;
import mvc.data.BaseAndPeakRow;
import mvc.device.DemodResultParam;
import mvc.device.DemodResultStatic;
import net.sf.json.JSONObject;
import tsme.table.deviceFB.bean.DEVICEFB;
import tsme.table.deviceWT.bean.DEVICEWT;
import tsme.table.frequencyBand.bean.FREQUENCYBAND;
import tsme.table.warningTemplate.DAO.WarningTemplateDAO;
import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;
import utils.AccountTools;
import utils.DataPoolTools;
import utils.DemodParameter;
import utils.DemodParameter.demod_status_enum;
import utils.DemodParameter.mode_type_enum;
import utils.DemodParameter.si_type_enum;
import utils.MonitorParameter;
import utils.MonitorParameter.monitor_status_enum;
import utils.ResponseTools;
import utils.task.DemodulateDataCreate;
import utils.task.TrainingDataFile;
import utils.task.WaringDataCreate;
import utils.task.WaringDataFile;

@Controller
public class SpectraController {
	
	@Autowired
	@Qualifier("dataAnalysisService")
	DataAnalysisService dataAnalysisService;
	
	@Autowired
	@Qualifier("spectraService")
	SpectraService spectraService;
	
	@Autowired
	@Qualifier("warningTemplateDAO")
	private WarningTemplateDAO warningTemplateDAO;
	
	
	/**
	 * 客户端使用地图上点击：获取模板列表。
	 * @param monitorParameter
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getWarningTemplateList")
	public void getWarningTemplateList(String deviceNum, HttpServletResponse response){
		JSONObject jsObj = new JSONObject();
		
		DataPoolTools.clearOverTimeMonitor();//清除超时的监控
		
		if(DataPoolTools.monitorReportStatusMap.containsKey(deviceNum)){
			if(!DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum)){
				List<WARNINGTEMPLATE> warningTemplateList = dataAnalysisService.getWarningTemplateIdAndNameListByDeviceNum(deviceNum);
				jsObj.put("warningTemplateList", warningTemplateList);
				jsObj.put("employ", false);
			} else {
				if(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).isWarning()){
					jsObj.put("employ", true);
				}
				else{
					jsObj.put("offline", true);
				}
			}
			jsObj.put("offline", false);
		} else {
			jsObj.put("offline", true);
		}
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	
	/**
	 * 客户端使用地图上点击：进入监控页面，此方法只需要记录下来这个templateId和设备id即可。
	 * @param monitorParameter
	 * @return
	 */
	@ResponseBody
	@RequestMapping("chart")
	public ModelAndView chart(String deviceNum, String templateId) {
		ModelAndView mav = new ModelAndView();
		
		if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum)){	
			mav.addObject("employ", 1);
		} else {
			mav.addObject("employ", 0);
		}
		
		mav.addObject("deviceNum", deviceNum);
		
		if(templateId == null && DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum)){
			//此处为copy显示情况下的处理方法
			templateId = DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getTemplateId();
		}
		
		mav.addObject("templateId", templateId);
		
		mav.setViewName("chart");
		return mav;
	}
	
	
	/**
	 * 
	 * @param deviceNum
	 * @param templateId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("startRealTimeMonitor")
	public SpectraPrePara startRealTimeMonitor(String deviceNum, String templateId, HttpServletRequest request){
		WARNINGTEMPLATE warningTemplate = warningTemplateDAO.findWarningTemplateWithFrequencyBandListByIdAndSortByStartFrequency(templateId);
		AccountTools accountTools = new AccountTools();
		
		//若设备为处于失去连接、监测中或解调中状态，则不可重复开启
		if(!DataPoolTools.monitorReportStatusMap.containsKey(deviceNum)
			|| DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum)
			|| DataPoolTools.deviceDemodParameterMap.containsKey(deviceNum)){	
			
			SpectraPrePara spectraPrePara = new SpectraPrePara();
			
			if(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId() == null
				|| DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId().equals(accountTools.getCurrentAccountId())){
				spectraPrePara.setMyself(true);
			} else {
				spectraPrePara.setMyself(false);
			}
			
			return spectraPrePara;
		}
		
		MonitorParameter monitorParameter = new MonitorParameter();
		SpectraPrePara spectraPrePara = new SpectraPrePara();
		DemodParameter demodParameter = new DemodParameter();
		
		demodParameter.setTemplateId(templateId);
		demodParameter.setInterval_ms(200);//解调间隔为200ms
		demodParameter.setMeasure_rate(1000);//每1000秒上传1000组数据
		demodParameter.setMode_type(mode_type_enum.REPETITION);//设置为重复解调模式
		demodParameter.setOverTime_m(2);//2分钟无反应提示设备丢失
		demodParameter.setSi_type(si_type_enum.third);
		demodParameter.setStatus(demod_status_enum.start);
		demodParameter.setTraining(false);
		demodParameter.setWarning(true);
		demodParameter.setLastPopDataTime(System.currentTimeMillis());
		
		spectraPrePara.setTemplateName(warningTemplate.getTemplate_name());
		
		monitorParameter.setOverTime_m(10);
		monitorParameter.setStatus(monitor_status_enum.stop);
		monitorParameter.setLastPopDataTime(System.currentTimeMillis());
		monitorParameter.setTemplateId(templateId);
		monitorParameter.setTraining(false);
		monitorParameter.setWarning(true);
		
		List<Float> frequencyList = new ArrayList<Float>();
		for(FREQUENCYBAND frequencyBand : warningTemplate.getFrequencyBandList()){
			frequencyList.add(frequencyBand.getStartFrequency());
			frequencyList.add(frequencyBand.getStopFrequency());
		}
		
		monitorParameter.setFrequencyList(frequencyList);
		spectraPrePara.setFrequencyList(frequencyList);
		
		Map<String, List<List<Float>>> warningLineMap = new HashMap<String, List<List<Float>>>();
		Map<String, List<BeanCoordinate>> warningPointMap = new HashMap<String, List<BeanCoordinate>>();
		dataAnalysisService.findWarningLineDataByWarningTemplateId(templateId, warningPointMap, warningLineMap);
		
		monitorParameter.setWarningPointMap(warningPointMap);
		monitorParameter.setWarningLineMap(warningLineMap);
		spectraPrePara.setWarningLineMap(warningLineMap);
		
		//获取基线与峰线值
		BaseAndPeakRow bpRow = dataAnalysisService.getBaseAndPeakRow(templateId);
		//获取极值点,frequencyBand-点列
		Map<String, List<List<Float>>> demodulationPointMap = dataAnalysisService.getDemodulationPointForChart(templateId, bpRow.getBaseRow());
		spectraPrePara.setDemodulationPointMap(demodulationPointMap);
		
		monitorParameter.setFftSize(warningTemplate.getFftSize());
		spectraPrePara.setFftSize(warningTemplate.getFftSize());
		monitorParameter.setBandWidth(warningTemplate.getBandWidth());
		spectraPrePara.setBandWidth(warningTemplate.getBandWidth());
		monitorParameter.setMaxMeans(5000);
		spectraPrePara.setMaxMeans(5000);
		
		spectraPrePara.setLongitude(DataPoolTools.monitorReportStatusMap.get(deviceNum).getLongitude());
		spectraPrePara.setLatitude(DataPoolTools.monitorReportStatusMap.get(deviceNum).getLatitude());
		spectraPrePara.setMyself(true);
		
		DataPoolTools.spectraPreParaCopyMap.put(deviceNum, spectraPrePara);
		
		
		/*******************存储预警模板*******************/
		DEVICEWT deviceWT = spectraService.copyWarningTemplate(warningTemplate, warningPointMap, demodulationPointMap);
		
		Map<String, String> frequencyBandMap = new HashMap<String, String>();
		for(DEVICEFB temp : deviceWT.getDeviceFBList()){
			frequencyBandMap.put(temp.getStartFrequency() + "-" + temp.getStopFrequency(), temp.getId());
		}
		monitorParameter.setFrequencyBandMap(frequencyBandMap);
		
		//获取解调结果的处理参数
		List<DemodResultParam> demodResultParamList = spectraService.getDemodResultParam(deviceWT.getId());
		demodParameter.setDemodResultParamList(demodResultParamList);
		
		
		/*******************启动设备监控*******************/
		monitorParameter.setAccountId(accountTools.getCurrentAccountId());
		monitorParameter.setStatus(monitor_status_enum.start);
		monitorParameter.setOverTime_m(5);
		
		demodParameter.setAccountId(accountTools.getCurrentAccountId());;
		
		DataPoolTools.deviceMonitorParameterMap.put(deviceNum, monitorParameter);
		
		//设置监控数据池
		HashMap<String, Queue<ArrayList<Double>>> monitorDataMap = new HashMap<String, Queue<ArrayList<Double>>>();
		for(String key : monitorParameter.getWarningLineMap().keySet()){
			monitorDataMap.put(key, new LinkedList<ArrayList<Double>>());
		}
		
		DataPoolTools.monitorDataPool.put(deviceNum, monitorDataMap);
		
		Thread thread1 = new Thread(new WaringDataCreate(deviceNum), deviceNum);
		thread1.start();
		
		//设置监控数据池,用于存储原始数据
		HashMap<String, Queue<ArrayList<Double>>> monitorDataMapToFile = new HashMap<String, Queue<ArrayList<Double>>>();
		for(String key : monitorParameter.getWarningLineMap().keySet()){
			monitorDataMapToFile.put(key, new LinkedList<ArrayList<Double>>());
		}
		
		DataPoolTools.monitorDataToFilePool.put(deviceNum, monitorDataMapToFile);
		
		Thread thread2 = new Thread(new WaringDataFile(spectraService, accountTools.getCurrentAccountId(), deviceNum), deviceNum);
		thread2.start();
		
		//设置解调
		DataPoolTools.deviceDemodParameterMap.put(deviceNum, demodParameter);
		
		//frequencyBand-频点-解调结果列，用于显示
		HashMap<String, HashMap<Float, Queue<String[]>>> demodulatingDataMap = new HashMap<String, HashMap<Float, Queue<String[]>>>();
		//frequencyBand-频点-解调结果列，用于存储
		HashMap<String, HashMap<Float, Queue<String>>> demodulatingDataMapToFile = new HashMap<String, HashMap<Float, Queue<String>>>();
		
		//frequencyBand-解调结果统计列，用于统计
		HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMap = new HashMap<String, ArrayList<DemodResultStatic>>();
		//frequencyBand-解调结果统计列，用于统计
		HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMapToDB = new HashMap<String, ArrayList<DemodResultStatic>>();
		for(DemodResultParam demodResultParam : demodResultParamList) {
			//频点-解调结果
			HashMap<Float, Queue<String[]>> demodResultMap = new HashMap<Float, Queue<String[]>>();
			//频点-解调结果
			HashMap<Float, Queue<String>> demodResultMapToFile = new HashMap<Float, Queue<String>>();
			
			ArrayList<DemodResultStatic> demodResultStaticList = new ArrayList<DemodResultStatic>();
			ArrayList<DemodResultStatic> demodResultStaticListToDB = new ArrayList<DemodResultStatic>();
			
			demodResultMap.put(demodResultParam.getX(), new LinkedList<String[]>());
			demodResultMapToFile.put(demodResultParam.getX(), new LinkedList<String>());
			
			if(!demodulatingDataMap.containsKey(demodResultParam.getFrequencyBand())){
				demodulatingDataMap.put(demodResultParam.getFrequencyBand(), demodResultMap);
			} else {
				demodulatingDataMap.get(demodResultParam.getFrequencyBand()).put(demodResultParam.getX(), new LinkedList<String[]>());
			}
			
			if(!demodulatingDataMapToFile.containsKey(demodResultParam.getFrequencyBand())){
				demodulatingDataMapToFile.put(demodResultParam.getFrequencyBand(), demodResultMapToFile);
			} else {
				demodulatingDataMapToFile.get(demodResultParam.getFrequencyBand()).put(demodResultParam.getX(), new LinkedList<String>());
			}
			
			if(!demodResultStaticMap.containsKey(demodResultParam.getFrequencyBand())){
				demodResultStaticMap.put(demodResultParam.getFrequencyBand(), demodResultStaticList);
			}
			
			if(!demodResultStaticMapToDB.containsKey(demodResultParam.getFrequencyBand())){
				demodResultStaticMapToDB.put(demodResultParam.getFrequencyBand(), demodResultStaticListToDB);
			}
		}
		
		DataPoolTools.demodulateDataPool.put(deviceNum, demodulatingDataMap);
		DataPoolTools.demodulateDataToFilePool.put(deviceNum, demodulatingDataMapToFile);
		DataPoolTools.demodResultStaticMap.put(deviceNum, demodResultStaticMap);
		DataPoolTools.demodResultStaticMapToDB.put(deviceNum, demodResultStaticMapToDB);
		
		Thread thread3 = new Thread(new DemodulateDataCreate(dataAnalysisService, deviceNum), deviceNum);
		thread3.start();
		
		return spectraPrePara;
	}
	
	@RequestMapping("stopRealTimeMonitor")
	public void stopRealTimeMonitor(String deviceNum, HttpServletResponse response){
		DataPoolTools.clearOverTimeMonitor();//清除超时的监控
		AccountTools accountTools = new AccountTools();
		
		JSONObject jsObj = new JSONObject();
		
		if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum) && 
			DataPoolTools.deviceDemodParameterMap.containsKey(deviceNum)){
			
			//MonitorParameter mp = DataPoolTools.deviceMonitorParameterMap.get(deviceNum);
			//DemodParameter dp = DataPoolTools.deviceDemodParameterMap.get(deviceNum);
			if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum) &&
				(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId().equals(accountTools.getCurrentAccountId())
				|| accountTools.doesCurrentAccountHasAdminRole() 
				|| accountTools.doesCurrentAccountHasSuperadminRole())) {
				
				DataPoolTools.deviceMonitorParameterMap.get(deviceNum).setWarning(false);
				DataPoolTools.deviceMonitorParameterMap.get(deviceNum).setStatus(monitor_status_enum.stop);//只有主监控端 client,才能发送停止监控的请求
				DataPoolTools.deviceDemodParameterMap.get(deviceNum).setWarning(false);
				DataPoolTools.deviceDemodParameterMap.get(deviceNum).setStatus(demod_status_enum.stop);
				System.err.println(deviceNum + " == 停止监测数据======");
				
				jsObj.put("result", true);
			} else {
				jsObj.put("result", false);
			}
		} else {
			jsObj.put("result", false);
		}
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	
	@ResponseBody
	@RequestMapping("getSpectraPreParaCopy")
	public SpectraPrePara getSpectraPreParaCopy(String deviceNum, HttpServletRequest request){
		SpectraPrePara spectraPrePara = DataPoolTools.spectraPreParaCopyMap.get(deviceNum);
		AccountTools accountTools = new AccountTools();
		
		if(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId() == null
			|| DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId().equals(accountTools.getCurrentAccountId())){
			spectraPrePara.setMyself(true);
		} else {
			spectraPrePara.setMyself(false);
		}
		
		return spectraPrePara;
	}
	
	/**
	 * 获取数据文件
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("getJsFileList")
	public ArrayList<String> getJsFileList(HttpServletResponse response){
		return TrainingDataFile.getDataFileList();
	}
	
	@ResponseBody
	@RequestMapping("showWarningDetails")
	public List<String> showWarningDetails(String deviceNum, Float startFrequency, Float stopFrequency, Float centerFrequency){
		if(DataPoolTools.spectraDataMap.containsKey(deviceNum)) {
			Map<String, ArrayList<WarningData>> warnDataMap = DataPoolTools.spectraDataMap.get(deviceNum).getWarnDataMap();
			String frequencyBand = startFrequency.toString() + "-" + stopFrequency.toString();
			if(warnDataMap.containsKey(frequencyBand)){
				ArrayList<WarningData> warningDataList = warnDataMap.get(frequencyBand);
				for(WarningData warningData : warningDataList){
					if(warningData.getCenterFrequency() == centerFrequency) {
						List<String> tempList = new ArrayList<String>(warningData.getWarningTimeList());
						Collections.reverse(tempList);
						return tempList;
					}
				}
			}
		}
			
		return null;
	}
	
	@ResponseBody
	@RequestMapping("releaseWarning")
	public boolean releaseWarning(String deviceNum, Float startFrequency, Float stopFrequency, Float centerFrequency){
		if(DataPoolTools.spectraDataMap.containsKey(deviceNum)) {
			Map<String, ArrayList<WarningData>> warnDataMap = DataPoolTools.spectraDataMap.get(deviceNum).getWarnDataMap();
			String frequencyBand = startFrequency.toString() + "-" + stopFrequency.toString();
			if(warnDataMap.containsKey(frequencyBand)){
				ArrayList<WarningData> warningDataList = warnDataMap.get(frequencyBand);
				for(int i = 0; i < warningDataList.size(); i ++){
					if(warningDataList.get(i).getCenterFrequency() == centerFrequency) {
						warningDataList.get(i).setVisible(false);
						return true;
					}
				}
			}
		}
			
		return false;
	}
}
