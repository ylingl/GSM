package logic.security.rescPath;

import java.util.List;

import mvc.security.rescPath.RecRescPath;
import tsme.table.controlProperty.bean.CONTROLPROPERTY;
import tsme.table.rescPath.bean.RESCPATH;

public interface RescPathManageService {
	
	public boolean createRescPath(RecRescPath recRescPath);
	
	public List<RESCPATH> cascadedFindAllRescPath(boolean active, String order);
	
	public RESCPATH findRescPathById(String rescPathId);
	
	public boolean updateRescPath(RecRescPath recRescPath, String rescPathId);
	
	public boolean cascadedDeleteRescPathById(String rescPathId);
	
	public boolean saveControlProperty(String rescPathId, String role);
	
	public boolean updateControlProperty(String controlPropertyId, String role);
	
	public boolean deleteControlProperty(String controlPropertyId);
	
	public CONTROLPROPERTY findControlPropertyById(String controlPropertyId); 
	
}
