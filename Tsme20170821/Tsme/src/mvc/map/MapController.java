package mvc.map;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import logic.map.MapService;
import mvc.device.ReportStatus;
import net.sf.json.JSONArray;
import utils.DataPoolTools;

@Controller
public class MapController {
	
	@Autowired
	@Qualifier("mapService")
	MapService mapService;
	
	@RequestMapping("mapIndex")
	public ModelAndView mapIndex(){
		ModelAndView mav = new ModelAndView();
		
		//获取基站
		List<GeoMsg> geoMsgs = mapService.getBSGeoMsg();
		//获取监测设备
		List<GeoMsg> deviceGeoMsgs = mapService.getDeviceGeoMsg();
		
		geoMsgs.addAll(deviceGeoMsgs);
		
		mav.addObject("geoMsgs", geoMsgs);
		mav.setViewName("displayAll");
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("getDeviceGPS")
	public List<GeoMsg> getDeviceStatus() {
		List<GeoMsg> geoMsgList = new ArrayList<GeoMsg>();
		for(Entry<String, ReportStatus> deviceMap : DataPoolTools.monitorReportStatusMap.entrySet()) {
			GeoMsg geoMsg = new GeoMsg();
			
			ReportStatus reportStatus = deviceMap.getValue();
			
			geoMsg.setDevice_num(deviceMap.getKey());
			geoMsg.setDevice_type(reportStatus.getDevice_type());
			geoMsg.setLAT(reportStatus.getLatitude());//纬度
			geoMsg.setLNG(reportStatus.getLongitude());//经度
			geoMsg.setIp(reportStatus.getIpAddress());
			geoMsg.setPoint_type("DS");
			geoMsg.setIntroduction("TSME2000");
			geoMsg.setState("normal");
			
			geoMsgList.add(geoMsg);
		}
		
		for(Entry<String, ReportStatus> deviceMap : DataPoolTools.offlineReportStatusMap.entrySet()) {
			GeoMsg geoMsg = new GeoMsg();
			
			ReportStatus reportStatus = deviceMap.getValue();
			
			geoMsg.setDevice_num(deviceMap.getKey());
			geoMsg.setDevice_type(reportStatus.getDevice_type());
			geoMsg.setLAT(reportStatus.getLatitude());//纬度
			geoMsg.setLNG(reportStatus.getLongitude());//经度
			geoMsg.setIp(reportStatus.getIpAddress());
			geoMsg.setPoint_type("DS");
			geoMsg.setIntroduction("TSME2000");
			geoMsg.setState("offline");
			
			geoMsgList.add(geoMsg);
		}
		
		DataPoolTools.offlineReportStatusMap.clear();
		
		return geoMsgList;
	}
	
	@ResponseBody
	@RequestMapping("getOnlineDeviceList")
	public List<OnlineDevice> getDeviceInfo() {
		List<OnlineDevice> onlineDeviceList = new ArrayList<OnlineDevice>();
		
		for(Entry<String, ReportStatus> monitorReportStatusEntry : DataPoolTools.monitorReportStatusMap.entrySet()){
			OnlineDevice onlineDevice = new OnlineDevice();
			
			onlineDevice.setDevice_num(monitorReportStatusEntry.getKey());
			if(monitorReportStatusEntry.getValue().getMonitor_code() == ReportStatus.report_code_req_monitorParam && 
				monitorReportStatusEntry.getValue().getDemod_code() == ReportStatus.report_code_req_demodParam) {
				onlineDevice.setDevice_status("free");
			} else {
				if(DataPoolTools.deviceMonitorParameterMap.get(monitorReportStatusEntry.getKey()).isTraining()) {
					onlineDevice.setDevice_status("training");
				} else if(DataPoolTools.deviceMonitorParameterMap.get(monitorReportStatusEntry.getKey()).isWarning()) {
					onlineDevice.setDevice_status("warning");
				} else {
					onlineDevice.setDevice_status("free");
				}
			}
			
			if(DataPoolTools.spectraDataMap.containsKey(monitorReportStatusEntry.getKey())){
				onlineDevice.setWarning(true);
			}
			
			onlineDeviceList.add(onlineDevice);
		}
		
		return onlineDeviceList;
	}
	
	@RequestMapping("mapIndexBaidu")
	public ModelAndView mapIndexBaidu(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("displayAllBaidu");
		return mav;
	}
	
	@RequestMapping("getBSGeoMsg")
	public void getBSGeoMsg(HttpServletResponse res){
		List<GeoMsg> geoMsgs = mapService.getBSGeoMsg();
		JSONArray array = JSONArray.fromObject(geoMsgs);
		res.setContentType("text/json;charset=UTF-8");
		res.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		try {
			writer = res.getWriter();
			String str = array.toString();
			writer.print(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("getDeviceGeoMsg")
	public void getDeviceGeoMsg(HttpServletResponse res){
		List<GeoMsg> geoMsgs = mapService.getDeviceGeoMsg();
		JSONArray array = JSONArray.fromObject(geoMsgs);
		res.setContentType("text/json;charset=UTF-8");
		res.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		try {
			writer = res.getWriter();
			String str = array.toString();
			writer.print(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("showBSDetail")
	public void showBSDetail(float LNG, float LAT, HttpServletResponse response) {
		/*ModelAndView mav = new ModelAndView();
		BASESTATION baseStation = mapService.getBSInfoWithPosition(LNG, LAT);
		if(baseStation != null) {
			mav.addObject("BS_id", baseStation.getId());
			mav.addObject("BS_intro", baseStation.getIntroduction());
			mav.addObject("BS_lng", LNG);
			mav.addObject("BS_lat", LAT);
		}else {
			mav.addObject("station", "unfound");
		}
		mav.setViewName("showBSDetail");*/
		
		try {
			PrintWriter writer = response.getWriter();
			writer.print("123");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	return mav;
	}
	
	@RequestMapping("loadSearch")
	public ModelAndView loadSearch() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("search");
		return mav;
	}
	
	@RequestMapping("promptForm")
	public ModelAndView promptForm(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("promptForm");
		return mav;
	}
}
