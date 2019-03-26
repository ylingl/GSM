<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page import="utils.AccountTools" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设备地图展示</title>
<link rel="stylesheet" href="/Tsme/css/map/map.css" />
<link rel="stylesheet" href="/Tsme/css/public.css" />
<link rel="stylesheet" href="/Tsme/css/topBar.css" type="text/css"/>

<script type="text/javascript" src="/Tsme/js/public/js_patch.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery.include.js"></script>

<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>

<style type="text/css">
.esriPopup .titlePane {
	padding-left: 0px;
	height: 30px;
	line-height: 30px;
}

.esriPopup .titleButton.maximize {
	right: 30px;
	width: 11px;
	height: 11px;
	top: 9px;
}

.esriPopup .titleButton.close {
	right: 8px;
	width: 12px;
	height: 17px;
	top: 6px;
}

#loading_div {
	width: 320px;
	height: 32px;
	background: url(/Tsme/images/dialog/ajax_loading.gif) 0px 0px no-repeat
		rgba(255, 255, 255, 0);
	position: absolute;
	z-index: 999;
	display: none;
	top: 50%;
	right: 50%;
}

</style>

<script type="text/javascript">
	//禁止修改这个变量的名称 HOSTNAME_AND_PATH_TO_JSAPI ,api js 中也用到这个变量了
	var arcgisMapUrl = "http://121.42.251.175:6080/arcgis/rest/services/china/MapServer";
	var HOSTNAME_AND_PATH_TO_JSAPI = "121.42.251.175:8081/arcgis_js_api/library/3.9/3.9/";
	$.includePath = "http://" + HOSTNAME_AND_PATH_TO_JSAPI;
	$.include([ 'js/dojo/dijit/themes/tundra/tundra.css', 'js/esri/css/esri.css', 'init.js' ]);
</script>

<script type="text/javascript">
var flag = false;

var accountHomepage = true;

//判断是否登录
function checkIsLogin() {
	<% 
	
	AccountTools accountTools= new AccountTools();
	boolean isLogin = accountTools.isAnyAccountInLoggedState();
		
	%>
	
	var flag = <%=isLogin%> ;
	return flag;
}

function getAccountName() {
	<%
	
	String accountName = "";
	if(accountTools.isAnyAccountInLoggedState()){
		accountName = accountTools.getCurrentAccountName();
	}
		
	%>
	
	var name = <%="\"" + accountName + "\""%>;
	return name;
}

function getAccountRole() {
	<%
	
	String accountRole = "角色:";
	if(accountTools.doesCurrentAccountHasSuperadminRole()){
		accountRole = accountRole + " 超级管理员";
	}
	if(accountTools.doesCurrentAccountHasAdminRole()){
		accountRole = accountRole + " 管理员";
	}
	if(accountTools.doesCurrentAccountHasTrainerRole()){
		accountRole = accountRole + " 训练员";
	}
	if(accountTools.doesCurrentAccountHasTesterRole()){
		accountRole = accountRole + " 监测员";
	}
		
	%>
	
	var role = <%="\"" + accountRole + "\""%>;
	return role;
}

function getAccountRoleCode() {
	<%
	
	String role_code = "";
	if(accountTools.doesCurrentAccountHasSuperadminRole()){
		role_code = "super";
	}
	if(accountTools.doesCurrentAccountHasAdminRole()){
		role_code = "admin";
	}
	if(accountTools.doesCurrentAccountHasTrainerRole()){
		role_code = "trainer";
	}
	if(accountTools.doesCurrentAccountHasTesterRole()){
		role_code = "tester";
	}
		
	%>
	
	var role = <%="\"" + role_code + "\""%>;
	return role;
}

function userLoginFlag(){
	flag = checkIsLogin();
	if(flag){
		$(".rightIcon").show();
		$("#divAccount").addClass("loginFlag");
		$("#name").html(getAccountName());
		$("#role").html(getAccountRole());
	}
	
	$.ajax({
		url: "/Tsme/account/canThisAccountApplyForTrainer",
		type: "POST",
		dataType: 'text',
		success: function(data){
			if(data == "show"){
				$("#applyForTrainerBotton").show();
				$("#applyForTrainerBotton").removeAttr("disabled");
			} else if(data == "disable" && getAccountRoleCode() == "tester"){
				$("#applyForTrainerBotton").show();
				$("#applyForTrainerBotton").attr("disabled","disabled");
				$("#applyForTrainerBotton").val("待核准为训练员");
			}
		}
	})
}

