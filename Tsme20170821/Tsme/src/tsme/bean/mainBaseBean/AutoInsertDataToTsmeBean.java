package tsme.bean.mainBaseBean;

import java.util.UUID;

public abstract class AutoInsertDataToTsmeBean implements TsmeMainBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6570101322500238575L;

	/*
	 * ���ù�������ʼ��ʵ���࣬��Ϊÿ����������Զ�����UUID
	 * */
	public AutoInsertDataToTsmeBean(){
		setId(UUID.randomUUID().toString());
	}
}
