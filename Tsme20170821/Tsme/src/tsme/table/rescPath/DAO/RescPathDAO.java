package tsme.table.rescPath.DAO;

import java.util.List;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;
import tsme.table.rescPath.bean.RESCPATH;

public interface RescPathDAO extends TsmeMainDAOPractice<RESCPATH>{
	
	public List<RESCPATH> cascadedQueryAll(boolean active, String order);
}
