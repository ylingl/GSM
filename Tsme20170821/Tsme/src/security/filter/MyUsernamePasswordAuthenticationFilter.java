package security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import utils.ValidateCodeTools;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {	
		if (!request.getMethod().equals("POST")){
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		//dialogFlag�����ж��Ƿ�ӵ��򴰿ڵ�¼
		HttpSession session = request.getSession();
		String dialogFlag = request.getParameter("dialogFlag");
		session.setAttribute("dialogFlag", dialogFlag);

		//��֤�û���������
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		
		if (username == null){
			username = "";
		}
		
		if (password == null){
			password = "";
		}
		
		username = username.trim();
	
		Object pwdErrorCount = session.getAttribute("pwdErrorCount");
		if(pwdErrorCount != null){
			//�˲���֤��
			checkValidateCode(request);
		}

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		
		Authentication authentication = getAuthenticationManager().authenticate(authRequest);
		
		return authentication;
	}
	
	protected void checkValidateCode(HttpServletRequest request) {
		//�ӱ����л�ȡ�û���д����֤��
		String validateCode = request.getParameter("validateCode");
		
		ValidateCodeTools validateCodeTools = new ValidateCodeTools();
		
		//ϵͳ�����ȡ����֤�룬���û���򱨴�
		if(!validateCodeTools.check(validateCode, request)){
			throw new AuthenticationServiceException("��֤���������������");
		}
	}
}