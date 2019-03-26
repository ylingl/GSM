<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>密码找回</title>

<link rel="stylesheet" type="text/css" href="/Tsme/css/public.css"/>
<link rel="stylesheet" type="text/css" href="/Tsme/css/userRegister/userLoginAndRegister.css"/>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/Tsme/js/security/userLoginAndRegister.js"></script>
<script>
javascript:window.history.forward(1); 

if(window != top){
	top.location.href = location.href;
}
$(function () {
		var msg = ${msg};
		if (msg == true){			
			document.getElementById("navTitle").innerHTML="找回密码";
			document.getElementById("navRegistText").innerHTML="申请成功，重新设置密码即可登录";
			loadPage("/Tsme/security/retrieve/retreiveSucceed?username=${username}&stamp=${stamp}");
		}
		else{
			loadPage("/Tsme/security/retrieve/retrieveFailed");
		}
})
 </script> 
</head>
<body>
	<div class="backgroundDiv">
		<div class="loginInfo">
			<div style="height:150px"></div>
			<div class="navTitlePos">
				<span id="navTitle"  class="on"></span>
				<span id="navRegistText" class="emailValidate"></span>		
				<div class="userInfoDetail"></div>
			</div>		
		</div>
	</div>
</body>
</html>