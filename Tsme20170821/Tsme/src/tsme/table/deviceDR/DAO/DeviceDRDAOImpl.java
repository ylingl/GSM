package tsme.table.deviceDR.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.deviceDR.bean.DEVICEDR;

@Repository("deviceDRDAO")
public class DeviceDRDAOImpl extends TsmeMainDAOPracticeImpl<DEVICEDR> implements DeviceDRDAO {

}
