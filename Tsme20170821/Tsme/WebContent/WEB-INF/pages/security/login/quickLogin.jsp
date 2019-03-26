<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>注册后登录</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>

</head>

<body>
<div>
	<form action="/Tsme/j_spring_security_check" method="post">
	<input type="hidden" name="j_username" value="${username}"/>
	<input type="hidden" name="j_password" value="${password}"/>
	<input type="hidden" name="QUICKLOGIN" value="QUICKLOGIN"/>
	</form>
	<script type="text/javascript">
		$("form")[0].submit();
	</script>
</div>
</body>
</html>


