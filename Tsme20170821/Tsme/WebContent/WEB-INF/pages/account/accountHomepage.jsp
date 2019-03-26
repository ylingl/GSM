<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>个人中心</title>

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>

<link rel="stylesheet" href="/Tsme/css/public.css" type="text/css"/>
<link rel="stylesheet" href="/Tsme/css/topBar.css" type="text/css"/>

<style type="text/css">
/**
 *设置整体框架的尺寸
 *20141226
 *薛颖
*/
.adaptive{font-family:"微软雅黑",Microsoft YaHei; min-width:1100px; _width:expression((document.documentElement.clientWidth||document.body.clientWidth)<1100?"1100px":"auto"));}
.leftBox{width:152px;/*height:650px;*/float:left;background:#f6f8fa;padding-bottom:10px;padding-top:10px;}
.rightBox{width:880px;/*height:650px;*/float:left;}
.top{width:100%; height:44px;}
.downPage{width:100%; height:500px;}
.InfoIntro{width:1100px; height:500px;}
</style> 
<script type="text/javascript" >
if(window != top){
	top.location.href = location.href;
}

// iframe 自适应高度  2015-8-04 luodongshuang
function iFrameAutoHeight(){
	var ifm = document.getElementById("rightAreaIframe");
		var subWeb = document.frames ? document.frames["rightAreaIframe"].document : ifm.contentDocument;
		// 判断浏览器的类型
		var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;

		if(ifm != null && subWeb != null){

			if(isChrome){
				var dHeight = ifm.contentWindow.document.documentElement.scrollHeight;
				ifm.height = dHeight;

			}else{
				ifm.height = subWeb.body.scrollHeight;
			}
			var h = parseInt(ifm.height);
			$('#rightAreaIframe').height(h + 40);
		}
	}
</script>
</head>
<body>
	<!-- 页面隐藏变量 -->
	<input type="hidden" name="roleOrder" value="${roleOrder}"/>
	
	<div class="top adaptive" align="center">
		<div id="topBar" style=" padding:0 2px;"></div>
	</div>
	
	<div class="downPage adaptive">
		<div class="InfoIntro" style="margin:0 auto;">
			<c:if test="${roleOrder == 1}">
				<div id="testerLeftBar" class="leftBox"></div>
			</c:if>
			<c:if test="${roleOrder == 2}">
				<div id="trainerLeftBar" class="leftBox"></div>
			</c:if>
			<c:if test="${roleOrder == 3}">
				<div id="adminLeftBar" class="leftBox"></div>
			</c:if>
			<c:if test="${roleOrder == 4}">
				<div id="superAdminLeftBar" class="leftBox"></div>
			</c:if>
			<div id="rightArea" class="rightBox">
				<div>
					<iframe style="width:930px;min-height:500px;" onLoad="iFrameAutoHeight()" id="rightAreaIframe" frameborder="0" scrolling="no"></iframe>
				</div>
			</div>
			<div style="clear:both;"></div>
		</div>	
	</div>
</body>
<script type="text/javascript" src="/Tsme/js/public/getTopBar.js"></script>
<script type="text/javascript" src="/Tsme/js/public/getSuperAdminLeftBar.js"></script>
<script type="text/javascript" src="/Tsme/js/public/getAdminLeftBar.js"></script>
<script type="text/javascript" src="/Tsme/js/public/getTesterLeftBar.js"></script>
<script type="text/javascript" src="/Tsme/js/public/getTrainerLeftBar.js"></script>
</html>