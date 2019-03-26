package logic.spectra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import logic.data.bean.BeanCoordinate;
import mvc.device.DemodResultParam;
import mvc.device.DemodResultStatic;
import tsme.table.deviceDP.DAO.DeviceDPDAO;
import tsme.table.deviceDP.bean.DEVICEDP;
import tsme.table.deviceDR.DAO.DeviceDRDAO;
import tsme.table.deviceDR.bean.DEVICEDR;
import tsme.table.deviceFB.DAO.DeviceFBDAO;
import tsme.table.deviceFB.bean.DEVICEFB;
import tsme.table.deviceWL.DAO.DeviceWLDAO;
import tsme.table.deviceWT.DAO.DeviceWTDAO;
import tsme.table.deviceWT.bean.DEVICEWT;
import tsme.table.frequencyBand.bean.FREQUENCYBAND;
import tsme.table.originalAlarm.DAO.OriginalAlarmDAO;
import tsme.table.originalAlarm.bean.ORIGINALALARM;
import tsme.table.originalData.DAO.OriginalDataDAO;
import tsme.table.originalData.bean.ORIGINALDATA;
import tsme.table.originalDemod.DAO.OriginalDemodDAO;
import tsme.table.originalDemod.bean.ORIGINALDEMOD;
import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;

@Service("spectraService")
public class SpectraServiceImpl implements SpectraService{
	
	@Autowired
	@Qualifier("deviceWTDAO")
	private DeviceWTDAO deviceWTDAO;
	
	@Autowired
	@Qualifier("deviceFBDAO")
	private DeviceFBDAO deviceFBDAO;
	
	@Autowired
	@Qualifier("deviceWLDAO")
	private DeviceWLDAO deviceWLDAO;
	
	@Autowired
	@Qualifier("deviceDPDAO")
	private DeviceDPDAO deviceDPDAO;
	
	@Autowired
	@Qualifier("deviceDRDAO")
	private DeviceDRDAO deviceDRDAO;
	
	@Autowired
	@Qualifier("originalDataDAO")
	private OriginalDataDAO originalDataDAO;
	
	@Autowired
	@Qualifier("originalDemodDAO")
	private OriginalDemodDAO originalDemodDAO;
	
	@Autowired
	@Qualifier("originalAlarmDAO")
	private OriginalAlarmDAO originalAlarmDAO;

	@SuppressWarnings("unchecked")
	public DEVICEWT copyWarningTemplate(WARNINGTEMPLATE warningTemplate, Map<String, List<BeanCoordinate>> warningPointMap, Map<String, List<List<Float>>> demodulationPointMap) {
		
		DEVICEWT deviceWT = new DEVICEWT();
		List<DEVICEWT> deviceWTList = new ArrayList<DEVICEWT>();
		
		String sql = "SELECT * FROM deviceWT WHERE template_id='" + warningTemplate.getId() + "'";
		deviceWTList = (List<DEVICEWT>) deviceWTDAO.findByQuery(sql, DEVICEWT.class);
		
		if(deviceWTList.isEmpty()){
			//已存在则不保存
			deviceWT.setBandWidth(warningTemplate.getBandWidth());
			deviceWT.setDevice_id(warningTemplate.getDevice_id());
			deviceWT.setFftSize(warningTemplate.getFftSize());
			deviceWT.setLAT(warningTemplate.getLAT());
			deviceWT.setLNG(warningTemplate.getLNG());
			deviceWT.setMaxMeans(warningTemplate.getMaxMeans());
			deviceWT.setTemplate_id(warningTemplate.getId());
			deviceWT.setTemplate_name(warningTemplate.getTemplate_name());
			deviceWT.setActive(true);
			deviceWT.setCreate_time(System.currentTimeMillis());
			
			deviceWTDAO.save(deviceWT);//保存模板
			
			List<DEVICEFB> deviceFBList = new ArrayList<DEVICEFB>();
			for(FREQUENCYBAND frequencyBand : warningTemplate.getFrequencyBandList()){
				
				DEVICEFB deviceFB = new DEVICEFB();
				
				deviceFB.setDeviceWT_id(deviceWT.getId());
				deviceFB.setStartFrequency(frequencyBand.getStartFrequency());
				deviceFB.setStopFrequency(frequencyBand.getStopFrequency());
				deviceFB.setActive(true);
				deviceFB.setCreate_time(System.currentTimeMillis());
				
				deviceFBDAO.save(deviceFB);//保存频段
				
				deviceFBList.add(deviceFB);
				
				StringBuilder sqlBuilder = new StringBuilder();
				sqlBuilder.append("INSERT INTO deviceWL (id, deviceFB_id, x, y, active, create_time) VALUES");
				
				for(BeanCoordinate entry : warningPointMap.get(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency())){
					sqlBuilder.append(" ('").append(UUID.randomUUID()).append("', ");
					sqlBuilder.append("'").append(deviceFB.getId()).append("', ");
					
					sqlBuilder.append("'").append(entry.getX()).append("', ");
					sqlBuilder.append("'").append(entry.getY()).append("', ");
					
					sqlBuilder.append("'").append(1).append("', ");
					sqlBuilder.append("'").append(System.currentTimeMillis()).append("'),");
				}
				
				sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
				deviceWLDAO.executeBySql(sqlBuilder.toString());//保存预警曲线
				
				sqlBuilder.delete(0, sqlBuilder.length());
				sqlBuilder.append("INSERT INTO deviceDP (id, deviceFB_id, x, active, create_time) VALUES");
				
				for(List<Float> temp : demodulationPointMap.get(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency())){
					sqlBuilder.append(" ('").append(UUID.randomUUID()).append("', ");
					sqlBuilder.append("'").append(deviceFB.getId()).append("', ");
					
					sqlBuilder.append("'").append(temp.get(0)).append("', ");
					
					sqlBuilder.append("'").append(1).append("', ");
					sqlBuilder.append("'").append(System.currentTimeMillis()).append("'),");
				}
				
				sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
				deviceDPDAO.executeBySql(sqlBuilder.toString());//保存待解调点
			}
			
			deviceWT.setDeviceFBList(deviceFBList);
			
		} else {
			deviceWT = deviceWTList.get(0);
			
			sql = "SELECT * FROM deviceFB WHERE deviceWT_id='" + deviceWT.getId() + "'";
			
			deviceWT.setDeviceFBList((List<DEVICEFB>) deviceFBDAO.findByQuery(sql, DEVICEFB.class));
		}
		
		return deviceWT;
	}
	
