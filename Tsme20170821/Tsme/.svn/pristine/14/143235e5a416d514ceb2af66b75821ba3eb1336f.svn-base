<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实时监控</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="description" content="This is my page">
<style>
body, div, input {
	margin: 0;
	padding: 0;
}
.title1{
	text-align: center;
	font-size: 30px;
	margin-top:10px;
	margin-bottom:10px;
}
.title2{
	text-align: left;
	font-size: 20px;
	margin-left:5px 10px;
}
#chart_parameter {
	margin: auto;
	width: 96%;
	height:250px;
}

#chart_parameter form {
	text-align: left;
	margin: auto;
	line-height: 38px;
	padding-left: 40px;
}

#chart_parameter input {
	width: 7%;
	margin-right: 10px;
}

#content {
	margin: auto;
	padding-left: 20px;
	padding-right: 20px;
}

#chart_spline {
	text-align: center;
	pointer-events: none;
	float: left;
}

#chart_right {
	float: right;
	padding-left:5px;
}

#chart_right button {
	display: block;
	margin-top:50px;
}

#chart_bottom {
	height:20px;
	margin-top:10px;
	text-align: center;
	clear: both;
}

#loading_div {
	width: 32px;
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
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery.include.js"></script>
<script type="text/javascript">
	$.includePath = "<%=basePath%>";
	$.include(['data/20150116001.js',
	           'data/20150116002.js',
	           //'data/20150116003.js',
	           //'data/20150116004.js',
	           //'data/20150116005.js',
	           'data/20150116006.js']);
</script>


