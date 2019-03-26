package tsme.table.frequencyBand.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.frequencyBand.bean.FREQUENCYBAND;

@Repository("frequencyBandDAO")
public class FrequencyBandDAOImpl extends TsmeMainDAOPracticeImpl<FREQUENCYBAND> implements FrequencyBandDAO {

}
