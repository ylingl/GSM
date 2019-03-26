/**
 * 模板训练js
 */
 
var chart0;
var chart1;
var chart2;

var key0;
var key1;
var key2;

var moniterTimer = null;
var demodulateTimer = null;

var ymin = -150;
var ymax = -30;
var ytick = 10;
var xtick = 0.2;

var templateName = '';
var fftSize = 0;
var bandWidth = 0;
var maxMeans = 0;
var deviceNum = "";

var avgArray = new Array();//均线数组
var warningLine = new Array();//预警线
var warningTemplateArray = new Array();//下拉框中的预警模板信息

var ws = null;
var timestample = "";
var connected = false;

$(function(){
	//js初始化
	initTemplate();
	btnInit();
	deviceNum = $("#submitdata #deviceNum").val();
});

function connect() {
	var localhostPaht = window.location.host;
	ws= new WebSocket("ws://" + localhostPaht + "/Tsme/webSocket/spectra.ws");
	//websocket = new SockJS("http://localhost:8084/SpringWebSocketPush/sockjs/websck");
	ws.onopen = function () {
		connected = true;
	};
	
	ws.onmessage = function (event) {
		var wsSpectraServer = JSON.parse(event.data).wsSpectraServer;
		
		if(wsSpectraServer.server_code == 800){
			return;
		}
		
		if(wsSpectraServer.server_code == 600){
			if(wsSpectraServer.business == "train"){
				var timeRec = wsSpectraServer.timestample;
				if(timestample != timeRec.toString()){
					//超时
					//alert("WebSocket阻塞！");
				} else {
					var spDataMap = wsSpectraServer.spectrumMap;
					if (spDataMap != null) {
						//此处需要重新设置坐标x轴的最左边和最右边以及步长
						for (var key in spDataMap){
							var frequency = new Array();
							frequency = key.split("-");
							
							switch (key) {
							case key0:
								if(spDataMap[key] != null)
									chart0.get("realTime").setData(formatData(spDataMap[key], frequency[0], frequency[1]), true);
								break;
							case key1:
								if(spDataMap[key] != null)
									chart1.get("realTime").setData(formatData(spDataMap[key], frequency[0], frequency[1]), true);
								break;
							case key2:
								if(spDataMap[key] != null)
									chart2.get("realTime").setData(formatData(spDataMap[key], frequency[0], frequency[1]), true);
								break;
							}
						}
					}
				}
				
				return;
			}
			
			if(wsSpectraServer.business == "demod"){
				var timeRec = wsSpectraServer.timestample;
				if(timestample != timeRec.toString()){
					//超时
					//alert("WebSocket阻塞！");
				} else {
					var demodResultMap = wsSpectraServer.demodulationPointMapForChart;
					if(demodResultMap != null){
						//console.log(demodResultMap);
						for (var key in demodResultMap){
							switch (key) {
							case key0:
								if(demodResultMap[key] != null)
									chart0.get("dr").setData(demodResultMap[key],true);
								break;
							case key1:
								if(demodResultMap[key] != null)
									chart1.get("dr").setData(demodResultMap[key],true);
								break;
							case key2:
								if(demodResultMap[key] != null)
									chart2.get("dr").setData(demodResultMap[key],true);
								break;
							}
						}
					}
				}
				
				return;
			}
		}
	};
	
	ws.onclose = function (event) {
		if(moniterTimer != null){
			clearInterval(moniterTimer);
			moniterTimer = null;
		}
		
		if (ws != null) {
			ws.close();
			ws = null;
		}
		
		connected = false;
	};
}

function disconnect() {
	if (ws != null) {
		ws.close();
		ws = null;
	}
	
	if(moniterTimer != null){
		clearInterval(moniterTimer);
		moniterTimer = null;
	}
	
	connected = false;
}

function initTemplate(){
	$.ajax({
		url : "/Tsme/data/getWarningTemplatesWithStatus",
		type : "POST",
		dataType : 'json',
		success : function(data) {
			if (data != null) {
				for(var i = 0; i < data.warningTemplateList.length; i ++){
					//console.log(warningTemplateArray[i].id+" "+warningTemplateArray[i].template_name);
					warningTemplateArray[data.warningTemplateList[i].id] = data.warningTemplateList[i];
					$("#warningTemplateSelect").append("<option value='" + data.warningTemplateList[i].id + "'>" + data.warningTemplateList[i].template_name + "</option>");
				}
				//console.log(warningTemplateArray);
				if(data.demod) {//与employ互斥
					var currentWarningTemplateId = data.currentwarningTemplate.id;
					$("#warningTemplateSelect option[value='" + currentWarningTemplateId + "']").attr("selected","selected");
					
					connected = false;
					connect();
					
					showTemplateDetail(true);
					
					if(demodulateTimer == null){
						setTimeout(function(){
							demodulateTimer = setInterval(function() {
								getDemodResultPoint();
							}, 1000);
						},1000);
					}
					$("#btnDemodulation").text("停止解调");
				}
				
				if(data.employ) {
					if(!data.myself){
						layer.alert("其他用户正在训练该设备，您目前不能进行任何训练操作！");
						operatorRemove();
					} else {
						btnChangeBegin();
					}
					
					var currentWarningTemplate = data.currentwarningTemplate;
					templateName = currentWarningTemplate.template_name;
					fftSize = currentWarningTemplate.fftSize;
					maxMeans = currentWarningTemplate.maxMeans;
					
					var frequencyBandList = currentWarningTemplate.frequencyBandList;
					
					$("#modifyForm #warningLineGroup").empty();
					
					for(var i = 0; i < frequencyBandList.length; i ++) {
						switch (i){
						case 0 :
							var startFrequency0 = frequencyBandList[0].startFrequency;
							var stopFrequency0 = frequencyBandList[0].stopFrequency;
							var parameter0 = "起始频率:" + startFrequency0 + 
							"MHz; 终止频率:" + stopFrequency0 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
							
							key0 = startFrequency0.toFixed(1) + "-" + stopFrequency0.toFixed(1);
							if($("input[type=radio][name=groupNum][value='" + key0 +"']").length == 0)
								$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key0 + "'/>第一组&nbsp;");
							
							hightchart0("chart0", templateName, parameter0, stopFrequency0, startFrequency0);
							chart0.addSeries({                       
							    id: "realTime",
							    name: "实时谱线",
							    type: 'line',
							    visible: true,
							    data: 0
							},true);
							
							break;
						case 1 :
							var startFrequency1 = frequencyBandList[1].startFrequency;
							var stopFrequency1 = frequencyBandList[1].stopFrequency;
							var parameter1 = "起始频率:" + startFrequency1 + 
							"MHz; 终止频率:" + stopFrequency1 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
							
							if($("#chart1").length==0){
								$("#chart0").after("<div id='chart1' style='height:400px; width:100%; margin-top:10px'></div>");
							}
							
							key1 = startFrequency1.toFixed(1) + "-" + stopFrequency1.toFixed(1);
							if($("input[type=radio][name=groupNum][value='" + key1 +"']").length == 0)
								$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key1 + "'/>第二组&nbsp;");
							
							hightchart1("chart1", templateName, parameter1, stopFrequency1, startFrequency1);
							chart1.addSeries({                       
							    id: "realTime",
							    name: "实时谱线",
							    type: 'line',
							    visible: true,
							    data: 0
							},true);
							
							break;
						case 2 :
							var startFrequency2 = frequencyBandList[2].startFrequency;
							var stopFrequency2 = frequencyBandList[2].stopFrequency;
							var parameter2 = "起始频率:" + startFrequency2 + 
							"MHz; 终止频率:" + stopFrequency2 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
							
							if($("#chart2").length==0){
								$("#chart1").after("<div id='chart2' style='height:400px; width:100%; margin-top:10px'></div>");
							}
							
							key2 = startFrequency2.toFixed(1) + "-" + stopFrequency2.toFixed(1);
							if($("input[type=radio][name=groupNum][value='" + key2 +"']").length == 0)
								$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key2 + "'/>第三组&nbsp;");
							
							hightchart2("chart2", templateName, parameter2, stopFrequency2, startFrequency2);
							chart2.addSeries({                       
							    id: "realTime",
							    name: "实时谱线",
							    type: 'line',
							    visible: true,
							    data: 0
							},true);
							
							break;
						}
					}
					
					connected = false;
					connect();
					
					if(moniterTimer == null) {
						setTimeout(function(){
							moniterTimer = setInterval(function() {
								getMonitorData();
							}, 500);
						},500);
					}
				}
			}
		}
	});
}

