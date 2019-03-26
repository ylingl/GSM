<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page import="utils.AccountTools" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html style="width: 100%; height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设备地图展示</title>
<link rel="stylesheet" href="/Tsme/css/map/map.css" />
<link rel="stylesheet" href="/Tsme/css/public.css" />
<link rel="stylesheet" href="/Tsme/css/topBar.css" type="text/css"/>

<script type="text/javascript" src="/Tsme/js/public/js_patch.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery.include.js"></script>
 <link rel="stylesheet" href="/Tsme/css/map/ol.css" type="text/css">
     <script src="/Tsme/js/map/polyfill.js"></script>
<!--      合并页面后加入的 -->
      <script src="/Tsme/js/echarts/echarts.min.js"></script>   
      <link rel="stylesheet" type="text/css" href="/Tsme/css/map/form.css">
    <link rel="stylesheet" type="text/css" href="/Tsme/css/treecss/sitemapstyler.css">
    <link rel="stylesheet" type="text/css" href="/Tsme/css/jqueryui/jqueryui.css">
    <script type="text/javascript" src="/Tsme/js/jqueryui/jqueryui.js"></script>
      <script src="/Tsme/js/jedate/jedate.min.js"></script>
    
<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>

<style type="text/css">
body{
	background-color: #E0E0E0;
}
a:link {text-decoration:none;}
a:visited {text-decoration:none;} 
a:hover {text-decoration:none;}
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
/* 弹窗设置样式开始 */
   .ol-popup {
            position: absolute;
            background-color: white;
            -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
            filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
            padding: 15px;
            border-radius: 10px;
            border: 1px solid #cccccc;
            bottom: 12px;
            left: -50px;
            min-width: 280px;
        }

        .ol-popup:after, .ol-popup:before {
            top: 100%;
            border: solid transparent;
            content: " ";
            height: 0;
            width: 0;
            position: absolute;
            pointer-events: none;
        }

        .ol-popup:after {
            border-top-color: white;
            border-width: 10px;
            left: 48px;
            margin-left: -10px;
        }

        .ol-popup:before {
            border-top-color: #cccccc;
            border-width: 11px;
            left: 48px;
            margin-left: -11px;
        }

        .ol-popup-closer {
            text-decoration: none;
            position: absolute;
            top: 2px;
            right: 8px;
        }

        .ol-popup-closer:after {
            content: "✖";
        }
/*         弹窗设置样式结束 */
  #onLineListTableTitle td,#onLineListTable td{
   text-align: center;
   border:solid 1px #add9c0;
   width:20%;
  }
 .deviceNameClass input{border:1px solid orange;background:gray;-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;position:absolute;padding:8px 0;text-align:center;width:100px;margin:-17px 0 0 4px;font-size:1.4em;}
/*  合并页面后新加入的 */
   * {
            margin: 0;
            padding: 0;
        }
        a{
            text-decoration:none;

        }
        body{
            font-family:"Helvetica Neue",Helvetica,Arial,sans-serif;
        }
        ul,li{


            list-style:none;
        }
        .routeTitleStyle {
            height: 7%;
            background-color: #01446D;
            line-hight:20px;
            /*             margin-top:3%; */
        }
        .routeContentStyle {
            height: 93%;
            background-color: #E0E0E0;
        }

        .routeContentStyle .routeNavLeft {
            width: 3.5%;
            height: 100%;
            float: left;
            background-color: #01446D;

        }

        .routeContentStyle .routeNavLeftContent,.routeContentStyle .routeNavLeftBigContent,.routeContentStyle #setDivIdRoute {
            width: 32.5%;
            height: 92%;
            float: left;
            padding: 0.5% 0px 0px 0.8%;
            position: absolute;
            left: 3.5%;
            top:7%;
            z-index: 1002;
            background-color: #1A567B;

        }

        .routeContentStyle .routeNavLeftContent #routeNavLeftDeviceList, .routeContentStyle .routeNavLeftContent #routeNavLeftTest {
            width: 99%;
            height: 96%;
            /*float: left;*/
            padding: 0.5% 0px 0px 0.8%;
            /*position: absolute;*/
            /*left: 3.5%;*/
            /*z-index: 999;*/
            /*background-color: red;*/
            display: none;

        }

        .routeContentStyle .routeDiv {
            width: 96.5%;
            height: 100%;
            padding-left: 1%;
            float: left;
            /*background-color: green;*/
        }

       .routeDivTop {
            height: 2%;
            position:absolute;
            top:8%;
         
        }

        .routeContentStyle .routeDiv .routeDivBottom {
            float:left;
            margin-top: 0.5%;
            width: 99%;
            height: 93.5%;
            /*background-color: pink;*/
        }
        .routeContentStyle .routeDiv .routeMapRightBing {
        	position:absolute;
        	top:57%;
        	right:1%;
/*             float:left; */
/*             margin-top: 0.5%; */
/*             margin-left: 0.5%; */
            width: 24.5%;
            height: 38.5%;
            border: solid 1px #9B9A98;
/*             background-color: white; */
             background-color:rgba(255,255,255,0.7);
        }
        .routeContentStyle .routeDiv .routeMapRightBing .routeRightContentBing{
            width: 98.0%;
            height: 73%;
            margin-left: 1.5%;
        }
         .routeContentStyle .routeDiv .routeMapRightBing .routeRightContentBing #routeRxlBing {
            width: 99%;
            height: 97%;
            margin-top:4%;
            /*background-color: white;*/
            border: solid 1px #9B9A98;
            border-radius: 15px;
            
        }

        .routeContentStyle .routeDiv .routeMapRightBing .routeRightContentBing #routeRxqBing {
            width: 99%;
            height: 45%;
            /*background-color: white;*/
            margin-top: 5%;
            border: solid 1px #9B9A98;
            border-radius: 15px;
        }
		.routeContentStyle .routeDiv .routeMapRightBing .routeMapRightBingTitle{
            width: 100.3%;
            height: 4.6%;
            border: solid 1px #9B9A98;
            background-color: #FAFAFA;
        }
		.routeContentStyle .routeDiv .routeMapRightBing .routeMapRightBingTitle span{
           margin-top:1%;
           display:inline-block;
        }
        .routeContentStyle .routeDiv .routeMapRightBing .routeMapRightBingTitle .routeData{
           margin-left:2%;
        }
        .routeContentStyle .routeDiv .routeMapRightBing .routeMapRightBingTitle .routeDataBtn{
           margin-left:66%;
        }
        .routeContentStyle .routeDiv .routeDivBottom .routeMap {
            /*             width: 75%; */
            width:100%;
            height: 100%;
            border: solid 1px #9B9A98;
            /*background-color: blue;*/
            float: left;
        }

        .routeContentStyle .routeDiv .routeDivBottom .routeMap .routeChart {
            width: 51.7%;
            height: 45%;
            background-color: grey;
            position: absolute;
            top: 17%;
            z-index: 999;
        }

        .routeContentStyle .routeDiv .routeDivBottom .routeMap .routeTable {
            width: 50.8%;
            height: 38%;
            background-color: #E0E0E0;
            position: absolute;
            top: 62%;
            z-index: 997;
        }

/*          .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapTitle {  */
/*              /*             width: 70.8%; */  */
/*              width:94.4%;  */
/*              height: 4.5%;  */
/*              background-color: #FAFAFA;  */
/*              position: absolute;  */
/*              top:12.5%; */
/*              z-index: 997;  */
/*              border-bottom: solid 1px #9B9A98;  */
/*          }  */
 .mapTitles{   

               width:94.4%; 
               height: 4.5%;
               background-color: #FAFAFA;
               position: absolute;
              top:12.5%;
               z-index: 997;
              border-bottom: solid 1px #9B9A98;
           }  
        
/*         .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapTitle select{ */
/*             position: absolute; */
/*             top:17%; */
/*             z-index: 997; */
/*         } */
/*        .mapTitle input{ */
/*             position: absolute; */
/*             top:10%; */
/*             z-index: 997; */
/*         } */
        .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapColorImage{
            position: absolute;
            z-index: 996;
            bottom:1%;
            left:4.5%;
        }
