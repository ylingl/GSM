package utils.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import logic.spectra.SpectraService;
import mvc.history.WarningGroup;
import mvc.history.WarningHistory;
import tsme.table.originalData.bean.ORIGINALDATA;
import utils.DataPoolTools;
import utils.GTime;
import utils.file.FileUtil;

/**
 * 数据存储处理，存储为js文件，供前台js调用
 * 
 * @author kin
 *
 */
public class WaringDataFile implements Runnable {

	// 客户端上传的实时监控数据缓冲池
	//public static Queue<List<Double>> monitorDataPool = new LinkedList<List<Double>>();
	
	private SpectraService spectraService;
	
	private String accountId;
	
	private String deviceNum;
	
	private boolean isRunning = false;
	// 文件存储根目录
	private static String js_root_dir = "d:\\";
	// 每个文件内的数据行数
	private static final int totalRowsOneFile = 2000;

	private static final int sleepTime = 500;//单位为毫秒
	
	public WaringDataFile(SpectraService spectraService, String accountId, String deviceNum) {
		this.spectraService = spectraService;
		this.accountId = accountId;
		this.deviceNum = deviceNum;
	}

	// 保存为文件
	@Override
	public void run() {
		
		if(isRunning)
			return;
		
		isRunning = true;
		
		while (DataPoolTools.deviceMonitorParameterMap.get(deviceNum).isWarning()) {
			
			DataPoolTools.clearOverTimeMonitor();//触发清除所有超时的监控
				
			if (DataPoolTools.monitorDataToFilePool.get(deviceNum).isEmpty()){
				try {
					Thread.sleep(sleepTime);
					System.out.println("缓冲池空，休眠"+ sleepTime +"毫秒！");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			String var = GTime.getTime(GTime.YYYYMMDDhhmmssxxx);
			boolean cloneFlag = true;
			//freauencyBand-WarningDataList
			Map<String, ArrayList<WarningHistory>> warningHistoryMap = null;
			//freauencyBand-SpectraDataQueue
			for (Entry<String, Queue<ArrayList<Double>>> entry : DataPoolTools.monitorDataToFilePool.get(deviceNum).entrySet()){
				if (entry.getValue().size() < totalRowsOneFile) {
					break;
				} else {
					if(cloneFlag){
						//仅需克隆一次，克隆完毕后需清空
						warningHistoryMap = new HashMap<String, ArrayList<WarningHistory>>(DataPoolTools.warningHistoryMapToFile.get(deviceNum));
						DataPoolTools.warningHistoryMapToFile.remove(deviceNum);
						cloneFlag = false;
					}
					
					/**
					 * 生成频谱文件
					 */
					boolean initFlag = true;
					StringBuilder sb = new StringBuilder();
					
					sb.append("var dataArray" + var + " = [\r\n");
					for(int count = 0; count < totalRowsOneFile; count ++) {
						if (!initFlag){
							sb.append(",\r\n");
						}
						
						initFlag = false;
						
						sb.append('[');
						
						List<Double> data = entry.getValue().poll();
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
					String filePath = js_root_dir + "data\\history\\" + deviceNum + "\\" + entry.getKey() + "\\spectrum\\" + var + ".js";
					FileUtil.saveFile(filePath, sb.toString());
					
					//System.out.println(entry.getValue().size());
					//将文件路径保存到数据库
					ORIGINALDATA originalData = new ORIGINALDATA();
					
					originalData.setCreator_id(accountId);
					originalData.setDeviceFB_id(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getFrequencyBandMap().get(entry.getKey()));
					originalData.setFile_path(filePath);
					originalData.setDate(new Date());
					originalData.setActive(true);
					originalData.setCreate_time(System.currentTimeMillis());
					
					spectraService.saveOriginalData(originalData);
					
					
					/**
					 * 生成告警文件
					 */
					if(warningHistoryMap != null && warningHistoryMap.containsKey(entry.getKey())
						&& warningHistoryMap.get(entry.getKey()).size() > 0){
						
						String filePathAlarm = js_root_dir + "data\\history\\" + deviceNum + "\\" + entry.getKey() + "\\alarm\\" + var + ".js";
			
						File file = new File(filePathAlarm);
						if(!file.exists()){
							new File(file.getParent()).mkdirs();
							try {
								file.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						StringBuilder sbAlarm = new StringBuilder();
						for(WarningHistory wh : warningHistoryMap.get(entry.getKey())){
							sbAlarm.append("warningTime=").append(wh.getWarningTime()).append(";");
							sbAlarm.append("warningGroup:");
							for(WarningGroup wg : wh.getWarningGroupList()){
								sbAlarm.append("beginPoint=").append(wg.getBeginPoint().getX()).append(",").append(wg.getBeginPoint().getY()).append("&");
								sbAlarm.append("endPoint=").append(wg.getEndPoint().getX()).append(",").append(wg.getEndPoint().getY()).append("&");
								sbAlarm.append("centerFre=").append(wg.getCenterFrequency()).append("@");
							}
							sbAlarm.setCharAt(sbAlarm.length() - 1, '#');
							sbAlarm.append("\r\n");
						}
						sbAlarm.setLength(sbAlarm.length() - 2);
		
						try {
							//开始写文件
							FileWriter writer = new FileWriter(filePathAlarm, true);
							writer.write(sbAlarm.toString());
							writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						spectraService.saveOriginalAlarmDataFilePath(filePathAlarm, originalData.getId());
					}
					
					
					/**
					 * 生成解调文件
					 */
					//frequencyBand-frequency-解调结果列
					if(DataPoolTools.demodulateDataToFilePool.containsKey(deviceNum)
						&& DataPoolTools.demodulateDataToFilePool.get(deviceNum).containsKey(entry.getKey())) {

						//待解调点-解调结果列
						HashMap<Float, Queue<String>> frequencyDemodMap = DataPoolTools.demodulateDataToFilePool.get(deviceNum).get(entry.getKey());
						//待解调点-解调结果列
						for(Entry<Float, Queue<String>> frequencyDemodMapEntry : frequencyDemodMap.entrySet()) {
							/*String dataGT2 = DataPoolTools.demodulateDataToMonitorFilePool.get(deviceNum).
									get(frequencyBandMapEntry.getKey()).
									get(frequencyDemodMapEntry.getKey()).peek();
							
							System.out.println("Peak2 " + frequencyDemodMapEntry.getKey() + ":" + dataGT2);*/
							
							long length = frequencyDemodMapEntry.getValue().size();
							
							if(length > 0){
								String filePathDemod = js_root_dir + "data\\history\\" + deviceNum + "\\" 
														+ entry.getKey() + "\\demodulation\\" + var + "\\" 
														+ frequencyDemodMapEntry.getKey() + ".js";
					
								File file = new File(filePathDemod);
								if(!file.exists()){
									new File(file.getParent()).mkdirs();
									try {
										file.createNewFile();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
								StringBuilder sbDemod = new StringBuilder();
								for(int count = 0; count < length; count ++){
									sbDemod.append(frequencyDemodMapEntry.getValue().poll());
								}
								
								try {
									//开始写文件
									FileWriter writer = new FileWriter(filePathDemod, true);
									writer.write(sbDemod.toString());
									writer.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								spectraService.saveOriginalDemodDataFilePath(filePathDemod, frequencyDemodMapEntry.getKey(), originalData.getId());
							}
						}
						
						spectraService.batchSaveDemodulationResult(DataPoolTools.demodResultStaticMapToDB.get(deviceNum).get(entry.getKey()), filePath);
						DataPoolTools.demodResultStaticMapToDB.get(deviceNum).remove(entry.getKey());
					}
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
		DataPoolTools.monitorDataToFilePool.remove(deviceNum);

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

	// 判断文件个数
}
