package tsme.table.deviceFB.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.deviceFB.bean.DEVICEFB;

@Repository("deviceFBDAO")
public class DeviceFBDAOImpl extends TsmeMainDAOPracticeImpl<DEVICEFB> implements DeviceFBDAO{

}
