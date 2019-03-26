<%@ page import="utils.task.*,java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试数据</title>
</head>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery.include.js"></script>
<script type="text/javascript">
	$.includePath = "<%=basePath%>";
	<%
	ArrayList<String> list = null;// MonitorDataFile.getDataFileList();
	String loadjs = "",objjs="";
	loadjs +="$.include([";
	objjs +="objFiles = [";
	for(int i=0; i<list.size(); i++){
	//for(int i=0; i<0; i++){
		loadjs +="'data/"+list.get(i)+"'";
		objjs +="dataArray"+list.get(i).substring(0,list.get(i).length()-3)+"";
		
		if(i<list.size()-1){
			loadjs +=",";
			objjs +=",";
		}
	}
	loadjs +="]);";
	objjs +="];";
	
	out.write(loadjs);
	out.write("\r\n");
	//out.write(objjs);
	%>
</script>
<!-- 引用的文件 -->
<script type="text/javascript" src="/Tsme/js/echarts/echarts.common.min.js"></script>
<script>
var chart;
var objFiles = new Array();
var isLoadingOver = false;
var startFrequency = null;
var stopFrequency = null;
var chartHeight = 600;
var chart;
var xdata;

var pointerSum = 0;
var allData = [];//原始数据数组
var avgArray = new Array();//均线数组
var extremeArray = new Array();//均线数组
var avgRangeArray = new Array(); //差值数组
var pointTotal = 1000;//每行绘制多少个点，包括原始数据和均值数据
var initRowsBegin = 0;//原始数据行数
var initRowsEnd = 100;//原始数据行数

function hightchart() {
	chart = echarts.init(document.getElementById('container'));
	// 显示标题，图例和空的坐标轴
	chart.setOption({
		title: {
			text: '异步数据加载示例'
		},
		tooltip : {
	        trigger: 'axis',
	        showDelay : 0,
	        textStyle: 'blue',
	        fontWeight: 'bolder',
	        showContent: true,
	        axisPointer:{
	            show: true,
	            type : 'cross',
	            lineStyle: {
	                type : 'dashed',
	                width : 1
	            }
	        }
	    },
		legend: {
			data:['min','max','一次均值','二次均值','三次均值','四次均值','五次均值','六次均值','九次均值','原始数据','极值']
		},
		xAxis: {
			type : 'value',
			scale: true
		},
		yAxis: {
			type : 'value',
			scale: true
		},
		series: [{
			name:'频谱图',
            type:'scatter',
			symbolSize: 1,
            large: true,
			data: []
		}]
	});
}
$(function () {
	//chart.series[0].setData(formatData(dataArray20150116001[0]));
	//var obj = [dataArray20150116001,dataArray20150116003,dataArray20150116004];
	<% out.write(objjs); %>
	
	getJsFileList();
});

/* function datanum(arr){
	var data1 = [];
	for(i=0;i<arr.length;i++){
	  data1.push(formatData(arr[i]));
	}
	console.log(data1);
	return data1;
} */

function formatData(arrObj){
	var len = pointTotal;//arrObj.length;
	var start = parseInt($('input[name=startFrequency]').val());
	var stop = parseInt($('input[name=stopFrequency]').val());
	var step = Number((stop-start)/len).toFixed(1);
	var data = [];
    for (i = 0; i <len; i++) { 
   		data[i]=[start,arrObj[i]];
		start += parseInt(step); 
    } 
    //console.log(data);
    return data; 
}

function formatDataExtreme(arrObj){
	var len = pointTotal;//arrObj.length;
	var start = parseInt($('input[name=startFrequency]').val());
	var stop = parseInt($('input[name=stopFrequency]').val());
	var step = Number((stop-start)/len).toFixed(1);
	var data = [];
    for (i = 0; i <len; i++) { 
		if(arrObj[0]=="undefined"){
			data[i]=[start,-120];
		}else{
			data[i]=[start,arrObj[i]];
		}
		start += parseInt(step); 
    }
    return data; 
}

