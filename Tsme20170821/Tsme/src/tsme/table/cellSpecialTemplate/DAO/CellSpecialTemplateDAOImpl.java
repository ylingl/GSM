package tsme.table.cellSpecialTemplate.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.cellSpecialTemplate.bean.CELLSPECIALTEMPLATE;

@Repository("cellSpecialTemplateDAO")
public class CellSpecialTemplateDAOImpl extends TsmeMainDAOPracticeImpl<CELLSPECIALTEMPLATE> implements CellSpecialTemplateDAO{

}