/*         .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapTitle img { */
/*             position: absolute; */
/*             z-index: 998; */
/*         } */
        .routeContentStyle .routeDiv .routeDivBottom .routeMap .routeChartTableIcon {
            position: absolute;
            z-index: 998;
            left:5.2%;
            top:24%;
        }
        .routeContentStyle .routeDiv .routeDivBottom .routeMap .routeChartTableIcon img{
            width:23px;
            height:23px;
            border-radius:1px;
        }
        .routeContentStyle .routeDiv .routeDivBottom .routeRightContent {
            width: 24.8%;
            height: 100%;
            /*background-color: orange;*/
            float: left;
        }

        .routeContentStyle .routeDiv .routeDivBottom .routeRightContent .routeRightContentDetail {
            width: 95%;
            height: 29.8%;
            padding-left: 5%;
            padding-top: 1%;
            /*background-color: pink;*/
        }

        .routeContentStyle .routeDiv .routeDivBottom .routeRightContent .routeRightContentBing {
            width: 98.8%;
            height: 70%;
/*             padding-left: 5%; */
            /*background-color: purple;*/
        }

        .routeContentStyle .routeDiv .routeDivBottom .routeRightContent .routeRightContentBing #routeRxlBing {
            width: 99%;
            height: 45%;
            /*background-color: white;*/
            border: solid 1px #9B9A98;
            border-radius: 15px;
        }

        .routeContentStyle .routeDiv .routeDivBottom .routeRightContent .routeRightContentBing #routeRxqBing {
            width: 99%;
            height: 45%;
            /*background-color: white;*/
            margin-top: 5%;
            border: solid 1px #9B9A98;
            border-radius: 15px;
        }

        span.routeBingName {
            display: block;
            width: 130px;
            height: 25px;
            line-height: 25px;
            position: relative;
            top: -15px;
            left: 50px;
            text-align: center;
            border-radius: 10px;
            background: white;
            z-index: 9999;
        }
        .ol-popup {
            position: absolute;
            background-color: white;
            -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
            filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
            padding: 15px;
            border-radius: 10px;
            border: 1px solid #cccccc;
            bottom: 12px;
            left: -50px;
            /*display: none;*/
            min-width: 280px;
        }
        .bottomTable{
            background-color:white;
        }
        .bottomTable td{
            height:20px;
        }
        .bottomTable tr:nth-child(even){
            background: #f5fafe;;
        }
        .bottomTable tr:nth-child(1) td{
            font-size:16px; //字体大小
        font-family:"宋体"; //字体
        font-weight:bold;
        }
        .bottomTable tr td{
            font-size:12px; //字体大小
        font-family:"宋体"; //字体
        font-weight:bold;
        }

        .ol-popup:after, .ol-popup:before {
            top: 100%;
            border: solid transparent;
            content: " ";
            height: 0;
            width: 0;
            position: absolute;
            pointer-events: none;
        }

        .ol-popup:after {
            border-top-color: white;
            border-width: 10px;
            left: 48px;
            margin-left: -10px;
        }

        .ol-popup:before {
            border-top-color: #cccccc;
            border-width: 11px;
            left: 48px;
            margin-left: -11px;
        }

        .ol-popup-closer {
            text-decoration: none;
            position: absolute;
            top: 2px;
            right: 8px;
        }

        .ol-popup-closer:after {
            content: "✖";
        }
        #dataSheZhi {
            margin: 15px 50px;
            padding: 10px;
            border: solid 1px;
        }
        #number {
            margin: 15px 50px;
            padding: 10px;
            border: solid 1px;

        }
        #routeMaskParent,#routeMaskParentBing{
            width:100%;
            height:100%;
            position:absolute;
            top:0;
            left:0;
            z-index:999999;
            display:none;
        }
        #routeMask,#routeMaskBing{
            width:100%;
            height:100%;
            background-color:black;
            position:absolute;
            top:0;
            left:0;
            opacity:0.2;
            /*兼容IE8及以下版本浏览器*/
            filter: alpha(opacity=30);
        }
        #routeMaskText,#routeMaskTextBing {
            width:100%;
            position:absolute;
            top:48%;
            left:0;
            z-index:9999999;
        }
        .routePointLat{
            position:fixed;
            top:8%;
            right:2%;
            z-index:999;
        }
        .routePointLatWord{
            position:fixed;
            top:7.8%;
            right:9.5%;
            z-index:999;
        }
        /*          地图控件设置样式开始 */
        .ol-box {
            box-sizing: border-box;
            border-radius: 2px;
            border: 2px solid #00f
        }

        .ol-mouse-position {
            top: 8px;
            right: 8px;
            position: absolute
        }

        .ol-scale-line {
            background: rgba(0, 60, 136, .3);
            border-radius: 4px;
            bottom: 8px;
            right: 8px;
            padding: 2px;
            position: absolute
        }

        .ol-scale-line-inner {
            border: 1px solid #eee;
            border-top: none;
            color: #eee;
            font-size: 10px;
            text-align: center;
            margin: 1px;
            will-change: contents, width
        }

        .ol-overlay-container {
            will-change: left, right, top, bottom
        }

        .ol-unsupported {
            display: none
        }

        .ol-unselectable, .ol-viewport {
            -webkit-touch-callout: none;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
            -webkit-tap-highlight-color: transparent
        }

        .ol-selectable {
            -webkit-touch-callout: default;
            -webkit-user-select: auto;
            -moz-user-select: auto;
            -ms-user-select: auto;
            user-select: auto
        }

        .ol-grabbing {
            cursor: -webkit-grabbing;
            cursor: -moz-grabbing;
            cursor: grabbing
        }

        .ol-grab {
            cursor: move;
            cursor: -webkit-grab;
            cursor: -moz-grab;
            cursor: grab
        }

        .ol-control {
            position: absolute;
            background-color: rgba(255, 255, 255, .4);
            border-radius: 4px;
            padding: 2px
        }

        .ol-control:hover {
            background-color: rgba(255, 255, 255, .6)
        }

        .ol-zoom {
            top: .7em;
            left: .5em
        }

        .ol-rotate {
            top: .5em;
            right: .5em;
            transition: opacity .25s linear, visibility 0s linear
        }

        .ol-rotate.ol-hidden {
            opacity: 0;
            visibility: hidden;
            transition: opacity .25s linear, visibility 0s linear .25s
        }

        .ol-zoom-extent {
            top: 4.643em;
            left: .5em
        }

        .ol-full-screen {
            right: .5em;
            top: .5em
        }

        @media print {
            .ol-control {
                display: none
            }
        }

        .ol-control button {
            display: block;
            margin: 1px;
            padding: 0;
            color: #fff;
            font-size: 1.14em;
            font-weight: 700;
            text-decoration: none;
            text-align: center;
            height: 1.375em;
            width: 1.375em;
            line-height: .4em;
            background-color: rgba(0, 60, 136, .5);
            border: none;
            border-radius: 2px
        }

        .ol-control button::-moz-focus-inner {
            border: none;
            padding: 0
        }

        .ol-zoom-extent button {
            line-height: 1.4em
        }

        .ol-compass {
            display: block;
            font-weight: 400;
            font-size: 1.2em;
            will-change: transform
        }

        .ol-touch .ol-control button {
            font-size: 1.5em
        }

        .ol-touch .ol-zoom-extent {
            top: 5.5em
        }

        .ol-control button:focus, .ol-control button:hover {
            text-decoration: none;
            background-color: rgba(0, 60, 136, .7)
        }

        .ol-zoom .ol-zoom-in {
            border-radius: 2px 2px 0 0;
            margin-top: 20px
        }

        .ol-zoom .ol-zoom-out {
            border-radius: 0 0 2px 2px
        }

        .ol-attribution {
            display:none;
            text-align: right;
            bottom: .5em;
            right: .5em;
            max-width: calc(100% - 1.3em)
        }

        .ol-attribution ul {
            margin: 0;
            padding: 0 .5em;
            font-size: .7rem;
            line-height: 1.375em;
            color: #000;
            text-shadow: 0 0 2px #fff
        }

        .ol-attribution li {
            display: inline;
            list-style: none;
            line-height: inherit
        }

        .ol-attribution li:not(:last-child):after {
            content: " "
        }

        .ol-attribution img {
            max-height: 2em;
            max-width: inherit;
            vertical-align: middle
        }

        .ol-attribution button, .ol-attribution ul {
            display: inline-block
        }

        .ol-attribution.ol-collapsed ul {
            display: none
        }

        .ol-attribution.ol-logo-only ul {
            display: block
        }

        .ol-attribution:not(.ol-collapsed) {
            background: rgba(255, 255, 255, .8)
        }

        .ol-attribution.ol-uncollapsible {
            bottom: 0;
            right: 0;
            border-radius: 4px 0 0;
            height: 1.1em;
            line-height: 1em
        }

        .ol-attribution.ol-logo-only {
            background: 0 0;
            bottom: .4em;
            height: 1.1em;
            line-height: 1em
        }

        .ol-attribution.ol-uncollapsible img {
            margin-top: -.2em;
            max-height: 1.6em
        }

        .ol-attribution.ol-logo-only button, .ol-attribution.ol-uncollapsible button {
            display: none
        }

        .ol-zoomslider {
            top: 4.5em;
            left: .5em;
            height: 200px
        }

        .ol-zoomslider button {
            position: relative;
            height: 10px
        }

        .ol-touch .ol-zoomslider {
            top: 5.5em
        }

        .ol-overviewmap {
            left: .5em;
            bottom: .5em
        }

        .ol-overviewmap.ol-uncollapsible {
            bottom: 0;
            left: 0;
            border-radius: 0 4px 0 0
        }

        .ol-overviewmap .ol-overviewmap-map, .ol-overviewmap button {
            display: inline-block
        }

        .ol-overviewmap .ol-overviewmap-map {
            border: 1px solid #7b98bc;
            height: 150px;
            margin: 2px;
            width: 150px
        }

        .ol-overviewmap:not(.ol-collapsed) button {
            bottom: 1px;
            left: 2px;
            position: absolute
        }

        .ol-overviewmap.ol-collapsed .ol-overviewmap-map, .ol-overviewmap.ol-uncollapsible button {
            display: none
        }

        .ol-overviewmap:not(.ol-collapsed) {
            background: rgba(255, 255, 255, .8)
        }

        .ol-overviewmap-box {
            border: 2px dotted rgba(0, 60, 136, .7)
        }

        .ol-overviewmap .ol-overviewmap-box:hover {
            cursor: move
        }
       
        /* 地图控件设置样式结束 */