function showTemplateDetail(reviewDemod){
	if(chart0 != null){
		chart0.destroy();
		chart0 = null;
	}
	if(chart1 != null){
		chart1.destroy();
		chart1 = null;
		$("#chart1").remove();
	}
	if(chart2 != null){
		chart2.destroy();
		chart2 = null;
		$("#chart2").remove();
	}
	
	var templateId = $("#warningTemplateSelect").val();
	resetChart(warningTemplateArray[templateId]);
	
	btnCreatingAvg();
	
	$.ajax({
		url : "/Tsme/data/getAvgDataList/"+templateId+"/9",
		type : "POST",
		dataType : 'json',
		success : function(data) {
			if(data != null){
				layer.msg('正在绘图均值曲线，请稍候...', {icon: 1});
				var avgMap = data;
				//console.log(avgMap);
				for (var key in avgMap){
					switch (key) {
					case key0:
						avgArray = avgMap[key];
						for(var i = 1 ; i <= avgArray.length; i ++){
							//console.log(avgArray[i-1]);
							if(avgArray[i-1].length > 0){
								chart0.addSeries({                       
								    id: "a"+i,
								    name: "第 "+i+" 级特征线",
								    type: 'line',
								    visible: true,
								    data: avgArray[i-1]
									},true);
							}
						}
						break;
					case key1:
						avgArray = avgMap[key];
						for(var i = 1 ; i <= avgArray.length; i ++){
							//console.log(avgArray[i-1]);
							if(avgArray[i-1].length > 0){
								chart1.addSeries({                       
								    id: "a"+i,
								    name: "第 "+i+" 级特征线",
								    type: 'line',
								    visible: true,
								    data: avgArray[i-1]
									},true);
							}
						}
						break;
					case key2:
						avgArray = avgMap[key];
						for(var i = 1 ; i <= avgArray.length; i ++){
							//console.log(avgArray[i-1]);
							if(avgArray[i-1].length > 0){
								chart2.addSeries({                      
								    id: "a"+i,
								    name: "第 "+i+" 级特征线",
								    type: 'line',
								    visible: true,
								    data: avgArray[i-1]
									},true);
							}
						}
						break;
					}
				}
			} else {
				layer.msg('均值曲线为空！', {icon: 1});
			}
			
			$.ajax({
				url : "/Tsme/data/getWarningLine/"+templateId,
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if(data != null){
						layer.msg('正在绘图预警曲线，请稍候...', {icon: 1});
						var warnLineMap = data;
						//console.log(warnLineMap);
						for (var key in warnLineMap){
							switch (key) {
							case key0:
								if(chart0.get("w") != null)
									chart0.get("w").remove();
								chart0.addSeries({                       
								    id:"w",
								    name:"预警线",
								    type: 'line',
								    visible: true,
								    data: warnLineMap[key]
									},true);
								break;
							case key1:
								if(chart1.get("w") != null)
									chart1.get("w").remove();
								chart1.addSeries({                       
								    id:"w",
								    name:"预警线",
								    type: 'line',
								    visible: true,
								    data: warnLineMap[key]
									},true);
								break;
							case key2:
								if(chart2.get("w") != null)
									chart2.get("w").remove();
								chart2.addSeries({                       
								    id:"w",
								    name:"预警线",
								    type: 'line',
								    visible: true,
								    data: warnLineMap[key]
									},true);
								break;
							}
						}
						
						$.ajax({
							url : "/Tsme/data/getBaseAndPeakRow/"+templateId,
							type : "POST",
							dataType : 'json',
							success : function(bpRow){
								if(bpRow != null){
									layer.msg('正在加载基线，请稍候...', {icon: 1});
									$("input[name='baseline'][value='" + bpRow.baseRow + "'").prop("checked",true);
									$("#baseRow").val(bpRow.baseRow);
									$("input[name='peakline'][value='" + bpRow.peakRow + "'").prop("checked",true);
									$("#peakRow").val(bpRow.peakRow);
								} else {
									layer.msg('基线加载失败！', {icon: 2});
								}
								
								if(reviewDemod){
									getAvgExtreme(bpRow.baseRow, templateId);
								}
							},
							error: function(msg){
								layer.msg('基线加载失败！', {icon: 2});
							}
						});
					} else {
						layer.msg('预警曲线为空！', {icon: 1});
					}
				},
				error: function(msg){
					layer.msg('预警曲线加载失败！', {icon: 2});
				}
			});
			
			btnView();
		}
	});
}

