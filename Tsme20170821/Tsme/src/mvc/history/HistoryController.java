package mvc.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import logic.data.bean.BeanCoordinate;
import logic.history.HistoryService;
import logic.spectra.SpectraService;
import mvc.data.ShowDemodResult;
import mvc.spectra.WarningData;
import net.sf.json.JSONObject;
import tsme.table.deviceWT.DAO.DeviceWTDAO;
import tsme.table.deviceWT.bean.DEVICEWT;
import tsme.table.originalData.bean.ORIGINALDATA;
import tsme.table.originalDemod.bean.ORIGINALDEMOD;
import utils.AccountTools;
import utils.ClearHttpSessionAttribute;
import utils.DataPoolTools;
import utils.MonitorParameter;
import utils.ResponseTools;

@Controller
public class HistoryController {
	
	private static final String js_root_dir = "d:\\";
	
	@Autowired
	@Qualifier("historyService")
	private HistoryService historyService;
	
	@Autowired
	@Qualifier("spectraService")
	private SpectraService spectraService;
	
	@Autowired
	@Qualifier("deviceWTDAO")
	private DeviceWTDAO deviceWTDAO;
	
	@Autowired
	@Qualifier("dataAnalysisService")
	private DataAnalysisService dataAnalysisService;
	
	
	@ResponseBody
	@RequestMapping("getDeviceStatus")
	public void getDeviceStatus(String deviceNum, HttpServletResponse response, HttpServletRequest request){
		JSONObject jsObj = new JSONObject();
		AccountTools accountTools = new AccountTools();
		
		if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum)){	
			jsObj.put("employ", true);
			
			if(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId() == null
				|| DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getAccountId().equals(accountTools.getCurrentAccountId())){
				jsObj.put("myself", true);
			} else {
				jsObj.put("myself", false);
			}
			
		} else {
			jsObj.put("employ", false);
			jsObj.put("myself", true);
		}
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	@ResponseBody
	@RequestMapping("showData/{deviceNum}")
	public ModelAndView show(@PathVariable("deviceNum") String deviceNum){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("deviceNum", deviceNum);
		mav.setViewName("showData");
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("getDateDeviceFBIdMap/{deviceNum}")
	public Map<String, List<String>> getDateDeviceFBIdMap(@PathVariable("deviceNum") String deviceNum, HttpSession httpSession){	
		Map<String, List<ORIGINALDATA>> deviceFBIdOriginalDataMap = new HashMap<String, List<ORIGINALDATA>>();
		Map<String, List<String>> dateDeviceFBIdMap = historyService.getDataDeviceFBIdMapByDeviceNum(deviceNum, deviceFBIdOriginalDataMap);
		
		new ClearHttpSessionAttribute(httpSession);
		httpSession.setAttribute("deviceFBIdQriginalDataMap", deviceFBIdOriginalDataMap);
		
		return dateDeviceFBIdMap;	
	}
	
	@ResponseBody
	@RequestMapping("getTemplateDeviceFBList")
	public List<TemplateDeviceFB> getTemplateDeviceFBList(String[] deviceFBIds){	
		List<TemplateDeviceFB> templateDeviceFBList = historyService.getTemplateDeviceFBMapByDeviceFBIds(deviceFBIds);
		return templateDeviceFBList;	
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("getFileList")
	public List<String> getFileList(String deviceFBId, String dateString, HttpSession httpSession){	
		List<String> filePathList = new ArrayList<String>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, List<ORIGINALDATA>> deviceFBIdQriginalDataMap = (Map<String, List<ORIGINALDATA>>) httpSession.getAttribute("deviceFBIdQriginalDataMap");
		if(!deviceFBIdQriginalDataMap.isEmpty() && deviceFBIdQriginalDataMap.containsKey(deviceFBId)){
			for(ORIGINALDATA temp : deviceFBIdQriginalDataMap.get(deviceFBId)){
				String date = sdf.format(temp.getDate());
				if(date.equals(dateString)){
					filePathList.add(temp.getFile_path());
				}
			}
		}
		
		return filePathList;	
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("getFrequencyFile")
	public void getFrequencyFile(String filePath, String deviceTemplateId, Float startFrequency, Float stopFrequency, HttpServletResponse response, HttpSession httpSession){	
		JSONObject jsObj = new JSONObject();
		
		DEVICEWT deviceWT = deviceWTDAO.findActivatedById(deviceTemplateId);
		
		List<List<Float>> warningline = new ArrayList<List<Float>>();
		List<BeanCoordinate> warningPointList = new ArrayList<BeanCoordinate>();
		historyService.getWarningLineAndPointList(deviceTemplateId, startFrequency, stopFrequency, warningline, warningPointList);
		
		List<List<Float>> demodulationPointList = new ArrayList<List<Float>>();
		List<ORIGINALDEMOD> originalDemodList = historyService.getDemodulationPointByOriginalDataFilePath(filePath, demodulationPointList);
		
		List<List<Float>> statisticPointList = new ArrayList<List<Float>>();
		Map<Float, List<ShowDemodResult>> showDemodResultMap = historyService.getShowDemodResultMapByOriginalDataFilePath(filePath, statisticPointList);
		
		File file = new File(filePath);
		
		int groupNum = 0;
		List<ArrayList<Double>> originalSpetrum = new ArrayList<ArrayList<Double>>();
		if(file.exists()){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				
				String tempString = null;
				
				//读文件并计算行数
				while ((tempString = reader.readLine()) != null ) {
					String row[] = null;
					
					if (tempString.startsWith("[") && tempString.endsWith("],")){
						row = tempString.substring(1, tempString.length() - 2).split(",");
					} else if (tempString.startsWith("[") && tempString.endsWith("]")) {
						row = tempString.substring(1, tempString.length() - 1).split(",");
					}
					
					if(row != null){
						ArrayList<Double> pointList = new ArrayList<Double>();
						for(String point : row){
							pointList.add(Double.parseDouble(point));
						}
						originalSpetrum.add(pointList);
						
						groupNum ++;
					}	
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		DataPoolTools.httpSessionIdSpetrumMap.put(httpSession.getId(), originalSpetrum);
		DataPoolTools.httpSessionIdWarningPointMap.put(httpSession.getId(), warningPointList);
		
		jsObj.put("warningline", warningline);
		jsObj.put("groupNum", groupNum);
		jsObj.put("demodulationPointList", demodulationPointList);
		jsObj.put("statisticPointList", statisticPointList);
		
		Map<String, List<ORIGINALDATA>> deviceFBIdQriginalDataMap = (Map<String, List<ORIGINALDATA>>) httpSession.getAttribute("deviceFBIdQriginalDataMap");
		
		new ClearHttpSessionAttribute(httpSession);
		httpSession.setAttribute("originalDemodList", originalDemodList);
		httpSession.setAttribute("showDemodResultMap", showDemodResultMap);
		httpSession.setAttribute("deviceFBIdQriginalDataMap", deviceFBIdQriginalDataMap);
		
		jsObj.put("templateName", deviceWT.getTemplate_name());
		jsObj.put("parameter", deviceWT.getFftSize());
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("getSpectraData")
	public void getSpectraData(String createTime, HttpServletResponse response, HttpSession httpSession){	
		JSONObject jsObj = new JSONObject();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		long time = 0;
		try {
			time = sdf.parse(createTime).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int index = 0;
		List<Double> show = new ArrayList<Double>();
		List<ArrayList<Double>> originalSpetrum = (List<ArrayList<Double>>) httpSession.getAttribute("originalSpetrum");
		for(ArrayList<Double> pointList : originalSpetrum){
			long timeO = Math.round(pointList.get(pointList.size() - 1));
			if(timeO == time){
				show.addAll(pointList.subList(0, pointList.size() - 3));
				jsObj.put("LNG", String.format("%.4f", pointList.get(pointList.size() - 3)));
				jsObj.put("LAT", String.format("%.4f", pointList.get(pointList.size() - 2)));
				jsObj.put("index", index);
				break;
			}
			index ++;
		}
		
		jsObj.put("spDataList", show);
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("showOriginalDemod")
	public void showDemonOriginal(HttpServletResponse response, HttpSession httpSession, float x){
		JSONObject jsObj = new JSONObject();
		
		List<ORIGINALDEMOD> originalDemodList = (List<ORIGINALDEMOD>) httpSession.getAttribute("originalDemodList");
		
		String filePath = "";
		StringBuffer originalDemod = new StringBuffer("");
		
		for(ORIGINALDEMOD temp : originalDemodList){
			if(temp.getFrequencyPoint() == x){
				filePath = temp.getFile_path();
				break;
			}
		}
		
		if(filePath != ""){
			File file = new File(filePath);
			BufferedReader reader = null;
			String tempString = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				
				while ((tempString = reader.readLine()) != null) {
					originalDemod.append(tempString);	
					originalDemod.append("<br>");
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		jsObj.put("originalDemod", originalDemod.toString());
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("showStatisticResult")
	public List<ShowDemodResult> showStatisticResult(HttpSession httpSession, float x){
		HashMap<Float, List<ShowDemodResult>> showDemodResultMap = (HashMap<Float, List<ShowDemodResult>>) httpSession.getAttribute("showDemodResultMap");
		
		if(showDemodResultMap != null && !showDemodResultMap.isEmpty()
			&& showDemodResultMap.containsKey(x)){
			return showDemodResultMap.get(x);
		} else {
			return null;
		}
	}
	
	@ResponseBody
	@RequestMapping("showWarningDetails")
	public List<String> showWarningDetails(Float centerFrequency, HttpSession httpSession){
		List<WarningData> warningDataList = DataPoolTools.httpSessionIdWarningDataMap.get(httpSession.getId());
		for(WarningData warningData : warningDataList) {
			if(warningData.getCenterFrequency() == centerFrequency) {
				List<String> tempList = new ArrayList<String>(warningData.getWarningTimeList());
				Collections.reverse(tempList);
				return tempList;
			}
		}
		
		return null;
	}
	
	@ResponseBody
	@RequestMapping("releaseWarning")
	public boolean releaseWarning(Float centerFrequency, HttpSession httpSession){
		DataPoolTools.httpSessionIdCenterFrequencyMap.put(httpSession.getId(), centerFrequency);

		return true;
	}
	
	@RequestMapping("getDeviceInfoToShowHistory")
	public ModelAndView getDeviceInfoToShowHistory(){
		ModelAndView mav = new ModelAndView();
		
		List<DeviceInfo> deviceInfoList = historyService.getDeviceInfoToShowHistory();
		
		for(int i = 0; i < deviceInfoList.size(); i ++){
			if(DataPoolTools.monitorReportStatusMap.containsKey(deviceInfoList.get(i).getDeviceNum())){
				if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceInfoList.get(i).getDeviceNum())){
					MonitorParameter monitorParameter = DataPoolTools.deviceMonitorParameterMap.get(deviceInfoList.get(i).getId());
					if(monitorParameter.isTraining()){
						deviceInfoList.get(i).setStatus("训练中");
					} else if(monitorParameter.isWarning()){
						deviceInfoList.get(i).setStatus("监测中");
					}
				} else {
					deviceInfoList.get(i).setStatus("空闲中");
				}
			} else {
				deviceInfoList.get(i).setStatus("未在线");
			}
		}
			
		mav.addObject("deviceInfoList", deviceInfoList);
		mav.setViewName("showDeviceForSpectrum");
		return mav;	
	}
	
	@RequestMapping("getDeviceInfoToShowTemplate")
	public ModelAndView getDeviceInfoToShowTemplate(){
		ModelAndView mav = new ModelAndView();
		
		List<DeviceInfo> deviceInfoList = dataAnalysisService.getDeviceInfoToShowTemplate();
		
		for(int i = 0; i < deviceInfoList.size(); i ++){
			if(DataPoolTools.monitorReportStatusMap.containsKey(deviceInfoList.get(i).getDeviceNum())){
				if(DataPoolTools.deviceMonitorParameterMap.containsKey(deviceInfoList.get(i).getDeviceNum())){
					MonitorParameter monitorParameter = DataPoolTools.deviceMonitorParameterMap.get(deviceInfoList.get(i).getId());
					if(monitorParameter.isTraining()){
						deviceInfoList.get(i).setStatus("训练中");
					} else if(monitorParameter.isWarning()){
						deviceInfoList.get(i).setStatus("监测中");
					}
				} else {
					deviceInfoList.get(i).setStatus("空闲中");
				}
			} else {
				deviceInfoList.get(i).setStatus("未在线");
			}
		}
			
		mav.addObject("deviceInfoList", deviceInfoList);
		mav.setViewName("showDeviceForTemplate");
		return mav;	
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("getAlarmFile")
	public void getAlarmFile(String deviceNum, String filePath, String deviceTemplateId, Float startFrequency, Float stopFrequency, 
							HttpServletResponse response, HttpSession httpSession) {	
		
		JSONObject jsObj = new JSONObject();
		
		DEVICEWT deviceWT = deviceWTDAO.findActivatedById(deviceTemplateId);
		
		/**
		 * 读取频谱数据
		 */
		List<ArrayList<Double>> originalSpetrum = new ArrayList<ArrayList<Double>>();
		File fileS = new File(filePath);
		int groupNum = 0;
		if(fileS.exists()){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(fileS));
				
				String tempString = null;
				
				//读文件并计算行数
				while ((tempString = reader.readLine()) != null ) {
					String row[] = null;
					
					if (tempString.startsWith("[") && tempString.endsWith("],")){
						row = tempString.substring(1, tempString.length() - 2).split(",");
					} else if (tempString.startsWith("[") && tempString.endsWith("]")) {
						row = tempString.substring(1, tempString.length() - 1).split(",");
					}
					
					if(row != null){
						ArrayList<Double> pointList = new ArrayList<Double>();
						for(String point : row){
							pointList.add(Double.parseDouble(point));
						}
						originalSpetrum.add(pointList);
						
						groupNum ++;
					}	
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		/**
		 * 生成告警文件
		
		List<WarningHistory> warningHistoryList = new ArrayList<WarningHistory>();
		if(DataPoolTools.httpSessionIdWarningHistoryMap != null
			&& DataPoolTools.httpSessionIdWarningHistoryMap.containsKey(httpSession.getId())){
			warningHistoryList = DataPoolTools.httpSessionIdWarningHistoryMap.get(httpSession.getId());
		}
		
		if(warningHistoryList != null && warningHistoryList.size() > 0){
			String var = GTime.getTime(GTime.YYYYMMDDhhmmssxxx);
			String filePathAlarm = js_root_dir + "data\\history\\" + deviceNum + "\\" + startFrequency + "-" + stopFrequency + "\\alarm\\" + var + ".js";

			File file1 = new File(filePathAlarm);
			if(!file1.exists()){
				new File(file1.getParent()).mkdirs();
				try {
					file1.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			StringBuilder sbAlarm = new StringBuilder();
			for(WarningHistory wh : warningHistoryList){
				sbAlarm.append("warningTime=").append(wh.getWarningTime()).append(";");
				sbAlarm.append("warningGroup:");
				for(WarningGroup wg : wh.getWarningGroupList()){
					sbAlarm.append("beginPoint=").append(wg.getBeginPoint().getX()).append(",").append(wg.getBeginPoint().getY()).append("&");
					sbAlarm.append("endPoint=").append(wg.getEndPoint().getX()).append(",").append(wg.getEndPoint().getY()).append("&");
					sbAlarm.append("centerFre=").append(wg.getCenterFrequency()).append("@");
				}
				sbAlarm.setCharAt(sbAlarm.length() - 1, '#');
				sbAlarm.append("\r\n");
			}
			sbAlarm.setLength(sbAlarm.length() - 2);

			try {
				//开始写文件
				FileWriter writer = new FileWriter(filePathAlarm, true);
				writer.write(sbAlarm.toString());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String originalDataId = historyService.getOriginalDataIdByOriginalDataFilePath(filePath);
			spectraService.saveOriginalAlarmDataFilePath(filePathAlarm, originalDataId);
		}
		DataPoolTools.httpSessionIdWarningHistoryMap.remove(httpSession.getId());
		 */
		
		/**
		 * 读取告警数据
		 */
		List<WarningHistory> warnHistoryList = new ArrayList<WarningHistory>();
		List<ArrayList<Double>> alarmPointList = new ArrayList<ArrayList<Double>>();
		String alarmFilePath = historyService.getOriginalAlarmFilePathByOriginalDataFilePath(filePath);
		File fileA = new File(alarmFilePath);
		if(fileA.exists()){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(fileA));
				
				//读文件并计算行数
				String tempString = null;
				while ((tempString = reader.readLine()) != null ) {
					String row[] = null;
					
					if (tempString.endsWith("#")){
						row = tempString.substring(0, tempString.length() - 1).split(";");
					}
					Long alarmY = (long) 0;
					if(row != null){
						WarningHistory warningHistory = new WarningHistory();
						for(String item : row){
							if(item.contains("warningTime")){
								String[] temp = item.split("=");
								alarmY = Long.valueOf(temp[1]);
								warningHistory.setWarningTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(Long.valueOf(temp[1]))));
								continue;
							}
							if(item.contains("warningGroup")){
								String[] temp = item.split(":");
								String wgt = temp[1];
								String[] wgs = wgt.split("@");
								List<WarningGroup> warningGroupList = new ArrayList<WarningGroup>();
								for(String wg : wgs){
									WarningGroup warningGroup = new WarningGroup();
									String[] points = wg.split("&");
									for(String point : points){
										if(point.contains("beginPoint")){
											String[] xyg = point.split("=");
											String[] xy = xyg[1].split(",");
											
											BeanCoordinate beginPoint = new BeanCoordinate();
											beginPoint.setX(Float.valueOf(xy[0]));
											beginPoint.setY(Float.valueOf(xy[1]));
											
											warningGroup.setBeginPoint(beginPoint);
											continue;
										}
										if(point.contains("endPoint")){
											String[] xyg = point.split("=");
											String[] xy = xyg[1].split(",");
											
											BeanCoordinate endPoint = new BeanCoordinate();
											endPoint.setX(Float.valueOf(xy[0]));
											endPoint.setY(Float.valueOf(xy[1]));
											
											warningGroup.setEndPoint(endPoint);;
											continue;
										}
										if(point.contains("centerFre")){
											String[] centerFreG = point.split("=");
											String centerFre = centerFreG[1];
											
											warningGroup.setCenterFrequency(Float.valueOf(centerFre));;
											
											ArrayList<Double> alarmPoint = new ArrayList<Double>();
											alarmPoint.add(Double.valueOf(centerFre));
											String timeStr = new SimpleDateFormat("HHmm.ss").format(new Date(alarmY)).toString();
											alarmPoint.add(Double.valueOf(timeStr));
											alarmPointList.add(alarmPoint);
											
											continue;
										}
									}
									warningGroupList.add(warningGroup);
								}
								warningHistory.setWarningGroupList(warningGroupList);
								continue;
							}
						}
						warnHistoryList.add(warningHistory);
					}	
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		List<List<Float>> warningline = new ArrayList<List<Float>>();
		List<BeanCoordinate> warningPointList = new ArrayList<BeanCoordinate>();
		historyService.getWarningLineAndPointList(deviceTemplateId, startFrequency, stopFrequency, warningline, warningPointList);
		
		List<List<Float>> demodulationPointList = new ArrayList<List<Float>>();
		List<ORIGINALDEMOD> originalDemodList = historyService.getDemodulationPointByOriginalDataFilePath(filePath, demodulationPointList);
		
		List<List<Float>> statisticPointList = new ArrayList<List<Float>>();
		Map<Float, List<ShowDemodResult>> showDemodResultMap = historyService.getShowDemodResultMapByOriginalDataFilePath(filePath, statisticPointList);
		
		jsObj.put("warningline", warningline);//预警曲线
		jsObj.put("groupNum", groupNum);//数据组个数
		jsObj.put("demodulationPointList", demodulationPointList);//待解调点
		jsObj.put("statisticPointList", statisticPointList);//已解调点
		jsObj.put("warnHistoryList", warnHistoryList);
		
		ArrayList<Double> tempGroup = originalSpetrum.get(originalSpetrum.size() - 1);
		String timeStr = new SimpleDateFormat("HHmm.ss").format(new Date(Math.round(tempGroup.get(tempGroup.size() - 1)))).toString();
		jsObj.put("stopTime", Double.valueOf(timeStr));
		tempGroup = originalSpetrum.get(0);
		timeStr = new SimpleDateFormat("HHmm.ss").format(new Date(Math.round(tempGroup.get(tempGroup.size() - 1)))).toString();
		jsObj.put("startTime", Double.valueOf(timeStr));
		jsObj.put("alarmPointList", alarmPointList);
		
		Map<String, List<ORIGINALDATA>> deviceFBIdQriginalDataMap = (Map<String, List<ORIGINALDATA>>) httpSession.getAttribute("deviceFBIdQriginalDataMap");
		
		new ClearHttpSessionAttribute(httpSession);
		httpSession.setAttribute("originalSpetrum", originalSpetrum);
		httpSession.setAttribute("originalDemodList", originalDemodList);
		httpSession.setAttribute("showDemodResultMap", showDemodResultMap);
		httpSession.setAttribute("deviceFBIdQriginalDataMap", deviceFBIdQriginalDataMap);
		
		jsObj.put("templateName", deviceWT.getTemplate_name());
		jsObj.put("parameter", deviceWT.getFftSize());
		
		ResponseTools.writeResponse(response, jsObj.toString());
	}
}