</style>

</head>
<script>
//控制设备列表显示和操作开始
function getOnlineDeviceList(){
		$.ajax({
			url : "/Tsme/map/getOnlineDeviceList",
			type : "POST",
			async : false,
			dataType : 'json',
			success : function(data) {
				console.log(data);
				$("#onLineListTable").empty();
// 				$("#onLineListTable").append("<div style='padding-left:6px; font-family:Microsoft YaHei; color:white;'>在线设备列表：</div>");
				if(data != null){
					for(var i in data){
						var icon = "";
						var butt = "";
						var html = "";
						var status = "";
						var statusText = "";
						if(data[i].device_status == "training"){
							statusText = "训练";
							butt = "<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"training(" + data[i].device_num + ")\" value=\"查看\"/> " + 
									"<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"stopTraining(" + data[i].device_num + ")\" value=\"释放\"/>" +
									"<input type=\"hidden\" id=\"h + data[i].device_num + \" value=\"training\"/>";
							status = "training";
						} else if(data[i].device_status == "warning"){
							statusText = "监测";
							butt = "<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"getDoubbleStatus(" + data[i].device_num + ")\" value=\"查看\"/> " + 
								"<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"stopWarning(" + data[i].device_num + ")\" value=\"释放\"/>" + 
								"<input type=\"hidden\" id=\"h + data[i].device_num + \" value=\"warning\"/>";
							status = "warning";
						} else if(data[i].device_status == "free"){
							statusText = "空闲";
							butt = "<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"training(" + data[i].device_num + ")\" value=\"训练\"/> " + 
								"<input type=\"button\" style=\"height:16px; line-height:10px; width:30px; padding:0px;\" onclick=\"getDoubbleStatus(" + data[i].device_num + ")\" value=\"监测\"/>" + 
								"<input type=\"hidden\" id=\"h + data[i].device_num + \" value=\"free\"/>";
							status = "free";
						}
						
						if(data[i].warning && status != "free"){
							icon = "<img id=\"i" + data[i].device_num + "\" style=\"width:16px; height:16px; padding-bottom:2px;\" src=\"\/Tsme\/images\/map\/redequipment.png\">"
// 							html = "<table id=\"f" + data[i].device_num + "\" style=\"padding-left:6px; height:18px; line-height:18px; color:red; font-size:12px; font-family:Microsoft YaHei;\">" + icon + data[i].device_num + butt + "</table>" 
							html='<tr id=\"f" + data[i].device_num + "\" style="cursor:pointer;" onclick="getDeviceLatLon(' + data[i].lng + ',' + data[i].lat + ',this)">'
								+'<td style="color:red;">'+icon + data[i].device_num+'</td>'
								+'<td class="deviceNameClass" ondblclick="getDeviceName(' + data[i].device_num + ',this)">' + data[i].name+'</td>'
								+'<td style="color:red;">'+statusText+'</td>'
								+'<td style="color:red;">'+butt+'</td>'
								+'</tr>'
								+'</table>'
						} else {
							icon = "<img id=\"i" + data[i].device_num + "\" style=\"width:16px; height:16px; padding-bottom:2px;\" src=\"\/Tsme\/images\/map\/greenequipment.png\">" 
// 							html = "<table id=\"f" + data[i].device_num + "\" style=\"padding-left:6px; height:18px; line-height:18px; color:green; font-size:12px; font-family:Microsoft YaHei;\">" + icon + data[i].device_num + butt  + "</table>"
							html='<tr id=\"f" + data[i].device_num + "\" style="cursor:pointer;" onclick="getDeviceLatLon(' + data[i].lng + ',' + data[i].lat + ',this)">'
								+'<td style="color:#0AD220;">'+icon + data[i].device_num+'</td>'
								+'<td class="deviceNameClass" ondblclick="getDeviceName(' + data[i].device_num + ',this)">' + data[i].name+'</td>'
								+'<td style="color:#0AD220;">'+statusText+'</td>'
								+'<td style="color:darkgreen;">'+butt+'</td>'
								+'</tr>'
								+'</table>'
						}
						
						if($("#f" + data[i].device_num).length && $("#f" + data[i].device_num).length > 0) {
							var temp = $("#h" + data[i].device_num).val();
							if(temp != status) {
								$("#f" + data[i].device_num).replaceWith(html);
							}
						} else {
							$("#onLineListTable").append(html);	
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
						init();
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
					setTimeout("getOnlineDeviceList()",3000);
					setTimeout("init()",3000);
					
				} else {
					layer.msg('无法停止', {icon: 1});
				}
			}
		})
	}
//控制设备列表显示和操作结束
//选择模板确定按钮函数开始
function beginMonitor_two(){
	var  temp = $("#templateId_two").val();
	if($("#templateId_two").val() != null){
		$("#selectTemplateForm_two").submit();
	} else {
		$("#selectTemplateDialog_two").modal('hide');
	}
}
//选择模板确定按钮函数结束
//直接进入两种状态的结合查看页面
	function getDoubbleStatus(deviceNum){
		$("#templateId_two").empty();
		$.ajax({
			url: "/Tsme/spectra/getWarningTemplateList",
			type: "POST",
			data: {deviceNum: deviceNum},
			dataType: 'json',
			success: function(data) {
// 				console.log(data);
					if(data.employ == true) {
						$("#deviceNum_two").val(deviceNum);
						$("#selectTemplateForm_two").submit();
					} else {
						if(data.warningTemplateList != null) {
							var warningTemplateList = data.warningTemplateList
							for(var i = 0; i < warningTemplateList.length; i++){
								$("#templateId_two").append("<option value='" + warningTemplateList[i].id +"'>" + warningTemplateList[i].template_name + "</option>");
							}
							$("#deviceNum_two").val(deviceNum);
							
							var obj = $("#" + deviceNum);
							
							$("#selectTemplateDialog_two").modal('show');
						} else {
							window.location.href="/Tsme/history/showData/" + deviceNum;
						}
					}
			}
		})
	}
</script>
<body class="tundra" style="height:100%;width: 100%;">
<div class="title" id="titleTime">
		<div style="text-align:left;">
			<img src="/Tsme/images/map/logo.png" style="display:inline-block;vertical-align: middle;margin-left:6px;"/>	
			<span style="font-size:18px;display:inline-block;vertical-align: middle;margin:7px 0 0 10px;">无线监测</span>
			<span id="info"></span>
			<div id="setDivId" style="display:block;position:absolute;left:84%;top:6px;">
		<div class="rightIcon">
			<div id="divAccount"  id="rightIconImage"></div>
			<div class="triggle"></div>
			<a class="logout" style="cursor:pointer;" href="/Tsme/security/logout/clearAccountData">登出</a>
		</div>
		<div class="divInfo">
			<div id="name"></div>
			<div id="role"></div>
			<input type="button" id="applyForTrainerBotton" style="display:none; height:20px; line-height:10px; width:100px; padding:0px;" onclick="applyForTrainer()" value="申请成为训练员" />
		</div>
<!-- 		<div id="onlineDeviceList" style="clear:both"> -->
<!-- 			<div style="padding-left:6px; font-family:Microsoft YaHei; color:"white";">设备列表：</div> -->
<!-- 		</div> -->
	</div>
		</div>
		
	</div>
<!-- 	<HR width="94.5%" style="margin-top:2%;" color=#987cb9> -->
	
    <div id="bottomContent" style="width: 100%; height:92%;">
   
   <!--左侧导航栏的3个ico图标  -->
    <div id="rightNav" class="rightNavDiv">
	   <div class="pointNav">
		    <a href="">
		      	<img src="/Tsme/images/map/button_nav_fixed-device.png " alt="" class="pointImg" id="pointCtrol" >
		    </a>
	 		<a href="/Tsme/history/route">
	 			<img src="/Tsme/images/map/button_nav_offline-data.png" alt="" style="margin-top:34px;width:29px;height:28px;" id="setCtrolRoute">
	 	    </a>
 		 	<!-- 扫频数据离线功能 -->
	 		<a href="/Tsme/history/sweep">
	 			<img src="/Tsme/images/map/button_nav_offline-scan.png" alt="" style="margin-top: 34px;width:29px;height:28px;" id="SweepOffLine">
	 		</a>
	   </div>
	   
	   
	   <div class="lineNav"></div>
	   <div class="setNav"></div>
	</div>
	<!--左侧导航栏的3个ico图标 结束  -->
	
	<div class="rightContent">
	 <div class="routeDivTop">
            <span style="line-height: 200%;" id="routeDeviceTime"></span><span style="display:none;" id="routeDeviceTimeJia"></span>
            <span class="routePointLat"><span class="routePointLatWord">经纬度：</span></span>
        </div>
	    <div class="line"></div>
		<div id="mapDiv" class="map" dojotype="dijit.layout.ContentPane" region="center">
		 <div class="routeChartTableIcon">
                    <img src="/Tsme/images/map/mapbutton_spectrum.png" alt="" style="left: 1%;display:block;" id="routeChartBtn">
                    <img src="/Tsme/images/map/mapbutton_table.png" alt="" style="margin-top: 4%;display:block;" id="routeTableBtn">
              	    <img src="/Tsme/images/map/buttonBing.png" alt="" style="margin-top: 4%;display:block;" id="routeBingBtn">
                </div>
		<div class="mapTitles" style="width:94.4%;height: 4.5%;background-color: #FAFAFA;position:absolute;z-index: 997;border-bottom: solid 1px #9B9A98;">
                    <!--                     <input type="checkbox" style="margin-left: 15%" id="routeJZ" checked="checked">基站/小区 &nbsp&nbsp&nbsp  -->
                    <!--                      <input type="checkbox" id="routeLJ" checked="checked">路径点&nbsp&nbsp&nbsp -->
                    <!--                      <input type="checkbox" id="routeLX">小区连线 -->
                    <span style="margin-left: 1%;margin-top:2%;">参数：</span>
                    <select name="" id="routeParam">
	                    <option value="">Rx Lev</option>
	                    <!--                     <option value="">Rx Qual</option> -->
                    </select>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                   
                    范围：<input type="text" style="width:4%" id="maxRangeLx"> <span style="position:absolute;left:23.3%;top:14%;"> - </span> <input type="text" style="position:absolute;width:4%;left:24.5%;top:8.3%" id="minRangeLx">&nbsp&nbsp&nbsp
                    <input type="button" value="提交" id="submitRangeLx" style="font-family:'宋体' ;position:absolute;left:30%;top:8%;">
                    <span style="margin-left:13%;line-height:35px;">路线选择：&nbsp</span><select id="chooseLine" style="line-height:35px;" onchange="chooseLine();">
                    <option value="">请选择</option>
                    <option value="lx">兰新线</option>
                </select>
                    <!-- 					<span><button style="margin-left:5%;" onclick="">兰新线</button></span> -->
                    <span style="margin-left:40.5%;">路径点时间：<span id="routePointTime"></span></span>
                    <!--                     <input type="button" value="离线暂停" id="startLx" style="font-family:'宋体' "> -->
                </div>
<!--                 饼图开始 -->
                  <div class="routeMapRightBing">
        		<a  style="position:absolute;top:0%;right:0%;" ><img src="/Tsme/images/map/button_close.png" alt="" id="routeCloseBing"></a>
<!--         	<div class="routeMapRightBingTitle"> -->
<!--         		<span class="routeData">数据统计</span> -->
<!--         		<span class="routeDataBtn">统计按钮</span> -->
<!--         	</div> -->
<!--         	<p style="margin:2% 0% 2% 4%;">数据概况</p> -->
<!-- 			<br /> -->
            <p style="margin:1.5% 0% 1% 4%;">数据总量：<span id="routeTotal">***</span></p>
            <p style="margin:0% 0% 1% 4%;">总测试时长：<span id="routeTotalTime">***</span></p>
            <p style="margin:0% 0% 0% 4%;">最近更新：<span>***</span></p>
<!--             <p style="margin:0% 0% 0% 4%;">设备数量：<span>2台</span></p> -->
            <div class="routeRightContentBing"> 
                <div id="routeRxlBing"> 
                     <span class="routeBingName">Rx Level(dBm)</span>
                </div> 
<!--                 <div id="routeRxqBing">  -->
<!--                      <span class="routeBingName">Rx Qual</span>  -->
<!--                 </div>  -->
            </div> 
        </div>
<!--         饼图结束 -->
 <!--放置折线图盒子开始-->
                <div class="routeChartBox">
<!--                 	<span class="routeChartCloseBtn" style="width:1%;height:1.5%;vertical-align: middle;text-align: center;cursor:pointer;position:absolute;top:17%;left:52%;z-index:1001;display:none;color:black;background-color:white;"> x</span> -->
                	    <img src="/Tsme/images/map/button_close.png" class="routeChartCloseBtn" style="cursor:pointer;position:absolute;top:17%;left:52.5%;z-index:1001;display:none;" alt="">
                	    <div class="routeDragChart" style="width:500px;;height:28px;position:absolute;top:24%;left:9%;z-index:1001;display:none;background-color:#FFFFB1;cursor: move;"></div>
                		<div class="routeRxqLine" style="width:5%;position:absolute;top:37%;left:4.5%;z-index:1000;border-top: solid 2px #EEEED8;display:none;"></div>
                		<div class="routeRxqLine" style="width:5%;position:absolute;top:45%;left:4.5%;z-index:1000;border-top: solid 2px #EEEED8;display:none;"></div>
                		<div class="routeChart" style="display: none;">
                </div>
                </div>
              
                <!--放置折线图盒子结束-->
			<div id="popup" class="ol-popup">
		    <a href="#" id="popup-closer" class="ol-popup-closer"></a>
		    <div id="popup-content"></div>
			</div>
		</div>
    		
	</div>
	
    </div>

    <!--放置地图上表格的盒子开始-->
                <div class="routeTable" style="overflow-y: auto;display: none;">
                    <span class="routeTableCloseBtn" style="width:2%;height:4.5%;vertical-align: middle;text-align: center;cursor:pointer;position:absolute;top:1%;left:97.6%;z-index:1001;display:none;background-color:white;"> x</span>
                         <img src="/Tsme/images/map/button_close.png" class="routeTableCloseBtn" style="position:absolute;top:1%;left:97.3%;z-index:1001;display:none;" alt="">
                    <!--控制表格中内容的盒子开始-->
                    <div id="routeTableTitle">
                        <select id="routeTableTitle0">
                            <option value="">主小区</option>
                            <option value="">邻小区</option>
                            <option value="">扫频数据</option>
                            <option value="">BA list</option>
                        </select>

                        <select style="margin-left:41%;" id="routeTableTitle1">
                            <option value="">邻小区</option>
                            <option value="">主小区</option>
                            <option value="">扫频数据</option>
                            <option value="">BA list</option>
                        </select>

                    </div>
                    <!--控制表格中内容的盒子结束-->
                    <!--放置主小区表格内容的盒子开始-->
                    <div id="routeMainTableParent">
                        <table id="routeMainTable" class="bottomTable" border="1" bordercolor="#a0c6e5"
                               style="border-collapse:collapse;color: black;width:49%;background-color:white;text-align:center;float:left;">
                            
                        </table>
                        <!--                         扫频表格 -->
                        <table id="routeMainTableSao" border="1" class="bottomTable" bordercolor="#a0c6e5"
                               style="border-collapse:collapse;color: black;width:49%;text-align: center;float:left;display:none;">
                            <tr class="bottomTableHead" style="font-weight:bold;">
                                <td width="170px">参数扫频</td>
                                <td width="380px">值</td>
                            </tr>
                            <tr>
                                <td>GSM MCC</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM LAC</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM CI</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM BSIC</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM rxl</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM cell</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM rxq</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM rla</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM mnc</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM TA</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM txq</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>GSM arfcn</td>
                                <td></td>
                            </tr>
                        </table>
                        </table>
                        <!--                         邻小区表格 -->
                        <table  border="1" bordercolor="#a0c6e5" class="bottomTable"
                                style="border-collapse:collapse;color: black;width:49%;text-align: center;float:left;display:none;" id="routeMainTableLin">
                           
                        </table>
                        <!--放置邻小区表格内容的盒子开始 -->

                        <div style="float:left;width:49%;height:99%;margin-left:2%;" id="routeNearContent">
                            <table id="routeMainTableRightZhu" border="1" bordercolor="#a0c6e5" class="bottomTable"
                                   style="border-collapse:collapse;color: black;width:98%;text-align: center;display:none;float:left;">
                              
                            </table>
                            <table  border="1" bordercolor="#a0c6e5" class="bottomTable"
                                    style="border-collapse:collapse;color: black;width:99%;text-align: center;float:left;" id="routeMainTableRightLin">
                               
                            </table>
                            <!--                         you扫频表格 -->
                            <table id="routeMainTableRightSao" border="1" bordercolor="#a0c6e5" class="bottomTable"
                                   style="border-collapse:collapse;color: black;width:99%;text-align: center;float:left;display:none;">
                                <tr class="bottomTableHead" style="font-weight:bold;">
                                    <td width="170px">参数扫频</td>
                                    <td width="380px">值</td>
                                </tr>
                                <tr>
                                    <td>GSM MCC</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM LAC</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM CI</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM BSIC</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM rxl</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM cell</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM rxq</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM rla</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM mnc</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM TA</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM txq</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>GSM arfcn</td>
                                    <td></td>
                                </tr>
                            </table>
                            </table>

                        </div>
                        <!--                     放置邻小区表格内容的盒子结束 -->
                    </div>
                    <!--放置主小区表格内容的盒子结束-->

                </div>
                <!--放置地图上表格的盒子结束-->
     <!-- 扫频离线开始 -->
    <!--  <div class="warnText" id="SweepOffLine0" style="display:none;" >
		<div class="warnTextDiv" style="margin-top:154px;">扫频离线&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#fff;">></span></div>
	 </div> -->
     <!-- 扫频离线结束 -->
     
     
	
   <!-- 左边导航移入显示结构开始 -->
   <div class="pointIConBg"></div>
   <div class="pointWrap" style="display:block;">
   		<div class="pointIConBox pointIConBox1">
   			<div class="pointList">
   				<img src="/Tsme/images/map/button_nav_fixed-device.png " alt="" class="pointImg" >
	   			<span>定点监测&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>></span></span>
   			</div>
	   		<!-- 定点监测表格开始 -->
		    <div class="warnDiv publicDiv" id="warnDivId" style="display:none;">
				<br/>
				<div id="onlineDeviceList" style="clear:both">
					<div id="onLineList" style="padding-left:12px; font-family:Microsoft YaHei; color:#333;background-color: rgb(239,239,239);">
						<div style="margin-bottom:10px;">定点监测设备：</div>
						<table id="onLineListTable"  border="1" cellspacing="0" cellpadding="5"  width="90%" style="margin-left:3%;margin-top:0%;"></table>
					</div>
					<br/>
				</div>
			</div>
			<!-- 定点监测表格结束 -->
   		</div>
	    <div class="pointIConBox pointIConBox2">
	    	<div class="pointList">
	    		<a href="/Tsme/history/route"><img src="/Tsme/images/map/button_nav_offline-data.png " alt="" class="pointImg" ></a>
	   			<span>路测离线&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>></span></span>
	    	</div>
	   		<!--离线数据盒子开始 -->
		    <div id="setDivIdRoute" style="display:none;" class="publicDiv ">
		        <p style="color:#333;margin:1.5%;">路测离线数据</p>
		        
		     	<form action="/Tsme/history/simulationOnlineUploadFile" method="post" enctype="multipart/form-data" style="margin-left:14%;">
		            <input type="file" name="file" id="upfile" multiple="multiple"/>
		            <input type="submit" style="position:absolute;left:75%;top:5%;" value="提交" onclick="uploadJson()"/>
		        </form>
		        <div style="width:98%;height:87%;color:#333;overflow-y:auto;">
				<br/>
		    	<hr  style="border-top:1px dashed #333;border-bottom:0px dashed #333" />
		    	<p style="margin:2% 0 2% 4.7%;">近期数据</p>
		     	<ul id="sitemap">
		            <li>
		            	<span  id="routeBigDeviceLuJuBig"></span>
		            	<a href="#" style="color:#333;" id="routeBigDeviceLuJuTextBig">广铁局1</a>
		                <ul id="routeBigDeviceBoxBig" style="display:none;">
		                    <li>
			                    <span  id="routeBigDeviceIconBig"></span>
			                    <a href="#" style="color:#333;" id="routeBigDeviceIconTextBig">设备002</a>
		                    </li>
		                </ul>
		            </li>
		        </ul>
				<!--     小范围开始 -->
				<br/>
				<br/>
		   		<hr  style="border-top:1px dashed #333;border-bottom:0px dashed #333" />
		    	<p style="margin:2% 0 2% 4.7%;">全部数据</p>
		        <ul id="sitemap">
		            <li>
		            	<span  id="routeBigDeviceLuJu"></span>
		            	<a href="#" style="color:#333;" id="routeBigDeviceLuJuText">广铁局1</a>
		                <ul id="routeBigDeviceBox" style="display:none;">
		                    <li>
		                    	<span  id="routeBigDeviceIcon"></span>
		                    	<a href="#" style="color:#333;" id="routeBigDeviceIconText">设备002</a>
		                    </li>
		                </ul>
		            </li>
		        </ul>
		       </div>
		    </div>
		    <!--离线数据盒子结束 -->
	    </div>
	    <div class="pointIConBox pointIConBox3">
	    	<div class="pointList">
	    		<img src="/Tsme/images/map/button_nav_offline-scan.png " alt="" class="pointImg" >
	   			<span>扫频离线&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>></span></span>
	    	</div>
	    	<div class="publicDiv" style="width:0;heigt:0;"></div>
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
	
	
	<div class="modal fade in" style="top:100px;" id="selectTemplateDialog_two" tabindex="-1" role="dialog" aria-labelledby="editmodelLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="">请选择设备模板</h4>
				</div>
				<div class="modal-body">
					<form id="selectTemplateForm_two" action="/Tsme/history/chart_two">
					    <input type="hidden" name="deviceNum_two" id="deviceNum_two"/>
						<div class="form-group">
							<select name="templateId_two" id="templateId_two"></select> 
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" onclick="beginMonitor_two()">确定</button>
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





<!-- 上传数据进度显示 -->
<div id="routeMaskParent">
    <div id="routeMask"></div>
    <marquee direction="right" id="routeMaskText">
        <span style="font-weight: bolder;font-size: 40px;color: black;">正在上传，请稍后......</span>
    </marquee>
</div>
<!-- 饼图统计进度显示 -->
<div id="routeMaskParentBing">
    <div id="routeMaskBing"></div>
    <marquee direction="right" id="routeMaskTextBing">
        <span style="font-weight: bolder;font-size: 40px;color: black;">正在统计，请稍后......</span>
    </marquee>
</div>



<div class="theme-popover">
    <div class="theme-poptit">
        <a href="javascript:;" title="关闭" class="close">×</a>
        <p id="newTaskTitle">任务创建</p>
    </div>
    <div class="theme-popbod dform">
        <form class="theme-signin" name="loginform" action="" method="post">
            <div id="dataSheZhi">
                任务名称:<input type="text" class="taskName" onkeyup="value=value.replace(/[\d]/g,'') "><span
                    style="color: red">*</span>&nbsp&nbsp
                任务创建人:<input type="text" placeholder="请输入汉字" class="taskN"
                             onkeyup="value=value.replace(/[^\u4E00-\u9FA5]/g,'')"><span style="color: red">*</span><br><br>
                <p>执行设备<span style="color: red">*</span></p><br>
                <ul id="chooseDevice">
                    <span class="chooseTreeSon1">和谐号分组1</span>
                    <li class="chooseTreeSon">
                        <ul>
                            <li><input name="routeDeviceLists" type="checkbox" value="CRH2A-2010">100002</li>
                            <li><input name="routeDeviceLists" type="checkbox" value="CRH2A-2010">100003</li>
                            <li><input name="routeDeviceLists" type="checkbox" value="CRH2A-2010">100004</li>
                        </ul>
                    </li>
                    <span class="chooseTreeSon1">和谐号分组2</span>
                    <li class="chooseTreeSon">
                        <ul>
                            <li><input name="routeDeviceLists" type="checkbox" value="CRH2C-2061">CRH2A06CH</li>

                        </ul>
                    </li>

                </ul>
                <br>
                <div id="timeRange">
                    <p class="datep" style="display: inline-block">开始时间：<input id="dateinfo" type="text"
                                                                               style="width: 170px"
                                                                               placeholder="请选择" readonly><span
                            style="color: red">*</span></p>
                    <p class="datep" style="display: inline-block">结束时间：<input id="dateinfoEnd" type="text"
                                                                               style="width: 170px"
                                                                               placeholder="请选择" readonly><span
                            style="color: red">*</span></p>
                </div>
                线路：&nbsp;&nbsp;&nbsp;&nbsp;<select name="" id="routeLine">
                <option value="广深线">广深线</option>
                <option value="广珠线">广珠线</option>
            </select><span style="color: red"></span><br>
                开始里程：<input type="text" id="routeBegin" placeholder="请输入数字"
                            onkeyup="value=value.replace(/[^\d]/g,'')"><span style="color: red"></span>
                结束里程：<input type="text" id="routeEnd" placeholder="请输入数字"
                            onkeyup="value=value.replace(/[^\d]/g,'')"><span style="color: red"></span>

            </div>
            <div id="number">
                <input type="checkbox" id="routeSao">扫频 <br>
                <input type="checkbox" id="routeBo">拨测 <br>
                <div id="numberContent" style="display: none">
                    <span style="display: inline-block;width:207px">呼叫号码：</span><input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" id="routeTelePhoneNum"><br>
                    <span style="display: inline-block;width: 200px">等待呼叫接通时长(秒):</span>
                    <select name="" id="routeWaitTime" style="width:130px;">
                        <option value="30">30</option>
                        <option value="15">15</option>
                        <option value="10">10</option>
                    </select><br>
                    <span style="display: inline-block;width: 200px">间隔时长(秒)：</span>

                    <select name="" id="routeIntervalTime" style="width:130px;">
                        <option value="10">10</option>
                        <option value="15">15</option>
                        <option value="30">30</option>
                    </select><br>
                    <span style="display: inline-block;width: 200px">掉话后间隔时长(秒)：</span>

                    <select name="" id="routeCallCut" style="width:130px;">
                        <option value="15">15</option>
                        <option value="10">10</option>
                        <option value="30">30</option>
                    </select><br>
                    <span style="display: inline-block;width: 200px">呼叫保持时长(秒)：</span>

                    <select name="" id="routeCallKeepTime" style="width:130px;">
                        <option value="100">100</option>
                        <option value="80">80</option>
                        <option value="90">90</option>
                    </select><br>
                    <span style="display: inline-block;width: 200px">呼叫失败后间隔时长(秒)：</span>

                    <select name="" id="routeFailTime" style="width:130px;">
                        <option value="15">15</option>
                        <option value="10">10</option>
                        <option value="30">30</option>
                    </select>
                    <span style="display: inline-block;width: 200px"> 循环次数：</span>

                    <select name="" id="routeLimitLoop" style="width:100px;" disabled="disabled">
                        <option value="3">3</option>
                        <option value="2">2</option>
                        <option value="5">5</option>
                    </select><input type="checkbox" id="routeInfinitLoop" style="margin-left: 140px" checked="checked">无限循环
                </div>

                <div id="delete" style="margin-left: 450px">
                    <input type="button" value="保存" id="routeSave">
                    <input type="button" value="修改" id="routeUpdate">
                </div>
            </div>


        </form>
    </div>
</div>
<div class="theme-popover-mask"></div>

</body>
<script src="/Tsme/js/map/ol.js" type="text/javascript"></script>
<script src="/Tsme/js/map/routemaptime.js"></script>
<script>
//    // 自定义分辨率和瓦片坐标系
var resolutions = [];
// var maxZoom = 18;
// // 计算百度使用的分辨率
// for (var i = 0; i <= maxZoom; i++) {
//     resolutions[i] = Math.pow(2, maxZoom - i);
// }
// var tilegrid = new ol.tilegrid.TileGrid({
//     origin: [0, 0],    // 设置原点坐标
//     resolutions: resolutions    // 设置分辨率
// });

// // 创建百度地图的数据源
// var baiduSource = new ol.source.TileImage({
//     projection: 'EPSG:3857',
//     tileGrid: tilegrid,
//     tileUrlFunction: function (tileCoord, pixelRatio, proj) {
//         var z = tileCoord[0];
//         var x = tileCoord[1];
//         var y = tileCoord[2];

//         // 百度瓦片服务url将负数使用M前缀来标识
//         if (x < 0) {
//             x = 'M' + (-x);
//         }
//         if (y < 0) {
//             y = 'M' + (-y);
//         }

//         return "http://online0.map.bdimg.com/onlinelabel/?qt=tile&x=" + x + "&y=" + y + "&z=" + z + "&styles=pl&udt=20160426&scaler=1&p=0";
//     }
// });

//百度地图层
// var baiduMapLayer2 = new ol.layer.Tile({
//     source: baiduSource
// });
// var url = 'http://121.42.251.175:6080/arcgis/rest/services/china/MapServer';
// var mousePositionControl = new ol.control.MousePosition({
//     coordinateFormat: ol.coordinate.createStringXY(4),
//     projection: 'EPSG:4326',
//     className: 'custom-mouse-position',
//     target: document.getElementById('mouse-position'),
//     undefinedHTML: '&nbsp;'
// });
// //创建地图
// var source = new ol.source.Vector({wrapX: false});
// var vector = new ol.layer.Vector({
//     source: source
// });
// var view = new ol.View({
//     // 设置北京为地图中心
//     center: ol.proj.transform([116.319000, 39.896000], 'EPSG:4326', 'EPSG:3857'),
//     zoom: 11
// })

// map = new ol.Map({
// //     layers: [
// //         baiduMapLayer2, vector
// //     ],

// //     controls: ol.control.defaults({
// //         attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
// //             collapsible: false
// //         })
// //     }).extend([mousePositionControl]),
//     controls: ol.control.defaults().extend([
//         new ol.control.ScaleLine({
//             units: 'metric'
//         }),
//     ]),
//     view: view,
//     target: 'mapDiv'
// });
 //arcgis底图
//  var url = 'http://121.42.251.175:6080/arcgis/rest/services/china/MapServer';
 var mousePositionControl = new ol.control.MousePosition({
    coordinateFormat: ol.coordinate.createStringXY(4),
    projection: 'EPSG:4326',
    className: 'routePointLat',
//     target: document.getElementById('mouse-position'),
    undefinedHTML: '&nbsp;'
 });
 var bilichi=new ol.control.ScaleLine({
     units: 'metric'
 })
//  var layers=new ol.layer.Image({
//      //        extent: [-13884991, 2870341, -7455066, 6338219],
//      source: new ol.source.ImageArcGISRest({
//          ratio: 1,
//          params: {},
//          url: url
//      })
//  })
/**
  * 在线天地图
  */
 var tian_di_tu_road_layer = new ol.layer.Tile({
		title: "天地图路网",
		source: new ol.source.XYZ({
			url: "http://t4.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}"
		})
	});

	var tian_di_tu_annotation = new ol.layer.Tile({
		title: "天地图文字标注",
		source: new ol.source.XYZ({
			url: 'http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}'
		})
	});
	/**
	 * 在线高德地图
	 */
		var gaodeMapLayer = new ol.layer.Tile({
		        source: new ol.source.XYZ({
		            url:'http://webrd03.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scale=1&style=8'//高德地图在线
		        }),
		        projection: 'EPSG:3857'
		    })
 var view = new ol.View({
     // 设置北京为地图中心
     center: ol.proj.transform([116.3004070, 39.9755555], 'EPSG:4326', 'EPSG:3857'),
     zoom: 11
 })

 var map = new ol.Map({
     layers: [
    	 gaodeMapLayer
     ],

     controls: ol.control.defaults({
         attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
             collapsible: false
         })
     }).extend([mousePositionControl]),
//      controls: ol.control.defaults().extend([
//          new ol.control.ScaleLine({
//              units: 'metric'
//          }),
//      ]),
     view: view,
     target: 'mapDiv'
 });


