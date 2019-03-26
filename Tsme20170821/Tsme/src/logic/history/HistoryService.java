package logic.history;

import java.util.List;
import java.util.Map;

import logic.data.bean.BeanCoordinate;
import mvc.data.ShowDemodResult;
import mvc.history.DeviceInfo;
import mvc.history.TemplateDeviceFB;
import tsme.table.deviceWT.bean.DEVICEWT;
import tsme.table.originalData.bean.ORIGINALDATA;
import tsme.table.originalDemod.bean.ORIGINALDEMOD;

public interface HistoryService {
	
	public List<DEVICEWT> getWarningTemplateListByDeviceNum(String deviceNum);
	
	public Map<String, List<String>> getDataDeviceFBIdMapByDeviceNum(String deviceNum, Map<String, List<ORIGINALDATA>> deviceFBIdOriginalDataMap);
	
	public List<TemplateDeviceFB> getTemplateDeviceFBMapByDeviceFBIds(String[] deviceFBIds);
	
	public Map<String, List<ORIGINALDATA>> getFrequencyFileMapByTemplateId(String deviceTemplateId, String date);
	
	public boolean getWarningLineAndPointList(String deviceTemplateId, float startFrequency, float stopFrequency, List<List<Float>> warningline, List<BeanCoordinate> warningPointList);
	
	public List<ORIGINALDEMOD> getDemodulationPointByOriginalDataFilePath(String filePath, List<List<Float>> demodulationPointList);
	
	public Map<Float, List<ShowDemodResult>> getShowDemodResultMapByOriginalDataFilePath(String filePath, List<List<Float>> statisticPointList);
	
	public List<DeviceInfo> getDeviceInfoToShowHistory();
	
	public String getOriginalDataIdByOriginalDataFilePath(String filePath);
	
	public String getOriginalAlarmFilePathByOriginalDataFilePath(String filePath);
}
