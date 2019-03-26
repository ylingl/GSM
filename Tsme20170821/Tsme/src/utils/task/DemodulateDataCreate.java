package utils.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;

import logic.data.DataAnalysisService;
import mvc.device.DemodResultParam;
import mvc.device.DemodResultStatic;
import utils.DataPoolTools;

public class DemodulateDataCreate implements Runnable {
	
	private DataAnalysisService dataAnalysisService;
	
	private String deviceNum;
	
	private boolean isRunning = false;
	
	private boolean trainingToken = true;
	
	// 文件存储根目录
	//private static String js_root_dir = "d:\\";

	private static final int sleepTime = 500;//单位为毫秒
	
	public DemodulateDataCreate(DataAnalysisService dataAnalysisService, String deviceNum) {
		this.dataAnalysisService = dataAnalysisService;
		this.deviceNum = deviceNum;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(isRunning)
			return;
		
		if(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isTraining()){
			trainingToken = true;
		} else if(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isWarning()){
			trainingToken = false;
		}
		
		isRunning = true;
		
		System.out.println("设备"+ deviceNum + "开始解调！");
		
		while (DataPoolTools.deviceDemodParameterMap.containsKey(deviceNum) && 
				(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isWarning() || 
				DataPoolTools.deviceDemodParameterMap.get(deviceNum).isTraining())) {
			
			if (DataPoolTools.demodulateDataPool.get(deviceNum).isEmpty()){
				try {
					Thread.sleep(sleepTime * 2);
					System.out.println("缓冲池为空，休眠1秒等待数据！");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			//frequencyBand-解调统计结果列，每个频点仅存放一组互不相同的统计数据
			HashMap<String, ArrayList<DemodResultStatic>> frequencyBandDemodStaticMap = new HashMap<String, ArrayList<DemodResultStatic>>();
			if(DataPoolTools.demodResultStaticMap.get(deviceNum) != null) {
				frequencyBandDemodStaticMap = DataPoolTools.demodResultStaticMap.get(deviceNum);
			}
			
			//frequencyBand-解调统计结果列，每个频点仅存放一组互不相同的统计数据
			HashMap<String, ArrayList<DemodResultStatic>> frequencyBandDemodStaticMapToDB = new HashMap<String, ArrayList<DemodResultStatic>>();
			if(DataPoolTools.demodResultStaticMapToDB.get(deviceNum) != null) {
				frequencyBandDemodStaticMapToDB = DataPoolTools.demodResultStaticMapToDB.get(deviceNum);
			}
			
			//frequencyBand-frequency-解调结果列
			HashMap<String, HashMap<Float, Queue<String[]>>> frequencyBandDataMap = DataPoolTools.demodulateDataPool.get(deviceNum);
			//frequencyBand-frequency-解调结果列
			for(Entry<String, HashMap<Float, Queue<String[]>>> frequencyBandDataMapEntry : frequencyBandDataMap.entrySet()){
				String frequencyBand = frequencyBandDataMapEntry.getKey();
				
				//解调统计结果列
				ArrayList<DemodResultStatic> demodResultStaticList = new ArrayList<DemodResultStatic>();
				if(!frequencyBandDemodStaticMap.isEmpty() && frequencyBandDemodStaticMap.containsKey(frequencyBandDataMapEntry.getKey())){
					demodResultStaticList.addAll(frequencyBandDemodStaticMap.get(frequencyBandDataMapEntry.getKey()));
				}
				
				//解调统计结果列
				ArrayList<DemodResultStatic> demodResultStaticListToDB = new ArrayList<DemodResultStatic>();
				if(!frequencyBandDemodStaticMapToDB.isEmpty() && frequencyBandDemodStaticMapToDB.containsKey(frequencyBandDataMapEntry.getKey())){
					demodResultStaticListToDB.addAll(frequencyBandDemodStaticMapToDB.get(frequencyBandDataMapEntry.getKey()));
				}
				
				//frequency-解调结果列
				for(Entry<Float, Queue<String[]>> frequencyDataMapEntry : frequencyBandDataMapEntry.getValue().entrySet()){
					//仅取当前时刻的数据深度
					int depth = frequencyDataMapEntry.getValue().size();
					for(int j = 0; j < depth; j ++) {
						String[] demodDataGroup = frequencyDataMapEntry.getValue().poll();
						
						/*System.out.println("Demod " + frequencyDataMapEntry.getKey() + ":" + demodDataGroup[0] 
								+ demodDataGroup[1] + demodDataGroup[2] + demodDataGroup[3]);*/
						
						parseAndAggregateDemodData(demodResultStaticList, demodDataGroup);
						parseAndAggregateDemodData(demodResultStaticListToDB, demodDataGroup);
					}
				}

				if(!demodResultStaticList.isEmpty()){
					frequencyBandDemodStaticMap.put(frequencyBand, demodResultStaticList);
				}
				
				if(!demodResultStaticListToDB.isEmpty()){
					frequencyBandDemodStaticMapToDB.put(frequencyBand, demodResultStaticListToDB);
				}
			}
			
			DataPoolTools.demodResultStaticMap.put(deviceNum, frequencyBandDemodStaticMap);
			DataPoolTools.demodResultStaticMapToDB.put(deviceNum, frequencyBandDemodStaticMapToDB);
			
			//休眠等待
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		HashMap<String, ArrayList<DemodResultStatic>> frequencyBandDemodStaticMap = DataPoolTools.demodResultStaticMap.get(deviceNum);
		
		List<DemodResultStatic> demodResultStaticList = new ArrayList<DemodResultStatic>();
		for(Entry<String, ArrayList<DemodResultStatic>> entry : frequencyBandDemodStaticMap.entrySet()) {
			demodResultStaticList.addAll(entry.getValue());
		}
		
		if(trainingToken){
			dataAnalysisService.batchSaveDemodulationResult(demodResultStaticList);
		}
		
		DataPoolTools.deviceDemodParameterMap.remove(deviceNum);
		DataPoolTools.demodulateDataPool.remove(deviceNum);
		//DataPoolTools.demodResultStaticMap.remove(deviceNum);
	}
	
	private void parseAndAggregateDemodData(List<DemodResultStatic> demodResultStaticList, String[] demodDataGroup){
		int index = -1;
		for(int i = 0; i < demodDataGroup.length; i ++) {
			/*
			 * 开始解析载干比
			 */
			if(demodDataGroup[i].contains("Power Result for Frequency Index")){
				String[] param1 = demodDataGroup[i].split(":");
				index = Integer.parseInt(param1[1].replaceAll(" ", ""));
				
				String indicatorOfSCHInfo = "";
				float carrierToInterference = 0;
				
				if(demodDataGroup[i + 5].contains("Indicator Of SCHInfo")){
					String[] param2 = demodDataGroup[i + 5].split("SCHInfo");
					indicatorOfSCHInfo = param2[1].replaceAll(" ", "");
				}
				
				if(demodDataGroup[i + 7].contains("Carrier to Interference in Db 100th")){
					String[] param3 = demodDataGroup[i + 7].split("100th");
					i = i + 7;
					if(param3[1].contains("[no value]")){
						continue;
					}
					carrierToInterference = Float.parseFloat(param3[1].replaceAll(" ", ""));	
				}

				boolean IOSempty = true;
				if(!demodResultStaticList.isEmpty()){
					for(DemodResultStatic temp : demodResultStaticList){
						if(temp.getIndex() == index && temp.getIndicatorOfSCHInfo().equals(indicatorOfSCHInfo)){
							IOSempty = false;
							temp.getCarrierToInterferenceList().add(carrierToInterference);
						}
					}
				}
				
				if(IOSempty || demodResultStaticList.isEmpty()){
					DemodResultStatic demodResultStatic = new DemodResultStatic();
					List<Float> carrierToInterferenceList = new ArrayList<Float>();
					
					demodResultStatic.setIndex(index);
					demodResultStatic.setIndicatorOfSCHInfo(indicatorOfSCHInfo);
					carrierToInterferenceList.add(carrierToInterference);
					demodResultStatic.setCarrierToInterferenceList(carrierToInterferenceList);
					
					List<DemodResultParam> demodResultParamList = DataPoolTools.deviceDemodParameterMap.get(deviceNum).getDemodResultParamList();
					for(DemodResultParam demodResultParam : demodResultParamList) {
						if(demodResultParam.getIndex() == index){
							demodResultStatic.setDemodulationPointId(demodResultParam.getDemodulationPointId());;
							demodResultStatic.setX(demodResultParam.getX());
							demodResultStatic.setFrequencyBand(demodResultParam.getFrequencyBand());
						}
					}
					
					demodResultStaticList.add(demodResultStatic);
				}
			}
			
			/*
			 * 开始解析解调结果
			 */
			if(demodDataGroup[i].contains("DemodResult")){
				DemodResultStatic demodResultStatic = new DemodResultStatic();
				
				String indicatorOfSCHInfo = "";
				String CIValue = "";
				
				for(i ++; i < demodDataGroup.length; i ++){
					if(demodDataGroup[i].contains("Frequency Index")){
						String[] param = demodDataGroup[i].split(":");
						index = Integer.parseInt(param[1].replaceAll(" ", ""));
						demodResultStatic.setIndex(index);
						demodResultStatic.setCount(1);
						
						List<DemodResultParam> demodResultParamList = DataPoolTools.deviceDemodParameterMap.get(deviceNum).getDemodResultParamList();
						for(DemodResultParam demodResultParam : demodResultParamList) {
							if(demodResultParam.getIndex() == index){
								demodResultStatic.setDemodulationPointId(demodResultParam.getDemodulationPointId());;
								demodResultStatic.setX(demodResultParam.getX());
								demodResultStatic.setFrequencyBand(demodResultParam.getFrequencyBand());
							}
						}
						
						continue;
					}
					
					if(demodDataGroup[i].contains("Indicator Of SCHInfo")){
						String[] param = demodDataGroup[i].split(":");
						indicatorOfSCHInfo = param[1].replaceAll(" ", "");
						demodResultStatic.setIndicatorOfSCHInfo(indicatorOfSCHInfo);
						continue;
					}
					
					if(demodDataGroup[i].contains("Pdu Type")){
						String[] param = demodDataGroup[i].split(":");
						String pduType = param[1].replaceAll(" ", "");
						demodResultStatic.setPduType(pduType);
						continue;
					}
					
					if(demodDataGroup[i].contains("CIValue")){
						String[] param = demodDataGroup[i].split(":");
						CIValue = param[1].replaceAll(" ", "");
						demodResultStatic.setCIValue(CIValue);
						continue;
					}
					
					if(demodDataGroup[i].contains("Mobile Country Code")){
						String[] param = demodDataGroup[i].split(":");
						String mobileCountryCode = param[1].replaceAll(" ", "");
						demodResultStatic.setMobileCountryCode(mobileCountryCode);
						continue;
					}
					
					if(demodDataGroup[i].contains("Mobile Network Code")){
						String[] param = demodDataGroup[i].split(":");
						String mobileNetworkCode = param[1].replaceAll(" ", "");
						demodResultStatic.setMobileNetworkCode(mobileNetworkCode);
						continue;
					}
					
					if(demodDataGroup[i].contains("Location Area Code")){
						String[] param = demodDataGroup[i].split(":");
						String locationAreaCode = param[1].replaceAll(" ", "");
						demodResultStatic.setLocationAreaCode(locationAreaCode);
						continue;
					}
					
					if(demodDataGroup[i].contains("RA COLOUR")){
						String[] param = demodDataGroup[i].split(":");
						String RAColour = param[1].replaceAll(" ", "");
						demodResultStatic.setRAColour(RAColour);
						continue;
					}
					
					if(demodDataGroup[i].contains("SI13 POSITION")){
						String[] param = demodDataGroup[i].split(":");
						String SI13Position = param[1].replaceAll(" ", "");
						demodResultStatic.setSI13Position(SI13Position);
						continue;
					}
				}
				
				i --;//为了抵消for中的i++
				
				boolean addEnable = true;
				if(!demodResultStaticList.isEmpty()) {
					int num = 0;
					for(DemodResultStatic temp : demodResultStaticList) {
						if(temp.getIndex() == index && temp.getIndicatorOfSCHInfo().equals(indicatorOfSCHInfo)) {
							addEnable = false;
							if(temp.getCIValue() == null) {
								demodResultStatic.setCarrierToInterferenceList(temp.getCarrierToInterferenceList());
								demodResultStaticList.set(num, demodResultStatic);
								break;
							} else {
								if(temp.getCIValue().equals(CIValue)) {
									//进来代表有重复值
									long count = temp.getCount() + 1;
									temp.setCount(count);
								} else {
									demodResultStatic.setCarrierToInterferenceList(temp.getCarrierToInterferenceList());
									demodResultStaticList.set(num, demodResultStatic);
								}
								break;
							}
						}
					}
					
					num ++;
				}
				
				if(addEnable){
					demodResultStaticList.add(demodResultStatic);
				}
			}
			
			if(demodDataGroup[i].contains("SCHInfo Result for Frequency Index")){
				continue;
			}
			
			if(demodDataGroup[i].contains("CellIdent Result for Frequency Index")){
				continue;
			}
		}
	}
	
}
