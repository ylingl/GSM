<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
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

$(function(){
	initPage();
	navTurn();
});

</script>
</head>
<body>
	<div class="backgroundDiv">
		<div class="loginInfo">
			<div style="height:150px"></div>
			<div class="navTitlePos">
				<input type="hidden" id="state" value="${state}">
				<span id="navTitle" class="on">系统登录</span>
				<span id="navLoginText" class="stateChange">已有账户<a>在此登录</a></span>		
				<span id="navRegistText" class="stateChange">还没有系统账号？<a>点击注册</a></span>		
				<div class="userInfoDetail"></div>
			</div>		
		</div>
	</div>
</body>
</html>