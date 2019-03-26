package tsme.table.demodulationPoint.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.demodulationPoint.bean.DEMODULATIONPOINT;

@Repository("demodulationPointDAO")
public class DemodulationPointDAOImpl extends TsmeMainDAOPracticeImpl<DEMODULATIONPOINT> implements DemodulationPointDAO{

}
