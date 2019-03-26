package logic.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import Jama.Matrix;
import logic.data.bean.BeanCoordinate;
import mvc.data.BaseAndPeakRow;
import mvc.data.WarningTemplateRecPara;
import mvc.device.DemodResultParam;
import mvc.device.DemodResultStatic;
import mvc.history.DeviceInfo;
import tsme.table.avgData.DAO.AvgDataDAO;
import tsme.table.avgExtreme.DAO.AvgExtremeDAO;
import tsme.table.demodulationPoint.DAO.DemodulationPointDAO;
import tsme.table.demodulationPoint.bean.DEMODULATIONPOINT;
import tsme.table.demodulationResult.DAO.DemodulationResultDAO;
import tsme.table.demodulationResult.bean.DEMODULATIONRESULT;
import tsme.table.device.DAO.DeviceDAO;
import tsme.table.deviceWT.DAO.DeviceWTDAO;
import tsme.table.frequencyBand.DAO.FrequencyBandDAO;
import tsme.table.frequencyBand.bean.FREQUENCYBAND;
import tsme.table.originalData.DAO.OriginalDataDAO;
import tsme.table.originalData.bean.ORIGINALDATA;
import tsme.table.trainingData.DAO.TrainingDataDAO;
import tsme.table.trainingData.bean.TRAININGDATA;
import tsme.table.warningLine.DAO.WarningLineDAO;
import tsme.table.warningTemplate.DAO.WarningTemplateDAO;
import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;
import utils.AccountTools;
import utils.DataPoolTools;

@Service("dataAnalysisService")
public class DataAnalysisServiceImpl implements DataAnalysisService {
	
	private static int num = 10;//在一定范围内，num值越大，局部峰值找的越准，极值点的个数越少。
	private static float ratio = (float) 0.05;//极差取比
	
	@Autowired
	@Qualifier("warningTemplateDAO")
	private WarningTemplateDAO warningTemplateDAO;
	
	@Autowired
	@Qualifier("frequencyBandDAO")
	private FrequencyBandDAO frequencyBandDAO;
	
	@Autowired
	@Qualifier("originalDataDAO")
	private OriginalDataDAO originalDataDAO;
	
	@Autowired
	@Qualifier("trainingDataDAO")
	private TrainingDataDAO trainingDataDAO;

	@Autowired
	@Qualifier("avgDataDAO")
	private AvgDataDAO avgDataDAO;

	@Autowired
	@Qualifier("warningLineDAO")
	private WarningLineDAO warningLineDAO;
	
	@Autowired
	@Qualifier("avgExtremeDAO")
	private AvgExtremeDAO avgExtremeDAO;
	
	@Autowired
	@Qualifier("demodulationPointDAO")
	private DemodulationPointDAO demodulationPointDAO;
	
	@Autowired
	@Qualifier("demodulationResultDAO")
	private DemodulationResultDAO demodulationResultDAO;
	
	@Autowired
	@Qualifier("deviceDAO")
	private DeviceDAO deviceDAO;
	
	@Autowired
	@Qualifier("deviceWTDAO")
	private DeviceWTDAO deviceWTDAO;
	
	public WARNINGTEMPLATE createWarningTemplate(WarningTemplateRecPara warningTemplateRecPara, HttpSession httpSession) {
		String sql = "SELECT id FROM device WHERE device_num='" + httpSession.getAttribute("currentDeviceNum") + "'";
		String deviceId = (String) deviceDAO.findByQueryForList(sql, String.class).get(0);
		
		AccountTools accountTools = new AccountTools();
		
		WARNINGTEMPLATE warningTemplate = new WARNINGTEMPLATE();
		
		warningTemplate.setDevice_id(deviceId);
		warningTemplate.setCreator_id(accountTools.getCurrentAccountId());
		warningTemplate.setTemplate_name(warningTemplateRecPara.getTemplate_name());
		warningTemplate.setFftSize(warningTemplateRecPara.getFftSize());
		warningTemplate.setLAT(DataPoolTools.monitorReportStatusMap.get(httpSession.getAttribute("currentDeviceNum")).getLatitude());
		warningTemplate.setLNG(DataPoolTools.monitorReportStatusMap.get(httpSession.getAttribute("currentDeviceNum")).getLongitude());
		warningTemplate.setMaxMeans(warningTemplateRecPara.getMaxMeans());
		warningTemplate.setBandWidth(warningTemplateRecPara.getBandWidth());
		warningTemplate.setActive(true);
		warningTemplate.setCreate_time(System.currentTimeMillis());
		
		warningTemplateDAO.save(warningTemplate);
		
		List<FREQUENCYBAND> frequencyBandList = new ArrayList<FREQUENCYBAND>();
		
		if(warningTemplateRecPara.getStopFrequency0() > 0) {
			FREQUENCYBAND frequencyBand = new FREQUENCYBAND();
			
			frequencyBand.setWarningTemplate_id(warningTemplate.getId());
			frequencyBand.setStartFrequency(warningTemplateRecPara.getStartFrequency0());
			frequencyBand.setStopFrequency(warningTemplateRecPara.getStopFrequency0());
			frequencyBand.setActive(true);
			frequencyBand.setCreate_time(System.currentTimeMillis());
			
			frequencyBandList.add(frequencyBand);
		}
		
		if(warningTemplateRecPara.getStopFrequency1() > 0) {
			FREQUENCYBAND frequencyBand = new FREQUENCYBAND();
			
			frequencyBand.setWarningTemplate_id(warningTemplate.getId());
			frequencyBand.setStartFrequency(warningTemplateRecPara.getStartFrequency1());
			frequencyBand.setStopFrequency(warningTemplateRecPara.getStopFrequency1());
			frequencyBand.setActive(true);
			frequencyBand.setCreate_time(System.currentTimeMillis());
			
			frequencyBandList.add(frequencyBand);
		}
		
		if(warningTemplateRecPara.getStopFrequency2() > 0) {
			FREQUENCYBAND frequencyBand = new FREQUENCYBAND();
			
			frequencyBand.setWarningTemplate_id(warningTemplate.getId());
			frequencyBand.setStartFrequency(warningTemplateRecPara.getStartFrequency2());
			frequencyBand.setStopFrequency(warningTemplateRecPara.getStopFrequency2());
			frequencyBand.setActive(true);
			frequencyBand.setCreate_time(System.currentTimeMillis());
			
			frequencyBandList.add(frequencyBand);
		}
		
		frequencyBandDAO.batchSave(frequencyBandList);
		
		warningTemplate.setFrequencyBandList(frequencyBandList);
		
		return warningTemplate;
	}
	
