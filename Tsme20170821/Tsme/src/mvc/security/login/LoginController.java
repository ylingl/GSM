package mvc.security.login;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.account.AccountService;
import logic.security.login.LoginService;
import tsme.table.account.DAO.AccountDAO;
import utils.ValidateCodeTools;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	
	// ע�����¼�ۺ�ҳ
	@RequestMapping("/registerAndLogin")
	public ModelAndView registerAndLogin(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		String state= request.getParameter("state");
		
		if(state == null || state.equals("login")){
			mav.addObject("state", "login");
		}
		else if(state.equals("register")){
			mav.addObject("state", "register");
		}
		else if(state.equals("retrieve"))
			mav.addObject("state", "retrieve");
		
		mav.setViewName("registerAndLogin/integrate");
		
		return mav;
	}

	// �û���¼
	@RequestMapping("/userLogin")
	public ModelAndView userLogin() {
		ModelAndView mav = new ModelAndView();	
		mav.setViewName("login/userLogin");
		return mav;
	}
	
	//����û���Ϣ
	@RequestMapping("/checkInfo")
	public void checkInfo(String username, String validateCode, 
		HttpServletResponse response, HttpServletRequest request) {	
		response.setCharacterEncoding("UTF-8");
		
		String msg = "right";
		
		ValidateCodeTools validateCodeTools = new ValidateCodeTools();
		if(validateCodeTools.check(validateCode, request)){
			if(!accountDAO.isUserNameExisted(username)){//�û����ֲ�����������ֻ�
				msg = "accountNotExisted";
			}
		}
		else{
			msg = "validateCodeFalse";
		}
		
		PrintWriter writer;	
		try {
			writer = response.getWriter();
			writer.print(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//����û���¼��Ϣ
	@RequestMapping("/checkloginInfo")
	public void checkloginInfo( String username, String password, String validateCode, HttpServletResponse response, HttpServletRequest request) {
		response.setCharacterEncoding("UTF-8");

		String msg = "right";
		HttpSession session = request.getSession();
		ValidateCodeTools validateCodeTools = new ValidateCodeTools();
		if(!accountDAO.isUserNameExisted(username)){//�û����ֲ�����������ֻ�
			session.setAttribute("pwdErrorCount", "1");
			msg = "usernameNotExisted";
		}
		else if(!loginService.verifyUsernameAndPasswordForWeb(username,password)){
			session.setAttribute("pwdErrorCount", "1");
			msg = "passwordFalse";
		}
		else{
			Object pwdErrorCount = session.getAttribute("pwdErrorCount");
			if(pwdErrorCount != null){
				//�˲���֤��
				if(!validateCodeTools.check(validateCode, request)){
					msg = "validateCodeFalse";
				}
			}
		}
		
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//δ��¼������ʱ������¼�Ի���
	@RequestMapping("/loginDialog")
	public ModelAndView loginDialog() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("registerAndLogin/integrate");
		
		return mav;	
	}

	// ��֤��
	@RequestMapping("/checkNumberShow")
	public ModelAndView checkNumberShow() {
		ModelAndView mav = new ModelAndView();	
		mav.setViewName("login/checkNumber");
		return mav;
	}
	
	//ע�����ٵ�¼
	@RequestMapping("/registerComplete")
	public ModelAndView registerComplete(String username, String password, String sec_password, String validate){	
		ModelAndView mav = new ModelAndView();
		
		if (!password.equals(sec_password))
			return null;
		
		if(!accountService.register(validate, username, password))
			return null;
		
		mav.addObject("username", username);
		mav.addObject("password", password);
		mav.setViewName("login/quickLogin");
		
		return mav;
	}
}
