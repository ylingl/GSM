package utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import security.MyUserDetails.MyProperty;
import security.MyUserDetails.MyUserDetails;

public class AccountTools {
	
	//��ȡ��ǰ�û���ID
	public String getCurrentAccountId(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		MyUserDetails myUserDetails = new MyUserDetails();
		if (principal instanceof MyUserDetails) {
			myUserDetails = (MyUserDetails) principal;
			return myUserDetails.getId();
		}
		
		return null;
	}
	
	//��ȡ��ǰ�û���Name
	public String getCurrentAccountName(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		MyUserDetails myUserDetails = new MyUserDetails();
		if (principal instanceof MyUserDetails) {
			myUserDetails = (MyUserDetails) principal;
			return myUserDetails.getUsername();
		}
		
		return null;
	}
	
	//��ȡ��ǰ�û��Ľ�ɫ
	public List<String> getCurrentAccountRoles(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		MyUserDetails myUserDetails = new MyUserDetails();
		List<String> roleCodeList = new ArrayList<String>();
		if (principal instanceof MyUserDetails) {
			myUserDetails = (MyUserDetails) principal;
			for(MyProperty myProperty : myUserDetails.getMyPropertyList()){
				roleCodeList.add(myProperty.getRole());
			}
		}
		
		return roleCodeList;
	}
	
	//�鿴��ǰ�Ƿ����û����ڵ�¼״̬
	public boolean isAnyAccountInLoggedState() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || ( authentication.getPrincipal().equals("anonymousUser") && authentication.getAuthorities().toString().contains("ROLE_ANONYMOUS"))) {
			return false;
		}
		else {
			return true;
		}
	}
	
	//�жϵ�ǰ�û��Ƿ���з�����Ȩ��
	public boolean doesCurrentAccountHasTesterRole() {
		List<String> roleList = getCurrentAccountRoles();
		
		for(String role : roleList) {
			if(role.equals("ROLE_TESTER")){
				return true;
			}
		}
		
		return false;
	}
	
	//�жϵ�ǰ�û��Ƿ���й�����Ȩ��
	public boolean doesCurrentAccountHasTrainerRole() {
		List<String> roleList = getCurrentAccountRoles();
		
		for(String role : roleList) {
			if(role.equals("ROLE_TRAINER")){
				return true;
			}
		}
		
		return false;
	}
	
	//�жϵ�ǰ�û��Ƿ���й���ԱȨ��
	public boolean doesCurrentAccountHasAdminRole() {
		List<String> roleList = getCurrentAccountRoles();
		
		for(String role : roleList) {
			if(role.equals("ROLE_ADMIN")){
				return true;
			}
		}
		
		return false;
	}
	
	//�жϵ�ǰ�û��Ƿ���г�������ԱȨ��
	public boolean doesCurrentAccountHasSuperadminRole() {
		List<String> roleList = getCurrentAccountRoles();
		
		for(String role : roleList) {
			if(role.equals("ROLE_SUPERADMIN")){
				return true;
			}
		}
		
		return false;
	}
	
}
