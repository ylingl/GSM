/**
 * 
 */

var deviceNum;
var dateDeviceFBIdMap;
var templateDeviceFBList;
var frequencyFileMap;
var startFrequency;
var stopFrequency;
var historyBK;

var chart0;
var chartS;
var moniterTimer = null;
var index;
var totalNum;

var ymin = -150;
var ymax = -30;
var ytick = 10;
var xtick = 0.2;

var ws = null;
var timestample = "";
var connected = false;

$(function(){
	$('#chartS').css('width', $(window).width() - 560);//设置统计区宽度
	$('#chartS').css('height', 400);//设置统计区宽度
	$('#chart0').css('width', $(window).width() - 560);//设置频谱区宽度
	$('#chart0').css('height', $(window).height() - 180);
	$('#content0').css('height', $(window).height() - 180);//设置页面高度
	$('#warningTable0').css('height', $(window).height() - 180);
	
	deviceNum = $("#deviceNum").val();
	initTemplate();
	initSlider();
	initOperate();
});

function connect() {
	var localhostPaht = window.location.host;
	ws= new WebSocket("ws://" + localhostPaht + "/Tsme/webSocket/history.ws");
	//websocket = new SockJS("http://localhost:8084/SpringWebSocketPush/sockjs/websck");
	ws.onopen = function () {
		connected = true;
	};
	
	ws.onmessage = function (event) {
		var wsHistoryServer = JSON.parse(event.data).wsHistoryServer;
		
		if(wsHistoryServer.server_code == 800){
			return; 
		}
		
		if(wsHistoryServer.server_code == 600){
			index = wsHistoryServer.index;
			var timeRec = wsHistoryServer.timestample;
			if(timestample != timeRec.toString()){
				//超时
				//alert("WebSocket阻塞！");
			} else {
				var spDataList = wsHistoryServer.spDataList;
				if(spDataList != null){
					if(spDataList != null){
						if(chart0.get("realTime") == null){
							chart0.addSeries({                       
							    id: "realTime",
							    name: "实时谱线",
							    color: 'blue',
							    type: 'line',
							    visible: true,
							    data: formatData(spDataList, startFrequency, stopFrequency)
							},true);
						} else {
							chart0.get("realTime").setData(formatData(spDataList, startFrequency, stopFrequency),true);
						}
					}
					
					$("#slider").slider("option", "value", index + 1);
					$("#current").html(index + 1);
					$("#createTime").html(wsHistoryServer.createTime);
					$("#LNG").html(wsHistoryServer.LNG);
					$("#LAT").html(wsHistoryServer.LAT);
					
					$("#warningData0").empty();
					count = 1;
					var warnDataList = wsHistoryServer.warnDataList;
					if(warnDataList != null){
						for(var i = 0 ; i < warnDataList.length; i ++){
							if((wsHistoryServer.serial == false || warnDataList[i].number > 0) && warnDataList[i].visible){
								$("#warningData0").append(
									"<span style='width:8%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
									"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warnDataList[i].startFrequency + "</span>" + 
									"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warnDataList[i].stopFrequency + "</span>" + 
									"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warnDataList[i].centerFrequency + "</span>" +
									"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warnDataList[i].number + "</span>" +
									"<span style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + warnDataList[i].currentTime + "</span>" + 
									"<span style='width:12%; display:block; border-bottom:1px solid #F00;float:left; '>" + 
										"<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='showWarningDetails(" + warnDataList[i].startFrequency + ", " + warnDataList[i].stopFrequency + ", " + warnDataList[i].centerFrequency + ")'>查看</button>" + 
										"&nbsp;<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='releaseWarning(" + warnDataList[i].centerFrequency + ")'>释放</button>" + 
									"</span>"
								);
														
								count ++;
							}
						}
					}
					
					if(index == totalNum - 1 && moniterTimer != null){
						clearInterval(moniterTimer);
						moniterTimer = null;
						$(".control").removeClass("stop").addClass("start");
					}
				}
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

function initOperate(){
	//设置开始按钮事件
	$(".control").on("click",function(){
		var _this=$(this);
		if(_this.hasClass("start")){//若正在播放，则暂停
			if(moniterTimer == null && index < totalNum - 1) {
				setTimeout(function(){
					moniterTimer = setInterval(function() {
						getHistoryData(true, "forward");
					}, 500);
				}, 500);
				
				_this.removeClass("start").addClass("stop");
			}
	    }else{
			if (moniterTimer != null) {
				clearInterval(moniterTimer);
				moniterTimer = null;
				
				_this.removeClass("stop").addClass("start");
			}
	    }
	});
	
	//设置后退按钮事件
	$(".prev").on("click",function(){
		if (moniterTimer != null) {
			clearInterval(moniterTimer);
			moniterTimer = null;
			if($(".control").hasClass("stop")){
				$(".control").removeClass("stop").addClass("start");
			}
		}
		
		getHistoryData(false, "backward");
	});
	
	//设置前进按钮事件
	$(".next").on("click",function(){
		if (moniterTimer != null) {
			clearInterval(moniterTimer);
			moniterTimer = null;
			if($(".control").hasClass("stop")){
				$(".control").removeClass("stop").addClass("start");
			}
		}
		
		getHistoryData(false, "forward");
	});
}

function initSlider(){
	$("#slider").slider({
		animate: "fast",
		disabled: true,
		range: "min",
		min: 0,
		step: 1,
		value: 0,
		slide: function(event, ui){		
			if (moniterTimer != null) {
				clearInterval(moniterTimer);
				moniterTimer = null;	
				$(".control").removeClass("stop").addClass("start");
			}
			
			$("#current").html(ui.value + 1);
		},
		stop: function(event, ui){
			index = ui.value;
			getHistoryData(false, "leap");
		}
	});
}

function initTemplate(){
	$.ajax({
		url : "/Tsme/history/getDateDeviceFBIdMap/"+ deviceNum,
		type : "POST",
		async : true,
		dataType : 'json',
		success : function(data) {
			if(data != null){
				dateDeviceFBIdMap = data;
				for (var key in dateDeviceFBIdMap){
					$("#dateSelect").append("<option value='" + key + "'>" + key + "</option>");
				}
			}
		}
	});
	
	$("#dateSelect").change(function() { dateSelectChange(); });
}

function dateSelectChange(){
	if($("#dateSelect").val() != "default"){
		if($("#templateSelect").length == 0 ){
			$("#templateDiv").append("请选择预警模板：<select id='templateSelect'><option value='default'>请选择</option></select>&nbsp;&nbsp;");
			$("#templateSelect").change(function() { templateSelectChange(); });
		} else {
			$("#templateSelect").empty();
			$("#templateSelect").append("<option value='default'>请选择</option>");
			$("#frequencyDiv").empty();
			$("#fileDiv").empty();
			$("#showButton").hide();
			$("#demodButton").hide();
		}
		
		$.ajax({
			url : "/Tsme/history/getTemplateDeviceFBList",
			type : "POST",
			traditional : true,
			async : true,
			data : {'deviceFBIds':dateDeviceFBIdMap[$("#dateSelect").val()]},
			dataType : 'json',
			success : function(data) {
				if(data != null){
					templateDeviceFBList = data;
					for (var i in templateDeviceFBList){
						$("#templateSelect").append("<option value='" + templateDeviceFBList[i].deviceWT.id + "'>" + templateDeviceFBList[i].deviceWT.template_name + "</option>");
					}
				}
			}
		});
	} else {
		$("#templateDiv").empty();
		$("#frequencyDiv").empty();
		$("#fileDiv").empty();
		$("#showButton").hide();
		$("#demodButton").hide();
	}
}

function templateSelectChange(){
	if($("#templateSelect").val() != "default"){
		if($("#frequencySelect").length == 0){
			$("#frequencyDiv").append("请选择预频段：<select id='frequencySelect'><option value='default'>请选择</option></select>&nbsp;&nbsp;");
			$("#frequencySelect").change(function() { frequencySelectChange(); });
		} else {
			$("#frequencySelect").empty();
			$("#frequencySelect").append("<option value='default'>请选择</option>");
			$("#fileDiv").empty();
			$("#showButton").hide();
			$("#demodButton").hide();
		}
		
		var deviceTemplateId = $("#templateSelect").val();
		for(var i in templateDeviceFBList){
			if(templateDeviceFBList[i].deviceWT.id == deviceTemplateId){
				var deviceFBList = templateDeviceFBList[i].deviceFBList;
				for(var j in deviceFBList){
					$("#frequencySelect").append("<option value='" + deviceFBList[j].id + "'>" 
												+ deviceFBList[j].startFrequency + "-" + deviceFBList[j].stopFrequency + "</option>");
				}
				break;
			}
		}
	} else {
		$("#frequencyDiv").empty();
		$("#fileDiv").empty();
		$("#showButton").hide();
		$("#demodButton").hide();
	}
}

function frequencySelectChange(){
	if($("#frequencySelect").val() != "default"){
		if($("#fileSelect").length == 0){
			$("#fileDiv").append("生成时间(终止时间)：<select id='fileSelect'><option value='default'>请选择</option></select>&nbsp;&nbsp;");
			$("#fileDiv").change(function() { fileSelectChange(); });
		} else {
			$("#fileSelect").empty();
			$("#showButton").hide();
			$("#demodButton").hide();
			$("#fileSelect").append("<option value='default'>请选择</option>");
		}
		
		var deviceFBId = $("#frequencySelect").val();
		var dateString = $("#dateSelect").val();
		$.ajax({
			url : "/Tsme/history/getFileList",
			type : "POST",
			data : {'deviceFBId':deviceFBId, 'dateString':dateString},
			async : true,
			dataType : 'json',
			success : function(data) {
				if(data != null){
					filePathList = data;
					for(var i in filePathList){
						var temps = filePathList[i].split("\\");
						var fileName = temps[temps.length - 1].split(".");
						$("#fileSelect").append("<option value='" + filePathList[i] + "'>" + formatName(fileName[0]) + "</option>");
					}
				}
			}
		});
	} else {
		$("#fileDiv").empty();
		$("#showButton").hide();
		$("#demodButton").hide();
	}
}

function fileSelectChange(){
	if($("#fileSelect").val() != "default"){
		$("#showButton").show();
		$("#demodButton").show();
	} else {
		$("#showButton").hide();
		$("#demodButton").hide();
	}
}

function alarm(){
	layer.msg('数据加载中，请稍候...', {icon: 1});
	
	if($("#fileSelect").val() != "default"){
		if (moniterTimer != null) {
			clearInterval(moniterTimer);
			moniterTimer = null;
		}
		
		if(ws != null || connected){
			var temp = {"browser_code":"100"};
			ws.send(JSON.stringify(temp));
			connected = false;
		}
		
		var key = $("#frequencySelect").find("option:selected").text().split("-");
		var filePath = $("#fileSelect").val();
		startFrequency = parseFloat(key[0]);
		stopFrequency = parseFloat(key[1]);
		
		if(chart0 != null){
			chart0.destroy();
			chart0 = null;
		}
		
		if(chartS != null){
			chartS.destroy();
			chartS = null;
		}
		
		$("#chartS").css("display","block");
		$('#chartS').css('width', $(window).width() - 350);//设置频谱区宽度
		$('#chart0').css('width', $(window).width() - 350);//设置频谱区宽度
		$('#warningTable0').css('width', 350);//设置频谱区宽度
		$('#warningTable0').css('height', $(window).height() + 220);//设置频谱区宽度
		$('#content0').css('height', $(window).height() + 220);//设置页面高度
		$("#slider").css("display","none");
		$("#panel").css("display","none");
		$("#current").html(0);
		$("#totalNum").html(0);
		$("#createTime").html(0);
		$("#LNG").html(0);
		$("#LAT").html(0);
		$("#warningData0").empty();
		$("#warningHead0").empty();
		$("#warningHead0").append("<span id='warningN0' class='warningN' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>序号</span>" + 
			"<span id='warningNum0' class='warningNum' style='width:16%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>告警数</span>" +
			"<span id='warningT0' class='warningT' style='width:51%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>最新告警时间</span>" +
			"<span id='warningD0' class='warningD' style='width:21%; display:block; border-bottom:1px solid #F00;float:left;'>详细信息</span>"
		);
		
		$.ajax({
			url: "/Tsme/history/getAlarmFile",
			type: "POST",
			data: {deviceNum:deviceNum, filePath:filePath, deviceTemplateId:$("#templateSelect").val(), startFrequency:startFrequency, stopFrequency:stopFrequency},
			async: true,
			dataType: 'json',
			success : function(data) {
				if(data != null){
					hightchart0("chart0", data.templateName, "FFT步长:" + data.parameter, stopFrequency, startFrequency);
					chart0.addSeries({                       
					    id: "warningLine",
					    name: "预警曲线",
					    color: 'red',
					    type: 'line',
					    visible: true,
					    data: data.warningline
					},true);
					
					chart0.addSeries({                       
					    id:"d",
					    name:"频点原始数据",
					    color: 'rgba(165,170,217,0.6)',
					    pointWidth: 9,
					    type: 'columnrange',
					    visible: true,
					    data: data.demodulationPointList,
					   /* cursor: 'pointer', //对应用图内灰色柱中有解调数据的部分，暂时不启用
						point: {
						    events: {
								click: function () {
									showOriginalDemod(this.x);
									//alert('Category: ' + this.x);
								}
							}
					    },*/
						tooltip: {
							pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
							shared: true
						}
					},true);
				
					chart0.addSeries({
						id:"dr",
						name:"频点统计数据",
						color: 'red',
						pointWidth: 5,
						type: 'columnrange',
						visible: true,
						data: data.statisticPointList,
						cursor: 'pointer',
						point: {
						    events: {
								click: function () {
									showStatisticResult(this.x);
									//alert('Category: ' + this.x);
								}
							}
					    },
					    tooltip: {
							pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
							shared: true
						}
					},true);
					
					hightchartS("chartS", data.templateName, "FFT步长:" + data.parameter, stopFrequency, startFrequency, data.stopTime, data.startTime);
					chartS.addSeries({                     
					    id: "alarmPoint",
					    name: "告警点",
					    color: 'rgba(255, 0, 0, .6)',
					    type: 'scatter',
					    visible: true,
					    data: data.alarmPointList
					},true);
					
					
					
					$("#totalNum").html(data.groupNum);
					
					var count = 1;
					var warnHistoryList = data.warnHistoryList;
					if(warnHistoryList != null){
						for(var i = 0; i < warnHistoryList.length; i ++){
							var pointList = "[";
							var centerList = "[";
							for(var j = 0; j < warnHistoryList[i].warningGroupList.length; j++){
								pointList = pointList + "[" + warnHistoryList[i].warningGroupList[j].beginPoint.x + "," + warnHistoryList[i].warningGroupList[j].beginPoint.y + "],";
								pointList = pointList + "[" + warnHistoryList[i].warningGroupList[j].endPoint.x + "," + warnHistoryList[i].warningGroupList[j].endPoint.y + "],"
								centerList = centerList + "[" + warnHistoryList[i].warningGroupList[j].centerFrequency + ", -30, -150],";
							}
							pointList = pointList.substring(0, pointList.length - 1) + "]";
							centerList = centerList.substring(0, centerList.length - 1) + "]";
							
							$("#warningData0").append(
								"<span class='bk" + i + "' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
								"<span class='bk" + i + "' style='width:16%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warnHistoryList[i].warningGroupList.length + "</span>" +
								"<span class='bk" + i + "' style='width:51%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + warnHistoryList[i].warningTime + "</span>" + 
								"<span class='bk" + i + "' style='width:21%; display:block; border-bottom:1px solid #F00;float:left; '>" + 
									"<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='getSpectraData(\"" + warnHistoryList[i].warningTime + "\", " + i + "," + pointList + "," + centerList +")'>查看频谱</button>" + 
								"</span>"
							);
							count ++;
						}
					}
				}
			}
		});
	}
}

function getSpectraData(createTime, i, pointList, centerList){
	$(historyBK).css("background-color", "");
	$(".bk" + i).css("background-color", "pink");
	historyBK = ".bk" + i;
	
	$.ajax({
		url: "/Tsme/history/getSpectraData",
		type: "POST",
		data: {createTime:createTime},
		async: true,
		dataType: 'json',
		success: function(data) {
			if(data != null){
				var spDataList = data.spDataList;
				if(spDataList != null){
					if(chart0.get("realTime") == null){
						chart0.addSeries({                       
						    id: "realTime",
						    name: "实时谱线",
						    color: 'blue',
						    type: 'line',
						    visible: true,
						    data: formatData(spDataList, startFrequency, stopFrequency)
						},true);
					} else {
						chart0.get("realTime").setData(formatData(spDataList, startFrequency, stopFrequency),true);
					}
				
					if(chart0.get("threshold") == null){
						chart0.addSeries({                       
						    id: "threshold",
						    name: "告警临界点",
						    color: '#ffd808',
						    type: 'scatter',
						    visible: true,
						    data: pointList
						},true);
					} else {
						chart0.get("threshold").setData(pointList,true);
					}
					
					if(chart0.get("centerFreq") == null){
						chart0.addSeries({                      
							id:"centerFreq",
							name:"告警中心点",
							color: '#d30097',
							pointWidth: 2,
							type: 'columnrange',
							visible: true,
							data: centerList,
						    tooltip: {
								pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
								shared: true
							}
						},true);
					} else {
						chart0.get("centerFreq").setData(centerList,true);
					}
				}
				
				$("#current").html(data.index + 1);
				$("#createTime").html(createTime);
				$("#LNG").html(data.LNG);
				$("#LAT").html(data.LAT);
			}
		},
		error: function(){
			layer.msg('频谱数据加载中!', {icon: 2});
        }
	})
}

function show(){
	layer.msg('数据加载中，请稍候...', {icon: 1});
	
	if($("#fileSelect").val() != "default"){
		if (moniterTimer != null) {
			clearInterval(moniterTimer);
			moniterTimer = null;
		}
		
		if(chart0 != null){
			chart0.destroy();
			chart0 = null;
		}
		
		initSlider();
		$("#slider").css("display","none");
		$("#panel").css("display","none");
		$("#chartS").css("display","none");
		$('#content0').css('height', $(window).height() - 180);//设置页面高度
		$('#warningTable0').css('width', 560);//设置频谱区宽度
		$('#warningTable0').css('height', $(window).height() - 180);
		$('#chart0').css('width', $(window).width() - 560);//设置频谱区宽度
		$('#chart0').css('height', $(window).height() - 180);
		$("#current").html(0);
		$("#totalNum").html(0);
		$("#createTime").html("无");
		$("#LNG").html(0);
		$("#LAT").html(0);
		$("#warningData0").empty();
		$("#warningHead0").empty();
		$("#warningHead0").append("<span id='warningN0' class='warningN' style='width:8%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>序号</span>" + 
			"<span id='warningStart0' class='warningStart' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>起始频率</span>" + 
			"<span id='warningStop0' class='warningStop' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>终止频率</span>" + 
			"<span id='warningCenter0' class='warningCenter' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>中心频率</span>" + 
			"<span id='warningNum0' class='warningNum' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>告警数</span>" + 
			"<span id='warningT0' class='warningT' style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>最新告警时间</span>" + 
			"<span id='warningD0' class='warningD' style='width:12%; display:block; border-bottom:1px solid #F00;float:left;'>详细信息</span>"
		);
		
		var key = $("#frequencySelect").find("option:selected").text().split("-");
		var filePath = $("#fileSelect").val();
		startFrequency = parseFloat(key[0]);
		stopFrequency = parseFloat(key[1]);
		
		$.ajax({
			url: "/Tsme/history/getFrequencyFile",
			type: "POST",
			data: {filePath:filePath, deviceTemplateId:$("#templateSelect").val(), startFrequency:startFrequency, stopFrequency:stopFrequency},
			async: true,
			dataType: 'json',
			success : function(data) {
				$("#slider").css("display","block");
				$("#panel").css("display","block");
				$('#chart0').css('width', $(window).width() - 560);//窗体反应慢需要重置
				if(data != null){
					hightchart0("chart0", data.templateName, "FFT步长:" + data.parameter, stopFrequency, startFrequency);
					chart0.addSeries({                       
					    id: "warningLine",
					    name: "预警曲线",
					    color: 'red',
					    type: 'line',
					    visible: true,
					    data: data.warningline
					},true);
					
					chart0.addSeries({                       
					    id:"d",
					    name:"频点原始数据",
					    color: 'rgba(165,170,217,0.6)',
					    pointWidth: 9,
					    type: 'columnrange',
					    visible: true,
					    data: data.demodulationPointList,
					   /* cursor: 'pointer', //对应用图内灰色柱中有解调数据的部分，暂时不启用
						point: {
						    events: {
								click: function () {
									showOriginalDemod(this.x);
									//alert('Category: ' + this.x);
								}
							}
					    },*/
						tooltip: {
							pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
							shared: true
						}
					},true);
				
					chart0.addSeries({                      
						id:"dr",
						name:"频点统计数据",
						color: 'red',
						pointWidth: 5,
						type: 'columnrange',
						visible: true,
						data: data.statisticPointList,
						cursor: 'pointer',
						point: {
						    events: {
								click: function () {
									showStatisticResult(this.x);
									//alert('Category: ' + this.x);
								}
							}
					    },
					    tooltip: {
							pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
							shared: true
						}
					},true);
				
					if(moniterTimer == null) {
						$("#slider").slider("option", "disabled", false);
						$("#slider").slider("option", "max", data.groupNum - 1);//后台从第0组开始
						$("#totalNum").html(data.groupNum);
						totalNum = data.groupNum;
						$("#current").html(0);
						
						if(data.groupNum == 0){
							layer.alert('未找到相应的历史数据，不能查看频谱！', {icon: 1});
						} else {
							connected = false;
							connect();
							setTimeout(function(){
								moniterTimer = setInterval(function() {
									getHistoryData(true, "forward");
								}, 500);
							}, 500);
						}
						
						if($(".control").hasClass("start")){//若正在播放，则暂停
							$(".control").removeClass("start").addClass("stop");
					    }
					}
				}
			}
		});
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

function hightchartS(containerId, templateName, parameter, stopFrequency, startFrequency, stopTime, startTime) {
	chartS = new Highcharts.Chart({
		chart : {
			backgroundColor : '#FFFAFA',
			renderTo : containerId, //图表放置的容器，DIV 
			defaultSeriesType : 'scatter', //图表类型为折线图 
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
			//tickPixelInterval : 0.2, //X轴标签间隔 
			tickInterval: xtick,
			title : {
				text : '频率(MHz)'
			}
		},
		yAxis : { //设置Y轴 
			max : stopTime > startTime ? stopTime : startTime, //Y轴最大值 
			min : stopTime > startTime ? startTime : stopTime, //Y轴最小值 
			//tickPixelInterval : 10,//y轴标签间隔 
			tickInterval: 0.1,
			title : {
				text : '时间(min)'
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
			}, 
			
	        series: {
	            marker: {
	                radius: 2
	            }
	        }
        },
		credits : {
			text : '告警统计图' //设置LOGO区文字 
		}
	});
}

/**
 * 监测获取数据
 */
function getHistoryData(serial, direction) {
	if(ws == null && moniterTimer != null){
		clearInterval(moniterTimer);
		moniterTimer = null;
		return;
	}
	
	if(!connected){
		return;
	}
	
	var temp0 = "";
	timestample = Math.random().toString();
	if(direction == "leap"){
		temp0 = {"browser_code":"900", "timestample":timestample, "serial":serial, "index":index, "direction":direction};
	} else {
		temp0 = {"browser_code":"900", "timestample":timestample, "serial":serial, "direction":direction};
	}
	
	ws.send(JSON.stringify(temp0));
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

function formatName(arrObj){
	var h = arrObj.substring(8, 10);
	var m = arrObj.substring(10, 12);
	var s = arrObj.substring(12, 14);
	var ms = arrObj.substring(14);

    return h + ":" + m + ":" + s + "." + ms; 
}

function showOriginalDemod(x){
	$.ajax({
		url : "/Tsme/history/showOriginalDemod",
		type : "POST",
		async : false,
		data : {x:x},
		dataType : 'json',
		success : function(data) {
			if(data != null){
				$("#originalDemodData").empty();
				$("#originalDemodData").html(data.originalDemod);
				$('#originalDemodMode').modal('show');
			} else {
				layer.alert("无解调数据！");
			}
		}
	})
}

function showStatisticResult(x){
	$.ajax({
		url : "/Tsme/history/showStatisticResult",
		type : "POST",
		async : false,
		data : {x:x},
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

				$("#statisticDemodData").empty();
				$("#statisticDemodData").html(text);
				$('#statisticDemodMode').modal('show');

			} else {
				layer.alert("未能获取解调统计结果！");
			}
		}
	})
}

function showWarningDetails(startFrequency, stopFrequency, centerFrequency){
	$.ajax({
		url : "/Tsme/history/showWarningDetails",
		type : "POST",
		async : false,
		data : {centerFrequency:centerFrequency},
		dataType : 'json',
		success : function(data) {
			if (data != null && data.length > 0) {
				var text = "";
				var count = 1;
				for(var i = 0; i < data.length; i ++){
					var temp = data[i];
					text = text + 
					"<span style='width:10%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
					"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + startFrequency + "</span>" + 
					"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + stopFrequency + "</span>" + 
					"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + centerFrequency + "</span>" +
					"<span style='width:8%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '> 1 </span>" +
					"<span style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + temp + "</span>" + 
					"<span style='width:14%; display:block; border-bottom:1px solid #F00;float:left; '>幅值越界</span>"
					count ++;
				}
				$("#warningDetail").empty();
				$("#warningDetail").html(text);
			} else {
				layer.alert("未能获取解调统计结果！");
			}
		}
	})
	$('#detailModal').modal('show');
}

function releaseWarning(centerFrequency){
	$.ajax({
		url : "/Tsme/history/releaseWarning",
		type : "POST",
		async : false,
		data : {centerFrequency:centerFrequency},
		dataType : 'json',
		success : function(data) {
			if (data) {
				layer.alert("释放成功");
			} else {
				layer.alert("未能释放，请重试！");
			}
		}
	})
}