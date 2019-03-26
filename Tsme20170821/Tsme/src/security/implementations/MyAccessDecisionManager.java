package security.implementations;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class MyAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object obj, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException{
		// TODO Auto-generated method stub
		if(configAttributes.contains(null)){
			//throw new AccessDeniedException("û��Ȩ�޷��ʣ�");
			return;
		}
		
		Iterator<ConfigAttribute> ite = configAttributes.iterator();
		
 		while(ite.hasNext()){
			ConfigAttribute configAttribute = ite.next();//configAttributeΪ��׼��Դ���Ʊ�
			String neededProperty = configAttribute.getAttribute();
			
			for(GrantedAuthority myProperty : authentication.getAuthorities()){
				if(neededProperty.equalsIgnoreCase(myProperty.getAuthority())){
					return;
				}
			}
		}
		
		throw new AccessDeniedException("û��Ȩ�޷��ʣ�");
	}

	@Override
	public boolean supports(ConfigAttribute arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