	@Override
	public int saveOriginalData(ORIGINALDATA originalData){
		return originalDataDAO.save(originalData);
	}
	
	@Override
	public int saveTrainingData(TRAININGDATA trainingData){
		return trainingDataDAO.save(trainingData);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteWarningTemplate(String warningTemplateId) {
		String sql = "SELECT id FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "'";
		
		List<String> frequencyBandIdList = (List<String>) frequencyBandDAO.findByQueryForList(sql, String.class);
		
		String ids = "";
		for(String frequencyBandId : frequencyBandIdList){
			ids = ids + "'" + frequencyBandId + "',";
		}
		ids = ids.substring(0, ids.length() - 1);
		
		sql = "DELETE FROM avgData WHERE frequencyBand_id IN (" + ids + ")";
		avgDataDAO.executeBySql(sql);
		
		sql = "DELETE FROM avgExtreme WHERE frequencyBand_id IN (" + ids + ")";
		avgExtremeDAO.executeBySql(sql);
		
		sql = "DELETE FROM warningLine WHERE frequencyBand_id IN (" + ids + ")";
		warningLineDAO.executeBySql(sql);
		
		sql = "DELETE FROM trainingData WHERE frequencyBand_id IN (" + ids + ")";
		trainingDataDAO.executeBySql(sql);
		
		
		sql = "DELETE FROM demodulationResult WHERE demodulationPoint_id IN ("
				+ "SELECT id FROM demodulationPoint WHERE frequencyBand_id IN (" + ids + ")"
				+ " )";
		demodulationResultDAO.executeBySql(sql);
		
		sql = "DELETE FROM demodulationPoint WHERE frequencyBand_id IN (" + ids + ")";
		demodulationPointDAO.executeBySql(sql);
		
		sql = "DELETE FROM frequencyBand WHERE id IN (" + ids + ")";
		frequencyBandDAO.executeBySql(sql);
		
		sql = "DELETE FROM warningTemplate WHERE id='" + warningTemplateId + "'";
		warningTemplateDAO.executeBySql(sql);

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int trainingData(String warningTemplateId) {
		String sql = "SELECT * FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active = 1 ORDER BY create_time";		
		List<FREQUENCYBAND> frequencyBandList = (List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class);
		
		for(FREQUENCYBAND frequencyBand : frequencyBandList) {
			//删除旧数据
			sql = "DELETE FROM avgData WHERE frequencyBand_id='" + frequencyBand.getId() + "'";
			avgDataDAO.executeBySql(sql);
			
			//读取原始数据
			sql = "SELECT file_path FROM trainingData WHERE frequencyBand_id='" + frequencyBand.getId() + "' AND active = 1 ORDER BY create_time";
			List<String> filePathList = (List<String>) trainingDataDAO.findByQueryForList(sql, String.class);
			
			if(filePathList.size() == 0){
				return -1;
			}
			
			Map<String, ArrayList<Matrix>> avgMap = new HashMap<String, ArrayList<Matrix>>();
			avgMap.put("avg1", new ArrayList<Matrix>());
			avgMap.put("avg2", new ArrayList<Matrix>());
			avgMap.put("avg3", new ArrayList<Matrix>());
			avgMap.put("avg4", new ArrayList<Matrix>());
			avgMap.put("avg5", new ArrayList<Matrix>());
			avgMap.put("avg6", new ArrayList<Matrix>());
			avgMap.put("avg7", new ArrayList<Matrix>());
			avgMap.put("avg8", new ArrayList<Matrix>());
			avgMap.put("avg9", new ArrayList<Matrix>());
			
			
			for(String filePath : filePathList) {
				File file = new File(filePath);
				BufferedReader reader = null;
				int rowNum = 0;
				try {
					reader = new BufferedReader(new FileReader(file));
					List<String> row = new ArrayList<String>();
					String tempString = null;
					//读文件并计算行数
					while ((tempString = reader.readLine()) != null ) {
						if (tempString.startsWith("[") && tempString.endsWith("],")){
							row.add(tempString.substring(1, tempString.length() - 2));
							rowNum ++;
						} else if (tempString.startsWith("[") && tempString.endsWith("]")) {
							row.add(tempString.substring(1, tempString.length() - 1));
							rowNum ++;
						}
					}
					
					reader.close();
					
					double[][] orignalData = new double[rowNum][row.get(0).split(",").length - 3];
					
					int i = 0;
					for(String temp : row) {	
						String[] data = temp.split(",");
						
						for(int j = 0; j < data.length - 3; j ++){//最后一个数记录了数据产生时间，倒数第二个是纬度，倒数第三个是经度
							orignalData[i][j] = Double.valueOf(data[j]);
						}
						
						i ++;
					}
					
					int columnNum = orignalData[0].length;
					Matrix dataMatrix = new Matrix(orignalData);
					
					//WARNINGTEMPLATE warningTemplate = warningTemplateDAO.findBothById(warningTemplateId);
					
					Matrix avg1 = calculateAvgData(dataMatrix, new Matrix(1, columnNum), 0, rowNum, columnNum);
					//saveAvgData(1, avg1, warningTemplate);
					avgMap.get("avg1").add(avg1);
					
					Matrix avg2 = calculateAvgData(dataMatrix, avg1, 12, rowNum, columnNum);
					//saveAvgData(2, avg2, warningTemplate);
					avgMap.get("avg2").add(avg2);
					
					Matrix avg3 = calculateAvgData(dataMatrix, avg2, 12, rowNum, columnNum);
					//saveAvgData(3, avg3, warningTemplate);
					avgMap.get("avg3").add(avg3);
					
					Matrix avg4 = calculateAvgData(dataMatrix, avg3, 15, rowNum, columnNum);
					//saveAvgData(4, avg4, warningTemplate);
					avgMap.get("avg4").add(avg4);
					
					Matrix avg5 = calculateAvgData(dataMatrix, avg4, 30, rowNum, columnNum);
					//saveAvgData(5, avg5, warningTemplate);
					avgMap.get("avg5").add(avg5);
					
					Matrix avg6 = calculateAvgData(dataMatrix, avg5, 30, rowNum, columnNum);
					//saveAvgData(6, avg6, warningTemplate);
					avgMap.get("avg6").add(avg6);
					
					Matrix avg7 = calculateAvgData(dataMatrix, avg6, 30, rowNum, columnNum);
					//saveAvgData(7, avg7, warningTemplate);
					avgMap.get("avg7").add(avg7);
					
					Matrix avg8 = calculateAvgData(dataMatrix, avg7, 30, rowNum, columnNum);
					//saveAvgData(8, avg8, warningTemplate);
					avgMap.get("avg8").add(avg8);
					
					Matrix avg9 = calculateAvgData(dataMatrix, avg8, 30, rowNum, columnNum);
					//saveAvgData(9, avg9, warningTemplate);
					avgMap.get("avg9").add(avg9);

				} catch (IOException e) {
					
					e.printStackTrace();
					
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e1) {
							
						}
					}
				}
			}
			
			for(String key : avgMap.keySet()){
				ArrayList<Matrix> avgMatrixList = avgMap.get(key);
				Matrix temp = new Matrix(1, avgMatrixList.get(0).getColumnDimension(), 0);
				for(Matrix avg : avgMatrixList){
					temp = temp.plus(avg);
				}
				
				temp = temp.times(1/(double)avgMatrixList.size());
				
				List<BeanCoordinate> avgList = new ArrayList<BeanCoordinate>();
				
				switch (key) {
				case "avg1" : 
					avgList = saveAvgData(1, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(1, frequencyBand.getId(), avgList);
					break;
				case "avg2" :
					avgList = saveAvgData(2, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(2, frequencyBand.getId(), avgList);
					break;
				case "avg3" : 
					avgList = saveAvgData(3, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(3, frequencyBand.getId(), avgList);
					break;
				case "avg4" :
					avgList = saveAvgData(4, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(4, frequencyBand.getId(), avgList);
					break;
				case "avg5" : 
					avgList = saveAvgData(5, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(5, frequencyBand.getId(), avgList);
					break;
				case "avg6" :
					avgList = saveAvgData(6, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(6, frequencyBand.getId(), avgList);
					break;
				case "avg7" :
					avgList = saveAvgData(7, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(7, frequencyBand.getId(), avgList);
					break;
				case "avg8" : 
					avgList = saveAvgData(8, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(8, frequencyBand.getId(), avgList);
					break;
				case "avg9" :
					avgList = saveAvgData(9, temp, frequencyBand.getId(), frequencyBand.getStartFrequency(), frequencyBand.getStopFrequency());
					createAvgExtreme(9, frequencyBand.getId(), avgList);
					break;
				}
			}
		}

		return frequencyBandList.size();
	}

	private Matrix calculateAvgData(Matrix dataMatrix, Matrix avgRow, double limit, int rowNum, int columnNum) {
		
		if(avgRow.times(new Matrix(columnNum, 1, 1)).get(0, 0) == 0) {
			//计算一次均值
			Matrix A = new Matrix(1, rowNum, 1/(double)rowNum);
			Matrix B = A.times(dataMatrix);
			
			return B;
		} 
		else {
			int[] weight = new int[columnNum];
			
			Matrix tempMatrix = new Matrix(rowNum, columnNum);
			
			for(int j = 0; j < columnNum; j ++) {
				int pointNum = 0;
				
				for(int i = 0; i < rowNum; i ++) {
					if(dataMatrix.get(i, j) >= avgRow.get(0, j) && dataMatrix.get(i, j) < avgRow.get(0, j) + limit){
						tempMatrix.set(i, j, dataMatrix.get(i, j));
						pointNum ++;
					}
				}
				
				weight[j] = pointNum;
			}
			
			Matrix A = new Matrix(1, rowNum, 1);
			Matrix B = A.times(tempMatrix);
			
			for(int j = 0; j < columnNum; j ++) {
				
				if(B.get(0, j) != 0){
					B.set(0, j, B.get(0, j)/(double)weight[j]);
				}
				else{
					B.set(0, j, avgRow.get(0, j) + limit/(double)2);//对限定区间内，"无点"情况的处理方法
				}
				
			}
			
			return B;
		}		
	}
	
	private List<BeanCoordinate> saveAvgData(int rowNum, Matrix avgRow, String frequencyBandId, float startFrequency, float stopFrequency) {
		
		double avgData[][] = avgRow.getArray();
		
		List<BeanCoordinate> avgList = new ArrayList<BeanCoordinate>();
		
		float bandwidth = stopFrequency - startFrequency;
		float step = bandwidth / (avgData[0].length - 1);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO avgData (id, frequencyBand_id, row, x, y, active, create_time) VALUES");
		for(int j = 0; j < avgData[0].length; j ++){
			BeanCoordinate temp = new BeanCoordinate();
			sql.append(" ('").append(UUID.randomUUID()).append("', ");
			sql.append("'").append(frequencyBandId).append("', ");
			sql.append("'").append(rowNum).append("', ");
			
			BigDecimal a = new BigDecimal(startFrequency + j * step);
			sql.append("'").append(a.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue()).append("', ");
			temp.setX(a.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue());
			
			BigDecimal b = new BigDecimal(avgData[0][j]);
			sql.append("'").append(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()).append("', ");
			temp.setY(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			
			sql.append("'").append(1).append("', ");
			sql.append("'").append(System.currentTimeMillis()).append("'),");
			
			avgList.add(temp);
		}
		sql.deleteCharAt(sql.length()-1);
		
		avgDataDAO.executeBySql(sql.toString());
		
		return avgList;
	}
	
	/**
	 * 计算第N次均值的极值点：规则就是这个点的前后至少三个点比其大或小。
	 * @param row
	 */
	private void createAvgExtreme(int rowNum, String frequencyBandId, List<BeanCoordinate> avgList) {
		//删除旧数据
		String sql = "DELETE FROM avgExtreme WHERE frequencyBand_id='" + frequencyBandId + "' AND row='" + rowNum + "'";
		avgExtremeDAO.executeBySql(sql);
		
		boolean trendMax;//是否向极大值发展
		boolean trendMin;//是否有变小的趋势
		short trendMinCount = 0;
		float maxYTemp = 0;
		float minYTemp = 0;
		List<BeanCoordinate> maxExtremeList = new ArrayList<BeanCoordinate>();
		
		BeanCoordinate maxExtreme = new BeanCoordinate();
		
		//DATAEXTREME minExtreme = new DATAEXTREME();
		//List<DATAEXTREME> minExtremes = new LinkedList<DATAEXTREME>();
		
		//根据前两个的点初始化数据
		if(avgList.get(0).getY() <= avgList.get(1).getY()){
			trendMax = true;
			trendMin = false;
			maxYTemp = avgList.get(1).getY();
			minYTemp = avgList.get(0).getY();
		} else {
			trendMax = false;
			trendMin = true;
			minYTemp = avgList.get(1).getY();
			maxYTemp = avgList.get(0).getY();
			trendMinCount = 1;
			//在初始变小趋势下，上一个点位为临时极大值点
			maxExtreme.setX(avgList.get(0).getX());
			maxExtreme.setY(avgList.get(0).getY());
		}
		
		for (int i = 2; i < avgList.size(); i++) {
			BeanCoordinate object = avgList.get(i);
			/*if(rowNum == 9 && object.getY() == (float) -72.89 && frequencyBandId.equalsIgnoreCase("c9a0df51-217f-4ce2-86e5-be5915473cc6")){
				trendMin = true;
			}*/
			if(trendMax) {
				if(object.getY() >= maxYTemp){//当前点大于等于上一个点，延续变大趋势
					trendMax = true;
					trendMin = false;
					trendMinCount = 0;
					
					maxYTemp = object.getY();
				} else {//当前点比上一个点小，标志着趋势开始变小，则上一点为极大值点
					trendMax = false;
					trendMin = true;
					trendMinCount = 1;
					
					//在初始变小趋势下，上一个点位为临时极大值点
					maxExtreme.setX(avgList.get(i-1).getX());
					maxExtreme.setY(avgList.get(i-1).getY());
					
					minYTemp = object.getY();
				}
			} else {
				if(object.getY() < minYTemp){//当前点小于等于上一点，延续变小趋势。
					minYTemp = object.getY();
					if(trendMinCount >= num && !maxExtremeList.contains(maxExtreme)){
						//表示已经出现num+1个连续下滑点。如果已经有连续两个下滑点，则表明已出现所需的极大值。
						BeanCoordinate temp = new BeanCoordinate();

						temp.setX(maxExtreme.getX());
						temp.setY(maxExtreme.getY());
						
						maxExtremeList.add(temp);
						
						trendMinCount ++;
					} else {
						trendMinCount ++;
					}
				} else {//当前点比上一个点大，标志着趋势开始变大，则上一点为极小值点
					float gainAdjust = (minYTemp - maxExtreme.getY()) * ratio;//ratio越大，调整后的门限值越低，但门限值过低会导致丢失极值点。
					
					if(trendMinCount <= num){//在一定范围内，num值越大，局部峰值找的越准，极值点的个数越少。
						if(object.getY() < (maxExtreme.getY() + gainAdjust)){//减小增益值(改变值=原值+极差*ratio)，是为了去除不必要的抖动。
							trendMax = false;		
							trendMin = true;
							
							trendMinCount ++;
						} else {
							trendMax = true;		
							trendMin = false;
							trendMinCount = 0;
							
							maxYTemp = object.getY();
						}
					} else {//已有连续num+1个点小于maxExtreme.getY()
						trendMax = true;		
						trendMin = false;
						trendMinCount = 0;
						
						BeanCoordinate temp = new BeanCoordinate();

						temp.setX(maxExtreme.getX());
						temp.setY(maxExtreme.getY());
						
						maxExtremeList.add(temp);
						
						maxYTemp = object.getY();
					}
					
					//minExtreme.setX(list.get(i-1).getX());
					//minExtreme.setY(list.get(i-1).getY());
					//minExtremes.add(minExtreme);
				}
				
			}
			
		}
		//avgExtremeDAO.batchSave(maxExtremeList);
		saveAvgExtreme(rowNum, maxExtremeList, frequencyBandId);
		createDemodulationPoint(rowNum, maxExtremeList, frequencyBandId);
	}
	
	private boolean saveAvgExtreme(int rowNum, List<BeanCoordinate> maxExtremeList, String frequencyBandId) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO avgExtreme (id, frequencyBand_id, row, x, y, active, create_time) VALUES");
		for(BeanCoordinate temp : maxExtremeList){
			sql.append(" ('").append(UUID.randomUUID()).append("', ");
			sql.append("'").append(frequencyBandId).append("', ");
			sql.append("'").append(rowNum).append("', ");
			sql.append("'").append(temp.getX()).append("', ");
			
			BigDecimal b = new BigDecimal(temp.getY());
			sql.append("'").append(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()).append("', ");
			
			sql.append("'").append(1).append("', ");
			sql.append("'").append(System.currentTimeMillis()).append("'),");
		}
		sql.deleteCharAt(sql.length()-1);
		
		avgExtremeDAO.executeBySql(sql.toString());
		
		return true;
	}
	
	/**
	 * 通过至极点计算出待解调频点
	 * @param warningTemplateId, row
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void createDemodulationPoint(int rowNum, List<BeanCoordinate> maxExtremeList, String frequencyBandId){
		//删除旧数据
		String sql = "SELECT id FROM demodulationPoint WHERE frequencyBand_id='" + frequencyBandId + "' AND row='" + rowNum + "'";
		List<String> demodulationPointIdList = (List<String>) demodulationPointDAO.findByQueryForList(sql, String.class);
		
		if(!demodulationPointIdList.isEmpty()){
			StringBuilder idSql = new StringBuilder();
			for(String id : demodulationPointIdList) {
				idSql.append("'").append(id).append("',");
			}
			idSql.deleteCharAt(idSql.length() - 1);
			
			sql = "DELETE FROM demodulationResult WHERE demodulationPoint_id IN (" + idSql + ")";
			demodulationResultDAO.executeBySql(sql);
			
			sql = "DELETE FROM demodulationPoint WHERE frequencyBand_id='" + frequencyBandId + "' AND row='" + rowNum + "'";
			demodulationPointDAO.executeBySql(sql);
		}
		
		//计算待解调频点
		List<Float> demodulationFrequencyList = new ArrayList<Float>();
		for (Iterator<BeanCoordinate> iterator = maxExtremeList.iterator(); iterator.hasNext();) {
			BeanCoordinate be = (BeanCoordinate) iterator.next();
			
			if(be.getY() >= -95.0){
				float integer = (int) be.getX();
				float decimal = be.getX() - integer;
				if(decimal <= 0.1 && !demodulationFrequencyList.contains((float) integer)){
					demodulationFrequencyList.add((float) integer);
				}
				if(0.1 < decimal && decimal <= 0.3 && !demodulationFrequencyList.contains((float) (integer + 0.2))){
					demodulationFrequencyList.add((float) (integer + 0.2));
				}
				if(0.3 < decimal && decimal <= 0.5 && !demodulationFrequencyList.contains((float) (integer + 0.4))){
					demodulationFrequencyList.add((float) (integer + 0.4));
				}
				if(0.5 < decimal && decimal <= 0.7 && !demodulationFrequencyList.contains((float) (integer + 0.6))){
					demodulationFrequencyList.add((float) (integer + 0.6));
				}
				if(0.7 < decimal && decimal <= 0.9 && !demodulationFrequencyList.contains((float) (integer + 0.8))){
					demodulationFrequencyList.add((float) (integer + 0.8));
				}
				if(0.9 < decimal && !demodulationFrequencyList.contains((float) (integer + 1.0))){
					demodulationFrequencyList.add((float) (integer + 1.0));
				}
			}
		}
		
		saveDemodulationPoint(rowNum, demodulationFrequencyList, frequencyBandId);
	}
	
	private boolean saveDemodulationPoint(int rowNum, List<Float> demodulationFrequencyList, String frequencyBandId) {
		if(!demodulationFrequencyList.isEmpty()){
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO demodulationPoint (id, frequencyBand_id, row, x, active, create_time) VALUES");
			
			for(Float temp : demodulationFrequencyList){
				sql.append(" ('").append(UUID.randomUUID()).append("', ");
				sql.append("'").append(frequencyBandId).append("', ");
				sql.append("'").append(rowNum).append("', ");
				sql.append("'").append(temp).append("', ");
				sql.append("'").append(1).append("', ");
				sql.append("'").append(System.currentTimeMillis()).append("'),");
			}
			sql.deleteCharAt(sql.length()-1);
			
			demodulationPointDAO.executeBySql(sql.toString());
			
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public void createWaringLine(int baseRowNum, int peakRowNum, String warningTemplateId) {
		String sql = "SELECT * FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active = 1 ORDER BY create_time";		
		List<FREQUENCYBAND> frequencyBandList = (List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class);
		
		for(FREQUENCYBAND frequencyBand : frequencyBandList){
			//删除老数据
			sql = "DELETE FROM warningLine WHERE frequencyBand_id='" + frequencyBand.getId() + "'";
			warningLineDAO.executeBySql(sql);
			//取出均值
			sql = "SELECT x,y FROM avgData WHERE row=" + baseRowNum + " AND frequencyBand_id='" + frequencyBand.getId() + "' ORDER BY x";
			List<BeanCoordinate> baseRow = (List<BeanCoordinate>) avgDataDAO.findByQuery(sql, BeanCoordinate.class);
			//取出极值
			sql = "SELECT x,y FROM avgExtreme where row=" + peakRowNum + " AND frequencyBand_id='" + frequencyBand.getId() + "' ORDER BY x";
			List<BeanCoordinate> peakRowExtreme = (List<BeanCoordinate>) avgExtremeDAO.findByQuery(sql, BeanCoordinate.class);
			
			Iterator<BeanCoordinate> iterator = baseRow.iterator();
			double difference = 0;
			for(BeanCoordinate temp : peakRowExtreme){
				while (iterator.hasNext()) {
					
					BeanCoordinate ba = (BeanCoordinate) iterator.next();
					
					if(temp.getX() == ba.getX()) {
						difference = difference + temp.getY() - ba.getY();		
						break;
					}
				}
			}
			
			difference = difference/(double)peakRowExtreme.size();
			
			BigDecimal b = new BigDecimal(difference);
			
			saveWarningLine(baseRow, baseRowNum, peakRowNum, b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(), frequencyBand.getId());
		}
		
	}

	private boolean saveWarningLine(List<BeanCoordinate> baseRow, int baseRowNum, int peakRowNum, float difference, String frequencyBandId) {	
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO warningLine (id, frequencyBand_id, baseRow, peakRow, x, y, active, create_time) VALUES");
		for(BeanCoordinate temp : baseRow){
			sql.append(" ('").append(UUID.randomUUID()).append("', ");
			sql.append("'").append(frequencyBandId).append("', ");
			sql.append("'").append(baseRowNum).append("', ");
			sql.append("'").append(peakRowNum).append("', ");
			sql.append("'").append(temp.getX()).append("', ");
			
			BigDecimal b = new BigDecimal(temp.getY() + difference);
			sql.append("'").append(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()).append("', ");
			
			sql.append("'").append(1).append("', ");
			sql.append("'").append(System.currentTimeMillis()).append("'),");
		}
		sql.deleteCharAt(sql.length()-1);
		
		warningLineDAO.executeBySql(sql.toString());
		
		return true;
		
	}
	
	@SuppressWarnings({"unchecked"})
	@Override
	public Map<String, List<List<Float>>> getAvgExtreme(String warningTemplateId, int row) {
		
		String sql = "SELECT * FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active = 1 ORDER BY create_time";
		List<FREQUENCYBAND> frequencyBandList = (List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class);
		
		Map<String, List<List<Float>>> avgExtremeMap = new HashMap<String, List<List<Float>>>();
		for(FREQUENCYBAND frequencyBand : frequencyBandList) {
			sql = "SELECT x,y FROM avgExtreme WHERE row=" + row + " AND frequencyBand_id='" + frequencyBand.getId() + "' ORDER BY x";
			List<BeanCoordinate> pointList = (List<BeanCoordinate>) avgExtremeDAO.findByQuery(sql, BeanCoordinate.class);
			
			List<List<Float>> extremeList = new ArrayList<List<Float>>();
			for (Iterator<BeanCoordinate> iterator = pointList.iterator(); iterator.hasNext();) {
				List<Float> tempPoint = new ArrayList<Float>();
				BeanCoordinate be = (BeanCoordinate) iterator.next();
				
				tempPoint.add(be.getX());
				tempPoint.add(be.getY());
				
				extremeList.add(tempPoint);
			}
			
			avgExtremeMap.put(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency(), extremeList);
		}
		
		return avgExtremeMap;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<List<List<Float>>>> getAvgDataList(String warningTemplateId, int row) {
		String sql = "SELECT * FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active = 1 ORDER BY create_time";
		List<FREQUENCYBAND> frequencyBandList = (List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class);
		
		Map<String, List<List<List<Float>>>> spectrmMap = new HashMap<String, List<List<List<Float>>>>();
		
		for(FREQUENCYBAND frequencyBand : frequencyBandList) {
			
			List<List<List<Float>>> lists = new ArrayList<List<List<Float>>>();
			for (int i = 1; i <= row; i ++) {
				sql = "SELECT x,y FROM avgData WHERE frequencyBand_id='" + frequencyBand.getId() + "' and row=" + i + " ORDER BY x";
				List<BeanCoordinate> listRow = (List<BeanCoordinate>) avgDataDAO.findByQuery(sql, BeanCoordinate.class);
				
				List<List<Float>> listTemp = new ArrayList<List<Float>>();
				for (Iterator<BeanCoordinate> iterator = listRow.iterator(); iterator.hasNext();) {
					List<Float> tempPoint = new ArrayList<Float>();
					BeanCoordinate ba = (BeanCoordinate) iterator.next();
					
					tempPoint.add(ba.getX());
					tempPoint.add(ba.getY());
					
					listTemp.add(tempPoint);
				}
				
				lists.add(listTemp);
			}
			
			spectrmMap.put(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency(), lists);
			
		}
		
		return spectrmMap;
	}
	
	//已进行了sql优化
	@SuppressWarnings("unchecked")
	@Override
	public List<WARNINGTEMPLATE> getWarningTemplateIdAndNameListByDeviceNum(String deviceNum) {
		String sql = "SELECT warningTemplate.id, warningTemplate.template_name FROM warningTemplate, device WHERE warningTemplate.device_id=device.id AND warningTemplate.active=1 AND device.device_num='" + deviceNum + "' ORDER BY warningTemplate.create_time DESC";
		List<WARNINGTEMPLATE> list = (List<WARNINGTEMPLATE>)warningTemplateDAO.findByQuery(sql, WARNINGTEMPLATE.class);
		
		Iterator<WARNINGTEMPLATE> iter = list.iterator();
		while (iter.hasNext()) {
			WARNINGTEMPLATE warningTemplate = iter.next();
			
			sql = "SELECT warningLine.* FROM warningLine, frequencyBand WHERE warningLine.active=1 AND warningLine.frequencyBand_id=frequencyBand.id AND frequencyBand.warningTemplate_id='" + warningTemplate.getId() + "'";
			
			long lineNum = warningLineDAO.getTotalItemsNumBySelectQuery(sql);
			
			if(lineNum == 0){
				iter.remove();
			}
		}
		
		return list;
	}
	
	//已进行了sql优化
	@SuppressWarnings("unchecked")
	@Override
	public List<WARNINGTEMPLATE> getWarningTemplateWithFrequencyBandListByDeviceNum(String deviceNum) {
		AccountTools accountTools = new AccountTools();
		String sql = "";
		if(accountTools.doesCurrentAccountHasSuperadminRole()){
			sql = "SELECT warningTemplate.* FROM warningTemplate, device WHERE warningTemplate.device_id=device.id "
				+ "AND warningTemplate.active=1 AND device.device_num='" + deviceNum + "' ORDER BY warningTemplate.create_time DESC";
		} else if(accountTools.doesCurrentAccountHasAdminRole()){
			sql = "SELECT warningTemplate.* FROM warningTemplate, device WHERE warningTemplate.device_id=device.id "
				+ "AND warningTemplate.active=1 AND warningTemplate.creator_id IN ( SELECT id FROM account WHERE department_id IN ( "
				+ "SELECT department_id FROM account WHERE id='" + accountTools.getCurrentAccountId() + "' ) )"
				+ "AND device.device_num='" + deviceNum + "' ORDER BY warningTemplate.create_time DESC";
		} else {
			sql = "SELECT warningTemplate.* FROM warningTemplate, device WHERE warningTemplate.device_id=device.id "
				+ "AND warningTemplate.active=1 AND creator_id='" + accountTools.getCurrentAccountId() + "'"
				+ "AND device.device_num='" + deviceNum + "' ORDER BY warningTemplate.create_time DESC";
		}
		
		List<WARNINGTEMPLATE> warningTemplateList = (List<WARNINGTEMPLATE>) warningTemplateDAO.findByQuery(sql, WARNINGTEMPLATE.class);
		
		for(int i = 0; i < warningTemplateList.size(); i ++) {
			sql = "SELECT * FROM frequencyBand WHERE active=1 AND warningTemplate_id='" + warningTemplateList.get(i).getId() + "' ORDER BY startFrequency";
			warningTemplateList.get(i).setFrequencyBandList((List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class));
		}
		
		return warningTemplateList;
	}
	
	//已进行了sql优化
	@SuppressWarnings("unchecked")
	@Override
	public boolean findWarningLineDataByWarningTemplateId(String warningTemplateId, Map<String, List<BeanCoordinate>> frequencyPointMap, Map<String, List<List<Float>>> frequencyLineMap){
		if(frequencyPointMap == null && frequencyLineMap == null){
			return false;
		}
		
		String sql = "SELECT * FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active = 1 ORDER BY create_time";
		List<FREQUENCYBAND> frequencyBandList = (List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class);
		
		for(FREQUENCYBAND frequencyBand : frequencyBandList) {
			sql = "SELECT x,y FROM warningLine WHERE frequencyBand_Id='" + frequencyBand.getId() + "' ORDER BY x";
			List<BeanCoordinate> pointList = (List<BeanCoordinate>) warningLineDAO.findByQuery(sql, BeanCoordinate.class);
			
			if(frequencyPointMap != null){
				frequencyPointMap.put(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency(), pointList);
			}
			
			if(frequencyLineMap != null){
				List<List<Float>> line = new ArrayList<List<Float>>();
				for (Iterator<BeanCoordinate> iterator = pointList.iterator(); iterator.hasNext();) {	
					List<Float> tempPoint = new ArrayList<Float>();	
					BeanCoordinate be = (BeanCoordinate) iterator.next();
					
					tempPoint.add(be.getX());
					tempPoint.add(be.getY());
					
					line.add(tempPoint);
				}
				
				frequencyLineMap.put(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency(), line);
			}
		}
		
		return true;
	}
	
	//已进行了sql优化
	@SuppressWarnings("unchecked")
	@Override
	public BaseAndPeakRow getBaseAndPeakRow(String warningTemplateId) {
		String sql = "SELECT warningLine.baseRow, warningLine.peakRow FROM warningLine, frequencyBand WHERE " + 
						"warningLine.active=1 AND frequencyBand.active = 1 AND warningLine.frequencyBand_id=frequencyBand.id AND " + 
						"frequencyBand.warningTemplate_id='" + warningTemplateId + "' LIMIT 1";
		
		List<BaseAndPeakRow> bpRowList = (List<BaseAndPeakRow>) warningLineDAO.findByQuery(sql, BaseAndPeakRow.class);
		
		if(!bpRowList.isEmpty()) return bpRowList.get(0);
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<List<Float>> updateWarningLine(String warningTemplateId, float startFrequency, float stopFrequency, float threshold, String groupNum){
		String frequencyList[] = groupNum.split("-");
		String sql = "SELECT id FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active=1 AND startFrequency=" + frequencyList[0] + " AND stopFrequency=" + frequencyList[1];
		String frequencyBandId = (String) frequencyBandDAO.findByQueryForList(sql, String.class).get(0);
		
		sql = "UPDATE warningLine SET y=" + threshold + " WHERE active=1 AND frequencyBand_id='" + frequencyBandId + "' AND  x>=" + startFrequency + " AND x<=" + stopFrequency;
		warningLineDAO.executeBySql(sql);
		
		sql = "SELECT x,y FROM warningLine WHERE active=1 AND frequencyBand_id='" + frequencyBandId + "' ORDER BY x";
		List<BeanCoordinate> pointList = (List<BeanCoordinate>) warningLineDAO.findByQuery(sql, BeanCoordinate.class);
		
		List<List<Float>> line = new ArrayList<List<Float>>();
		for (Iterator<BeanCoordinate> iterator = pointList.iterator(); iterator.hasNext();) {
			List<Float> tempPoint = new ArrayList<Float>();
			
			BeanCoordinate be = (BeanCoordinate) iterator.next();
			
			tempPoint.add(be.getX());
			tempPoint.add(be.getY());
			
			line.add(tempPoint);
		}
		
		return line;	
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, List<List<Float>>> getDemodulationPointForChart(String warningTemplateId, int row) {
		
		String sql = "SELECT * FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active = 1 ORDER BY create_time";
		List<FREQUENCYBAND> frequencyBandList = (List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class);
		
		Map<String, List<List<Float>>> demodulationPointMap = new HashMap<String, List<List<Float>>>();
		for(FREQUENCYBAND frequencyBand : frequencyBandList) {
			sql = "SELECT x FROM demodulationPoint WHERE row=" + row + " AND frequencyBand_id='" + frequencyBand.getId() + "' ORDER BY x";
			List<Float> frequencyList = (List<Float>) demodulationPointDAO.findByQueryForList(sql, Float.class);
			
			List<List<Float>> demodulationPointList = new ArrayList<List<Float>>();
			for (Iterator<Float> iterator = frequencyList.iterator(); iterator.hasNext();) {
				List<Float> tempPoint = new ArrayList<Float>();
				Float x = iterator.next();
				
				tempPoint.add(x);
				tempPoint.add((float) -140.0);//用于控制柱体高度
				tempPoint.add((float) -150.0);//默认为-150.0
				
				demodulationPointList.add(tempPoint);
			}
			
			demodulationPointMap.put(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency(), demodulationPointList);
		}
		
		return demodulationPointMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<DemodResultParam> getDemodResultParam(String warningTemplateId, int row) {
		String sql = "SELECT * FROM frequencyBand WHERE warningTemplate_id='" + warningTemplateId + "' AND active = 1 ORDER BY startFrequency";
		List<FREQUENCYBAND> frequencyBandList = (List<FREQUENCYBAND>) frequencyBandDAO.findByQuery(sql, FREQUENCYBAND.class);
		
		List<DemodResultParam> demodResultParamList = new ArrayList<DemodResultParam>();
		Integer index = 0;
		for(FREQUENCYBAND frequencyBand : frequencyBandList) {
			sql = "SELECT id, x FROM demodulationPoint WHERE row=" + row + " AND frequencyBand_id='" + frequencyBand.getId() + "' ORDER BY x";
			List<DEMODULATIONPOINT> pointList = (List<DEMODULATIONPOINT>) demodulationPointDAO.findByQuery(sql, DEMODULATIONPOINT.class);		
			
			for(DEMODULATIONPOINT point : pointList){
				DemodResultParam demodResultParam = new DemodResultParam();
				demodResultParam.setDemodulationPointId(point.getId());
				demodResultParam.setX(point.getX());
				demodResultParam.setIndex(index);
				demodResultParam.setFrequencyBand(frequencyBand.getStartFrequency() + "-" + frequencyBand.getStopFrequency());
				
				demodResultParamList.add(demodResultParam);
				
				index ++;
			}
		}
		
		return demodResultParamList;
	}
	
	@SuppressWarnings({ "unchecked" })
	public void batchSaveDemodulationResult(List<DemodResultStatic> demodResultStaticList){
		for(DemodResultStatic temp : demodResultStaticList){
			String sqlCount = "SELECT * FROM demodulationResult WHERE demodulationPoint_id='" + temp.getDemodulationPointId() + "' AND groupNum=" + temp.getIndex();
			List<DEMODULATIONRESULT> demodulationResultList = (List<DEMODULATIONRESULT>) demodulationResultDAO.findByQuery(sqlCount, DEMODULATIONRESULT.class);
			
			for(DEMODULATIONRESULT demodulationResult : demodulationResultList){
				if(demodulationResult.getCIValue() != null && demodulationResult.getCIValue().length() > 1){
					if(demodulationResult.getCIValue().equals(temp.getCIValue())){
						update(demodulationResult, temp);
					} else {
						insert(temp);
					}
				} else {
					//进来代表无CIValue
					if(demodulationResult.getIndicatorOfSCHInfo() != null && demodulationResult.getIndicatorOfSCHInfo().length() > 1){
						if(demodulationResult.getIndicatorOfSCHInfo().equals(temp.getIndicatorOfSCHInfo())){
							update(demodulationResult, temp);
						} else {
							insert(temp);
						}
					} else {
						//进来代表无indicatorOfSCHInfo
						update(demodulationResult, temp);
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void insert(DemodResultStatic temp){
		DEMODULATIONRESULT demodulationResult = new DEMODULATIONRESULT();
		
		demodulationResult.setDemodulationPoint_id(temp.getDemodulationPointId());
		demodulationResult.setGroupNum(temp.getIndex());
		demodulationResult.setPduType(temp.getPduType());
		demodulationResult.setIndicatorOfSCHInfo(temp.getIndicatorOfSCHInfo());
		
		float avgCTI = (float) 0.0;
		if(!temp.getCarrierToInterferenceList().isEmpty()){
			for(float CTI : temp.getCarrierToInterferenceList()){
				avgCTI = avgCTI + CTI;
			}
			avgCTI = avgCTI/temp.getCarrierToInterferenceList().size();
		}
		
		demodulationResult.setAvgCTI(avgCTI);
		demodulationResult.setCIValue(temp.getCIValue());
		demodulationResult.setCICount(temp.getCount());
		demodulationResult.setMobileCountryCode(temp.getMobileCountryCode());
		demodulationResult.setMobileNetworkCode(temp.getMobileNetworkCode());
		demodulationResult.setLocationAreaCode(temp.getLocationAreaCode());
		demodulationResult.setRAColour(temp.getRAColour());
		demodulationResult.setSI13Position(temp.getSI13Position());
		
		String sqlFilePath = "SELECT file_path FROM demodulationResult WHERE demodulationPoint_id='" + temp.getDemodulationPointId() + "'"; 
		List<String> filePathList = (List<String>) demodulationResultDAO.findByQueryForList(sqlFilePath, String.class);
		if(filePathList.isEmpty()){
			demodulationResult.setFile_path("");//file_path
		} else {
			demodulationResult.setFile_path(filePathList.get(0));//file_path
		}
		
		demodulationResult.setActive(true);
		demodulationResult.setCreate_time(System.currentTimeMillis());
		
		demodulationResultDAO.save(demodulationResult);
	}
	
	private void update(DEMODULATIONRESULT demodulationResult, DemodResultStatic temp){
		demodulationResult.setPduType(temp.getPduType());
		demodulationResult.setIndicatorOfSCHInfo(temp.getIndicatorOfSCHInfo());
		
		float avgCTI = (float) 0.0;
		if(!temp.getCarrierToInterferenceList().isEmpty()){
			for(float CTI : temp.getCarrierToInterferenceList()){
				avgCTI = avgCTI + CTI;
			}
			avgCTI = avgCTI/temp.getCarrierToInterferenceList().size();
		}
		
		demodulationResult.setAvgCTI(avgCTI);
		demodulationResult.setCIValue(temp.getCIValue());
		demodulationResult.setCICount(temp.getCount());
		demodulationResult.setMobileCountryCode(temp.getMobileCountryCode());
		demodulationResult.setMobileNetworkCode(temp.getMobileNetworkCode());
		demodulationResult.setLocationAreaCode(temp.getLocationAreaCode());
		demodulationResult.setRAColour(temp.getRAColour());
		demodulationResult.setSI13Position(temp.getSI13Position());
		demodulationResult.setActive(true);
		demodulationResult.setCreate_time(System.currentTimeMillis());
		
		demodulationResultDAO.update(demodulationResult);
	}
	
	public boolean saveDemodulationFilePath(String filePath, int index, String demodulationPointId) {
		String sql = "SELECT * FROM demodulationResult WHERE demodulationPoint_id='" + demodulationPointId + "' AND file_path='" + filePath + "'";
		long num = -1;
		num = demodulationResultDAO.getTotalItemsNumBySelectQuery(sql);
		if(num == 0){
			DEMODULATIONRESULT demodulationResult = new DEMODULATIONRESULT();
			
			demodulationResult.setDemodulationPoint_id(demodulationPointId);
			demodulationResult.setGroupNum(index);
			demodulationResult.setFile_path(filePath);
			demodulationResult.setActive(true);
			demodulationResult.setCreate_time(System.currentTimeMillis());
	
			demodulationResultDAO.save(demodulationResult);
			
			return true;
		}
		
		if(num > 0){
			return true;
		} else {
			return false;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean deleteDemodResultWithFileByDemodulationPointIdList(List<String> demodulationPointIdList){
		String ids = "";
		for(String demodulationPointId : demodulationPointIdList){
			ids = ids + "'" + demodulationPointId + "',";
		}
		ids = ids.substring(0, ids.length() - 1);
		
		String sql = "SELECT file_path FROM demodulationResult WHERE demodulationPoint_id IN (" + ids + ")";
		List<String> filePathList = (List<String>) demodulationResultDAO.findByQueryForList(sql, String.class);
		
		for(String filePath : filePathList){
			File file = new File(filePath);
			// 路径为文件且不为空则进行删除  
			if (file.isFile() && file.exists()) {
				file.delete();
			}
		}
		
		sql = "DELETE FROM demodulationResult WHERE demodulationPoint_id IN (" + ids + ")";
		demodulationResultDAO.executeBySql(sql);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceInfo> getDeviceInfoToShowTemplate(){
		List<DeviceInfo> deviceInfoList = new ArrayList<DeviceInfo>();
		AccountTools accountTools = new AccountTools();
		String sql = "";
		if(accountTools.doesCurrentAccountHasSuperadminRole()){
			sql = "SELECT device.id AS id, device.device_num AS deviceNum, device.device_type AS deviceType, "
				+ "device.LNG AS LNG, device.LAT AS LAT FROM warningTemplate, device WHERE "
				+ "device.id=warningTemplate.device_id GROUP BY device.id ORDER BY deviceNum ASC";
		} else if(accountTools.doesCurrentAccountHasAdminRole()){
			sql = "SELECT device.id AS id, device.device_num AS deviceNum, device.device_type AS deviceType, "
				+ "device.LNG AS LNG, device.LAT AS LAT FROM warningTemplate, device WHERE "
				+ "warningTemplate.creator_id IN ( SELECT id FROM account WHERE department_id IN ( "
				+ "SELECT department_id FROM account WHERE id='" + accountTools.getCurrentAccountId() + "' ) ) AND "
				+ "device.id=warningTemplate.device_id GROUP BY device.id ORDER BY deviceNum ASC";
		} else {
			sql = "SELECT device.id AS id, device.device_num AS deviceNum, device.device_type AS deviceType, "
				+ "device.LNG AS LNG, device.LAT AS LAT FROM warningTemplate, device WHERE "
				+ "warningTemplate.creator_id='" + accountTools.getCurrentAccountId() + "' AND "
				+ "device.id=warningTemplate.device_id GROUP BY device.id ORDER BY deviceNum ASC";
		}
		
		deviceInfoList = (List<DeviceInfo>) deviceDAO.findByQuery(sql, DeviceInfo.class);
		
		return deviceInfoList;
	}
	
	public boolean canWarningTemplateBeModify(String warningTemplateId){
		String sql = "SELECT * FROM deviceWT, deviceFB, originalData WHERE deviceWT.template_id='" + warningTemplateId + 
					"' AND deviceWT.id=deviceFB.deviceWT_id AND deviceFB.id=originalData.deviceFB_id";
		
		if(deviceWTDAO.getTotalItemsNumBySelectQuery(sql) > 0){
			return false;
		}
		
		return true;
	}
}
