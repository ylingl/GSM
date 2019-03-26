package mvc.security.retrieve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import logic.account.AccountService;
import logic.security.login.LoginService;
import mvc.account.ValidateEmailContent;
import utils.email.MailSender;
import utils.email.ValidateMailInfo;

/**
 * @author yyw
 * @create date 20141225
 * �����һغ�̨����
 * @return
 */

@Controller
@RequestMapping("/retrieve")
public class RetrieveController {

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	
	@RequestMapping("/userRetrieve")
	public ModelAndView retrievePwd() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("retrieve/userRetrieve");
		
		return mav;
	}
		
	@RequestMapping("/retrieveEmailSend")
	public ModelAndView retrieveAccount(String username, String stamp) {
		ModelAndView mav = new ModelAndView();
		
		ValidateMailInfo validateMail = new ValidateMailInfo();
		
		validateMail.setMailServerHost("smtp.nemmobile.com");//"smtp.163.com"
		validateMail.setMailServerPort("25");//"25"
		validateMail.setValidate(true);    
		validateMail.setUserName("hd.ma@nemmobile.com");    
		validateMail.setPassword("mhdmsdkj0125/");
		validateMail.setFromAddress("hd.ma@nemmobile.com");    
		validateMail.setToAddress(username);    
		validateMail.setSubject("GSM-R���ż��ϵͳ�һ��ʻ�����");
		
		ValidateEmailContent validateContent = new ValidateEmailContent();
		if (stamp == null){
			stamp = accountService.saveRetrieveInfoByEmail(username);
		}
			
		validateContent.setRetreiveCode(stamp);
		validateMail.setContent(validateContent.getHtml());    
		
		//�������Ҫ�������ʼ�   
		MailSender sms = new MailSender();  
		sms.sendHtmlMail(validateMail);//����html��ʽ 		
		
		String domain = username.substring(username.indexOf('@') + 1);
		
		mav.addObject("username", username);
		mav.addObject("stamp", stamp);
		
		String url = "http://mail." + domain;
		
		mav.addObject("domain", url);
		mav.setViewName("retrieve/retrieveEmailSend");
		
		return mav;
	}
	
	//�˻��һ�
	@RequestMapping("/accountRetreive")
	public ModelAndView emailValidate(@RequestParam("stamp") String stamp){
		ModelAndView mav = new ModelAndView();
		
		String username = accountService.validateRetrieveInfo(stamp);
		if (username != null)
		{
			mav.addObject("msg", "true");
		}else{
			mav.addObject("msg", "false");
		}
		
		mav.addObject("username", username);
		mav.addObject("stamp", stamp);
		mav.setViewName("retrieve/accountRetreive");
		
		return mav;
	}
		
	//��֤�ɹ�
	@RequestMapping("/retreiveSucceed")
	public ModelAndView retreiveSucceed(String username, String stamp){	
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("username", username);
		mav.addObject("stamp", stamp);
		mav.setViewName("retrieve/retreiveSucceed");
		
		return mav;
	}
	
	//��֤ʧ��
	@RequestMapping("/retrieveFailed")
	public ModelAndView retreiveFailed(){	
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("retrieve/retrieveFailed");
		
		return mav;
	}
	
	//��ת����ҳ
	@RequestMapping("/retrieveAndLogin")
	public ModelAndView retrieveAndLogin(String username, String password, String sec_password, String stamp){	
		if (!password.equals(sec_password))
			return null;			
		
		String accountID = accountService.findIdByUsername(username);
		
		if (accountID == null)
			return null;
		
		accountService.ResetUserPassword(accountID, password);
		accountService.retrieveCompleted(stamp);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("username", username);
		mav.addObject("password", password);
		mav.setViewName("login/quickLogin");
		
		return mav;
	}
	
}
