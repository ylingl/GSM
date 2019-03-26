package tsme.table.accountProperty.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import tsme.table.accountProperty.bean.ACCOUNTPROPERTY;
import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;

@Repository("accountPropertyDAO")
public class AccountPropertyDAOImpl extends TsmeMainDAOPracticeImpl<ACCOUNTPROPERTY> implements AccountPropertyDAO {

	@SuppressWarnings("unchecked")
	public List<ACCOUNTPROPERTY> findByForeignKey(String foreignKey){
		String sql = "SELECT * FROM accountProperty WHERE account_id='" + foreignKey +"' AND active=1";
		
		return (List<ACCOUNTPROPERTY>) findByQuery(sql, ACCOUNTPROPERTY.class);
	}
}
