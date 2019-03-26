package mvc.device;

import utils.DataPoolTools;
import utils.DemodParameter;
import utils.DemodParameter.si_type_enum;

public class ReportDemodulation extends Report {

	private String demodData;

	public void addDemodResultToPool(DemodParameter demodParam){
		String[] demodResultGroup = demodData.split("\r\n");
		
		String headS = "\r\n";
		if(demodParam.getSi_type().equals(si_type_enum.third)){
			for(int i = 0; i < demodResultGroup.length; i ++){
				//每包数据均有格式相同的包头，共四行数据
				if(demodResultGroup[i].contains("MeasResult: -")){
					if(demodResultGroup[i + 3] == ""){
						headS = headS + demodResultGroup[i] + "\r\n" +
								demodResultGroup[i + 1] + "\r\n" +
								demodResultGroup[i + 2] + "\r\n"; 
						i = i + 2;
					} else {
						headS = headS + demodResultGroup[i] + "\r\n" +
								demodResultGroup[i + 1] + "\r\n" +
								demodResultGroup[i + 2] + "\r\n" +
								demodResultGroup[i + 3] + "\r\n"; 
						i = i + 3;
					}
				}
				
				/*****开始解析包数据*****/
				if(demodResultGroup[i].contains("DemodResult") && demodResultGroup[i + 1].contains("Frequency Index")){
					int index = -1;
					String[] param = demodResultGroup[i + 1].split(":");
					index = Integer.parseInt(param[1].replaceAll(" ", ""));
					
					putDataIntoDemodDataPool(index, demodParam, demodData, demodResultGroup);
					demodResultGroup = null;
					break;
				}
				
				if(demodResultGroup[i].contains("MeasResult.ListSCHInfoResults.dwCount")){
					int index = -1;
					int subCount = -1;
					String[] param1 = demodResultGroup[i].split("=");
					subCount = Integer.parseInt(param1[1].replaceAll(" ", ""));
					
					for(int j = 0; j < subCount; j ++){
						if(demodResultGroup[i + j * 7 + 1].contains("SCHInfo Result for Frequency Index")){
							String[] param2 = demodResultGroup[i + j * 7 + 1].split(":");
							index = Integer.parseInt(param2[1].replaceAll(" ", ""));
							
							String dataS = null;
							dataS = headS + demodResultGroup[i + j * 7 + 1] + "\r\n" +
									demodResultGroup[i + j * 7 + 2] + "\r\n" +
									demodResultGroup[i + j * 7 + 3] + "\r\n" +
									demodResultGroup[i + j * 7 + 4] + "\r\n" +
									demodResultGroup[i + j * 7 + 5] + "\r\n" +
									demodResultGroup[i + j * 7 + 6] + "\r\n" +
									demodResultGroup[i + j * 7 + 7] + "\r\n";
							
							String[] dataG = new String[7];
							dataG[0] = demodResultGroup[i + j * 7 + 1];
							dataG[1] = demodResultGroup[i + j * 7 + 2];
							dataG[2] = demodResultGroup[i + j * 7 + 3];
							dataG[3] = demodResultGroup[i + j * 7 + 4];
							dataG[4] = demodResultGroup[i + j * 7 + 5];
							dataG[5] = demodResultGroup[i + j * 7 + 6];
							dataG[6] = demodResultGroup[i + j * 7 + 7];
							
							putDataIntoDemodDataPool(index, demodParam, dataS, dataG);
						}
					}
					
					i = i + 7 * subCount;
				}
				
				if(demodResultGroup[i].contains("MeasResult.ListCellIdentResults.dwCount")){
					int index = -1;
					int subCount = -1;
					String[] param1 = demodResultGroup[i].split("=");
					subCount = Integer.parseInt(param1[1].replaceAll(" ", ""));
					
					for(int j = 0; j < subCount; j ++){
						if(demodResultGroup[i + j * 4 + 1].contains("CellIdent Result for Frequency Index")){
							String[] param2 = demodResultGroup[i + j * 4 + 1].split(":");
							index = Integer.parseInt(param2[1].replaceAll(" ", ""));
							
							String dataS = null;
							dataS = headS + demodResultGroup[i + j * 4 + 1] + "\r\n" +
									demodResultGroup[i + j * 4 + 2] + "\r\n" +
									demodResultGroup[i + j * 4 + 3] + "\r\n" +
									demodResultGroup[i + j * 4 + 4] + "\r\n";
							
							String[] dataG = new String[4];
							dataG[0] = demodResultGroup[i + j * 4 + 1];
							dataG[1] = demodResultGroup[i + j * 4 + 2];
							dataG[2] = demodResultGroup[i + j * 4 + 3];
							dataG[3] = demodResultGroup[i + j * 4 + 4];
							
							putDataIntoDemodDataPool(index, demodParam, dataS, dataG);
						}
					}
					
					i = i + 4 * subCount;
				}
				
				if(demodResultGroup[i].contains("MeasResult.ListPowerResults.dwCount")){
					int index = -1;
					int subCount = -1;
					String[] param1 = demodResultGroup[i].split("=");
					subCount = Integer.parseInt(param1[1].replaceAll(" ", ""));
					
					for(int j = 0; j < subCount; j ++){
						if(demodResultGroup[i + j * 8 + 1].contains("Power Result for Frequency Index")){
							String[] param2 = demodResultGroup[i + j * 8 + 1].split(":");
							index = Integer.parseInt(param2[1].replaceAll(" ", ""));
							
							String dataS = null;
							dataS = headS + demodResultGroup[i + j * 8 + 1] + "\r\n" +
									demodResultGroup[i + j * 8 + 2] + "\r\n" +
									demodResultGroup[i + j * 8 + 3] + "\r\n" +
									demodResultGroup[i + j * 8 + 4] + "\r\n" +
									demodResultGroup[i + j * 8 + 5] + "\r\n" +
									demodResultGroup[i + j * 8 + 6] + "\r\n" +
									demodResultGroup[i + j * 8 + 7] + "\r\n" +
									demodResultGroup[i + j * 8 + 8] + "\r\n";
							
							String[] dataG = new String[8];
							dataG[0] = demodResultGroup[i + j * 8 + 1];
							dataG[1] = demodResultGroup[i + j * 8 + 2];
							dataG[2] = demodResultGroup[i + j * 8 + 3];
							dataG[3] = demodResultGroup[i + j * 8 + 4];
							dataG[4] = demodResultGroup[i + j * 8 + 5];
							dataG[5] = demodResultGroup[i + j * 8 + 6];
							dataG[6] = demodResultGroup[i + j * 8 + 7];
							dataG[7] = demodResultGroup[i + j * 8 + 8];
							
							putDataIntoDemodDataPool(index, demodParam, dataS, dataG);
						}
					}
					
					i = i + 8 * subCount;
				}
				
				if(demodResultGroup[i].contains("MeasResult.ListExecutedMeasSpec.dwCount")){
					int index = -1;
					int subCount = -1;
					String[] param1 = demodResultGroup[i].split("=");
					subCount = Integer.parseInt(param1[1].replaceAll(" ", ""));
					
					for(int j = 0; j < subCount; j ++){
						if(demodResultGroup[i + j * 9 + 1].contains("Frequency Index")){
							String[] param2 = demodResultGroup[i + j * 9 + 1].split(":");
							index = Integer.parseInt(param2[1].replaceAll(" ", ""));
							
							String dataS = null;
							dataS = headS + demodResultGroup[i + j * 9 + 1] + "\r\n" +
									demodResultGroup[i + j * 9 + 2] + "\r\n" +
									demodResultGroup[i + j * 9 + 3] + "\r\n" +
									demodResultGroup[i + j * 9 + 4] + "\r\n" +
									demodResultGroup[i + j * 9 + 5] + "\r\n" +
									demodResultGroup[i + j * 9 + 6] + "\r\n" +
									demodResultGroup[i + j * 9 + 7] + "\r\n" +
									demodResultGroup[i + j * 9 + 8] + "\r\n" +
									demodResultGroup[i + j * 9 + 9] + "\r\n";
							
							String[] dataG = null;
							putDataIntoDemodDataPool(index, demodParam, dataS, dataG);
						}
					}
					
					i = i + 9 * subCount;
				}
				
				if(demodResultGroup[i].contains("Channel Power Measurement Results:")){
					i = i + 8;
				}
			}
		}
	}
	
