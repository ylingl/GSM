<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册与登录--绥知</title>
<link rel="shortcut icon" href="/DRM/images/logo/suizhi.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="/DRM/css/public.css"/>
<link rel="stylesheet" type="text/css" href="/DRM/css/userRegister/userLoginAndRegister.css"/>
<script type="text/javascript" src="/DRM/js/regularExpression/regExp.js"></script>
<script type="text/javascript" src="/DRM/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/DRM/js/security/userLoginAndRegister.js"></script>
<style>
.topTitle{
	font-size:20px;
	text-align:center;
	position:absolute;
	width:100%;
	font-family:Microsoft YaHei;
}
.content{
	text-align:center;
	position:absolute;
	top:40px;
	width:100%;
	font-family:Microsoft YaHei;
}
span{
	color:#327bb9;
	font-size:14px;
}
span:hover{
 cursor:pointer
}
</style>
</head>
<body>
	<div class="userInfoEmailValidate">
		<div class="topTitle">哦哦~链接失效了！</div>
		<div class="content">继续<span onclick="location='/Tsme/security/login/registerAndLogin?state=retrieve'">找回密码</span>，坚持就会胜利的~或者，<span onclick="location='/Tsme/security/login/registerAndLogin?state=register'">重新注册</span>吧。</div>
	</div>
</body>
</html>