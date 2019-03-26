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
	src="Tsme/js/public/js_patch.js""></script>
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
<script type="text/javascript">
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
			center : [ 118, 39 ]
		});
	    var point = new esri.geometry.Point(116.404, 39.915, myMap.spatialReference);// 创建点坐标   
		myMap.centerAndZoom(point, 15); 
		var layer = new esri.layers.ArcGISDynamicMapServiceLayer(
				"http://video.suizhi.net:8656/arcgis/rest/services/china/MapServer");
		myMap.addLayer(layer);
		//创建图层
        var graphicLayer = new esri.layers.GraphicsLayer();
        //把图层添加到地图上
        myMap.addLayer(graphicLayer);
        var pt = new esri.geometry.Point(116.40, 39.91, myMap.spatialReference);
        var pt1 =new esri.geometry.Point(114.48, 38.06, myMap.spatialReference);
        var pt2 =new esri.geometry.Point( 116.91,38.71, myMap.spatialReference);
        var pt3 =new esri.geometry.Point( 116.92,36.82, myMap.spatialReference);
        var pt4 =new esri.geometry.Point( 112.50,39.37, myMap.spatialReference);
        var symbol = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/greenmark.png",32,32);
        var symbol1 = new esri.symbol.PictureMarkerSymbol("/Tsme/images/map/redmark.png",32,32); 
        //要在模版中显示的参数
        var attr = { "id": "deviceId01","Xcoord":pt.x,"Ycoord":pt.y,"deviceDesc":"东经 116.40, 北纬 39.91"};
        //创建模版
        var infoTemplate = new esri.InfoTemplate("主要信息", "设备id:"+attr["id"]+"<div></div><p>设备地理坐标:"+attr["Xcoord"]+","+attr["Ycoord"]+"</p><p>设备地址坐标:"+attr["Xcoord"]+","+attr["Ycoord"]+"</p><div><a href='/Tsme/spectra/spectraIndex?deviceDesc="+attr["deviceDesc"]+"' target='_blank'>查看详情</a> </div>");
        var infoTemplate1 =new esri.InfoTemplate("标题", "地址:${address}");
        //创建图像
        var graphic = new esri.Graphic(pt, symbol, attr, infoTemplate);
        var graphic1 = new esri.Graphic(pt1, symbol1, attr, infoTemplate1);
        var graphic2 = new esri.Graphic(pt2, symbol1, attr, infoTemplate1);
        var graphic3 = new esri.Graphic(pt3, symbol1, attr, infoTemplate1);
        var graphic4 = new esri.Graphic(pt4, symbol1, attr, infoTemplate1);
        //把图像添加到刚才创建的图层上
        graphicLayer.add(graphic);
        graphicLayer.add(graphic1);
        graphicLayer.add(graphic2);
        graphicLayer.add(graphic3);
        graphicLayer.add(graphic4);
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
	<div id="mapDiv" class="map" style="width: 100%; height:90%;" dojotype="dijit.layout.ContentPane" region="center">
	</div>
</body>
</html>
