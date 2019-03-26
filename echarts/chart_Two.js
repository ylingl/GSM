var chart_zero;
var chart_one;
var chart_two;

var key_zero;
var key_one;
var key_two;


var templateName = "";
var longitude = 0;
var latitude = 0;
var fftSize = 0;
var bandWidth = 0;
var maxMeans = 0;

var count;
var templateId;
var employ;

var moniterTimer_chart = null;
var ws_chart = null;

$(function() {
	initChart();
});

function initChart() {
	templateId = $("input[name='templateId']").val();
	employ = Number($("input[name='employ']").val());
	console.log(employ);
	deviceNum = $("input[name='deviceNum']").val();
	layer.msg('数据装载中，请稍候...', {icon: 1});
	if(employ){
		$.ajax({
			url : "/Tsme/spectra/getSpectraPreParaCopy",//当前地址为/Tsme/spectra/chart
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
//								var chartWid=$(window).width()-$(".warningTable").width()-10;
								$('#chart_zero').css('width', $(window).width() - 560);
								
//								hightchart_zero('chart_zero', subTitle, startFrequency, stopFrequency);
								key_zero = key;
								 var myChart_zero = echarts.init(document.getElementById('chart_zero'));
									option_zero = {
											color:['red','rgba(165,170,217,0.6)','red'],
										    backgroundColor: 'rgb(255,250,250)',
										    title:{
										    	text:'频谱图',
												right:0,
												bottom:3,
												textStyle:{
													color:'#999',
													fontSize:9,
													fontWeight:'normal'
												}
										    },
										    tooltip: {
												trigger: 'axis'
										    },
										  //图例设置
											legend: {
									        	type:'plain',
								                top:15,
								                textStyle: {
								                	fontSize:10
								                },
								                itemWidth:12,
								                itemHeight:8,
								                align:'auto',
								                data:['预警曲线','待解调频点','已解调频点']
											},
										    xAxis: {
										        type: 'value',
										        min:startFrequency,
												max:stopFrequency,
										        interval:0.2,
										        axisLabel:{
										            rotate:-30,//倾斜显示，-：顺时针旋转，+或不写：逆时针旋转
										            fontSize: 9
										        },
										        axisPointer: {
										            type: 'none' //鼠标移入显示几种状态
										        },
										        grid: {
										            left: '10%',//因旋转导致名字太长的类目造成遮蔽，可以配合这两个属性
										            bottom:'10%'// 分别表示：距离左边距和底部的距离，具体数值按实际情况调整
										        },
										        name:'频率(MHz)',
										        nameTextStyle:{
										        	fontSize: 10
										        }
										        
										    },
										    yAxis: {
										    	scale:true,
										        type: 'value',
										        min:-150,
										        max:-40
										    },
										    series: [
										    	{
											    	id:'warningLine',
											    	name:'预警曲线',
											        data: warningLineMap[key],
											        type: 'line',
											        smooth: true,
											        symbol:'none',
											        itemStyle: {
									                    normal: {
									                        lineStyle: {
									                            color: 'red'
									                        }
									                    }
											        }
										    },
										    {
										    	id:'d',
										    	name:'待解调频点',
										        data: demodPointMap[key],
										        type: 'bar',
										        barCategoryGap:9,
										        barWidth: '15%',
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'rgba(165,170,217,0.6)'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'dr',
										    	name:'已解调频点',
										        data: 0,
										        type: 'bar',
										        barCategoryGap:5,
										        barWidth: '15%',
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    }
										    ]
										};
								// 使用刚指定的配置项和数据显示图表。
						        myChart_zero.setOption(option_zero);
						        
						        //点击事件
						        myChart_zero.on('click', function (params){
						        	showDemonResult(deviceNum, myChart_zero, params.name);
						        });

//								chart_zero.addSeries({                       
//								    id: "warningLine",
//								    name: "预警曲线",
//								    color: 'red',
//								    type: 'line',
//								    visible: true,
//								    data: warningLineMap[key]
//								},true);
								
//								chart_zero.addSeries({                       
//								    id:"d",
//								    name:"待解调频点",
//								    color: 'rgba(165,170,217,0.6)',
//								    pointWidth: 9,
//								    type: 'columnrange',
//								    visible: true,
//								    data: demodPointMap[key],
//									tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
								
//								chart_zero.addSeries({                      
//									id:"dr",
//									name:"已解调频点",
//									color: 'red',
//									pointWidth: 5,
//									type: 'columnrange',
//									visible: true,
//									data: 0,
//									cursor: 'pointer',
//									point: {
//									    events: {
//											click: function () {
//												showDemonResult(deviceNum, chart_zero, this.x);
//												//alert('Category: ' + this.x);
//											}
//										}
//								    },
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
								
								break;
							case 2:
								if($("#content1").html() != "") {
									$("#content_chart").after(
										"<br/>" +
										"<div id='content1' class='content'>" + 
											"<div id='chart_one' class='chart_spline'></div>" + 
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
								
								$('#chart_one').css('width', $(window).width() - 584);
								
//								hightchart_one('chart_one', subTitle, startFrequency, stopFrequency);
								key_one = key;
								 var myChart_one = echarts.init(document.getElementById('chart_one'));
									 option_one = {
											color:['red','rgba(165,170,217,0.6)','red'],
										    backgroundColor: 'rgb(255,250,250)',
										    title:{
										    	text:'频谱图',
												right:0,
												bottom:3,
												textStyle:{
													color:'#999',
													fontSize:9,
													fontWeight:'normal'
												}
										    },
										    tooltip: {
												trigger: 'axis'
										    },
										  //图例设置
											legend: {
									        	type:'plain',
								                top:5,
								                textStyle: {
								                	fontSize:10
								                },
								                itemWidth:12,
								                itemHeight:8,
								                align:'auto',
								                data:['预警曲线','待解调频点','已解调频点']
											},
										    xAxis: {
										        type: 'value',
										        min:startFrequency,
												max:stopFrequency,
										        interval:0.2,
										        axisLabel:{
										            rotate:-30,//倾斜显示，-：顺时针旋转，+或不写：逆时针旋转
										            fontSize: 9
										        },
										        axisPointer: {
										            type: 'none' //鼠标移入显示几种状态
										        },
										        grid: {
										            left: '10%',//因旋转导致名字太长的类目造成遮蔽，可以配合这两个属性
										            bottom:'10%'// 分别表示：距离左边距和底部的距离，具体数值按实际情况调整
										        },
										        name:'频率(MHz)',
										        nameTextStyle:{
										        	fontSize: 10,
										        	align:'right'
										        }
										        
										    },
										    yAxis: {
										    	scale:true,
										        type: 'value',
										        min:-150,
										        max:-40
										    },
										    series: [{
										    	id:'warningLine',
										    	name:'预警曲线',
										        data: warningLineMap[key],
										        type: 'line',
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'d',
										    	name:'待解调频点',
										        data: demodPointMap[key],
										        type: 'bar',
										        barCategoryGap:9,
										        barWidth: '15%',
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'rgba(165,170,217,0.6)'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'dr',
										    	name:'已解调频点',
										        data: 0,
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:5,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    }
										   
										    ]
										};
								// 使用刚指定的配置项和数据显示图表。
						        myChart_one.setOption(option_one);
//								chart_one.addSeries({                       
//								    id: "warningLine",
//								    name: "预警曲线",
//								    color: 'red',
//								    type: 'line',
//								    visible: true,
//								    data: warningLineMap[key]
//								},true);
//								
//								chart_one.addSeries({                       
//								    id:"d",
//								    name:"待解调频点",
//								    color: 'rgba(165,170,217,0.6)',
//								    pointWidth: 9,
//								    type: 'columnrange',
//								    visible: true,
//								    data: demodPointMap[key],
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
//								
//								chart_one.addSeries({                       
//									id:"dr",
//									name:"已解调频点",
//									color: 'red',
//									pointWidth: 5,
//									type: 'columnrange',
//									visible: true,
//									data: 0,
//									cursor: 'pointer',
//									point: {
//									    events: {
//											click: function () {
//												showDemonResult(deviceNum, key_one, this.x);
//												//alert('Category: ' + this.x);
//											}
//										}
//								    },
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
								
								break;
							case 4:
								if($("#content2").html() != ""){
									$("#content1").after(
										"<br/>" +
										"<div id='content2' class='content'>" + 
											"<div id='chart_two' class='chart_spline'></div>" + 
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
								
								$('#chart_two').css('width', $(window).width() - 584);
								
//								hightchart_two("chart_two", subTitle, startFrequency, stopFrequency);
								key_two = key;
								 var myChart_two = echarts.init(document.getElementById('chart_two'));
									option_two = {
											color:['red','rgba(165,170,217,0.6)','red'],
											backgroundColor: 'rgb(255,250,250)',
											title:{
												text:'频谱图',
												right:0,
												bottom:3,
												textStyle:{
													color:'#999',
													fontSize:9,
													fontWeight:'normal'
												}
											},
											tooltip: {
												trigger: 'axis'
											},
											//图例设置
											legend: {
									        	type:'plain',
								                top:5,
								                textStyle: {
								                	fontSize:10
								                },
								                itemWidth:12,
								                itemHeight:8,
								                align:'auto',
								                data:['预警曲线','待解调频点','已解调频点']
											},
										    xAxis: {
										        type: 'value',
										        min:startFrequency,
												max:stopFrequency,
										        interval:0.2,
										        axisLabel:{
													rotate:-30,//倾斜显示，-：顺时针旋转，+或不写：逆时针旋转
													fontSize: 9
												},
												axisPointer: {
													type: 'none' //鼠标移入显示几种状态
												},
												grid: {
													left: '10%',//因旋转导致名字太长的类目造成遮蔽，可以配合这两个属性
													bottom:'10%'// 分别表示：距离左边距和底部的距离，具体数值按实际情况调整
												},
												name:'频率(MHz)',
												nameTextStyle:{
													fontSize: 10,
													align:'right'
												}
										        
										    },
										    yAxis: {
										    	scale:true,
										        type: 'value',
										        min:-150,
										        max:-40
										    },
										    series: [{
										    	id:'warningLine',
										    	name:'预警曲线',
										        data: warningLineMap[key],
										        type: 'line',
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'d',
										    	name:'待解调频点',
										        data: demodPointMap[key],
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:9,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'rgba(165,170,217,0.6)'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'dr',
										    	name:'已解调频点',
										        data: 0,
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:5,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    }
										    ]
										};
								// 使用刚指定的配置项和数据显示图表。
						        myChart_two.setOption(option_two);
//								chart_two.addSeries({                       
//								    id: "warningLine",
//								    name: "预警曲线",
//								    color: 'red',
//								    type: 'line',
//								    visible: true,
//								    data: warningLineMap[key]
//								},true);
//								
//								chart_two.addSeries({                       
//								    id:"d",
//								    name:"待解调频点",
//								    color: 'rgba(165,170,217,0.6)',
//								    pointWidth: 9,
//								    type: 'columnrange',
//								    visible: true,
//								    data: demodPointMap[key],
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
//								
//								chart_two.addSeries({                       
//									id:"dr",
//									name:"已解调频点",
//									color: 'red',
//									pointWidth: 5,
//									type: 'columnrange',
//									visible: true,
//									data: 0,
//									cursor: 'pointer',
//									point: {
//									    events: {
//											click: function () {
//												showDemonResult(deviceNum, key_two, this.x);
//												//alert('Category: ' + this.x);
//											}
//										}
//								    },
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
								
								break;
							}
						}
					}
					
					connected = false;
					connect_chart();
//					connected = false;
					//开始显示数据，后台已经处于检测状态
					setTimeout(function(){
						moniterTimer_chart = setInterval(function() {
							getMonitorData();
						}, 1000);
					},1000);
				}
			}
		})
	} else{
		$.ajax({
			url : "/Tsme/spectra/startRealTimeMonitor",//当前地址为/Tsme/spectra/chart
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
								$('#chart_zero').css('width', $(window).width() - 560);
								
//								hightchart_zero('chart_zero', subTitle, startFrequency, stopFrequency);
								key_zero = key;
								 var myChart_zero = echarts.init(document.getElementById('chart_zero'));
									option_zero = {
											color:['red','rgba(165,170,217,0.6)','red'],
											backgroundColor: 'rgb(255,250,250)',
											title:{
												text:'频谱图',
												right:0,
												bottom:3,
												textStyle:{
													color:'#999',
													fontSize:9,
													fontWeight:'normal'
												}
											},
											tooltip: {
												trigger: 'axis'
											},
											//图例设置
											legend: {
									        	type:'plain',
								                top:5,
								                textStyle: {
								                	fontSize:10
								                },
								                itemWidth:12,
								                itemHeight:8,
								                align:'auto',
								                data:['预警曲线','待解调频点','已解调频点']
											},
										    xAxis: {
										        type: 'value',
										        min:startFrequency,
												 max:stopFrequency,
										        interval:0.2,
										        axisLabel:{
													rotate:-30,//倾斜显示，-：顺时针旋转，+或不写：逆时针旋转
													fontSize: 9
												},
												axisPointer: {
													type: 'none' //鼠标移入显示几种状态
												},
												grid: {
													left: '10%',//因旋转导致名字太长的类目造成遮蔽，可以配合这两个属性
													bottom:'10%'// 分别表示：距离左边距和底部的距离，具体数值按实际情况调整
												},
												name:'频率(MHz)',
												nameTextStyle:{
													fontSize: 10,
													align:'right'
												}
										        
										    },
										    yAxis: {
										    	scale:true,
										        type: 'value',
										        min:-150,
										        max:-40
										    },
										    series: [{
										    	id:'warningLine',
										    	name:'预警曲线',
										        data: warningLineMap[key],
										        type: 'line',
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'d',
										    	name:'待解调频点',
										        data: demodPointMap[key],
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:9,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'rgba(165,170,217,0.6)'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'dr',
										    	name:'已解调频点',
										        data: 0,
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:5,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    }
										    ]
										};
								// 使用刚指定的配置项和数据显示图表。
						        myChart_zero.setOption(option_zero);
//								chart_zero.addSeries({                       
//								    id: "warningLine",
//								    name: "预警曲线",
//								    color: 'red',
//								    type: 'line',
//								    visible: true,
//								    data: warningLineMap[key]
//								},true);
//								
//								chart_zero.addSeries({                       
//								    id:"d",
//								    name:"待解调频点",
//								    color: 'rgba(165,170,217,0.6)',
//								    pointWidth: 9,
//								    type: 'columnrange',
//								    visible: true,
//								    data: demodPointMap[key],
//									tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
//								
//								chart_zero.addSeries({                      
//									id:"dr",
//									name:"已解调频点",
//									color: 'red',
//									pointWidth: 5,
//									type: 'columnrange',
//									visible: true,
//									data: 0,
//									cursor: 'pointer',
//									point: {
//									    events: {
//											click: function () {
//												showDemonResult(deviceNum, chart_zero, this.x);
//												//alert('Category: ' + this.x);
//											}
//										}
//								    },
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
								
								break;
								
							case 2:
								if($("#content1").html() != ""){
									$("#content_chart").after(
										"<br/>" +
										"<div id='content1' class='content'>" + 
											"<div id='chart_one' class='chart_spline'></div>" + 
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
								
								$('#chart_one').css('width', $(window).width() - 584);
								
//								hightchart_one('chart_one', subTitle, startFrequency, stopFrequency);
								key_one = key;
								 var myChart_one = echarts.init(document.getElementById('chart_one'));
									option_one = {
											color:['red','rgba(165,170,217,0.6)','red'],
											backgroundColor: 'rgb(255,250,250)',
											title:{
												text:'频谱图',
												right:0,
												bottom:3,
												textStyle:{
													color:'#999',
													fontSize:9,
													fontWeight:'normal'
												}
											},
											tooltip: {
												trigger: 'axis'
											},
											//图例设置
											legend: {
									        	type:'plain',
								                top:5,
								                textStyle: {
								                	fontSize:10
								                },
								                itemWidth:12,
								                itemHeight:8,
								                align:'auto',
								                data:['预警曲线','待解调频点','已解调频点']
											},
										    xAxis: {
										        type: 'value',
										        min:startFrequency,
												 max:stopFrequency,
										        interval:0.2,
										        axisLabel:{
													rotate:-30,//倾斜显示，-：顺时针旋转，+或不写：逆时针旋转
													fontSize: 9
												},
												axisPointer: {
													type: 'none' //鼠标移入显示几种状态
												},
												grid: {
													left: '10%',//因旋转导致名字太长的类目造成遮蔽，可以配合这两个属性
													bottom:'10%'// 分别表示：距离左边距和底部的距离，具体数值按实际情况调整
												},
												name:'频率(MHz)',
												nameTextStyle:{
													fontSize: 10,
													align:'right'
												}
										        
										    },
										    yAxis: {
										    	scale:true,
										        type: 'value',
										        min:-150,
										        max:-40
										    },
										    series: [{
										    	id:'warningLine',
										    	name:'预警曲线',
										        data: warningLineMap[key],
										        type: 'line',
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'d',
										    	name:'待解调频点',
										        data: demodPointMap[key],
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:9,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'rgba(165,170,217,0.6)'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'dr',
										    	name:'已解调频点',
										        data: 0,
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:5,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    }
										    ]
										};
								// 使用刚指定的配置项和数据显示图表。
						        myChart_one.setOption(option_one);
//								chart_one.addSeries({                       
//								    id: "warningLine",
//								    name: "预警曲线",
//								    color: 'red',
//								    type: 'line',
//								    visible: true,
//								    data: warningLineMap[key]
//								},true);
//								
//								chart_one.addSeries({                       
//								    id:"d",
//								    name:"待解调频点",
//								    color: 'rgba(165,170,217,0.6)',
//								    pointWidth: 9,
//								    type: 'columnrange',
//								    visible: true,
//								    data: demodPointMap[key],
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
//								
//								chart_one.addSeries({                       
//									id:"dr",
//									name:"已解调频点",
//									color: 'red',
//									pointWidth: 5,
//									type: 'columnrange',
//									visible: true,
//									data: 0,
//									cursor: 'pointer',
//									point: {
//									    events: {
//											click: function () {
//												showDemonResult(deviceNum, key_one, this.x);
//												//alert('Category: ' + this.x);
//											}
//										}
//								    },
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
								
								break;
							case 4:
								if($("#content2").html() != ""){
									$("#content1").after(
										"<br/>" +
										"<div id='content2' class='content'>" + 
											"<div id='chart_two' class='chart_spline'></div>" + 
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
								
								$('#chart_two').css('width', $(window).width() - 584);
								
//								hightchart_two("chart_two", subTitle, startFrequency, stopFrequency);
								key_two = key;
								 var myChart_two = echarts.init(document.getElementById('chart_two'));
									option_two = {
											color:['red','rgba(165,170,217,0.6)','red'],
											backgroundColor: 'rgb(255,250,250)',
											title:{
												text:'频谱图',
												right:0,
												bottom:3,
												textStyle:{
													color:'#999',
													fontSize:9,
													fontWeight:'normal'
												}
											},
											tooltip: {
												trigger: 'axis'
											},
											//图例设置
										    legend: {
										        	type:'plain',
									                top:5,
									                textStyle: {
									                	fontSize:10
									                },
									                itemWidth:12,
									                itemHeight:8,
									                align:'auto',
										        data:['预警曲线','待解调频点','已解调频点']
										    },
										    xAxis: {
										        type: 'value',
										        min:startFrequency,
												 max:stopFrequency,
										        interval:0.2,
										        axisLabel:{
													rotate:-30,//倾斜显示，-：顺时针旋转，+或不写：逆时针旋转
													fontSize: 9
												},
												axisPointer: {
													type: 'none' //鼠标移入显示几种状态
												},
												grid: {
													left: '10%',//因旋转导致名字太长的类目造成遮蔽，可以配合这两个属性
													bottom:'10%'// 分别表示：距离左边距和底部的距离，具体数值按实际情况调整
												},
												name:'频率(MHz)',
												nameTextStyle:{
													fontSize: 10,
													align:'right'
												}
										        
										    },
										    yAxis: {
										    	scale:true,
										        type: 'value',
										        min:-150,
										        max:-40
										    },
										    series: [{
										    	id:'warningLine',
										    	name:'预警曲线',
										        data: warningLineMap[key],
										        type: 'line',
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'d',
										    	name:'待解调频点',
										        data: demodPointMap[key],
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:9,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'rgba(165,170,217,0.6)'
								                        }
								                    }
								                }
										    },
										    {
										    	id:'dr',
										    	name:'已解调频点',
										        data: 0,
										        type: 'bar',
										        barWidth: '15%',
										        barCategoryGap:5,
//										        tooltip: {
//										        	formatter: function (name) {
//										        	    return 'Legend ' + name;
//										        	}
//											    },
										        smooth: true,
										        symbol:'none',
										        itemStyle: {
								                    normal: {
								                        lineStyle: {
								                            color: 'red'
								                        }
								                    }
								                }
										    }
										    ]
										};
								// 使用刚指定的配置项和数据显示图表。
						        myChart_two.setOption(option_two);
//								chart_two.addSeries({                       
//								    id: "warningLine",
//								    name: "预警曲线",
//								    color: 'red',
//								    type: 'line',
//								    visible: true,
//								    data: warningLineMap[key]
//								},true);
//								
//								chart_two.addSeries({                       
//								    id:"d",
//								    name:"待解调频点",
//								    color: 'rgba(165,170,217,0.6)',
//								    pointWidth: 9,
//								    type: 'columnrange',
//								    visible: true,
//								    data: demodPointMap[key],
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
//								
//								chart_two.addSeries({                       
//									id:"dr",
//									name:"已解调频点",
//									color: 'red',
//									pointWidth: 5,
//									type: 'columnrange',
//									visible: true,
//									data: 0,
//									cursor: 'pointer',
//									point: {
//									    events: {
//											click: function () {
//												showDemonResult(deviceNum, key_two, this.x);
//												//alert('Category: ' + this.x);
//											}
//										}
//								    },
//								    tooltip: {
//										pointFormat: '{series.name}: <b>{point.x}（MHz）<br/>',
//										shared: true
//									}
//								},true);
								
								break;
							}
						}
					}
					
					connected = false;
					connect_chart();
					
					setTimeout(function(){
						moniterTimer_chart = setInterval(function() {
							getMonitorData();
						}, 1000);
					},1000);
				}
			}
		})
	}
}

