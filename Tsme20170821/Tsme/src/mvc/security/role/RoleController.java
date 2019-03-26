package mvc.security.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.security.role.RoleManageService;
import tsme.table.role.bean.ROLE;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	@Qualifier("roleManageService")
	private RoleManageService roleManageService;
	
	//添加角色信息
	@RequestMapping("/fillRole")
	public ModelAndView fillRole(){	
		ModelAndView mav = new ModelAndView();
		
		List<ROLE> roleList = roleManageService.findAllRole();
		mav.addObject("roleList", roleList);
		
		mav.setViewName("role/roleElements");
		return mav;
	}
	
	//获取角色信息
	@RequestMapping("/getRole")
	public ModelAndView getRole(RecRole recRole){	
		ModelAndView mav = new ModelAndView();
		
		roleManageService.createRole(recRole);
		
		mav.setViewName("redirect:/security/role/fillRole");
		return mav;
	}
	
	//修改角色信息
	@RequestMapping("/modifyRole/{currentRoleId}")
	public ModelAndView modifyRole(@PathVariable("currentRoleId") String currentRoleId){	
		ModelAndView mav = new ModelAndView();
		
		List<ROLE> roleList = roleManageService.findAllRole();
		mav.addObject("roleList", roleList);
		
		ROLE role = roleManageService.findRoleById(currentRoleId);
		mav.addObject("role", role);
		
		String token = "ModifyRole";
		mav.addObject("token", token);
		
		mav.setViewName("role/roleElements");
		
		return mav;
	}
	
	//更新角色信息
	@RequestMapping("/updateRole")
	public ModelAndView updateRole(RecRole recRole, String roleId){	
		ModelAndView mav = new ModelAndView();
		
		roleManageService.updateRole(recRole, roleId);
		
		mav.setViewName("redirect:/security/role/fillRole");
		return mav;
	}
	
	//删除角色信息
	@RequestMapping("/deleteRole/{currentRoleId}")
	public ModelAndView deleteRole(@PathVariable("currentRoleId") String currentRoleId){	
		ModelAndView mav = new ModelAndView();
		
		roleManageService.deleteRoleById(currentRoleId);
		
		mav.setViewName("redirect:/security/role/fillRole");
		return mav;
	}
}
