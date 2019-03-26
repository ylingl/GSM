<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="/Tsme/css/public.css"/>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>

<style type="text/css">
 .table{
     float:left;
     width:930px;  
     font-size: 14px;
 }
 .table .tableHeader {
 	line-height:25px; 
 	background:#eee;
 	font-size:14px;
 	overflow:hidden;
 	text-align:left;
 	border-bottom:1px solid #B3B0B0
 }

 .table .tableHeader span{
 	 float:left;
 	 width:150px;
 }
 
 .showContent {
 	height: 450px;
 }
 
 .showContent li{
 	float:left;
 	text-align:left;
 	width:930px;
	line-height:25px;
	font-size:12px;
 }
 .showContent li.rowStyle_1{background:#f2f2f2;}
 .showContent li span{
 	float:left;
 	width: 150px;
 }
 .fillOpinions{clear: both;margin:10px 0px 10px 20px;}
 </style>
<script type="text/javascript">
	$(function(){
		bindingOperation();
	});
	
	function bindingOperation() {
		$(".showTemplate").off("click").on("click", function() {
			var deviceNum = $(this).parents("li").find("input[name=deviceNum]").val();
			top.location.href = "/Tsme/data/warningTemplate/" + deviceNum;
		});	
	}
</script>
</head>
<body>
	<div class="table">
		<div class="tableHeader">
			<span>设备编号</span>
			<span>设备类型</span>
			<span>设备状态</span>
			<span>经度</span>
			<span>纬度</span>
			<span>操作</span>
		</div>			
		<div class="showContent">
			<c:forEach items="${deviceInfoList}" var="deviceInfo" varStatus="status">
		       	<li class="rowStyle_${status.index%2}">	
		         	<input type="hidden" name="deviceNum" value="${deviceInfo.deviceNum}">
		         	<span>${deviceInfo.deviceNum}</span>
					<span>${deviceInfo.deviceType}</span>
					<span>${deviceInfo.status}&nbsp;</span>
					<span>${deviceInfo.LNG}&nbsp;</span>
					<span>${deviceInfo.LAT}&nbsp;</span>
					<span>
						<a href="javascript:void(0);" class="showTemplate">查看预警模板</a>&nbsp;
					</span>		
				</li>
		  	</c:forEach>
		</div>
	</div>
</body>
</html>