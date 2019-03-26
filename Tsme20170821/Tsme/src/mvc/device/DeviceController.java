package mvc.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tsme.table.device.DAO.DeviceDAO;
import tsme.table.device.bean.DEVICE;
import utils.AddressTools;
import utils.DataPoolTools;
import utils.DemodParameter;
import utils.DemodParameter.demod_status_enum;
import utils.MonitorParameter;
import utils.MonitorParameter.monitor_status_enum;

/**
 * 客户端传输数据使用
 * @author kitty
 */
@Controller
public class DeviceController {
	
	@Autowired
	@Qualifier("deviceDAO")
	private DeviceDAO deviceDAO;
	
	//设备接入
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/deviceAccess")
	public Response deviceAccess(ReportStatus reportStatus, HttpServletRequest request, HttpServletResponse response){
		Response respMsg = new Response();
		
		if(reportStatus.getDevice_num() != null && reportStatus.getDevice_num().length() > 3 
			&& DataPoolTools.authorizedDeviceNumSet.contains(reportStatus.getDevice_num())
			&& reportStatus.getLongitude() > 0 && reportStatus.getLatitude() > 0) {
			
			String sql = "SELECT * FROM device WHERE device_num = '" + reportStatus.getDevice_num() + "' AND active = 1";
			List<DEVICE> deviceList = (List<DEVICE>) deviceDAO.findByQuery(sql, DEVICE.class);
			
			if(deviceList != null && deviceList.size() > 0){
				DEVICE device = deviceList.get(0);
				
				device.setLAT(reportStatus.getLatitude());
				device.setLNG(reportStatus.getLongitude());
				device.setIp(AddressTools.getRemoteIPAddr(request));
				device.setCreate_time(System.currentTimeMillis());
				
				deviceDAO.update(device);
			} else {
				DEVICE device = new DEVICE();
				
				device.setDevice_num(reportStatus.getDevice_num());
				device.setDevice_type(reportStatus.getDevice_type());
				device.setLAT(reportStatus.getLatitude());
				device.setLNG(reportStatus.getLongitude());
				device.setIp(AddressTools.getRemoteIPAddr(request));
				device.setActive(true);
				device.setCreate_time(System.currentTimeMillis());
				
				deviceDAO.save(device);
			}
			respMsg.setCommon_code(Response.resp_common_code_normal);
		} else {
			respMsg.setCommon_code(Response.resp_common_code_format_error);
		}
		
		return respMsg;
	}
	
