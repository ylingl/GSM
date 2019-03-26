var routeTimeBe=null;//初始化开始时间
var routeTimeE=null;//初始化结束时间 
var minRange=null;
var maxRange=null;
var minRangeLx=null;
var maxRangeLx=null;
var flagLianDong;
var leftLayerOverlay;
 var markerArr = [];
 var paramData;
 var option;
 var optionPieRxl;
 var indexHis=0;
// var num = 0;
 var echartsData = [];
 var myChart;
 var myChartPie;
 var routeTableContent=[];
 var $tablesZhu = $('#routeMainTable tr');//主小区表格
 var lengthZhu = $tablesZhu.length;//主小区表格行数
 var $tablesLin = $('#routeMainTableLin tr');//邻小区表格
 var lengthLin = $tablesLin.length;//邻小区表格行数
 var rxlRxqVal;
 var pause2;
 var rxqData = [];
 var rxl1 = [];
 var rxl2 = [];
 var rxl3 = [];
 var rxl4 = [];
 var rxl5 = [];
 var rxl6 = [];
 //主小区和邻小区rxl计算开始
 var rxl;
 var rxl11;
 var rxl22;
 var rxl33;
 var rxl44;
 var rxl55;
 var rxl66;
 //主小区和邻小区rxl计算结束
 var routeXdata=[];
 var flag = 36
 var tableContent = [];
 var tableContentMany = [];
 var strYearMonthDayParam;//地图上显示离线数据时传入的日期
 var timeStrParam;//地图上显示离线数据时传入的时间
 var clickLineMapPoint;
 var clickLineMapPointSource;
 var clickLineMapPointLayer;
 //将秒变为时分秒格式（大小范围总测试时长会用到）
 function formatSeconds(value) {
     if(value == undefined)
     {
         value = 0;
     }
     var second = parseInt(value);// 秒
     var min = 0;// 分
     var hour = 0;// 小时
     if(second > 60) {
         min = parseInt(second/60);
         second = parseInt(second%60);
         if(min > 60) {
             hour = parseInt(min/60);
             min = parseInt(min%60);
         }
     }
     var result = ""+parseInt(second)+"秒";
     if(min > 0) {
         result = ""+parseInt(min)+"分"+result;
     }
     if(hour > 0) {
         result = ""+parseInt(hour)+"小时"+result;
     }
     return result;
 }
 $("#routeParam").change(function () {
     if ($("#routeParam").find("option:selected").text() == 'Rx Lel') {
    	 rxlRxqVal=$("#routeParam").find("option:selected").text();//用户选取的参数后台展示地图需要
     } else if ($("#routeParam").find("option:selected").text() == 'Rx Qual') {
    	  rxlRxqVal=$("#routeParam").find("option:selected").text();//用户选取的参数后台展示地图需要
       
     }
 });
 
 
 $(".routeTable").resizable();//设置缩放
 $(".routeTable").draggable();//设置拖动
 $(".routeChartBox").resizable();//设置缩放
 $(".routeChartBox").draggable({handle: ".routeDragChart"});//设置拖动
 /**
  * 历史播放
  */
 function timeSetHistory(data) {
     var fps = 2;
     var now;
     var then = Date.now();
     var interval = 1000 / fps;
     var delta;
     window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;
     function tick1() {
         if (data[indexHis][0] >= flag) {
             return;
         }
         if (window.requestAnimationFrame) {
             pause2 = requestAnimationFrame(tick1);
             now = Date.now();
             delta = now - then;
             if (delta > interval) {
                 // 这里不能简单then=now，否则还会出现上边简单做法的细微时间差问题。例如fps=10，每帧100ms，而现在每16ms（60fps）执行一次draw。16*7=112>100，需要7次才实际绘制一次。这个情况下，实际10帧需要112*10=1120ms>1000ms才绘制完成。
                 then = now - (delta % interval);
                
                 if (option.xAxis[0].max - data[indexHis][0] < 1 || option.xAxis[1].max - data[indexHis][0] < 1) {
                     option.xAxis[0].min = option.xAxis[0].min + 8;
                     option.xAxis[1].min = option.xAxis[1].min + 8;
                     option.xzAxis[0].max = option.xAxis[0].min + (0.5 * 30);
                     option.xAxis[1].max = option.xAxis[1].min + (0.5 * 30);
                     myChart.clear();
                     myChart.setOption(option);
                 }
                 indexHis++;
//                 console.log(indexHis);
                 myChart.dispatchAction({
                     type: 'showTip',
                     seriesIndex: 0,
                     dataIndex: indexHis
                 });
//                 option.series[2].data = [[data[j][0], 0], [data[j][0], 20]];
//                 option.series[5].data = [[data[j][0], -150], [data[j][0], -30]];
//                 myChart.setOption(option,true);
              
//                 var $tables = $('#routeMainTable tr');
//                 var length = $tables.length;
//             for (var i = 0; i < lengthZhu; i++) {
//                 $("#routeMainTable").find("tr").eq(i).children("td").eq(1).html(dataRxlLxTable0[indexHis - 1][i]);
//                 $("#routeMainTableRightZhu").find("tr").eq(i).children("td").eq(1).html(dataRxlLxTable0[indexHis - 1][i]);
//             }
//             for (var i = 0; i < lengthLin; i++) {
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(1).html(dataRxlLxTable1[indexHis - 1][i]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(1).html(dataRxlLxTable1[indexHis - 1][i]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(2).html(dataRxlLxTable2[indexHis - 1][i]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(2).html(dataRxlLxTable2[indexHis - 1][i]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(3).html(dataRxlLxTable3[indexHis - 1][i]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(3).html(dataRxlLxTable3[indexHis - 1][i]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(4).html(dataRxlLxTable4[indexHis - 1][i]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(4).html(dataRxlLxTable4[indexHis - 1][i]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(5).html(dataRxlLxTable5[indexHis - 1][i]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(5).html(dataRxlLxTable5[indexHis - 1][i]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(6).html(dataRxlLxTable6[indexHis - 1][i]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(6).html(dataRxlLxTable6[indexHis - 1][i]);
//             }
             }
         }
//         else {
//             setTimeout(tick, interval);
//             option.series[2].data = [[data[j][0], 0], [data[j][0], 20]]
//             option.series[5].data = [[data[j][0], -150], [data[j][0], -30]]
//             myChart.setOption(option);
//         }

     }

     tick1();
 }

 //封装历史播放函数结束
 
 
 if(parseFloat($("#minRange").val())!=NaN&&parseFloat($("#maxRange").val())!=NaN){
	 
	 var minRange=parseFloat($("#minRange").val());//用户选取的参数范围后台展示地图需要
	 var maxRange=parseFloat($("#maxRange").val());//用户选取的参数范围后台展示地图需要
 }else{
	 minRange='';
	 maxRange='';
 }
 
//arcgis底图
 var mousePositionControl = new ol.control.MousePosition({
     coordinateFormat: ol.coordinate.createStringXY(8),
     projection: 'EPSG:4326',
     // comment the following two lines to have the mouse position
     // be placed within the map.
     className: 'routePointLat',
//     target: document.getElementById('mouse-position'),
     undefinedHTML: '&nbsp;'
 });
 var bilichi=new ol.control.ScaleLine({
     units: 'metric'
 })
// var url = 'http://121.42.251.175:6080/arcgis/rest/services/china/MapServer';
// var layers=new ol.layer.Image({
//     //        extent: [-13884991, 2870341, -7455066, 6338219],
//     source: new ol.source.ImageArcGISRest({
//         ratio: 1,
//         params: {},
//         url: url
//     })
// })
 
/**
 * 谷歌交通地图
 */
 var googleMapLayer = new ol.layer.Tile({
         source: new ol.source.XYZ({
             url:'http://www.google.cn/maps/vt/pb=!1m4!1m3!1i{z}!2i{x}!3i{y}!2m3!1e0!2sm!3i345013117!3m8!2szh-CN!3scn!5e1105!12m4!1e68!2m2!1sset!2sRoadmap!4e0'
         })
     });
/**
 * 谷歌卫星地图
 */
 var googleSatelliteLayer = new ol.layer.Tile({
         source: new ol.source.XYZ({
             url:'http://mt2.google.cn/vt/lyrs=y&hl=zh-CN&gl=CN&src=app&x={x}&y={y}&z={z}&s=G'//谷歌卫星地图 混合
         }),
         projection: 'EPSG:3857'
     });
 /**
  * 谷歌地形地图
  */
 var googleTerrainLayer = new ol.layer.Tile({
     source: new ol.source.XYZ({
         url:'http://mt3.google.cn/vt/lyrs=t@131,r@216000000&hl=zh-CN&gl=CN&src=app&x={x}&y={y}&z={z}&s=Gal'//谷歌地形地图
     }),
     projection: 'EPSG:3857'
 });
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
     center: ol.proj.transform([113.78453521994804,34.765258094239705], 'EPSG:4326', 'EPSG:3857'),
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
     }).extend([mousePositionControl,bilichi]),
//     controls: ol.control.defaults().extend([
//         new ol.control.ScaleLine({
//             units: 'metric'
//         }),
//     ]),

     target: 'routeMapId',
     view: view
 });
 

////加载离线地图
// var view = new ol.View({
//     // 设置北京为地图中心
//     center: ol.proj.transform([116.319000, 39.896000], 'EPSG:4326', 'EPSG:3857'),
//     zoom: 11
// })
//
// var map = new ol.Map({
//     controls: ol.control.defaults().extend([
//         new ol.control.ScaleLine({
//             units: 'metric'
//         }),
//     ]),
//     view: view,
//     target: 'routeMapId'
// }); 
 // map添加wms图层开始121.42.251.175:8000   localhost:8080
 var wmsUrl = '/geoserver/myGis/wms';
 var taskReady = [];
 var strBSub;
 var routeFeatures;
 var dgOverlay;
 /**
  * 删除图层
  * @param layer
  * @returns
  */
 //删除图层函数开始
 var DC={};
  DC.controlLayerName="";
  DC.removeName="";

 //删除图层函数结束
 function removeLayers(layer) {
     if (DC.controlLayerName.indexOf(",") == -1) {
         DC.controlLayerName = "";
     } else {
         if (DC.controlLayerName.indexOf(layer) == 0) {
             DC.removeName = layer + ",";
             DC.controlLayerName = DC.controlLayerName
                     .replace(DC.removeName, "");
         } else {
             DC.removeName = "," + layer;
             DC.controlLayerName = DC.controlLayerName
                     .replace(DC.removeName, "");
         }
     }
     for (var i = 1; i < map.getLayers().a.length; i++) {
//         console.log(map.getLayers().a);
         if (map.getLayers().a[i].S.source.f != null && map.getLayers().a[i].S.source.f != undefined) {
             if (map.getLayers().a[i].S.source.f.LAYERS == layer) {
//             	console.log(map.getLayers().a[i]);
                 map.removeLayer(map.getLayers().a[i]);
                 break;
             }
         }
     }
 }
// 关闭图层属性弹出框
        function closeleftMapTCK() {
        	if (leftLayerOverlay != null) {
        		map.removeOverlay(leftLayerOverlay);
        		leftLayerOverlay = null;
        	}
        	return true;
        }
        
        
        // 点击地图对象时的通用弹出框routeMainTableRightLin
        function showMapfeature(contentId) {
        	
         	map.removeOverlay(dgOverlay);
//         	 for (var k = 0; k < lengthZhu; k++) {
//	               $("#routeMainTable").find("tr").eq(k).children("td").eq(1).html(dataRxlLxTable0[contentId][k]);
//	               $("#routeMainTableRightZhu").find("tr").eq(k).children("td").eq(1).html(dataRxlLxTable0[contentId][k]);
//	            }
//         	for (var k = 0; k < lengthLin; k++) {
//         		 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(1).html(dataRxlLxTable1[contentId][k]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(1).html(dataRxlLxTable1[contentId][k]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(2).html(dataRxlLxTable2[contentId][k]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(2).html(dataRxlLxTable2[contentId][k]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(3).html(dataRxlLxTable3[contentId][k]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(3).html(dataRxlLxTable3[contentId][k]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(4).html(dataRxlLxTable4[contentId][k]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(4).html(dataRxlLxTable4[contentId][k]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(5).html(dataRxlLxTable5[contentId][k]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(5).html(dataRxlLxTable5[contentId][k]);
//                 $("#routeMainTableLin").find("tr").eq(i).children("td").eq(6).html(dataRxlLxTable6[contentId][k]);
//                 $("#routeMainTableRightLin").find("tr").eq(i).children("td").eq(6).html(dataRxlLxTable6[contentId][k]);
//         	}
			 var pointNum = contentId;

           option.series[3].data = [[echartsData[pointNum][0], 0], [echartsData[pointNum][0], 20]]
           option.series[4].data = [[echartsData[pointNum][0], -150], [echartsData[pointNum][0], -30]]
           if(echartsData[pointNum][0]>option.xAxis[0].max||echartsData[pointNum][0]>option.xAxis[1].max){

               option.xAxis[0].min=echartsData[pointNum][0]-10;
               option.xAxis[1].min=echartsData[pointNum][0]-10;
//               option.xAxis[0].min = option.xAxis[0].min + 0.2;
//               option.xAxis[1].min = option.xAxis[1].min + 0.2;
               option.xAxis[0].max = option.xAxis[0].min + (0.2 * 80);
               option.xAxis[1].max = option.xAxis[1].min + (0.2 * 80);

               myChart.setOption(option);
           }else if(echartsData[pointNum][0]<option.xAxis[0].min||echartsData[pointNum][0]<option.xAxis[1].min){

               option.xAxis[0].min=echartsData[pointNum][0]-0.44;
               option.xAxis[1].min=echartsData[pointNum][0]-0.44;
               option.xAxis[0].max = option.xAxis[0].min + (0.2 * 80);
               option.xAxis[1].max = option.xAxis[1].min + (0.2 * 80);

               myChart.setOption(option);
           }
           myChart.setOption(option);
//        	var obj = {};
//        	var features = routeFeatures.features[num]
//        	obj.properties = features.properties;
//        	obj.layer = features.id;
//        	if (obj.layer.indexOf('.') != -1) {
//        		obj.layer = obj.layer.substring(0, obj.layer.indexOf('.'));
//        	}
//        	if (obj.layer.indexOf('_') != -1) {
//        		obj.layer = obj.layer.substring(obj.layer.indexOf('_') + 1,
//        				obj.layer.length);
//        	}
//        	if (features.geometry.coordinates[0].length == null) {
//        		var coordinates = features.geometry.coordinates;
//        		if (coordinates.length == 2) {
//        			obj.coordinate = features.geometry.coordinates;// ol.proj.transform(features.geometry.coordinates,
//        															// 'EPSG:4326',
//        															// 'EPSG:900913');
//        		} else {
//        			obj.coordinate = [ x, y ];
//        		}
//        	} else {
//        		obj.coordinate = [ x, y ];
//        	}
//
//        		clickLeftMapfeaturePopup(obj);
        	
        }
      
        

     
        function clickLeftMapfeaturePopup(obj,x,y) {
        	//点击地图上的点和表格以及折线图联动以及显示时间开始
            $.ajax({
				 url: "/Tsme/history/queryOfflineCellDetail",
				 type:"POST",
				 async: false,
				 dataType:"json",
				 data:{"num":obj[0].properties.num},
				 success:function(datas){
					 console.log(obj[0].properties.times);
					 $("#routePointTime").html(obj[0].properties.times);
					 
					 option.dataZoom[0].start=0;
					 option.dataZoom[0].end=100;
					 option.xAxis[0].axisPointer.value=obj[0].properties.times;
					 option.xAxis[1].axisPointer.value=obj[0].properties.times;
					 myChart.setOption(option);
					 clickMapPoint("routeMainTable","routeMainTableLin","routeMainTableRightZhu","routeMainTableRightLin",datas);
				 },
				 error:function(datas){
		        	 console.log(datas);
		         }
			 })
          //点击地图上的点和表格以及折线图联动以及显示时间结束
//        	closeleftMapTCK();
//        	map.removeOverlay(dgOverlay);
//        	var oDiv = document.createElement('div');
//        	oDiv.setAttribute("id", "tckzPopr");
//        	oDiv.setAttribute("style", "position:absolute; top:-43px; left:48px;");
//        	oDiv.setAttribute("class", "box_tc tc_sj_dg");
//
//        	var html = '';
//        	html += '<div class="box_tc_cn" style="width:220px; height:100px;background-color: white;">';
//        	html += '<span onclick="closeleftMapTCK()" class="cl_gb" style="margin-left:80px;">关闭</span>';
//        	html += '	<div class="cn_cn_nr content_g mCustomScrollbar" style="max-height:270px;">';
//        	html += '		<table width="100%" class="table table-bordered table-hover table-striped zdy_bk">';
//        	html += '			<tbody>';
        	console.log(obj[0].properties.num);
//        	for (var i = 0; i < obj.length; i++) {
//        	console.log(obj[i]);
//             var long= parseFloat(obj[i].geometry.coordinates[0]);
//             var lang=parseFloat(obj[i].geometry.coordinates[1]);
//            
//             var zhuanHuanZuoBiao=ol.proj.transform([long, lang], 'EPSG:3857', 'EPSG:4326');
////             zhuanHuanZuoBiao[0]=zhuanHuanZuoBiao[0].toFixed(2);
////             zhuanHuanZuoBiao[1]=zhuanHuanZuoBiao[1].toFixed(2);
//               zhuanHuanZuoBiao[0]=zhuanHuanZuoBiao[0];
//               zhuanHuanZuoBiao[1]=zhuanHuanZuoBiao[1];
////             console.log(zhuanHuanZuoBiao);
//    			html += '<a style="text-decoration:underline; color:#555;">rxl:' + obj[i].properties.rxl + '</a><br/>'+'<a style="text-decoration:underline; color:#555;">经度:' + zhuanHuanZuoBiao[0] + '</a><br/>'+'<a style="text-decoration:underline; color:#555;">纬度:' + zhuanHuanZuoBiao[1] + '</a><br/>'+'<a style="text-decoration:underline; color:#555;">日期:' + obj[i].properties.dateStr + '</a><br/>'+'<a style="text-decoration:underline; color:#555;">时间:' + obj[i].properties.times + '</a><br/>';
////    		}
//    	}

//        	oDiv.innerHTML = html;
//        	document.body.appendChild(oDiv);
//
//        	leftLayerOverlay = new ol.Overlay(({
//        		element : document.getElementById('tckzPopr'),
//        		autoPan : true,
//        		autoPanAnimation : {
//        			duration : 250
//        		}
//        	}));
//        	map.addOverlay(leftLayerOverlay);
//         	if (obj.coordinate[0] < 500) {
//         		leftLayerOverlay.setPosition(ol.proj.transform(obj.coordinate,
//         				'EPSG:4326', 'EPSG:900913'));// 12683064.01039048,2589012.8654283723
//         	} else {
        		
//        		leftLayerOverlay.setPosition([x,y]);// 12683064.01039048,2589012.8654283723
//         	}
        }
        
        
      //查询任务列表开始
        function routeTaskLists(){
        	 $.ajax({
                 url: "/Tsme/history/queryTaskList",
                 type:"POST",
                 dataType:"json",
//                 data:{"id":1},
                	 success : function(data) {
             			 
             			$.each(data.result, function(i, val) {
             	
//             				console.log(data.result[i].taskId);
             				$("#taskContentContainter0").append('<tr>'
                                    + '<td><span style="cursor: pointer" class="searchs" onclick="routeTaskDetails(' + data.result[i].id + ')">' + data.result[i].taskName + '</span><b style="display: inline-block;width: 10px;height: 10px;background-color: red;border-radius:5px;margin-left:2px;"></b></td>'
                                    + '<td class="searchs">' + data.result[i].taskInitiator + '</td>'
//                                    + '<td><p class="searchs" style="display: inline-block">' + $(".taskN").val() + '</p></td>'
                                    + '<td>' + data.result[i].startTime + '</td>'
                                    + '<td><input type="button" class="edit" style="cursor: pointer;font-size:12px;" value="编辑" onclick="routeUpdateTask(' + data.result[i].id + ')"><input type="button" class="delete" style="cursor: pointer;font-size:12px;" value="删除" onclick="routeRemoveTask(' + data.result[i].id + ',this)"></td>'
                                    + '</tr>'
                            );
             				});
             			 
                       
             		},

                 error:function(data){
                	 console.log(data);
                 }
             }) 
        }
        //查询任务列表结束       
        //点击任务列表显示任务详情开始
        function routeTaskDetails(id){
//        	alert();
//        	$.ajax({
//                url: "http://121.42.251.175:8000/Tsme/history/queryTaskDetail",
//                type:"POST",
//                dataType:"json",
//                data:{"id":id},
//               	 success : function(data) {
//               		 $("#testContent").html('<p>设备状态:测试中</p>'
//               				+'<p>上传时间:18/06/20 16:22:22</p>'
//               				+'<p>设备位置:京沪1103</p>'
//               				+'<p>平均时速:180km/h</p>'
//               				+'<p>任务:京沪108923-zhao01A</p>'
//               				+'<p>已执行:0天2小时2分钟</p>'
//               				+'<p>发起人:赵一</p>');
//            			console.log(data.result.id);
//            		},
//
//                error:function(data){
//               	 console.log(data);
//                }
//            })
        }
        //点击任务列表显示任务详情结束
        
        //删除任务列表中的任务开始
        function routeRemoveTask(id,that){
        	$.ajax({
                url: "/Tsme/history/deleteTask",
                type:"POST",
                dataType:"json",
                data:{"id":id},
               	 success : function(data) {
               		if(confirm("确定删除吗")){
               			$(that).parent().parent().hide();
                        alert("删除成功");
                        return true;
                    }
                    return false;
            			
            		},

                error:function(data){
               	 console.log(data);
               
                }
            })
        }
        //删除任务列表中的任务结束
        //新建任务修改开始
        function routeUpdateTask(id){
        	$("#routeSave").attr({"disabled":"disabled"});
        	$("#routeUpdate").removeAttr("disabled");
       //查询任务详情
        	$.ajax({
                url: "/Tsme/history/queryTaskDetail",
                type:"POST",
                dataType:"json",
                data:{"id":id},
               	 success : function(data) {
//               		console.log(data.result.testWays);
                   var taskId=data.result.taskId;
            
               		 $('.taskName').val(data.result.taskName);
               		 $('.taskN').val(data.result.taskInitiator);
               		$('.taskN').val(data.result.taskInitiator);
//               	    var routeUpdateDevices=$('input:checkbox[name=routeDeviceLists]:checked').map(function(index,elem){
//               	     return $(elem).val();
//               	     if(data.result.deviceList){
//               	    	 
//               	     }
//                    })
               		$('#dateinfo').val(data.result.startTime);
               		$('#dateinfoEnd').val(data.result.endTime);
               		$('#routeBegin').val(data.result.startKilometres);
               		$('#routeEnd').val(data.result.endKilometres);
               	    $("#routeLine").val(data.result.testLine);
               		if(data.result.testWays==0){
               			$("#routeSao").attr("checked",true);
               		}else if(data.result.testWays==1){
               			$("#routeBo").attr("checked",true);
               		}else if(data.result.testWays==2){
               			$("#routeSao").attr("checked",true);
               			$("#routeBo").attr("checked",true);
               		}
               		$('#routeTelePhoneNum').val(data.result.Telephone);
               		$("#routeWaitTime").val(data.result.callWait);//等待呼叫接通时长
               		$("#routeIntervalTime").val(data.result.callInterval);//间隔时长
               		$("#routeCallCut").val(data.result.callCut);//掉话后间隔时长
               		$("#routeCallKeepTime").val(data.result.callKeep);//呼叫保持时长
               		$("#routeFailTime").val(data.result.callFailure);//呼叫失败间隔时长
               		$("#routeLimitLoop").val(data.result.callTime);//循环次数
               		
               		$('.theme-popover-mask').fadeIn(100);
                    $('.theme-popover').slideDown(200);
                    
               $("#routeUpdate").off("click").click(function(){
            	   $('.theme-popover-mask').fadeOut(100);
                   $('.theme-popover').slideUp(200);
            		//修改参数开始
                   var routeDeviceListsUp='';//任务列表
                   $('input:checkbox[name=routeDeviceLists]:checked').each(function(k){
                       if(k == 0){
                      	 routeDeviceListsUp = $(this).val();
                       }else{
                      	 routeDeviceListsUp += ','+$(this).val();
                       }
                   })
              
                   var routeTaskNameUp=$(".taskName").val();//任务名称
                   var routeTaskInitiatorUp=$(".taskN").val();//任务创建人
                   var routeDeviceListUp=routeDeviceListsUp;//任务列表
                   var routeStarTimeUp=$("#dateinfo").val();
                   var routeEndTimeUp=$("#dateinfoEnd").val();
                   var routeTestLineUp=$("#routeLine").find("option:selected").text();
                   var routeStartKilometresUp=$("#routeBegin").val();
                   var routeEndKilometresUp=$("#routeEnd").val();
                   var routeTestWaysUp;
                   if($('#routeSao').is(':checked')){
                  	 routeTestWaysUp=0;
                   }else if($('#routeBo').is(':checked')){
                  	 routeTestWaysUp=1;
                   }else if($('#routeBo').is(':checked')&&$('#routeBo').is(':checked')){
                  	 routeTestWaysUp=2;
                   }
                  
                   var routeTelephoneUp=$("#routeTelePhoneNum").val();//电话
                   var routeCallWaitUp=$("#routeWaitTime").find("option:selected").text();//等待呼叫接通时长（秒）
                   var routeCallIntervalUp=$("#routeIntervalTime").find("option:selected").text();//间隔时长（秒）
                   var routeCallFailureUp=$("#routeFailTime").find("option:selected").text();//呼叫失败后间隔时长
                   var routeCallCutUp=$("#routeCallCut").find("option:selected").text();//掉话时长
                 
                   var routeCallKeepUp=$("#routeCallKeepTime").find("option:selected").text();//呼叫保持时长
                   var routeCallTimeUp;
                   if($('#routeInfinitLoop').is(':checked')){
                  	 routeCallTimeUp=0;
                   }else{
                  	 routeCallTimeUp=$("#routeLimitLoop").find("option:selected").text();
                   }
                   //修改参数结束
               	$.ajax({
                       url: "/Tsme/history/updateTask",
                       type:"POST",
                       dataType:"json",
                       data:{"id":id,"taskId":taskId,"taskName":routeTaskNameUp,"taskInitiator":routeTaskInitiatorUp,"deviceList":routeDeviceListUp,"startTime":routeStarTimeUp,
                      	 "endTime":routeEndTimeUp,"testLine":routeTestLineUp,"startKilometres":routeStartKilometresUp,"endKilometres":routeEndKilometresUp,
                      	 "testWays":routeTestWaysUp,"Telephone":routeTelephoneUp,"callWait":routeCallWaitUp,"callInterval":routeCallIntervalUp,"callFailure":routeCallFailureUp,
                      	 "callCut":routeCallCutUp,"callKeep":routeCallKeepUp,"callTime":routeCallTimeUp
                      	 },
                      	 success : function(data) {
                      		if(confirm("确定修改吗")){
                      		  $("#taskContentContainter0 tr:not(:first)").html("");
                        		routeTaskLists();
                                alert("修改成功");
                                return true;
                            }
                            return false;
                          
                      		
                   		},

                       error:function(data){
                      	 console.log(data);
                      
                       }
                   })
               })
               		
            		},

                error:function(data){
               	 console.log(data);
                }
            })
             
        	
        }
        //新建任务修改结束
      //获取ID名函数开始
        function getId(id) {
            var idName = document.getElementById(id);
            return idName;
        }

//获取ID名函数结束
    $(function () {
    	 var location = new Object();//传入后端地图上经纬度的对象
    	 routeTaskLists();//任务列表初始化
         $("#routeDeviceLists").append(
                '<tr>'
                + '<td>CRH2A06CH</td>'
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

//控制测试任务的显示和隐藏
//     $("#lineCtrol").mouseover(function (){
//        $(".routeNavLeftContent").show();
//        $("#setDivIdRoute").hide();
//    }).mouseout(function (){
////        $(".routeNavLeftContent").hide();
//        $("#setDivIdRoute").hide();
//    });
        
         
    
        
        
//---------------------------------------------------------------------------------------------------------------//        
//设备列表和测试任务下拉列表开始
        $("#deviceTestSele").change(function () {
        	
            if ($("#deviceTestSele").find("option:selected").text() == '设备列表') {
            	 
                getId("routeNavLeftDeviceList").style.display = "block";
                getId("routeNavLeftTest").style.display = "none";
            } else if ($("#deviceTestSele").find("option:selected").text() == '测试任务') {
            	
                getId("routeNavLeftDeviceList").style.display = "none";
                getId("routeNavLeftTest").style.display = "block";
              
            }
        });
        $('#routeEdit').click(function () {
        	$("#routeUpdate").attr({"disabled":"disabled"});
        	$("#routeSave").removeAttr("disabled");
            $('.theme-popover-mask').fadeIn(100);
            $('.theme-popover').slideDown(200);
            
        })
        $('.theme-poptit .close').click(function () {
            $('.theme-popover-mask').fadeOut(100);
            $('.theme-popover').slideUp(200);
        })
//设备列表和测试任务下拉列表结束
         //拨测控制内容开始
        $("#routeBo").click(function () {
            if ($("#routeBo").is(':checked')) {
                $("#numberContent").show();
            } else {
                $("#numberContent").hide();
            }
        })
        //拨测控制内容结束
        //选取时间开始
         //控制日期开始
//        jeDate({
//            dateCell: "#dateinfo",
//            format: "YYYY年MM月DD日 hh:mm:ss",
////            isinitVal:true,
//            isTime: true, //isClear:false,
//            minDate: "2014-09-19 00:00:00",
////            okfun:function(val){}
//        })
//        jeDate({
//            dateCell: "#dateinfoEnd",
//            format: "YYYY年MM月DD日 hh:mm:ss",
////            isinitVal:true,
//            isTime: true, //isClear:false,
//            minDate: "2014-09-19 00:00:00",
////            okfun:function(val){}
//        })
        //选取时间结束
//        //控制日期开始
//        jeDate({
//            dateCell: "#dateinfoHistory",
//            format: "YYYY年MM月DD日 hh:mm:ss",
////            isinitVal:true,
//            isTime: true, //isClear:false,
//            minDate: "2014-09-19 00:00:00",
////            okfun:function(val){}
//        })
//        jeDate({
//            dateCell: "#dateinfoEndHistory",
//            format: "YYYY年MM月DD日 hh:mm:ss",
////            isinitVal:true,
//            isTime: true, //isClear:false,
//            minDate: "2014-09-19 00:00:00",
////            okfun:function(val){}
//        })
//        //选取时间结束
        
        //无限循环与有限循环切换开始
         $("#routeInfinitLoop").change(function () {
            if ($("#routeInfinitLoop").is(':checked')) {
           
            	$("#routeLimitLoop").attr("disabled","disabled");
            } else {
            	$("#routeLimitLoop").removeAttr("disabled");
            }
        })
       
        //将经纬度传给后台开始
       function setGis(creatTime,rxq){
    	   $.ajax({
               url: "/Tsme/history/insertDataToMysql",
               type:"POST",
               dataType:"json",
               data:{"creatTime":creatTime,"rxq":rxq},
              	 success : function(data) {
           
           		},

               error:function(data){
             	
               }
           })
       }
        //将经纬度传给后台结束
   
        
        //保存新建任务开始
        
        $("#routeSave").click(function(){
        	
       	 if ($(".taskName").val() == "" || $(".taskN").val() == "" || $("#dateinfo").val() == "" || $("#dateinfoEnd").val() == "" || $("#chooseDevice input[type='checkbox']:checked").length < 1) {
             alert("请将信息填完整");
             return;
         }
       	if ($("#routeLine").find("option:selected").text() == "广深线") {
            
            if ($("#routeBegin").val() <= 0 || $("#routeEnd").val() >= 88) {
                alert("输入超出范围");
               
                return;
            }
        } else if ($("#routeLine").find("option:selected").text() == "广珠线") {
            if ($("#routeBegin").val() <= 0 || $("#routeEnd").val() >= 66) {
                alert("输入超出范围了");
               
                return;
            }
        }
        	 
             var timeCurrent = new Date().getTime();
             var strB = $('#dateinfo').val();
             strB = strB.replace('日', '');
             strB = strB.replace(/[\u4e00-\u9fa5]/g, '-');
             strBSub = strB.substring(5, 10);
             var timeBe = new Date(strB).getTime();
        
             var strE = $('#dateinfoEnd').val()
             strE = strE.replace('日', '');
             strE = strE.replace(/[\u4e00-\u9fa5]/g, '-');
             var timeEn = new Date(strE).getTime();
             if (timeBe > timeEn) {
     
                 alert("开始时间不能大于结束时间");
                 return;
             }
             $('.theme-popover-mask').fadeOut(100);
             $('.theme-popover').slideUp(200);
             if (timeCurrent >= timeBe && timeCurrent <= timeEn) {
                 taskReady.push("正在执行")
             } else if (timeCurrent < timeBe) {
                 taskReady.push("待执行")
             } else if (timeCurrent > timeEn) {
                 taskReady.push("已完成")
             }
             
             var routeDeviceLists='';//任务列表
             $('input:checkbox[name=routeDeviceLists]:checked').each(function(k){
                 if(k == 0){
                	 routeDeviceLists = $(this).val();
                 }else{
                	 routeDeviceLists += ','+$(this).val();
                 }
             })
        
             var routeTaskName=$(".taskName").val();//任务名称
             var routeTaskInitiator=$(".taskN").val();//任务创建人
             var routeDeviceList=routeDeviceLists;//任务列表
             var routeStarTime=$("#dateinfo").val();
             var routeEndTime=$("#dateinfoEnd").val();
             var routeTestLine=$("#routeLine").find("option:selected").text();
             var routeStartKilometres=$("#routeBegin").val();
             var routeEndKilometres=$("#routeEnd").val();
             var routeTestWays;
             if($('#routeSao').is(':checked')){
            	 routeTestWays=0;
             }else if($('#routeBo').is(':checked')){
            	 routeTestWays=1;
             }else if($('#routeBo').is(':checked')&&$('#routeBo').is(':checked')){
            	 routeTestWays=2;
             }
            
             var routeTelephone=$("#routeTelePhoneNum").val();//电话
             var routeCallWait=$("#routeWaitTime").find("option:selected").text();//等待呼叫接通时长（秒）
             var routeCallInterval=$("#routeIntervalTime").find("option:selected").text();//间隔时长（秒）
             var routeCallFailure=$("#routeFailTime").find("option:selected").text();//呼叫失败后间隔时长
             var routeCallCut=$("#routeCallCut").find("option:selected").text();//掉话时长
           
             var routeCallKeep=$("#routeCallKeepTime").find("option:selected").text();//呼叫保持时长
             var routeCallTime;
             if($('#routeInfinitLoop').is(':checked')){
            	 routeCallTime=0;
             }else{
            	 routeCallTime=$("#routeLimitLoop").find("option:selected").text();
             }

             //新建任务ajax开始
             $.ajax({
                 url: "/Tsme/history/addTask",
                 type:"POST",
                 dataType:"json",
                 data:{"taskName":routeTaskName,"taskInitiator":routeTaskInitiator,"deviceList":routeDeviceList,"startTime":routeStarTime,
                	 "endTime":routeEndTime,"testLine":routeTestLine,"startKilometres":routeStartKilometres,"endKilometres":routeEndKilometres,
                	 "testWays":routeTestWays,"Telephone":routeTelephone,"callWait":routeCallWait,"callInterval":routeCallInterval,"callFailure":routeCallFailure,
                	 "callCut":routeCallCut,"callKeep":routeCallKeep,"callTime":routeCallTime
                	 },
                	 success : function(data) {
                		 if(confirm("确定保存吗")){
                			 $("#taskContentContainter0 tr:not(:first)").html("");
                    		 routeTaskLists();
                    		 setTimeout(function() {alert("任务下发成功！");},5000);
                               alert("保存成功");
                               return true;
                           }
                           return false;
                		
             		},

                 error:function(data){
                	 alert("新建任务失败！");
                 }
             })
             //新建任务ajax结束

        })
     
        



         
  

        
     
        
        
       
       

//模拟查询的wms图层名称比如是wmsLayer
//该wmsLayer的数据源是墨卡托的3857举例
//var routeFeatures;
        map.on('click',mapClick);
        function mapClick(evt){
            var viewResolution = map.getView().getResolution();
            var coordinate = evt.coordinate;
                var source = new ol.source.TileWMS({
                    name : "wms",
                    url : wmsUrl,
                    params : {
                        'LAYERS' : 'pointByRxl',
                        FEATURE_COUNT : 5,
                        VERSION : '1.1.0'
                    },
                    serverType : 'geoserver'
                });
                var url = source.getGetFeatureInfoUrl(
                        evt.coordinate, viewResolution, 'EPSG:3857',
                        {
                            'INFO_FORMAT': 'application/json',//geoserver支持jsonp才能输出为jsonp的格式
                            'FEATURE_COUNT': 50     //点击查询能返回的数量上限
                        });
                $.ajax({
                    type: 'GET',
                    url:url,
                    dataType: 'json',
                    
                    success: function (datas) {
                        console.log(datas);
                    
						if (datas.features != "" && datas.features != null) {

							 routeFeatures=datas;
							 
							if (datas.features.length > 1) {
//								chickDGmap(datas.features, coordinate[0],
//										coordinate[1]);

							} else {
//								console.log(datas.features[0].geometry.coordinates);
								clickLeftMapfeaturePopup(datas.features,coordinate[0],
										coordinate[1]);
								
							}

						}
                    }
                });
        }

      
        
        
        function chickDGmap(obj, x, y) {
        	closeleftMapTCK();
        	map.removeOverlay(dgOverlay);
        	var oDiv = document.createElement('div');
        	oDiv.setAttribute("id", "baoantak");
        	oDiv.setAttribute("style", "position:absolute; top:-42px; left:50px;");
        	oDiv.setAttribute("class", "box_tc tc_sj_dg");

        	document.body.appendChild(oDiv);
        	dgOverlay = new ol.Overlay(({
        		element : document.getElementById('baoantak'),
        		autoPan : true,
        		autoPanAnimation : {
        			duration : 250
        		}
        	}));

        	var html = '';
        	html += '<span onclick="closeleftMapTCK()" class="cl_gb">关闭</span>';
        	html += '<div class="tc_cn_box" style="width:130px;background-color: #00A1CB">';
        	html += '<div>';
        	for (var i = 0; i < obj.length; i++) {
        			
//        			html += '<a style="text-decoration:underline; color:#555;" href="javascript:showMapfeature(' + obj[i].properties.contentId + ');">'
//        					+ obj[i].properties.timeStr
//        					+ '</a><br/>';
//        		 else {
//        			if (obj[i].id.indexOf('.') != -1) {
//        				obj[i].id = obj[i].id.substring(0, obj[i].id.indexOf('.'));
//        			}
//        			if (obj[i].id.indexOf('_') != -1) {
//        				obj[i].id = obj[i].id.substring(obj[i].id.indexOf('_') + 1,
//        						obj[i].id.length);
//        			}
//        			if (obj[i].id.indexOf('_') != -1) {
//        				obj[i].id = obj[i].id.substring(0, obj[i].id.indexOf('_'));
//        			}
        			html += '<a style="text-decoration:underline; color:#555;" href="javascript:showMapfeature(' + obj[i].properties.contentId + ');">' + obj[i].properties.timeStr + '</a><br/>';
//        		}
        	}
        	html += '</div>';
        	html += '</div>';
        	oDiv.innerHTML = html;

        	map.addOverlay(dgOverlay);
        	dgOverlay.setPosition([ x, y ]);
        }

        
       
      
   
     

//        var wmsSource = new ol.source.TileWMS({
//            url:wmsUrl,
//            params:{'LAYERS':'railway'},
//            serverType:'geoserver'
//        })
//        function getInfo() {
//            var url = wmsSource.getGetFeatureInfoUrl(
//                    [111.47003, 27.26120], map.getView().getResolution(), 'EPSG:4326',
//                    {
//                        'INFO_FORMAT': 'text/html', //这个返回的是一个html页面
//                        'FEATURE_COUNT': 5
//                    });//最大查询要素数量，默认为1
//
//            console.log(url);
//            if (url) {
////                document.getElementById('info').innerHTML = '<iframe   seamless src="' + url + '"></iframe>';
//            }
//        }
//
//       getInfo();
        //为map注册一个pointermove事件的监听
        //pointermove事件
//        map.on('pointermove', function (e) {
//            //获取map的像素位置信息
//            var pixel = map.getEventPixel(e.originalEvent);
//            //map视口中是否包含某个要素
//            var hit = map.hasFeatureAtPixel(pixel);
//            //设置符合当前条件的鼠标样式
//            map.getTargetElement().style.cursor = hit ? 'pointer' : '';
//        });
////        view.on('change', function () {
////            console.log(view.getZoom());
////        })
//        closer.onclick = function () {
//            popup.setPosition(undefined);
//            closer.blur();
//            return false;
//        };
//地图点击事件以及弹出框结束
        //控制路测点显示隐藏开始
        $("#routeLJ").change(function () {
                    if ($("#routeLJ").is(':checked')) {
                    	 removeLayers('signalPoint');
                    	 addwmsLayer('signalPoint', '');
                    } else {
                    	 removeLayers('signalPoint');
                    }
                })
        //控制路测点显示隐藏结束
                 //控制小区连线显示隐藏开始
        $("#routeLX").change(function () {
                    if ($("#routeLX").is(':checked')) {
                    	 removeLayers('devicemoveline');
                    	 addwmsLayer('devicemoveline', ''); 
                    } else {
                    	 removeLayers('devicemoveline');
                    }
                })
        //关闭饼图的盒子
            $("#routeCloseBing").click(function(){
        	$(".routeMapRightBing").hide();
//        	$(this).hide();
        })   
        //控制小区连线显示隐藏结束
        
        $(".routeTableCloseBtn").click(function(){
        	$(".routeTable").hide();
        	$(this).hide();
        })
        $("#routeChartBtn").click(function () {	
        	$(".routeRxqLine").toggle(500);
            $(".routeChart").toggle(500);
           
            $(".routeDragChart").toggle(500);
        })
        $("#routeTableBtn").click(function () {
            $(".routeTable").toggle(500);
            $(".routeTableCloseBtn").toggle(500);
        })
    })
    
    
   
    
   
    
    
    
    
    
    
   
 
   
    //添加图层
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

//-------------------------------------------------我是分割线---------------------------------------------------------

/**
 * 处理时间格式
 * @returns
 */
function dealTime(){
		routeTimeBe=null;
		routeTimeE=null;//清空缓存routeStrB
	  var routeStrB = $('#dateinfoHistory').val();
	  if(routeStrB!=""){
		  routeStrB = routeStrB.replace('日', '');
	      routeStrB = routeStrB.replace(/[\u4e00-\u9fa5]/g, '-');
	      routeTimeBe = new Date(routeStrB).getTime();//开始时间时间戳
	  }else{
		  routeTimeBe="";
	  }
     
      var routeStrE = $('#dateinfoEndHistory').val();
      if(routeStrE!=""){
    	  routeStrE = routeStrE.replace('日', '');
          routeStrE = routeStrE.replace(/[\u4e00-\u9fa5]/g, '-');
          routeTimeE = new Date(routeStrE).getTime();//结束时间时间戳
      }else{
    	  routeTimeE="";
      }
      
}

/**
 * 根据条件绘制路测信号点
 * @returns
 */

function showMapAgain(){
	removeLayers("signalPoint");
	dealTime();
	var condition="";
		//拼接时间参数
	 if(routeTimeBe!=""){
		 condition = "\"creatTime\">="+routeTimeBe;
	 }
	 if(routeTimeE!=""){
		 if(condition==""){
			 condition = "\"creatTime\"<="+routeTimeE;
		 }else{
			 condition += " and \"creatTime\"<="+routeTimeE;
		 }
	 }
	 var sel = $("#routeParam").find("option:selected").text();
	 minRange=$("#minRange").val();
	 maxRange=$("#maxRange").val();
	 //拼接范围参数
	 if(minRange!=""&&maxRange!=""){
		 if(sel=="Rx Lev"){
			 if(condition==""){
				 condition = "\"rxl\">="+minRange+""+" and \"rxl\"<="+maxRange;
			 }else{
				 condition += " and \"rxl\">="+minRange+" and \"rxl\"<="+maxRange;
			 }
		 }else if(sel=="Rx Qual"){
			 if(condition==""){
				 condition = "\"rxq\">="+minRange+" and \"rxq\"<="+maxRange;
			 }else{
				 condition += " and \"rxq\">="+minRange+" and \"rxq\"<="+maxRange;
			 }
		 }
	 }
	 
	 addwmsLayer("signalPoint", condition);
}
/**
 * 加載離線地圖
 */
function showOnlineMap(dateStr,timeStr,uploader){
	removeLayers("pointByRxl");
	var condition = "";
	if(getAccountRole()=="超级管理员"){
		if(dateStr!=undefined&&timeStr!=undefined){
			condition="dateStr=\'"+dateStr+"\' and timeStr like \'%"+timeStr+"%\'";
		}
		maxRangeLx=$("#maxRangeLx").val();
		minRangeLx=$("#minRangeLx").val();
		 //拼接范围参数
		 if(minRangeLx!=""&&maxRangeLx!=""){
			 if(condition==""){
				 condition += "\"rxl\">="+minRangeLx+" and \"rxl\"<="+maxRangeLx;
			 }else{
				 condition += " and \"rxl\">="+minRangeLx+" and \"rxl\"<="+maxRangeLx;
			 }
					 
		}
	}else{
		if(dateStr!=undefined&&timeStr!=undefined){
			condition="dateStr=\'"+dateStr+"\' and timeStr like \'%"+timeStr+"%\'";
		}
		maxRangeLx=$("#maxRangeLx").val();
		minRangeLx=$("#minRangeLx").val();
		 //拼接范围参数
		 if(minRangeLx!=""&&maxRangeLx!=""){
			 if(condition==""){
				 condition += "\"rxl\">="+minRangeLx+" and \"rxl\"<="+maxRangeLx;
			 }else{
				 condition += " and \"rxl\">="+minRangeLx+" and \"rxl\"<="+maxRangeLx;
			 }
					 
		}
		 if(condition!=""){
			 condition +=" and uploader=\'"+getAccountName()+"\'";
		 }else{
			 condition ="uploader=\'"+getAccountName()+"\'";
		 }
	}
	
	
	
//	 else if(sel=="Rx Qual"){
//				 condition += " and \"rxq\">="+minRangeLx+" and \"rxq\"<="+minRangeLx;
//		 }
	addwmsLayer('pointByRxl', condition);
}



//扫频离线数据柱状图显示隐藏j's
$("#sweepChartBtn").click(function () {	
	$("#sweepBox").toggle(500);
	$(".routeChartCloseBtn").toggle(500);
	
})


//点击关闭按钮，将柱状图和关闭按钮消失
$(".routeChartCloseBtn").click(function(){
	$(this).hide();
	$("#sweepBox").hide();
})
  

//初始化柱状图的宽度
function resize(){
	$('#sweep').css('width', $("#sweepBox").width());//设置统计区宽度
}
resize();

//静态柱状图样式
var myChart = echarts.init(document.getElementById('sweep'));
	
	console.log(myChart);
	option11 = {
	    color: ['#3398DB'],
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    grid: {
	        left: '1%',
	        right: '6%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : ['1', '2', '3', '4', '5', '6', '7','8','9', '10', '11', '12', '13','14', '15','16','17', '18', '19', '20'],
	            axisTick: {
	                alignWithLabel: true
	            },
//	            min:2,
//				max:1000,
//		        interval:0.2,
		        name:'频率(MHz)',
		        nameTextStyle:{
		        	fontSize: 10,
		        	align:'right'
		        }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
//	            min:-150,
//		        max:-40,
		        name:'电平值(dBm)',
		        nameTextStyle:{
		        	align:'center'
		        }
	        }
	    ],
	    series : [
	        {
	            name:'直接访问',
	            type:'bar',
	            barWidth: '30%',
	            data:[10, 52, 200, 334, 390, 330, 220,10, 52, 200, 334, 390, 330, 220,10, 52, 200, 334, 390, 330]
	        }
	    ]
	};
	myChart.setOption(option11);





