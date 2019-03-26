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

<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/style.css" />

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>

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
		$(".disable").off("click").on("click", function() {
			layer.msg('申请提交中，请等待...', {icon: 1});
			var accountId = $(this).parents("li").find("input[name=id]").val();
			$.ajax({
				async : false,
				cache : false,
				url : "/Tsme/account/setAccountActiveAttribute",
				type : "POST",
				data : {'accountId':accountId, 'active':false},
				dataType : "json",
				success : function(result){
					if(result){
						self.location = "/Tsme/account/findAllAccount";
					}
				},
				error: function(error){
					alert(error);
				}
			});
		});
		
		$(".enable").off("click").on("click", function() {
			layer.msg('申请提交中，请等待...', {icon: 1});
			var accountId = $(this).parents("li").find("input[name=id]").val();
			$.ajax({
				async : false,
				cache : false,
				url : "/Tsme/account/setAccountActiveAttribute",
				type : "POST",
				data : {'accountId':accountId, 'active':true},
				dataType : "json",
				success : function(result){
					if(result){
						self.location = "/Tsme/account/findAllAccount";
					}
				},
				error: function(error){
					alert(error);
				}
			});
		});
		
		$(".modifyRoles").off("click").on("click", function() {
			var accountId = $(this).parents("li").find("input[name=id]").val();
			$("#currentAccountId").val(accountId);
			$.ajax({
				async : false,
				cache : false,
				url : "/Tsme/account/getAccountRoleList",
				type : "POST",
				data : {'accountId':accountId},
				dataType : "json",
				success : function(result){
					if(result != null){
						var roleList = result.roleList;
						var accountRoleList = result.accountRoleList;
						if(roleList.length > 0){
							var content = "";
							for(var i = 0; i < roleList.length; i ++){
								var flag = false;
								for(var j = 0; j < accountRoleList.length; j ++){
									if(roleList[i].code == accountRoleList[j]){
										flag = true;
										break;
									}
								}
								if(flag){
									content = content + "<div style='float:left;width:30%;font-size:12px;padding:5px 10px;'><input type='checkbox' name='roleList' value='" 
									+ roleList[i].code + "' checked>" + roleList[i].code + "</div>";
								} else {
									content = content + "<div style='float:left;width:30%;font-size:12px;padding:5px 10px;'><input type='checkbox' name='roleList' value='" 
									+ roleList[i].code + "'>" + roleList[i].code + "</div>";
								}
							}
							$("#rolePole").html(content);
							
							$('#modifyRolesModel').modal('show');
						}
						
					}
				},
				error: function(error){
					alert(error);
				}
			});
		});	
		
	}
	
	function modifyRolesPresubmit() {
		layer.msg('申请提交中，请等待...', {icon: 1});
		$.ajax({
			async : false,
			cache : false,
			url : "/Tsme/account/modifyAccountProperty",
			type : "POST",
			data : $("#modifyRolesForm").serialize(),
			dataType : "json",
			success : function(result){
				if(result){
					self.location = "/Tsme/account/findAllAccount";
				}
			},
			error: function(error){
				alert(error);
			}
		});
	}
	
</script>
</head>
<body>
	<div class="table">
		<div class="tableHeader">
			<span>帐户</span>
			<span>姓名</span>
			<span>电话</span>
			<span>是否启用</span>
			<span>角色</span>
			<span>操作</span>
		</div>			
		<div class="showContent">
			<c:forEach items="${accountInfoList}" var="accountInfo" varStatus="status">
		       	<li class="rowStyle_${status.index%2}">	
		         	<input type="hidden" name="id" value="${accountInfo.id}">
		         	<span>${accountInfo.username}</span>
					<span>${accountInfo.real_name}&nbsp;</span>
					<span>${accountInfo.phone_num}&nbsp;</span>
					<span>${accountInfo.active}</span>
					<span>${accountInfo.roles}</span>
					<span>
					<c:if test="${accountInfo.active}">
						<a href="javascript:void(0);" class="disable">停用</a>
					</c:if>
					<c:if test="${accountInfo.active == false}">
						<a href="javascript:void(0);" class="enable">启用</a>
					</c:if>
						<a href="javascript:void(0);" class="modifyRoles">更改角色</a>
					</span>		
				</li>
		  	</c:forEach>
		</div>
	</div>

<!-- controlProperty添加页面 -->
<div class="modal fade in" style="top:100px;" id="modifyRolesModel" tabindex="-1" role="dialog" aria-labelledby="modifyRolesLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				</button>
				<h4 class="modal-title" id="modifyRolesLabel">修改用户角色</h4>
			</div>
			<div class="modal-body">
				<form:form modelAttribute="recRescPath" id="modifyRolesForm">
					<input type="hidden" name="currentAccountId" id="currentAccountId">
					<div style="font-size:12px;font-weight:bold;" id="rolePole">角色选项</div>
					<div style="clear:both;"></div>
				</form:form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="modifyRolesPresubmit();">提交</button>
			</div>
		</div>
	</div>
</div>
<!-- controlProperty添加页面 结束 -->

</body>
</html>