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
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>

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
 	text-align:center;
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
 	text-align:center;
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

</head>
<body>
	<div class="table">
		<div class="tableHeader">
			<span>姓名</span>
			<span>电话</span>
			<span>审核日期</span>
			<span>审核状态</span>
			<span>审核人</span>
			<span>审核意见</span>
		</div>			
		<div class="showContent">
			<c:forEach items="${trainerAuditList}" var="trainerAudit" varStatus="status">
		       	<li class="rowStyle_${status.index%2}">	
		         	<input type="hidden" name="id" value="${trainerAudit.id}">
		       		<input type="hidden" name="account_id" value="${trainerAudit.account_id}">
					<span>${trainerAudit.real_name}</span>
					<span>${trainerAudit.phone_num}</span>
					<span><fmt:formatDate value="${trainerAudit.audit_date}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;</span>
					<span>${trainerAudit.state}&nbsp;</span>
					<span>${trainerAudit.auditor_name}&nbsp;</span>
					<span>${trainerAudit.audit_opinions}&nbsp;</span>		
				</li>
		  	</c:forEach>
		</div>
	</div>
</body>
</html>