// //加载离线地图
//  var view = new ol.View({
//      // 设置北京为地图中心
//      center: ol.proj.transform([116.319000, 39.896000], 'EPSG:4326', 'EPSG:3857'),
//      zoom: 11
//  })

//  var map = new ol.Map({
//      controls: ol.control.defaults().extend([
//          new ol.control.ScaleLine({
//              units: 'metric'
//          }),
//      ]),
//      view: view,
//      target: 'mapDiv'
//  }); 
//map添加wms图层开始121.42.251.175:8000   localhost:8080 geoserver加载的地图
var wmsUrl = '/geoserver/myGis/wms';
//geoserver加载的arcgis作为底图
// var wmsUrl = 'http://121.42.251.175:6080/arcgis/rest/services/china/MapServer';
//添加图层方法
function addwmsLayer(layer, condition) {
    var params = {};
    if (condition.trim() != '') {
        params = {
            'LAYERS': layer,
            'TILED': false,
            'cql_filter': condition
        };
    } else {
        params = {
            'LAYERS': layer,
            'TILED': true
        };
    }
    var Source = new ol.source.ImageWMS({
        name: "wms",
        url: wmsUrl,
        params: params,
        serverType: 'geoserver'
    });

    map.addLayer(new ol.layer.Image({
        extent: [-20037508.34, -20037508.34, 20037508.34, 20037508.34],
        source: Source
    }));
}

