package ws.spectra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import mvc.device.DemodResultStatic;
import mvc.spectra.SpectraData;
import net.sf.json.JSONObject;
import utils.DataPoolTools;
import ws.history.WsHistoryServer;

public class SpectraHandler implements WebSocketHandler {

	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		if(wss.isOpen()){
			wss.close();
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
		// TODO Auto-generated method stub
		WsSpectraServer wsSpectraServer = new WsSpectraServer();
		
		wsSpectraServer.setServer_code(WsHistoryServer.server_code_initial);
		
		JSONObject jsObj = new JSONObject();
		jsObj.put("wsSpectraServer", wsSpectraServer);
		
		wss.sendMessage(new TextMessage(jsObj.toString()));
	}

	@Override
	public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsObjRec = JSONObject.fromObject(wsm.getPayload());
		WsSpectraBrowser wsSpectraBrowser = (WsSpectraBrowser) JSONObject.toBean(jsObjRec, WsSpectraBrowser.class);
		
		switch(wsSpectraBrowser.getBrowser_code()){
			case WsSpectraBrowser.browser_code_close : {
				wss.close();
				break;
			}
			
			case WsSpectraBrowser.browser_code_normal : {
				WsSpectraServer wsSpectraServer = new WsSpectraServer();
				
				if(wsSpectraBrowser.getBusiness().equals("realtime")){
					if(DataPoolTools.spectraDataMap != null 
						&& DataPoolTools.spectraDataMap.containsKey(wsSpectraBrowser.getDeviceNum())){
						
						SpectraData spectraData = DataPoolTools.spectraDataMap.get(wsSpectraBrowser.getDeviceNum());
						wsSpectraServer.setSpectraData(spectraData);
					}
					
					if(DataPoolTools.demodResultStaticMap != null 
						&& DataPoolTools.demodResultStaticMap.containsKey(wsSpectraBrowser.getDeviceNum())){
						
						Map<String, List<List<Float>>> demodulationPointMapForChart = new HashMap<String, List<List<Float>>>();
						HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMap = DataPoolTools.demodResultStaticMap.get(wsSpectraBrowser.getDeviceNum());
						for(Entry<String, ArrayList<DemodResultStatic>> demodResultStaticEntry : demodResultStaticMap.entrySet()){
							List<List<Float>> demodulationPointList = new ArrayList<List<Float>>();
							for(DemodResultStatic temp : demodResultStaticEntry.getValue()){
								List<Float> tempPoint = new ArrayList<Float>();
								
								tempPoint.add(temp.getX());
								tempPoint.add((float) -140.0);//用于控制柱体高度
								tempPoint.add((float) -150.0);//默认为-150.0
								
								demodulationPointList.add(tempPoint);
							}
							
							demodulationPointMapForChart.put(demodResultStaticEntry.getKey(), demodulationPointList);
						}
						
						wsSpectraServer.setDemodulationPointMapForChart(demodulationPointMapForChart);
					}
					
					wsSpectraServer.setBusiness("realtime");
				}
				
				if(wsSpectraBrowser.getBusiness().equals("train")){
					if(DataPoolTools.showDataPool != null
						&& DataPoolTools.showDataPool.containsKey(wsSpectraBrowser.getDeviceNum())) {
						
						HashMap<String, ArrayList<Double>> spectrumMap = new HashMap<String, ArrayList<Double>>();
						for(String key : DataPoolTools.showDataPool.get(wsSpectraBrowser.getDeviceNum()).keySet()){
							ArrayList<Double> temp = new ArrayList<Double>();
							if(DataPoolTools.showDataPool.get(wsSpectraBrowser.getDeviceNum()).get(key).size() > 0){
								temp.addAll(DataPoolTools.showDataPool.get(wsSpectraBrowser.getDeviceNum()).get(key).poll());
								temp.remove(temp.size() - 1);//倒数第一个用于记录时间
								temp.remove(temp.size() - 1);//倒数第二个用于记录纬度
								temp.remove(temp.size() - 1);//倒数第三个用于记录经度
							}
							spectrumMap.put(key, temp);
							//System.out.println("showDataPoolDepth:" + DataPoolTools.showDataPool.get(deviceNum).get(key).size());
						}
						
						wsSpectraServer.setSpectrumMap(spectrumMap);
					}
					
					wsSpectraServer.setBusiness("train");
				}
				
				if(wsSpectraBrowser.getBusiness().equals("demod")){
					if(DataPoolTools.demodResultStaticMap != null 
						&& DataPoolTools.demodResultStaticMap.containsKey(wsSpectraBrowser.getDeviceNum())){
						
						Map<String, List<List<Float>>> demodulationPointMapForChart = new HashMap<String, List<List<Float>>>();
						HashMap<String, ArrayList<DemodResultStatic>> demodResultStaticMap = DataPoolTools.demodResultStaticMap.get(wsSpectraBrowser.getDeviceNum());
						for(Entry<String, ArrayList<DemodResultStatic>> demodResultStaticEntry : demodResultStaticMap.entrySet()){
							List<List<Float>> demodulationPointList = new ArrayList<List<Float>>();
							for(DemodResultStatic temp : demodResultStaticEntry.getValue()){
								List<Float> tempPoint = new ArrayList<Float>();
								
								tempPoint.add(temp.getX());
								tempPoint.add((float) -140.0);//用于控制柱体高度
								tempPoint.add((float) -150.0);//默认为-150.0
								
								demodulationPointList.add(tempPoint);
							}
							
							demodulationPointMapForChart.put(demodResultStaticEntry.getKey(), demodulationPointList);
						}
						
						wsSpectraServer.setDemodulationPointMapForChart(demodulationPointMapForChart);
					}
					
					wsSpectraServer.setBusiness("demod");
				}
				
				wsSpectraServer.setServer_code(WsHistoryServer.server_code_normal);
				wsSpectraServer.setTimestample(wsSpectraBrowser.getTimestample());
				
				JSONObject jsObj = new JSONObject();
				jsObj.put("wsSpectraServer", wsSpectraServer);
				TextMessage returnMessage = new TextMessage(jsObj.toString());
				
				wss.sendMessage(returnMessage);
				break;
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession wss, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		if(wss.isOpen()){
			wss.close();
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

}
