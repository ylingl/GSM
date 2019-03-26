package utils;

import java.security.MessageDigest;

import org.apache.commons.lang.math.RandomUtils;

public class MD5Tools {
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md5Inst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md5Inst.update(btInput);
            // 获得密文
            byte[] md = md5Inst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static String generateToken(String username) {
		
		int nSeed = RandomUtils.nextInt(Integer.MAX_VALUE);
		
		String seed = MD5Tools.MD5(String.valueOf(nSeed));
		
		String token = MD5Tools.MD5(System.currentTimeMillis() + seed);
		
		token = MD5Tools.MD5(username + token);
		
		return token;
		
	}
	
    public static void main(String[] args) {
        
/*    	String md5 = MD5Tools.MD5("20132013");
        System.out.print(md5);*/
    	
    	String md5 = MD5Tools.MD5("2{1}");
    	System.out.print(md5);
    	
    }
}