//function removeLayer(layerName) {
//    console.log(layerName);
//    map.removeLayer(layerName);
//}

//console.log(addwmsLayer('device_move', ''));
// addwmsLayer('xycMap', '');
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

//获取用户名
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

// function getAccountRole() {
<%-- 	<% --%>
	
// 	String accountRole = "角色:";
// 	if(accountTools.doesCurrentAccountHasSuperadminRole()){
// 		accountRole = accountRole + " 超级管理员";
// 	}
// 	if(accountTools.doesCurrentAccountHasAdminRole()){
// 		accountRole = accountRole + " 管理员";
// 	}
// 	if(accountTools.doesCurrentAccountHasTrainerRole()){
// 		accountRole = accountRole + " 训练员";
// 	}
// 	if(accountTools.doesCurrentAccountHasTesterRole()){
// 		accountRole = accountRole + " 监测员";
// 	}
		
<%--  	%>  --%>
	
<%-- 	var role = <%="\"" + accountRole + "\""%>; --%>
// 	return role;
// }

//获取用户角色
function getAccountRole() {
	<%
	
	String accountRole = "角色:";
	if(accountTools.doesCurrentAccountHasTesterRole()){
		accountRole = " 监测员";
	}
	if(accountTools.doesCurrentAccountHasTrainerRole()){
		accountRole = " 训练员";
	}
	if(accountTools.doesCurrentAccountHasAdminRole()){
		accountRole = " 管理员";
	}
	if(accountTools.doesCurrentAccountHasSuperadminRole()){
		accountRole = " 超级管理员";
	}
		
 	%> 
	
	var role = <%="\"" + accountRole + "\""%>;
	return role;
}



