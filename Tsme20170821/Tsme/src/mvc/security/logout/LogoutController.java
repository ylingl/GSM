package mvc.security.logout;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Liu Mingqi
 * @date 20150505
 * @function 清除服务器缓存中的用户权限信息
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

	/**
	 * @author Liu Mingqi
	 * @date 20150506
	 * @function 用户退出账户时，清除服务器缓存中用户的权限信息
	 */
	@RequestMapping("/clearAccountData")
	public ModelAndView clearAccountData(HttpServletResponse response){
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("redirect:/logout");
		return mav;
	}
}