<!-- 引用的文件 -->
<script type="text/javascript" src="/Tsme/js/chart/highcharts.js"></script>
</head>
<body>
	<div id="chart_parameter">
		<div class="title1">频谱实时动态监控图</div>
		<div class="title2">当前设备地理位置：${deviceDesc}</div>
		<div class="title2">设置实时检测参数：</div>
		<form name="deviceParaForm" id="deviceParaForm" action=""
			onsubmit="submitForm()">
			
			<select name="deviceId" id="deviceId" onchange="changeDevice()">
				<option value="device01">第一个点</option>
				<option value="device02">第二个点</option>
				<option value="device03">第三个点</option>
			</select> 
			startFrequency:<input type="text" name="startFrequency" value="0" /> 
			stopFrequency:<input type="text" name="stopFrequency" value="1000" /> 
			maxMeans:<input type="text" name="maxMeans" value="50" /> 
			bandWidth:<input type="text" name="bandWidth" value="20" /> 
			fftSize:<input type="text" name="fftSize" value="2" />
			<!-- br/> 请选择监控点 刷新间隔毫秒数：（测试用）  是否模拟 -->
			<input type="text" name="clientId" value="abc" />
			<input type="text" name="interval" value="300" />
			<input type="text" name="simulate" value="false" />
			<button type="button" onclick="startMonitor()">查看</button>
			<button type="button" onclick="stopMonitor()">停止查看</button>
			<br/>
			<button type="button" onclick="getJsFileList()">获取文件列表</button>
			<span id="jsFileListSpan"></span>
		</form>
	</div>
	
	<div id="loading_div"></div>
	<div id="content">
		<div id="chart_spline" class="chart_spline"></div>
		<div id="chart_right" class="chart_right"><button type="button" class="add">增加</button>
			<button type="button" class="reduce">减少</button>
		</div>
	</div>
	<div id="chart_bottom" class="chart_bottom"><button type="button" class="amplification">放大</button>
		<button type="button" class="narrow">缩小</button>
	</div>
	
	
	<script type="text/javascript">
		var chart;
		var startFrequency = null;
		var stopFrequency = null;
		var maxMeans = 500;
		var bandWidth = 10;
		var fftSize = 20;//表示点的个数
		var point_padding = 3;
		var ymin=null;
		var ymax=null;
		var ytick=null;
		var xtick=null;
		$(function() {
			adjust();
			hightchart();
			numberchange();
			zoomchange();
		});
		/*为图书右侧高度自适应*/
		function adjust(){
			var contentHeight = $(window).height()-250-20-50;
			$('#content').css('height',contentHeight);
			$('#chart_spline').css('height',contentHeight);
			$('#chart_spline').css('width',$(window).width()-120);
			$('#chart_right').css('height',contentHeight);
		}
		function hightchart() {
			chart = new Highcharts.Chart({
				chart : {
					backgroundColor : '#302f2f',
					renderTo : 'chart_spline', //图表放置的容器，DIV 
					defaultSeriesType : 'scatter', //图表类型为折线图 
					zoomType : 'xy',
					events : {
						load : function() {
							var series = this.series[0];
						}
					}
				},
				title : {
					text : '' //图表标题 
				},
				xAxis : { //设置X轴 
					max : stopFrequency, //x轴最大值 
					min : startFrequency,
					gridLineColor : '#999',
					gridLineWidth : 1,
					//tickPixelInterval : 100, //X轴标签间隔 
					tickInterval: xtick,
					title : {
						text : 'Frequency[MHz]'
					}
				},
				yAxis : { //设置Y轴 
					title : '占有百分比',
					max : ymax, //Y轴最大值 
					min : ymin, //Y轴最小值 
					//tickPixelInterval : 100,//y轴标签间隔 
					tickInterval:ytick,
					title : {
						text : 'Level[dBm]'
					}
				},
				legend : {
					enabled : false, //设置图例不可见 
					backgroundColor : '#fff'
				},
				credits : {
					text : '频谱图' //设置LOGO区文字 
				},
				series : [ {
					lineWidth : 1,
					color : '#f90707',
					marker : {
						enabled : false,
					},
					pointInterval : point_padding,
					data : (function() { //设置默认数据， 
						//return initData(); 
					})()
				} ]
			});
		}
		function initData() {
			var data = [], time = (new Date()).getTime(), i;
			for (i = 0; i <= 10; i++) {
				data.push({
					//x: (i+Math.random())*10,  
					y : Math.random() * 10
				});
			}
			return data;
		}
		function numberchange(){
			$('.add').click(function(){
				ymax+=ytick;
				ymin-=ytick;console.log(ymax+"  "+ymin);
				hightchart();
			});
			$('.reduce').click(function(){
				ymax-=ytick;
				ymin+=ytick;console.log(ymax+"  "+ymin);
				hightchart();
			});
		}
		function zoomchange(){
			 $('.amplification').click(function(){
				 startFrequency=parseInt(startFrequency)-parseInt(xtick);
				 stopFrequency=parseInt(stopFrequency)+parseInt(xtick);
				 hightchart();
			 });
	         $('.narrow').click(function(){
	        	 startFrequency=parseInt(startFrequency)+parseInt(xtick);
				 stopFrequency=parseInt(stopFrequency)-parseInt(xtick);
				 hightchart();
			 });
		 }
		var moniterTimer = null;
		var isReadyTimer = null;
		var interval = 300;//刷新间隔频率，毫秒
		function startMonitor() {
			if ($('input[name=interval]').val() == "") {
				interval = 300;
			} else {
				interval = $('input[name=interval]').val();
			}
			if ($('input[name=startFrequency]').val() == "") {
				startFrequency = 0;
			} else {
				startFrequency = $('input[name=startFrequency]').val();
			}
			if ($('input[name=stopFrequency]').val() == "") {
				stopFrequency = 1000;
			} else {
				stopFrequency = $('input[name=stopFrequency]').val();
			}
			if ($('input[name=maxMeans]').val() == "") {
				maxMeans = 500;
			} else {
				max = $('input[name=maxMeans]').val();
				maxMeans = parseInt(1 / max * 1000);
			}
			fftSize = $('input[name=fftSize]').val();
			
			if (moniterTimer != null)
				return;
			$.ajax({
				url : "./spectra/realTimeMonitorStart",
				type : "POST",
				async : true,
				data : $("#deviceParaForm").serialize(),
				dataType : 'json',
				success : function(data) {
					if (data.result) {
						//chart.series[0].remove();
						//$("#loading_div").html("装载中………………");
						$("#loading_div").show();
						var deviceId = $("#deviceId option:selected").val();
						isReadyTimer = setInterval(function() {
							isMonitorDataReady(deviceId);
						}, 200);
					}
				}
			})
		}
		function isMonitorDataReady(id) {
			$.ajax({
				url : "./spectra/realTimeMonitorIsReady",
				type : "POST",
				data : {"deviceId":id,"simulate":$('input[name=simulate]').val()},
				dataType : 'json',
				success : function(data) {
					if (data.ready == true) {
						moniterTimer = setInterval(function() {
							getMonitorData();
						}, interval);
						//$("#loading_div").html("");
						$("#loading_div").hide();
						ymin=Math.min(data.yMin);
						ymax=Math.max(data.yMax);
						startFrequency=$('input[name=startFrequency]').val();
						stopFrequency=$('input[name=stopFrequency]').val();
						ytick= Math.ceil((Math.max(data.yMax)-Math.min(data.yMin))/5);
						xtick=Math.ceil(($('input[name=stopFrequency]').val()-$('input[name=startFrequency]').val())/$('input[name=bandWidth]').val());
						console.log(Math.max(data.yMax));
						console.log(Math.min(data.yMin));
						hightchart();
						clearInterval(isReadyTimer);
						
					}
				}
			})
		}
		function getMonitorData() {
			$.ajax({
				url : "./spectra/realTimeMonitorGetData",
				type : "POST",
				data : $("#deviceParaForm").serialize(),
				dataType : 'json',
				success : function(data) {
					if (data != null) {
						//此处需要重新设置坐标x轴的最左边和最右边以及步长
						var returnData = data.data;
						//point_padding
						console.log("return null");
						if (returnData != null) {
							console.log(formatData(returnData));
							console.log(returnData);
							chart.series[0].setData(formatData(returnData), true);
						}
					}
				}
			})
		}

		function stopMonitor() {
			if (moniterTimer != null || isReadyTimer != null) {
				clearInterval(moniterTimer);
				clearInterval(isReadyTimer);
				$("#loading_div").hide();
				$.ajax({
					url : "./spectra/realTimeMonitorStop",
					type : "POST",
					data : $("#deviceParaForm").serialize(),
					dataType : 'json',
					success : function(data) {
						console.log(data);
					}
				})
			}
			moniterTimer = null;
			isReadyTimer = null;
		}
		var jsFileArray = new Array();
		function getJsFileList() {
			$.ajax({
				url : "./spectra/getJsFileList",
				type : "POST",
				data : $("#deviceParaForm").serialize(),
				dataType : 'json',
				success : function(data) {
					var fileMsg = " 文件个数:"+data.length+" 文件名称分别为："+ data;
					jsFileArray = data;
					
					$("#jsFileListSpan").html(" 文件个数:"+data.length+" 文件名称分别为："+ data);
					console.log(data);
				}
			})
		}

		function loadJsFiles() {
			for(var i=0; i<jsFileArray.length ;i++ ){
				$.include("data/"+jsFileArray[i]);
			}
			setTimeout(function(){
				for(var i=0; i<jsFileArray.length ;i++ ){
					var varName = "dataArray" + jsFileArray[i].substring(0,jsFileArray[i].length-3);
					var data = eval(varName);
					alert(data + " " + data.length);
				}
			},3000);
		}
		
		function submitForm() {
			alert(1);
		}
		function formatData(arrObj){
			var data = [];
		    var start = parseInt($('input[name=startFrequency]').val());
		    var step = parseInt($('input[name=fftSize]').val());
		    for (i = 0; i < arrObj.length; i++) { 
		        data.push({ 
		            x: start,  
		            y: arrObj[i]
		        }); 
		        start += step;
		    } 
		    console.log(data);
		    return data; 
		}
		function changeDevice(){
			stopMonitor();
		}
		
		
		$(document).ready(function(){
			/*alert("文件1的行数 "+dataArray20150116001.length);
			alert("文件3的行数 "+dataArray20150116003.length);
			alert("文件4的行数 "+dataArray20150116004.length);
			alert("文件5的行数 "+dataArray20150116005.length);
			alert("文件6的行数 "+dataArray20150116006.length);*/
			//alert(bb)
			//alert("文件2的行数 "+dataArray20150116002.length);
			//alert(dataArray20150116002[0])
			//alert(formatData(dataArray20150116002[0]))
			chart.series[0].setData(formatData(dataArray20150116002[0]), true);
			for(var i=1; i<dataArray20150116002.length; i++){
				var ser = chart.addSeries( {
					lineWidth : 1,
					color : '#f90707',
					marker : {
						enabled : false,
					},
					pointInterval : point_padding,
					data : (function() { //设置默认数据， 
						//return initData(); 
					})()
				} );
				ser.setData(formatData(dataArray20150116002[i]), true);
			}
			
			//chart.series[0].setData(formatData(dataArray20150116001[1]), false);
		});
		
		function getMonitorData_old() {
			chart.series[0].setData(formatData(dataArray20150116001[0]), false);
			chart.series[0].setData(formatData(dataArray20150116001[1]), false);
			chart.series[0].setData(formatData(dataArray20150116001[2]), false);
			chart.series[0].setData(formatData(dataArray20150116001[3]), false);
		}
		
	</script>
</body>
</html>
