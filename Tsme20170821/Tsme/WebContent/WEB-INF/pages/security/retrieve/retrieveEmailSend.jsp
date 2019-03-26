<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邮箱验证</title>

<link rel="stylesheet" type="text/css" href="/Tsme/css/public.css"/>
<link rel="stylesheet" type="text/css" href="/Tsme/css/userRegister/userLoginAndRegister.css"/>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/Tsme/js/security/userLoginAndRegister.js"></script>
<script>
javascript:window.history.forward(1); 
</script>
</head>
<body>
	<div class="userInfoEmailValidate">
		<span class="title">请查收邮箱！</span>
		<span class="check">邮件已发送到${account}，请<a href="${domain}">查收邮件</a>修改账号</span>
		<span class="check">没有收到邮件？请耐心等待，或<a href="/Tsme/security/retrieve/retrieveEmailSend?username=${username}&stamp=${stamp}">重新发送</a></span>
	</div>
</body>
</html>