<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="utils.AccountTools" %>
<html style="height:100%;width: 100%;">
<head>
    <meta charset="UTF-8">
    <title>GSMR无线监测一体化系统</title>
    <script type="text/javascript" src="/Tsme/js/jQuery/jquery.min.js"></script>
     <script type="text/javascript" src="/Tsme/js/jqueryui/jqueryui.js"></script>
     <link rel="stylesheet" href="/Tsme/css/map/ol.css" type="text/css">
    <script src="/Tsme/js/map/polyfill.js"></script>

    <script src="/Tsme/js/echarts/echarts.min.js"></script>
    <script src="/Tsme/js/jedate/jedate.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/Tsme/css/map/form.css">
    <link rel="stylesheet" type="text/css" href="/Tsme/css/treecss/sitemapstyler.css">
    <link rel="stylesheet" type="text/css" href="/Tsme/css/jqueryui/jqueryui.css">
    <style>
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
            border-top:1px dashed #999;
            padding-top:30px;

        }

        .routeContentStyle .routeNavLeftContent,.routeContentStyle .routeNavLeftBigContent,.routeContentStyle #setDivIdRoute {
            width: 32.5%;
            height: 92%;
            float: left;
            padding: 0.5% 0px 0px 0.8%;
            position: absolute;
            left: 3.5%;
            z-index: 1002;
            /* background-color: #1A567B; */
            background-color:rgb(239,239,239); 

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

        .routeContentStyle .routeDiv .routeDivTop {
            height: 5%;
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

        .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapTitle {
            /*             width: 70.8%; */
            width:94.4%;
            height: 4%;
            background-color: #FAFAFA;
            position: absolute;
            z-index: 997;
            border-bottom: solid 1px #9B9A98;
        }
        .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapTitle select{
            position: absolute;
            top:17%;
            z-index: 997;
        }
        .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapTitle input{
            position: absolute;
            top:17%;
            z-index: 997;
        }
        .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapColorImage{
            position: absolute;
            z-index: 996;
            bottom:1%;
            left:4.5%;
        }
        .routeContentStyle .routeDiv .routeDivBottom .routeMap .mapTitle img {
            position: absolute;
            z-index: 998;
        }
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
            right:13.5%;
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
        
        #sweepBox{
        	width:90%;
        	height:500px;;
        	background:rgb(255,250,250);
        	position:absolute;
        	top:0;
        	left:0;z-index:1002;
        	border:1px solid #ccc;
        	display:block;
        }
        #sweep{
        	background:rgb(255,250,250);
        	position:absolute;
        	top:0;
        	left:0;
        	z-index:1000;
        	width:100%;
        	height:200px;
        }
        .sweepTable{
       		margin:0 5%;
        	width:90%;
        	position: absolute;
		    top: 42%;
		    left: 0;
		    z-index: 1002;
		    border-collapse:collapse;
		    text-align:center;
        }
       
        /* 地图控件设置样式结束 */
    </style>

</head>

<body style="height:100%;width: 100%;">
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

<!--上面标题div开始-->
<div id="routeTitle" class="routeTitleStyle" align="left" style="position:relative">
	<img src="/Tsme/images/map/logo.png" style="display:inline-block;vertical-align: middle;margin:8px 0 0 6px;"/>	
    <span style="font-size:18px;display:inline-block;vertical-align: middle;margin:7px 0 0 10px;position:absolute;top:20%;left:3%;color:white;">无线监测222</span>
</div>
<!--上面标题div结束-->

