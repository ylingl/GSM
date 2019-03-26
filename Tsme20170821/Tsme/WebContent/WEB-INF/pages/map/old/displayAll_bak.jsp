<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设备地图展示</title>
<link rel="stylesheet" href="/Tsme/css/map/map.css" />
<link rel="stylesheet" href="/Tsme/css/public.css" />
<script type="text/javascript"
	src="/Tsme/js/public/js_patch.js"></script>
<script type="text/javascript"
	src="http://video.suizhi.net:8659/arcgis_js_api/library/jquery-1.11.3.min.js"></script>
<script type="text/javascript"
	src="http://video.suizhi.net:8659/arcgis_js_api/library/jquery.include.js"></script>
<script type="text/javascript">
	//禁止修改这个变量的名称 HOSTNAME_AND_PATH_TO_JSAPI ,api js 中也用到这个变量了
	var HOSTNAME_AND_PATH_TO_JSAPI = "video.suizhi.net:8659/arcgis_js_api/library/3.9/3.9/";
	$.includePath = "http://" + HOSTNAME_AND_PATH_TO_JSAPI;
	$.include([ 'js/dojo/dijit/themes/tundra/tundra.css',
			'js/esri/css/esri.css', 'init.js' ]);
</script>
<style type="text/css">
.esriPopup .titlePane{
	padding-left:0px;
	height: 30px;
    line-height: 30px;
}
.title {
    height: 30px;
    text-align: center;
    line-height: 30px;
    font-size: 16px;
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
	var thisId; 
	$(function(){
		thisId = window.location.hash;
	    if(thisId != "" && thisId != undefined){
	    	thisId = thisId.substring(1);
	    }else{
			alert("缺少客户端Id，请检查请求地址是否正确！");
	    }
	})
	
	function beginMonitor(deviceId,deviceGPS,deviceAddress){
		$.ajax({
			url : "/Tsme/spectra/realTimeMonitor",
			type : "POST",
			data : {
				clientId: thisId, deviceId: deviceId, deviceGPS: deviceGPS, deviceAddress:deviceAddress 
			},
			dataType : 'json',
			success : function(data) {
				if (data) {
					$("#loading_div").show();
				}
			}
		})
	}
	
	function showCoordinates(evt)//显示地图坐标
	{
		var mp = evt.mapPoint;
		dojo.byId("info").innerHTML = "东经：" + parseFloat(mp.x).toFixed(2) + " , 北纬：" + parseFloat(mp.y).toFixed(2);//+"  鼠标坐标：" + evt.screenPoint.x + " , " + evt.screenPoint.y;
		$("#info").css("margin-left","70px");
		//$("#info").css("index","9999");
		//$("#info").css("top",evt.screenPoint.y);
		//$("#info").css("left",evt.screenPoint.x);
	}
	
	dojo.require("esri.map");//类似引入命名空间
	function init() {
		 
		var myMap = new esri.Map("mapDiv", {
			center : [ 116.404, 39.915]
		});
		 var initialExtent = new esri.geometry.Extent({ "xmin": 115.33, "ymin": 39.40, "xmax": 117.40, "ymax": 40.42, "spatialReference": { "wkid": 4326} });
		 myMap.setExtent(initialExtent); 
		 /*
	    var point = new esri.geometry.Point(116.404, 39.915, myMap.spatialReference);// 创建点坐标   
		myMap.centerAndZoom(point, 15);  */
		var layer = new esri.layers.ArcGISDynamicMapServiceLayer(
				"http://video.suizhi.net:8656/arcgis/rest/services/china/MapServer");
		myMap.addLayer(layer);
		//创建图层
        var graphicLayer = new esri.layers.GraphicsLayer();
        //把图层添加到地图上 x
        myMap.addLayer(graphicLayer);
        
        //数据
        var pt = new Array();
        pt[0] = new esri.geometry.Point(116.58,39.93, myMap.spatialReference);
        pt[1] =new esri.geometry.Point(115.48,39.46, myMap.spatialReference);
        pt[2] =new esri.geometry.Point(116.91,39.71, myMap.spatialReference);
        pt[3] =new esri.geometry.Point(116.32,39.82, myMap.spatialReference);
        pt[4] =new esri.geometry.Point(117.10,40.37, myMap.spatialReference);
        pt[5] =new esri.geometry.Point(115.50,40.18, myMap.spatialReference);
        pt[6] =new esri.geometry.Point(116.66,39.51, myMap.spatialReference);
        pt[7] =new esri.geometry.Point(115.90,39.68, myMap.spatialReference);
        pt[8] =new esri.geometry.Point(116.20,40.28, myMap.spatialReference);
        pt[9] =new esri.geometry.Point(115.50,39.88, myMap.spatialReference);
        
        //要在模版中显示的参数
        var ptAttr = new Array();
        ptAttr[0] = {"id": "P19887","ip":"192.168.85.21","addr":"北京市"};
        ptAttr[1] = {"id": "P19888","ip":"192.168.85.22","addr":"河北省保定市"};
        ptAttr[2] = {"id": "P19889","ip":"192.168.85.23","addr":"北京市"};
        ptAttr[3] = {"id": "P19891","ip":"192.168.85.24","addr":"北京市"};
        ptAttr[4] = {"id": "P19892","ip":"192.168.85.25","addr":"北京市密云县"};
        ptAttr[5] = {"id": "P19893","ip":"192.168.85.26","addr":"河北省张家口市"};
        ptAttr[6] = {"id": "P19894","ip":"192.168.85.27","addr":"河北省廊坊市"};
        ptAttr[7] = {"id": "P19895","ip":"192.168.85.28","addr":"北京市"};
        ptAttr[8] = {"id": "P19896","ip":"192.168.85.29","addr":"北京市昌平县"};
        ptAttr[9] = {"id": "P19897","ip":"192.168.85.30","addr":"北京市"};
        
        var symbol = new Array();
        symbol[0] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/greentower.png",32,32);
        symbol[1] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/redtower.png",32,32); 
        symbol[2] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/graytower.png",32,32);
        symbol[3] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/grayequipment.png",22,22); 
        symbol[4] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/greenequipment.png",22,22); 
        symbol[5] = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/redequipment.png",22,22); 
       
        for(var i=0; i<9; i++){
        	var attr = { "id": ptAttr[i].id,"Xcoord":pt[i].x,"Ycoord":pt[i].y,"ip": ptAttr[i].ip, "addr": ptAttr[i].addr};
        	var deviceGPS = "GPS "+pt[i].x+"E "+pt[i].y+"N";
        	var deviceAddress = "GPS "+pt[i].x+"E "+pt[i].y+"N";
        	//创建模版 
        	var infoTemplate = new esri.InfoTemplate("主要信息", 
             		"设备id:"+attr["id"]+
             		"<div><p class='information'>GPS:"+"东经"+parseFloat(attr["Xcoord"]).toFixed(2)+"度"+","+"西经"+parseFloat(attr["Ycoord"]).toFixed(2)+"度"+"</p>"+
             		"<p class='information'>设备地址位置:"+attr["addr"]+"</p>"+
             		"<p class='information'>IP地址:"+attr["ip"]+"</p></div>"+
             		"<div><p class='information'><a href='javascript:void(0)' onclick='beginMonitor(\""+attr["id"]+"\",\""+deviceGPS+"\",\""+attr["addr"]+"\")'>查看实时频谱</a>"+
             		"<a href='/Tsme/spectra/spectraIndex' target='_self'>查看报警信息</a></p>"+
             		"<p class='information'><a href='/Tsme/spectra/spectraIndex' target='_self'>查看历史信息</a>"+
             		"<a href='/Tsme/spectra/spectraIndex' target='_self'>查看设备属性</a></p></div>");
        	 
        	 var graphic = new esri.Graphic(pt[i], symbol[i%6], attr, infoTemplate);
        	 graphicLayer.add(graphic);
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
	}
	dojo.addOnLoad(init);
</script>
</head>
<body class="tundra" style="width: 100%; height:100%;">
	<div class="title" dojotype="dijit.layout.BorderContainer" design="headline"
		gutters="true" style="width: 100%; height:10%; margin: 0;">
		<div dojotype="dijit.layout.ContentPane" region="top" id="navtable" style="text-align:center;padding-top:20px">
			<span style="font-size:24px;">GSM-R网络固定干扰监测系统地理信息图</span>
			<span id="info"></span>
		</div>
	</div>
	<div id="mapDiv" class="map" style="width: 81%; height:89.5%;" dojotype="dijit.layout.ContentPane" region="center">
	
	</div>
	<div class="warnDiv">
	<h1>报警栏时间倒序</h1>
	</div>
	<div id="loading_div">加载中....</div>
</body>
</html>