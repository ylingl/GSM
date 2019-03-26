package tsme.table.warningLine.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.warningLine.bean.WARNINGLINE;

@Repository("warningLineDAO")
public class WarningLineDAOImpl extends TsmeMainDAOPracticeImpl<WARNINGLINE> implements WarningLineDAO {

}
