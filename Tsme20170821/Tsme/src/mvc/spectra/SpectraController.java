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
	 * �ͻ���ʹ�õ�ͼ�ϵ������ȡģ���б�
	 * @param monitorParameter
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getWarningTemplateList")
	public void getWarningTemplateList(String deviceNum, HttpServletResponse response){
		JSONObject jsObj = new JSONObject();
		
		DataPoolTools.clearOverTimeMonitor();//�����ʱ�ļ��
		
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
	 * �ͻ���ʹ�õ�ͼ�ϵ����������ҳ�棬�˷���ֻ��Ҫ��¼�������templateId���豸id���ɡ�
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
			//�˴�Ϊcopy��ʾ����µĴ�����
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
		
		//���豸Ϊ����ʧȥ���ӡ�����л�����״̬���򲻿��ظ�����
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
		demodParameter.setInterval_ms(200);//������Ϊ200ms
		demodParameter.setMeasure_rate(1000);//ÿ1000���ϴ�1000������
		demodParameter.setMode_type(mode_type_enum.REPETITION);//����Ϊ�ظ����ģʽ
		demodParameter.setOverTime_m(2);//2�����޷�Ӧ��ʾ�豸��ʧ
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
		
		//��ȡ���������ֵ
		BaseAndPeakRow bpRow = dataAnalysisService.getBaseAndPeakRow(templateId);
		//��ȡ��ֵ��,frequencyBand-����
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
		
		
		/*******************�洢Ԥ��ģ��*******************/
		DEVICEWT deviceWT = spectraService.copyWarningTemplate(warningTemplate, warningPointMap, demodulationPointMap);
		
		Map<String, String> frequencyBandMap = new HashMap<String, String>();
		for(DEVICEFB temp : deviceWT.getDeviceFBList()){
			frequencyBandMap.put(temp.getStartFrequency() + "-" + temp.getStopFrequency(), temp.getId());
		}
		monitorParameter.setFrequencyBandMap(frequencyBandMap);
		
		//��ȡ�������Ĵ������
		List<DemodResultParam> demodResultParamList = spectraService.getDemodResultParam(deviceWT.getId());
		demodParameter.setDemodResultParamList(demodResultParamList);
		
		
		/*******************�����豸���*******************/
		monitorParameter.setAccountId(accountTools.getCurrentAccountId());
		monitorParameter.setStatus(monitor_status_enum.start);
		monitorParameter.setOverTime_m(5);
		
		demodParameter.setAccountId(accountTools.getCurrentAccountId());;
		
		DataPoolTools.deviceMonitorParameterMap.put(deviceNum, monitorParameter);
		
		//���ü�����ݳ�
		HashMap<String, Queue<ArrayList<Double>>> monitorDataMap = new HashMap<String, Queue<ArrayList<Double>>>();
		for(String key : monitorParameter.getWarningLineMap().keySet()){
			monitorDataMap.put(key, new LinkedList<ArrayList<Double>>());
		}
		
		DataPoolTools.monitorDataPool.put(deviceNum, monitorDataMap);
		
		Thread thread1 = new Thread(new WaringDataCreate(deviceNum), deviceNum);
		thread1.start();
		
		//���ü�����ݳ�,���ڴ洢ԭʼ����
		HashMap<String, Queue<ArrayList<Double>>> monitorDataMapToFile = new HashMap<String, Queue<ArrayList<Double>>>();
		for(String key : monitorParameter.getWarningLineMap().keySet()){
			monitorDataMapToFile.put(key, new LinkedList<ArrayList<Double>>());
		}
		
		DataPoolTools.monitorDataToFilePool.put(deviceNum, monitorDataMapToFile);
		
		Thread thread2 = new Thread(new WaringDataFile(spectraService, accountTools.getCurrentAccountId(), deviceNum), deviceNum);
		thread2.start();
		
		//���ý��
		DataPoolTools.deviceDemodParameterMap.put(deviceNum, demodParameter);
		
		//frequencyBand-Ƶ��-�������У�������ʾ
		HashMap<String, HashMap<Float, Queue<String[]>>> demodulatingDataMap = new HashMap<String, HashMap<Float, Queue<String[]>>>();
		//frequencyBand-Ƶ��-�������У����ڴ洢
		HashMap<String, HashMap<Float, Queue<String>>> demodulatingDataMapToFile = new HashMap<String, HashMap<Float, Queue<String>>>();
		
		//frequencyBand-������ͳ���У�����ͳ��
		HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMap = new HashMap<String, ArrayList<DemodResultStatic>>();
		//frequencyBand-������ͳ���У�����ͳ��
		HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMapToDB = new HashMap<String, ArrayList<DemodResultStatic>>();
		for(DemodResultParam demodResultParam : demodResultParamList) {
			//Ƶ��-������
			HashMap<Float, Queue<String[]>> demodResultMap = new HashMap<Float, Queue<String[]>>();
			//Ƶ��-������
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
		DataPoolTools.clearOverTimeMonitor();//�����ʱ�ļ��
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
				DataPoolTools.deviceMonitorParameterMap.get(deviceNum).setStatus(monitor_status_enum.stop);//ֻ������ض� client,���ܷ���ֹͣ��ص�����
				DataPoolTools.deviceDemodParameterMap.get(deviceNum).setWarning(false);
				DataPoolTools.deviceDemodParameterMap.get(deviceNum).setStatus(demod_status_enum.stop);
				System.err.println(deviceNum + " == ֹͣ�������======");
				
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
	 * ��ȡ�����ļ�
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
