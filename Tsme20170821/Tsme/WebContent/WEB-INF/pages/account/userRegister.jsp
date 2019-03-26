<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
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
<body onload="changeCheckNum();">
	<form:form id="userRegisterForm" enctype="multipart/form-data" method="post">
		<div id="registerAccountDiv" class="validateFormInput">
			<div class="accountIcon"></div>
			<input type="text" name="email" id="username" class="formInput" tabindex="1" onkeydown='if(event.keyCode==13){registerBtn.click()}'>
			<label id="userrorLog" class="errorLog"></label>
			<label id="AccountPlaceholder" class="loginUserplaceholder" for="username">请输入电子邮箱作为账号</label>
		</div>
		<div  class="userInfoInputValidate" >		
			<div class="userInfoInputValidateInput">
				<div id="validateDiv" class="validateCodeInput">
					<input type="text" name="validateCode" class="validateCodeInput" id="validateCode" tabindex="2" onkeydown='if(event.keyCode==13){registerBtn.click()}'>
					<label id="validateCodeErrorLog" class="errorLog"></label>
					<label id="validatePlaceholder" class="validateCodeRegisterplaceholder" for="validateCode">验证码</label>
			</div>
			<img id="checkNumImage" class="validatePic" src="/Tsme/security/login/checkNumberShow" onclick="changeCheckNum()" title="点击换一张"/>
			<span id="changeCodeImage" class="validateSpan" onclick="changeCheckNum()">换一张</span>
			<script type="text/javascript">
				$("#checkNumImage").attr("src", "/Tsme/security/login/checkNumberShow" + "?tempid=" + Math.random());
				</script>
			</div>
		</div>
		<div id="registerBtn" class="userInfoLoginBtn" onclick="checkInfo()">
			<span class="userLoginSpan">注册</span>
		</div>
		<div class="userInfoQQLogin">
		</div>
	</form:form>		
</body>
</html>