<!--下面地图和左边栏以及右边栏开始-->
<div id="routeContent" class="routeContentStyle">
    <!--左侧导航栏开始-->
    <div class="routeNavLeft">
        <a href="/Tsme/map/mapIndex">
        	<img src="/Tsme/images/map/button_nav_fixed-device.png" alt="" style="width:29px;height:28px;" id="routePoint">
        </a>
        <a>
        	<img src="/Tsme/images/map/button_nav_offline-data.png" alt="" style="width:29px;height:28px;margin-top:34px;" id="setCtrolRoute">
        </a>
    </div>
    <!--左侧导航栏结束-->
    
    <!--鼠标悬浮到导航栏时开始-->
    <!--设备列表盒子开始 -->
    <div class="routeNavLeftContent" style="display: none">
        <div>
            <select name="" id="deviceTestSele">
                <option value="">设备列表</option>
                <option value="">测试任务</option>
            </select>
        </div>

        <hr>
        <div id="routeNavLeftDeviceList" class="routeNavLeftDeviceListC" style="display: block;">
            <table id="routeDeviceLists" border="1" bordercolor="#a0c6e5"
                   style="border-collapse:collapse;color: white;width: 99%;text-align: center;">
                <tr>
                    <td>测试设备</td>
                    <td>设备状态</td>
                    <td>测试任务</td>
                    <td>发起人</td>
                    <td>实时历史监控播放</td>
                </tr>
            </table>
        </div>
        <div id="routeNavLeftTest">
            <button id="routeEdit">新建任务</button>
            <br>
            <input type="text" placeholder="请输入任务名或创建人" id="searchContent">

            <div style="overflow-y: scroll;height:80%;">
                <table id="taskContentContainter0" border="1" bordercolor="#a0c6e5"
                       style="border-collapse:collapse;color: white;width: 99%;text-align: center;overflow-y: hidden;">

                    <tr id="tableTitle">
                        <td>任务名及状态</td>
                        <td>创建人</td>
                        <td>时间</td>
                        <td>操作</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <!--设备列表盒子结束 -->
    
    <!--离线数据盒子开始 -->
    <div id="setDivIdRoute" style="display:none;">
        <p style="color:#333;margin:0.5%;">离线数据</p>
        
	     <form action="/Tsme/history/simulationOnlineUploadFile" method="post" enctype="multipart/form-data" style="margin-left:44%;">
	            <input type="file" name="file" id="upfile" multiple="multiple"/>
	            <input type="submit" style="margin-left:-27%;" value="提交" onclick="uploadJson()"/>
	     </form>
     	<div style="width:98%;height:87%;color:#333;overflow-y:auto;">

	<br/>
    <hr  style="border-top:1px dashed #333;border-bottom:0px dashed #333" />
    <p style="margin:2% 0 2% 4.7%;">近期数据</p>
     <ul id="sitemap">
            <li><span  id="routeBigDeviceLuJuBig"></span><a href="#" style="color:#333;" id="routeBigDeviceLuJuTextBig">广铁局1</a>
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
            <li><span  id="routeBigDeviceLuJu"></span><a href="#" style="color:#333;" id="routeBigDeviceLuJuText">广铁局1</a>
                <ul id="routeBigDeviceBox" style="display:none;">
                    <li>
                    	<span  id="routeBigDeviceIcon"></span>
                    	<a href="#" style="color:#333;" id="routeBigDeviceIconText">设备002</a>
                    </li>
                </ul>
            </li>
        </ul>

    <!--大小范围盒子结束 -->

    </div>
    </div>
    <!--离线数据盒子结束 -->
    <!--鼠标悬浮到导航栏时结束-->
    <!--地图以及右侧栏开始-->
    <div class="routeDiv">
        <!--左侧上部分开始-->
        <div class="routeDivTop">
            <span style="line-height: 200%;" id="routeDeviceTime"></span><span style="display:none;" id="routeDeviceTimeJia"></span>
            <span class="routePointLat"><span class="routePointLatWord">经纬度：</span></span>
        </div>
        <HR width="99%" color=#987cb9 SIZE=1>
        <!--左侧上部分结束-->
        <!--左侧下部分开始-->
        <div class="routeDivBottom">
            <!--放置地图div开始-->
            <div class="routeMap" id="routeMapId">
                <div class="routeChartTableIcon">
                    <img src="/Tsme/images/map/sweep_ico.png" alt="" style="left: 1%;display:block;" id="sweepChartBtn">
                </div>

                <!--地图-->
                <div class="mapTitle">
                    <span style="margin-left: 1%;margin-top:2%;">参数：</span>
                    <select name="" id="routeParam">
	                    <option value="">Rx Lev</option>
	                    <!--                     <option value="">Rx Qual</option> -->
                    </select>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                    
                    范围：<input type="text" style="width:4%" id="maxRangeLx"> <span style="position:absolute;left:18.3%;top:12%;"> - </span> <input type="text" style="width:4%;left:19%;" id="minRangeLx">&nbsp&nbsp&nbsp
                    <input type="button" value="提交" id="submitRangeLx" style="font-family:'宋体' ;left:24%;">
                    <span style="margin-left:13%;line-height:35px;">路线选择：&nbsp</span><select id="chooseLine" style="line-height:35px;" onchange="chooseLine();">
                    <option value="">请选择</option>
                    <option value="lx">兰新线</option>
                </select>
                    <!-- 					<span><button style="margin-left:5%;" onclick="">兰新线</button></span> -->
                    <span style="margin-left:43%;">路径点时间：<span id="routePointTime"></span></span>
                    <!--                     <input type="button" value="离线暂停" id="startLx" style="font-family:'宋体' "> -->
                </div>
                
                <!--放置柱状图盒子开始-->
                <div class="routeChartBox">
                	<img src="/Tsme/images/map/button_close.png" 
	                	class="routeChartCloseBtn" 
	                	style="cursor:pointer;position:absolute;top:17%;right:10%;z-index:1005;display:block;" 
	                	alt=""
                	>
               		<div class="routeChart" style="display: none;"></div>
               		
               		<!--放置柱状图盒子外框开始-->
               		<div id="sweepBox">
               			<!-- 柱状图echarts -->
               			<div id="sweep"></div>
               			<table border="1" bordercolor="#ccc" class="sweepTable" id="sweepTable"> 
               				<tr>
               					<td>canshu</td>
               					<td>canshu2</td>
               					<td>canshu</td>
               					<td>canshu2</td>
               				</tr>
               				<tr>
               					<td>canshu3</td>
               					<td>canshu4</td>
               					<td>canshu3</td>
               					<td>canshu4</td>
               				</tr>
               				
               			</table>
               		</div>
               		<!--放置柱状图盒子外框结束-->
                </div>
                <!--放置柱状图盒子结束-->
                
                <!--放置地图上表格的盒子开始-->
                <div class="routeTable" style="overflow-y: auto;display: none;">
