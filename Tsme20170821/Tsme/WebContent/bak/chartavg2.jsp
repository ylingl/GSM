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
	ArrayList<String> list = null;//MonitorDataFile.getDataFileList();
	String loadjs = "",objjs="";
	loadjs +="$.include([";
	objjs +="obj = [";
	//for(int i=0; i<list.size(); i++){
	for(int i=0; i<1; i++){
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
/* 	$(document).ready(function(){
		alert("文件1的行数 "+dataArray20150116001[1]);
		alert("文件2的行数 "+dataArray20150116002.length);
		alert("文件3的行数 "+dataArray20150116003.length);
		alert("文件4的行数 "+dataArray20150116004.length);
		alert("文件5的行数 "+dataArray20150116005.length);
		alert("文件6的行数 "+dataArray20150116006.length);
	}); */
	
</script>
<!-- 引用的文件 -->
<script type="text/javascript" src="/Tsme/js/echarts/echarts.common.min.js"></script>
<script>
var chart;
var obj;
var isLoadingOver = false;
var startFrequency = null;
var stopFrequency = null;
var chart;
function hightchart() {
	chart = echarts.init(document.getElementById('container'));
	// 显示标题，图例和空的坐标轴
	chart.setOption({
		title: {
			text: '异步数据加载示例'
		},
		tooltip: {},
		legend: {
			data:['销量']
		},
		xAxis: {
			max : stopFrequency, //Y轴最大值 
			min : startFrequency, //Y轴最小值 
			type : 'value',
            scale:true
		},
		yAxis: {
			max : null, //x轴最大值 
			min : null
		},
		series: [{
			name:'频谱图',
            type:'scatter',
			symbolSize: 2,
            large: true,
			data: []
		}]
	});
}
$(function () {
	//chart.series[0].setData(formatData(dataArray20150116001[0]));
	//var obj = [dataArray20150116001,dataArray20150116003,dataArray20150116004];
	<% out.write(objjs); %>
	isLoadingOver = true;
	$("#jsFileListSpan").html("数据装载完毕，请绘制");
});

var pointerSum = 0;
var allData = [];
function addseriesIta(obj,objIndex,index,max,leng){
	setTimeout(function(){
		//var ser = chart.addSeries({
		//	color : 'red',
		//} );
		var allData = [];
		for(var i=0; i< 100; i++){
			allData = allData.concat(formatData(obj[objIndex][i]));
			pointerSum += obj[objIndex][index].length;
			index++;
		}
		console.log(allData);
		//ser.setData(allData, true);
		chart.setOption({
			series: [{
				// 根据名字对应到相应的系列
				name: '销量',
				data: allData
			}]
		});
		$("#draw_msg").html("已经绘制数据,第 "+ (objIndex+1) + " 个文件 ，第 " + index +" 条数据 ，点数总计：" +pointerSum +" ");
		
		if(index<1)
			addseriesIta(obj,objIndex,index,max,leng);
		/*else{
			objIndex++; index=0;
			if(objIndex < obj.length)
				addseriesIta(obj,objIndex,index,obj[objIndex].length,formatData(obj[objIndex][0]).length);
		}*/
	},1000);
}
/* function datanum(arr){
	var data1 = [];
	for(i=0;i<arr.length;i++){
	  data1.push(formatData(arr[i]));
	}
	console.log(data1);
	return data1;
} */
function formatData(arrObj){
	 var start = parseInt($('input[name=startFrequency]').val());
	 var stop = parseInt($('input[name=stopFrequency]').val());
	 var step = Number((stop-start)/arrObj.length).toFixed(1);console.log(step+" "+arrObj.length)
	var data = [];
    for (i = 0; i <arrObj.length; i++) { 
   		data[i]=[start,arrObj[i]];
		start += parseInt(step); 
    } 
    return data; 
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
	hightchart();
	if(!isLoadingOver)
		alert("数据尚未装载完毕，稍后再试...")
	var objIndex = 0;index=0;
	addseriesIta(obj,objIndex,index,obj[index].length,formatData(obj[0]).length);
}
</script>
<body>
<form name="deviceParaForm" id="deviceParaForm" action=""
	onsubmit="submitForm()">
	startFrequency:<input type="text" name="startFrequency" value="0" /> 
	stopFrequency:<input type="text" name="stopFrequency" value="1000" /> 
	<button type="button" onclick="getJsFileList()">获取文件列表</button>
	<button type="button" onclick="loadJsFiles()">绘制图形</button>&nbsp;<span id="jsFileListSpan"></span>
</form>
<hr>
<div id="container" style="min-width:400px;height:800px"></div>
<div><span id="draw_msg"></span></div>
</body>
</html>