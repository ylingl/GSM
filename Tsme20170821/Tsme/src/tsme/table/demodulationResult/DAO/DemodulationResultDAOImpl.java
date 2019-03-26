package tsme.table.demodulationResult.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.demodulationResult.bean.DEMODULATIONRESULT;

@Repository("demodulationResultDAO")
public class DemodulationResultDAOImpl extends TsmeMainDAOPracticeImpl<DEMODULATIONRESULT> implements DemodulationResultDAO {

}