	public int saveOriginalData(ORIGINALDATA originalData){
		return originalDataDAO.save(originalData);
	}
	
	@SuppressWarnings("unchecked")
	public List<DemodResultParam> getDemodResultParam(String deviceWTId){
		String sql = "SELECT * FROM deviceFB WHERE deviceWT_id='" + deviceWTId + "' AND active = 1 ORDER BY startFrequency";
		List<DEVICEFB> deviceFBList = (List<DEVICEFB>) deviceFBDAO.findByQuery(sql, DEVICEFB.class);
		
		List<DemodResultParam> demodResultParamList = new ArrayList<DemodResultParam>();
		Integer index = 0;
		for(DEVICEFB deviceFB : deviceFBList) {
			sql = "SELECT id, x FROM deviceDP WHERE deviceFB_id='" + deviceFB.getId() + "' ORDER BY x";
			List<DEVICEDP> pointList = (List<DEVICEDP>) deviceDPDAO.findByQuery(sql, DEVICEDP.class);		
			
			for(DEVICEDP point : pointList){
				DemodResultParam demodResultParam = new DemodResultParam();
				demodResultParam.setDemodulationPointId(point.getId());
				demodResultParam.setX(point.getX());
				demodResultParam.setIndex(index);
				demodResultParam.setFrequencyBand(deviceFB.getStartFrequency() + "-" + deviceFB.getStopFrequency());
				
				demodResultParamList.add(demodResultParam);
				
				index ++;
			}
		}
		
		return demodResultParamList;
	}
	
