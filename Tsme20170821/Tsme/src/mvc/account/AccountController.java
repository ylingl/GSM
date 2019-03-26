package mvc.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import logic.account.AccountService;
import logic.security.role.RoleManageService;
import net.sf.json.JSONObject;
import tsme.table.account.DAO.AccountDAO;
import tsme.table.account.bean.ACCOUNT;
import tsme.table.role.bean.ROLE;
import tsme.table.trainerAudit.bean.TRAINERAUDIT;
import utils.AccountTools;
import utils.ResponseTools;
import utils.ValidateCodeTools;
import utils.email.MailSender;
import utils.email.ValidateMailInfo;

@Controller
public class AccountController {
	
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	
	@Autowired
	@Qualifier("accountDAO")
	private AccountDAO accountDAO;
	
	@Autowired
	@Qualifier("roleManageService")
	private RoleManageService roleManageService;
	
	private AccountTools accountTools = new AccountTools();
	
	@RequestMapping("/userRegister")
	public ModelAndView userRegister(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("userRegister");
		return mav;
	}
	
	public String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
	
	//检查用户信息
	@RequestMapping("/checkInfo")
	public void checkInfo( String username, String validateCode, HttpServletResponse response, HttpServletRequest request) {	
		response.setCharacterEncoding("UTF-8");
		String msg = "right";
		
		ValidateCodeTools validateCodeTools = new ValidateCodeTools();
			
		if(validateCodeTools.check(validateCode, request)){
			if(accountService.isUserNameExisted(username)){
				msg = "usernameExisted";
			}
		} else {
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
	
	//跳转到邮箱验证页
	@RequestMapping("/sendValidate")
	public ModelAndView sendValidate(String email, String stamp){
		ValidateMailInfo validateMail = new ValidateMailInfo();
		
		//配置邮件服务器参数
		validateMail.setMailServerHost("smtp.nemmobile.com");//"smtp.163.com"
		validateMail.setMailServerPort("25");//"25"
		validateMail.setValidate(true);
		validateMail.setUserName("hd.ma@nemmobile.com");    
		validateMail.setPassword("mhdmsdkj0125/");//您的邮箱密码    
		validateMail.setFromAddress("hd.ma@nemmobile.com");    
		validateMail.setToAddress(email);
		validateMail.setSubject("GSM-R干扰监测系统注册认证");
		
		//暂存账户信息并生成验证随机数
		if (stamp == null){
			stamp = accountService.saveRegisterInfoByEmail(email);
		}
			
		//生成验证链接
		ValidateEmailContent validateContent = new ValidateEmailContent();
		validateContent.setValidateCode(stamp);
		
		validateMail.setContent(validateContent.getHtml());     
		
		MailSender sms = new MailSender();  
		sms.sendHtmlMail(validateMail);
		
		ModelAndView mav = new ModelAndView();
		
		String domain = email.substring(email.indexOf('@') + 1);
		String url = "http://mail." + domain;
		
		mav.addObject("email", email);
		mav.addObject("domain", url);
		mav.addObject("stamp", stamp);
		mav.setViewName("sendValidate");
		
		return mav;
	}
	
	//邮箱认证填写密码
	@RequestMapping("/emailValidate")
	public ModelAndView emailValidate(@RequestParam("stamp") String stamp){
		ModelAndView mav = new ModelAndView();
		
		String result = accountService.validateEmailRegisterByStamp(stamp);
		
		if (result != null) {
			mav.addObject("msg", "true");
		} else {
			mav.addObject("msg", "false");
		}
		
		mav.addObject("email", result);
		mav.addObject("validate", stamp);
		mav.setViewName("emailValidate");
		
		return mav;
	}
	
	//认证成功
	@RequestMapping("/registerSucceed")
	public ModelAndView registerSucceed(String email, String validate){	
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("email", email);
		mav.addObject("validate", validate);
		mav.setViewName("registerSucceed");
		
		return mav;
	}
		
	//认证失败
	@RequestMapping("/registerFailed")
	public ModelAndView registerFailed(){	
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("registerFailed");
		return mav;
	}
	
	//账户首页
	@RequestMapping("/accountHomepage")
	public ModelAndView homepage() {
		ModelAndView mav = new ModelAndView();
		
		SetModelAndView(mav);
		
		return mav;
	}
	
	private void SetModelAndView(ModelAndView mav) {
		// 设置角色
		SetRole(mav);
		
		mav.setViewName("accountHomepage");			
	}
	
	private void SetRole(ModelAndView mav) {
		int roleOrder = 0;
		
		List<String> roleList = accountTools.getCurrentAccountRoles();
		for(String role : roleList){
			if(role.equals("ROLE_SUPERADMIN")){
				if(roleOrder < 4){
					roleOrder = 4;
				}
			} else if(role.equals("ROLE_ADMIN")){
				if(roleOrder < 3){
					roleOrder = 3;
				}
			} else if(role.equals("ROLE_TRAINER")){
				if(roleOrder < 2){
					roleOrder = 2;
				}
			} else if(role.equals("ROLE_TESTER")){
				if(roleOrder < 1){
					roleOrder = 1;
				}
			}
		}
		
		mav.addObject("roleOrder", roleOrder);
	}
	
	@RequestMapping("/canThisAccountApplyForTrainer")
	@ResponseBody
	public String canThisAccountApplyForTrainer() {
		AccountTools accountTools = new AccountTools();
		
		if(accountTools.doesCurrentAccountHasTrainerRole()){
			return "hide";
		} else if(accountService.canThisAccountApplyForTrainer(accountTools.getCurrentAccountId())){
			return "show";
		} else {
			return "disable";
		}
	}

	@RequestMapping("/applyForTrainer")
	@ResponseBody
	public boolean applyForTrainer(String realName, String phoneNum) {
		AccountTools accountTools = new AccountTools();
		
		if(!accountTools.doesCurrentAccountHasTrainerRole() && 
			accountService.canThisAccountApplyForTrainer(accountTools.getCurrentAccountId())){
			
			accountService.applyForTrainer(accountTools.getCurrentAccountId(), realName, phoneNum);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @author zp
	 * @create date 20140625 
	 * 查找未审核卖家用户
	 * @return
	 */
	@RequestMapping("/findUnauditAccount")
	public ModelAndView findUnauditAccount() {
		ModelAndView mav = new ModelAndView();
		
		List<TRAINERAUDIT> trainerAuditList = accountService.findAccountByState("waiting");
		
		mav.addObject("trainerAuditList", trainerAuditList);
		mav.setViewName("unauditAccount");
		return mav;	
	}
	
	@RequestMapping("/findAccountDetail/{accountId}")
	public ModelAndView findAccountDetail(@PathVariable("accountId") String accountId) {
		ModelAndView mav = new ModelAndView();
		
		ACCOUNT account = accountDAO.findBothById(accountId);
		
		mav.addObject("account", account);
		mav.setViewName("accountDetail");
		return mav;
	}
	
	@RequestMapping("/updateAuditAccount")
	@ResponseBody
	public boolean updateAuditAccount(String id, String state, String opinions) {
		AccountTools accountTools = new AccountTools();
		return accountService.audit(id, state, opinions, accountTools.getCurrentAccountId());
	}
	
	@RequestMapping("/findAuditAccount")
	public ModelAndView findAuditAccount() {
		ModelAndView mav = new ModelAndView();
		
		List<TRAINERAUDIT> trainerAuditList = accountService.findAccountByState("pass' OR state='deny");
		
		mav.addObject("trainerAuditList", trainerAuditList);
		mav.setViewName("auditAccount");
		return mav;	
	}
	
	@RequestMapping("/findAllAccount")
	public ModelAndView findAllAccount() {
		ModelAndView mav = new ModelAndView();
		
		List<AccountInfo> accountInfoList = accountService.getAllAccountInfo();
		
		mav.addObject("accountInfoList", accountInfoList);
		mav.setViewName("allAccount");
		return mav;	
	}
	
	@RequestMapping("/setAccountActiveAttribute")
	@ResponseBody
	public boolean setAccountActiveAttribute(String accountId, boolean active) {
		return accountService.setAccountActiveAttribute(accountId, active);
	}
	
	@RequestMapping("/getAccountRoleList")
	@ResponseBody
	public void getAccountRoleList(String accountId, HttpServletResponse response) {
		JSONObject jsObj = new JSONObject();
		
		List<ROLE> roleList = roleManageService.findAllRole();
		List<String> accountRoleList = accountService.getAccountRoleList(accountId);
		
		jsObj.put("roleList", roleList);
		jsObj.put("accountRoleList", accountRoleList);
		
		ResponseTools.writeResponse(response, jsObj.toString());
		return;
	}
	
	@RequestMapping("/modifyAccountProperty")
	@ResponseBody
	public boolean modifyRoles(String currentAccountId, String roleList) {
		return accountService.modifyAccountProperty(currentAccountId, roleList);
	}
}
