package tsme.table.controlProperty.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.controlProperty.bean.CONTROLPROPERTY;

@Repository("controlPropertyDAO")
public class ControlPropertyDAOImpl extends TsmeMainDAOPracticeImpl<CONTROLPROPERTY> implements ControlPropertyDAO{

}
