package tsme.table.originalAlarm.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.originalAlarm.bean.ORIGINALALARM;

@Repository("originalAlarmDAO")
public class OriginalAlarmDAOImpl extends TsmeMainDAOPracticeImpl<ORIGINALALARM> implements OriginalAlarmDAO {

}
