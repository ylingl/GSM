package mvc.security.rescPath;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.security.rescPath.RescPathManageService;
import logic.security.role.RoleManageService;
import tsme.table.controlProperty.bean.CONTROLPROPERTY;
import tsme.table.rescPath.bean.RESCPATH;
import tsme.table.role.bean.ROLE;


@Controller
@RequestMapping("/rescPath")
public class RescPathController {
	@Autowired
	@Qualifier("rescPathManageService")
	private RescPathManageService rescPathManageService;
	
	@Autowired
	@Qualifier("roleManageService")
	private RoleManageService roleManageService;
	
	//添加角色信息
	@RequestMapping("/showRescPath")
	public ModelAndView showRescPath(HttpSession httpSession){	
		ModelAndView mav = new ModelAndView();
		
		List<RESCPATH> rescPathList = rescPathManageService.cascadedFindAllRescPath(true, "ASC");
		mav.addObject("rescPathList", rescPathList);
		
		List<ROLE> roleList = roleManageService.findAllRole();
		mav.addObject("roleList", roleList);		
		//锚点
		if(httpSession.getAttribute("anchor") != null ){
			String anchor= (String) httpSession.getAttribute("anchor");
			//删除锚点
			httpSession.removeAttribute("anchor");
			mav.addObject("anchor",anchor);
		}
				
		mav.setViewName("rescPath/rescPathElements");
		return mav;
	}
	
	//获取角色信息
	@RequestMapping("/getRescPath/{anchor}")
	public ModelAndView getRescPath(RecRescPath recRescPath, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		
		rescPathManageService.createRescPath(recRescPath);
		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("redirect:/security/rescPath/showRescPath");
		return mav;
	}
	
	//修改角色信息
	@RequestMapping("/modifyRescPath/{currentRescPathId}/{anchor}")
	public ModelAndView modifyRescPath(@PathVariable("currentRescPathId") String currentRescPathId, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		
		List<RESCPATH> rescPathList = rescPathManageService.cascadedFindAllRescPath(true, "ASC");
		mav.addObject("rescPathList", rescPathList);
		
		RESCPATH rescPath = rescPathManageService.findRescPathById(currentRescPathId);
		mav.addObject("rescPath", rescPath);
		
		String token = "ModifyRescPath";
		mav.addObject("token", token);
		
		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("rescPath/rescPathElements");
		
		return mav;
	}
	
	//更新角色信息
	@RequestMapping("/updateRescPath/{anchor}")
	public ModelAndView updateRescPath(RecRescPath recRescPath, String rescPathId, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		
		rescPathManageService.updateRescPath(recRescPath, rescPathId);
		
		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("redirect:/security/rescPath/showRescPath");
		return mav;
	}
	
	//删除角色信息
	@RequestMapping("/deleteRescPath/{currentRescPathId}/{anchor}")
	public ModelAndView deleteRescPath(@PathVariable("currentRescPathId") String currentRescPathId, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		
		rescPathManageService.cascadedDeleteRescPathById(currentRescPathId);

		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("redirect:/security/rescPath/showRescPath");
		return mav;
	}
	
	//添加控制属性
	@RequestMapping("/addControlProperty/{currentRescPathId}/{anchor}")
	public ModelAndView addControlProperty(@PathVariable("currentRescPathId") String currentRescPathId, String role, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		
		rescPathManageService.saveControlProperty(currentRescPathId, role);

		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("redirect:/security/rescPath/showRescPath");
		return mav;
	}
	
	//删除控制属性
	@RequestMapping("/deleteControlProperty/{controlPropertyId}/{anchor}")
	public ModelAndView deleteControlProperty(@PathVariable("controlPropertyId") String controlPropertyId, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		
		rescPathManageService.deleteControlProperty(controlPropertyId);

		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("redirect:/security/rescPath/showRescPath");
		return mav;
	}
	
	//修改控制属性
	@RequestMapping("/modifyControlProperty/{controlPropertyId}/{anchor}")
	public ModelAndView modifyControlProperty(@PathVariable("controlPropertyId") String controlPropertyId, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		List<RESCPATH> rescPathList = rescPathManageService.cascadedFindAllRescPath(true, "ASC");
		mav.addObject("rescPathList", rescPathList);
		
		List<ROLE> roleList = roleManageService.findAllRole();
		mav.addObject("roleList", roleList);
		
		CONTROLPROPERTY controlProperty = rescPathManageService.findControlPropertyById(controlPropertyId);

		mav.addObject("controlPropertyRole", controlProperty.getRole_code());
		mav.addObject("token", "ModifyControlProperty");
		mav.addObject("controlPropertyId", controlPropertyId);

		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("rescPath/rescPathElements");
		return mav;
	}
	
	//修改控制属性
	@RequestMapping("/updateControlProperty/{controlPropertyId}/{anchor}")
	public ModelAndView updateControlProperty(@PathVariable("controlPropertyId") String controlPropertyId, String role, HttpSession httpSession, @PathVariable("anchor") String anchor){	
		ModelAndView mav = new ModelAndView();
		
		rescPathManageService.updateControlProperty(controlPropertyId, role);

		httpSession.setAttribute("anchor", anchor);
		
		mav.setViewName("redirect:/security/rescPath/showRescPath");
		return mav;
	}
}
