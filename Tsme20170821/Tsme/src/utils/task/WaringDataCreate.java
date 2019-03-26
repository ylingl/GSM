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
 * ���ݴ洢�����洢Ϊjs�ļ�����ǰ̨js����
 * 
 * @author kin
 *
 */
public class WaringDataCreate implements Runnable {
	
	private String deviceNum;
	
	private boolean isRunning = false;
	// �ļ��洢��Ŀ¼
	//private static String js_root_dir = "d:\\";

	private static final int sleepTime = 500;//��λΪ����
	
	private static final double ratio = 0.1;
	
	private static final double threshold = -90.0;
	
	private static final int pointNum = 2;
	
	private static final int basicLine = 150;//��׼�ο���
	
	public WaringDataCreate(String deviceNum) {
		this.deviceNum = deviceNum;
	}

	// ����Ϊ�ļ�
	@Override
	public void run() {
		
		if(isRunning)
			return;
		
		isRunning = true;
		
		while (DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum) && 
				DataPoolTools.deviceMonitorParameterMap.get(deviceNum).isWarning()) {
			
			DataPoolTools.clearOverTimeMonitor();//����������г�ʱ�ļ��
				
			if (DataPoolTools.monitorDataPool.get(deviceNum).isEmpty()){
				try {
					Thread.sleep(sleepTime);
					System.out.println("��ʼ����1��");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			SpectraData spectraData = new SpectraData();
			//frequencyBand-Ƶ�׶���
			Map<String, ArrayList<Double>> spDataMap = new HashMap<String, ArrayList<Double>>();
			HashMap<String, ArrayList<WarningData>> warnDataMap = new HashMap<String, ArrayList<WarningData>>();
			
			HashMap<String, ArrayList<WarningHistory>> warningHistoryMap = new HashMap<String, ArrayList<WarningHistory>>();
			//frequencyBand-Ƶ�׶���
			HashMap<String, Queue<ArrayList<Double>>> dataMap = DataPoolTools.monitorDataPool.get(deviceNum);
			
			int depth = 0;
			for(String key : dataMap.keySet()){//ȡ����Ƶ��
				//��ȡ��һƵ�ε��������
				depth = dataMap.get(key).size();
				
				//��ȡͷһ������������ʾ����ʼ//
				ArrayList<Double> firstGroup = new ArrayList<Double>();
				if(dataMap.get(key).peek() != null){
					firstGroup.addAll(dataMap.get(key).peek());
					spectraData.setLNG(firstGroup.get(firstGroup.size() - 3));//������������¼�˾���
					spectraData.setLAT(firstGroup.get(firstGroup.size() - 2));//�����ڶ�����¼��γ��
					
					firstGroup.remove(firstGroup.size() - 1);//���һ������¼�����ݲ���ʱ��
					firstGroup.remove(firstGroup.size() - 1);//�����ڶ�����¼��γ��
					firstGroup.remove(firstGroup.size() - 1);//������������¼�˾���
				}
				spDataMap.put(key, firstGroup);
				//��ȡͷһ������������ʾ������//
				
				//ȡԤ�����ߣ��������ڼ�س�ʼ��ʱ����
				List<BeanCoordinate> warningLine = DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getWarningPointMap().get(key);
				
				//Ԥ�������б�
				ArrayList<WarningData> warningDataList = new ArrayList<WarningData>();
				if(DataPoolTools.spectraDataMap.containsKey(deviceNum) && DataPoolTools.spectraDataMap.get(deviceNum).getWarnDataMap().containsKey(key)) {
					//�������е�Ԥ������
					warningDataList = DataPoolTools.spectraDataMap.get(deviceNum).getWarnDataMap().get(key);
				}
				
				//������ʷ���ݴ洢
				ArrayList<WarningHistory> warningHistoryList = new ArrayList<WarningHistory>();
				
				for(int j = 0; j < depth; j ++) {//һ��Ƶ���е�depth������
					List<Double> spData = DataPoolTools.monitorDataPool.get(deviceNum).get(key).poll();

					if(spData != null && warningLine != null && spData.size() - 3 == warningLine.size()) {//���һ������¼�����ݲ���ʱ�䣬�����ڶ�����γ�ȣ������������Ǿ���
						int count = 0;
						float beginFrequency = 0, beginY = -150, endFrequency = 0, endY = -150;
						
						//��һɸѡһ�������е�ÿ��Ƶ��
						for (int i = 0; i < spData.size() - 3; i ++) {//���һ������¼�����ݲ���ʱ�䣬�����ڶ�����γ�ȣ������������Ǿ���
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
									
									//������ʾʵʱ�澯�������ʼ//
									WarningData warningData = new WarningData();
									
									BigDecimal start = new BigDecimal(beginFrequency);
									BigDecimal stop = new BigDecimal(endFrequency);
									BigDecimal center = new BigDecimal((beginFrequency + endFrequency) / 2);
									
									warningData.setStartFrequency(start.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
									warningData.setStopFrequency(stop.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
									warningData.setCenterFrequency(center.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
									warningData.setCurrentTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(Math.round(spData.get(spData.size() - 1)))));
									
									aggregateList(warningDataList, warningData);
									//������ʾʵʱ�澯���������//
									
									//���ڴ洢�澯�������ʼ//
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
									//���ڴ洢�澯���������//
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
				//������ʾʵʱ�澯�����ÿ��Ƶ����һ����
				warnDataMap.put(key, warningDataList);
				
				//������ʾʵʱ�澯�����ÿ��Ƶ����һ��������¼���̸������
				if(!DataPoolTools.warningHistoryMapToFile.containsKey(deviceNum)){
					warningHistoryMap.put(key, warningHistoryList);
				} else if (!DataPoolTools.warningHistoryMapToFile.get(deviceNum).containsKey(key)){
					warningHistoryMap.put(key, warningHistoryList);
				} else {
					DataPoolTools.warningHistoryMapToFile.get(deviceNum).get(key).addAll(warningHistoryList);
				}
				
			}

			spectraData.setSpDataMap(spDataMap);//�����һ������������ʾ
			spectraData.setWarnDataMap(warnDataMap);
			DataPoolTools.spectraDataMap.put(deviceNum, spectraData);
			
			//�����½�����ֵ���ǿ�����ѭ�������ۼ���ֵ
			if(!DataPoolTools.warningHistoryMapToFile.containsKey(deviceNum)){
				DataPoolTools.warningHistoryMapToFile.put(deviceNum, warningHistoryMap);
			}
			
			//���ߵȴ�
			try {
				//System.out.println("��ʼ����"+sleepTime/1000+"��");
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
		if(warningDataList == null || warningDataList.isEmpty()) {//��ʼ״̬,warningDataList����ֵ
			List<String> warningTimeList = new ArrayList<String>();
			warningTimeList.add(warningData.getCurrentTime());
			
			warningData.setWarningTimeList(warningTimeList);
			warningDataList.add(warningData);
		} else {//warningDataList����ֵ
			boolean addEnable = true;
			
			for(int i = 0; i < warningDataList.size(); i ++){
				if(warningData.getCenterFrequency() < warningDataList.get(i).getCenterFrequency()) {//warningDataΪ��ֵ
					addEnable = false;
					
					List<String> warningTimeList = new ArrayList<String>();
					warningTimeList.add(warningData.getCurrentTime());
					
					warningData.setWarningTimeList(warningTimeList);
					warningDataList.add(i, warningData);
					break;
				}
				
				if(warningDataList.get(i).getCenterFrequency() == warningData.getCenterFrequency()) {////warningDataΪ����ֵ
					addEnable = false;
					
					WarningData warningDataOld = warningDataList.get(i);
					
					warningDataOld.getWarningTimeList().add(warningData.getCurrentTime());
					warningDataOld.setNumber(warningDataOld.getNumber() + 1);
					warningDataOld.setCurrentTime(warningData.getCurrentTime());
					
					warningDataList.set(i, warningDataOld);
					break;
				}
			}
			
			if(addEnable) {//warningDataΪ��ֵ
				List<String> warningTimeList = new ArrayList<String>();
				warningTimeList.add(warningData.getCurrentTime());
				
				warningData.setWarningTimeList(warningTimeList);
				warningDataList.add(warningData);
			}
		}
	}
	
	private void aggregateList(List<WarningHistory> warningHistoryList, String warningTime, WarningGroup warningGroup){
		if(warningHistoryList == null || warningHistoryList.isEmpty()) {//��ʼ״̬,warningHistoryList����ֵ
			WarningHistory warningHistory = new WarningHistory();
			
			List<WarningGroup> warningGroupList = new ArrayList<WarningGroup>();
			warningGroupList.add(warningGroup);
			
			warningHistory.setWarningGroupList(warningGroupList);
			warningHistory.setWarningTime(warningTime);
			
			warningHistoryList.add(warningHistory);
		} else {//warningHistoryList����ֵ
			boolean addEnable = true;
			for(int i = 0; i < warningHistoryList.size(); i ++){
				if(warningHistoryList.get(i).getWarningTime().equals(warningTime)) {//��ͬʱ����warningGroup��Ϊһ��
					addEnable = false;
					warningHistoryList.get(i).getWarningGroupList().add(warningGroup);
					break;
				}
			}
			
			if(addEnable) {//��ʱ��㣬����һ��
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
