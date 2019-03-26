package logic.history;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import logic.data.bean.BeanCoordinate;
import mvc.data.ShowDemodResult;
import mvc.history.DeviceInfo;
import mvc.history.TemplateDeviceFB;
import tsme.table.account.DAO.AccountDAO;
import tsme.table.device.DAO.DeviceDAO;
import tsme.table.deviceDR.DAO.DeviceDRDAO;
import tsme.table.deviceDR.bean.DEVICEDR;
import tsme.table.deviceFB.DAO.DeviceFBDAO;
import tsme.table.deviceFB.bean.DEVICEFB;
import tsme.table.deviceWL.DAO.DeviceWLDAO;
import tsme.table.deviceWT.DAO.DeviceWTDAO;
import tsme.table.deviceWT.bean.DEVICEWT;
import tsme.table.originalData.DAO.OriginalDataDAO;
import tsme.table.originalData.bean.ORIGINALDATA;
import tsme.table.originalDemod.DAO.OriginalDemodDAO;
import tsme.table.originalDemod.bean.ORIGINALDEMOD;
import utils.AccountTools;

@Service("historyService")
public class HistoryServiceImpl implements HistoryService{
	
	@Autowired
	@Qualifier("deviceDAO")
	private DeviceDAO deviceDAO;
	
	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
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
	@Qualifier("deviceDRDAO")
	private DeviceDRDAO deviceDRDAO;
	
	@Autowired
	@Qualifier("originalDataDAO")
	private OriginalDataDAO originalDataDAO;
	
	@Autowired
	@Qualifier("originalDemodDAO")
	private OriginalDemodDAO originalDemodDAO;
	
	@Override
	public List<DEVICEWT> getWarningTemplateListByDeviceNum(String deviceNum){			
		return new ArrayList<DEVICEWT>();
	}
	
