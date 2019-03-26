package logic.security.rescPath;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mvc.security.rescPath.RecRescPath;
import tsme.table.controlProperty.DAO.ControlPropertyDAO;
import tsme.table.controlProperty.bean.CONTROLPROPERTY;
import tsme.table.rescPath.DAO.RescPathDAO;
import tsme.table.rescPath.bean.RESCPATH;

@Service("rescPathManageService")
public class RescPathManageServiceImpl implements RescPathManageService {
	@Autowired
	@Qualifier("rescPathDAO")
	private RescPathDAO rescPathDAO;
	
	@Autowired
	@Qualifier("controlPropertyDAO")
	private ControlPropertyDAO controlPropertyDAO;
	
	public boolean createRescPath(RecRescPath recRescPath){
		RESCPATH rescPath = new RESCPATH();
		long create_time = System.currentTimeMillis();
		rescPath.setActive(true);
		rescPath.setCreate_time(create_time);
		rescPath.setUri(recRescPath.getUri());
		rescPath.setFunction(recRescPath.getFunction());
		
		rescPathDAO.save(rescPath);
		return true;
	}

	public List<RESCPATH> cascadedFindAllRescPath(boolean active, String order){
		List<RESCPATH> rescPath = rescPathDAO.cascadedQueryAll(active, order);
		//List<RESCPATH> rescPath = rescPathDAO.findAll(RESCPATH.class, "ASC");
		return rescPath;
	}
	
	public RESCPATH findRescPathById(String rescPathId){
		RESCPATH rescPath = rescPathDAO.findBothById(rescPathId);
		return rescPath;
	}
	
	public boolean updateRescPath(RecRescPath recRescPath, String rescPathId){
		RESCPATH rescPath  = rescPathDAO.findBothById(rescPathId);
		rescPath.setUri(recRescPath.getUri());
		rescPath.setFunction(recRescPath.getFunction());
		rescPathDAO.update(rescPath);
		
		return true;
	}
	
	public boolean cascadedDeleteRescPathById(String rescPathId){
		rescPathDAO.cascadedDeleteById(rescPathId);
		return true;
	}
	
	public boolean saveControlProperty(String rescPathId, String role){
		CONTROLPROPERTY controlProperty = new CONTROLPROPERTY();
		long create_time = System.currentTimeMillis();
		controlProperty.setRescPath_id(rescPathId);
		controlProperty.setActive(true);
		controlProperty.setCreate_time(create_time);
		controlProperty.setRole_code(role);
		controlPropertyDAO.save(controlProperty);
		return true;
	}
	
	public boolean updateControlProperty(String controlPropertyId, String role){
		CONTROLPROPERTY controlProperty = controlPropertyDAO.findBothById(controlPropertyId);
		controlProperty.setRole_code(role);
		controlPropertyDAO.update(controlProperty);
		return true;
	}
	
	public boolean deleteControlProperty(String controlPropertyId){
		controlPropertyDAO.deleteById(controlPropertyId);
		return true;
	}
	
	public CONTROLPROPERTY findControlPropertyById(String controlPropertyId){
		CONTROLPROPERTY controlProperty = controlPropertyDAO.findBothById(controlPropertyId);
		return controlProperty;
	}
}