	private void putDataIntoDemodDataPool(int index, DemodParameter demodParam, String dataS, String[] dataG) {
		//frequencyBand-频点
		for(DemodResultParam demodResultParam : demodParam.getDemodResultParamList()) {
			if(demodResultParam.getIndex() == index) {
				if(dataG != null && dataG[0] != null &&
					DataPoolTools.demodulateDataPool.containsKey(getDevice_num())
					&& DataPoolTools.demodulateDataPool.get(getDevice_num()).
					containsKey(demodResultParam.getFrequencyBand())
					&& DataPoolTools.demodulateDataPool.get(getDevice_num()).
					get(demodResultParam.getFrequencyBand()).
					containsKey(demodResultParam.getX())) {
					
					//System.out.println("Orig " + demodResultParam.getX() + "-" + index + ":" + dataG[0] + dataG[1] + dataG[2] + dataG[3]);
					
					DataPoolTools.demodulateDataPool.get(getDevice_num()).
					get(demodResultParam.getFrequencyBand()).
					get(demodResultParam.getX()).offer(dataG);
					
					/*String[] dataGT1 = DataPoolTools.demodulateDataPool.get(getDevice_num()).
							get(demodResultParam.getFrequencyBand()).
							get(demodResultParam.getX()).peek();
					
					System.out.println("Peak1 " + demodResultParam.getX() + "-" + index + ":" + dataGT1[0] 
							+ dataGT1[1] + dataGT1[2] + dataGT1[3]);*/
				}
					
				if(dataS != null && dataS.length() > 0 && 
					DataPoolTools.demodulateDataToFilePool.containsKey(getDevice_num()) && 
					DataPoolTools.demodulateDataToFilePool.get(getDevice_num()).
					containsKey(demodResultParam.getFrequencyBand())
					&& DataPoolTools.demodulateDataToFilePool.get(getDevice_num()).
					get(demodResultParam.getFrequencyBand()).
					containsKey(demodResultParam.getX())) {
						
					DataPoolTools.demodulateDataToFilePool.get(getDevice_num()).
					get(demodResultParam.getFrequencyBand()).
					get(demodResultParam.getX()).offer(dataS);
					
					/*String dataGT2 = DataPoolTools.demodulateDataToFilePool.get(getDevice_num()).
										get(demodResultParam.getFrequencyBand()).
										get(demodResultParam.getX()).peek();
			
					System.out.println("Peak " + demodResultParam.getX() + "-" + index + ":" + dataGT2);*/
				}
			}
		}
	}

	public String getDemodData() {
		return demodData;
	}

	public void setDemodData(String demodData) {
		this.demodData = demodData;
	}
}
