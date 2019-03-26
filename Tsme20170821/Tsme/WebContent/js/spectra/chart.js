var chart0;
var chart1;
var chart2;

var key0;
var key1;
var key2;

var ymin = -150;
var ymax = -20;
var ytick = 10;
var xtick = 0.2;

var templateName = "";
var longitude = 0;
var latitude = 0;
var fftSize = 0;
var bandWidth = 0;
var maxMeans = 0;

var count;
var templateId;
var employ;
var deviceNum;

var moniterTimer = null;

var ws = null;
var timestample = "";
var connected = false;

$(function() {
	initChart();
});

function initChart() {
	templateId = $("input[name='templateId']").val();
	employ = Number($("input[name='employ']").val());
	deviceNum = $("input[name='deviceNum']").val();
	
	layer.msg('数据装载中，请稍候...', {icon: 1});
	
	if(employ){
		$.ajax({
			url : "./getSpectraPreParaCopy",//当前地址为/Tsme/spectra/chart
			type : "POST",
			async : true,
			data : {deviceNum:deviceNum},
			dataType : 'json',
			success : function(data) {
				if(data != null){
					templateName = data.templateName;
					longitude = data.longitude;
					latitude = data.latitude;
					fftSize = data.fftSize;
					bandWidth = data.bandWidth;
					maxMeans = data.maxMeans;
					var frequencyList = data.frequencyList;			
					var warningLineMap = data.warningLineMap;
					var demodPointMap = data.demodulationPointMap;
					
					if(data.myself) {
						$("#startButton").hide();
						$("#stopButton").show();
					} else {
						$("#startButton").hide();
						$("#stopButton").hide();
					}
					
					for(var i = 0; i < frequencyList.length; i ++){
						if(i % 2 == 0){
							var startFrequency = frequencyList[i];
							var stopFrequency = frequencyList[i + 1];
							var key = startFrequency.toFixed(1) + "-" + stopFrequency.toFixed(1);
							
							var subTitle = "经度：" + longitude + "；纬度：" + latitude + "；起始频率：" + startFrequency
							+ "MHz；终止频率：" + stopFrequency + "MHz；FFT步长" + fftSize
							+ "；测量频率：" + maxMeans + "Hz";
							
							switch (i) {
							case 0:
								$('#chart0').css('width', $(window).width() - 620);
								
								hightchart0('chart0', subTitle, startFrequency, stopFrequency);
								key0 = key;
								chart0.addSeries({                       
								    id: "warningLine",
								    name: "预警曲线",
								    color: 'red',
								    type: 'line',
								    visible: true,
								    data: warningLineMap[key]
								},true);
								
								chart0.addSeries({                       
								    id:"d",
								    name:"待解调频点",
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
								
								chart0.addSeries({                      
									id:"dr",
									name:"已解调频点",
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
							case 2:
								if($("#content1").length==0){
									$("#content0").after(
										"<br/>" +
										"<div id='content1' class='content'>" + 
											"<div id='chart1' class='chart_spline'></div>" + 
											"<div id='warningTable1' class='warningTable'>" + 
												"<div id='warningTitle1' class='warningTitle'>告警信息列表</div>" + 
												"<div id='warningHead1' class='warningHead'>" + 
													"<span id='warningN1' class='warningN' style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>序号</span>" + 
													"<span id='warningStart1' class='warningStart' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>起始频率</span>" + 
													"<span id='warningStop1' class='warningStop' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>终止频率</span>" + 
													"<span id='warningCenter1' class='warningCenter' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>中心频率</span>" + 
													"<span id='warningNum1' class='warningNum' style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>告警数</span>" + 
													"<span id='warningT1' class='warningT' style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>最新告警时间</span>" + 
													"<span id='warningD1' class='warningD' style='width:12%; display:block; border-bottom:1px solid #F00;float:left;'>详细信息</span>" + 
												"</div>" + 
												"<div id='warningData1' class='warningData'>" + 
												"</div>" + 
											"</div>" + 
										"</div>")
								}
								
								$('#chart1').css('width', $(window).width() - 605);
								
								hightchart1('chart1', subTitle, startFrequency, stopFrequency);
								key1 = key;
								chart1.addSeries({                       
								    id: "warningLine",
								    name: "预警曲线",
								    color: 'red',
								    type: 'line',
								    visible: true,
								    data: warningLineMap[key]
								},true);
								
								chart1.addSeries({                       
								    id:"d",
								    name:"待解调频点",
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
								
								chart1.addSeries({                       
									id:"dr",
									name:"已解调频点",
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
							case 4:
								if($("#content2").length==0){
									$("#content1").after(
										"<br/>" +
										"<div id='content2' class='content'>" + 
											"<div id='chart2' class='chart_spline'></div>" + 
											"<div id='warningTable2' class='warningTable'>" + 
												"<div id='warningTitle2' class='warningTitle'>告警信息列表</div>" + 
												"<div id='warningHead2' class='warningHead'>" + 
													"<span id='warningN2' class='warningN' style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>序号</span>" + 
													"<span id='warningStart2' class='warningStart' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>起始频率</span>" + 
													"<span id='warningStop2' class='warningStop' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>终止频率</span>" + 
													"<span id='warningCenter2' class='warningCenter' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>中心频率</span>" + 
													"<span id='warningNum2' class='warningNum' style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>告警数</span>" + 
													"<span id='warningT2' class='warningT' style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>最新告警时间</span>" + 
													"<span id='warningD2' class='warningD' style='width:12%; display:block; border-bottom:1px solid #F00;float:left;'>详细信息</span>" + 
												"</div>" + 
												"<div id='warningData2' class='warningData'>" + 
												"</div>" + 
											"</div>" + 
										"</div>")
								}
								
								$('#chart2').css('width', $(window).width() - 605);
								
								hightchart2("chart2", subTitle, startFrequency, stopFrequency);
								key2 = key;
								chart2.addSeries({                       
								    id: "warningLine",
								    name: "预警曲线",
								    color: 'red',
								    type: 'line',
								    visible: true,
								    data: warningLineMap[key]
								},true);
								
								chart2.addSeries({                       
								    id:"d",
								    name:"待解调频点",
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
								
								chart2.addSeries({                       
									id:"dr",
									name:"已解调频点",
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
					}
					
					connected = false;
					connect();
					
					//开始显示数据，后台已经处于检测状态
					setTimeout(function(){
						moniterTimer = setInterval(function() {
							getMonitorData();
						}, 500);
					},500);
				}
			}
		})
	} else {
		$.ajax({
			url : "./startRealTimeMonitor",//当前地址为/Tsme/spectra/chart
			type : "POST",
			async : true,
			data : {deviceNum:deviceNum, templateId:templateId},
			dataType : 'json',
			success : function(data) {
				if(data != null){
					templateName = data.templateName;
					longitude = data.longitude;
					latitude = data.latitude;
					fftSize = data.fftSize;
					bandWidth = data.bandWidth;
					maxMeans = data.maxMeans;
					var frequencyList = data.frequencyList;
					var warningLineMap = data.warningLineMap;
					var demodPointMap = data.demodulationPointMap;
					
					if(data.myself) {
						$("#startButton").hide();
						$("#stopButton").show();
					} else {
						$("#startButton").hide();
						$("#stopButton").hide();
					}
					
					for(var i = 0; i < frequencyList.length; i ++){
						if(i % 2 == 0){
							var startFrequency = frequencyList[i];
							var stopFrequency = frequencyList[i + 1];
							var key = startFrequency.toFixed(1) + "-" + stopFrequency.toFixed(1);
							
							var subTitle = "经度：" + longitude + "；纬度：" + latitude + "；起始频率：" + startFrequency
							+ "MHz；终止频率：" + stopFrequency + "MHz；FFT步长" + fftSize
							+ "；测量频率：" + maxMeans + "Hz";
							
							switch (i) {
							case 0:
								$('#chart0').css('width', $(window).width() - 620);
								
								hightchart0('chart0', subTitle, startFrequency, stopFrequency);
								key0 = key;
								chart0.addSeries({                       
								    id: "warningLine",
								    name: "预警曲线",
								    color: 'red',
								    type: 'line',
								    visible: true,
								    data: warningLineMap[key]
								},true);
								
								chart0.addSeries({                       
								    id:"d",
								    name:"待解调频点",
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
								
								chart0.addSeries({                      
									id:"dr",
									name:"已解调频点",
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
								
							case 2:
								if($("#content1").length==0){
									$("#content0").after(
										"<br/>" +
										"<div id='content1' class='content'>" + 
											"<div id='chart1' class='chart_spline'></div>" + 
											"<div id='warningTable1' class='warningTable'>" + 
												"<div id='warningTitle1' class='warningTitle'>告警信息列表</div>" + 
												"<div id='warningHead1' class='warningHead'>" + 
													"<span id='warningN1' class='warningN' style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>序号</span>" + 
													"<span id='warningStart1' class='warningStart' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>起始频率</span>" + 
													"<span id='warningStop1' class='warningStop' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>终止频率</span>" + 
													"<span id='warningCenter1' class='warningCenter' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>中心频率</span>" + 
													"<span id='warningNum1' class='warningNum' style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>告警数</span>" + 
													"<span id='warningT1' class='warningT' style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>最新告警时间</span>" + 
													"<span id='warningD1' class='warningD' style='width:12%; display:block; border-bottom:1px solid #F00;float:left;'>详细信息</span>" + 
												"</div>" + 
												"<div id='warningData1' class='warningData'>" + 
												"</div>" + 
											"</div>" + 
										"</div>")
								}
								
								$('#chart1').css('width', $(window).width() - 605);
								
								hightchart1('chart1', subTitle, startFrequency, stopFrequency);
								key1 = key;
								chart1.addSeries({                       
								    id: "warningLine",
								    name: "预警曲线",
								    color: 'red',
								    type: 'line',
								    visible: true,
								    data: warningLineMap[key]
								},true);
								
								chart1.addSeries({                       
								    id:"d",
								    name:"待解调频点",
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
								
								chart1.addSeries({                       
									id:"dr",
									name:"已解调频点",
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
							case 4:
								if($("#content2").length==0){
									$("#content1").after(
										"<br/>" +
										"<div id='content2' class='content'>" + 
											"<div id='chart2' class='chart_spline'></div>" + 
											"<div id='warningTable2' class='warningTable'>" + 
												"<div id='warningTitle2' class='warningTitle'>告警信息列表</div>" + 
												"<div id='warningHead2' class='warningHead'>" + 
													"<span id='warningN2' class='warningN' style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>序号</span>" + 
													"<span id='warningStart2' class='warningStart' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>起始频率</span>" + 
													"<span id='warningStop2' class='warningStop' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>终止频率</span>" + 
													"<span id='warningCenter2' class='warningCenter' style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>中心频率</span>" + 
													"<span id='warningNum2' class='warningNum' style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>告警数</span>" + 
													"<span id='warningT2' class='warningT' style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00;'>最新告警时间</span>" + 
													"<span id='warningD2' class='warningD' style='width:12%; display:block; border-bottom:1px solid #F00;float:left;'>详细信息</span>" + 
												"</div>" + 
												"<div id='warningData2' class='warningData'>" + 
												"</div>" + 
											"</div>" + 
										"</div>")
								}
								
								$('#chart2').css('width', $(window).width() - 605);
								
								hightchart2("chart2", subTitle, startFrequency, stopFrequency);
								key2 = key;
								chart2.addSeries({                       
								    id: "warningLine",
								    name: "预警曲线",
								    color: 'red',
								    type: 'line',
								    visible: true,
								    data: warningLineMap[key]
								},true);
								
								chart2.addSeries({                       
								    id:"d",
								    name:"待解调频点",
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
								
								chart2.addSeries({                       
									id:"dr",
									name:"已解调频点",
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
					}
					
					connected = false;
					connect();
					
					setTimeout(function(){
						moniterTimer = setInterval(function() {
							getMonitorData();
						}, 500);
					},500);
				}
			}
		})
	}
}

function hightchart0(containerId, subTitle, startFrequency, stopFrequency) {
	chart0 = new Highcharts.Chart({
		chart : {
			backgroundColor : '#FDF5E6',
			renderTo : containerId, // 图表放置的容器，DIV
			defaultSeriesType : 'line', // 图表类型为折线图
			zoomType : 'xy',
			events : {
				load : function() {
					var series = this.series[0];
				}
			}
		},
		title : {
			text : templateName + '模板下的实时频谱图'  // 图表标题
		},
		subtitle: {
			text: subTitle
		},
		xAxis : { // 设置X轴
			max : stopFrequency, // x轴最大值
			min : startFrequency,
			gridLineColor : '#999',
			gridLineWidth : 1,
			// tickPixelInterval : 100, //X轴标签间隔
			tickInterval : xtick,
			title : {
				text : '频率(MHz)'
			}
		},
		yAxis : { // 设置Y轴
			max : ymax, //Y轴最大值 
			min : ymin, // Y轴最小值
			// tickPixelInterval : 100,//y轴标签间隔
			tickInterval : ytick,
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
			text : '频谱图' // 设置LOGO区文字
		}
	});
}

function hightchart1(containerId, subTitle, startFrequency, stopFrequency) {
	chart1 = new Highcharts.Chart({
		chart : {
			backgroundColor : '#FDF5E6',
			renderTo : containerId, // 图表放置的容器，DIV
			defaultSeriesType : 'line', // 图表类型为折线图
			zoomType : 'xy',
			events : {
				load : function() {
					var series = this.series[0];
				}
			}
		},
		title : {
			text : templateName + '模板下的实时频谱图'  // 图表标题
		},
		subtitle: {
			text: subTitle
		},
		xAxis : { // 设置X轴
			max : stopFrequency, // x轴最大值
			min : startFrequency,
			gridLineColor : '#999',
			gridLineWidth : 1,
			// tickPixelInterval : 100, //X轴标签间隔
			tickInterval : xtick,
			title : {
				text : '频率(MHz)'
			}
		},
		yAxis : { // 设置Y轴
			max : ymax, //Y轴最大值 
			min : ymin, // Y轴最小值
			// tickPixelInterval : 100,//y轴标签间隔
			tickInterval : ytick,
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
			text : '频谱图' // 设置LOGO区文字
		}
	});
}

function hightchart2(containerId, subTitle, startFrequency, stopFrequency) {
	chart2 = new Highcharts.Chart({
		chart : {
			backgroundColor : '#FDF5E6',
			renderTo : containerId, // 图表放置的容器，DIV
			defaultSeriesType : 'line', // 图表类型为折线图
			zoomType : 'xy',
			events : {
				load : function() {
					var series = this.series[0];
				}
			}
		},
		title : {
			text : templateName + '模板下的实时频谱图'  // 图表标题
		},
		subtitle: {
			text: subTitle
		},
		xAxis : { // 设置X轴
			max : stopFrequency, // x轴最大值
			min : startFrequency,
			gridLineColor : '#999',
			gridLineWidth : 1,
			// tickPixelInterval : 100, //X轴标签间隔
			tickInterval : xtick,
			title : {
				text : '频率(MHz)'
			}
		},
		yAxis : { // 设置Y轴
			max : ymax, //Y轴最大值 
			min : ymin, // Y轴最小值
			// tickPixelInterval : 100,//y轴标签间隔
			tickInterval : ytick,
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
			text : '频谱图' // 设置LOGO区文字
		}
	});
}

function startMonitor() {
	if (moniterTimer != null)
		return;
	
	layer.msg('数据装载中，请稍候...', {icon: 1});
	
	$.ajax({
		url : "./startRealTimeMonitor",//用于重新开始的时候装填预备数据
		type : "POST",
		async : true,
		data : {deviceNum:deviceNum, templateId:templateId},
		dataType : 'json',
		success : function(data) {
			if(data != null){
				connected = false;
				connect();
				
				setTimeout(function(){
					moniterTimer = setInterval(function() {
						getMonitorData();
					}, 500);
				},500);
				if(data.myself == true){
					$("#startButton").hide();
					$("#stopButton").show();
				} else {
					layer.alert("设备已占用，您仅可以查看此设备的监控数据!");
					$("#startButton").hide();
					$("#stopButton").hide();
				}
			}
		}
	});
}

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
		
		if(wsSpectraServer.server_code == 600 && wsSpectraServer.business == "realtime"){
			var timeRec = wsSpectraServer.timestample;
			if(timestample != timeRec.toString()){
				//超时
				//alert("WebSocket阻塞！");
			} else {
				var spectraData = wsSpectraServer.spectraData;
				if(spectraData != null){
					var LNG = parseFloat(spectraData.lng).toFixed(4);
					var LAT = parseFloat(spectraData.lat).toFixed(4);
					var spDataMap = spectraData.spDataMap;
					var warnDataMap = spectraData.warnDataMap;
					
					for (var key in spDataMap){
						var frequency = key.split("-");
						var start = parseFloat(frequency[0]);
						var stop = parseFloat(frequency[1]);
						
						var subTitle = "经度：" + LNG + "；纬度：" + LAT + "；起始频率：" + start
						+ "MHz；终止频率：" + stop + "MHz；FFT步长" + fftSize
						+ "；测量频率：" + maxMeans + "Hz";
						
						switch (key) {
						case key0:
							if(spDataMap[key] != null && spDataMap[key].length > 0){
								chart0.setTitle(null, { text: subTitle });
								if(chart0.get("realTime") == null){
									chart0.addSeries({                       
									    id: "realTime",
									    name: "实时谱线",
									    color: 'blue',
									    type: 'line',
									    visible: true,
									    data: formatData(spDataMap[key], start, stop)
									},true);
								} else {
									chart0.get("realTime").setData(formatData(spDataMap[key], start, stop),true);
								}
							}
							
							$("#warningData0").empty();
							count = 1;
							var warningData0 = warnDataMap[key];
							for(var i = 0 ; i < warningData0.length; i ++){
								if(warningData0[i].number > 2 && warningData0[i].visible){
									$("#warningData0").append(
										"<span style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData0[i].startFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData0[i].stopFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData0[i].centerFrequency + "</span>" +
										"<span style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData0[i].number + "</span>" +
										"<span style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + warningData0[i].currentTime + "</span>" + 
										"<span style='width:12%; display:block; border-bottom:1px solid #F00;float:left; '>" + 
											"<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;'onclick='showWarningDetails(" + start + ", " + stop + ", "  + warningData0[i].startFrequency + ", " + warningData0[i].stopFrequency + ", " + warningData0[i].centerFrequency + ")'>查看</button>" + 
											"&nbsp;<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='releaseWarning(" + start + ", " + stop + ", "  + warningData0[i].centerFrequency + ")'>释放</button>" + 
										"</span>"
									);
									count ++;
								}
							}
								
							break;
						case key1:
							if(spDataMap[key] != null && spDataMap[key].length > 0){
								chart1.setTitle(null, { text: subTitle });
								if(chart1.get("realTime") == null){
									chart1.addSeries({                       
									    id: "realTime",
									    name: "实时谱线",
									    color: 'blue',
									    type: 'line',
									    visible: true,
									    data: formatData(spDataMap[key], start, stop)
									},true);
								} else {
									chart1.get("realTime").setData(formatData(spDataMap[key], start, stop),true);
								}
							}
							
							$("#warningData1").empty();
							count = 1;
							var warningData1 = warnDataMap[key];
							for(var i = 0 ; i < warningData1.length; i ++){
								if(warningData1[i].number > 2 && warningData1[i].visible){
									$("#warningData1").append(
										"<span style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData1[i].startFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData1[i].stopFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData1[i].centerFrequency + "</span>" +
										"<span style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData1[i].number + "</span>" +
										"<span style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + warningData1[i].currentTime + "</span>" + 
										"<span style='width:12%; display:block; border-bottom:1px solid #F00;float:left; '>" + 
											"<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;'onclick='showWarningDetails(" + start + ", " + stop + ", "  + warningData1[i].startFrequency + ", " + warningData1[i].stopFrequency + ", " + warningData1[i].centerFrequency + ")'>查看</button>" + 
											"&nbsp;<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='releaseWarning(" + start + ", " + stop + ", "  + warningData1[i].centerFrequency + ")'>释放</button>" + 
										"</span>"
									);
									count ++;
								}
							}
							
							break;
						case key2:
							if(spDataMap[key] != null && spDataMap[key].length > 0){
								chart2.setTitle(null, { text: subTitle });
								if(chart2.get("realTime") == null){
									chart2.addSeries({                       
									    id: "realTime",
									    name: "实时谱线",
									    color: 'blue',
									    type: 'line',
									    visible: true,
									    data: formatData(spDataMap[key], start, stop)
									},true);
								} else {
									chart2.get("realTime").setData(formatData(spDataMap[key], start, stop),true);
								}
							}
							
							$("#warningData2").empty();
							count = 1;
							var warningData2 = warnDataMap[key];
							for(var i = 0 ; i < warningData2.length; i ++){
								if(warningData2[i].number > 2 && warningData2[i].visible){
									$("#warningData2").append(
										"<span style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData2[i].startFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData2[i].stopFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData2[i].centerFrequency + "</span>" +
										"<span style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningData2[i].number + "</span>" +
										"<span style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + warningData2[i].currentTime + "</span>" + 
										"<span style='width:12%; display:block; border-bottom:1px solid #F00;float:left; '>" + 
											"<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;'onclick='showWarningDetails(" + start + ", " + stop + ", "  + warningData2[i].startFrequency + ", " + warningData2[i].stopFrequency + ", " + warningData2[i].centerFrequency + ")'>查看</button>" + 
											"&nbsp;<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='releaseWarning(" + start + ", " + stop + ", "  + warningData2[i].centerFrequency + ")'>释放</button>" + 
										"</span>"
									);
									count ++;
								}
							}
							
							break;
						}
					}
				}
				
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
	var temp = {"browser_code":"900", "deviceNum":deviceNum, "timestample":timestample, "business":"realtime"};
	
	ws.send(JSON.stringify(temp));
}

function stopMonitor() {
	if (moniterTimer != null) {
		$.ajax({
			url : "/Tsme/spectra/stopRealTimeMonitor",
			type : "POST",
			data : {deviceNum:deviceNum},
			dataType : 'json',
			success : function(data) {
				//console.log(data);
				if (!data.result) {
					layer.alert("设备已占用，您仅可以查看此设备的监控数据!");
					
					$("#startButton").hide();
					$("#stopButton").hide();
				} else {
					clearInterval(moniterTimer);
					moniterTimer = null;
					
					if(ws != null || connected){
						var temp = {"browser_code":"100"};
						ws.send(JSON.stringify(temp));
						connected = false;
					}
					
					$("#startButton").show();
					$("#stopButton").hide();
				}
			}
		})
	} else {
		layer.alert("已成功停止监控该设备!");
		
		$("#startButton").show();
		$("#stopButton").hide();
	}
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

function changeDevice() {
	stopMonitor();
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

/**
 * 监测获取数据
 */
function getDemodResultPoint() {
	$.ajax({
		url : "/Tsme/data/getDemodResultPoint",
		type : "POST",
		async : false,
		data : {deviceNum:deviceNum},
		dataType : 'json',
		success : function(data) {
			
		},
		error: function(msg){
			layer.msg('频点数据加载失败！', {icon: 2});
		}
	})
}

function showWarningDetails(start, stop, startFrequency, stopFrequency, centerFrequency){
	$.ajax({
		url : "/Tsme/spectra/showWarningDetails",
		type : "POST",
		async : false,
		data : {deviceNum:deviceNum, startFrequency:start, stopFrequency:stop, centerFrequency:centerFrequency},
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

function releaseWarning(start, stop, centerFrequency){
	$.ajax({
		url : "/Tsme/spectra/releaseWarning",
		type : "POST",
		async : false,
		data : {deviceNum:deviceNum, startFrequency:start, stopFrequency:stop, centerFrequency:centerFrequency},
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
