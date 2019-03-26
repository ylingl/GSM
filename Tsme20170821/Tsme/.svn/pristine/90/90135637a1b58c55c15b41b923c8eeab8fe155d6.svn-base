package tsme.table.baseStation.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.baseStation.bean.BASESTATION;

@Repository("baseStationDAO")
public class BaseStationDAOImpl extends TsmeMainDAOPracticeImpl<BASESTATION> implements BaseStationDAO{
	
	@SuppressWarnings("unchecked")
	public BASESTATION findBSByPosition(float lng, float lat) {
		
		String sql = "select id, introduction from BASESTATION where id in (select baseStation_id from BSLOCATION where LNG = " + lng + " and LAT = " + lat + ")";
		
		List<BASESTATION> stations = (List<BASESTATION>) this.findByQuery(sql, BASESTATION.class);
		
		return stations.size() > 0 ? stations.get(0) : null;
		
	}

}