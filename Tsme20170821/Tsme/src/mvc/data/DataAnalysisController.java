package mvc.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import logic.data.DataAnalysisService;
import mvc.device.DemodResultParam;
import mvc.device.DemodResultStatic;
import net.sf.json.JSONObject;
import tsme.table.frequencyBand.bean.FREQUENCYBAND;
import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;
import utils.AccountTools;
import utils.ClearHttpSessionAttribute;
import utils.DataPoolTools;
import utils.DemodParameter;
import utils.DemodParameter.demod_status_enum;
import utils.DemodParameter.mode_type_enum;
import utils.DemodParameter.si_type_enum;
import utils.MonitorParameter;
import utils.MonitorParameter.monitor_status_enum;
import utils.ResponseTools;
import utils.task.DemodulateDataCreate;
import utils.task.DemodulateDataFile;
import utils.task.TrainingDataFile;

@Controller
public class DataAnalysisController {
	
	@Autowired
	@Qualifier("dataAnalysisService")
	private DataAnalysisService dataAnalysisService;
	
	
	//此链接应放于地图中的设备弹窗上(加一个“训练预警模型”链接)，deviceId为device表的ID，WarningTemplate的父级表是device，此方法仅用于跳转页面
	@RequestMapping("warningTemplate/{deviceNum}")
	public ModelAndView template(@PathVariable("deviceNum") String deviceNum, HttpSession httpSession, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		new ClearHttpSessionAttribute(httpSession);
		httpSession.setAttribute("currentDeviceNum", deviceNum);
		
		mav.setViewName("warningTemplate");
		return mav;
	}
	
