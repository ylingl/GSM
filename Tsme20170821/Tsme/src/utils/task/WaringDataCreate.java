package utils.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import logic.data.bean.BeanCoordinate;
import mvc.history.WarningGroup;
import mvc.history.WarningHistory;
import mvc.spectra.SpectraData;
import mvc.spectra.WarningData;
import utils.DataPoolTools;

/**
 * 数据存储处理，存储为js文件，供前台js调用
 * 
 * @author kin
 *
 */
public class WaringDataCreate implements Runnable {
	
	private String deviceNum;
	
	private boolean isRunning = false;
	// 文件存储根目录
	//private static String js_root_dir = "d:\\";

	private static final int sleepTime = 500;//单位为毫秒
	
	private static final double ratio = 0.1;
	
	private static final double threshold = -90.0;
	
	private static final int pointNum = 2;
	
	private static final int basicLine = 150;//基准参考线
	
	public WaringDataCreate(String deviceNum) {
		this.deviceNum = deviceNum;
	}

	// 保存为文件
	@Override
	public void run() {
		
		if(isRunning)
			return;
		
		isRunning = true;
		
		while (DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum) && 
				DataPoolTools.deviceMonitorParameterMap.get(deviceNum).isWarning()) {
			
			DataPoolTools.clearOverTimeMonitor();//触发清除所有超时的监控
				
			if (DataPoolTools.monitorDataPool.get(deviceNum).isEmpty()){
				try {
					Thread.sleep(sleepTime);
					System.out.println("开始休眠1秒");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			SpectraData spectraData = new SpectraData();
			//frequencyBand-频谱队列
			Map<String, ArrayList<Double>> spDataMap = new HashMap<String, ArrayList<Double>>();
			HashMap<String, ArrayList<WarningData>> warnDataMap = new HashMap<String, ArrayList<WarningData>>();
			
			HashMap<String, ArrayList<WarningHistory>> warningHistoryMap = new HashMap<String, ArrayList<WarningHistory>>();
			//frequencyBand-频谱队列
			HashMap<String, Queue<ArrayList<Double>>> dataMap = DataPoolTools.monitorDataPool.get(deviceNum);
			
			int depth = 0;
			for(String key : dataMap.keySet()){//取各个频段
				//仅取第一频段的数据深度
				depth = dataMap.get(key).size();
				
				//仅取头一组数据用作显示，开始//
				ArrayList<Double> firstGroup = new ArrayList<Double>();
				if(dataMap.get(key).peek() != null){
					firstGroup.addAll(dataMap.get(key).peek());
					spectraData.setLNG(firstGroup.get(firstGroup.size() - 3));//倒数第三个记录了经度
					spectraData.setLAT(firstGroup.get(firstGroup.size() - 2));//倒数第二个记录了纬度
					
					firstGroup.remove(firstGroup.size() - 1);//最后一个数记录了数据产生时间
					firstGroup.remove(firstGroup.size() - 1);//倒数第二个记录了纬度
					firstGroup.remove(firstGroup.size() - 1);//倒数第三个记录了经度
				}
				spDataMap.put(key, firstGroup);
				//仅取头一组数据用作显示，结束//
				
				//取预警曲线，该曲线在监控初始化时加载
				List<BeanCoordinate> warningLine = DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getWarningPointMap().get(key);
				
				//预警数据列表
				ArrayList<WarningData> warningDataList = new ArrayList<WarningData>();
				if(DataPoolTools.spectraDataMap.containsKey(deviceNum) && DataPoolTools.spectraDataMap.get(deviceNum).getWarnDataMap().containsKey(key)) {
					//加载已有的预警数据
					warningDataList = DataPoolTools.spectraDataMap.get(deviceNum).getWarnDataMap().get(key);
				}
				
				//用于历史数据存储
				ArrayList<WarningHistory> warningHistoryList = new ArrayList<WarningHistory>();
				
				for(int j = 0; j < depth; j ++) {//一个频段中的depth组数据
					List<Double> spData = DataPoolTools.monitorDataPool.get(deviceNum).get(key).poll();

					if(spData != null && warningLine != null && spData.size() - 3 == warningLine.size()) {//最后一个数记录了数据产生时间，倒数第二个是纬度，倒数第三个是经度
						int count = 0;
						float beginFrequency = 0, beginY = -150, endFrequency = 0, endY = -150;
						
						//逐一筛选一组数据中的每个频点
						for (int i = 0; i < spData.size() - 3; i ++) {//最后一个数记录了数据产生时间，倒数第二个是纬度，倒数第三个是经度
							if(spData.get(i) > (basicLine + warningLine.get(i).getY()) * ratio + warningLine.get(i).getY() 
								&& spData.get(i) > threshold){
								if(count == 0){
									beginFrequency = warningLine.get(i).getX();
									beginY = spData.get(i).floatValue();
								}
								count ++;
							} else {
								if(count >= pointNum && beginFrequency > 0){
									count = 0;
									endFrequency = warningLine.get(i).getX();
									endY = spData.get(i).intValue();
									
									//用于显示实时告警结果，开始//
									WarningData warningData = new WarningData();
									
									BigDecimal start = new BigDecimal(beginFrequency);
									BigDecimal stop = new BigDecimal(endFrequency);
									BigDecimal center = new BigDecimal((beginFrequency + endFrequency) / 2);
									
									warningData.setStartFrequency(start.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
									warningData.setStopFrequency(stop.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
									warningData.setCenterFrequency(center.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
									warningData.setCurrentTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(Math.round(spData.get(spData.size() - 1)))));
									
									aggregateList(warningDataList, warningData);
									//用于显示实时告警结果，结束//
									
									//用于存储告警结果，开始//
									WarningGroup warningGroup = new WarningGroup();
									
									BeanCoordinate beginPoint = new BeanCoordinate();
									beginPoint.setX(beginFrequency);
									beginPoint.setY(beginY);
									
									BeanCoordinate endPoint = new BeanCoordinate();
									endPoint.setX(endFrequency);
									endPoint.setY(endY);
									
									warningGroup.setBeginPoint(beginPoint);
									warningGroup.setCenterFrequency(center.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
									warningGroup.setEndPoint(endPoint);
									
									aggregateList(warningHistoryList, Long.toString(Math.round(spData.get(spData.size() - 1))), warningGroup);
									//用于存储告警结果，结束//
								} else {
									count = 0;
									beginFrequency = 0;
									beginY = -150;
									endFrequency = 0;
									endY = -150;
								}
							}
						}
					}
				}
				//用于显示实时告警结果，每个频段逐一更替
				warnDataMap.put(key, warningDataList);
				
				//用于显示实时告警结果，每个频段逐一新增，记录进程负责清空
				if(!DataPoolTools.warningHistoryMapToFile.containsKey(deviceNum)){
					warningHistoryMap.put(key, warningHistoryList);
				} else if (!DataPoolTools.warningHistoryMapToFile.get(deviceNum).containsKey(key)){
					warningHistoryMap.put(key, warningHistoryList);
				} else {
					DataPoolTools.warningHistoryMapToFile.get(deviceNum).get(key).addAll(warningHistoryList);
				}
				
			}

			spectraData.setSpDataMap(spDataMap);//仅存放一组数据用于显示
			spectraData.setWarnDataMap(warnDataMap);
			DataPoolTools.spectraDataMap.put(deviceNum, spectraData);
			
			//空则新建并赋值，非空则由循环体内累加新值
			if(!DataPoolTools.warningHistoryMapToFile.containsKey(deviceNum)){
				DataPoolTools.warningHistoryMapToFile.put(deviceNum, warningHistoryMap);
			}
			
			//休眠等待
			try {
				//System.out.println("开始休眠"+sleepTime/1000+"秒");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		DataPoolTools.monitorDataPool.remove(deviceNum);
		DataPoolTools.spectraDataMap.remove(deviceNum);
		DataPoolTools.spectraPreParaCopyMap.remove(deviceNum);
	}

	private void aggregateList(List<WarningData> warningDataList, WarningData warningData){	
		if(warningDataList == null || warningDataList.isEmpty()) {//初始状态,warningDataList中无值
			List<String> warningTimeList = new ArrayList<String>();
			warningTimeList.add(warningData.getCurrentTime());
			
			warningData.setWarningTimeList(warningTimeList);
			warningDataList.add(warningData);
		} else {//warningDataList中有值
			boolean addEnable = true;
			
			for(int i = 0; i < warningDataList.size(); i ++){
				if(warningData.getCenterFrequency() < warningDataList.get(i).getCenterFrequency()) {//warningData为新值
					addEnable = false;
					
					List<String> warningTimeList = new ArrayList<String>();
					warningTimeList.add(warningData.getCurrentTime());
					
					warningData.setWarningTimeList(warningTimeList);
					warningDataList.add(i, warningData);
					break;
				}
				
				if(warningDataList.get(i).getCenterFrequency() == warningData.getCenterFrequency()) {////warningData为已有值
					addEnable = false;
					
					WarningData warningDataOld = warningDataList.get(i);
					
					warningDataOld.getWarningTimeList().add(warningData.getCurrentTime());
					warningDataOld.setNumber(warningDataOld.getNumber() + 1);
					warningDataOld.setCurrentTime(warningData.getCurrentTime());
					
					warningDataList.set(i, warningDataOld);
					break;
				}
			}
			
			if(addEnable) {//warningData为新值
				List<String> warningTimeList = new ArrayList<String>();
				warningTimeList.add(warningData.getCurrentTime());
				
				warningData.setWarningTimeList(warningTimeList);
				warningDataList.add(warningData);
			}
		}
	}
	
	private void aggregateList(List<WarningHistory> warningHistoryList, String warningTime, WarningGroup warningGroup){
		if(warningHistoryList == null || warningHistoryList.isEmpty()) {//初始状态,warningHistoryList中无值
			WarningHistory warningHistory = new WarningHistory();
			
			List<WarningGroup> warningGroupList = new ArrayList<WarningGroup>();
			warningGroupList.add(warningGroup);
			
			warningHistory.setWarningGroupList(warningGroupList);
			warningHistory.setWarningTime(warningTime);
			
			warningHistoryList.add(warningHistory);
		} else {//warningHistoryList中有值
			boolean addEnable = true;
			for(int i = 0; i < warningHistoryList.size(); i ++){
				if(warningHistoryList.get(i).getWarningTime().equals(warningTime)) {//相同时间点的warningGroup归为一组
					addEnable = false;
					warningHistoryList.get(i).getWarningGroupList().add(warningGroup);
					break;
				}
			}
			
			if(addEnable) {//新时间点，新起一组
				WarningHistory warningHistory = new WarningHistory();
				
				List<WarningGroup> warningGroupList = new ArrayList<WarningGroup>();
				warningGroupList.add(warningGroup);
				
				warningHistory.setWarningGroupList(warningGroupList);
				warningHistory.setWarningTime(warningTime);
				
				warningHistoryList.add(warningHistory);
			}
		}
	}
	
}
