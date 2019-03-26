package security.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import tsme.table.authorizedDevice.DAO.AuthorizedDeviceDAO;
import tsme.table.controlProperty.bean.CONTROLPROPERTY;
import tsme.table.rescPath.DAO.RescPathDAO;
import tsme.table.rescPath.bean.RESCPATH;
import utils.DataPoolTools;

public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	
	@Autowired
	@Qualifier("rescPathDAO")
	private RescPathDAO rescPathDAO;
	
	@Autowired
	@Qualifier("authorizedDeviceDAO")
	private AuthorizedDeviceDAO authorizedDeviceDAO;
	
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	/**
	 * 在发布过程中，此方法即被执行，因此resourceMap作为静态变量将被贮存在系统之中
	 */
	public void loadResource() {
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		
		List<RESCPATH> rescPathList = rescPathDAO.cascadedQueryAll(true, "ASC");
		if(rescPathList != null){
			for (RESCPATH rescPath : rescPathList) {
				Collection<ConfigAttribute> configAttributeList = new ArrayList<ConfigAttribute>();
				for(CONTROLPROPERTY controlProperty : rescPath.getControlPropertyList()){
					//将属性整合在一起
					String para = controlProperty.getRole_code();
					ConfigAttribute configAttribute = new SecurityConfig(para);
					configAttributeList.add(configAttribute);
				}
				
				resourceMap.put(rescPath.getUri(), configAttributeList);
			}
		}
		
		//加载授权设备列表
		loadAuthorizedDeviceNumSet();
    }
	
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object obj) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (resourceMap == null) {
			loadResource();
		}
		String requestUrl = ((FilterInvocation) obj).getRequestUrl();
		
		Iterator<String> iter = resourceMap.keySet().iterator();
		while (iter.hasNext()) {
			String rescURL = iter.next();
			if (pathMatcher.match(rescURL, requestUrl)) {
				return resourceMap.get(rescURL);
			}
		}
		
		Collection<ConfigAttribute> empty = new ArrayList<ConfigAttribute>();
		empty.add(null);
		return empty;
	}
	
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private void loadAuthorizedDeviceNumSet() {
		List<String> authorizedDeviceNumList = authorizedDeviceDAO.getAuthorizedDeviceNumList();
		DataPoolTools.authorizedDeviceNumSet.addAll(authorizedDeviceNumList);
	}

}