function resetChart(obj){
	templateName = obj.template_name;
	fftSize = obj.fftSize;
	bandWidth = obj.bandWidth;
	maxMeans = obj.maxMeans;
	
	var frequencyBandList = obj.frequencyBandList;
	
	$("#modifyForm #warningLineGroup").empty();
	
	for(var i = 0; i < frequencyBandList.length; i ++){
		switch (i){
		case 0 :
			var startFrequency0 = frequencyBandList[0].startFrequency;
			var stopFrequency0 = frequencyBandList[0].stopFrequency;
			var parameter0 = "起始频率:" + startFrequency0 + 
			"MHz; 终止频率:" + stopFrequency0 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
			
			key0 = startFrequency0.toFixed(1) + "-" + stopFrequency0.toFixed(1);
			if($("input[type=radio][name=groupNum][value='" + key0 +"']").length == 0)
				$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key0 + "'/>第一组&nbsp;");
			
			hightchart0("chart0", templateName, parameter0, stopFrequency0, startFrequency0);
			
			break;
		case 1 :
			var startFrequency1 = frequencyBandList[1].startFrequency;
			var stopFrequency1 = frequencyBandList[1].stopFrequency;
			var parameter1 = "起始频率:" + startFrequency1 + 
			"MHz; 终止频率:" + stopFrequency1 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
			
			if($("#chart1").length==0){
				$("#chart0").after("<div id='chart1' style='height:400px; width:100%; margin-top:10px'></div>");
			}
			
			key1 = startFrequency1.toFixed(1) + "-" + stopFrequency1.toFixed(1);
			if($("input[type=radio][name=groupNum][value='" + key1 +"']").length == 0)
				$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key1 + "'/>第二组&nbsp;");
			
			hightchart1("chart1", templateName, parameter1, stopFrequency1, startFrequency1);
			
			break;
		case 2 :
			var startFrequency2 = frequencyBandList[2].startFrequency;
			var stopFrequency2 = frequencyBandList[2].stopFrequency;
			var parameter2 = "起始频率:" + startFrequency2 + 
			"MHz; 终止频率:" + stopFrequency2 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
			
			if($("#chart2").length==0){
				$("#chart1").after("<div id='chart2' style='height:400px; width:100%; margin-top:10px'></div>");
			}
			
			key2 = startFrequency2.toFixed(1) + "-" + stopFrequency2.toFixed(1);
			if($("input[type=radio][name=groupNum][value='" + key2 +"']").length == 0)
				$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key2 + "'/>第三组&nbsp;");
			
			hightchart2("chart2", templateName, parameter2, stopFrequency2, startFrequency2);
			
			break;
		}
	}
}

function hightchart0(containerId, templateName, parameter, stopFrequency, startFrequency) {
	chart0 = new Highcharts.Chart({
		chart : {
			backgroundColor : '#FDF5E6',
			renderTo : containerId, //图表放置的容器，DIV 
			defaultSeriesType : 'line', //图表类型为折线图 
			zoomType : 'xy',
			events : {
				load : function() {
					var series = this.series[0];
				}
			}
		},
		title : {
			text : templateName //图表标题 
		},
		subtitle: {
			text: parameter
		},
		xAxis : { //设置X轴 
			max : stopFrequency, //x轴最大值 
			min : startFrequency,
			gridLineColor : '#999',
			gridLineWidth : 1,
			//tickPixelInterval : 100, //X轴标签间隔 
			tickInterval: xtick,
			title : {
				text : '频率(MHz)'
			}
		},
		yAxis : { //设置Y轴 
			max : ymax, //Y轴最大值 
			min : ymin, //Y轴最小值 
			//tickPixelInterval : 100,//y轴标签间隔 
			tickInterval: ytick,
			title : {
				text : '电平值(dBm)'
			}
		},
		legend : {
			layout: 'horizontal',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
            /*layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            borderWidth: 0*/
		},
		plotOptions: {
			columnrange: {
				grouping: false,
				shadow: false,
				borderWidth: 0,
			}
        },
		credits : {
			text : '频谱图' //设置LOGO区文字 
		}
	});
}

function hightchart1(containerId, templateName, parameter, stopFrequency, startFrequency) {
	chart1 = new Highcharts.Chart({
		chart : {
			backgroundColor : '#FDF5E6',
			renderTo : containerId, //图表放置的容器，DIV 
			defaultSeriesType : 'line', //图表类型为折线图 
			zoomType : 'xy',
			events : {
				load : function() {
					var series = this.series[0];
				}
			}
		},
		title : {
			text : templateName //图表标题 
		},
		subtitle: {
			text: parameter
		},
		xAxis : { //设置X轴 
			max : stopFrequency, //x轴最大值 
			min : startFrequency,
			gridLineColor : '#999',
			gridLineWidth : 1,
			//tickPixelInterval : 100, //X轴标签间隔 
			tickInterval: xtick,
			title : {
				text : '频率(MHz)'
			}
		},
		yAxis : { //设置Y轴 
			max : ymax, //Y轴最大值 
			min : ymin, //Y轴最小值 
			//tickPixelInterval : 100,//y轴标签间隔 
			tickInterval: ytick,
			title : {
				text : '电平值(dBm)'
			}
		},
		legend : {
			layout: 'horizontal',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
            /*layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            borderWidth: 0*/
		},
		plotOptions: {
			columnrange: {
                grouping: false,
                shadow: false,
                borderWidth: 0
            }
        },
		credits : {
			text : '频谱图' //设置LOGO区文字 
		}
	});
}

function hightchart2(containerId, templateName, parameter, stopFrequency, startFrequency) {
	chart2 = new Highcharts.Chart({
		chart : {
			backgroundColor : '#FDF5E6',
			renderTo : containerId, //图表放置的容器，DIV 
			defaultSeriesType : 'line', //图表类型为折线图 
			zoomType : 'xy',
			events : {
				load : function() {
					var series = this.series[0];
				}
			}
		},
		title : {
			text : templateName //图表标题 
		},
		subtitle: {
			text: parameter
		},
		xAxis : { //设置X轴 
			max : stopFrequency, //x轴最大值 
			min : startFrequency,
			gridLineColor : '#999',
			gridLineWidth : 1,
			//tickPixelInterval : 100, //X轴标签间隔 
			tickInterval: xtick,
			title : {
				text : '频率(MHz)'
			}
		},
		yAxis : { //设置Y轴 
			max : ymax, //Y轴最大值 
			min : ymin, //Y轴最小值 
			//tickPixelInterval : 100,//y轴标签间隔 
			tickInterval: ytick,
			title : {
				text : '电平值(dBm)'
			}
		},
		legend : {
			layout: 'horizontal',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
            /*layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            borderWidth: 0*/
		},
		plotOptions: {
			columnrange: {
                grouping: false,
                shadow: false,
                borderWidth: 0
            }
        },
		credits : {
			text : '频谱图' //设置LOGO区文字 
		}
	});
}

