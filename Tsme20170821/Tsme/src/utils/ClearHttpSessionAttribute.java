package utils;

import javax.servlet.http.HttpSession;

public class ClearHttpSessionAttribute {

	public ClearHttpSessionAttribute(HttpSession httpSession){
		
		httpSession.removeAttribute("currentDeviceNum");
		httpSession.removeAttribute("currentWarningTemplateId");
		httpSession.removeAttribute("currentWarningTemplateLine");
		httpSession.removeAttribute("originalSpetrum");
		httpSession.removeAttribute("originalDemodList");
		httpSession.removeAttribute("showDemodResultMap");
		httpSession.removeAttribute("deviceFBIdQriginalDataMap");
		
	}
}
