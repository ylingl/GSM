package utils;

import java.util.Random;


/**
* @Description: ��������ַ�����������������ʼ�ע����֤���
* @author user1
* @date 2015��9��15��
*
*/
public class RandomStampTools {
	
	public String getRandomString(int length) { //length��ʾ�����ַ����ĳ���  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }  
}
