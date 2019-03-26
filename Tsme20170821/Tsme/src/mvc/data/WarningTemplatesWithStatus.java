package mvc.data;

import java.util.ArrayList;
import java.util.List;

import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;

public class WarningTemplatesWithStatus {
	
	private boolean employ = false;
	
	private boolean myself = false;
	
	private boolean demod = false;
	
	private List<WARNINGTEMPLATE> warningTemplateList = new ArrayList<WARNINGTEMPLATE>();
	
	private WARNINGTEMPLATE currentwarningTemplate;

	public boolean isEmploy() {
		return employ;
	}

	public void setEmploy(boolean employ) {
		this.employ = employ;
	}

	public boolean isMyself() {
		return myself;
	}

	public void setMyself(boolean myself) {
		this.myself = myself;
	}

	public List<WARNINGTEMPLATE> getWarningTemplateList() {
		return warningTemplateList;
	}

	public void setWarningTemplateList(List<WARNINGTEMPLATE> warningTemplateList) {
		this.warningTemplateList = warningTemplateList;
	}

	public WARNINGTEMPLATE getCurrentwarningTemplate() {
		return currentwarningTemplate;
	}

	public void setCurrentwarningTemplate(WARNINGTEMPLATE currentwarningTemplate) {
		this.currentwarningTemplate = currentwarningTemplate;
	}

	public boolean isDemod() {
		return demod;
	}

	public void setDemod(boolean demod) {
		this.demod = demod;
	}

}
