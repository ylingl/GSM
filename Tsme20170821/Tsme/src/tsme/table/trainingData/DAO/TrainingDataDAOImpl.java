package tsme.table.trainingData.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.trainingData.bean.TRAININGDATA;

@Repository("trainingDataDAO")
public class TrainingDataDAOImpl extends TsmeMainDAOPracticeImpl<TRAININGDATA> implements TrainingDataDAO {

}
