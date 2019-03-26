<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>预警模板训练</title>

<link rel="stylesheet" href="/Tsme/css/public.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" href="/Tsme/css/bootstrap/style.css" />

<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/layer/layer.js"></script>
<script type="text/javascript" src="/Tsme/js/bootstrap/bootstrap.min.js"></script>

</head>
<body>
<!-- 头部模板 -->
<input type="hidden" name="baseRow" id="baseRow" value="4">
<input type="hidden" name="peakRow" id="peakRow" value="9">
<div style="min-width:1024px; font-size:12px" id="topDiv">
	<div id="drawButton" style="width:100%;height:26px;padding:10px;">
		<span style="float:left;">	
			<button type="button" id="btnBegin" data-toggle="modal" data-target="#editmodel">开始采集</button>&nbsp;
			<button type="button" id="btnEnd" onclick="stopMonitor()">停止采集</button>&nbsp;
			<button type="button" id="btnCreateAvg" onclick="trainTemplate()">训练预警模板</button>
		</span>
		<span style="float:right">
			历史预警模板：
			<select name="warningTemplateSelect" id="warningTemplateSelect"></select> 
			<button type="button" id="btnView" onclick="showTemplateDetail(false)">查看</button>&nbsp;
			<button type="button" id="btnDel" onclick="deleteTemplate()">删除</button>
		</span>
	</div>
</div>
<!-- 图形显示部分 -->
<br/>
<div id="container" style="min-width:1024px;width:100%;">
	<div id="chart0" style="height:400px;width:100%;">
		<span style="display:block; text-align:center; line-height:400px; color:blue; font-size:20px">点击“开始采集”启动训练，选择“历史预警模板”查看模板记录。</span>
	</div>
</div>

<!-- 底部操作部分 -->
<div style="width:100%;padding:10px;min-width:1024px;" id="bottomDiv">
	<div style="font-size:12px; float:right;"></div>
	<form name="warningLineForm" id="warningLineForm" action="" style="font-size:12px; float:left;">
		选择基准线：
		<label><input name="baseline" type="radio" value="1" />曲线1 </label> 
		<label><input name="baseline" type="radio" value="2" />曲线2 </label> 
		<label><input name="baseline" type="radio" value="3" />曲线3 </label> 
		<label><input name="baseline" type="radio" value="4" checked/>曲线4 </label> 
		<label><input name="baseline" type="radio" value="5" />曲线5 </label> 
		<label><input name="baseline" type="radio" value="6" />曲线6 </label> 
		<label><input name="baseline" type="radio" value="7" />曲线7 </label> 
		<label><input name="baseline" type="radio" value="8" />曲线8 </label> 
		<label><input name="baseline" type="radio" value="9" />曲线9 </label>
		<br/>
		 
		选择浮动线：
		<label><input name="peakline" type="radio" value="1" />曲线1 </label> 
		<label><input name="peakline" type="radio" value="2" />曲线2 </label> 
		<label><input name="peakline" type="radio" value="3" />曲线3 </label> 
		<label><input name="peakline" type="radio" value="4" />曲线4 </label> 
		<label><input name="peakline" type="radio" value="5" />曲线5 </label> 
		<label><input name="peakline" type="radio" value="6" />曲线6 </label> 
		<label><input name="peakline" type="radio" value="7" />曲线7 </label> 
		<label><input name="peakline" type="radio" value="8" />曲线8 </label> 
		<label><input name="peakline" type="radio" value="9" checked/>曲线9 </label>
		<br/>
		<button type="button" id="btnCreateLine" onclick="createWarningLine()">生成预警曲线</button>&nbsp;
		<button type="button" id="btnModifyLine" data-toggle="modal" data-target="#modifyLine">修改预警曲线</button>
		<!-- 特征线编号：<input name="rowNumForPeak" type="text" value="" style="width:30px"/>  -->
		<button type="button" id="btnDemodulation" onclick="viewPointAndDemod()">开始解调</button>
	</form>
	
<!--开始监测弹出部分 -->
	<div class="modal fade in" id="editmodel" tabindex="-1"
		role="dialog" aria-labelledby="editmodelLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="editmodelLabel">设置设备参数</h4>
				</div>
				<div class="modal-body">
					<form id="submitdata" action="/Tsme/data/startRecord">
						<input type="hidden" name="deviceNum" id="deviceNum" value="${currentDeviceNum}"/>
						<input type="hidden" name="clientId" value="${currentClientId}"/>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">模板名称：</label> 
							<input type="text" class="form-control form-width" name="template_name" id="template" value=""/>
						</div>
						
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">第一组起始频率(MHz)：</label>
							<input class="form-control form-width" name="startFrequency0" id="startFrequency0" value=""/>
						</div>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">第一组终止频率(MHz)：</label>
							<input class="form-control form-width" name="stopFrequency0" id="stopFrequency0" value=""></input>
						</div>
						
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">第二组起始频率(MHz)：</label>
							<input class="form-control form-width" name="startFrequency1" id="startFrequency1" value=""/>
						</div>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">第二组终止频率(MHz)：</label>
							<input class="form-control form-width" name="stopFrequency1" id="stopFrequency1" value=""></input>
						</div>
						
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">第三组起始频率(MHz)：</label>
							<input class="form-control form-width" name="startFrequency2" id="startFrequency2" value=""/>
						</div>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">第三组终止频率(MHz)：</label>
							<input class="form-control form-width" name="stopFrequency2" id="stopFrequency2" value=""></input>
						</div>

						<input class="form-control form-width" name="fftSize" id="fftSize" value="8192" type="hidden"></input>
						<input class="form-control form-width" name="bandWidth" id="bandWidth" value="20" type="hidden"></input>
						<input class="form-control form-width" name="maxMeans" id="maxMeans" value="5000" type="hidden"></input>

						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<button type="button" class="btn btn-primary" onclick="presubmit();">提交</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
<!-- 开始监测结束 -->
<!-- 修改弹出部分 -->
	<div class="modal fade in" id="modifyLine" tabindex="-1" role="dialog" aria-labelledby="editmodelLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="editmodelLabel">修改预警参数</h4>
				</div>
				<div class="modal-body">
					<form id="modifyForm" action="">
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">曲线组别：</label>
							<label for="recipient-name" class="control-label label-width" id="warningLineGroup"></label>
						</div>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">起始频率：</label>
							<input type="text" class="form-control form-width" name="startFrequency" id="startFrequency" value=""/>
						</div>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">终止频率：</label>
							<input class="form-control form-width" name="stopFrequency" id="stopFrequency" value=""></input>
						</div>
						<div class="form-group">
							<label for="recipient-name" class="control-label label-width">阈值：</label>
							<input class="form-control form-width" name="threshold" id="threshold" value=""></input>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<button type="button" class="btn btn-primary" onclick="alterPresubmit()">提交</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
<!-- 修改弹出部分结束 -->

<!-- 解调弹出部分 -->
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
<!-- 解调弹出部分结束 -->

</div>
<script type="text/javascript" src="/Tsme/js/chart/highcharts.js"></script>
<script type="text/javascript" src="/Tsme/js/chart/highcharts-more.js"></script>
<script src="/Tsme/js/public/js_patch.js"></script>
<script src="/Tsme/js/data/template.js"></script>
</body>
</html>