package mvc.account;

/**
 * @author yyw
 * @create date 20141225
 * �ʼ�html����
 * @return
 */

public class ValidateEmailContent{
	
	private String html;
	
	private static String ip_port = "121.42.251.175:8080";//"localhost:8080";//;
	
	void setValidateCode(String validateCode){ 
		StringBuffer text = new StringBuffer();
		
		text.append("<html><head></head><body>");
		text.append("<div style=\"margin-top:100px\"><div style=\"width:600px; height:60px;margin:auto;font-family:'΢���ź�',Microsoft YaHei;background-color:#4fbfe8; font-size:24px\">");
		text.append("<span style=\"text-align:center;display:block;line-height:60px;color:#ffffff\">GSM-R���ż��ϵͳע��</span></div>");
		text.append("<div style=\"width:598px; height:400px;margin:0px auto; font-family:'΢���ź�',Microsoft YaHei; font-size:18px;border:1px solid #a1a4a6;\">");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>����!</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>��ӭ����GSM-R���ż��ϵͳ</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>Ϊ��֤����ʹ����վ���ܣ�����24Сʱ�ڼ����˺�</label></div>");
		text.append("<div style=\"width:170px; height:45px;margin:20px auto;padding-top:15px;padding-left:40px;background:#4fc1e9\"><a style=\"color:#ffffff;text-decoration:none;\" href=\"http://" + ip_port + "/Tsme/account/emailValidate?stamp=" + validateCode +"\">���������˺�</a></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>������ϰ�ť�޷��򿪣������������Ӹ��Ƶ��������ַ���д򿪣�</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><a href=\"http://" + ip_port + "/Tsme/account/emailValidate?stamp=" + validateCode +"\">http://" + ip_port + "/Tsme/account/emailValidate?stamp=" + validateCode + "</a></div>");
		text.append("</div></div></body></html>");
		
		html = text.toString();
	}
	
	public void setRetreiveCode(String retreiveCode){ 
		StringBuffer text = new StringBuffer();
		
		text.append("<html><head></head><body>");
		text.append("<div style=\"margin-top:100px\"><div style=\"width:600px; height:60px;margin:auto;font-family:'΢���ź�',Microsoft YaHei;background-color:#4fbfe8; font-size:24px\">");
		text.append("<span style=\"text-align:center;display:block;line-height:60px;color:#ffffff\">�����һ�</span></div>");
		text.append("<div style=\"width:598px; height:400px;margin:0px auto; font-family:'΢���ź�',Microsoft YaHei; font-size:18px;border:1px solid #a1a4a6;\">");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>����!</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>��ӭʹ����֪</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><label>Ϊ��֤����ʹ����վ���ܣ�����24Сʱ���޸�����</label></div>");
		text.append("<div style=\"width:170px; height:45px;margin:20px auto;padding-top:15px;padding-left:40px;background:#4fc1e9\"><a style=\"text-decoration:none;color:#ffffff\" href=\"http://" + ip_port + "/Tsme/security/retrieve/accountRetreive?stamp=" + retreiveCode +"\">�����޸�����</a></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:10px;padding-left:20px\"><label>������ϰ�ť�޷��򿪣������������Ӹ��Ƶ��������ַ���д򿪣�</label></div>");
		text.append("<div style=\"width:560px; height:25px;padding-top:15px;padding-left:20px\"><a href=\"http://" + ip_port + "/Tsme/security/retrieve/accountRetreive?stamp=" + retreiveCode +"\">http://" + ip_port + "/Tsme/security/retrieve/accountRetreive?stamp=" + retreiveCode + "</a></div>");
		text.append("</div></div></body></html>");
		
		html = text.toString();
	}
	
	public String getHtml(){
		return this.html;
	}
}