<!--                     <span class="routeTableCloseBtn" style="width:2%;height:4.5%;vertical-align: middle;text-align: center;cursor:pointer;position:absolute;top:1%;left:97.6%;z-index:1001;display:none;background-color:white;"> x</span> -->
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
                <!--放置地图上路径点颜色示意图开始 -->
                <div class="mapColorImage">
                    <img src="/Tsme/images/map/routepointcolor.png" alt="" style="left: 1%; width:40%;">
                </div>
                <!--放置地图上路径点颜色示意图结束 -->
            </div>
            <!--放置地图div结束-->
            
        </div>
        <!--左侧下部分结束-->
        
    </div>
    <!--地图以及右侧栏结束-->
</div>
<!--下面地图和左边栏以及右边栏-->

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
<!-- <script src="/Tsme/js/map/routemap.js"></script> -->
<script src="/Tsme/js/map/sweepmaptime.js"></script>
<script>
    function getAccountName() {
        <%
    AccountTools accountTools= new AccountTools();
    String	accountName = accountTools.getCurrentAccountName();

    %>

    var name = <%="\"" + accountName + "\""%>;
    return name;
    }
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
        accountRole = "超级管理员";
    }

    %>

    var role = <%="\"" + accountRole + "\""%>;
    return role;
    }
