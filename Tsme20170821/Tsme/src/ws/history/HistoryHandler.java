package ws.history;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import logic.data.bean.BeanCoordinate;
import mvc.history.WarningGroup;
import mvc.history.WarningHistory;
import mvc.spectra.WarningData;
import net.sf.json.JSONObject;
import utils.DataPoolTools;

public class HistoryHandler implements WebSocketHandler {
	
	private static final int basicLine = 150;//��׼�ο���
	private static final double ratio = 0.1;
	private static final int pointNum = 2;
	private static final double threshold = -90.0;

	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		if(wss.isOpen()){
			wss.close();
		}
		
		String httpSessionId = (String) wss.getAttributes().get("HTTP.SESSION.ID");
		DataPoolTools.httpSessionIdCenterFrequencyMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdSpetrumMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdWarningDataMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdWarningHistoryMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdWarningPointMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdIndexMap.remove(httpSessionId);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
		// TODO Auto-generated method stub
		WsHistoryServer wsHistoryServer = new WsHistoryServer();
		
		wsHistoryServer.setServer_code(WsHistoryServer.server_code_initial);
		DataPoolTools.httpSessionIdIndexMap.put((String) wss.getAttributes().get("HTTP.SESSION.ID"), -1);
		
		JSONObject jsObj = new JSONObject();
		jsObj.put("wsHistoryServer", wsHistoryServer);
		