//获取用户角色编码
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
	$("#rightIconImage").hover(function(){
		if(flag == true){
// 			$(".logout").show();
// 			$(".triggle").show();
		}
	},function(){
// 		$(".logout").hide();
// 		$(".triggle").hide();
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
// 		$(".logout").hide();
// 		$(".triggle").hide();
		$("#divAccount").addClass("on");
		if(accountHomepage){
			top.location.href="/Tsme/account/accountHomepage";
		}
	});
	
	getOnlineDeviceList();
}
//双击设备名改变设备名称
function getDeviceName(deviceNum,that){
	if(!$(that).is('.input')){
        $(that).addClass('input').html('<input type="text" value="'+ $(that).text() +'" />').find('input').focus().blur(function(){
            $(this).parent().removeClass('input').html($(this).val() || 0);
            var nameBox=$(this).val();
            $.ajax({
   	         url: "/Tsme/history/updateDeviceName",
   	         type:"GET",
   	         dataType:"json",
//   	         async:false,
   	         data:{"name":nameBox,"deviceNum":deviceNum},
   	        	 success : function(data) {
   	        		 console.log(data);
   	     		},
   	           error:function(data){
   	        	 console.log(data);
   	         }
   	     }) 
        });
    }
}
//单击设备所在行定位设备所在位置
function getDeviceLatLon(lat,lon,that){
	$(that).css("background","#778899").siblings().css("background","");
	var mapCenterNew=ol.proj.transform([lat,lon], 'EPSG:4326', 'EPSG:3857')
	map.getView().setCenter(mapCenterNew);
}

