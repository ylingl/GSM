package utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import security.MyUserDetails.MyProperty;
import security.MyUserDetails.MyUserDetails;

public class AccountTools {
	
	//获取当前用户的ID
	public String getCurrentAccountId(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		MyUserDetails myUserDetails = new MyUserDetails();
		if (principal instanceof MyUserDetails) {
			myUserDetails = (MyUserDetails) principal;
			return myUserDetails.getId();
		}
		
		return null;
	}
	
	//获取当前用户的Name
	public String getCurrentAccountName(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		MyUserDetails myUserDetails = new MyUserDetails();
		if (principal instanceof MyUserDetails) {
			myUserDetails = (MyUserDetails) principal;
			return myUserDetails.getUsername();
		}
		
		return null;
	}
	
	//获取当前用户的角色
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
	
	//查看当前是否有用户处于登录状态
	public boolean isAnyAccountInLoggedState() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || ( authentication.getPrincipal().equals("anonymousUser") && authentication.getAuthorities().toString().contains("ROLE_ANONYMOUS"))) {
			return false;
		}
		else {
			return true;
		}
	}
	
	//判断当前用户是否具有发布者权限
	public boolean doesCurrentAccountHasTesterRole() {
		List<String> roleList = getCurrentAccountRoles();
		
		for(String role : roleList) {
			if(role.equals("ROLE_TESTER")){
				return true;
			}
		}
		
		return false;
	}
	
	//判断当前用户是否具有购买者权限
	public boolean doesCurrentAccountHasTrainerRole() {
		List<String> roleList = getCurrentAccountRoles();
		
		for(String role : roleList) {
			if(role.equals("ROLE_TRAINER")){
				return true;
			}
		}
		
		return false;
	}
	
	//判断当前用户是否具有管理员权限
	public boolean doesCurrentAccountHasAdminRole() {
		List<String> roleList = getCurrentAccountRoles();
		
		for(String role : roleList) {
			if(role.equals("ROLE_ADMIN")){
				return true;
			}
		}
		
		return false;
	}
	
	//判断当前用户是否具有超级管理员权限
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
