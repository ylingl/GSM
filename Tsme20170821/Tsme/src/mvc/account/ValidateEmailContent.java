package mvc.account;

/**
 * @author yyw
 * @create date 20141225
 * 邮件html生成
 * @return
 */

public class ValidateEmailContent{
	
	private String html;
	
	private static String ip_port = "121.42.251.175:8080";//"localhost:8080";//;
	
	void setValidateCode(String validateCode){ 
		StringBuffer text = new StringBuffer();
		
		text.append("<html><head></head><body>");
		text.append("<div style=\"margin-top:100px\"><div style=\"width:600px; height:60px;margin:auto;font-family:'微软雅黑',Microsoft YaHei;background-color:#4fbfe8; font-size:24px\">");
		text.append("<span style=\"text-align:center;display:block;line-height:60px;color:#ffffff\">GSM-R干扰监测系统注册</span></div>");
		text.append("<div style=\"width:598px; height:400px;margin:0px auto; font-family:'微软雅黑',Microsoft YaHei; font-size:18px;border:1px solid #a1a4a6;\">");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>您好!</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>欢迎加如GSM-R干扰监测系统</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>为保证正常使用网站功能，请于24小时内激活账号</label></div>");
		text.append("<div style=\"width:170px; height:45px;margin:20px auto;padding-top:15px;padding-left:40px;background:#4fc1e9\"><a style=\"color:#ffffff;text-decoration:none;\" href=\"http://" + ip_port + "/Tsme/account/emailValidate?stamp=" + validateCode +"\">立即激活账号</a></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>如果以上按钮无法打开，请把下面的链接复制到浏览器地址栏中打开：</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><a href=\"http://" + ip_port + "/Tsme/account/emailValidate?stamp=" + validateCode +"\">http://" + ip_port + "/Tsme/account/emailValidate?stamp=" + validateCode + "</a></div>");
		text.append("</div></div></body></html>");
		
		html = text.toString();
	}
	
	public void setRetreiveCode(String retreiveCode){ 
		StringBuffer text = new StringBuffer();
		
		text.append("<html><head></head><body>");
		text.append("<div style=\"margin-top:100px\"><div style=\"width:600px; height:60px;margin:auto;font-family:'微软雅黑',Microsoft YaHei;background-color:#4fbfe8; font-size:24px\">");
		text.append("<span style=\"text-align:center;display:block;line-height:60px;color:#ffffff\">密码找回</span></div>");
		text.append("<div style=\"width:598px; height:400px;margin:0px auto; font-family:'微软雅黑',Microsoft YaHei; font-size:18px;border:1px solid #a1a4a6;\">");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>您好!</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>欢迎使用绥知</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>为保证正常使用网站功能，请于24小时内修改密码</label></div>");
		text.append("<div style=\"width:170px; height:45px;margin:20px auto;padding-top:15px;padding-left:40px;background:#4fc1e9\"><a style=\"text-decoration:none;color:#ffffff\" href=\"http://" + ip_port + "/Tsme/security/retrieve/accountRetreive?stamp=" + retreiveCode +"\">立即修改密码</a></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>如果以上按钮无法打开，请把下面的链接复制到浏览器地址栏中打开：</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><a href=\"http://" + ip_port + "/Tsme/security/retrieve/accountRetreive?stamp=" + retreiveCode +"\">http://" + ip_port + "/Tsme/security/retrieve/accountRetreive?stamp=" + retreiveCode + "</a></div>");
		text.append("</div></div></body></html>");
		
		html = text.toString();
	}
	
	public String getHtml(){
		return this.html;
	}
}
