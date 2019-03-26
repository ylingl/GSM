package tsme.table.warningTemplate.DAO;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;
import tsme.table.warningTemplate.bean.WARNINGTEMPLATE;

public interface WarningTemplateDAO extends TsmeMainDAOPractice<WARNINGTEMPLATE>{
	
	public WARNINGTEMPLATE findWarningTemplateWithFrequencyBandListByIdAndSortByStartFrequency(String warningTemplateId);

}
