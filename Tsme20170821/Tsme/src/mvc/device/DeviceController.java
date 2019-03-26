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
 * �ͻ��˴�������ʹ��
 * @author kitty
 */
@Controller
public class DeviceController {
	
	@Autowired
	@Qualifier("deviceDAO")
	private DeviceDAO deviceDAO;
	
	//�豸����
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
	
	//�ϱ�״̬
	@ResponseBody
	@RequestMapping("/reportStatus")
	public Response reportStatus(ReportStatus reportStatus, HttpServletRequest request, HttpServletResponse response){
		
		Response respMsg = new Response();
		respMsg.setId(reportStatus.getDevice_num());
		
		reportStatus.setIpAddress(AddressTools.getRemoteIPAddr(request));
		reportStatus.setUpdateTime(System.currentTimeMillis());
		DataPoolTools.monitorReportStatusMap.put(reportStatus.getDevice_num(), reportStatus);
		
		switch(reportStatus.getMonitor_code()){
			case ReportStatus.report_code_req_monitorParam:{ //����21,�豸�����������Ƶ�׼�����
				if(DataPoolTools.deviceMonitorParameterMap.containsKey(reportStatus.getDevice_num())
					&& DataPoolTools.authorizedDeviceNumSet.contains(reportStatus.getDevice_num())){
					//��ȡ�û��趨���豸����
					MonitorParameter monitorParameter = DataPoolTools.deviceMonitorParameterMap.get(reportStatus.getDevice_num());
					//�豸������������û��趨�Ĳ���
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
						
						//System.out.println(reportStatus.getDevice_num() + " ֪ͨ�豸�ϴ�����");
						
						monitorParameter.setStatus(monitor_status_enum.transfer);
						
					} else if(monitorParameter.getStatus().equals(monitor_status_enum.stop) 
						|| monitorParameter.getStatus().equals(monitor_status_enum.stopped) 
						|| monitorParameter.isMainClientOverTime()){
						/** ״̬Ϊ��Ҫֹͣ����֪ͨ�豸�����ϴ�������;
						 *  ��ʱʱ����Ҫ�ڿ������ʱ����
						 */
						System.out.println(reportStatus.getDevice_num() + " ֪ͨ�豸ֹͣɨ�裡");
						monitorParameter.setStatus(monitor_status_enum.stopped);
						respMsg.setMonitor_code(Response.resp_monitor_code_stop);//12
					}
				} else {
					respMsg.setMonitor_code(Response.resp_monitor_code_stop);//12
				}
			}
			break;
			
			case ReportStatus.report_code_normal:{ //����20���豸��������״̬
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
			case ReportStatus.report_code_req_demodParam: { //����22,�豸�����������������
				if(DataPoolTools.deviceDemodParameterMap.containsKey(reportStatus.getDevice_num())
					&& DataPoolTools.authorizedDeviceNumSet.contains(reportStatus.getDevice_num())){
					//��ȡ�û��趨�Ľ������
					DemodParameter demodParameter = DataPoolTools.deviceDemodParameterMap.get(reportStatus.getDevice_num());
					//�豸������������û��趨�Ĳ���
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
								
						//System.out.println(reportStatus.getDevice_num() + " ֪ͨ�豸�ϴ�����");
							
						demodParameter.setStatus(demod_status_enum.transfer);
					} else if(demodParameter.getStatus().equals(demod_status_enum.stop) 
						|| demodParameter.getStatus().equals(demod_status_enum.stopped) 
						|| demodParameter.isMainClientOverTime()){
						/** ״̬Ϊ��Ҫֹͣ����֪ͨ�豸�����ϴ�������;
						 *  ��ʱʱ����Ҫ�ڿ������ʱ����
						 */
						System.out.println(reportStatus.getDevice_num() + " ֪ͨ�豸ֹͣ�����");
						demodParameter.setStatus(demod_status_enum.stopped);
						respMsg.setDemod_code(Response.resp_demod_code_stop);//32
					}
				} else {
					respMsg.setDemod_code(Response.resp_demod_code_stop);//32
				}
			}
			break;
			
			case ReportStatus.report_code_normal:{ //����20���豸��������״̬
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
	
	//�ϴ�ʵʱ�������
	@ResponseBody
	@RequestMapping("/sendRealTimeData")
	public Response sendRealTimeData(ReportSpectrum reportSpec, HttpServletResponse response){
		
		Response respMsg = new Response();
		respMsg.setId(reportSpec.getDevice_num());
		
		/** 
		 * Ҫ�洢ʵʱ���ݣ�����Ҫ�ڿ�������ʱ�򴴽�monitorDeviceMap,monitorDataPool��monitorDataToFilePool
		 */
		if(DataPoolTools.deviceMonitorParameterMap.containsKey(reportSpec.getDevice_num())
			&& DataPoolTools.authorizedDeviceNumSet.contains(reportSpec.getDevice_num())) {
			//��ȡ�û��趨���豸����
			if(DataPoolTools.deviceMonitorParameterMap.get(reportSpec.getDevice_num()).getStatus().equals(monitor_status_enum.transfer)){
				DataPoolTools.deviceMonitorParameterMap.get(reportSpec.getDevice_num()).setLastPopDataTime(System.currentTimeMillis());
				//��Ƶ�����ݱ��������ݳ���
				double LNG = DataPoolTools.monitorReportStatusMap.get(reportSpec.getDevice_num()).getLongitude();
				double LAT = DataPoolTools.monitorReportStatusMap.get(reportSpec.getDevice_num()).getLatitude();
				reportSpec.addSpectrumToPool(LNG, LAT);
				
				//long nowTime = System.currentTimeMillis();
				//System.out.println(GTime.getTime(GTime.YYYYMMDDhhmmss)+"  "+ request.getId()+ " �ϴ����ݺ󻺳�������� "+queue.size() +" �����ϴ��ϴ�����ʱ������������" + (nowTime - lastTime));
				//lastTime = nowTime;
				respMsg.setCommon_code(Response.resp_common_code_normal);
			} else {
				respMsg.setCommon_code(Response.resp_common_code_status_error);
			}
		} else {
			//����豸���Ѿ�û���������豸�ˣ���֪ͨ�豸ֹͣ�ϴ�
			respMsg.setCommon_code(Response.resp_common_code_vacant_error);
		}
		
		return respMsg;
	}
	
	
	//�ϱ��������
	@ResponseBody
	@RequestMapping("/sendDemodulationResult")
	public Response sendDemodulationResult(ReportDemodulation reportDemod, HttpServletResponse response) throws IOException{
		
		Response respMsg = new Response();
		respMsg.setId(reportDemod.getDevice_num());
		
		/** 
		 * Ҫ�洢������ݣ�����Ҫ�ڿ����������ʱ�򴴽�demodDeviceMap,demodulateDataPool��demodulateDataToFilePool
		 */
		if(DataPoolTools.deviceDemodParameterMap.containsKey(reportDemod.getDevice_num())
			&& DataPoolTools.authorizedDeviceNumSet.contains(reportDemod.getDevice_num())) {
			//��ȡ�û��趨�Ľ������
			if(DataPoolTools.deviceDemodParameterMap.get(reportDemod.getDevice_num()).getStatus().equals(demod_status_enum.transfer)){//transfer״̬��reportStatus����������
				DataPoolTools.deviceDemodParameterMap.get(reportDemod.getDevice_num()).setLastPopDataTime(System.currentTimeMillis());
				//��������ݱ��浽�����
				reportDemod.addDemodResultToPool(DataPoolTools.deviceDemodParameterMap.get(reportDemod.getDevice_num()));
				
				respMsg.setCommon_code(Response.resp_common_code_normal);
			} else {
				respMsg.setCommon_code(Response.resp_common_code_status_error);
			}
		} else {
			//����豸���Ѿ�û���������豸�ˣ���֪ͨ�豸ֹͣ�ϴ�
			respMsg.setCommon_code(Response.resp_common_code_vacant_error);
		}
		
		return respMsg;
	}
}
