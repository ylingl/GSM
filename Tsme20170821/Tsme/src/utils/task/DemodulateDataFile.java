package utils.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import logic.data.DataAnalysisService;
import mvc.device.DemodResultParam;
import utils.DataPoolTools;
import utils.GTime;

public class DemodulateDataFile implements Runnable {
	
	private DataAnalysisService dataAnalysisService;
	
	private String deviceNum;
	
	private boolean isRunning = false;
	
	// 每个文件内的数据行数
	private static final int totalRowsPerWrite = 1;

	private static final int sleepTime = 500;//单位为毫秒
	
	public DemodulateDataFile(DataAnalysisService dataAnalysisService, String deviceNum) {
		this.dataAnalysisService = dataAnalysisService;
		this.deviceNum = deviceNum;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(isRunning)
			return;
		
		isRunning = true;
		
		System.out.println("设备"+ deviceNum + "开始存储解调数据！");
		
		String var = GTime.getTime(GTime.YYYYMMDDhhmmssxxx);
		
		Map<String, Boolean> filePathExistTokenMap = new HashMap<String, Boolean>();
		
		while (DataPoolTools.deviceDemodParameterMap.containsKey(deviceNum) && 
				(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isWarning()
				|| DataPoolTools.deviceDemodParameterMap.get(deviceNum).isTraining())) {
			
			if (DataPoolTools.demodulateDataToFilePool.get(deviceNum).isEmpty()) {
				try {
					Thread.sleep(sleepTime * 2);
					System.out.println("缓冲池为空，休眠1秒等待数据！");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			//frequencyBand-frequency-解调结果列
			for (Entry<String, HashMap<Float, Queue<String>>> frequencyBandMapEntry : DataPoolTools.demodulateDataToFilePool.get(deviceNum).entrySet()) {
				//frequency-解调结果列
				HashMap<Float, Queue<String>> frequencyDemodMap = frequencyBandMapEntry.getValue();
				//frequency-解调结果列
				for(Entry<Float, Queue<String>> frequencyDemodMapEntry : frequencyDemodMap.entrySet()) {
					if(frequencyDemodMapEntry.getValue().size() < totalRowsPerWrite){
						//System.out.println(frequencyDemodMapEntry.getKey());
						continue;
					} else {
						StringBuilder sb = new StringBuilder();
						for(int count = 0; count < totalRowsPerWrite; count ++){
							sb.append(frequencyDemodMapEntry.getValue().poll());
						}
						
						String filePath = "";
						if(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isTraining()){
							filePath = DataPoolTools.root_path + "\\data\\training\\" + deviceNum + "\\" 
												+ frequencyBandMapEntry.getKey()
												+ "\\demodulation\\" + frequencyDemodMapEntry.getKey() + ".js";
						} else if(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isWarning()){
							filePath = DataPoolTools.root_path + "\\data\\history\\" + deviceNum + "\\" 
												+ frequencyBandMapEntry.getKey()
												+ "\\demodulation\\statistics\\" + frequencyDemodMapEntry.getKey() + "\\" + var + ".js";
						}
						
						File file = new File(filePath);
						if(!file.exists()){
							new File(file.getParent()).mkdirs();
							try {
								file.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						try {
							//开始写文件
							FileWriter writer = new FileWriter(filePath, true);
							writer.write(sb.toString());
							writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						for(DemodResultParam temp : DataPoolTools.deviceDemodParameterMap.get(deviceNum).getDemodResultParamList()){
							if(temp.getX() == frequencyDemodMapEntry.getKey()){
								if(!filePathExistTokenMap.containsKey(temp.getDemodulationPointId())){
									if(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isTraining()){
										filePathExistTokenMap.put(temp.getDemodulationPointId(), dataAnalysisService.saveDemodulationFilePath(filePath, temp.getIndex(), temp.getDemodulationPointId()));
									}
									
									break;
								}
								if(filePathExistTokenMap.containsKey(temp.getDemodulationPointId()) && !filePathExistTokenMap.get(temp.getDemodulationPointId())) {
									if(DataPoolTools.deviceDemodParameterMap.get(deviceNum).isTraining()){
										filePathExistTokenMap.put(temp.getDemodulationPointId(), dataAnalysisService.saveDemodulationFilePath(filePath, temp.getIndex(), temp.getDemodulationPointId()));
									}
									
									break;
								}
								break;
							}
						}
					}
				}
			}
			
			//休眠等待
			try {
				//System.out.println("开始休眠"+sleepTime+"毫秒");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		DataPoolTools.demodulateDataToFilePool.remove(deviceNum);
	}

}
