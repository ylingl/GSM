package mvc.security.logout;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Liu Mingqi
 * @date 20150505
 * @function ��������������е��û�Ȩ����Ϣ
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

	/**
	 * @author Liu Mingqi
	 * @date 20150506
	 * @function �û��˳��˻�ʱ������������������û���Ȩ����Ϣ
	 */
	@RequestMapping("/clearAccountData")
	public ModelAndView clearAccountData(HttpServletResponse response){
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("redirect:/logout");
		return mav;
	}
}
