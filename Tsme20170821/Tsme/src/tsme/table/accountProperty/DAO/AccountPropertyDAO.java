package tsme.table.accountProperty.DAO;

import java.util.List;

import tsme.table.accountProperty.bean.ACCOUNTPROPERTY;
import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;

public interface AccountPropertyDAO extends TsmeMainDAOPractice<ACCOUNTPROPERTY>{
	
	public List<ACCOUNTPROPERTY> findByForeignKey(String foreignKey);

}
