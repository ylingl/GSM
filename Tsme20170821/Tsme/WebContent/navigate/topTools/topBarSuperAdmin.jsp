<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="utils.AccountTools" %>

<link rel="stylesheet" href="/Tsme/css/topBar.css" type="text/css" />
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" >

$(function(){
	userLoginFlag();
});

var flag = false;

function userLoginFlag(){
	navHover();
	flag = checkIsLogin();
	if(flag){
		$(".rightIcon").show();
		$(".loginbox").hide();
		$(".s").show();
		$("#divAccount").addClass("loginFlag");
	} 
}

//判断是否登录
function checkIsLogin() {
	<% 
	
	AccountTools accountTools= new AccountTools();
	boolean isLogin = accountTools.isAnyAccountInLoggedState();
	
	%>
	
	var flag = <%=isLogin%>;
	return flag;
}

function navHover(){
	$(".top_bar_left_index").hover(function(){
		$(this).addClass("hv");
	},function(){
		$(this).removeClass("hv");
	});
		
	$(".top_bar_left_index").on("click",function(){
		$(this).addClass("on");
		top.location.href="/Tsme";
	});
	 
	$(".rightIcon").hover(function(){
		if(flag==true){
			$(".logout").show();
			$(".triggle").show();
		}
	},function(){
		$(".logout").hide();
		$(".triggle").hide();
	});
	$("#divAccount").hover(function(){
		$(this).addClass("hv");
	},function(){
		$(this).removeClass("hv");
	});
	$("#divAccount").on("click",function(){
		$(".logout").hide();
		$(".triggle").hide();
		$(this).addClass("on");
		top.location.href="/Tsme/account/accountHomepage";
	});
}

</script>

<div class="top_bar_wrap">
	<div id="divBar" class="top_bar fix2">
		<div class="top_bar_left">
			<div class="top_bar_left_index" style="width:40px;margin:15px 0 0 50px;font-size:12px;color:#003399;font-weight:bold;">主页</div>
			<div class="iconCategoryBar fix">
			</div>
		</div>
		<div class="top_bar_right" style="width:90px;">
			<div class="rightIcon">
				<div id="divAccount" title="个人中心"></div>
				<div class="triggle"></div>
				<a class="logout" href="/Tsme/security/logout/clearAccountData">退出</a>
			</div>
		</div>
	</div>
</div>