function navHover(){
	$(".rightIcon").hover(function(){
		if(flag == true){
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
	
	$(".logout").on("click",function(){
		accountHomepage = false;
	});
	
	$(".rightIcon").on("click",function(){
		$(".logout").hide();
		$(".triggle").hide();
		$("#divAccount").addClass("on");
		if(accountHomepage){
			top.location.href="/Tsme/account/accountHomepage";
		}
	});

	getOnlineDeviceList();
	if(deviceTimer == null) {
		setTimeout(function(){
			deviceTimer = setInterval(function() {
				getOnlineDeviceList();
			}, 10000);
		}, 10000);
	}
}

function getWarningTemplateList(deviceNum){
	layer.msg('数据装载中，请稍候...', {icon: 1});
	$("#templateId").empty();
	$.ajax({
		url: "/Tsme/spectra/getWarningTemplateList",
		type: "POST",
		data: {deviceNum: deviceNum},
		dataType: 'json',
		success: function(data) {
			if(data.offline == true){
				layer.msg('设备已下线，不能查看频谱！', {icon: 1});
			} else {
				if(data.employ == true) {
					$("#deviceNum").val(deviceNum);
					$("#selectTemplateForm").submit();
				} else {
					if(data.warningTemplateList != null) {
						var warningTemplateList = data.warningTemplateList
						for(var i = 0; i < warningTemplateList.length; i++){
							$("#templateId").append("<option value='" + warningTemplateList[i].id +"'>" + warningTemplateList[i].template_name + "</option>");
						}
						$("#deviceNum").val(deviceNum);
						
						var obj = $("#" + deviceNum);
						
						$("#selectTemplateDialog").modal('show');
						/* obj.removeAttr("onclick");
						obj.attr("data-toggle", "modal");
						obj.attr("data-target", "#selectTemplateDialog");
						obj.click();
						
						obj.attr("onclick", "getWarningTemplateList("+ deviceNum + ")");
						obj.removeAttr("data-toggle");
						obj.removeAttr("data-target");
						data-toggle='modal' data-target='#selectTemplateDialog' */
					} else {
						layer.msg('设备已被占用，不能查看频谱！', {icon: 1});
					}
				}
			}
		}
	})
}
	
function getDeviceStatus(deviceNum){
	$.ajax({
		url : "/Tsme/history/getDeviceStatus",
		type : "POST",
		data : {'deviceNum':deviceNum},
		dataType : 'json',
		success : function(data) {
			if(data.employ == true) {
				if(data.myself == true){
					layer.confirm("设备已被占用，点击“确定”按钮将释放该设备，并跳转至历史记录展示！", function(){echo(deviceNum);});
				} else {
					layer.msg('设备已占用，您目前没用操作权限...', {icon: 1});
				}
				/*$("#deviceNum").val(deviceNum);
				$("#selectTemplateForm").submit();*/
			} else {
				window.location.href="/Tsme/history/showData/" + deviceNum;
			}
		}
	})
}
	
function beginMonitor(){
	var  temp = $("#templateId").val();
	if($("#templateId").val() != null){
		$("#selectTemplateForm").submit();
	} else {
		$("#selectTemplateDialog").modal('hide');
	}
}
	
function showCoordinates(evt){//显示地图坐标
	var mp = evt.mapPoint;
	dojo.byId("info").innerHTML = "东经：" + parseFloat(mp.x).toFixed(7) + " , 北纬：" + parseFloat(mp.y).toFixed(7);//+"  鼠标坐标：" + evt.screenPoint.x + " , " + evt.screenPoint.y;
	$("#info").css("margin-left","70px");
	//$("#info").css("index","9999");
	//$("#info").css("top",evt.screenPoint.y);
	//$("#info").css("left",evt.screenPoint.x);
}

dojo.require("esri.map");//类似引入命名空间

var graphicGroup = new Array();
var count = 0;
var updateTimer = null;
var deviceTimer = null;

function init() {
	userLoginFlag();//用于加载帐户状态
	navHover();//用于初始化右边栏样式
	
	var myMap = new esri.Map("mapDiv", {center: [116.404, 39.915]});
	var initialExtent = new esri.geometry.Extent({ "xmin": 115.33, "ymin": 39.40, "xmax": 117.40, "ymax": 40.42, "spatialReference": { "wkid": 4326} });
	myMap.setExtent(initialExtent); 
	 /*
    var point = new esri.geometry.Point(116.404, 39.915, myMap.spatialReference);// 创建点坐标   
	myMap.centerAndZoom(point, 15);  */
	var layer = new esri.layers.ArcGISDynamicMapServiceLayer(arcgisMapUrl);
	myMap.addLayer(layer);
	//创建图层
	var graphicLayer = new esri.layers.GraphicsLayer();
	//把图层添加到地图上 x
	myMap.addLayer(graphicLayer);
       
	//图标定义
	var symbolBS = new Array();
	symbolBS[0] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/graytower.png",32,32);
	symbolBS[1] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/greentower.png",32,32); 
	symbolBS[2] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/redtower.png",32,32);
	
	var symbolDS = new Array();
	symbolDS[0] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/grayequipment.png",22,22); 
	symbolDS[1] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/greenequipment.png",22,22); 
	symbolDS[2] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/redequipment.png",22,22); 
	
	//数据
	var pt = new Array();
	//要在模版中显示的参数
	var ptAttr = new Array();
	<c:forEach items="${geoMsgs}" var="geoMsg" varStatus="status">
		pt["${status.index}"] = new esri.geometry.Point("${geoMsg.LNG}","${geoMsg.LAT}", myMap.spatialReference);
		ptAttr["${status.index}"] = {"device_num":"${geoMsg.device_num}", "name":"${geoMsg.name}", "BSIC":"${geoMsg.BSIC}", "model":"${geoMsg.model}", "ip":"${geoMsg.ip}", "device_type":"${geoMsg.device_type}", "point_type":"${geoMsg.point_type}", "km_stone":"${geoMsg.km_stone}", "state":"${geoMsg.state}"};
	</c:forEach>
	
	var attr;
	for(var i = 0; i < "${fn:length(geoMsgs)}"; i++) {
		attr = {"device_num":ptAttr[i].device_num, 
				"name":ptAttr[i].name,
				"BSIC":ptAttr[i].BSIC, 
				"model":ptAttr[i].model, 
				"Xcoord":pt[i].x, 
				"Ycoord":pt[i].y, 
				"ip": ptAttr[i].ip, 
				"device_type":ptAttr[i].device_type, 
				"point_type":ptAttr[i].point_type, 
				"km_stone":ptAttr[i].km_stone,
				"state":ptAttr[i].state
				};
		
		//创建模版 
		var infoTemplate;
		var symbolImg;
	      	 
		if(attr["point_type"]=="BS" ){
			symbolImg = symbolBS[i%3];
			/* if(attr["state"] == "offline"){
				symbolImg = symbolBS[0];
			} else if (attr["state"] == "normal"){
				symbolImg = symbolBS[1];
			} */
			
			infoTemplate = new esri.InfoTemplate("基站信息", 
							"基站名称：" + attr["name"] + "<br/>" + 
							"基站识别码：" + attr["BSIC"] + "<br/>" + 
							"基站类型：" + attr["model"] +
							"<div><p class='information'>经度：" + parseFloat(attr["Xcoord"]).toFixed(7) + "&#176<br/>纬度：" + parseFloat(attr["Ycoord"]).toFixed(7) +
							"&#176<br/>公里标：" + attr["km_stone"] + "</p></div>" +
							"<p class='information'><a href='/Tsme/spectra/spectraIndex' target='_self'>查看历史信息</a>" +
							"<a href='/Tsme/spectra/spectraIndex' target='_self'>查看基站属性</a></p></div>");
		    	 
		} else {
			if(attr["state"] == "offline"){
				symbolImg = symbolDS[0];
			} else if (attr["state"] == "normal"){
				symbolImg = symbolDS[1];
			}
			
			infoTemplate = new esri.InfoTemplate(attr["device_num"], 
							"设备编号：" + attr["device_num"] + "<br/>" +
							"设备类型：" + attr["device_type"] + 
							"<div><p class='information'>经度：" + parseFloat(attr["Xcoord"]).toFixed(7) + "&#176<br/>纬度：" + parseFloat(attr["Ycoord"]).toFixed(7) + "&#176</p>" +
							"<p class='information'>IP地址：" + attr["ip"] +"</p></div>"+
							"<div><p class='information'><a href='/Tsme/data/warningTemplate/" + attr["device_num"] + "' target='_self'>训练预警模型</a>"+
							"<a href='javascript:void(0)' id='" + attr["device_num"] + "' onclick='getWarningTemplateList(" + attr["device_num"] + ")'>查看实时频谱</a></p>" +
							"<p class='information'><a href='javascript:void(0)' onclick='getDeviceStatus(" + attr["device_num"] + ")'>查看告警历史</p></a></div>");
		}
	      	
		var graphic = new esri.Graphic(pt[i], symbolImg, attr, infoTemplate);
		graphicLayer.add(graphic);
		
		if(attr["point_type"] == "DS"){
			graphicGroup[count] = graphic;
			count ++;
		}
	}
	dojo.connect(myMap, "onClick", function(event){
		/*  var pt = new esri.geometry.Point(event.mapPoint.x,event.mapPoint.y,myMap.spatialReference);
		//1.画点
		//var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([255,0,0,0.5]));
		//2.自定义打点图片
		var sms = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/greenmark.png",32,32);
		var attr = {"Xcoord":event.mapPoint.x,"Ycoord":event.mapPoint.y,"Description":"This is a map point!"};
		var infoTemplate = new esri.InfoTemplate("这是主要信息","X坐标: ${Xcoord} <br/>Y坐标: ${Ycoord} <br/>Description:${Description}<div><a href='/Tsme/spectra/spectraIndex' target='_blank'>查看详情</a> </div>");
		var graphic = new esri.Graphic(pt,sms,attr,infoTemplate); 
		myMap.graphics.add(graphic);  */
		console.log("屏幕坐标："+event.screenPoint.x + ","+event.screenPoint.y+" 地图坐标："+event.mapPoint.x+","+event.mapPoint.y);
	});
	
	dojo.connect(myMap, "onMouseMove", showCoordinates);
	dojo.connect(myMap, "onMouseDrag", showCoordinates);
	
	if(updateTimer == null) {
		setTimeout(function(){
			updateTimer = setInterval(function() {
				getDeviceGPS(myMap, graphicLayer);
			}, 10000);
		}, 10000);
	}
	count = 0;
}

dojo.addOnLoad(init);

function getDeviceGPS(myMap, graphicLayer){
	$.ajax({
		url : "/Tsme/map/getDeviceGPS",
		type : "POST",
		async : false,
		dataType : 'json',
		success : function(data) {
			if (data != null && data.length > 0) {
				for(var i = 0; i < data.length; i ++){
					var temp = data[i];
					var symbol;
					if(temp.state == "offline"){
						symbol = esri.symbol.PictureMarkerSymbol("/Tsme/images/map/grayequipment.png",22,22);
					} else if (temp.state == "normal"){
						symbol = esri.symbol.PictureMarkerSymbol("/Tsme/images/map/greenequipment.png",22,22);
					}
					var find = false;
					for(var j = 0; j < graphicGroup.length; j ++){
						var graphicTemp = graphicGroup[j];
						if(temp.device_num == graphicTemp.getInfoTemplate().title) {
							var pt = new Array();
							pt[0] = new esri.geometry.Point(temp.lng, temp.lat, myMap.spatialReference);
							
							graphicTemp.setGeometry(pt[0]);
							
							graphicTemp.attr("Xcoord", pt[0].x);
							graphicTemp.attr("Ycoord", pt[0].y);
							
							graphicTemp.setSymbol(symbol);
							
							var newInfoTemplate = new esri.InfoTemplate(temp.device_num, 
									"设备编号：" + temp.device_num + "<br/>" +
									"设备类型：" + temp.device_type + 
									"<div><p class='information'>经度：" + temp.lng.toFixed(7) + "&#176<br/>纬度：" + temp.lat.toFixed(7) + "&#176</p>" +
									"<p class='information'>IP地址：" + temp.ip +"</p></div>"+
									"<div><p class='information'><a href='/Tsme/data/warningTemplate/" + temp.device_num + "' target='_self'>训练预警模型</a>"+
									"<a href='javascript:void(0)' id='" + temp.device_num + "' onclick='getWarningTemplateList(" + temp.device_num + ")'>查看实时频谱</a></p>" +
									"<p class='information'><a href='javascript:void(0)' onclick='getDeviceStatus(" + temp.device_num + ")'>查看告警历史</p></a></div>");
							
							graphicTemp.setInfoTemplate(newInfoTemplate);
							
							graphicTemp.draw();
							
							find = true;
							break;
						}
					}
					
					if(!find){
						var pt = new Array();
						pt[0] = new esri.geometry.Point(temp.lng, temp.lat, myMap.spatialReference);
						
						attr = {"device_num":temp.device_num, 
								"BSIC":temp.BSIC, 
								"model":temp.model, 
								"Xcoord":pt[0].x, 
								"Ycoord":pt[0].y, 
								"ip": temp.ip, 
								"device_type":temp.device_type, 
								"point_type":temp.point_type, 
								"km_stone":temp.km_stone
								};
						
						var newInfoTemplate = new esri.InfoTemplate(temp.device_num, 
								"设备编号：" + temp.device_num + "<br/>" +
								"设备类型：" + temp.device_type + 
								"<div><p class='information'>经度：" + temp.lng.toFixed(7) + "&#176<br/>纬度：" + temp.lat.toFixed(7) + "&#176</p>" +
								"<p class='information'>IP地址：" + temp.ip +"</p></div>"+
								"<div><p class='information'><a href='/Tsme/data/warningTemplate/" + temp.device_num + "' target='_self'>训练预警模型</a>"+
								"<a href='javascript:void(0)' id='" + temp.device_num + "' onclick='getWarningTemplateList(" + temp.device_num + ")'>查看实时频谱</a></p>" +
								"<p class='information'><a href='javascript:void(0)' onclick='getDeviceStatus(" + temp.device_num + ")'>查看告警历史</p></a></div>");
						
						var graphic = new esri.Graphic(pt[0], symbol, attr, newInfoTemplate);
						graphicLayer.add(graphic);
						
						graphicGroup.push(graphic);
					}
				}
			}
		}
	})
}

function applyForTrainer(){
	$("#realName").val("");
	$("#phoneNum").val("");
	$('#applyForTrainer').modal('show');
}

function applyForTrainerSubmit(){
	layer.msg('申请提交中，请等待...', {icon: 1});
	$.ajax({
		url : "/Tsme/account/applyForTrainer",
		type : "POST",
		async : false,
		data : $("#applyForTrainerForm").serialize(),
		dataType : 'json',
		success : function(data) {
			$('#applyForTrainer').modal('hide');
			layer.msg('申请提交成功！', {icon: 1});
			if(!data){
				layer.msg('申请失败，请重新提交！', {icon: 1});	
			} else {
				$("#applyForTrainerBotton").attr("disabled","disabled");
				$("#applyForTrainerBotton").val("待核准为训练员");
			}
		}
	})
}

function getOnlineDeviceList(){
	$.ajax({
		url : "/Tsme/map/getOnlineDeviceList",
		type : "POST",
		async : false,
		dataType : 'json',
		success : function(data) {
			if(data != null){
				for(var i in data){
					var icon = "";
					var butt = "";
					var html = "";
					var status = "";
					if(data[i].device_status == "training"){
						butt = " 训练 <input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"training(" + data[i].device_num + ")\" value=\"查看\"/> " + 
								"<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"stopTraining(" + data[i].device_num + ")\" value=\"释放\"/>" +
								"<input type=\"hidden\" id=\"h + data[i].device_num + \" value=\"training\"/>";
						status = "training";
					} else if(data[i].device_status == "warning"){
						butt = " 监测 <input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"getWarningTemplateList(" + data[i].device_num + ")\" value=\"查看\"/> " + 
							"<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"stopWarning(" + data[i].device_num + ")\" value=\"释放\"/>" + 
							"<input type=\"hidden\" id=\"h + data[i].device_num + \" value=\"warning\"/>";
						status = "warning";
					} else if(data[i].device_status == "free"){
						butt = " 空闲 <input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"training(" + data[i].device_num + ")\" value=\"训练\"/> " + 
							"<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"getWarningTemplateList(" + data[i].device_num + ")\" value=\"监测\"/>" + 
							"<input type=\"hidden\" id=\"h + data[i].device_num + \" value=\"free\"/>";
						status = "free";
					}
					
					if(data[i].warning && status != "free"){
						icon = "<img id=\"i" + data[i].device_num + "\" style=\"width:16px; height:16px; padding-bottom:2px;\" src=\"\/Tsme\/images\/map\/redequipment.png\">"
						html = "<div id=\"f" + data[i].device_num + "\" style=\"padding-left:6px; height:18px; line-height:18px; color:red; font-size:12px; font-family:Microsoft YaHei;\">" + icon + data[i].device_num + butt + "</div>" 
					} else {
						icon = "<img id=\"i" + data[i].device_num + "\" style=\"width:16px; height:16px; padding-bottom:2px;\" src=\"\/Tsme\/images\/map\/greenequipment.png\">" 
						html = "<div id=\"f" + data[i].device_num + "\" style=\"padding-left:6px; height:18px; line-height:18px; color:green; font-size:12px; font-family:Microsoft YaHei;\">" + icon + data[i].device_num + butt  + "</div>" 
					}
					
					if($("#f" + data[i].device_num).length && $("#f" + data[i].device_num).length > 0) {
						var temp = $("#h" + data[i].device_num).val();
						if(temp != status) {
							$("#f" + data[i].device_num).replaceWith(html);
						}
					} else {
						$("#onlineDeviceList").append(html);
					}
				}
			}
		}
	})
}

function training(deviceNum){
	location.href = "/Tsme/data/warningTemplate/" + deviceNum;
}

function stopTraining(deviceNum){
	$.ajax({
		url : "/Tsme/data/stopRecord",
		data : {deviceNum:deviceNum},
		type : "POST",
		dataType : 'json',
		success : function(data) {
			if(data != null){
				if(data.result == true){
					layer.msg('已停止,请稍候...', {icon: 1});
					getOnlineDeviceList();
				} else {
					layer.msg('无法停止', {icon: 1});
				}
			}
		}
	})
}

function stopWarning(deviceNum){
	$.ajax({
		url : "/Tsme/spectra/stopRealTimeMonitor",
		type : "POST",
		data : {deviceNum:deviceNum},
		dataType : 'json',
		success : function(data) {
			if (data.result) {
				layer.msg('已停止,请稍候...', {icon: 1});
				getOnlineDeviceList();
			} else {
				layer.msg('无法停止', {icon: 1});
			}
		}
	})
}

</script>
</head>
<body class="tundra" style="width: 100%; height:100%;">
	<div class="title" dojotype="dijit.layout.BorderContainer" design="headline" gutters="true">
		<div dojotype="dijit.layout.ContentPane" region="top" id="navtable" style="text-align:center;">
			<span style="font-size:24px;">GSM-R网络无线干扰监测管理系统</span>
			<span id="info"></span>
		</div>
	</div>
	
	<div id="mapDiv" class="map" dojotype="dijit.layout.ContentPane" region="center">
	</div>
	
	<div class="warnDiv">
		<div class="rightIcon" title="单击进入个人中心">
			<div id="divAccount"></div>
			<div class="triggle"></div>
			<a class="logout" href="/Tsme/security/logout/clearAccountData">退出</a>
		</div>
		<div class="divInfo">
			<div id="name"></div>
			<div id="role"></div>
			<input type="button" id="applyForTrainerBotton" style="display:none; height:20px; line-height:10px; width:100px; padding:0px;" onclick="applyForTrainer()" value="申请成为训练员" />
		</div>
		<div id="onlineDeviceList" style="clear:both">
			<div style="padding-left:6px; font-family:Microsoft YaHei; color:#0080c0">在线设备列表：</div>
		</div>
	</div>
	
	<div id="loading_div">加载中....</div>
	
<!-- 修改弹出部分 -->
	<div class="modal fade in" style="top:100px;" id="selectTemplateDialog" tabindex="-1" role="dialog" aria-labelledby="editmodelLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="editmodelLabel">请选择设备模板</h4>
				</div>
				<div class="modal-body">
					<form id="selectTemplateForm" action="/Tsme/spectra/chart">
					    <input type="hidden" name="deviceNum" id="deviceNum"/>
					    
						<div class="form-group">
							<select name="templateId" id="templateId"></select> 
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" onclick="beginMonitor()">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade in" style="top:100px;" id="applyForTrainer" tabindex="-1" role="dialog" aria-labelledby="applyForTrainerLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="applyForTrainerLabel">申请成为训练员</h4>
				</div>
				<div class="modal-body">
					<form id="applyForTrainerForm" action="/Tsme/account/applyForTrainer">
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">真实姓名：</label>
							<input type="text" name="realName" id="realName"/>
						</div>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">联系电话：</label>
							<input type="text" name="phoneNum" id="phoneNum"/>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" onclick="applyForTrainerSubmit()">确定</button>
				</div>
			</div>
		</div>
	</div>
<!-- 修改弹出部分结束 -->

</body>
</html>
