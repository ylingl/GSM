<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册与登录</title>

<link rel="stylesheet" type="text/css" href="/Tsme/css/public.css"/>
<link rel="stylesheet" type="text/css" href="/Tsme/css/userRegister/userLoginAndRegister.css"/>
<script type="text/javascript" src="/Tsme/js/regularExpression/regExp.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/Tsme/js/security/userLoginAndRegister.js"></script>
<script>
$(function(){
	initInput();
});
javascript:window.history.forward(1); 
</script>
</head>
<body>
	<form id="userRegisterForm" action="/Tsme/security/login/registerComplete" method="post">
		<div class="validateFormInput showPlaceholder">
			<div class="accountIcon"></div>
			<input name="username" type="text" class="formInput" value="${email}" readonly="readonly">
			<input name="validate" style="display:none" value="${validate}">
		</div>
		<div id="validatePwdDiv" class="validateFormInput showPlaceholder">
			<div class="pwdIcon"></div>
			<input id="password" name="password" type="password" class="formInput" tabindex="1" onkeydown='if(event.keyCode==13){loginBtn.click()}'>
			<label id="PwdPlaceholder" class="loginPwdplaceholder" for="password">请输入6~20个字符作为密码</label>
			<label id="pwderrorLog" class="errorLog"></label>
		</div>
		<div id="sec_validatePwdDiv" class="validateFormInput showPlaceholder">
			<input id="sec_password" name="sec_password" type="password" class="formInput" tabindex="2" onkeydown='if(event.keyCode==13){loginBtn.click()}' onpaste="return false">
			<div class="pwdIcon"></div>
			<label id="PwdTwicePlaceholder" class="loginPwdConfirmplaceholder" for="sec_password">再次输入密码</label>
			<label id="ensurePwdErrorLog" class="errorLog"></label>
		</div>
		<div id="loginBtn" class="userInfoLoginBtn" onclick="registerAndLogin()">
			<span class="userLoginSpan">登录</span>
		</div>
		<div class="userInfoQQLogin">
		</div>
	</form>		
</body>
</html>