		wss.sendMessage(new TextMessage(jsObj.toString()));
	}

	@Override
	public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
		JSONObject jsObjRec = JSONObject.fromObject(wsm.getPayload());
		WsHistoryBrowser wsHistoryBrowser = (WsHistoryBrowser) JSONObject.toBean(jsObjRec, WsHistoryBrowser.class);
		String httpSessionId = (String) wss.getAttributes().get("HTTP.SESSION.ID");
		
		switch(wsHistoryBrowser.getBrowser_code()){
			case WsHistoryBrowser.browser_code_close : {
				wss.close();
				
				DataPoolTools.httpSessionIdCenterFrequencyMap.remove(httpSessionId);
				DataPoolTools.httpSessionIdSpetrumMap.remove(httpSessionId);
				DataPoolTools.httpSessionIdWarningDataMap.remove(httpSessionId);
				DataPoolTools.httpSessionIdWarningHistoryMap.remove(httpSessionId);
				DataPoolTools.httpSessionIdWarningPointMap.remove(httpSessionId);
				DataPoolTools.httpSessionIdIndexMap.remove(httpSessionId);
				
				break;
			}
			
			case WsHistoryBrowser.browser_code_normal : {
				WsHistoryServer wsHistoryServer = new WsHistoryServer();
				int index = 0;
				
				//ԭʼ����
				List<ArrayList<Double>> originalSpetrum = new ArrayList<ArrayList<Double>>();
				if(DataPoolTools.httpSessionIdSpetrumMap != null 
					&& DataPoolTools.httpSessionIdSpetrumMap.containsKey(httpSessionId)){
					originalSpetrum = DataPoolTools.httpSessionIdSpetrumMap.get(httpSessionId);
				} else {
					return;
				}
				
				//Ԥ������
				List<BeanCoordinate> warningLine = new ArrayList<BeanCoordinate>();
				if(DataPoolTools.httpSessionIdWarningPointMap != null
					&& DataPoolTools.httpSessionIdWarningPointMap.containsKey(httpSessionId)){
					warningLine = DataPoolTools.httpSessionIdWarningPointMap.get(httpSessionId);
				} else {
					return;
				}
				
				List<WarningData> warnDataList = new ArrayList<WarningData>();
				if(wsHistoryBrowser.isSerial()){
					wsHistoryServer.setSerial(true);
					if(DataPoolTools.httpSessionIdWarningDataMap != null 
						&& DataPoolTools.httpSessionIdWarningDataMap.containsKey(httpSessionId)){
						
						warnDataList = DataPoolTools.httpSessionIdWarningDataMap.get(httpSessionId);
						
						if(DataPoolTools.httpSessionIdCenterFrequencyMap != null 
							&& DataPoolTools.httpSessionIdCenterFrequencyMap.containsKey(httpSessionId)){//���ڸ澯�ͷ�
							
							Float centerFrequency = DataPoolTools.httpSessionIdCenterFrequencyMap.get(httpSessionId);
							for(WarningData temp : warnDataList){
								if(temp.getCenterFrequency() == centerFrequency){
									temp.setVisible(false);
									DataPoolTools.httpSessionIdCenterFrequencyMap.remove(httpSessionId);
									break;
								}
							}
						}
					}
				} else {
					wsHistoryServer.setSerial(false);
				}
				
				if(wsHistoryBrowser.getDirection().equalsIgnoreCase("forward")){
					if(DataPoolTools.httpSessionIdIndexMap.get(httpSessionId) < (originalSpetrum.size() - 1)){
						index = DataPoolTools.httpSessionIdIndexMap.get(httpSessionId) + 1;
					} else {
						index = originalSpetrum.size() - 1;
					}					
				} else if(wsHistoryBrowser.getDirection().equalsIgnoreCase("backward")){
					if(DataPoolTools.httpSessionIdIndexMap.get(httpSessionId) > 0){
						index = DataPoolTools.httpSessionIdIndexMap.get(httpSessionId) - 1;
					} else {
						index = 0;
					}
				} else if(wsHistoryBrowser.getDirection().equalsIgnoreCase("leap")){
					if(wsHistoryBrowser.getIndex() >= 0 && wsHistoryBrowser.getIndex() <= (originalSpetrum.size() - 1)){
						index = wsHistoryBrowser.getIndex();
					} else {
						index = DataPoolTools.httpSessionIdIndexMap.get(httpSessionId);
					}
				}
				
				List<Double> spDataList = originalSpetrum.get(index);
				if(spDataList != null && warningLine != null && spDataList.size() - 3 == warningLine.size()) {//���һ������¼�����ݲ���ʱ�䣬�����ڶ�����γ�ȣ������������Ǿ���
					int count = 0;
					float beginFrequency = 0, beginY = -150, endFrequency = 0, endY = -150;
					
					//����ͳ�ƣ���ʽ����ɾ��
					List<WarningHistory> warningHistoryList = new ArrayList<WarningHistory>();
					if(DataPoolTools.httpSessionIdWarningHistoryMap != null 
						&& DataPoolTools.httpSessionIdWarningHistoryMap.containsKey(httpSessionId)){
						warningHistoryList = DataPoolTools.httpSessionIdWarningHistoryMap.get(httpSessionId);
					}	
					//����ͳ�ƣ���ʽ����ɾ��
					
					for (int i = 0; i < spDataList.size() - 3; i ++) {//���һ������¼�����ݲ���ʱ�䣬�����ڶ�����γ�ȣ������������Ǿ���
						if(spDataList.get(i) > (basicLine + warningLine.get(i).getY()) * ratio + warningLine.get(i).getY()
							&& spDataList.get(i) > threshold) {
							if(count == 0){
								beginFrequency = warningLine.get(i).getX();
								
								//����ͳ�ƣ���ʽ����ɾ��
								beginY = spDataList.get(i).floatValue();
								//����ͳ�ƣ���ʽ����ɾ��
							}
							count ++;
						} else {
							if(count >= pointNum && beginFrequency > 0){
								count = 0;
								endFrequency = warningLine.get(i).getX();
								
								//����ͳ�ƣ���ʽ����ɾ��
								endY = spDataList.get(i).intValue();
								//����ͳ�ƣ���ʽ����ɾ��
								
								WarningData temp = new WarningData();
								
								BigDecimal start = new BigDecimal(beginFrequency);
								BigDecimal stop = new BigDecimal(endFrequency);
								BigDecimal center = new BigDecimal((beginFrequency + endFrequency) / 2);
								
								temp.setStartFrequency(start.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
								temp.setStopFrequency(stop.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
								temp.setCenterFrequency(center.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
								temp.setCurrentTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(Math.round(spDataList.get(spDataList.size() - 1)))));
								
								aggregateList(warnDataList, temp);
								
								//����ͳ�ƣ���ʽ����ɾ��//
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
								
								aggregateList(warningHistoryList, Long.toString(Math.round(spDataList.get(spDataList.size() - 1))), warningGroup);
								//����ͳ�ƣ���ʽ����ɾ��//
							} else {
								count = 0;
								beginFrequency = 0;
								beginY = -150;
								endFrequency = 0;
								endY = -150;
							}
						}
					}
					
					DataPoolTools.httpSessionIdWarningDataMap.put(httpSessionId, warnDataList);
					DataPoolTools.httpSessionIdIndexMap.put(httpSessionId, index);
					
					//����ͳ�ƣ���ʽ����ɾ��//
					DataPoolTools.httpSessionIdWarningHistoryMap.put(httpSessionId, warningHistoryList);
					//����ͳ�ƣ���ʽ����ɾ��//
				}
				
				wsHistoryServer.setServer_code(WsHistoryServer.server_code_normal);
				wsHistoryServer.setIndex(index);
				wsHistoryServer.setTimestample(wsHistoryBrowser.getTimestample());
				wsHistoryServer.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(Math.round(spDataList.get(spDataList.size() - 1)))));
				wsHistoryServer.setLAT(String.format("%.4f", spDataList.get(spDataList.size() - 2)));
				wsHistoryServer.setLNG(String.format("%.4f", spDataList.get(spDataList.size() - 3)));
				wsHistoryServer.setSpDataList(spDataList.subList(0, spDataList.size() - 3));
				wsHistoryServer.setWarnDataList(warnDataList);
				
				JSONObject jsObj = new JSONObject();
				jsObj.put("wsHistoryServer", wsHistoryServer);
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
		
		String httpSessionId = (String) wss.getAttributes().get("HTTP.SESSION.ID");
		DataPoolTools.httpSessionIdCenterFrequencyMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdSpetrumMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdWarningDataMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdWarningHistoryMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdWarningPointMap.remove(httpSessionId);
		DataPoolTools.httpSessionIdIndexMap.remove(httpSessionId);
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

	private List<WarningData> aggregateList(List<WarningData> warningDataList, WarningData warningData){
		if(warningDataList == null || warningDataList.isEmpty()) {//��ʼ״̬,warningDataList����ֵ
			List<String> warningTimeList = new ArrayList<String>();
			warningTimeList.add(warningData.getCurrentTime());
			
			warningData.setWarningTimeList(warningTimeList);
			warningDataList.add(warningData);
		} else {
			boolean addEnable = true;
			
			for(int i = 0; i < warningDataList.size(); i ++){
				if(warningData.getCenterFrequency() < warningDataList.get(i).getCenterFrequency()){
					addEnable = false;
					
					List<String> warningTimeList = new ArrayList<String>();
					warningTimeList.add(warningData.getCurrentTime());
					
					warningData.setWarningTimeList(warningTimeList);
					warningDataList.add(i, warningData);
					break;
				}
				
				if(warningDataList.get(i).getCenterFrequency() == warningData.getCenterFrequency()) {
					addEnable = false;
					
					WarningData warningDataOld = warningDataList.get(i);
					
					warningDataOld.getWarningTimeList().add(warningData.getCurrentTime());
					warningDataOld.setNumber(warningDataOld.getNumber() + 1);
					warningDataOld.setCurrentTime(warningData.getCurrentTime());
					
					warningDataList.set(i, warningDataOld);
					break;
				}
			}
			
			if(addEnable){
				List<String> warningTimeList = new ArrayList<String>();
				warningTimeList.add(warningData.getCurrentTime());
				
				warningData.setWarningTimeList(warningTimeList);
				warningDataList.add(warningData);
			}
		}
		
		return warningDataList;
	}
	
	private void aggregateList(List<WarningHistory> warningHistoryList, String warningTime, WarningGroup warningGroup){
		if(warningHistoryList == null) return;
		if(warningHistoryList.isEmpty()) {//��ʼ״̬,warningHistoryList����ֵ
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
