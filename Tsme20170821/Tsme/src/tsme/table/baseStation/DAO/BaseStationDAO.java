package tsme.table.baseStation.DAO;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPractice;
import tsme.table.baseStation.bean.BASESTATION;

public interface BaseStationDAO extends TsmeMainDAOPractice<BASESTATION>{
	
	public BASESTATION findBSByPosition(float lng, float lat);

}
