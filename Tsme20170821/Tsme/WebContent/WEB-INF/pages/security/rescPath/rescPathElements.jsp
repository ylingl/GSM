<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>系统资源管理</title>

<link rel="stylesheet" type="text/css" href="/Tsme/css/formatElements.css"/>
<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/style.css" />

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>

<script type="text/javascript">

//获取锚点
function getAnchor(){
	//锚点
	if (document.documentElement) {
		var y = document.documentElement.scrollTop;
		return y;
	} 
	else if (document.body){
		var y = document.body.scrollTop;
		return y;
	}
}

//初始化资源信息值
function initializeRescPathformValue(){
	//初始化弹框信息
	var rescPathUrl = document.getElementById("rescPathUrl");
	rescPathUrl.value = "";	
	var rescPathFunction = document.getElementById("rescPathFunction");
	rescPathFunction.value = "";	
}

//初始化资源信息样式
function initializeRescPathformStyle(){
	//初始化样式
	$('.rescPathUrlError').css({
		display:'none'
	});

	$('.rescPathFunctionError').css({
		display:'none'
	});	
}

//添加资源信息
function fillRescPath(){
	initializeRescPathformValue();
	initializeRescPathformStyle();
	$('#rescPathModel').modal('show');
}

function rescPathPresubmit(){
	var form = document.forms["rescPathForm"];
	var anchor = getAnchor();
		
	var rescPathUrl = document.getElementById("rescPathUrl");
	var rescPathFunction = document.getElementById("rescPathFunction");
	
	var submitToken = true;
	if(rescPathUrl.value == "" ){
		submitToken = false;
		$('.rescPathUrlError').css({
			display:'inline'
		});
	}
	else{
		$('.rescPathUrlError').css({
			display:'none'
		});
	}
	if(rescPathFunction.value == "" ){
		submitToken = false;
		$('.rescPathFunctionError').css({
			display:'inline'
		});
	}
	else{
		$('.rescPathFunctionError').css({
			display:'none'
		});
	}
	if(submitToken == true){
		var token = document.getElementsByName("token")[0].value;
		
		if(token == "ModifyRescPath"){
			form.action="/Tsme/security/rescPath/updateRescPath/" + anchor;
		} else {
			form.action="/Tsme/security/rescPath/getRescPath/" + anchor;
		}
		
		form.submit();
	}
}

//提交修改请求
function modifyRescPath(currentRescPathId){
	var anchor = getAnchor();
	location.href='/Tsme/security/rescPath/modifyRescPath/' + currentRescPathId + '/' + anchor;
}

//修改资源信息
$(document).ready(function(){
	//锚点
	var anchor = document.getElementsByName("anchorPoint")[0].value;
	if(anchor != ""){
		window.scrollTo(0, anchor);
	}
	var token = document.getElementsByName("token")[0].value;
	if(token == "ModifyRescPath"){
		initializeRescPathformStyle();
		$('#rescPathModel').modal('show');
	}
});

//删除资源信息
function deleteRescPath(currentRescPathId){
	var anchor = getAnchor();
	location.href='/Tsme/security/rescPath/deleteRescPath/' + currentRescPathId + '/' + anchor;
}

//添加控制属性
function addControlProperty(currentRescPathId){
	$('#controlPropertyModel').modal('show');
	document.getElementsByName("currentRescPathId")[0].value = currentRescPathId;
}

function controlPropertyPresubmit(){
	var anchor = getAnchor();
	var form = document.forms["controlPropertyForm"];
	
	var token = document.getElementsByName("token")[0].value;
	if(token == "ModifyControlProperty"){
		var controlPropertyId = document.getElementsByName("controlPropertyId")[0].value;
		form.action="/Tsme/security/rescPath/updateControlProperty/" + controlPropertyId + '/' + anchor;
	} else {
		var currentRescPathId = document.getElementsByName("currentRescPathId")[0].value;
		form.action="/Tsme/security/rescPath/addControlProperty/" + currentRescPathId + '/' + anchor;
	}
	form.submit();
}

//修改控制属性
function modifyControlProperty(controlPropertyId){
	var anchor = getAnchor();
	location.href='/Tsme/security/rescPath/modifyControlProperty/' + controlPropertyId + '/' + anchor;
}

//修改控制属性
$(document).ready(function(){
	//锚点
	var anchor = document.getElementsByName("anchorPoint")[0].value;
	if(anchor != ""){
		window.scrollTo(0, anchor);
	}
	var token = document.getElementsByName("token")[0].value;
	if(token == "ModifyControlProperty"){
		$('#controlPropertyModel').modal('show');
	}
});

//删除控制属性
function deleteControlProperty(controlPropertyId){
	var anchor = getAnchor();
	location.href='/Tsme/security/rescPath/deleteControlProperty/' + controlPropertyId + '/' + anchor;
}

//锚点
$(document).ready(function(){
	var height = document.getElementsByName("anchorPoint")[0].value;
	if(height != ""){
		window.setTimeout(window.scrollTo(0, height),50);
	}
});

</script>
</head>

<body>
<!-- 页面隐藏变量 -->
<div style="display:none;">
	<input type="hidden" name="token" value="${token}"/>
	<input type="hidden" name="anchorPoint" value="${anchor}"/>