	//上报状态
	@ResponseBody
	@RequestMapping("/reportStatus")
	public Response reportStatus(ReportStatus reportStatus, HttpServletRequest request, HttpServletResponse response){
		
		Response respMsg = new Response();
		respMsg.setId(reportStatus.getDevice_num());
		
		reportStatus.setIpAddress(AddressTools.getRemoteIPAddr(request));
		reportStatus.setUpdateTime(System.currentTimeMillis());
		DataPoolTools.monitorReportStatusMap.put(reportStatus.getDevice_num(), reportStatus);
		
		switch(reportStatus.getMonitor_code()){
			case ReportStatus.report_code_req_monitorParam:{ //代码21,设备向服务器请求频谱监测参数
				if(DataPoolTools.deviceMonitorParameterMap.containsKey(reportStatus.getDevice_num())
					&& DataPoolTools.authorizedDeviceNumSet.contains(reportStatus.getDevice_num())){
					//获取用户设定的设备参数
					MonitorParameter monitorParameter = DataPoolTools.deviceMonitorParameterMap.get(reportStatus.getDevice_num());
					//设备向服务器请求用户设定的参数
					if(monitorParameter.getStatus().equals(monitor_status_enum.start)
						|| monitorParameter.getStatus().equals(monitor_status_enum.transfer)){
							
						respMsg.setMonitor_code(Response.resp_monitor_code_begin);//11
							
						ResponseMonitorParam responseMonitorParam = new ResponseMonitorParam();
						responseMonitorParam.setMaxMeans(monitorParameter.getMaxMeans());
						responseMonitorParam.setFrequencyList(monitorParameter.getFrequencyList());
						responseMonitorParam.setFftSize(monitorParameter.getFftSize());
						responseMonitorParam.setBandWidth(monitorParameter.getBandWidth());
						responseMonitorParam.setInterval(monitorParameter.getInterval());
						
						respMsg.setResponseMonitorParam(responseMonitorParam);
						
						//System.out.println(reportStatus.getDevice_num() + " 通知设备上传数据");
						
						monitorParameter.setStatus(monitor_status_enum.transfer);
						
					} else if(monitorParameter.getStatus().equals(monitor_status_enum.stop) 
						|| monitorParameter.getStatus().equals(monitor_status_enum.stopped) 
						|| monitorParameter.isMainClientOverTime()){
						/** 状态为需要停止，则通知设备不用上传数据了;
						 *  超时时限需要在开启检测时设置
						 */
						System.out.println(reportStatus.getDevice_num() + " 通知设备停止扫描！");
						monitorParameter.setStatus(monitor_status_enum.stopped);
						respMsg.setMonitor_code(Response.resp_monitor_code_stop);//12
					}
				} else {
					respMsg.setMonitor_code(Response.resp_monitor_code_stop);//12
				}
			}
			break;
			
			case ReportStatus.report_code_normal:{ //代码20，设备正常报告状态
				if(DataPoolTools.deviceMonitorParameterMap.containsKey(reportStatus.getDevice_num())
					&& DataPoolTools.authorizedDeviceNumSet.contains(reportStatus.getDevice_num())){
					if(DataPoolTools.deviceMonitorParameterMap.get(reportStatus.getDevice_num()).getStatus().equals(monitor_status_enum.transfer) ||
						DataPoolTools.deviceMonitorParameterMap.get(reportStatus.getDevice_num()).getStatus().equals(monitor_status_enum.start)){
						respMsg.setMonitor_code(Response.resp_monitor_code_normal);//10
					} else {
						respMsg.setMonitor_code(Response.resp_monitor_code_stop);//12
					}
					
				} else {
					respMsg.setMonitor_code(Response.resp_monitor_code_stop);//12
				}
			}
			break;
		}
		
		switch(reportStatus.getDemod_code()){
			case ReportStatus.report_code_req_demodParam: { //代码22,设备向服务器请求解调参数
				if(DataPoolTools.deviceDemodParameterMap.containsKey(reportStatus.getDevice_num())
					&& DataPoolTools.authorizedDeviceNumSet.contains(reportStatus.getDevice_num())){
					//获取用户设定的解调参数
					DemodParameter demodParameter = DataPoolTools.deviceDemodParameterMap.get(reportStatus.getDevice_num());
					//设备向服务器请求用户设定的参数
					if(demodParameter.getStatus().equals(demod_status_enum.start)
						|| demodParameter.getStatus().equals(demod_status_enum.transfer)){
							
						respMsg.setDemod_code(Response.resp_demod_code_begin);//31
							
						ResponseDemodParam responseDemodParam = new ResponseDemodParam();
						
						List<Float> xList = new ArrayList<Float>();
						for(DemodResultParam temp : demodParameter.getDemodResultParamList()){
							xList.add(temp.getX());
						}
						responseDemodParam.setDemodPointList(xList);
						responseDemodParam.setInterval_ms(demodParameter.getInterval_ms());
						responseDemodParam.setMeasure_rate(demodParameter.getMeasure_rate());
						responseDemodParam.setMode_type(demodParameter.getMode_type().getIndex());
						responseDemodParam.setSi_type(demodParameter.getSi_type().getIndex());
						respMsg.setResponseDemodParam(responseDemodParam);
								
						//System.out.println(reportStatus.getDevice_num() + " 通知设备上传数据");
							
						demodParameter.setStatus(demod_status_enum.transfer);
					} else if(demodParameter.getStatus().equals(demod_status_enum.stop) 
						|| demodParameter.getStatus().equals(demod_status_enum.stopped) 
						|| demodParameter.isMainClientOverTime()){
						/** 状态为需要停止，则通知设备不用上传数据了;
						 *  超时时限需要在开启检测时设置
						 */
						System.out.println(reportStatus.getDevice_num() + " 通知设备停止解调！");
						demodParameter.setStatus(demod_status_enum.stopped);
						respMsg.setDemod_code(Response.resp_demod_code_stop);//32
					}
				} else {
					respMsg.setDemod_code(Response.resp_demod_code_stop);//32
				}
			}
			break;
			
			case ReportStatus.report_code_normal:{ //代码20，设备正常报告状态
				if(DataPoolTools.deviceDemodParameterMap.containsKey(reportStatus.getDevice_num())
					&& DataPoolTools.authorizedDeviceNumSet.contains(reportStatus.getDevice_num())){
					if(DataPoolTools.deviceDemodParameterMap.get(reportStatus.getDevice_num()).getStatus().equals(demod_status_enum.transfer) ||
						DataPoolTools.deviceDemodParameterMap.get(reportStatus.getDevice_num()).getStatus().equals(demod_status_enum.start)){
						respMsg.setDemod_code(Response.resp_demod_code_normal);//10
					} else {
						respMsg.setDemod_code(Response.resp_demod_code_stop);//32
					}
					
				} else {
					respMsg.setDemod_code(Response.resp_demod_code_stop);//32
				}
			}
			break;
		}
		
		return respMsg;
	}
	
