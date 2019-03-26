package utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse ���ص�ͨ�ô�����
 * @author kitty
 *
 */
public class ResponseTools {
	/**
	 * �첽���󷵻��ַ�����ʱ��д�� HttpServletResponse ��ͨ�÷�����
	 * @param response
	 * @param str
	 */
	public static void writeResponse(HttpServletResponse response, String str) {
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
