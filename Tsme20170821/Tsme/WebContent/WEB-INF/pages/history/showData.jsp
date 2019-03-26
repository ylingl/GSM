<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="description" content="This is my page">

<title>实时监控</title>

<link rel="stylesheet" href="/Tsme/css/public.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/style.css" />
<link rel="stylesheet" href="/Tsme/css/history/showData.css" />
<link rel="stylesheet" href="/Tsme/css/jQuery-ui/jquery-ui.css">

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/Tsme/js/ws/sockjs.min.js"></script>
<script type="text/javascript" src="/Tsme/js/ws/sockjs.min.js.map"></script>

</head>
<body onselectstart="return false;" style="-moz-user-select:none;overflow:-Scroll;overflow-x:hidden">
	<div style="min-width:1024px; width:100%; text-align:center; height:30px;">
		<label style="font-size:20px;">${deviceNum}的历史监测数据</label>
		<input type="hidden" name="deviceNum" id="deviceNum" value="${deviceNum}"/>
	</div>
	<div style="padding:10px; width:100%; height:40px;">
		<form name="deviceParaForm" id="deviceParaForm" action="" onsubmit="submitForm()" style="font-size:10px;">
			<div style="float:left;">
				请选择查询日期：<select id="dateSelect"><option value="default">请选择</option></select>&nbsp;
			</div>
			<div id="templateDiv" style="float:left;">&nbsp;</div>
			<div id="frequencyDiv" style="float:left;">&nbsp;</div>
			<div id="fileDiv" style="float:left;">&nbsp;</div>
			<button type="button" id="showButton" onclick="show()" style="display:none; height:20px; line-height:12px;">回放频谱数据</button>
			<button type="button" id="demodButton" onclick="alarm()" style="display:none; height:20px; line-height:12px;">查看告警统计</button>
		</form>
	</div>
	<div style="clear:both"></div>
	<div id="content0" style="float:left">
		<div id="chartS" class="chart_spline" style="display:none;height:300px">
			<span style="display:block; text-align:center; line-height:300px; color:red; font-size:20px">统计图加载中，请稍候！</span>
		</div>
		<div id="chart0" class="chart_spline">
			<span style="display:block; text-align:center; line-height:400px; color:red; font-size:20px">请选择查询条件！</span>
		</div>
	</div>
	<div id="warningTable0" class="warningTable">
		<div id="warningTitle0" class="warningTitle">告警信息列表</div>
		<div id="warningHead0" class="warningHead">
			<span id="warningN0" class="warningN" style="width:8%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">序号</span>
			<span id="warningStart0" class="warningStart" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">起始频率</span>
			<span id="warningStop0" class="warningStop" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">终止频率</span>
			<span id="warningCenter0" class="warningCenter" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">中心频率</span>
			<span id="warningNum0" class="warningNum" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">告警数</span>
			<span id="warningT0" class="warningT" style="width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">最新告警时间</span>
			<span id="warningD0" class="warningD" style="width:12%; display:block; border-bottom:1px solid #F00;float:left;">详细信息</span>
		</div>
		<div id="warningData0" class="warningData"></div>
	</div>
	<div style="clear:both"></div>
	<div style="text-align:center; padding-top:5px; height:100px; background-color: #6c6e69;">
		<div id="slider" style="width:98%; margin:0 auto;display:none;"></div>
		<div style="width:100%; display:inline-block">
			<div style="width:25%;padding:6px;float:left;text-align:left;color:#FFF">
				回放进度：<span id="current">0</span>/<span id="totalNum">0</span><br>
				告警时间：<span id="createTime">0</span><br>
				地理位置：<span id="LNG">0</span>&#176E，<span id="LAT">0</span>&#176N<br>
			</div>
			<div id="panel" style="width:50%;padding:6px;float:left;display:none;">
				<a class="prev" title="播放前一帧数据"></a>
				<a class="control start" title="开始/暂停"></a>
				<a class="next" title="播放后一帧数据"></a>
			</div>
		</div>
	</div>
	
	
