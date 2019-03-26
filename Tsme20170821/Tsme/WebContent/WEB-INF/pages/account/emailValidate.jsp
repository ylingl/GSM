<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册与登录</title>
<link rel="stylesheet" type="text/css" href="/Tsme/css/public.css"/>
<link rel="stylesheet" type="text/css" href="/Tsme/css/userRegister/userLoginAndRegister.css"/>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/Tsme/js/security/userLoginAndRegister.js"></script>
<script>
javascript:window.history.forward(1); 

$(function () {
	$('.backgroundDiv').css('height', $(window).height() - 80);

	var msg = ${msg};
	if (msg == true){	
		document.getElementById("navTitle").innerHTML="验证成功";
		document.getElementById("navRegistText").innerHTML="验证成功，设置密码即可登录";
		loadPage("/Tsme/account/registerSucceed?email=${email}&validate=${validate}");
	}
	else{
		loadPage("/Tsme/account/registerFailed");
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