/**
 * 开始监测
 */
function startMonitor() {
	if(moniterTimer != null) { return; }
	
	templateName = $("#submitdata #template").val();
	fftSize = $("#submitdata #fftSize").val();
	bandWidth = $("#submitdata #bandWidth").val();
	maxMeans = $("#submitdata #maxMeans").val();
	
	$("#modifyForm #warningLineGroup").empty();
	
	if($("#submitdata #stopFrequency0").val() > 0) {
		var startFrequency0 = parseFloat($("#submitdata #startFrequency0").val());
		var stopFrequency0 = parseFloat($("#submitdata #stopFrequency0").val());
		var parameter0 = "起始频率:" + startFrequency0 + 
		"MHz; 终止频率:" + stopFrequency0 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
		
		key0 = startFrequency0.toFixed(1) + "-" + stopFrequency0.toFixed(1);
		
		$.ajax({
			url : "/Tsme/data/startRecord",
			type : "POST",
			async : false,
			data : $("#submitdata").serialize(),
			dataType : 'json',
			success : function(data) {
				if (data.result) {
					hightchart0("chart0", templateName, parameter0, stopFrequency0, startFrequency0);
					chart0.addSeries({                       
					    id: "realTime",
					    name: "实时谱线",
					    type: 'line',
					    visible: true,
					    data: 0
					},true);
					
					if($("input[type=radio][name=groupNum][value='" + key0 +"']").length == 0)
						$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key0 + "'/>第一组&nbsp;");
					
					if($("#submitdata #stopFrequency1").val() > 0) {
						var startFrequency1 = parseFloat($("#submitdata #startFrequency1").val());
						var stopFrequency1 = parseFloat($("#submitdata #stopFrequency1").val());
						var parameter1 = "起始频率:" + startFrequency1 + 
						"MHz; 终止频率:" + stopFrequency1 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
						
						if($("#chart1").length==0){
							$("#chart0").after("<div id='chart1' style='height:400px; width:100%; margin-top:10px'></div>");
						}
						
						key1 = startFrequency1.toFixed(1) + "-" + stopFrequency1.toFixed(1);
						
						if($("input[type=radio][name=groupNum][value='" + key1 +"']").length == 0)
							$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key1 + "'/>第二组&nbsp;");
						
						hightchart1("chart1", templateName, parameter1, stopFrequency1, startFrequency1);
						chart1.addSeries({                       
						    id: "realTime",
						    name: "实时谱线",
						    type: 'line',
						    visible: true,
						    data: 0
						},true);
						
					}
					
					if($("#submitdata #stopFrequency2").val() > 0) {
						var startFrequency2 = parseFloat($("#submitdata #startFrequency2").val());
						var stopFrequency2 = parseFloat($("#submitdata #stopFrequency2").val());
						var parameter2 = "起始频率:" + startFrequency2 + 
						"MHz; 终止频率:" + stopFrequency2 + "MHz; FFT步长:" + fftSize + "; 同步频率:" + maxMeans + "Hz";
						
						if($("#chart2").length==0){
							$("#chart1").after("<div id='chart2' style='height:400px; width:100%; margin-top:10px'></div>");
						}
						
						key2 = startFrequency2.toFixed(1) + "-" + stopFrequency2.toFixed(1);
						
						if($("input[type=radio][name=groupNum][value='" + key2 +"']").length == 0)
							$("#modifyForm #warningLineGroup").append("<input name='groupNum' type='radio' value='" + key2 + "'/>第三组");
						
						hightchart2("chart2", templateName, parameter2, stopFrequency2, startFrequency2);
						chart2.addSeries({                       
						    id: "realTime",
						    name: "实时谱线",
						    type: 'line',
						    visible: true,
						    data: 0
						},true);
						
					}
					
					connected = false;
					connect();
					
					setTimeout(function(){
						moniterTimer = setInterval(function() {
							getMonitorData();
						}, 500);
					},500);
					
					warningTemplateArray[data.template.id] = data.template;
					$("#warningTemplateSelect").prepend("<option value='"+ data.template.id+"'>"+ data.template.template_name+"</option>");
					$("#warningTemplateSelect option[value='"+data.template.id+"']").attr("selected", true);
					btnChangeBegin();
				} else {
					if(data.type == 1){
						layer.alert("监测设备已被占用，您不能进行训练！");
					}
					if(data.type == 2){
						layer.alert("监测设备未在线，您不能进行训练！");
					}
				}
			}
		})
	}
}

/**
 * 监测获取数据
 */
function getMonitorData() {
	if(ws == null && moniterTimer != null){
		clearInterval(moniterTimer);
		moniterTimer = null;
		return;
	}
	
	if(!connected){
		return;
	}
	
	timestample = Math.random().toString();
	var temp = {"browser_code":"900", "deviceNum":deviceNum, "timestample":timestample, "business":"train"};
	
	ws.send(JSON.stringify(temp));
}

/**
 * 停止监测
 */
function stopMonitor() {
	if (moniterTimer != null) {
		$.ajax({
			url : "/Tsme/data/stopRecord",
			data : {deviceNum:deviceNum},
			type : "POST",
			dataType : 'json',
			success : function(data) {
				if(data != null){
					if(data.result == true){
						clearInterval(moniterTimer);
						moniterTimer = null;
						
						if(ws != null || connected){
							var temp = {"browser_code":"100"};
							ws.send(JSON.stringify(temp));
							connected = false;
						}
						
						if(data.writable == true){
							layer.msg('已停止,请稍候...', {icon: 1});
							btnChangeEnd();
						} else
							btnInit();	
					} else {
						layer.msg('无法停止', {icon: 1});
					}
				}
			}
		})
	}
}


/**
 * 生成预警模板
 */
function trainTemplate(){
	layer.msg('预警模板训练中，请稍候····', {icon: 1});	
	btnCreatingAvg();
	
	var templateId = $("#warningTemplateSelect").val();
	
	$.ajax({
		url : "/Tsme/data/trainingData/" + templateId,
		type : "POST",
		dataType : 'json',
		success : function(data) {
			if(data != -1){
				resetChart(warningTemplateArray[templateId]);
				layer.confirm("训练结束，共对" + data + "组数据进行了训练，点击“确定”显示训练结果！", 
					function(){echo(templateId);});
			} else {
				layer.msg("预警模板已启用，不能修改！", {icon: 0});
				btnView();
			}
		}
	});
}

