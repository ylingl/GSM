package mvc.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.config.ConfigService;
import tsme.table.authorizedDevice.DAO.AuthorizedDeviceDAO;
import tsme.table.authorizedDevice.bean.AUTHORIZEDDEVICE;
import utils.AccountTools;
import utils.DataPoolTools;

@Controller
public class ConfigController {
	
	@Autowired
	@Qualifier("configService")
	private ConfigService configService;
	
	@Autowired
	@Qualifier("authorizedDeviceDAO")
	private AuthorizedDeviceDAO authorizedDeviceDAO;
	
	@RequestMapping("/findAllAuthorizedDevice")
	public ModelAndView findAllAuthorizedDevice() {
		ModelAndView mav = new ModelAndView();
		
		List<AUTHORIZEDDEVICE> authorizedDeviceList = configService.findAllAuthorizedDevice(true);
		
		mav.addObject("authorizedDeviceList", authorizedDeviceList);
		mav.setViewName("allAuthorizedDevice");
		
		return mav;	
	}
	
	@RequestMapping("/addAuthorizedDevice")
	public ModelAndView addAuthorizedDevice(String device_num, String expiry_date) throws ParseException {
		ModelAndView mav = new ModelAndView();
		
		AccountTools accountTools = new AccountTools();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		Date date = sdf.parse(expiry_date);
		
		AUTHORIZEDDEVICE authorizedDevice = new AUTHORIZEDDEVICE();
		authorizedDevice.setActive(true);
		authorizedDevice.setCreate_time(System.currentTimeMillis());
		authorizedDevice.setCreator_id(accountTools.getCurrentAccountId());
		authorizedDevice.setDevice_num(device_num);
		authorizedDevice.setExpiry_date(date);
		authorizedDevice.setValid_date(new Date());
		
		int flag = configService.saveAuthorizedDevice(authorizedDevice, accountTools.getCurrentAccountId());
		if(flag > 0){
			List<String> authorizedDeviceNumList = authorizedDeviceDAO.getAuthorizedDeviceNumList();
			DataPoolTools.authorizedDeviceNumSet.addAll(authorizedDeviceNumList);
		}
		
		mav.setViewName("redirect:/config/findAllAuthorizedDevice");
		
		return mav;
	}
	
	@RequestMapping("/deleteAuthorizedDevice/{currentDeviceNum}")
	public ModelAndView deleteAuthorizedDevice(@PathVariable("currentDeviceNum") String currentDeviceNum){	
		ModelAndView mav = new ModelAndView();
		
		configService.deleteAuthorizedDevice(currentDeviceNum);
		
		mav.setViewName("redirect:/config/findAllAuthorizedDevice");
		
		return mav;
	}
	
	@RequestMapping("/updateAuthorizedDevice")
	public ModelAndView updateAuthorizedDevice(String device_num, String expiry_date) throws ParseException {	
		ModelAndView mav = new ModelAndView();
		
		configService.updateAuthorizedDeviceExpiryDate(device_num, expiry_date);
		
		List<String> authorizedDeviceNumList = authorizedDeviceDAO.getAuthorizedDeviceNumList();
		DataPoolTools.authorizedDeviceNumSet.addAll(authorizedDeviceNumList);
		
		mav.setViewName("redirect:/config/findAllAuthorizedDevice");
		
		return mav;
	}
	
	@RequestMapping("adminHomepage")
	public ModelAndView adminHomePage(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("frame");
		return mav;
	}
}
