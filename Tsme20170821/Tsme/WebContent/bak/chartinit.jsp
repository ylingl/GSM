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
	for(int i=0; i<list.size(); i++){
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
<script type="text/javascript" src="/Tsme/js/chart/highcharts.js"></script>
<script>
var chart;
var obj;
var isLoadingOver = false;
var startFrequency = null;
var stopFrequency = null;
function hightchart() {
	chart = new Highcharts.Chart({
        chart: {
        	renderTo: 'container',
            type: 'scatter',
            zoomType: 'xy'
        },
       /*  title: {
            text: 'Height Versus Weight of 507 Individuals by Gender'
        },
        subtitle: {
            text: 'Source: Heinz  2003'
        }, */
        credits : {
			text : '频谱图' //设置LOGO区文字 
		},
        xAxis: {
            title: {
                enabled: true,
                text: 'Frequency[MHz]'
            },
            startOnTick: true,
            endOnTick: true,
            showLastLabel: true,
			max : stopFrequency, //x轴最大值 
			min : startFrequency,
        },
        yAxis: {
            title: {
                text: 'Level[dBm]'
            },
			max : null, //Y轴最大值 
			min : null, //Y轴最小值 
        },
        legend : {
			enabled : false, //设置图例不可见 
			backgroundColor : '#fff'
		},
		 /*  legend: {
        	   layout: 'vertical',
            align: 'left',
            verticalAlign: 'top',
            floating: true,
            backgroundColor: '#f90707',
            borderWidth: 1
        }, */
        plotOptions: {
            scatter: {
                marker: {
                    radius: 1,
                    states: {
                        hover: {
                            enabled: true,
                            lineColor: 'rgb(100,100,100)'
                        }
                    }
                },
                states: {
                    hover: {
                        marker: {
                            enabled: false
                        }
                    }
                },
              /*   tooltip: {
                    headerFormat: '<b>{series.name}</b><br>',
                    pointFormat: '{point.x} cm, {point.y} kg'
                } */
            }
        },
        series: [{
            color: 'red'
            }],
    });
	
}
$(function () {
	//chart.series[0].setData(formatData(dataArray20150116001[0]));
	//var obj = [dataArray20150116001,dataArray20150116003,dataArray20150116004];
	<% out.write(objjs); %>
	isLoadingOver = true;
	$("#jsFileListSpan").html("数据装载完毕，请绘制");
	//addseriesIta(obj,1,obj.length,formatData(obj[0]).length);
});

var pointerSum = 0;
function addseriesIta(obj,objIndex,index,max,leng){
	setTimeout(function(){
		var ser = chart.addSeries( {
			color : 'red',
		} );
		ser.setData(formatData(obj[objIndex][index]), true);
		pointerSum += obj[objIndex][index].length;
		index++;
		$("#draw_msg").html("已经绘制数据,第 "+ (objIndex+1) + " 个文件 ，第 " + index +" 条数据 ，点数总计：" +pointerSum +" ");
		if(index<max)
			addseriesIta(obj,objIndex,index,max,leng);
		else{
			objIndex++; index=0;
			if(objIndex < obj.length)
				addseriesIta(obj,objIndex,index,obj[objIndex].length,formatData(obj[objIndex][0]).length);
		}
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
   		 data.push({ 
 	            x: start,  
 	            y: arrObj[i],
   	      
   	        }); 
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
<div id="container" style="min-width:400px;height:600px"></div>
<div><span id="draw_msg"></span></div>
</body>
</html>