//初始化右边导航栏开始
function init() {
	userLoginFlag();//用于加载帐户状态
	navHover();//用于初始化右边栏样式
	//画基站和小区开始
	//数据
	var pt = new Array();
	//要在模版中显示的参数"${geoMsg.LNG}","${geoMsg.LAT}"
	var ptAttr = new Array();
	var pointBs = new Array();
	var pointDs = new Array();
	<c:forEach items="${geoMsgs}" var="geoMsg" varStatus="status">
		pt["${status.index}"] =  [parseFloat("${geoMsg.LNG}"),parseFloat("${geoMsg.LAT}")];
		ptAttr["${status.index}"] = {"device_num":"${geoMsg.device_num}", "name":"${geoMsg.name}", "BSIC":"${geoMsg.BSIC}", "model":"${geoMsg.model}", "ip":"${geoMsg.ip}", "device_type":"${geoMsg.device_type}", "point_type":"${geoMsg.point_type}", "km_stone":"${geoMsg.km_stone}", "state":"${geoMsg.state}"};
	</c:forEach>
// 	console.log(pt[0][0],pt[0][1]);
	var attr = new Array();
// 	console.log(pt);
	for(var i=0;i<"${fn:length(geoMsgs)}";i++){
	
		attr[attr.length]={
				"device_num":ptAttr[i].device_num, 
				"nam":ptAttr[i].name,
				"BSIC":ptAttr[i].BSIC, 
				"model":ptAttr[i].model, 
				"Xcoord":pt[i][0], 
				"Ycoord":pt[i][1], 
				"ip": ptAttr[i].ip, 
				"device_type":ptAttr[i].device_type, 
				"point_type":ptAttr[i].point_type, 
				"km_stone":ptAttr[i].km_stone,
				"state":ptAttr[i].state	
		};
	}
	
	for(var i=0;i<attr.length;i++){
// 		console.log(attr[i].state)
		
		if(attr[i].point_type=="BS"){
// 			console.log(attr[i].nam);
			pointBs[i] = new ol.Feature({
		         geometry: new ol.geom.Point(ol.proj.transform([attr[i].Xcoord,attr[i].Ycoord], 'EPSG:4326', 'EPSG:3857')),
		         device_type:attr[i].device_type,
		         Xcoord:attr[i].Xcoord,
		         Ycoord:attr[i].Ycoord,
		         ip:attr[i].ip,
		         device_num:attr[i].device_num
		     });
			pointBs[i].setStyle(new ol.style.Style({
				image: new ol.style.Icon({
		            anchor: [0.1, 0.1],
		            src: '/Tsme/images/map/graytower.png'
		        })
		    }));
			
		}else if(attr[i].point_type=="DS"){
// 			console.log(attr[i].nam);
			$("#popup-content").html("<a>地  址：</a>");
			pointDs[i] = new ol.Feature({
		         geometry: new ol.geom.Point(ol.proj.transform([attr[i].Xcoord,attr[i].Ycoord], 'EPSG:4326', 'EPSG:3857')),
		        device_type:attr[i].device_type,
		         Xcoord:attr[i].Xcoord,
		         Ycoord:attr[i].Ycoord,
		         ip:attr[i].ip,
		         device_num:attr[i].device_num
		     });
			if(attr[i].state == "offline"){
// 			console.log(attr[i].nam);
                console.log(attr[i].Xcoord,attr[i].Ycoord);
				var buttOffLine="";
				var htmlOffLine="";
				var iconOffLine="";
				buttOffLine = " &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp离线&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type=\"button\" style=\"height:16px; line-height:10px;color:black; width:30px; padding:0px;\" onclick=\"getDoubbleStatus(" + attr[i].device_num + ")\" value=\"监测\"/>" +  
				"<input type=\"hidden\" value=\"free\"/>";
				iconOffLine = "<span style=\"padding-left:20px; height:18px; line-height:18px; color:white; font-size:12px; font-family:Microsoft YaHei;\">" + attr[i].nam + "</span>" 
				htmlOffLine = "<div style=\"padding-left:20px; height:18px; line-height:18px; color:white; font-size:12px; font-family:Microsoft YaHei;\">" + iconOffLine + attr[i].device_num + buttOffLine + "</div>" 
				$("#onLineListTable").append("<tr style='cursor:pointer;' onclick='getDeviceLatLon(" + attr[i].Xcoord + "," + attr[i].Ycoord + ",this)'>"
	            +"<td>" + attr[i].device_num + "</td>"
	            +"<td class='deviceNameClass' ondblclick='getDeviceName(" + attr[i].device_num + ",this)'>" + attr[i].nam + "</td>"
	            +"<td>离线</td>"
// 	            +"<td><input type=\"button\" style=\"height:18px; line-height:10px;color:black; width:34px; outline：none;padding:0px;\" onclick=\"getDoubbleStatus(" + attr[i].device_num + ")\" value=\"查看\"/></td>"
	            +"<td><span style=\"color:#333;padding:0px;\" onclick=\"getDoubbleStatus(" + attr[i].device_num + ")\">查看</span></td>"
	            +"</tr>"
	            +"<tr>");
				pointDs[i].setStyle(new ol.style.Style({
					image: new ol.style.Icon({
			            anchor: [0.1, 0.1],
			            src: '/Tsme/images/map/grayDevice.png'
			        })
			    }));
			} else if (attr[i].state == "normal"){
		
				pointDs[i].setStyle(new ol.style.Style({
					image: new ol.style.Icon({
			            anchor: [0.1, 0.1],
			            src: '/Tsme/images/map/greenDevice.png'
			        })
			    }));
			}
			
		}
	}
	
	var pointDss=[];
	for(var i=0;i<pointDs.length;i++){
		if(pointDs[i]!=undefined){
			pointDss[pointDss.length]=pointDs[i];
		}
	}
// 	console.log(pointDss);
	 var sourceBs = new ol.source.Vector({
         features: pointBs,
         
	 })
     // 创建一个用于放置图标的layer
     var activityLayerBs = new ol.layer.Vector({
         source: sourceBs
     });
//      map.addLayer(activityLayerBs);
     var sourceDs = new ol.source.Vector({
         features: pointDss,
         
	 })
     // 创建一个用于放置图标的layer
     var activityLayerDs = new ol.layer.Vector({
         source: sourceDs
     });
     map.addLayer(activityLayerDs);
	
// 	var circlePointManys = new ol.Feature({
//          geometry: new ol.geom.Point(ol.proj.transform([116.319000, 39.896000], 'EPSG:4326', 'EPSG:3857')),
//          text:"jizhan1",
//          title:"jz"
//      });
// 	circlePointManys.setStyle(new ol.style.Style({
// 		image: new ol.style.Icon({
//             anchor: [0.5, 1],
//             src: '/Tsme/images/map/graytower.png'
//         })
//     }));
// 	var circlePointManys1 = new ol.Feature({
//         geometry: new ol.geom.Point(ol.proj.transform([116.339000, 39.897000], 'EPSG:4326', 'EPSG:3857')),
//         text:"jizhan2",
//         title:"jz1"
//     });
// 	circlePointManys1.setStyle(new ol.style.Style({
// 		image: new ol.style.Icon({
//            anchor: [0.5, 1],
//            src: '/Tsme/images/map/greentower.png'
//        })
//    }));
// 	 var sourceMov = new ol.source.Vector({
//          features: [circlePointManys,circlePointManys1],
         
// 	 })
//      // 创建一个用于放置图标的layer
//      var activityLayerMov = new ol.layer.Vector({
//          source: sourceMov
//      });
//      map.addLayer(activityLayerMov);
     //画基站和小区结束
  
//      点击图标弹出框设置开始
     //获取id为popup的div标签
     var container = document.getElementById('popup');
     //获取id为popup-content的div标签
     var content = document.getElementById('popup-content');
     //获取id为popup-closer的a标签
     var closer = document.getElementById('popup-closer');

     //初始化一个覆盖层
     var popup = new ol.Overlay({
         //元素内容
         element: container,
         autoPan: true,
         ////覆盖层如何与位置坐标匹配
         positioning: 'bottom-center',
         //事件传播到地图视点的时候是否应该停止
         stopEvent: false,
         autoPanAnimation: {
             //动画持续时间
             duration: 250
         }
     });
     //将覆盖层添加到map中
     map.addOverlay(popup);
     $("#delet").click(function () {
//          console.log(view.getZoom());

     })
     //为要素添加信息的函数
     function addFeatureInfo(info) {
         //创建一个div标签元素
         var elementDiv = document.createElement('div');
         //设置div标签的内容
         setInnerText(elementDiv, info);
         //将div标签加入到内容div标签中
         content.appendChild(elementDiv);
     }

     //设置文本函数
     function setInnerText(element, text) {
         if (typeof element.textContent == 'string') {
             element.textContent = text;
         } else {
             element.innerText = text;
         }
     }

     //为map注册一个单击事件的监听
     map.on('click', function (evt) {
    	 
         //获取坐标点
         var coordinate = evt.coordinate;
         var feature = map.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
             //在视口中遍历所有具有像素颜色的图层，如果图层存在，则返回
             return feature;
         });

         if (feature) {

             //将内容div的内容清空
//              console.log(feature.S.title);
             content.innerHTML ='<div><p>设备编号：'+feature.S.device_num+'</p><p>设备类型：'+feature.S.device_type+'</p><p>经度：'+feature.S.Xcoord+'</p><p>纬度：'+feature.S.Ycoord+'</p><p>IP地址：'+feature.S.ip+'</p><a href="/Tsme/data/warningTemplate/' + feature.S.device_num + '" target="_self">训练预警模型</a><br><a href="javascript:void(0)" onclick="getDoubbleStatus(' + feature.S.device_num + ');">查看实时和历史频谱</a></div>';
//              console.log(feature.S);
             //添加要素信息
//              addFeatureInfo("rxll:" + feature.S.text);
//              addFeatureInfo("title:" + feature.S.title);
//              addFeatureInfo("设备编号:" + feature.S.device_num);
             //如果当前popup覆盖层没有坐标，则设置坐标
             if (popup.getPosition() == undefined) {
                 popup.setPosition(coordinate);
             }
         }
     });

     //为map注册一个pointermove事件的监听
     //pointermove事件
     var element = document.getElementById('popup');
     map.on('pointermove', function (e) {
//         if (e.dragging) {
//             $(element).popover('destroy');
//             return;
//         }
         //获取map的像素位置信息
         var pixel = map.getEventPixel(e.originalEvent);
         //map视口中是否包含某个要素
         var hit = map.hasFeatureAtPixel(pixel);
         //设置符合当前条件的鼠标样式
         map.getTargetElement().style.cursor = hit ? 'pointer' : '';
     });
     view.on('change', function () {
//          console.log(view.getZoom());
     })
     closer.onclick = function () {
         popup.setPosition(undefined);
         closer.blur();
         return false;
     };