<!-- 弹出部分 -->
	<div class="modal fade in" id="originalDemodMode" tabindex="-1" role="dialog" aria-labelledby="originalDemodModelLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="originalDemodModelLabel">频点原始数据</h4>
				</div>
				<div class="modal-body" id="originalDemodData"></div>
			</div>
		</div>
	</div>
	
	<div class="modal fade in" id="editmode1" tabindex="-1" role="dialog" aria-labelledby="editmodel1Label">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="editmodelLabel">解调结果</h4>
				</div>
				<div class="modal-body">
					<p style="color:red">存在同频干扰！
					<div style="float:left; padding-right:10px">
					<P>
						L3DecoderResult<br/>
						System Information Type 3:<br/>
						Cell Identity:<br/>
						CIValue: 311, 0x137<br/>
						Location Area Identification:<br/>
						Mobile Country Code: 4 6 0<br/>
						Mobile Network Code: 0 0<br/>
						Location Area Code: 4138, 0x102A<br/>
						BCCH NUMBER: 92<br/>
						Max. TX power level: 5<br/>
						14 dB RXLEV hysteresis for cell re-selection<br/>
						Min. received signal level: 13<br/>
						Cell Bar Qualify: 0<br/>
						Cell Reselect Offset: 110 dB<br/>
						Temporary Offset: 0 dB<br/>
						Penalty Time: 20 s<br/>
					</P>
					</div>
					<div>
					<P style="color:red">
						L3DecoderResult<br/>
						System Information Type 3:<br/>
						Cell Identity:<br/>
						CIValue: 51883, 0xCAAB<br/>
						Location Area Identification:<br/>
						Mobile Country Code: 4 6 0<br/>
						Mobile Network Code: 0 0<br/>
						Location Area Code: 4335, 0x10EF<br/>
						BCCH NUMBER: 87<br/>
						Max. TX power level: 5<br/>
						14 dB RXLEV hysteresis for cell re-selection<br/>
						Min. received signal level: 13<br/>
						Cell Bar Qualify: 0<br/>
						Cell Reselect Offset: 90 dB<br/>
						Temporary Offset: 0 dB<br/>
						Penalty Time: 20 s<br/>
					</P>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade in" id="statisticDemodMode" tabindex="-1" role="dialog" aria-labelledby="statisticDemodLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="statisticDemodLabel">频点统计数据</h4>
				</div>
				<div class="modal-body" id="statisticDemodData"></div>
			</div>
		</div>
	</div>
	
	<div class="modal fade in" id="detailModal" tabindex="-1" role="dialog" aria-labelledby="detailModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="detailModalLabel">详细预警信息</h4>
				</div>
				<div id="content1" class="content">
					<div id="warningTableJ" class="warningTable">
						<div id="warningTitleJ" class="warningTitle">告警信息列表</div>
						<div id="warningHeadJ" class="warningHead">
							<span id="warningNJ" class="warningN" style="width:10%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">序号</span>
							<span id="warningStartJ" class="warningStart" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">起始频率</span>
							<span id="warningStopJ" class="warningStop" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">终止频率</span>
							<span id="warningCenterJ" class="warningCenter" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">中心频率</span>
							<span id="warningNumJ" class="warningNum" style="width:8%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">告警数</span>
							<span id="warningTJ" class="warningT" style="width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">最新告警时间</span>
							<span id="warningDJ" class="warningD" style="width:14%; display:block; border-bottom:1px solid #F00;float:left;">告警类型</span>
						</div>
						<div id="warningDetail" class="warningData" >
						</div>
					</div>
				</div>
				<div>&nbsp;</div>
			</div>
		</div>
	</div>
<!-- 弹出部分结束 -->
	<script type="text/javascript" src="/Tsme/js/chart/highcharts.js"></script>
	<script type="text/javascript" src="/Tsme/js/chart/highcharts-more.js"></script>
	<script src="/Tsme/js/public/js_patch.js"></script>
	<script src="/Tsme/js/history/showData.js"></script>
</body>
</html>