function hightchart_zero(containerId, subTitle, startFrequency, stopFrequency) {
	chart_zero = new Highcharts.Chart({
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

function hightchart_one(containerId, subTitle, startFrequency, stopFrequency) {
	chart_one = new Highcharts.Chart({
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

function hightchart_two(containerId, subTitle, startFrequency, stopFrequency) {
	chart_two = new Highcharts.Chart({
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
	if (moniterTimer_chart != null)
		return;
	
	layer.msg('数据装载中，请稍候...', {icon: 1});
	
	$.ajax({
		url : "/Tsme/spectra/startRealTimeMonitor",//用于重新开始的时候装填预备数据
		type : "POST",
		async : true,
		data : {deviceNum:deviceNum, templateId:templateId},
		dataType : 'json',
		success : function(data) {
			if(data != null){
				connected = false;
				connect_chart();
				
				setTimeout(function(){
					moniterTimer_chart = setInterval(function() {
						getMonitorData();
					}, 1000);
				},1000);
				
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

function connect_chart() {
	var localhostPaht = window.location.host;
	ws_chart = new WebSocket("ws://" + localhostPaht + "/Tsme/webSocket/spectra.ws");
	//websocket = new SockJS("http://localhost:8084/SpringWebSocketPush/sockjs/websck");
	ws_chart.onopen = function () {
		connected = true;
	};
	
	ws_chart.onmessage = function (event) {
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
					var LNG = parseFloat(spectraData.LNG).toFixed(4);
					var LAT = parseFloat(spectraData.LAT).toFixed(4);
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
						case key_zero:
							if(spDataMap[key] != null && spDataMap[key].length > 0){
								var myChart_0 = echarts.init(document.getElementById('chart_zero'));
								var oldLine = option_zero.series[option_zero.series.length-1];
								if(oldLine.id!="realTime"){
									var lineStyle = new Object();
									lineStyle.color = 'blue';
									var normal = new Object();
									normal.lineStyle = lineStyle;
									var itemStyle = new Object();
									itemStyle.normal = normal;
									var realTime = new Object();
									realTime.id = "realTime";
									realTime.name = "实时谱线";
									realTime.data = formatData_chart(spDataMap[key], start, stop);
									realTime.type = 'line';
									realTime.smooth = true;
									realTime.symbol = 'none';
									realTime.itemStyle = itemStyle;
									option_zero.series.push(realTime);
									option_zero.color.push('blue');
									option_zero.legend.data.push('实时谱线');
								}else{
									oldLine.data = formatData_chart(spDataMap[key], start, stop);
								}
								myChart_0.setOption(option_zero);
//								chart_zero.setTitle(null, { text: subTitle });
//								if(chart_zero.get("realTime") == null){
//									chart_zero.addSeries({                       
//									    id: "realTime",
//									    name: "实时谱线",
//									    color: 'blue',
//									    type: 'line',
//									    visible: true,
//									    data: formatData_chart(spDataMap[key], start, stop)
//									},true);
//								} else {
//									var dataArray = formatData_chart(spDataMap[key], start, stop);
//									chart_zero.get("realTime").setData(dataArray, true);
//								}
							}
							
							$("#warningData_chart").empty();
							count = 1;
							var warningDataChart = warnDataMap[key];
							for(var i = 0 ; i < warningDataChart.length; i ++){
								if(warningDataChart[i].number > 2 && warningDataChart[i].visible){
									$("#warningData_chart").append(
//											
										"<span style='width:6%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningDataChart[i].startFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningDataChart[i].stopFrequency + "</span>" + 
										"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningDataChart[i].centerFrequency + "</span>" +
										"<span style='width:14%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + warningDataChart[i].number + "</span>" +
										"<span style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + warningDataChart[i].currentTime + "</span>" + 
										"<span style='width:12%; display:block; border-bottom:1px solid #F00;float:left; '>" + 
											"<span style='font-size:12px; line-height:20px;cursor: pointer; padding:0px; height:20px;'onclick='showWarningDetails_chart(" + start + ", " + stop + ", "  + warningDataChart[i].startFrequency + ", " + warningDataChart[i].stopFrequency + ", " + warningDataChart[i].centerFrequency + ",this)'>查看" + 
											"&nbsp;<span type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='releaseWarning_chart(" + start + ", " + stop + ", "  + warningDataChart[i].centerFrequency + ")'>释放</button>" + 
										//新添加的开始
											"<p style='width:558px;background-color: blue;position: relative;left: -491px;display:none;'>"+
											"<span id='' class='warningN' style='width:6%;margin-left:11px; display:block;background-color:WhiteSmoke;border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>序号</span>" + 
											"<span id='' class='warningStart' style='width:12%; display:block;background-color:WhiteSmoke; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>起始频率</span>" + 
											"<span id='' class='warningStop' style='width:12%; display:block;background-color:WhiteSmoke; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>终止频率</span>" + 
											"<span id='' class='warningCenter' style='width:12%; display:block;background-color:WhiteSmoke; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>中心频率</span>" +
											"<span id='' class='warningNum' style='width:12%; display:block; background-color:WhiteSmoke;border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '> 告警数 </span>" +
											"<span id='' class='warningT' style='width:32%; display:block; background-color:WhiteSmoke;border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>最新告警时间</span>" + 
											"<span id='' class='warningD' style='width:12%; display:block;background-color:WhiteSmoke; border-bottom:1px solid #F00;float:left; '>告警类型</span>"+
											"</p>"+
											"<div style='width:558px;background-color: green;position: relative;left: -490px;display:none;'></div>" +

													"</span>" + 
										"</span>"
//													//新添加的结束
									);
									count ++;
								}
							}
								
							break;
						case key_one:
							if(spDataMap[key] != null && spDataMap[key].length > 0){
								var myChart_1 = echarts.init(document.getElementById('chart_one'));
								var oldLine = option_one.series[option_one.series.length-1];
								if(oldLine.id!="realTime"){
									var lineStyle = new Object();
									lineStyle.color = 'blue';
									var normal = new Object();
									normal.lineStyle = lineStyle;
									var itemStyle = new Object();
									itemStyle.normal = normal;
									var realTime = new Object();
									realTime.id = "realTime";
									realTime.name = "实时谱线";
									realTime.data = formatData_chart(spDataMap[key], start, stop);
									realTime.type = 'line';
									realTime.smooth = true;
									realTime.symbol = 'none';
									realTime.itemStyle = itemStyle;
									option_one.series.push(realTime);
									option_one.color.push('blue');
									option_one.legend.data.push('实时谱线');
								}else{
									oldLine.data = formatData_chart(spDataMap[key], start, stop);
								}
								myChart_1.setOption(option_one);
//								chart_one.setTitle(null, { text: subTitle });
//								if(chart_one.get("realTime") == null){
//									chart_one.addSeries({                       
//									    id: "realTime",
//									    name: "实时谱线",
//									    color: 'blue',
//									    type: 'line',
//									    visible: true,
//									    data: formatData_chart(spDataMap[key], start, stop)
//									},true);
//								} else {
//									chart_one.get("realTime").setData(formatData_chart(spDataMap[key], start, stop),true);
//								}
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
											"<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;'onclick='showWarningDetails_chart(" + start + ", " + stop + ", "  + warningData1[i].startFrequency + ", " + warningData1[i].stopFrequency + ", " + warningData1[i].centerFrequency + ")'>查看</button>" + 
											"&nbsp;<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='releaseWarning_chart(" + start + ", " + stop + ", "  + warningData1[i].centerFrequency + ")'>释放</button>" + 
										"</span>"
									);
									count ++;
								}
							}
							
							break;
						case key_two:
							if(spDataMap[key] != null && spDataMap[key].length > 0){
								var myChart_2 = echarts.init(document.getElementById('chart_two'));
								var oldLine = option_two.series[option_two.series.length-1];
								if(oldLine.id!="realTime"){
									var lineStyle = new Object();
									lineStyle.color = 'blue';
									var normal = new Object();
									normal.lineStyle = lineStyle;
									var itemStyle = new Object();
									itemStyle.normal = normal;
									var realTime = new Object();
									realTime.id = "realTime";
									realTime.name = "实时谱线";
									realTime.data = formatData_chart(spDataMap[key], start, stop);
									realTime.type = 'line';
									realTime.smooth = true;
									realTime.symbol = 'none';
									realTime.itemStyle = itemStyle;
									option_two.series.push(realTime);
									option_two.color.push('blue');
									option_two.legend.data.push('实时谱线');	
								}else{
									oldLine.data = formatData_chart(spDataMap[key], start, stop);
								}
								myChart_2.setOption(option_two);
//								chart_two.setTitle(null, { text: subTitle });
//								if(chart_two.get("realTime") == null){
//									chart_two.addSeries({                       
//									    id: "realTime",
//									    name: "实时谱线",
//									    color: 'blue',
//									    type: 'line',
//									    visible: true,
//									    data: formatData_chart(spDataMap[key], start, stop)
//									},true);
//								} else {
//									chart_two.get("realTime").setData(formatData_chart(spDataMap[key], start, stop),true);
//								}
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
											"<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;'onclick='showWarningDetails_chart(" + start + ", " + stop + ", "  + warningData2[i].startFrequency + ", " + warningData2[i].stopFrequency + ", " + warningData2[i].centerFrequency + ")'>查看</button>" + 
											"&nbsp;<button type='button' style='font-size:12px; line-height:14px; padding:0px; height:20px;' onclick='releaseWarning_chart(" + start + ", " + stop + ", "  + warningData2[i].centerFrequency + ")'>释放</button>" + 
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
						case chart_zero:
							if(demodResultMap[key] != null)
								chart_zero.get("dr").setData(demodResultMap[key],true);
							break;
						case key_one:
							if(demodResultMap[key] != null)
								chart_one.get("dr").setData(demodResultMap[key],true);
							break;
						case key_two:
							if(demodResultMap[key] != null)
								chart_two.get("dr").setData(demodResultMap[key],true);
							break;
						}
					}
				}
			}
		}
	};
	
	ws_chart.onclose = function (event) {
		if(moniterTimer_chart != null){
			clearInterval(moniterTimer_chart);
			moniterTimer_chart = null;
		}
		
		if (ws_chart != null) {
			ws_chart.close();
			ws_chart = null;
		}
		
		connected = false;
	};
	connected = false;
}

function disconnect_chart() {
	if (ws_chart != null) {
		ws_chart.close();
		ws_chart = null;
	}
	
	if(moniterTimer_chart != null){
		clearInterval(moniterTimer_chart);
		moniterTimer_chart = null;
	}
	
	connected = false;
}

function getMonitorData() {
	if(ws_chart == null && moniterTimer_chart != null){
		clearInterval(moniterTimer_chart);
		moniterTimer_chart = null;
		return;
	}
	
	if(!connected){
		return;
	}
	
	timestample = Math.random().toString();
	var temp = {"browser_code":"900", "deviceNum":deviceNum, "timestample":timestample, "business":"realtime"};
	
	ws_chart.send(JSON.stringify(temp));
}

function stopMonitor() {

	if (moniterTimer_chart != null) {
		
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
					clearInterval(moniterTimer_chart);
					moniterTimer_chart = null;
					
					if(ws_chart != null || connected){
						var temp = {"browser_code":"100"};
						ws_chart.send(JSON.stringify(temp));
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

function formatData_chart(arrObj, startFrequency, stopFrequency){
	var data = [];
	if(arrObj != null) {
		var len = arrObj.length;
		var start = parseFloat(startFrequency);
		var stop = parseFloat(stopFrequency);
		var step = parseFloat((stop - start)/(len - 1));
		
	    for (i = 0; i < len; i ++) { 
	   		data[i]=[Math.round((start + step * i) * 10000) / 10000, arrObj[i]];
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
var warningDetailDivReal;
var warningDetailPReal;
function showWarningDetails_chart(start, stop, startFrequency, stopFrequency, centerFrequency,te){
	//实时播放点击告警列表查看停止监控设备开始
	clearInterval(moniterTimer_chart);
	moniterTimer_chart = null;
	$("#startButton").show();
	$("#stopButton").hide();
	//实时播放点击告警列表查看停止监控设备结束
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
//					"<span style='width:10%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
//					"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + startFrequency + "</span>" + 
//					"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + stopFrequency + "</span>" + 
//					"<span style='width:12%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + centerFrequency + "</span>" +
//					"<span style='width:8%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '> 1 </span>" +
//					"<span style='width:32%; display:block; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + temp + "</span>" + 
//					"<span style='width:14%; display:block; border-bottom:1px solid #F00;float:left; '>幅值越界</span>"
					"<div onclick='showSpectraReal(event)'>"+
					"<span style='width:8%; display:block;background-color:	WhiteSmoke; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + count + "</span>" + 
					"<span style='width:12%; display:block;background-color:WhiteSmoke; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + startFrequency + "</span>" + 
					"<span style='width:12%; display:block;background-color:WhiteSmoke; border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + stopFrequency + "</span>" + 
					"<span style='width:12%; display:block; background-color:WhiteSmoke;border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '>" + centerFrequency + "</span>" +
					"<span style='width:12%; display:block; background-color:WhiteSmoke;border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; '> 1 </span>" +
					"<span style='width:32%; display:block; background-color:WhiteSmoke;border-right:1px solid #F00;float:left; border-bottom:1px solid #F00; word-break:normal; white-space:pre-wrap; word-wrap:break-word; overflow:hidden; '>" + temp + "</span>" + 
					"<span style='width:12%; display:block; background-color:WhiteSmoke;border-bottom:1px solid #F00;float:left; '>幅值越界</span>"+
					"</div>"
					count ++;
				}
//				$("#warningDetail").empty();
//				$("#warningDetail").html(text);
				warningDetailDivReal = te.getElementsByTagName("div")[0];
				
				warningDetailPReal=te.getElementsByTagName("p")[0];
					  $(warningDetailDivReal).empty();
					 $(warningDetailDivReal).html(text);
			} else {
				layer.alert("未能获取解调统计结果！");
			}
		}
	})
//	$('#detailModal').modal('show');
	  if (warningDetailPReal.style.display == "block") {
		  warningDetailPReal.style.display = "none";
         }
            else {
            	warningDetailPReal.style.display = "block";
            }
if (warningDetailDivReal.style.display == "block") {
	warningDetailDivReal.style.display = "none";
}
else {
	warningDetailDivReal.style.display = "block";
}

}

function showSpectraReal(e){
	  warningDetailPReal.style.display = "block";
		warningDetailDivReal.style.display = "block";
	e.stopPropagation();
}
//	$.ajax({
//		url: "/Tsme/history/getSpectraData",
//		type: "POST",
//		data: {createTime:createTime},
//		async: true,
//		dataType: 'json',
//		success: function(data) {
////			if(data != null){
////				var spDataList = data.spDataList;
////				if(chart0.get("realLine") == null){
////					if(spDataList != null){
////							chart0.addSeries({                       
////							    id: "realLine",
////							    name: "异常曲线",
////							    color: 'black',
////							    type: 'line',
////							    visible: true,
////							    data: formatData(spDataList, startFrequency, stopFrequency)
////							},true);
////					}
////				} else {
////					chart0.get("realLine").setData(formatData(spDataList, startFrequency, stopFrequency),true);
////				}
//////				$("#current").html(data.index + 1);
//////				$("#createTime").html(createTime);
//////				$("#LNG").html(data.LNG);
//////				$("#LAT").html(data.LAT);
////			}
////		},
////		error: function(){
////			layer.msg('频谱数据加载中!', {icon: 2});
////        }
//	})
//	 
//         	

//}

function releaseWarning_chart(start, stop, centerFrequency){
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
