package logic.spectra;

import java.util.List;
import java.util.Map;

import logic.data.bean.BeanCoordinate;
import mvc.device.DemodResultParam;
import mvc.device.DemodResultStatic;
import tsme.table.deviceWT.bean.DEVICEWT;
import tsme.table.originalData.bean.ORIGINALDATA;
import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;

public interface SpectraService {
	
	public DEVICEWT copyWarningTemplate(WARNINGTEMPLATE warningTemplate, Map<String, List<BeanCoordinate>> warningPointMap, Map<String, List<List<Float>>> demodulationPointMap);

	public int saveOriginalData(ORIGINALDATA originalData);
	
	public List<DemodResultParam> getDemodResultParam(String deviceWTId);
	
	public boolean saveOriginalDemodDataFilePath(String filePath, float frequencyPoint, String originalDataId);

	public void batchSaveDemodulationResult(List<DemodResultStatic> demodResultStaticList, String demodOriginalFilePath);
	
	public boolean saveOriginalAlarmDataFilePath(String filePath, String originalDataId);
}
