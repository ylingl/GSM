package logic.data;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import logic.data.bean.BeanCoordinate;
import mvc.data.BaseAndPeakRow;
import mvc.data.WarningTemplateRecPara;
import mvc.device.DemodResultParam;
import mvc.device.DemodResultStatic;
import mvc.history.DeviceInfo;
import tsme.table.originalData.bean.ORIGINALDATA;
import tsme.table.trainingData.bean.TRAININGDATA;
import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;

public interface DataAnalysisService {
	
	public WARNINGTEMPLATE createWarningTemplate(WarningTemplateRecPara warningTemplateRecPara, HttpSession httpSession);
	
	/**
	 * 将上报的js的数据导入数据库，用来计算均值
	 * @param fileName
	 * @return
	 */
	public int trainingData(String warningTemplateId);

	/**
	 * 生成均差，写入均差表，找到均线中的极大值点，保存极大值点
	 */
	public void createWaringLine(int baseRowNum, int peakRowNum, String warningTemplateId);
	
	public List<WARNINGTEMPLATE> getWarningTemplateIdAndNameListByDeviceNum(String deviceNum);
	
	public List<WARNINGTEMPLATE> getWarningTemplateWithFrequencyBandListByDeviceNum(String deviceNum);
	
	/**
	 * @param row
	 * @return
	 */
	public Map<String, List<List<Float>>> getAvgExtreme(String warningTemplateId, int row);
	
	public Map<String, List<List<Float>>> getDemodulationPointForChart(String warningTemplateId, int row);
	
	public List<DemodResultParam> getDemodResultParam(String warningTemplateId, int row);
	
	/**
	 * 获取均值线
	 * @param row
	 * @return
	 */
	public Map<String, List<List<List<Float>>>> getAvgDataList(String warningTemplateId,int row);
	
	/**
	 * 
	 * @param warningTemplateId
	 * @param frequencyPointMap
	 * @param frequencyLineMap
	 * @return
	 */
	public boolean findWarningLineDataByWarningTemplateId(String warningTemplateId, Map<String, List<BeanCoordinate>> frequencyPointMap, Map<String, List<List<Float>>> frequencyLineMap);
	
	public BaseAndPeakRow getBaseAndPeakRow(String warningTemplateId);
	
	public List<List<Float>> updateWarningLine(String warningTemplateId, float startFrequency, float stopFrequency, float threshold, String groupNum);

	public boolean deleteWarningTemplate(String warningTemplateId);

	/**
	 * 保存原始数据
	 * @param originalData
	 * @return
	 */
	public int saveOriginalData(ORIGINALDATA originalData);
	
	public int saveTrainingData(TRAININGDATA trainingData);
	
	public void batchSaveDemodulationResult(List<DemodResultStatic> demodResultStaticList);
	
	public boolean saveDemodulationFilePath(String file_Path, int index, String demodulationPointId);
	
	public boolean deleteDemodResultWithFileByDemodulationPointIdList(List<String> demodulationPointIdList);

	public List<DeviceInfo> getDeviceInfoToShowTemplate();
	
	public boolean canWarningTemplateBeModify(String warningTemplateId);
}

