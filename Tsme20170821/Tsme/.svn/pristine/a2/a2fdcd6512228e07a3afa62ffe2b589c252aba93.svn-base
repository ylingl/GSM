package utils;

import javax.servlet.http.HttpServletRequest;

public class ValidateCodeTools {
	
	public boolean check(String validateCode, HttpServletRequest request){
    	//从session中获取正确的验证码
    	String sessionValidateCode = "";
    	Object obj = request.getSession().getAttribute("SESSION_VALIDATE_CODE");
    	if(obj != null){
    		sessionValidateCode = obj.toString();
    		if(sessionValidateCode.equalsIgnoreCase(validateCode))
    			return true;
    		else
    			return false;
    	}
    	else
    		return false;	
	}

}