	//上传实时监测数据
	@ResponseBody
	@RequestMapping("/sendRealTimeData")
	public Response sendRealTimeData(ReportSpectrum reportSpec, HttpServletResponse response){
		
		Response respMsg = new Response();
		respMsg.setId(reportSpec.getDevice_num());
		
		/** 
		 * 要存储实时数据，就需要在开启监测的时候创建monitorDeviceMap,monitorDataPool和monitorDataToFilePool
		 */
		if(DataPoolTools.deviceMonitorParameterMap.containsKey(reportSpec.getDevice_num())
			&& DataPoolTools.authorizedDeviceNumSet.contains(reportSpec.getDevice_num())) {
			//获取用户设定的设备参数
			if(DataPoolTools.deviceMonitorParameterMap.get(reportSpec.getDevice_num()).getStatus().equals(monitor_status_enum.transfer)){
				DataPoolTools.deviceMonitorParameterMap.get(reportSpec.getDevice_num()).setLastPopDataTime(System.currentTimeMillis());
				//将频谱数据保存至数据池中
				double LNG = DataPoolTools.monitorReportStatusMap.get(reportSpec.getDevice_num()).getLongitude();
				double LAT = DataPoolTools.monitorReportStatusMap.get(reportSpec.getDevice_num()).getLatitude();
				reportSpec.addSpectrumToPool(LNG, LAT);
				
				//long nowTime = System.currentTimeMillis();
				//System.out.println(GTime.getTime(GTime.YYYYMMDDhhmmss)+"  "+ request.getId()+ " 上传数据后缓冲池数量： "+queue.size() +" 距离上次上传数据时间间隔毫秒数：" + (nowTime - lastTime));
				//lastTime = nowTime;
				respMsg.setCommon_code(Response.resp_common_code_normal);
			} else {
				respMsg.setCommon_code(Response.resp_common_code_status_error);
			}
		} else {
			//监控设备池已经没有这个监控设备了，则通知设备停止上传
			respMsg.setCommon_code(Response.resp_common_code_vacant_error);
		}
		
		return respMsg;
	}
	
	
	//上报解调数据
	@ResponseBody
	@RequestMapping("/sendDemodulationResult")
	public Response sendDemodulationResult(ReportDemodulation reportDemod, HttpServletResponse response) throws IOException{
		
		Response respMsg = new Response();
		respMsg.setId(reportDemod.getDevice_num());
		
		/** 
		 * 要存储解调数据，就需要在开启解调监测的时候创建demodDeviceMap,demodulateDataPool和demodulateDataToFilePool
		 */
		if(DataPoolTools.deviceDemodParameterMap.containsKey(reportDemod.getDevice_num())
			&& DataPoolTools.authorizedDeviceNumSet.contains(reportDemod.getDevice_num())) {
			//获取用户设定的解调参数
			if(DataPoolTools.deviceDemodParameterMap.get(reportDemod.getDevice_num()).getStatus().equals(demod_status_enum.transfer)){//transfer状态在reportStatus方法中设置
				DataPoolTools.deviceDemodParameterMap.get(reportDemod.getDevice_num()).setLastPopDataTime(System.currentTimeMillis());
				//将解调数据保存到缓冲池
				reportDemod.addDemodResultToPool(DataPoolTools.deviceDemodParameterMap.get(reportDemod.getDevice_num()));
				
				respMsg.setCommon_code(Response.resp_common_code_normal);
			} else {
				respMsg.setCommon_code(Response.resp_common_code_status_error);
			}
		} else {
			//监控设备池已经没有这个监控设备了，则通知设备停止上传
			respMsg.setCommon_code(Response.resp_common_code_vacant_error);
		}
		
		return respMsg;
	}
}
