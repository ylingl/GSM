<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>密码找回</title>

<link rel="stylesheet" type="text/css" href="/Tsme/css/public.css"/>
<link rel="stylesheet" type="text/css" href="/Tsme/css/userRegister/userLoginAndRegister.css"/>
<script type="text/javascript" src="/Tsme/js/regularExpression/regExp.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/Tsme/js/security/userLoginAndRegister.js"></script>
<script>
$(function(){
	initInput();
	checkState();
});
javascript:window.history.forward(1); 
</script>
</head>
<body>
	<form id="userRetrieveForm" action="/Tsme/security/retrieve/retrieveAndLogin" method="post">
		<div class="validateFormInput showPlaceholder">
			<div class="accountIcon"></div>
			<input name="username" type="text" class="formInput" value="${username}" readonly="readonly">
		</div>
		<div id="validatePwdDiv" class="validateFormInput showPlaceholder">
			<input id="validateStamp" name="stamp" style="display:none" value="${stamp}">
			<div class="pwdIcon"></div>
			<input id="password" name="password" type="password" class="formInput" tabindex="1" tabindex="1" onkeydown='if(event.keyCode==13){loginBtn.click()}'>
			<label id="PwdPlaceholder" class="loginPwdplaceholder" for="password">请输入6~20个字符作为登录密码</label>
			<label id="pwderrorLog" class="errorLog"></label>
		</div>
		<div class="validateFormInput showPlaceholder">
			<input id="sec_password" name="sec_password" type="password" class="formInput" tabindex="2" tabindex="2" onkeydown='if(event.keyCode==13){loginBtn.click()}' onpaste="return false">
			<div class="pwdIcon"></div>
			<label id="PwdTwicePlaceholder" class="loginPwdConfirmplaceholder" for="sec_password">再次输入</label>
			<label id="ensurePwdErrorLog" class="errorLog"></label>
		</div>
		<div id="loginBtn" class="userInfoLoginBtn" onclick="retrieveAndLogin()">
			<span class="userLoginSpan">登录</span>
		</div>
		<div class="userInfoQQLogin">
		</div>
	</form>		
</body>
</html>