package tsme.table.trainerAudit.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.trainerAudit.bean.TRAINERAUDIT;

@Repository("trainerAuditDAO")
public class TrainerAuditDAOImpl extends TsmeMainDAOPracticeImpl<TRAINERAUDIT> implements TrainerAuditDAO {

}
