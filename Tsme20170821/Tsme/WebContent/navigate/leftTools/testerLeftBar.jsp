<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">

<title>Insert title here</title>

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>

<script type="text/javascript">
	$(function() {
		initControl();
	});
	
	function initControl() {
		$(".divLeft1").on("click",function() {
			var url = "http://" + location.host + "/Tsme/history/getDeviceInfoToShowHistory";
			$("#rightAreaIframe").attr("src", url);
		});
	}
</script>
</head>
<body>
	<div style="padding-left:10px;padding-right:10px;">
		<div align="left" style="padding:8px;">
			<a class="divLeft1" style="text-decoration:none;font-size:12px;color:#003399;font-weight:bold;" href="javascript:void(0);">监测数据管理</a>			
		</div>
	</div>
</body>
</html>