function echo(templateId){
	layer.msg('数据装载中，请稍候...', {icon: 1});
	$.ajax({
		url : "/Tsme/data/getAvgDataList/"+templateId+"/9",
		type : "get",
		dataType : 'json',
		success : function(data) {
			if(data != null){
				var avgMap = data;
				//console.log(avgMap);
				for (var key in avgMap){
					switch (key) {
					case key0:
						avgArray = avgMap[key];
						for(var i = 1 ; i <= avgArray.length; i ++){
							//console.log(avgArray[i-1]);
							if(avgArray[i-1].length > 0){
								chart0.addSeries({                       
								    id: "a"+i,
								    name: "第 "+i+" 级特征线",
								    type: 'line',
								    visible: true,
								    data: avgArray[i-1]
									},true);
							}
						}
						if(chart0.get("realTime") != null)
							chart0.get("realTime").remove();
						break;
					case key1:
						avgArray = avgMap[key];
						for(var i = 1 ; i <= avgArray.length; i ++){
							//console.log(avgArray[i-1]);
							if(avgArray[i-1].length > 0){
								chart1.addSeries({                       
								    id: "a"+i,
								    name: "第 "+i+" 级特征线",
								    type: 'line',
								    visible: true,
								    data: avgArray[i-1]
									},true);
							}
						}
						if(chart1.get("realTime") != null)
							chart1.get("realTime").remove();
						break;
					case key2:
						avgArray = avgMap[key];
						for(var i = 1 ; i <= avgArray.length; i ++){
							//console.log(avgArray[i-1]);
							if(avgArray[i-1].length > 0){
								chart2.addSeries({                      
								    id: "a"+i,
								    name: "第 "+i+" 级特征线",
								    type: 'line',
								    visible: true,
								    data: avgArray[i-1]
									},true);
							}
						}
						if(chart2.get("realTime") != null)
							chart2.get("realTime").remove();
						break;
					}
				}
				
				btnAfterAvg();
			}
		}
	});
}

/**
 * 删除模板
 */
function deleteTemplate(){
	var templateId = $("#warningTemplateSelect").val(); 
	var templateName = $("#warningTemplateSelect").find("option:selected").text(); 
	layer.confirm("您确定要删除模板“"+templateName+"”吗？",function(){
		layer.msg('删除中，请稍候...', {icon: 1});
		$.ajax({
			url : "/Tsme/data/deleteWarningTemplate/"+templateId,
			type : "POST",
			dataType : 'json',
			success : function(data) {
				if(data=='1'){
					layer.msg('删除成功', {icon: 1});
					warningTemplateArray[templateId] = null;//清空
					$("#warningTemplateSelect option[value="+templateId+"]").remove(); 
					//console.log(warningTemplateArray);
					if(chart0 != null) {
						chart0.destroy();
						$("#chart0").append("<span style='display:block; width:100%; text-align:center; line-height:540px; color:red; font-size:20px'>点击“开始采集”启动训练，选择“历史预警模板”查看模板记录。</span>"); 
						chart0 = null;
					}
					if(chart1 != null) {
						chart1.destroy();
						chart1 = null;
					}
					if(chart2 != null) {
						chart2.destroy();
						chart2 = null;
					}
					
				}
				else
					layer.msg('删除失败', {icon: 0});
			}
		});
	});
	
	btnInit();
	
}


/**
 * 生成预警曲线
 */
function createWarningLine(){
	layer.msg('计算预警曲线...', {icon: 1});
	var baseLine = $("input:radio[name=baseline]:checked").val();
	$("#baseRow").val(baseLine);
	var peakLine = $("input:radio[name=peakline]:checked").val();
	$("#peakRow").val(peakLine);
	var templateId = $("#warningTemplateSelect").val(); 
	$.ajax({
		url : "/Tsme/data/createWarningLine/"+templateId+"/"+baseLine+"/"+peakLine+"",
		type : "POST",
		async : false,
		dataType : 'json',
		success : function(data) {
			if(data != null) {
				layer.msg('预警曲线成功生成！', {icon: 1});
				$("#btnDemodulation").removeAttr("disabled");
				var warnLineMap = data;
				//console.log(warnLineMap);
				for(var key in warnLineMap) {
					switch (key) {
					case key0:
						if(chart0.get("w") != null)
							chart0.get("w").remove();
						
						for(var i = 1 ; i <= chart0.series.length; i ++){
							if(chart0.get("a" + i) != null){
								if(baseLine != i && peakLine != i)
									chart0.get("a" + i).setVisible(false);
								else
									chart0.get("a" + i).setVisible(true);
							}
						}
						
						if(chart0.get("d") != null)
							chart0.get("d").remove();
						
						if(chart0.get("p") != null)
							chart0.get("p").remove();
						
						chart0.addSeries({                       
						    id:"w",
						    name:"预警线",
						    type: 'line',
						    visible: true,
						    data: warnLineMap[key]
							},true);
						break;
					case key1:
						if(chart1.get("w") != null)
							chart1.get("w").remove();
						
						for(var i = 1 ; i <= chart1.series.length; i ++){
							if(chart1.get("a" + i) != null){
								if(baseLine != i && peakLine != i)
									chart1.get("a" + i).setVisible(false);
								else
									chart1.get("a" + i).setVisible(true);
							}
						}
						
						if(chart1.get("d") != null)
							chart1.get("d").remove();
						
						if(chart1.get("p") != null)
							chart1.get("p").remove();
						
						chart1.addSeries({                       
						    id:"w",
						    name:"预警线",
						    type: 'line',
						    visible: true,
						    data: warnLineMap[key]
							},true);
						break;
					case key2:
						if(chart2.get("w") != null)
							chart2.get("w").remove();
						
						for(var i = 1 ; i <= chart2.series.length; i ++){
							if(chart2.get("a" + i) != null){
								if(baseLine != i && peakLine != i)
									chart2.get("a" + i).setVisible(false);
								else
									chart2.get("a" + i).setVisible(true);
							}
						}
						
						if(chart2.get("d") != null)
							chart2.get("d").remove();
						
						if(chart2.get("p") != null)
							chart2.get("p").remove();
						
						chart2.addSeries({                       
						    id:"w",
						    name:"预警线",
						    type: 'line',
						    visible: true,
						    data: warnLineMap[key]
							},true);
						break;
					}
				}
			} else {
				layer.msg('预警曲线生成失败！', {icon: 2});
			}
		},
		error: function(msg){
			layer.msg("预警模板已启用，不能修改！", {icon: 0});
			btnView();
		}
	});
}

/**
 * 开始监测
 */
