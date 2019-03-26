package tsme.table.deviceDP.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.deviceDP.bean.DEVICEDP;

@Repository("deviceDPDAO")
public class DeviceDPDAOImpl extends TsmeMainDAOPracticeImpl<DEVICEDP> implements DeviceDPDAO{

}
