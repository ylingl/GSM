package tsme.table.bsLocation.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.bsLocation.bean.BSLOCATION;

@Repository("bsLocationDAO")
public class BsLocationDAOImpl extends TsmeMainDAOPracticeImpl<BSLOCATION> implements BsLocationDAO{

	@SuppressWarnings("unchecked")
	public List<BSLOCATION> getBsLocation() {
	
		String sql = "SELECT baseStation_id, LNG, LAT FROM BSLOCATION";
		return (List<BSLOCATION>) this.findByQuery(sql, BSLOCATION.class);
	}
}