function initxdata(){
	var len = pointTotal;//arrObj.length;
	var start = parseInt($('input[name=startFrequency]').val());
	var stop = parseInt($('input[name=stopFrequency]').val());
	var step = Number((stop-start)/len).toFixed(1);
	var data = [];
    for (i = 0; i <len; i++) { 
   		data[i]=start;
		start += parseInt(step); 
    }
    xdata = data;
    //console.log("xdata: "+xdata);
    return data; 
}

function getAvgData(){
	$.ajax({
		url : "./data/getAvgDataList/9/",
		type : "POST",
		data : $("#deviceParaForm").serialize(),
		dataType : 'json',
		success : function(data) {
			avgArray = data;
			isLoadingOver = true;
			$("#jsFileListSpan").html("数据装载完毕，请绘制");
			$("#drawButton").css('display','inline');
		}
	})
}

function getExtremeData(){
	$.ajax({
		url : "./data/getAvgExtreme/9/",
		type : "POST",
		data : $("#deviceParaForm").serialize(),
		dataType : 'json',
		success : function(data) {
			extremeArray = eval(data);
		}
	})
}
function getAvgRangeData(){
	$.ajax({
		url : "./data/getrangeList/1/",
		type : "POST",
		data : $("#deviceParaForm").serialize(),
		dataType : 'json',
		success : function(data) {
			avgRangeArray = data;
			console.log("avgRange: " + data );
		}
	})
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
			//$("#jsFileListSpan").html(" 文件个数:"+data.length+" 文件名称分别为："+ data);
			console.log(data);
			getAvgRangeData();
			getExtremeData();
			getAvgData();
		}
	});
	
	
}

function loadJsFiles(){
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
	if ($('input[name=chartHeight]').val() == "") {
		chartHeight = 600;
	} else {
		chartHeight = $('input[name=chartHeight]').val();
	}
	if ($('input[name=pointTotal]').val() == "") {
		pointTotal = 300;
	} else {
		pointTotal = $('input[name=pointTotal]').val();
	}
	if ($('input[name=initRowsBegin]').val() == "") {
		initRowsBegin = 100;
	} else {
		initRowsBegin = $('input[name=initRowsBegin]').val();
	}
	if ($('input[name=initRowsEnd]').val() == "") {
		initRowsEnd = 100;
	} else {
		initRowsEnd = $('input[name=initRowsEnd]').val();
	}
	 
	$('#container').css('height',chartHeight);
	
	initxdata();
	hightchart();
	if(!isLoadingOver)
		alert("数据尚未装载完毕，稍后再试...")
	
	if(objFiles.length>0){
		var objIndex = 0;index=0;
		addseriesIta(objFiles,objIndex,index,objFiles[index].length);
	}
}