</div>
<!-- 页面显示部分 -->
<div id="adaptive" style="min-height:500px;">
	<div style="width:79%;float:left;margin-left:20px;">
		<div style="width:870px;" align="left">		
			<div id="box" align="center">系统资源管理</div>
			<div align="right">
				<br><input type="button" value="添加资源" onclick="fillRescPath()">
			</div>
			<br>		
			<c:forEach items="${rescPathList}" var="rescPath" varStatus="status1">
				<div style="border: 1px solid #aed0ea;">
					<div style="border-bottom: 1px solid #aed0ea;">
						<div style="float:left;">		
							<div style="padding:6px;color:#0066dc;font-weight:bold;">
								<label style="color:#000000;">URL：</label>${rescPath.uri}
							</div>
							<div style="padding:6px;color:#0066dc;font-weight:bold;">
								<label style="color:#000000;">作用：</label>${rescPath.function}
							</div>
							<div style="clear:both;"></div>
						</div>	
						<div style="float:right;padding:16px 0px;">
							<input type="button" value="添加控制属性" onclick="addControlProperty('${rescPath.id}','${rescPath.uri}')">&nbsp;
							<input type="button" value="修改" onclick="modifyRescPath('${rescPath.id}')">&nbsp;
							<input type="button" value="删除" onclick="deleteRescPath('${rescPath.id}')">&nbsp;
						</div>
						<div style="clear:both;"></div>
					</div>	
					<div style="">
						<div align="center" style="">
							<div style="float:left;width:190px;border-right: 1px solid #aed0ea;padding:4px 0px;font-weight:bold;">角色</div>
							<div style="float:left;width:190px;border-right: 1px solid #aed0ea;padding:4px 0px;font-weight:bold;">属性A</div>
							<div style="float:left;width:190px;border-right: 1px solid #aed0ea;padding:4px 0px;font-weight:bold;">属性B</div>
							<div style="float:left;width:190px;border-right: 1px solid #aed0ea;padding:4px 0px;font-weight:bold;">属性C</div>
							<div style="float:left;width:104px;padding:4px 0px;font-weight:bold;">操作</div>
							<div style="clear:both;"></div>
						</div>
	
						<c:forEach items="${rescPath.controlPropertyList}" var="controlProperty" varStatus="status2">							
							<div style="border-top: 1px solid #aed0ea;">
								<div style="float:left;font-size:12px;width:190px;border-right: 1px solid #aed0ea;padding:6px;">${controlProperty.role_code}</div>
								<div style="float:left;font-size:12px;width:190px;border-right: 1px solid #aed0ea;padding:6px;">&nbsp;</div><!--属性A的值  -->
								<div style="float:left;font-size:12px;width:190px;border-right: 1px solid #aed0ea;padding:6px;">&nbsp;</div><!--属性B的值  -->
								<div style="float:left;font-size:12px;width:190px;border-right: 1px solid #aed0ea;padding:6px;">&nbsp;</div><!--属性C的值  -->
								<div align="center" style="padding-top:2px;">
									<input type="button" value="修改" style="height:24px;line-height:18px;" onclick="modifyControlProperty('${controlProperty.id}')">&nbsp;
									<input type="button" value="删除" style="height:24px;line-height:18px;" onclick="deleteControlProperty('${controlProperty.id}')">
								</div>
								<div style="clear:both;"></div>
							</div>
						</c:forEach>
					</div>
					<div style="clear:both;"></div>
				</div>
			</c:forEach>				
			<div align="right">
				<br><input type="button" value="添加资源" onclick="fillRescPath()">
			</div>
			<br>
		</div>
	</div>
</div>

<!-- rescPath添加页面 -->
<div class="modal fade in" style="top:100px;" id="rescPathModel" tabindex="-1" role="dialog" aria-labelledby="rescPathModelLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				</button>
				<h4 class="modal-title" id="rescPathModelLabel">填写资源信息</h4>
			</div>
			<div class="modal-body">
				<form:form modelAttribute="recRescPath" id="rescPathForm">
					<input type="hidden" name="rescPathId" value="${rescPath.id}"/>
					<div style="margin-left:80px;">
						资源地址：<input type="text" name="uri" id="rescPathUrl" size="30"  value="${rescPath.uri}"/>
						<span class="rescPathUrlError" style="font-size:12px;color:#FF0000;">资源地址不能为空！</span>
					</div>		
					<br>
					<div style="margin-left:80px;">
						资源功能：<input type="text" name="function" id="rescPathFunction" size="30" value="${rescPath.function}"/>
						<span class="rescPathFunctionError" style="font-size:12px;color:#FF0000;">资源功能不能为空！</span>
					</div>
				</form:form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="rescPathPresubmit();">提交</button>
			</div>
		</div>
	</div>
</div>
<!-- rescPath添加页面 结束 -->

<!-- controlProperty添加页面 -->
<div class="modal fade in" style="top:100px;" id="controlPropertyModel" tabindex="-1" role="dialog" aria-labelledby="controlPropertyModelLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				</button>
				<h4 class="modal-title" id="controlPropertyModelLabel">添加控制属性信息</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" name="currentRescPathId">
				<form:form modelAttribute="recRescPath" id="controlPropertyForm">
					<input type="hidden" name="controlPropertyId" value="${controlPropertyId}">
					<div style="font-size:12px;font-weight:bold;">角色选项</div>
					
					<c:forEach items="${roleList}" var="role" varStatus="status">
						<div style="float:left;width:30%;font-size:12px;padding:5px 10px;">
							<c:if test="${role.code == controlPropertyRole}">
								<input type="radio" name="role" value="${role.code}" checked>${role.code}
							</c:if>
							<c:if test="${role.code != controlPropertyRole}">
								<c:if test="${status.index == 0}">			
									<input type="radio" name="role" value="${role.code}" checked>${role.code}			
								</c:if>
								<c:if test="${status.index != 0}">
									<input type="radio" name="role" value="${role.code}" >${role.code}
								</c:if>	
							</c:if>
						</div>										
					</c:forEach>
					<div style="clear:both;"></div>
				</form:form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="controlPropertyPresubmit();">提交</button>
			</div>
		</div>
	</div>
</div>
<!-- controlProperty添加页面 结束 -->
</body>
</html>