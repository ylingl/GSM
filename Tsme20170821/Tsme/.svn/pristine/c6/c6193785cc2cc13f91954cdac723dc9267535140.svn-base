package utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse 返回的通用处理方法
 * @author kitty
 *
 */
public class ResponseTools {
	/**
	 * 异步请求返回字符串的时候写入 HttpServletResponse 的通用方法。
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