	/**
	 * DEVICEWT仅有1层
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getDataDeviceFBIdMapByDeviceNum(String deviceNum, Map<String, List<ORIGINALDATA>> deviceFBIdOriginalDataMap){
		AccountTools accountTools = new AccountTools();
		String sql = "";
		
		if(accountTools.doesCurrentAccountHasSuperadminRole()) {
			//DEVICEWT仅有自己1层
			sql = "SELECT originalData.* FROM originalData, deviceFB, deviceWT, device WHERE "
				+ "device.device_num='" + deviceNum + "' AND device.id=deviceWT.device_id AND "
				+ "deviceWT.id=deviceFB.deviceWT_id AND deviceFB.id=originalData.deviceFB_id "
				+ "ORDER BY originalData.date DESC";
		} else if(accountTools.doesCurrentAccountHasAdminRole()) {
			sql = "SELECT originalData.* FROM originalData, deviceFB, deviceWT, device WHERE "
				+ "originalData.creator_id IN ( SELECT id FROM account WHERE department_id IN ( "
				+ "SELECT department_id FROM account WHERE id='" + accountTools.getCurrentAccountId() + "' ) ) AND "
				+ "device.device_num='" + deviceNum + "' AND device.id=deviceWT.device_id AND "
				+ "deviceWT.id=deviceFB.deviceWT_id AND deviceFB.id=originalData.deviceFB_id "
				+ "ORDER BY originalData.date DESC";
			
		} else {
			sql = "SELECT originalData.* FROM originalData, deviceFB, deviceWT, device WHERE "
				+ "originalData.creator_id='" + accountTools.getCurrentAccountId() + "' AND "
				+ "device.device_num='" + deviceNum + "' AND device.id=deviceWT.device_id AND "
				+ "deviceWT.id=deviceFB.deviceWT_id AND deviceFB.id=originalData.deviceFB_id "
				+ "ORDER BY originalData.date DESC";
		}
		
		List<ORIGINALDATA> originalDataList = (List<ORIGINALDATA>) originalDataDAO.findByQuery(sql, ORIGINALDATA.class);
		
		//去除重复的deviceFBId
		Map<String, List<String>> dateDeviceFBIdMap = new LinkedHashMap<String, List<String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(ORIGINALDATA originalData : originalDataList){
			if(deviceFBIdOriginalDataMap.containsKey(originalData.getDeviceFB_id())){
				deviceFBIdOriginalDataMap.get(originalData.getDeviceFB_id()).add(originalData);
			} else {
				List<ORIGINALDATA> tempList = new ArrayList<ORIGINALDATA>();
				tempList.add(originalData);
				deviceFBIdOriginalDataMap.put(originalData.getDeviceFB_id(), tempList);
			}
			
			String dataString = sdf.format(originalData.getDate());
			if(dateDeviceFBIdMap.containsKey(dataString)){
				//日期已出现过
				List<String> deviceFBIdList = dateDeviceFBIdMap.get(dataString);
				boolean putToken = true;
				//判断该模板是否已经存在于相同日期的dateTemplateMap中
				for(String temp : deviceFBIdList){
					if(temp.equalsIgnoreCase(originalData.getDeviceFB_id())){
						putToken = false;
						break;
					}
				}
				
				if(putToken){
					deviceFBIdList.add(originalData.getDeviceFB_id());
					dateDeviceFBIdMap.put(dataString, deviceFBIdList);
				}
			} else {
				//新日期，没出现过
				List<String> deviceFBIdList = new ArrayList<String>();
				deviceFBIdList.add(originalData.getDeviceFB_id());
				dateDeviceFBIdMap.put(dataString, deviceFBIdList);
			}
		}
		
		return dateDeviceFBIdMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<TemplateDeviceFB> getTemplateDeviceFBMapByDeviceFBIds(String[] deviceFBIds){
		List<TemplateDeviceFB> templateDeviceFBList = new ArrayList<TemplateDeviceFB>();
		
		String ids = "";
		for(String deviceFBId : deviceFBIds){
			ids = ids + "'" + deviceFBId + "',";
		}
		ids = ids.substring(0, ids.length() - 1);
		
		String sql = "SELECT distinct deviceWT.* FROM deviceWT, deviceFB WHERE deviceFB.id IN (" + ids + ") AND deviceWT.id=deviceFB.deviceWT_id ORDER BY create_time DESC";
		List<DEVICEWT> deviceWTList = (List<DEVICEWT>) deviceWTDAO.findByQuery(sql, DEVICEWT.class);
		
		for(DEVICEWT deviceWT : deviceWTList) {
			TemplateDeviceFB templateDeviceFB = new TemplateDeviceFB();
			
			sql = "SELECT * FROM deviceFB WHERE deviceWT_id='" + deviceWT.getId() + "' ORDER BY create_time DESC";
			List<DEVICEFB> deviceFBList = (List<DEVICEFB>) deviceFBDAO.findByQuery(sql, DEVICEFB.class);
		
			templateDeviceFB.setDeviceWT(deviceWT);
			templateDeviceFB.setDeviceFBList(deviceFBList);
			
			templateDeviceFBList.add(templateDeviceFB);
		}
		
		return templateDeviceFBList;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, List<ORIGINALDATA>> getFrequencyFileMapByTemplateId(String deviceTemplateId, String date){
		Map<String, List<ORIGINALDATA>> frequencyFileMap = new LinkedHashMap<String, List<ORIGINALDATA>>();
		
		String sql = "SELECT * FROM deviceFB WHERE deviceWT_id = '" + deviceTemplateId + "' AND active = 1";
		List<DEVICEFB> deviceFBList = (List<DEVICEFB>) deviceFBDAO.findByQuery(sql, DEVICEFB.class);
		
		for(DEVICEFB deviceFB : deviceFBList){
			String key = deviceFB.getStartFrequency() + "-" + deviceFB.getStopFrequency();
			
			sql = "SELECT * FROM originalData WHERE deviceFB_id = '" + deviceFB.getId() + "' AND active=1 AND DATE(date)='" + date + "' ORDER BY create_time DESC";
			List<ORIGINALDATA> originalDataList = (List<ORIGINALDATA>) originalDataDAO.findByQuery(sql, ORIGINALDATA.class);
			
			frequencyFileMap.put(key, originalDataList);
		}
		
		return frequencyFileMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean getWarningLineAndPointList(String deviceTemplateId, float startFrequency, float stopFrequency, List<List<Float>> warningline, List<BeanCoordinate> warningPointList){
		String sql = "SELECT deviceWL.x, deviceWL.y FROM deviceWL, deviceFB WHERE deviceWL.active=1 AND deviceWL.deviceFB_id=deviceFB.id "
				+ "AND deviceFB.deviceWT_id='" + deviceTemplateId + "' AND deviceFB.startFrequency=" + startFrequency + " AND deviceFB.stopFrequency=" + stopFrequency + " AND deviceFB.active=1 ORDER BY deviceWL.x";
		
		List<BeanCoordinate> warningPointListTemp = (List<BeanCoordinate>) deviceWLDAO.findByQuery(sql, BeanCoordinate.class);
		for (Iterator<BeanCoordinate> iterator = warningPointListTemp.iterator(); iterator.hasNext();) {	
			BeanCoordinate be = (BeanCoordinate) iterator.next();
			
			List<Float> tempPoint = new ArrayList<Float>();	
			tempPoint.add(be.getX());
			tempPoint.add(be.getY());
			
			warningline.add(tempPoint);
			warningPointList.add(be);
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<ORIGINALDEMOD> getDemodulationPointByOriginalDataFilePath(String filePath, List<List<Float>> demodulationPointList){
		filePath = filePath.replaceAll("\\\\", "\\\\\\\\");
		String sql = "SELECT originalDemod.* FROM originalDemod, originalData WHERE originalData.file_path='" + filePath + "' AND originalDemod.originalData_id=originalData.id "
				+ "AND originalDemod.active=1 AND originalData.active=1 ORDER BY originalDemod.frequencyPoint";
		
		List<ORIGINALDEMOD> originalDemodList = (List<ORIGINALDEMOD>) originalDemodDAO.findByQuery(sql, ORIGINALDEMOD.class);
		
		for (Iterator<ORIGINALDEMOD> iterator = originalDemodList.iterator(); iterator.hasNext();) {
			List<Float> tempPoint = new ArrayList<Float>();
			ORIGINALDEMOD originalDemod = iterator.next();
			
			tempPoint.add(originalDemod.getFrequencyPoint());
			tempPoint.add((float) -140.0);//用于控制柱体高度
			tempPoint.add((float) -150.0);//默认为-150.0
			
			demodulationPointList.add(tempPoint);
		}
		
		return originalDemodList;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Float, List<ShowDemodResult>> getShowDemodResultMapByOriginalDataFilePath(String filePath, List<List<Float>> statisticPointList){
		Map<Float, List<ShowDemodResult>> showDemodResultMap = new HashMap<Float, List<ShowDemodResult>>();
	
		filePath = filePath.replaceAll("\\\\", "\\\\\\\\");
		String sql = "SELECT originalDemod.* FROM originalDemod, originalData WHERE originalData.file_path='" + filePath + "' AND originalDemod.originalData_id=originalData.id "
				+ "AND originalDemod.active=1 AND originalData.active=1 ORDER BY originalDemod.frequencyPoint";
		
		List<ORIGINALDEMOD> originalDemodList = (List<ORIGINALDEMOD>) originalDemodDAO.findByQuery(sql, ORIGINALDEMOD.class);
		
		for (Iterator<ORIGINALDEMOD> iterator = originalDemodList.iterator(); iterator.hasNext();) {
			ORIGINALDEMOD originalDemod = iterator.next();
			
			sql = "SELECT * FROM deviceDR WHERE deviceDR.originalDemod_id='" + originalDemod.getId() + "' AND active=1";
			List<DEVICEDR> deviceDRList = (List<DEVICEDR>) deviceDRDAO.findByQuery(sql, DEVICEDR.class);
		
			List<ShowDemodResult> showDemodResultList = new ArrayList<ShowDemodResult>();
			for(DEVICEDR temp : deviceDRList) {
				ShowDemodResult showDemodResult = new ShowDemodResult();
				
				showDemodResult.setCiValue(temp.getCIValue());
				showDemodResult.setCount(temp.getCICount());
				showDemodResult.setIndex(temp.getGroupNum());
				showDemodResult.setIndicatorOfSCHInfo(temp.getIndicatorOfSCHInfo());
				showDemodResult.setLocationAreaCode(temp.getLocationAreaCode());
				showDemodResult.setMobileCountryCode(temp.getMobileCountryCode());
				showDemodResult.setMobileNetworkCode(temp.getMobileNetworkCode());
				showDemodResult.setPduType(temp.getPduType());
				showDemodResult.setRaColour(temp.getRAColour());
				showDemodResult.setSi13Position(temp.getSI13Position());
				showDemodResult.setX(originalDemod.getFrequencyPoint());
				
				BigDecimal b = new BigDecimal(temp.getAvgCTI()/100);
				showDemodResult.setAvgCI(b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue());
				
				showDemodResultList.add(showDemodResult);
				
				List<Float> tempPoint = new ArrayList<Float>();
				
				tempPoint.add(originalDemod.getFrequencyPoint());
				tempPoint.add((float) -140.0);//用于控制柱体高度
				tempPoint.add((float) -150.0);//默认为-150.0
				
				statisticPointList.add(tempPoint);
			}
			
			showDemodResultMap.put(originalDemod.getFrequencyPoint(), showDemodResultList);
		}
		
		return showDemodResultMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceInfo> getDeviceInfoToShowHistory(){
		List<DeviceInfo> deviceInfoList = new ArrayList<DeviceInfo>();
		AccountTools accountTools = new AccountTools();
		String sql = "";
		if(accountTools.doesCurrentAccountHasSuperadminRole()){
			sql = "SELECT device.id AS id, device.device_num AS deviceNum, device.device_type AS deviceType, "
				+ "device.LNG AS LNG, device.LAT AS LAT FROM originalData, deviceFB, deviceWT, device WHERE "
				+ "device.id=deviceWT.device_id AND deviceWT.id=deviceFB.deviceWT_id AND deviceFB.id=originalData.deviceFB_id "
				+ "GROUP BY device.id ORDER BY deviceNum ASC";
		} else if(accountTools.doesCurrentAccountHasAdminRole()){
			sql = "SELECT device.id AS id, device.device_num AS deviceNum, device.device_type AS deviceType, "
				+ "device.LNG AS LNG, device.LAT AS LAT FROM originalData, deviceFB, deviceWT, device WHERE "
				+ "originalData.creator_id IN ( SELECT id FROM account WHERE department_id IN ( "
				+ "SELECT department_id FROM account WHERE id='" + accountTools.getCurrentAccountId() + "' ) ) AND "
				+ "device.id=deviceWT.device_id AND deviceWT.id=deviceFB.deviceWT_id AND deviceFB.id=originalData.deviceFB_id "
				+ "GROUP BY device.id ORDER BY deviceNum ASC";
		} else {
			sql = "SELECT device.id AS id, device.device_num AS deviceNum, device.device_type AS deviceType, "
				+ "device.LNG AS LNG, device.LAT AS LAT FROM originalData, deviceFB, deviceWT, device WHERE "
				+ "originalData.creator_id='" + accountTools.getCurrentAccountId() + "' AND "
				+ "device.id=deviceWT.device_id AND deviceWT.id=deviceFB.deviceWT_id AND deviceFB.id=originalData.deviceFB_id "
				+ "GROUP BY device.id ORDER BY deviceNum ASC";
		}
		
		deviceInfoList = (List<DeviceInfo>) deviceDAO.findByQuery(sql, DeviceInfo.class);
		
		return deviceInfoList;
	}
	
	@SuppressWarnings("unchecked")
	public String getOriginalDataIdByOriginalDataFilePath(String filePath){
		filePath = filePath.replaceAll("\\\\", "\\\\\\\\");
		
		String sql = "SELECT id FROM originalData WHERE file_path='" + filePath + "' AND active=1";
		List<String> idList = (List<String>) originalDataDAO.findByQueryForList(sql, String.class);
		
		if(idList != null && !idList.isEmpty()){
			return idList.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String getOriginalAlarmFilePathByOriginalDataFilePath(String filePath){
		filePath = filePath.replaceAll("\\\\", "\\\\\\\\");
		
		String sql = "SELECT originalAlarm.file_path FROM originalAlarm, originalData WHERE originalData.file_path='" + filePath 
					+ "' AND originalData.id=originalAlarm.originalData_id AND originalAlarm.active=1";
		List<String> filePathList = (List<String>) originalDataDAO.findByQueryForList(sql, String.class);
		
		if(filePathList != null && !filePathList.isEmpty()){
			return filePathList.get(0);
		}
		
		return "";
	}
}
