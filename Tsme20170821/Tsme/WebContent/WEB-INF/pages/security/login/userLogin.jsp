<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>注册与登录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/Tsme/css/public.css"/>
<link rel="stylesheet" type="text/css" href="/Tsme/css/userRegister/userLoginAndRegister.css"/>
<script type="text/javascript" src="/Tsme/js/regularExpression/regExp.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/Tsme/js/security/userLoginAndRegister.js"></script>
<script>
$(function(){
	initInput();
	checkState();
	changeCheckNum();
});
javascript:window.history.forward(1); 
</script>
</head>
<body onload="changeCheckNum();">
 <form:form modelAttribute="account" action="/Tsme/j_spring_security_check" method="post" id="userLoginForm">
	<div id="loginAccountDiv" class="validateFormInput showPlaceholder">
		<div class="accountIcon" ></div>
		<input type="text" name="j_username" id="username" class="formInput" tabindex="1" autocomplete="off">
		<label id="userrorLog" class="errorLog"></label>
		<label id="AccountPlaceholder" class="loginUserplaceholder"  for="username">账号</label>
	</div>
	<div id="passwordDiv" class="validateFormInput showPlaceholder">
		<input type="password" name="j_password" id="password" class="formInput" tabindex="2" onkeydown='if(event.keyCode==13){loginBtn.click()}' autocomplete="off">
		<div class="pwdIcon"></div>
		<label id="pwderrorLog" class="errorLog"></label>
		<label id="PwdPlaceholder" class="loginPwdplaceholder" for="password">密码</label>
	</div>
	<div class="userInfoInputValidate validatehide">	
		<div class="userInfoInputValidateInput">
			<div id="validateDiv" class="validateCodeInput">
				<input type="text" name="validateCode" class="validateCodeInput" id="validateCode" tabindex="3" onkeydown='if(event.keyCode==13){loginBtn.click()}' autocomplete="off">
				<label id="validateCodeErrorLog" class="errorLog"></label>
				<label id="validatePlaceholder" class="validateCodeplaceholder" for="validateCode">验证码</label>
			</div>
			<img id="checkNumImage" class="validatePic" src="/Tsme/security/login/checkNumberShow" onclick="changeCheckNum()" title="点击换一张"/>
			<span id="changeCodeImage" class="validateSpan" onclick="changeCheckNum()">换一张</span>
			<script type="text/javascript">
				$("#checkNumImage").attr("src", "/Tsme/security/login/checkNumberShow" + "?tempid=" + Math.random());
			</script>
		</div>				
	</div>
	<div id="loginBtn" class="userInfoLoginBtn"  onclick="userSubmit()">
		<span class="userLoginSpan">登录</span>
	</div>	
	<div class="userInfoQQLogin">
		<a class="forget">忘记密码?</a>
	</div>
 </form:form> 
</body>
</html>