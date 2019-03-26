package mvc.device;

public class Response {

	public static final int resp_common_code_normal = 90;//����
	public static final int resp_common_code_format_error = 911;//���ĸ�ʽ����
	public static final int resp_common_code_status_error = 921;//������δ���ڽ���ת̬
	public static final int resp_common_code_vacant_error = 922;//������δ�������
	
	public static final int resp_monitor_code_normal = 10;//�������豸��δ����������
	public static final int resp_monitor_code_begin = 11;//��ʼ���
	public static final int resp_monitor_code_stop = 12;//ֹͣ���
	
	//public static final int resp_code_chg_report_interval = 13;
	//public static final int resp_code_chg_history_interval = 14;
	
	public static final int resp_demod_code_normal = 30;//�������豸��δ����������
	public static final int resp_demod_code_begin = 31;//��ʼ���
	public static final int resp_demod_code_stop = 32;//ֹͣ���
	
	private String id;
	private int monitor_code = 0;
	private int demod_code = 0;
	private int common_code = 0;
	private String msg;
	private ResponseMonitorParam responseMonitorParam;
	private ResponseDemodParam responseDemodParam;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ResponseMonitorParam getResponseMonitorParam() {
		return responseMonitorParam;
	}

	public void setResponseMonitorParam(ResponseMonitorParam responseMonitorParam) {
		this.responseMonitorParam = responseMonitorParam;
	}

	public ResponseDemodParam getResponseDemodParam() {
		return responseDemodParam;
	}

	public void setResponseDemodParam(ResponseDemodParam responseDemodParam) {
		this.responseDemodParam = responseDemodParam;
	}

	public int getMonitor_code() {
		return monitor_code;
	}

	public void setMonitor_code(int monitor_code) {
		this.monitor_code = monitor_code;
	}

	public int getDemod_code() {
		return demod_code;
	}

	public void setDemod_code(int demod_code) {
		this.demod_code = demod_code;
	}

	public int getCommon_code() {
		return common_code;
	}

	public void setCommon_code(int common_code) {
		this.common_code = common_code;
	}

}