function addseriesIta(obj,objIndex,index,max){
	var allData = [];
	for(var i=initRowsBegin; i < initRowsEnd; i++){
		allData = allData.concat(formatData(obj[objIndex][i]));
		pointerSum += obj[objIndex][index].length;
		index++;
	}
	console.log(extremeArray[0]);
	chart.setOption({
		series: [/*{
			// 根据名字对应到相应的系列
			name: 'min', type: 'line',symbol: 'none',symbolSize:'1',
			data: formatData(avgArray[0])
		},{
			// 根据名字对应到相应的系列
			name: 'max',type: 'line',symbol: 'none',symbolSize:'1',
			data:  formatData(avgArray[1])
		},{
			// 根据名字对应到相应的系列
			name: '一次均值',type: 'line',symbol: 'none',symbolSize:'2',
			data:  formatData(avgArray[2])
		},{
			// 根据名字对应到相应的系列
			name: '二次均值',type: 'line',symbol: 'none',symbolSize:'2',
			data:  formatData(avgArray[3])
		},{
			// 根据名字对应到相应的系列
			name: '四次均值',type: 'scatter',symbolSize:'3',
			data:  formatData(avgArray[5])
		},*//*{
			// 根据名字对应到相应的系列
			name: '四次均值',type: 'line',symbol: 'none',symbolSize:'1',
			data:  formatData(avgArray[5])
		},{
			// 根据名字对应到相应的系列
			name: '五次均值',type: 'line',symbol: 'none',symbolSize:'1',
			data:  formatData(avgArray[6])
		},*/{
			// 根据名字对应到相应的系列
			name: '九次均值',type: 'line', symbolSize:'1',
			data:  formatData(avgArray[10])
		},/*{
			// 根据名字对应到相应的系列
			name: '原始数据', type: 'scatter',symbolSize:'1',
			data: allData
		},*/{
			// 根据名字对应到相应的系列
			name: '极值', type: 'scatter',symbolSize:'8',
			data: formatDataExtreme(extremeArray)
		}]
	});
	$("#jsFileListSpan").html("已经绘制数据,第 "+ (objIndex+1) + " 个文件 ，第 " + index +" 条数据 ，点数总计：" +pointerSum +" ");
}

var dataIndex = 0;
var intervalTimer;
function updateInit(){
	intervalTimer = setInterval(function () {
		jisuan(dataIndex,objFiles[0][dataIndex],avgArray[5],avgRangeArray[0][0]);  
		var thisdata = allData.concat(formatData(objFiles[0][dataIndex]));
		 
		 chart.setOption({
			series: [{
				// 根据名字对应到相应的系列
				name: '原始数据', type: 'line',symbolSize:'1',
				data: thisdata
			}]
		});
		console.log(dataIndex);
		dataIndex++;
		if(dataIndex >= objFiles[index].length)
		{
			clearInterval(intervalTimer);
		}
	}, 500);
}
var alertRange;
function jisuan(index,initList,avgList,range){
	var begin = false;
	var beginIndex=0,endIndex=0;
	var msg = "<br/>预警 第" + index + " 条信息：";
	for(var i=0; i < initList.length; i++){
		console.log(initList[i]+"=="+(avgList[i]+range)+" =="+initList[i]<(avgList[i]+range));
		if(initList[i]> (avgList[i]+range)){
			begin = false;
			endIndex = i;
			if(beginIndex>0 && endIndex-beginIndex>5){
				msg += "[" + beginIndex +"-" + endIndex + "] "
				beginIndex=0;
			}
		}else{
			begin = true;
			beginIndex = i;
		}
	}
	if(msg !=  "<br/>预警 第" + index + " 条信息："){
		$("#alertMsgSpan").append(msg+"<br/>");
	}
}
function stopUpdateInit(){
	clearInterval(intervalTimer);
}
</script>
<body>
<form name="deviceParaForm" id="deviceParaForm" action=""
	onsubmit="submitForm()">
	startFrequency:<input type="text" name="startFrequency" value="1" /> 
	stopFrequency:<input type="text" name="stopFrequency" value="600" /> 
	图形高度:<input type="text" name="chartHeight" value="800" /> 
	原始数据开始行数:<input type="text" name="initRowsBegin" value="0" />  
	结束行数:<input type="text" name="initRowsEnd" value="0" />  
	每行数据点数:<input type="text" name="pointTotal" value="600" /> 
	<span  id="drawButton" style="display:none">
	<button type="button" onclick="loadJsFiles()">绘制图形</button>&nbsp;
	<button type="button" onclick="updateInit()">动态更新</button>&nbsp;
	<button type="button" onclick="stopUpdateInit()">停止更新</button>&nbsp;
	</span>	<br/>
<span id="jsFileListSpan">数据装载中，请稍后......</span><span id="draw_msg"></span>
</form>
<hr>
<div id="container" style="min-width:1500px;height:500px"></div>
<div id="alertMsgSpan"></div>
</body>
</html>