//      点击图标弹出框设置结束
	
}


//初始化右边导航栏结束
$(function(){
	init();
	$("#pointCtrol").mouseover(function (){ 
		$(".pointIConBg").addClass("pointIConBgActive");
		$("#pointCtrol").hide();
		//$("#pointCtrol").hide();
		$(".pointWrap").addClass("pointActivese");
       
    }).mouseout(function (){  
    	/* $(".pointIConBg").removeClass("pointIConBgActive");
		$(".pointWrap").removeClass("pointActivese"); */
    });  
	//移除大背景收回样式
	/* $(".pointWrap ").mouseout(function(){
    		console.log(11)
    		$("#rightNav").show();
    		$(".pointIConBg").removeClass("pointIConBgActive");
    		$(".pointWrap").removeClass("pointActivese");
    })
 */
     
	
   //移入每个导航循环显示背景色
    $(".pointIConBox").each(function(){
    	$(this).mouseover(function(){
    		$(this).find(".pointList").addClass("pointActives");
    		$(this).find(".publicDiv").show();
    	});
    	
    	$(this).mouseout(function(){
    		$(this).find(".pointList").removeClass("pointActives");
    		$(this).find(".publicDiv").hide();
    		
    	});
    	
    	
    })
    
	
     

	
	
	
	
	
	
	
	
	/*  $("#setCtrolRoute").mouseover(function (){  
		//$(".pointWrap").toggle(500);
	   	$(".pointIConBg").show(500);
		$(".pointIConBox").show(500);
	})  */
	
	//移入背景添加class
	/* $(".pointIConBox").mouseover(function (){ 
		$(".pointIConBox").removeClass("pointActives");
		$(this).addClass("pointActives");
	});
	
	$(".pointIConBox1").mouseover(function (){ 
		$("#warnDivId").show();
	});
	$(".pointIConBox2").mouseover(function (){ 
		$("#warnDivId").hide();
		$("#setDivIdRoute").show();
	});
	$(".pointIConBox3").mouseover(function (){ 
		$("#setDivIdRoute").hide();
		$("#warnDivId2").show(500);
	}); */
	
	//移除背景全部消失
	/* $(".pointIConBg").mouseout(function (){ 
		$("#warnDivId").hide(200);
		$("#setDivIdRoute").hide(200);
		$(".pointIConBg").hide(200);;
		$(".pointIConBox").hide(200);
	}); */
	
	
	
	
	
	
	
	
	
	//路测离线移入效果
	/* $("#setCtrolRoute").mouseover(function (){ 
		$("#setDivIdRouteText").show();
		$("#setDivIdRoute").css("left","8.9%");
		$("#setCtrolRoute").addClass("pointImgActive");
		
    }).mouseout(function (){  
    	$("#setDivIdRouteText").hide();
		$("#setDivIdRoute").css("left","3.5%");
		$("#setCtrolRoute").removeClass("pointImgActive");
    	
    });  
	
	//扫频离线移入效果
	$("#SweepOffLine").mouseover(function (){ 
		$("#SweepOffLine0").show();
		$("#SweepOffLine").addClass("pointImgActive");
		
    }).mouseout(function (){  
    	$("#SweepOffLine0").hide();
		$("#SweepOffLine").removeClass("pointImgActive");
    	
    });   */
	
	
	
	
	
	
	
	
// 	$("#setCtrol").mouseover(function (){  
//         $("#warnDivId").hide();
//         $("#setDivId").show(); 
//     }).mouseout(function (){  
//         $("warnDivId").hide();  
//         $("#setDivId").hide(); 
//     });  
	/* $(".warnDiv").mouseover(function (){  
        $(this).show();  
    }).mouseout(function (){  
        $(this).hide();  
    });   */
})
</script>
</html>
