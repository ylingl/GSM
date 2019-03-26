<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>系统角色管理</title>

<link rel="stylesheet" type="text/css" href="/Tsme/css/formatElements.css"/>
<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/style.css" />

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>

<script type="text/javascript"> 

//初始化角色信息值
function initializeRoleformValue(){
	//初始化弹框信息
	var roleName = document.getElementById("roleName");
	roleName.value = "";	
	var roleCode = document.getElementById("roleCode");
	roleCode.value = "";	
}

//初始化角色信息样式
function initializeRoleformStyle(){
	//初始化样式
	$('.roleNameError').css({
		display:'none'
	});

	$('.roleCodeError').css({
		display:'none'
	});	
}

//添加角色信息
function fillRole(){
	initializeRoleformValue();
	initializeRoleformStyle();
	$('#editmodel').modal('show');
}

function presubmit(){
	var form = document.forms["roleForm"];
	
	var roleName = document.getElementById("roleName");
	var roleCode = document.getElementById("roleCode");
	
	var submitToken = true;
	if(roleName.value == "" ){
		submitToken = false;
		$('.roleNameError').css({
			display:'inline'
		});
	}
	else{
		$('.roleNameError').css({
			display:'none'
		});
	}
	if(roleCode.value == "" ){
		submitToken = false;
		$('.roleCodeError').css({
			display:'inline'
		});
	}
	else{
		$('.roleCodeError').css({
			display:'none'
		});
	}
	
	if(submitToken == true){
		var token = document.getElementsByName("token")[0].value;
		
		if(token == "ModifyRole"){
			form.action="/Tsme/security/role/updateRole";
		} else {
			form.action="/Tsme/security/role/getRole";
		}
		
		form.submit();
	}
}

//提交修改请求
function modifyRole(currentRoleId){
	location.href='/Tsme/security/role/modifyRole/' + currentRoleId;
}

//修改角色信息
$(document).ready(function(){
	var token = document.getElementsByName("token")[0].value;
	if(token == "ModifyRole"){
		initializeRoleformStyle();
		$('#editmodel').modal('show');
	}
});

//删除角色信息
function deleteRole(currentRoleId){
	location.href='/Tsme/security/role/deleteRole/' + currentRoleId;
}

</script>
</head>
<body>
<!-- 页面隐藏变量 -->
<div style="display:none;">
	<input type="hidden" name="token" value="${token}"/>
</div>

<!-- 页面显示部分 -->
<div id="adaptive" style="min-height:500px;">
	<div style="width:79%;float:left;margin-left:20px;">
		<div style="width:870px;" align="left">		
			<div id="box" align="center">系统角色管理</div>
			<div align="right">
				<br><input type="button" value="添加角色" onclick="fillRole()">
			</div>
			<br>
			<div style="border: 1px solid #aed0ea;">
				<div align="center" style="float:left;width:330px;font-weight:bold;padding:7px;">角色名称</div>
				<div align="center" style="float:left;width:330px;border-left: 1px solid #aed0ea;border-right: 1px solid #aed0ea;font-weight:bold;padding:7px;">角色代码</div>
				<div align="center" style="float:left;width:200px;font-weight:bold;padding:7px;">相关操作</div>
				<div style="clear:both;"></div>
			</div>			
			<c:forEach items="${roleList}" var="role" varStatus="status">
				<div style="border-bottom: 1px solid #aed0ea;border-left: 1px solid #aed0ea;border-right: 1px solid #aed0ea;">				
					<div align="center" style="float:left;width:330px;padding:7px;">
						${role.name}
					</div>
					<div align="center" style="float:left;width:330px;border-left: 1px solid #aed0ea;border-right: 1px solid #aed0ea;padding:7px;">
						${role.code}
					</div>
					<div align="center" style="padding-top:3px;">
						<input type="button" value="修改" onclick="modifyRole('${role.id}')">&nbsp;
						<input type="button" value="删除" onclick="deleteRole('${role.id}')">
					</div>
					<div style="clear:both;"></div>
				</div>				
			</c:forEach>				
			<div align="right">
				<br><input type="button" value="添加角色" onclick="fillRole()">
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
				<h4 class="modal-title" id="editmodelLabel">填写角色信息</h4>
			</div>
			<div class="modal-body">
				<form:form id="roleForm">
					<input type="hidden" name="roleId" value="${role.id}"/>
					
					<div style="margin-left:80px;">
						角色名称：<input type="text" name="role_name" id="roleName" size="30" value="${role.name}"/>
						<span class="roleNameError" style="font-size:12px;color:#FF0000;">角色名称不能为空！</span>
					</div>		
					
					<br>
					
					<div style="margin-left:80px;">
						角色代码：<input type="text" name="role_code" id="roleCode" size="30" value="${role.code}"/>
						<span class="roleCodeError" style="font-size:12px;color:#FF0000;">角色代码不能为空！</span>
					</div>
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