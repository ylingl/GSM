package mvc.tsme;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TsmeController {
	
	@RequestMapping("tsmeIndex")
	public ModelAndView tsmeIndex(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addData");
		return mav;
	}
	
	@RequestMapping("iterator")
	public ModelAndView iterator(String deviceId){
		ModelAndView mav = new ModelAndView();
		mav.addObject("deviceId", deviceId);
		mav.setViewName("iterator");
		return mav;
	}
	
	@RequestMapping("initDevice")
	public ModelAndView initDevice(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("initDevice");
		return mav;
	}
}
