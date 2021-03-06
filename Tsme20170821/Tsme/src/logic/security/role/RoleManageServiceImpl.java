package logic.security.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mvc.security.role.RecRole;
import tsme.table.role.DAO.RoleDAO;
import tsme.table.role.bean.ROLE;

@Service("roleManageService")
public class RoleManageServiceImpl implements RoleManageService {
	@Autowired
	@Qualifier("roleDAO")
	private RoleDAO roleDAO;
	public boolean createRole(RecRole recRole){
		ROLE role = new ROLE();
		long create_time = System.currentTimeMillis();
		role.setActive(true);
		role.setCreate_time(create_time);
		role.setName(recRole.getRole_name());
		role.setCode(recRole.getRole_code());
		
		roleDAO.save(role);
		return true;
	}

	public List<ROLE> findAllRole(){
		List<ROLE> roleList = roleDAO.findAll(ROLE.class, "ASC");
		return roleList;
	}
	
	public ROLE findRoleById(String roleId){
		ROLE role = roleDAO.findBothById(roleId);
		return role;
	}
	
	public boolean updateRole(RecRole recRole, String roleId){
		ROLE role = roleDAO.findBothById(roleId);
		role.setName(recRole.getRole_name());
		role.setCode(recRole.getRole_code());
		roleDAO.update(role);
		
		return true;
	}
	
	public boolean deleteRoleById(String roleId){
		roleDAO.deleteById(roleId);
		return true;
	}
}