var message="";
function presubmit(){
	if( $("#submitdata #template").val()==""){
		message += "请填写template_name,";
	}
	
	if($("#submitdata #startFrequency0").val()==""){
		message += "填写第一组开始频率；";
	}
	if($("#submitdata #stopFrequency0").val()==""){
		message += "填写第一组终止频率；";
	}
	
	if($("#submitdata #startFrequency1").val()==""){
		$("#submitdata #startFrequency1").val(0);
	}
	if($("#submitdata #stopFrequency1").val()==""){
		$("#submitdata #stopFrequency1").val(0);
	}
	
	if($("#submitdata #startFrequency2").val()==""){
		$("#submitdata #startFrequency2").val(0);
	}
	if($("#submitdata #stopFrequency2").val()==""){
		$("#submitdata #stopFrequency2").val(0);
	}

	if(message==""){
		$('#editmodel').modal('hide');
		startMonitor();
	}else{
		layer.alert(message);
	}
	message="";
}

//修改预警曲线
var altermessage="";
function alterPresubmit(){
	
	if($("#modifyForm #startFrequency").val()==""){
		altermessage += "请填写开始频率,";
	}
	if($("#modifyForm #stopFrequency").val()==""){
		altermessage += "请填写停止频率,";
	}
	if($("#modifyForm #threshold").val()==""){
		altermessage += "请填阈值";
	}

	if(altermessage==""){
		//执行的函数
		$('#modifyLine').modal('hide');
	}else{
		layer.alert(altermessage);
	}
	
	altermessage="";
	
	var baseLine = $("#baseRow").val();
	var peakLine = $("#peakRow").val();
	$("input[name='baseline'][value='" + baseLine + "'").prop("checked",true);
	$("input[name='peakline'][value='" + peakLine + "'").prop("checked",true);
	var templateId = $("#warningTemplateSelect").val();
	var key = $("input:radio[name=groupNum]:checked").val();
	
	$.ajax({
		url : "/Tsme/data/modifyWarningLine/" + templateId,
		type : "POST",
		async : false,
		data : $("#modifyForm").serialize(),
		dataType : 'json',
		success : function(data) {
			warningLine = data;
			//console.log(warningLine);
			
			if(warningLine.length > 0){
				layer.msg('预警曲线修改成功', {icon: 1});
				switch(key){
				case key0 :
					if(chart0.get("w") != null)
						chart0.get("w").remove()
					
					for(var i = 1 ; i <= chart0.series.length; i ++){
						if(chart0.get("a" + i) != null){
							if(baseLine != i && peakLine != i)
								chart0.get("a" + i).setVisible(false);
							else
								chart0.get("a" + i).setVisible(true);
						}
					}
					
					if(chart0.get("d") != null)
						chart0.get("d").remove();
					
					if(chart0.get("p") != null)
						chart0.get("p").remove();
					
					chart0.addSeries({                       
					    id:"w",
					    name:"预警线",
					    type: 'line',
					    visible: true,
					    data:warningLine
						},true);
					
					break;
				case key1:
					if(chart1.get("w") != null)
						chart1.get("w").remove();
					
					for(var i = 1 ; i <= chart1.series.length; i ++){
						if(chart1.get("a" + i) != null){
							if(baseLine != i && peakLine != i)
								chart1.get("a" + i).setVisible(false);
							else
								chart1.get("a" + i).setVisible(true);
						}
					}
					
					if(chart1.get("d") != null)
						chart1.get("d").remove();
					
					if(chart1.get("p") != null)
						chart1.get("p").remove();
					
					chart1.addSeries({                       
					    id:"w",
					    name:"预警线",
					    type: 'line',
					    visible: true,
					    data:warningLine
						},true);
					
					break;
				case key2:
					if(chart2.get("w") != null)
						chart2.get("w").remove()
					
					for(var i = 1 ; i <= chart2.series.length; i ++){
						if(chart2.get("a" + i) != null){
							if(baseLine != i && peakLine != i)
								chart2.get("a" + i).setVisible(false);
							else
								chart2.get("a" + i).setVisible(true);
						}
					}
					
					if(chart2.get("d") != null)
						chart2.get("d").remove();
					
					if(chart2.get("p") != null)
						chart2.get("p").remove();
					
					chart2.addSeries({                       
					    id:"w",
					    name:"预警线",
					    type: 'line',
					    visible: true,
					    data:warningLine
						},true);
					break;
				}
				//chart.redraw();
			} else {
				layer.msg("预警模板已启用，不能修改！", {icon: 0});
				btnView();
			}
		}
	})
}

function formatData(arrObj, startFrequency, stopFrequency){
	var data = [];
	if(arrObj != null) {
		var len = arrObj.length;
		var start = parseFloat(startFrequency);
		var stop = parseFloat(stopFrequency);
		var step = parseFloat((stop - start)/(len - 1));
		
	    for (i = 0; i < len; i ++) { 
	   		data[i]=[Number(start + step * i).toFixed(4), arrObj[i]];
	    }
	}
    //console.log(data);
    return data;
}

function operatorRemove(){
	$("#bottomDiv").remove();
	$("#topDiv").remove();
}

/**
 * 初始操作状态
 */
function btnInit(){
	$("#btnBegin").removeAttr("disabled");
	$("#btnEnd").attr("disabled","true");
	$("#btnCreateAvg").attr("disabled","true");
	$("#warningTemplateSelect").removeAttr("disabled");
	$("#btnDel").removeAttr("disabled");
	$("#btnView").removeAttr("disabled");
	$(":radio").attr("disabled","true");
	$("#btnCreateLine").attr("disabled","true");
	$("#btnModifyLine").attr("disabled","true");
	$("#btnDemodulation").attr("disabled","true");
}

/**
 * 点击查看后的操作状态
 */
function btnView(){
	$("#btnBegin").removeAttr("disabled");
	$("#btnEnd").attr("disabled","true");
	$("#btnCreateAvg").removeAttr("disabled");//.attr("disabled","true");
	$("#warningTemplateSelect").removeAttr("disabled");
	$("#btnDel").removeAttr("disabled");
	$("#btnView").removeAttr("disabled");
	$(":radio").removeAttr("disabled");
	$("#btnCreateLine").removeAttr("disabled");
	$("#btnModifyLine").removeAttr("disabled");
	$("#btnDemodulation").removeAttr("disabled");
}

/**
 * 点击开始监控后各个按钮的变化
 */
