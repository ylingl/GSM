<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<link rel="stylesheet" href="/Tsme/css/spectra/chart.css" />

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>

</head>
<body>
	<div style="min-width:1024px; width:100%; text-align:center;">
		<label style="font-size:20px;">实时告警界面</label>
		<input type="hidden" name="templateId" value="${templateId}"/>
		<input type="hidden" name="employ" value="${employ}"/>
	</div>
	<div style="margin-left:20px; margin-top:10px;">
		<form name="deviceParaForm" id="deviceParaForm" action="" onsubmit="submitForm()" style="font-size:10px;">
			<input type="hidden" name="deviceNum" value="${deviceNum}" /> 
			<input type="hidden" name="interval" value="500"/>
			<button type="button" id="startButton" onclick="startMonitor()" style="color:green;display:none;">开始监控</button>
			<button type="button" id="stopButton" onclick="stopMonitor()" style="color:red;display:none;">停止监控</button>
		</form>
	</div>
	
	<div id="content0" class="content">
		<div id="chart0" class="chart_spline"></div>
		<div id="warningTable0" class="warningTable">
			<div id="warningTitle0" class="warningTitle">告警信息列表</div>
			<div id="warningHead0" class="warningHead">
				<span id="warningN0" class="warningN" style="width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">序号</span>
				<span id="warningStart0" class="warningStart" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">起始频率</span>
				<span id="warningStop0" class="warningStop" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">终止频率</span>
				<span id="warningCenter0" class="warningCenter" style="width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">中心频率</span>
				<span id="warningNum0" class="warningNum" style="width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">告警数</span>
				<span id="warningT0" class="warningT" style="width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;">最新告警时间</span>
				<span id="warningD0" class="warningD" style="width:12%; display:block; border-bottom:1px solid #F00;float:left;">详细信息</span>
			</div>
			<div id="warningData0" class="warningData" >
			</div>
		</div>
	</div>	
	
<!-- 弹出部分 -->
	<div class="modal fade in" id="demodModal" tabindex="-1" role="dialog" aria-labelledby="demodModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="demodModalLabel">解调结果</h4>
				</div>
				<div class="modal-body">
				<P></P>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade in" id="detailModal" tabindex="-1" role="dialog" aria-labelledby="detailModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content" style="width:600px;">
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
<script type="text/javascript" src="/Tsme/js/spectra/chart.js"></script>
</body>
</html>
