package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

import javax.servlet.http.HttpServletRequest;

public class AddressTools {
	
	public static String getRemoteIPAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		return ip;
	}
	
	public static String getLocalMACAddress(InetAddress ia) throws Exception{  
        //�������ӿڶ��󣨼������������õ�mac��ַ��mac��ַ������һ��byte�����С�  
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();  
          
        //��������ǰ�mac��ַƴװ��String  
        StringBuffer sb = new StringBuffer();  
          
        for(int i=0;i<mac.length;i++){  
            if(i!=0){  
                sb.append("-");  
            }  
            //mac[i] & 0xFF ��Ϊ�˰�byteת��Ϊ������  
            String s = Integer.toHexString(mac[i] & 0xFF);  
            sb.append(s.length()==1?0+s:s);  
        }  
          
        //���ַ�������Сд��ĸ��Ϊ��д��Ϊ�����mac��ַ������  
        return sb.toString().toUpperCase();  
    }
	
	public static String getRemoteMACAdress(String remoteIP) {
		
        String str = null;
        String macAddress = null;
        
        try {
        	//����Windows������
            Process p = Runtime.getRuntime().exec("nbtstat -A " + remoteIP);  
            InputStreamReader ir = new InputStreamReader(p.getInputStream());  
            LineNumberReader input = new LineNumberReader(ir);  
            while(true) {  
                str = input.readLine();  
                if (str != null) {  
                    if (str.indexOf("MAC Address") > 1) {  
                        macAddress = str.substring(str.indexOf("MAC Address") + 14);  
                        break;  
                    }  
                }  
            }  
        } catch (IOException e) {  
        	
            e.printStackTrace(System.out);  
            return null;  
        }  
        return macAddress;  
    }

}