function btnChangeBegin(){
	$("#btnBegin").attr("disabled","true");
	$("#btnEnd").removeAttr("disabled");
	$("#btnCreateAvg").attr("disabled","true");
	$("#warningTemplateSelect").attr("disabled","true");
	$("#btnDel").attr("disabled","true");
	$("#btnView").attr("disabled","true");
	$(":radio").attr("disabled","true");
	$("#btnCreateLine").attr("disabled","true");
	$("#btnModifyLine").attr("disabled","true");
}

/**
 * 点击停止监控后各个按钮的变化
 */
function btnChangeEnd(){
	$("#btnBegin").attr("disabled","true");
	$("#btnEnd").attr("disabled","true");
	$("#btnCreateAvg").removeAttr("disabled");
	$("#warningTemplateSelect").attr("disabled","true");
	$("#btnDel").removeAttr("disabled");
	$("#btnView").attr("disabled","true");
	$(":radio").attr("disabled","true");
	$("#btnCreateLine").attr("disabled","true");
	$("#btnModifyLine").attr("disabled","true");
}


/**
 * 点击生成均线后各个按钮的变化
 */
function btnCreatingAvg(){
	$("#btnBegin").attr("disabled","true");
	$("#btnEnd").attr("disabled","true");
	$("#btnCreateAvg").attr("disabled","true");
	$("#warningTemplateSelect").attr("disabled","true");
	$("#btnDel").attr("disabled","true");
	$("#btnView").attr("disabled","true");
	$(":radio").attr("disabled","true");
	$("#btnCreateLine").attr("disabled","true");
	$("#btnModifyLine").attr("disabled","true");
}

function btnAfterAvg(){
	$("#btnBegin").attr("disabled","true");
	$("#btnEnd").attr("disabled","true");
	$("#btnCreateAvg").attr("disabled","true");
	$("#warningTemplateSelect").attr("disabled","true");
	$("#btnDel").removeAttr("disabled");
	$("#btnView").attr("disabled","true");
	$(":radio").removeAttr("disabled");
	$("#btnCreateLine").removeAttr("disabled");
	$("#btnModifyLine").removeAttr("disabled");
}

function viewPointAndDemod(){
	if($("#btnDemodulation").text() == "开始解调"){
		var rowNum = $("#baseRow").val();
		var templateId = $("#warningTemplateSelect").val();
		$("input[name='baseline'][value='" + rowNum + "'").prop("checked",true);
		$("input[name='peakline'][value='" + $("#peakRow").val() + "'").prop("checked",true);
		
		if(rowNum > 0){
			getAvgExtreme(rowNum, templateId);
			demodulation(rowNum, templateId);//调用解调
		} else {
			layer.msg('请先生成预警曲线！', {icon: 1});
		}
	} else if($("#btnDemodulation").text() == "停止解调"){
		layer.msg('停止解调，请稍候...', {icon: 1});
		if (demodulateTimer != null) {
			$.ajax({
				url : "/Tsme/data/stopDemodulate",
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if(data != null){
						if(data.result == true){
							clearInterval(demodulateTimer);
							demodulateTimer = null;
							
							if(ws != null || connected){
								var temp = {"browser_code":"100"};
								ws.send(JSON.stringify(temp));
								connected = false;
							}
							
							$("#btnDemodulation").text("开始解调");
							if(data.writable == true){
								layer.msg('已停止，请稍候...', {icon: 1});
								btnChangeEnd();
							} else
								btnInit();	
						} else {
							layer.msg('无法停止，请重试...', {icon: 1});
							$("#btnDemodulation").text("停止解调");
						}
					}
				}
			})
		}
	}
}

function getAvgExtreme(rowNum, templateId){
	layer.msg('开始加载解调参数，请稍候...', {icon: 1});
	$.ajax({
		url : "/Tsme/data/getAvgExtreme/" + templateId + "/" + rowNum,
		type : "POST",
		async : false,
		dataType : 'json',
		success : function(data) {
			if(data != null){
				var avgExtremeMap = data;
				//console.log(warnLineMap);
				for (var key in avgExtremeMap){
					switch (key) {
					case key0:
						if(chart0.get("p") != null)
							chart0.get("p").remove();
						for(var i = 1 ; i <= chart0.series.length; i ++){
							if(chart0.get("a" + i) != null){					
								if(rowNum != i)
									chart0.get("a" + i).setVisible(false);
								else
									chart0.get("a" + i).setVisible(true);		
							}
						}	
						if(chart0.get("w") != null){
							//chart0.get("w").setVisible(false);
							chart0.get("w").setVisible(true);
						}
						
						chart0.addSeries({                       
						    id:"p",
						    name:"第 " + rowNum + " 级特征峰值",
						    color: 'rgba(119, 152, 191, .5)', 
						    type: 'scatter',
						    visible: false,
						    data: avgExtremeMap[key]
							},true);
						break;
					case key1:
						if(chart1.get("p") != null)
							chart1.get("p").remove();
						for(var i = 1 ; i <= chart1.series.length; i ++){
							if(chart1.get("a" + i) != null){
								if(rowNum != i)
									chart1.get("a" + i).setVisible(false);
								else
									chart1.get("a" + i).setVisible(true);
							}
						}	
						if(chart1.get("w") != null){
							//chart1.get("w").setVisible(false);
							chart1.get("w").setVisible(true);
						}
						
						chart1.addSeries({                       
						    id:"p",
						    name:"第 " + rowNum + " 级特征峰值",
						    color: 'rgba(119, 152, 191, .5)', 
						    type: 'scatter',
						    visible: false,
						    data: avgExtremeMap[key]
							},true);
						break;
					case key2:
						if(chart2.get("p") != null)
							chart2.get("p").remove();
						for(var i = 1 ; i <= chart2.series.length; i ++){
							if(chart2.get("a" + i) != null){
								if(rowNum != i)
									chart2.get("a" + i).setVisible(false);
								else
									chart2.get("a" + i).setVisible(true);
							}
						}	
						if(chart2.get("w") != null){
							//chart2.get("w").setVisible(false);
							chart2.get("w").setVisible(true);
						}
							
						
						chart2.addSeries({                       
						    id:"p",
						    name:"第 " + rowNum + " 级特征峰值",
						    color: 'rgba(119, 152, 191, .5)', 
						    type: 'scatter',
						    visible: false,
						    data: avgExtremeMap[key]
							},true);
						break;
					}
				}
				
				getDemodulationPointForChart(rowNum, templateId);//开始加载待解调频点
				
			} else {
				layer.msg('无峰值数据！', {icon: 1});
			}
		},
		error: function(msg){
			layer.msg('峰值数据加载失败！', {icon: 2});
		}
	});
}

