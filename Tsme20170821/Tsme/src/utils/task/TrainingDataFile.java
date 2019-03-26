package utils.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;

import logic.data.DataAnalysisService;
import tsme.table.trainingData.bean.TRAININGDATA;
import utils.DataPoolTools;
import utils.GTime;
import utils.file.FileUtil;

/**
 * 数据存储处理，存储为js文件，供前台js调用
 * 
 * @author kin
 *
 */
public class TrainingDataFile implements Runnable {

	// 客户端上传的实时监控数据缓冲池
	//public static Queue<List<Double>> monitorDataPool = new LinkedList<List<Double>>();
	
	private DataAnalysisService dataAnalysisService;
	
	private String deviceNum;
	
	private boolean isRunning = false;
	// 文件存储根目录
	private static String js_root_dir = "d:\\";
	// 每个文件内的数据行数
	private static final int totalRowsOneFile = 2000;

	private static final int sleepTime = 1000 * 5 * 1;// 如果没有数据10秒检查一次
	
	public TrainingDataFile(DataAnalysisService dataAnalysisService, String deviceNum) {
		this.dataAnalysisService = dataAnalysisService;
		this.deviceNum = deviceNum;
	}

	// 保存为文件
	@Override
	public void run() {
		
		if(isRunning)
			return;
		
		isRunning = true;
		
		while (DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum) && 
				DataPoolTools.deviceMonitorParameterMap.get(deviceNum).isTraining()) {
			
			DataPoolTools.clearOverTimeMonitor();//触发清除所有超时的监控
				
			if (DataPoolTools.trainingDataPool.get(deviceNum).isEmpty()){
				try {
					Thread.sleep(1000);
					System.out.println("开始休眠1秒");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			String var = GTime.getTime(GTime.YYYYMMDDhhmmssxxx);
			
			for(Entry<String, Queue<ArrayList<Double>>> entry : DataPoolTools.trainingDataPool.get(deviceNum).entrySet()) {
				if (entry.getValue().size() < totalRowsOneFile) {
					break;
				} else {
					// 生成文件
					boolean initFlag = true;
					StringBuilder sb = new StringBuilder();
					
					sb.append("var dataArray" + var + " = [\r\n");
					
					for(int count = 0; count < totalRowsOneFile; count ++) {
						if (!initFlag)
							sb.append(",\r\n");
						
						initFlag = false;
						
						sb.append('[');
						
						List<Double> data = DataPoolTools.trainingDataPool.get(deviceNum).get(entry.getKey()).poll();
						
						for (int j = 0; j < data.size() - 1; j ++) {//最后一个数记录了数据产生时间			
							sb.append(data.get(j));
							sb.append(",");
						}
						
						sb.append(Math.round(data.get(data.size() - 1)));//最后一个数记录了数据产生时间
						sb.append("]");
						
					}
							
					sb.append("\r\n");
					sb.append("];");
					
					//开始写文件
					String filePath = js_root_dir + "data\\training\\" + deviceNum + "\\" + entry.getKey() + "\\" + var + ".js";
					FileUtil.saveFile(filePath, sb.toString());
					
					//System.out.println(filePath);
					
					//将文件路径保存到数据库
					TRAININGDATA trainingData = new TRAININGDATA();
					
					trainingData.setFile_path(filePath);
					trainingData.setFrequencyBand_id(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getFrequencyBandMap().get(entry.getKey()));
					trainingData.setActive(true);
					trainingData.setCreate_time(System.currentTimeMillis());
					
					dataAnalysisService.saveTrainingData(trainingData);
				}
			}
			
			//休眠等待
			try {
				//System.out.println("开始休眠"+sleepTime/1000+"秒");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	
		DataPoolTools.deviceMonitorParameterMap.remove(deviceNum);
		DataPoolTools.trainingDataPool.remove(deviceNum);
		DataPoolTools.showDataPool.remove(deviceNum);

	}

	// 写文本文件

	// 读取文件大小

	// 读取文件列表
	public static ArrayList<String> getDataFileList() {
		File dir = new File(js_root_dir + "data");
		File[] listFile = dir.listFiles();
		ArrayList<String> fileList = new ArrayList<String>();
		for (int i = 0; i < listFile.length; i++) {
			File file = listFile[i];
			if(file.isFile() && file.getName().endsWith(".js"))
				fileList.add(file.getName());
		}
		return fileList;
	}
	
}
