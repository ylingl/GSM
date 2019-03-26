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
 * ���ݴ洢�����洢Ϊjs�ļ�����ǰ̨js����
 * 
 * @author kin
 *
 */
public class TrainingDataFile implements Runnable {

	// �ͻ����ϴ���ʵʱ������ݻ����
	//public static Queue<List<Double>> monitorDataPool = new LinkedList<List<Double>>();
	
	private DataAnalysisService dataAnalysisService;
	
	private String deviceNum;
	
	private boolean isRunning = false;
	// �ļ��洢��Ŀ¼
	private static String js_root_dir = "d:\\";
	// ÿ���ļ��ڵ���������
	private static final int totalRowsOneFile = 2000;

	private static final int sleepTime = 1000 * 5 * 1;// ���û������10����һ��
	
	public TrainingDataFile(DataAnalysisService dataAnalysisService, String deviceNum) {
		this.dataAnalysisService = dataAnalysisService;
		this.deviceNum = deviceNum;
	}

	// ����Ϊ�ļ�
	@Override
	public void run() {
		
		if(isRunning)
			return;
		
		isRunning = true;
		
		while (DataPoolTools.deviceMonitorParameterMap.containsKey(deviceNum) && 
				DataPoolTools.deviceMonitorParameterMap.get(deviceNum).isTraining()) {
			
			DataPoolTools.clearOverTimeMonitor();//����������г�ʱ�ļ��
				
			if (DataPoolTools.trainingDataPool.get(deviceNum).isEmpty()){
				try {
					Thread.sleep(1000);
					System.out.println("��ʼ����1��");
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
					// �����ļ�
					boolean initFlag = true;
					StringBuilder sb = new StringBuilder();
					
					sb.append("var dataArray" + var + " = [\r\n");
					
					for(int count = 0; count < totalRowsOneFile; count ++) {
						if (!initFlag)
							sb.append(",\r\n");
						
						initFlag = false;
						
						sb.append('[');
						
						List<Double> data = DataPoolTools.trainingDataPool.get(deviceNum).get(entry.getKey()).poll();
						
						for (int j = 0; j < data.size() - 1; j ++) {//���һ������¼�����ݲ���ʱ��			
							sb.append(data.get(j));
							sb.append(",");
						}
						
						sb.append(Math.round(data.get(data.size() - 1)));//���һ������¼�����ݲ���ʱ��
						sb.append("]");
						
					}
							
					sb.append("\r\n");
					sb.append("];");
					
					//��ʼд�ļ�
					String filePath = js_root_dir + "data\\training\\" + deviceNum + "\\" + entry.getKey() + "\\" + var + ".js";
					FileUtil.saveFile(filePath, sb.toString());
					
					//System.out.println(filePath);
					
					//���ļ�·�����浽���ݿ�
					TRAININGDATA trainingData = new TRAININGDATA();
					
					trainingData.setFile_path(filePath);
					trainingData.setFrequencyBand_id(DataPoolTools.deviceMonitorParameterMap.get(deviceNum).getFrequencyBandMap().get(entry.getKey()));
					trainingData.setActive(true);
					trainingData.setCreate_time(System.currentTimeMillis());
					
					dataAnalysisService.saveTrainingData(trainingData);
				}
			}
			
			//���ߵȴ�
			try {
				//System.out.println("��ʼ����"+sleepTime/1000+"��");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	
		DataPoolTools.deviceMonitorParameterMap.remove(deviceNum);
		DataPoolTools.trainingDataPool.remove(deviceNum);
		DataPoolTools.showDataPool.remove(deviceNum);

	}

	// д�ı��ļ�

	// ��ȡ�ļ���С

	// ��ȡ�ļ��б�
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
