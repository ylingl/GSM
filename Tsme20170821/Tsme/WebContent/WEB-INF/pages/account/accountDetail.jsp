<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>训练员审核-查看帐户详细信息</title>
<link rel="stylesheet" type="text/css" href="/DRM/css/public.css"/>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<style type="text/css">
	.table {
	  font-size: 14px;padding:10px 10px;
	  width:500px;
	}
	.table table{
		border:1px solid #000;
		border-collapse:collapse;
		width:100%;
	}
	.table table caption{
		font-weight:bold;
		height:24px;
	}
	.table table td{
		border:1px solid #000;
		height:24px;
		padding-left:2px;
	}
	.table table .name{
		width:100px;
		text-align:center;
		font-weight:bold;
		white-space: nowrap;
	}
</style>
<script type="text/javascript">	
</script>
</head>
<body>
	<center>
	<div>		
		<div class="table">
		<table>
			<caption>训练员审核-查看帐户详细信息</caption>
			<tr>
				<td class="name" nowarp>帐户名称</td>
				<td>${account.username}</td>
			</tr>
			<tr>
				<td class="name">真实姓名</td>
				<td>${account.real_name}</td>
			</tr>
			<tr>
				<td class="name">邮&nbsp;箱&nbsp;号</td>
				<td>${account.email}</td>
			</tr>
			<tr>
				<td class="name">电&nbsp;话&nbsp;号</td>
				<td>${account.phone_num}</td>
			</tr>
		</table>
		</div>
	</div>	
	</center>
</body>
</html>