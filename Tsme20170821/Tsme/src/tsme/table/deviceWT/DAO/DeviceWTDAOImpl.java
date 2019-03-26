package tsme.table.deviceWT.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.deviceWT.bean.DEVICEWT;

@Repository("deviceWTDAO")
public class DeviceWTDAOImpl extends TsmeMainDAOPracticeImpl<DEVICEWT> implements DeviceWTDAO{

}
