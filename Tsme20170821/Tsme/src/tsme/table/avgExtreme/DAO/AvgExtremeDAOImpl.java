package tsme.table.avgExtreme.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.avgExtreme.bean.AVGEXTREME;

@Repository("avgExtremeDAO")
public class AvgExtremeDAOImpl extends TsmeMainDAOPracticeImpl<AVGEXTREME> implements AvgExtremeDAO{

}
