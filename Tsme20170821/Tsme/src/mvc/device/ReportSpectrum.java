package mvc.device;

import java.util.ArrayList;

import utils.DataPoolTools;

/**
 * 上报实时监控数据请求参数
 * @author kitty
 *
 */
public class ReportSpectrum extends Report {

	//private long time;// 数据产生的时间 ，时间戳
	
	private String spectrumData0;//上传的数据
	
	private String spectrumData1;
	
	private String spectrumData2;

	/*public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}*/

	public void addSpectrumToPool(double LNG, double LAT) {
		double create_time = (double) System.currentTimeMillis();
		
		String spectrumData0 = getSpectrumData0();
		ArrayList<ArrayList<Double>> vectorList0 = new ArrayList<ArrayList<Double>>();
		if(spectrumData0 != null && spectrumData0 != ""){
			String map[] = spectrumData0.split("=");
			String vector[] = map[1].split(";");
			double dif = 0;
			for(String tempV : vector) {
				String list[] = tempV.split(",");
				
				ArrayList<Double> dataList = new ArrayList<Double>();
				for(String temp : list){
					dataList.add(Double.parseDouble(temp));
				}
				
				dataList.add(LNG);
				dataList.add(LAT);
				dataList.add(create_time + dif);//最后一个数用于记录数据产生时间
				dif++;

				vectorList0.add(dataList);
			}

			//用于实时监控
			if(DataPoolTools.monitorDataPool.containsKey(getDevice_num())
				&& DataPoolTools.monitorDataPool.get(getDevice_num()).containsKey(map[0])) {
				//告警数据缓冲池上限约为3000
				if(DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList0.size(); i ++){
						DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).addAll(vectorList0);
			}
			
			//用于存储实时监控数据
			if(DataPoolTools.monitorDataToFilePool.containsKey(getDevice_num())
				&& DataPoolTools.monitorDataToFilePool.get(getDevice_num()).containsKey(map[0])) {
				//数据缓冲池上限约为3000
				if(DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList0.size(); i ++){
						DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).addAll(vectorList0);
			}
			
			//用于训练
			if(DataPoolTools.trainingDataPool.containsKey(getDevice_num())
				&& DataPoolTools.trainingDataPool.get(getDevice_num()).containsKey(map[0])) {
				//训练数据缓冲池上限约为3000
				if(DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList0.size(); i ++){
						DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).addAll(vectorList0);
				
				//训练显示数据缓冲池上限约为30
				if(DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).size() > 30){
					DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).clear();
				}
				
				DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).add(vectorList0.get(0));
				DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).add(vectorList0.get(1));
			}
			
		}
		
		String spectrumData1 = getSpectrumData1();
		ArrayList<ArrayList<Double>> vectorList1 = new ArrayList<ArrayList<Double>>();
		if(spectrumData1 != null && spectrumData1 != ""){
			String map[] = spectrumData1.split("=");
			String vector[] = map[1].split(";");
			double dif = 0;
			for(String tempV : vector) {
				String list[] = tempV.split(",");
				
				ArrayList<Double> dataList = new ArrayList<Double>();
				for(String temp : list){
					dataList.add(Double.parseDouble(temp));
				}
				dataList.add(LNG);
				dataList.add(LAT);
				dataList.add(create_time + dif);//最后一个数用于记录数据产生时间
				dif++;
				
				vectorList1.add(dataList);
			}
			
			//用于实时监控
			if(DataPoolTools.monitorDataPool.containsKey(getDevice_num())
				&& DataPoolTools.monitorDataPool.get(getDevice_num()).containsKey(map[0])) {
				if(DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList1.size(); i ++){
						DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).addAll(vectorList1);
			}
			
			//用于存储实时监控数据
			if(DataPoolTools.monitorDataToFilePool.containsKey(getDevice_num())
				&& DataPoolTools.monitorDataToFilePool.get(getDevice_num()).containsKey(map[0])) {
				//数据缓冲池上限约为3000
				if(DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList1.size(); i ++){
						DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).addAll(vectorList1);
			}
			
			//用于训练
			if(DataPoolTools.trainingDataPool.containsKey(getDevice_num())
				&& DataPoolTools.trainingDataPool.get(getDevice_num()).containsKey(map[0])) {
				if(DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList1.size(); i ++){
						DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).addAll(vectorList1);
				
				//训练显示数据缓冲池上限约为30
				if(DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).size() > 30){
					DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).clear();
				}
				
				DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).add(vectorList1.get(0));
				DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).add(vectorList1.get(1));
			}
			
		}
		
		String spectrumData2 = getSpectrumData2();
		ArrayList<ArrayList<Double>> vectorList2 = new ArrayList<ArrayList<Double>>();
		if(spectrumData2 != null && !spectrumData2.isEmpty()){
			String map[] = spectrumData2.split("=");
			String vector[] = map[1].split(";");
			double dif = 0;
			for(String tempV : vector) {
				String list[] = tempV.split(",");
				
				ArrayList<Double> dataList = new ArrayList<Double>();
				for(String temp : list){
					dataList.add(Double.parseDouble(temp));
				}
				dataList.add(LNG);
				dataList.add(LAT);
				dataList.add(create_time + dif);//最后一个数用于记录数据产生时间
				dif++;
				
				vectorList2.add(dataList);
			}
			
			//用于实时监控
			if(DataPoolTools.monitorDataPool.containsKey(getDevice_num())
				&& DataPoolTools.monitorDataPool.get(getDevice_num()).containsKey(map[0])) {
				if(DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList2.size(); i ++){
						DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).addAll(vectorList2);
				//System.out.println("monitorDataPool:" + map[0] + "-" + DataPoolTools.monitorDataPool.get(getDevice_num()).get(map[0]).size());
			}
			
			//用于存储实时监控数据
			if(DataPoolTools.monitorDataToFilePool.containsKey(getDevice_num())
				&& DataPoolTools.monitorDataToFilePool.get(getDevice_num()).containsKey(map[0])) {
				//数据缓冲池上限约为3000
				if(DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList2.size(); i ++){
						DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.monitorDataToFilePool.get(getDevice_num()).get(map[0]).addAll(vectorList2);
			}
			
			//用于训练
			if(DataPoolTools.trainingDataPool.containsKey(getDevice_num())
				&& DataPoolTools.trainingDataPool.get(getDevice_num()).containsKey(map[0])) {
				if(DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).size() > 3000){
					for(int i = 0; i < vectorList2.size(); i ++){
						DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).poll();
					}
				}
				DataPoolTools.trainingDataPool.get(getDevice_num()).get(map[0]).addAll(vectorList2);
				
				//训练显示数据缓冲池上限约为30
				if(DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).size() > 30){
					DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).clear();
				}
				
				DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).add(vectorList2.get(0));
				DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).add(vectorList2.get(1));
				
				//System.out.println("showDataPool:" + map[0] + "-" + DataPoolTools.showDataPool.get(getDevice_num()).get(map[0]).size());
			}
		}
	}

	public String getSpectrumData0() {
		return spectrumData0;
	}

	public void setSpectrumData0(String spectrumData0) {
		this.spectrumData0 = spectrumData0;
	}

	public String getSpectrumData1() {
		return spectrumData1;
	}

	public void setSpectrumData1(String spectrumData1) {
		this.spectrumData1 = spectrumData1;
	}

	public String getSpectrumData2() {
		return spectrumData2;
	}

	public void setSpectrumData2(String spectrumData2) {
		this.spectrumData2 = spectrumData2;
	}

}