function getDemodulationPointForChart(rowNum, templateId){
	$.ajax({
		url : "/Tsme/data/getDemodulationPointForChart/" + templateId + "/" + rowNum,
		type : "POST",
		async : false,
		dataType : 'json',
		success : function(data) {
			if(data != null){
				var demodPointMap = data;
				//console.log(warnLineMap);
				for (var key in demodPointMap){
					switch (key) {
					case key0:
						if(chart0.get("d") != null)
							chart0.get("d").remove();
						
						chart0.addSeries({                       
						    id:"d",
						    name:"第 " + rowNum + " 级待解调频点",
						    color: 'rgba(165,170,217,0.6)',
						    pointWidth: 9,
						    type: 'columnrange',
						    visible: true,
						    data: demodPointMap[key],
							tooltip: {
								pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
								shared: true
							}
						},true);
						
						if(chart0.get("dr") != null)
							chart0.get("dr").remove();
						
						chart0.addSeries({                      
							id:"dr",
							name:"第 " + rowNum + " 级已解调频点",
							color: 'red',
							pointWidth: 5,
							type: 'columnrange',
							visible: true,
							data: 0,
							cursor: 'pointer',
							point: {
							    events: {
									click: function () {
										showDemonResult(deviceNum, key0, this.x);
										//alert('Category: ' + this.x);
									}
								}
						    },
						    tooltip: {
								pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
								shared: true
							}
						},true);
						
						break;
						
					case key1:
						if(chart1.get("d") != null)
							chart1.get("d").remove();
						
						chart1.addSeries({                       
						    id:"d",
						    name:"第 " + rowNum + " 级待解调频点",
						    color: 'rgba(165,170,217,0.6)',
						    pointWidth: 9,
						    type: 'columnrange',
						    visible: true,
						    data: demodPointMap[key],
						    tooltip: {
								pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
								shared: true
							}
						},true);
						
						if(chart1.get("dr") != null)
							chart1.get("dr").remove();
						
						chart1.addSeries({                       
							id:"dr",
							name:"第 " + rowNum + " 级已解调频点",
							color: 'red',
							pointWidth: 5,
							type: 'columnrange',
							visible: true,
							data: 0,
							cursor: 'pointer',
							point: {
							    events: {
									click: function () {
										showDemonResult(deviceNum, key1, this.x);
										//alert('Category: ' + this.x);
									}
								}
						    },
						    tooltip: {
								pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
								shared: true
							}
						},true);
						
						break;
						
					case key2:
						if(chart2.get("d") != null)
							chart2.get("d").remove();
						
						chart2.addSeries({                       
						    id:"d",
						    name:"第 " + rowNum + " 级待解调频点",
						    color: 'rgba(165,170,217,0.6)',
						    pointWidth: 9,
						    type: 'columnrange',
						    visible: true,
						    data: demodPointMap[key],
						    tooltip: {
								pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
								shared: true
							}
						},true);
						
						if(chart2.get("dr") != null)
							chart2.get("dr").remove();
						
						chart2.addSeries({                       
							id:"dr",
							name:"第 " + rowNum + " 级已解调频点",
							color: 'red',
							pointWidth: 5,
							type: 'columnrange',
							visible: true,
							data: 0,
							cursor: 'pointer',
							point: {
							    events: {
									click: function () {
										showDemonResult(deviceNum, key2, this.x);
										//alert('Category: ' + this.x);
									}
								}
						    },
						    tooltip: {
								pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
								shared: true
							}
						},true);
						
						break;
					}
				}
			} else {
				layer.msg('无频点数据！', {icon: 1});
			}
		},
		error: function(msg){
			layer.msg('频点数据加载失败！', {icon: 2});
		}
	})
}

function showDemonResult(deviceNum, frequencyBand, x){
	$.ajax({
		url : "/Tsme/data/showDemodResult",
		type : "POST",
		async : false,
		data : {deviceNum:deviceNum, frequencyBand:frequencyBand, x:x},
		dataType : 'json',
		success : function(data) {
			if (data != null && data.length > 0) {
				var text = "";
				for(var i = 0; i < data.length; i ++){
					var temp = data[i];
					text = text + "<b>第" + ( i + 1 ) + "组解调结果</b><br/>" +
					"频点序号：" + temp.index + "<br/>" +
					"频点：" + x + "（MHz）<br/>" +
					"SCH信息序号：" + temp.indicatorOfSCHInfo + "<br/>" +
					"均值载干比：" + temp.avgCI + "<br/>" +
					"解调次数：" + temp.count + "<br/>" +
					"CIValue：" + temp.ciValue + "<br/>" +
					"Mobile Country Code：" + temp.mobileCountryCode + "<br/>" +
					"Mobile Network Code：" + temp.mobileNetworkCode + "<br/>" +
					"Location Area Code：" + temp.locationAreaCode + "<br/>" +
					"pduType：" + temp.pduType + "<br/>" +
					"SI13Position：" + temp.si13Position + "<br/>" +
					"RAColour：" + temp.raColour + "<br/><br/>"
				}
				$("p").empty();
				$("p").html(text);
			} else {
				layer.alert("未能获取解调统计结果！");
			}
		}
	})
	$('#demodModal').modal('show');
}

function demodulation(rowNum, templateId){
	layer.msg('开始解调，请稍候...', {icon: 1});
	$.ajax({
		url : "/Tsme/data/startDemodulate",
		type : "POST",
		async : false,
		data : {warningTemplateId:templateId, row:rowNum},
		dataType : 'json',
		success : function(data) {
			if (data.result) {
				connected = false;
				connect();
				
				setTimeout(function(){
					demodulateTimer = setInterval(function() {
						getDemodResultPoint();
					}, 1000);
				},1000);
				
				btnChangeBegin();//待修改
				$("#btnDemodulation").text("停止解调");
			} else {
				if(data.type == 1){
					layer.msg("监测设备已被占用，您不能进行训练！", {icon: 0});
				}
				if(data.type == 2){
					layer.msg("监测设备未在线，您不能进行训练！", {icon: 0});
				}
				if(data.type == 3){
					layer.msg("预警模板已启用，不能修改！", {icon: 0});
				}
				$("#btnDemodulation").text("开始解调");
			}
		}
	})
}

/**
 * 监测获取数据
 */
function getDemodResultPoint() {
	if(ws == null && moniterTimer != null){
		clearInterval(moniterTimer);
		moniterTimer = null;
		return;
	}
	
	if(!connected){
		return;
	}
	
	timestample = Math.random().toString();
	var temp = {"browser_code":"900", "deviceNum":deviceNum, "timestample":timestample, "business":"demod"};
	
	ws.send(JSON.stringify(temp));
}
