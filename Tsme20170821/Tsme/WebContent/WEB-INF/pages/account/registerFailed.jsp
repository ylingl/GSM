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
<style>
.topTitle{
	font-size:20px;
	text-align:center;
	width:100%;
	font-family:Microsoft YaHei;
	position:absolute;
}

.content{
	text-align:center;
	width:100%;;
	font-family:Microsoft YaHei;
	height:40px;
	position:absolute;
	top:40px;
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
		<div class="content"><span onclick="location='/Tsme/security/login/registerAndLogin?state=register'">重新注册</span>，坚持就会胜利的~已经有账户了，点此<span onclick="location='/Tsme/security/login/registerAndLogin?state=login'">登录</span>~</div>
	</div>
</body>
</html>