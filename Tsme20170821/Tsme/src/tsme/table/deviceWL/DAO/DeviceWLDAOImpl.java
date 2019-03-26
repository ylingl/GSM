package tsme.table.deviceWL.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.deviceWL.bean.DEVICEWL;

@Repository("deviceWLDAO")
public class DeviceWLDAOImpl extends TsmeMainDAOPracticeImpl<DEVICEWL> implements DeviceWLDAO{

}