	//点击开始按钮后的响应方法，创建WarningTemplate数据，创建数据存储文件，并开始记录数据。warningTemplateRecPara来自于点击开始后弹出的设备参数填写框
	//返回参数显示在左上角区域
	@RequestMapping("startRecord")
	public void startRecord(WarningTemplateRecPara warningTemplateRecPara, HttpSession httpSession, HttpServletResponse response){
		String deviceNum = (String) httpSession.getAttribute("currentDeviceNum");
		AccountTools accountTools = new AccountTools();
		
		JSONObject jsObj = new JSONObject();
		
		//仅能进行唯一的训练，不允许同时开启多个训练
		if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum)){
			
			jsObj.put("result", false);
			jsObj.put("type", 1);
			
			ResponseTools.writeResponse(response, jsObj.toString());
			
			return;
		}
		
		//检测设备状态
		if(!DataPoolTools.monitorReportStatusMap.containsKey(deviceNum)){
			
			jsObj.put("result", false);
			jsObj.put("type", 2);
			
			ResponseTools.writeResponse(response, jsObj.toString());
			
			return;
			
		}
		
		WARNINGTEMPLATE warningTemplate = dataAnalysisService.createWarningTemplate(warningTemplateRecPara, httpSession);
		//尚缺创建文件与originalData保存操作，originalData表改一下，只存路径。开始后原始数据每隔20组数据显示一组，共显示100组
		
		//下面的方法类似 SpectraController.realTimeMonitorStart
		//转换为数据监测对象（保持和因为c++客户端请求的一样，方便调试）
		MonitorParameter monitorParameter = new MonitorParameter();
		monitorParameter.setAccountId(accountTools.getCurrentAccountId());
		monitorParameter.setBandWidth(warningTemplate.getBandWidth());
		
		HashMap<String, Queue<ArrayList<Double>>> trainingDataMap = new HashMap<String, Queue<ArrayList<Double>>>();
		HashMap<String, Queue<ArrayList<Double>>> showDataMap = new HashMap<String, Queue<ArrayList<Double>>>();
		
		List<Float> frequencyList = new ArrayList<Float>();
		if(warningTemplateRecPara.getStopFrequency0() > 0) {
			frequencyList.add(warningTemplateRecPara.getStartFrequency0());
			frequencyList.add(warningTemplateRecPara.getStopFrequency0());
			trainingDataMap.put(warningTemplateRecPara.getStartFrequency0() + "-" + warningTemplateRecPara.getStopFrequency0(), new LinkedList<ArrayList<Double>>());
			showDataMap.put(warningTemplateRecPara.getStartFrequency0() + "-" + warningTemplateRecPara.getStopFrequency0(), new LinkedList<ArrayList<Double>>());
		}
		if(warningTemplateRecPara.getStopFrequency1() > 0) {
			frequencyList.add(warningTemplateRecPara.getStartFrequency1());
			frequencyList.add(warningTemplateRecPara.getStopFrequency1());
			trainingDataMap.put(warningTemplateRecPara.getStartFrequency1() + "-" + warningTemplateRecPara.getStopFrequency1(), new LinkedList<ArrayList<Double>>());
			showDataMap.put(warningTemplateRecPara.getStartFrequency1() + "-" + warningTemplateRecPara.getStopFrequency1(), new LinkedList<ArrayList<Double>>());
		}
		if(warningTemplateRecPara.getStopFrequency2() > 0) {
			frequencyList.add(warningTemplateRecPara.getStartFrequency2());
			frequencyList.add(warningTemplateRecPara.getStopFrequency2());
			trainingDataMap.put(warningTemplateRecPara.getStartFrequency2() + "-" + warningTemplateRecPara.getStopFrequency2(), new LinkedList<ArrayList<Double>>());
			showDataMap.put(warningTemplateRecPara.getStartFrequency2() + "-" + warningTemplateRecPara.getStopFrequency2(), new LinkedList<ArrayList<Double>>());
		}
		monitorParameter.setFrequencyList(frequencyList);
		
		Map<String, String> frequencyBandMap = new HashMap<String, String>();
		for(FREQUENCYBAND temp : warningTemplate.getFrequencyBandList()){
			frequencyBandMap.put(temp.getStartFrequency() + "-" + temp.getStopFrequency(), temp.getId());
		}
		monitorParameter.setFrequencyBandMap(frequencyBandMap);
		
		monitorParameter.setFftSize(warningTemplate.getFftSize());
		monitorParameter.setMaxMeans(warningTemplate.getMaxMeans());
		monitorParameter.setTemplateId(warningTemplate.getId());
		monitorParameter.setOverTime_m(2);//默认2分钟超时
		monitorParameter.setStatus(monitor_status_enum.start);
		monitorParameter.setTraining(true);
		monitorParameter.setWarning(false);
		
		System.err.println(deviceNum + " == 开始监测数据"+deviceNum);
		
		monitorParameter.setLastPopDataTime(System.currentTimeMillis());
		DataPoolTools.deviceMonitorParameterMap.put(deviceNum, monitorParameter);
		DataPoolTools.trainingDataPool.put(deviceNum, trainingDataMap);
		DataPoolTools.showDataPool.put(deviceNum, showDataMap);
		
		Thread thread = new Thread(new TrainingDataFile(dataAnalysisService, deviceNum), deviceNum);
		thread.start();
		
		//用于回显select
		jsObj.put("template", warningTemplate);
		jsObj.put("result", true);
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}

	/**
	 * @param httpSession
	 * @param response
	 * 如果httpSession中currentWarningTemplateId为空，则页面不可点击该按钮。
	 * 此方法只设置training的状态，由文件保存线程处理数据停止。
	 */
	@RequestMapping("stopRecord")
	public void stopRecord(String deviceNum, HttpServletResponse response){
		AccountTools accountTools = new AccountTools();
		
		DataPoolTools.clearOverTimeMonitor();//触发清除其它超时的监控
		
		JSONObject jsObj = new JSONObject();
		
		if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum) && 
			(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId().equals(accountTools.getCurrentAccountId())
			|| accountTools.doesCurrentAccountHasAdminRole() 
			|| accountTools.doesCurrentAccountHasSuperadminRole())){//只有主监控端 client,才能发送停止监控的请求
			//下面的方法类似 SpectraController.realTimeMonitorStop
			DataPoolTools.deviceMonitorParameterMap.get(deviceNum).setTraining(false);
			DataPoolTools.deviceMonitorParameterMap.get(deviceNum).setStatus(monitor_status_enum.stop);
			
			if(DataPoolTools.trainingDataPool.containsKey(deviceNum)){
				jsObj.put("writable", true);
			} else {
				jsObj.put("writable", false);
			}
			
			jsObj.put("result", true);
		} else {
			jsObj.put("result", false);
		}
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	//如果httpSession中currentWarningTemplateId为空，则页面不可点击该按钮
	@RequestMapping("deleteWarningTemplate/{warningTemplateId}")
	public void deleteWarningTemplate(@PathVariable("warningTemplateId") String warningTemplateId, HttpServletResponse response){
		if(dataAnalysisService.deleteWarningTemplate(warningTemplateId)){
			ResponseTools.writeResponse(response, "1");
		} else{
			ResponseTools.writeResponse(response, "0");
		}
	}
	
	//训练预警模型按钮响应事件，如果httpSession中currentWarningTemplateId为空，则页面不可点击该按钮，file关闭才可点击
	@RequestMapping("trainingData/{warningTemplateId}")
	public void trainingData(@PathVariable("warningTemplateId") String warningTemplateId, HttpServletResponse response){
		int rows = -1;
		if(warningTemplateId != null && dataAnalysisService.canWarningTemplateBeModify(warningTemplateId)){
			rows = dataAnalysisService.trainingData(warningTemplateId);
		}
		
		ResponseTools.writeResponse(response, String.valueOf(rows));
	}
	
	//生成预警曲线按钮响应事件，如果httpSession中currentWarningTemplateId为空，则页面不可点击该按钮，有均值曲线后才可点击
	//此处的模板id，需要从页面的下拉框中获取
	@ResponseBody
	@RequestMapping("createWarningLine/{warningTemplateId}/{baseRowNum}/{peakRowNum}")
	public Map<String, List<List<Float>>> createWarningLine(@PathVariable("warningTemplateId") String warningTemplateId, @PathVariable("baseRowNum") int baseRowNum, @PathVariable("peakRowNum") int peakRowNum){
		if(dataAnalysisService.canWarningTemplateBeModify(warningTemplateId)){
			Map<String, List<List<Float>>> warningLine = new HashMap <String, List<List<Float>>>();
			
			dataAnalysisService.createWaringLine(baseRowNum, peakRowNum, warningTemplateId);
			dataAnalysisService.findWarningLineDataByWarningTemplateId(warningTemplateId, null, warningLine);
			
			return warningLine;
		}

		return null;
	}
	
	@ResponseBody
	@RequestMapping("getWarningTemplatesWithStatus")
	public WarningTemplatesWithStatus getWarningTemplatesWithStatus(HttpSession httpSession, HttpServletResponse response){	
		String deviceNum = (String) httpSession.getAttribute("currentDeviceNum");
		AccountTools accountTools = new AccountTools();
		
		WarningTemplatesWithStatus warningTemplatesWithStatus = new WarningTemplatesWithStatus();
		
		List<WARNINGTEMPLATE> warningTemplateList = dataAnalysisService.getWarningTemplateWithFrequencyBandListByDeviceNum(deviceNum);
		warningTemplatesWithStatus.setWarningTemplateList(warningTemplateList);
		
		if(DataPoolTools.demodulateDataPool.containsKey(deviceNum) && 
			DataPoolTools.deviceDemodParameterMap.get(deviceNum).isTraining()){
			warningTemplatesWithStatus.setDemod(true);
			
			String currentWarningTemplateId = DataPoolTools.deviceDemodParameterMap.get(deviceNum).getTemplateId();
			for(WARNINGTEMPLATE warningTemplate : warningTemplateList){
				if(warningTemplate.getId().equals(currentWarningTemplateId)){
					warningTemplatesWithStatus.setCurrentwarningTemplate(warningTemplate);
				}
			}
			
			if(DataPoolTools.deviceDemodParameterMap.get(deviceNum).getAccountId().equalsIgnoreCase(accountTools.getCurrentAccountId())){
				warningTemplatesWithStatus.setMyself(true);
			} else {
				warningTemplatesWithStatus.setMyself(false);
			}
		} else {
			warningTemplatesWithStatus.setDemod(false);
			warningTemplatesWithStatus.setMyself(true);
		}
		
		if(DataPoolTools.trainingDataPool.containsKey(deviceNum)){		
			warningTemplatesWithStatus.setEmploy(true);
			
			String currentWarningTemplateId = DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getTemplateId();
			for(WARNINGTEMPLATE warningTemplate : warningTemplateList){
				if(warningTemplate.getId().equals(currentWarningTemplateId)){
					warningTemplatesWithStatus.setCurrentwarningTemplate(warningTemplate);
				}
			}
			
			if(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId().equalsIgnoreCase(accountTools.getCurrentAccountId())){
				warningTemplatesWithStatus.setMyself(true);
			} else {
				warningTemplatesWithStatus.setMyself(false);
			}
		} else {
			warningTemplatesWithStatus.setEmploy(false);
			warningTemplatesWithStatus.setMyself(true);
		}

		return warningTemplatesWithStatus;
	}
	
	/**
	 * 获取均值曲线
	 * @param row 获取多少条。如果是1则获取3条[最小值 最大值 第一条均值...]
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getAvgDataList/{warningTemplateId}/{row}")
	public Map<String, List<List<List<Float>>>> getAvgDataList(@PathVariable("warningTemplateId") String warningTemplateId, @PathVariable("row") int row, HttpServletResponse response){	
		Map<String, List<List<List<Float>>>> resultMap = dataAnalysisService.getAvgDataList(warningTemplateId, row);
		
		return resultMap;
	}
	
	/**
	 * 获取一条极值点
	 * @param row 获取第row条均值的极值点
	 * @param response [[x,y],[x,y]...]
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getAvgExtreme/{warningTemplateId}/{row}")
	public Map<String, List<List<Float>>> getAvgExtreme(@PathVariable("warningTemplateId") String warningTemplateId, @PathVariable("row") int row, HttpServletResponse response){	
		return dataAnalysisService.getAvgExtreme(warningTemplateId, row);
	}
	
	@ResponseBody
	@RequestMapping("getWarningLine/{warningTemplateId}")
	public Map<String, List<List<Float>>> getWarningLine(@PathVariable("warningTemplateId") String warningTemplateId, HttpServletResponse response){	
		Map<String, List<List<Float>>> warningLine = new HashMap<String, List<List<Float>>>();
		
		dataAnalysisService.findWarningLineDataByWarningTemplateId(warningTemplateId, null, warningLine);
		return warningLine;
	}
	
	@ResponseBody
	@RequestMapping("getBaseAndPeakRow/{warningTemplateId}")
	public void getBaseAndPeakRow(@PathVariable("warningTemplateId") String warningTemplateId, HttpServletResponse response){	
		BaseAndPeakRow bpRow = dataAnalysisService.getBaseAndPeakRow(warningTemplateId);
		
		JSONObject jsObj = new JSONObject();
		if(bpRow != null){
			jsObj.put("baseRow", bpRow.getBaseRow());
			jsObj.put("peakRow", bpRow.getPeakRow());
		}
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	@ResponseBody
	@RequestMapping("modifyWarningLine/{warningTemplateId}")
	public List<List<Float>> modifyWarningLine(@PathVariable("warningTemplateId") String warningTemplateId, float startFrequency, float stopFrequency, float threshold, String groupNum){
		List<List<Float>> warningLine = new ArrayList<List<Float>>();
		
		if(dataAnalysisService.canWarningTemplateBeModify(warningTemplateId)){
			warningLine = dataAnalysisService.updateWarningLine(warningTemplateId, startFrequency, stopFrequency, threshold, groupNum);
		}
		
		return warningLine;
	}
	
	@ResponseBody
	@RequestMapping("getDemodulationPointForChart/{warningTemplateId}/{row}")
	public Map<String, List<List<Float>>> getDemodulationPoint(@PathVariable("warningTemplateId") String warningTemplateId, @PathVariable("row") int row, HttpServletResponse response) {
		Map<String, List<List<Float>>> demodulationPointMapForChart = dataAnalysisService.getDemodulationPointForChart(warningTemplateId, row);
		
		return demodulationPointMapForChart;
	}
	
	@ResponseBody
	@RequestMapping("startDemodulate")
	public void startDemodulate(String warningTemplateId, int row, HttpServletResponse response, HttpSession httpSession, HttpServletRequest request) {
		String deviceNum = (String) httpSession.getAttribute("currentDeviceNum");
		AccountTools accountTools = new AccountTools();
		
		JSONObject jsObj = new JSONObject();
		
		//仅能进行唯一的解调，不允许同时开启多个解调
		if(DataPoolTools.deviceDemodParameterMap.containsKey(deviceNum)){
			
			jsObj.put("result", false);
			jsObj.put("type", 1);
			
			ResponseTools.writeResponse(response, jsObj.toString());
			
			return;
		}
		
		//检测设备状态
		if(!DataPoolTools.monitorReportStatusMap.containsKey(deviceNum)){
			
			jsObj.put("result", false);
			jsObj.put("type", 2);
			
			ResponseTools.writeResponse(response, jsObj.toString());
			
			return;
		}
		
		//检测模板是否已启用
		if(!dataAnalysisService.canWarningTemplateBeModify(warningTemplateId)){
			
			jsObj.put("result", false);
			jsObj.put("type", 3);
			
			ResponseTools.writeResponse(response, jsObj.toString());
			
			return;
		}
		
		//序号-frequencyBand-频点
		List<DemodResultParam> demodResultParamList = dataAnalysisService.getDemodResultParam(warningTemplateId, row);
	
		DemodParameter demodParameter = new DemodParameter();
		
		demodParameter.setTemplateId(warningTemplateId);
		demodParameter.setAccountId(accountTools.getCurrentAccountId());
		demodParameter.setDemodResultParamList(demodResultParamList);
		demodParameter.setInterval_ms(200);//解调间隔为200ms
		demodParameter.setMeasure_rate(1000);//每1000秒上传500组数据
		demodParameter.setMode_type(mode_type_enum.REPETITION);//设置为重复解调模式
		demodParameter.setOverTime_m(2);//2分钟无反应提示设备丢失
		demodParameter.setSi_type(si_type_enum.third);
		demodParameter.setStatus(demod_status_enum.start);
		demodParameter.setTraining(true);
		demodParameter.setWarning(false);
		demodParameter.setLastPopDataTime(System.currentTimeMillis());
		
		System.err.println(deviceNum + " == 开始解调"+deviceNum);
		
		DataPoolTools.deviceDemodParameterMap.put(deviceNum, demodParameter);
		
		//frequencyBand-频点-解调结果列，用于显示
		HashMap<String, HashMap<Float, Queue<String[]>>> demodulatingDataMap = new HashMap<String, HashMap<Float, Queue<String[]>>>();
		//frequencyBand-频点-解调结果列，用于存储
		HashMap<String, HashMap<Float, Queue<String>>> demodulatingDataMapToFile = new HashMap<String, HashMap<Float, Queue<String>>>();
		//frequencyBand-解调结果统计列，用于统计
		HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMap = new HashMap<String, ArrayList<DemodResultStatic>>();
		
		List<String> demodulationPointIdList = new ArrayList<String>();
		for(DemodResultParam demodResultParam : demodResultParamList) {
			//频点-解调结果
			HashMap<Float, Queue<String[]>> demodResultMap = new HashMap<Float, Queue<String[]>>();
			//频点-解调结果
			HashMap<Float, Queue<String>> demodResultMapToFile = new HashMap<Float, Queue<String>>();
			
			ArrayList<DemodResultStatic> demodResultStaticList = new ArrayList<DemodResultStatic>();
			
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
			
			demodulationPointIdList.add(demodResultParam.getDemodulationPointId());
		}
		
		dataAnalysisService.deleteDemodResultWithFileByDemodulationPointIdList(demodulationPointIdList);
		
		DataPoolTools.demodulateDataPool.put(deviceNum, demodulatingDataMap);
		DataPoolTools.demodulateDataToFilePool.put(deviceNum, demodulatingDataMapToFile);
		DataPoolTools.demodResultStaticMap.put(deviceNum, demodResultStaticMap);
		
		Thread thread1 = new Thread(new DemodulateDataCreate(dataAnalysisService, deviceNum), deviceNum);
		thread1.start();
		
		Thread thread2 = new Thread(new DemodulateDataFile(dataAnalysisService, deviceNum), deviceNum);
		thread2.start();
		
		//用于回显
		jsObj.put("result", true);
		
		ResponseTools.writeResponse(response, jsObj.toString());
		
	}
	
	/**
	 * 
	 * @param httpSession
	 * @param response
	 */
	@RequestMapping("stopDemodulate")
	public void stopDemodulate(HttpSession httpSession, HttpServletResponse response){
		String deviceNum = (String) httpSession.getAttribute("currentDeviceNum");
		AccountTools accountTools = new AccountTools();
		
		DataPoolTools.clearOverTimeMonitor();//触发清除其它超时的监控
		
		JSONObject jsObj = new JSONObject();
		
		if(DataPoolTools.deviceDemodParameterMap.containsKey(deviceNum) && 
			(DataPoolTools.deviceDemodParameterMap.get(deviceNum).getAccountId().equals(accountTools.getCurrentAccountId())
			|| accountTools.doesCurrentAccountHasAdminRole() 
			|| accountTools.doesCurrentAccountHasSuperadminRole())){//只有主监控端 client,才能发送停止监控的请求
			//下面的方法类似 SpectraController.realTimeMonitorStop	
			DataPoolTools.deviceDemodParameterMap.get(deviceNum).setTraining(false);
			DataPoolTools.deviceDemodParameterMap.get(deviceNum).setStatus(demod_status_enum.stop);
			
			if(DataPoolTools.demodulateDataPool.containsKey(deviceNum)){
				jsObj.put("writable", true);
			} else {
				jsObj.put("writable", false);
			}
			
			jsObj.put("result", true);
		} else {
			jsObj.put("result", false);
		}
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	@ResponseBody
	@RequestMapping("showDemodResult")
	public List<ShowDemodResult> showDemodResult(String deviceNum, String frequencyBand, float x){
		List<ShowDemodResult> showDemodResultList = new ArrayList<ShowDemodResult>();
		HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMap = DataPoolTools.demodResultStaticMap.get(deviceNum);
		if(demodResultStaticMap != null && !demodResultStaticMap.isEmpty()
			&& demodResultStaticMap.containsKey(frequencyBand)){
			
			ArrayList<DemodResultStatic> demodResultStaticList = demodResultStaticMap.get(frequencyBand);
			for(DemodResultStatic temp : demodResultStaticList){
				if(temp.getX() == x){
					ShowDemodResult showDemodResult = new ShowDemodResult();
					
					showDemodResult.setCiValue(temp.getCIValue());
					showDemodResult.setCount(temp.getCount() != 0 ? temp.getCount() : temp.getCarrierToInterferenceList().size());
					showDemodResult.setIndex(temp.getIndex());
					showDemodResult.setIndicatorOfSCHInfo(temp.getIndicatorOfSCHInfo());
					showDemodResult.setLocationAreaCode(temp.getLocationAreaCode());
					showDemodResult.setMobileCountryCode(temp.getMobileCountryCode());
					showDemodResult.setMobileNetworkCode(temp.getMobileNetworkCode());
					showDemodResult.setPduType(temp.getPduType());
					showDemodResult.setRaColour(temp.getRAColour());
					showDemodResult.setSi13Position(temp.getSI13Position());
					showDemodResult.setX(temp.getX());
					
					float avgCI = 0;
					for(float ci : temp.getCarrierToInterferenceList()){
						avgCI = avgCI + ci;
					}
					
					BigDecimal b = new BigDecimal(avgCI/temp.getCarrierToInterferenceList().size()/100);
					showDemodResult.setAvgCI(b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue());
				
					showDemodResultList.add(showDemodResult);
				}
			}
		}
		
		return showDemodResultList;
	}
}