	public boolean saveOriginalDemodDataFilePath(String filePath, float frequencyPoint, String originalDataId){
		String sql = "SELECT * FROM originalDemod WHERE originalData_id='" + originalDataId + "' AND file_path='" + filePath + "'";
		long num = -1;
		num = originalDemodDAO.getTotalItemsNumBySelectQuery(sql);
		if(num == 0){
			ORIGINALDEMOD originalDemod = new ORIGINALDEMOD();
			
			originalDemod.setOriginalData_id(originalDataId);
			originalDemod.setFrequencyPoint(frequencyPoint);
			originalDemod.setFile_path(filePath);
			originalDemod.setActive(true);
			originalDemod.setCreate_time(System.currentTimeMillis());
	
			originalDemodDAO.save(originalDemod);
			
			return true;
		}
		
		if(num > 0){
			return true;
		} else {
			return false;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void batchSaveDemodulationResult(List<DemodResultStatic> demodResultStaticList, String demodOriginalFilePath){
		demodOriginalFilePath = demodOriginalFilePath.replaceAll("\\\\", "\\\\\\\\");
		
		String sql = "SELECT originalDemod.* FROM originalDemod, originalData WHERE originalData.file_Path='" + demodOriginalFilePath + "' "
					+ "AND originalDemod.originalData_id=originalData.id AND originalData.active=1 AND originalDemod.active=1";
		
		List<ORIGINALDEMOD> originalDemodList = (List<ORIGINALDEMOD>) originalDemodDAO.findByQuery(sql, ORIGINALDEMOD.class);
		
		if(demodResultStaticList != null && !demodResultStaticList.isEmpty()){
			for(DemodResultStatic temp : demodResultStaticList) {
				for(ORIGINALDEMOD originalDemod : originalDemodList) {
					if(temp.getX() == originalDemod.getFrequencyPoint()){
						insert(temp, originalDemod.getId());
						continue;
					}
				}
			}
		}
	}
	
	private void insert(DemodResultStatic temp, String originalDemodId){
		DEVICEDR deviceDR = new DEVICEDR();
		
		deviceDR.setOriginalDemod_id(originalDemodId);
		deviceDR.setGroupNum(temp.getIndex());
		deviceDR.setPduType(temp.getPduType());
		deviceDR.setIndicatorOfSCHInfo(temp.getIndicatorOfSCHInfo());
		
		float avgCTI = (float) 0.0;
		if(!temp.getCarrierToInterferenceList().isEmpty()){
			for(float CTI : temp.getCarrierToInterferenceList()){
				avgCTI = avgCTI + CTI;
			}
			avgCTI = avgCTI/temp.getCarrierToInterferenceList().size();
		}
		
		deviceDR.setAvgCTI(avgCTI);
		deviceDR.setCIValue(temp.getCIValue());
		deviceDR.setCICount(temp.getCount());
		deviceDR.setMobileCountryCode(temp.getMobileCountryCode());
		deviceDR.setMobileNetworkCode(temp.getMobileNetworkCode());
		deviceDR.setLocationAreaCode(temp.getLocationAreaCode());
		deviceDR.setRAColour(temp.getRAColour());
		deviceDR.setSI13Position(temp.getSI13Position());		
		deviceDR.setActive(true);
		deviceDR.setCreate_time(System.currentTimeMillis());
		
		deviceDRDAO.save(deviceDR);
	}
	
	@SuppressWarnings("unused")
	private void update(DEVICEDR deviceDR, DemodResultStatic temp){
		deviceDR.setPduType(temp.getPduType());
		deviceDR.setIndicatorOfSCHInfo(temp.getIndicatorOfSCHInfo());
		
		float avgCTI = (float) 0.0;
		if(!temp.getCarrierToInterferenceList().isEmpty()){
			for(float CTI : temp.getCarrierToInterferenceList()){
				avgCTI = avgCTI + CTI;
			}
			avgCTI = avgCTI/temp.getCarrierToInterferenceList().size();
		}
		
		deviceDR.setAvgCTI(avgCTI);
		deviceDR.setCIValue(temp.getCIValue());
		deviceDR.setCICount(temp.getCount());
		deviceDR.setMobileCountryCode(temp.getMobileCountryCode());
		deviceDR.setMobileNetworkCode(temp.getMobileNetworkCode());
		deviceDR.setLocationAreaCode(temp.getLocationAreaCode());
		deviceDR.setRAColour(temp.getRAColour());
		deviceDR.setSI13Position(temp.getSI13Position());
		deviceDR.setActive(true);
		deviceDR.setCreate_time(System.currentTimeMillis());
		
		deviceDRDAO.update(deviceDR);
	}
	
	public boolean saveOriginalAlarmDataFilePath(String filePath, String originalDataId){
		String sql = "SELECT * FROM originalAlarm WHERE originalData_id='" + originalDataId + "' AND file_path='" + filePath + "'";
		long num = -1;
		num = originalAlarmDAO.getTotalItemsNumBySelectQuery(sql);
		if(num == 0){
			ORIGINALALARM originalAlarm = new ORIGINALALARM();
			
			originalAlarm.setOriginalData_id(originalDataId);
			originalAlarm.setFile_path(filePath);
			originalAlarm.setActive(true);
			originalAlarm.setCreate_time(System.currentTimeMillis());
	
			originalAlarmDAO.save(originalAlarm);
			
			return true;
		}
		
		if(num > 0){
			return true;
		} else {
			return false;
		}
	}
}
