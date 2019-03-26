package tsme.table.archive.DAO;

import org.springframework.stereotype.Repository;

import tsme.DAO.mainDAOPractice.TsmeMainDAOPracticeImpl;
import tsme.table.archive.bean.ARCHIVE;

@Repository("archiveDAO")
public class ArchiveDAOImpl extends TsmeMainDAOPracticeImpl<ARCHIVE> implements ArchiveDAO{

}
