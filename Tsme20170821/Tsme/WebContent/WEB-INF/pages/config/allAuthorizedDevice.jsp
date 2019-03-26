<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>监测设备管理</title>

<link rel="stylesheet" type="text/css" href="/Tsme/css/formatElements.css"/>
<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/style.css" />

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/Tsme/js/calendar/calendar.js"></script>

<script type="text/javascript"> 

//初始化角色信息值
function initializeValue(){
	//初始化弹框信息
	var deviceNum = document.getElementById("deviceNum");
	deviceNum.value = "";
	var expiryDate = document.getElementById("expiryDate");
	expiryDate.value = "";
}

/*日历插件中有此函数，为空即可*/
function validateDate(){
	
}

//初始化角色信息样式
function initializeStyle(){
	//初始化样式
	$('.deviceNumError').css({
		display:'none'
	});

	$('.expiryDateError').css({
		display:'none'
	});	
}

//添加角色信息
function fillAuthorizedDevice(){
	initializeValue();
	initializeStyle();
	$('#deviceNum').removeAttr("readonly");
	$('#editmodel').modal('show');
}

function presubmit(){
	var form = document.forms["deviceForm"];
	
	var deviceNum = document.getElementById("deviceNum");
	var expiryDate = document.getElementById("expiryDate");
	
	var submitToken = true;
	if(deviceNum.value == "" ){
		submitToken = false;
		$('.deviceNumError').css({
			display:'inline'
		});
	}
	else{
		$('.deviceNumError').css({
			display:'none'
		});
	}
	
	if(expiryDate.value == "" ){
		submitToken = false;
		$('.expiryDateError').css({
			display:'inline'
		});
	}
	else{
		$('.expiryDateError').css({
			display:'none'
		});
	}
	
	if(submitToken == true){
		if(token == "Modify"){
			form.action="/Tsme/config/updateAuthorizedDevice";
			token = "";
		} else {
			form.action="/Tsme/config/addAuthorizedDevice";
		}
		
		form.submit();
	}
}

var token;

//提交修改请求
function modifyDevice(deviceNum, expiryDate){
	$('#deviceNum').attr("readonly","readonly");
	$('#deviceNum').val(deviceNum);
	$('#expiryDate').val(expiryDate);
	$('#editmodel').modal('show');
	initializeStyle();
	token = "Modify";
}

//删除角色信息
function deleteDevice(currentDeviceNum){
	layer.msg('正在删除，请稍候····', {icon: 1});	
	location.href='/Tsme/config/deleteAuthorizedDevice/' + currentDeviceNum;
}

</script>
</head>
<body>
<!-- 页面显示部分 -->
<div id="adaptive" style="min-height:500px;">
	<div style="width:79%;float:left;margin-left:20px;">
		<div style="width:870px;" align="left">		
			<div id="box" align="center">监测设备管理</div>
			<div align="right">
				<br><input type="button" value="添加设备" onclick="fillAuthorizedDevice()">
			</div>
			<br>
			<div style="border: 1px solid #aed0ea;">
				<div align="center" style="float:left;width:165px;font-weight:bold;padding:7px;">设备编号</div>
				<div align="center" style="float:left;width:165px;border-left: 1px solid #aed0ea;font-weight:bold;padding:7px;">启用日期</div> 
				<div align="center" style="float:left;width:165px;border-left: 1px solid #aed0ea;font-weight:bold;padding:7px;">失效日期</div> 
				<div align="center" style="float:left;width:165px;border-left: 1px solid #aed0ea;border-right: 1px solid #aed0ea;font-weight:bold;padding:7px;">创建者</div> 
				<div align="center" style="float:left;width:200px;font-weight:bold;padding:7px;">相关操作</div>
				<div style="clear:both;"></div>
			</div>			
			<c:forEach items="${authorizedDeviceList}" var="authorizedDevice" varStatus="status">
				<div style="border-bottom: 1px solid #aed0ea;border-left: 1px solid #aed0ea;border-right: 1px solid #aed0ea;">				
					<div align="center" style="float:left;width:165px;padding:7px;">
						${authorizedDevice.device_num}
					</div>
					<div align="center" style="float:left;width:165px;border-left: 1px solid #aed0ea;padding:7px;">
						${authorizedDevice.valid_date}
					</div>
					<div align="center" style="float:left;width:165px;border-left: 1px solid #aed0ea;padding:7px;">
						<fmt:formatDate pattern="yyyy-MM-dd" value="${authorizedDevice.expiry_date}"/>
					</div>
					<div align="center" style="float:left;width:165px;border-left: 1px solid #aed0ea;border-right: 1px solid #aed0ea;padding:7px;">
						${authorizedDevice.creator_id}
					</div>
					<div align="center" style="padding-top:3px;">
						<input type="button" value="修改有效期" onclick="modifyDevice('${authorizedDevice.device_num}', '<fmt:formatDate pattern="yyyy-MM-dd" value="${authorizedDevice.expiry_date}"/>')">&nbsp;
						<input type="button" value="删除" onclick="deleteDevice('${authorizedDevice.device_num}')">
					</div>
					<div style="clear:both;"></div>
				</div>				
			</c:forEach>				
			<div align="right">
				<br><input type="button" value="添加设备" onclick="fillAuthorizedDevice()">
			</div>
			<br>
		</div>
	</div>
</div>	

<!-- Role添加页面 -->
<div class="modal fade in" style="top:100px;" id="editmodel" tabindex="-1" role="dialog" aria-labelledby="editmodelLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				</button>
				<h4 class="modal-title" id="editmodelLabel">填写授权设备信息</h4>
			</div>
			<div class="modal-body">
				<form:form id="deviceForm">
					<input type="hidden" name="authorizedDeviceId" value="${authorizedDevice.id}"/>
					
					<div style="margin-left:80px;">
						设备编号：<input type="text" id="deviceNum" name="device_num" size="30" value="${authorizedDevice.device_num}"/>
						<span class="deviceNumError" style="font-size:12px;color:#FF0000;">设备编号不能为空！</span>
					</div>
					<br>
					<div style="margin-left:80px;">
						失效日期：<input type="text" id="expiryDate" name="expiry_date" size="30" value="${authorizedDevice.expiry_date}" onclick="calendar.show(this);" readonly="readonly" style="cursor:pointer;"/>
						<span class="expiryDateError" style="font-size:12px;color:#FF0000;">失效日期不能为空！</span>
					</div>			
					<br>
				</form:form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="presubmit();">提交</button>
			</div>
		</div>
	</div>
</div>
<!-- Role添加页面 结束 -->

</body>
</html>