//     showOnlineMap(strYearMonthDayParam,timeStrParam,getAccountName());
    function uploadJson(){
        $('#routeMaskParent').css("display", "block");

    }
    //选择线路
    function chooseLine(){
        var lineName = $("#chooseLine").val()
        if(lineName=="lx"){
            addwmsLayer('movelinestation', "");
            addwmsLayer('movelinedetail', "");
            addwmsLayer('linetostation', "");
            var lineCenter=ol.proj.transform([95.61318101,40.17854835], 'EPSG:4326', 'EPSG:3857')
            map.getView().setCenter(lineCenter);
            map.getView().setZoom(6);
        }else{
            removeLayers("linetostation");
            removeLayers("movelinedetail");
            removeLayers("movelinestation");
        }
    }
    //基站展示开始
    //  var view = new ol.View({
    //             // 设置北京为地图中心
    //             center: ol.proj.transform([116.319000, 39.896000], 'EPSG:4326', 'EPSG:3857'),
    //             zoom: 11
    //         })

    //         map = new ol.Map({

    // //            controls: ol.control.defaults({
    // //                attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
    // //                    collapsible: false
    // //                })
    // //            }).extend([mousePositionControl]),
    //             controls: ol.control.defaults().extend([
    //                 new ol.control.ScaleLine({
    //                     units: 'metric'
    //                 }),
    //             ]),
    //             view: view,
    //             target: 'routeMapId'
    //         });
    var pt = new Array();
    //要在模版中显示的参数"${geoMsg.LNG}","${geoMsg.LAT}"
    var ptAttr = new Array();
    var pointBs = new Array();
    var pointDs = new Array();
    <c:forEach items="${geoMsgs}" var="geoMsg" varStatus="status">
    pt["${status.index}"] =  [parseFloat("${geoMsg.LNG}"),parseFloat("${geoMsg.LAT}")];
    ptAttr["${status.index}"] = {"device_num":"${geoMsg.device_num}", "name":"${geoMsg.name}","type":"${geoMsg.type}", "BSIC":"${geoMsg.BSIC}", "model":"${geoMsg.model}", "ip":"${geoMsg.ip}", "device_type":"${geoMsg.device_type}", "point_type":"${geoMsg.point_type}", "km_stone":"${geoMsg.km_stone}", "state":"${geoMsg.state}"};
    </c:forEach>
    //	console.log(pt[0][0],pt[0][1]);
    var attr = new Array();
    //	console.log(pt);
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
            "state":ptAttr[i].state,
            "type":ptAttr[i].type
        };
    }
    // console.log(attr);
    for(var i=0;i<attr.length;i++){
// 		console.log(attr[i].type);

        if(attr[i].point_type=="BS"){
// 		console.log(attr[i].nam);
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

        }else if(attr[i].point_type=="DS"&&attr[i].type==2){
// 		console.log(attr[i].nam);
            $("#popup-content").html("<a>地  址：</a>");
            $("#routeDeviceLists").append(
                    '<tr>'
                    + '<td>' + attr[i].device_num + '</td>'
                    + '<td>测试中</td>'
                    + '<td>LI-08-广C</td>'
                    + '<td>赵一</td>'
                    + '<td>' +
                    '<button>实时播放</button>' +
                    '<button style="margin:0 5px">历史播放</button>' +
                    '<button>暂停</button>' +
                    '</td>'
                    + '</tr>'
            )

// 		pointDs[i] = new ol.Feature({
// 	         geometry: new ol.geom.Point(ol.proj.transform([attr[i].Xcoord,attr[i].Ycoord], 'EPSG:4326', 'EPSG:3857')),
// 	        device_type:attr[i].device_type,
// 	         Xcoord:attr[i].Xcoord,
// 	         Ycoord:attr[i].Ycoord,
// 	         ip:attr[i].ip,
// 	         device_num:attr[i].device_num
// 	     });
// 		if(attr[i].state == "offline"){
// 		console.log(attr[i].nam);
// 			var buttOffLine="";
// 			var htmlOffLine="";
// 			var iconOffLine="";
// 			buttOffLine = " &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp离线&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type=\"button\" style=\"height:16px; line-height:10px;color:black; width:30px; padding:0px;\" onclick=\"getDoubbleStatus(" + attr[i].device_num + ")\" value=\"监测\"/>" +
// 			"<input type=\"hidden\" value=\"free\"/>";
// 			iconOffLine = "<span style=\"padding-left:20px; height:18px; line-height:18px; color:white; font-size:12px; font-family:Microsoft YaHei;\">" + attr[i].nam + "</span>"
// 			htmlOffLine = "<div style=\"padding-left:20px; height:18px; line-height:18px; color:white; font-size:12px; font-family:Microsoft YaHei;\">" + iconOffLine + attr[i].device_num + buttOffLine + "</div>"
// 			$("#offLineList").append(htmlOffLine);
// 			pointDs[i].setStyle(new ol.style.Style({
// 				image: new ol.style.Icon({
// 		            anchor: [0.1, 0.1],
// 		            src: '/Tsme/images/map/grayDevice.png'
// 		        })
// 		    }));
// 		} else if (attr[i].state == "normal"){

// 			pointDs[i].setStyle(new ol.style.Style({
// 				image: new ol.style.Icon({
// 		            anchor: [0.1, 0.1],
// 		            src: '/Tsme/images/map/greenDevice.png'
// 		        })
// 		    }));
        }
    }


    var pointBss=[];

    for(var i=0;i<pointBs.length;i++){
        if(pointBs[i]!=undefined){
            pointBss[pointBss.length]=pointBs[i];
        }
    }

    var sourceBs = new ol.source.Vector({
        features: pointBss,

    })
    // 创建一个用于放置图标的layer
    //  var activityLayerBs = new ol.layer.Vector({
    //      source: sourceBs
    //  });

    //  map.addLayer(activityLayerBs);
    //基站展示结束
    //控制基站和小区显示隐藏开始
    $("#routeJZ").change(function () {
        if ($("#routeJZ").is(':checked')) {
            map.addLayer(activityLayerBs);
//                 map.addLayer(activityLayerMove3);
        } else {
            map.removeLayer(activityLayerBs);
//                 map.removeLayer(activityLayerMove3);
        }
    })
    //控制基站和小区显示隐藏结束
    // $("tr:eq(0)").css({color:"red", fontWeight:"bold"});
</script>

</html>
