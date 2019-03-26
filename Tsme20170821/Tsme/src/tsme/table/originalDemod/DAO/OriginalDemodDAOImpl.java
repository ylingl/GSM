package tsme.table.originalDemod.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.originalDemod.bean.ORIGINALDEMOD;

@Repository("originalDemodDAO")
public class OriginalDemodDAOImpl extends TsmeMainDAOPracticeImpl<ORIGINALDEMOD> implements OriginalDemodDAO{

}
