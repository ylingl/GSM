<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试页面</title>
<script type="text/javascript" src="js/jQuery/jquery-1.11.3.min.js"></script>
<script>
var countStatus = {device01:0,device02:0,P19887:0};
var countData = {device01:0,device02:0,P19887:0};
var countPoint = {device01:20,device02:60,P19887:300};
var yMax = {device01:-30,device02:-20,P19887:-50};

function submitMonitroData(deviceId){
	$.ajax({
		cache: true,
		type: "POST",
		url: "client/device/sendRealTimeData",
		data: {id:deviceId,data:initData(deviceId)},// 你的formid
		async: true,
		error: function(request){
			$('#msg'+deviceId).html("提交数据失败！");
		},
		success: function(result){
			if(result.code == "10"){
				$('#msg'+result.id).html(" <br/>&nbsp;&nbsp;&nbsp;&nbsp;上传数据结果： "+ result.msg + " <br/>&nbsp;&nbsp;&nbsp;&nbsp;上传数据次数： " + (countData[result.id]++));
			}else if(result.code == "91"){
				clearInterval(upDataTimer[deviceId]);
				$('#msg'+result.id).html(" <br/>&nbsp;&nbsp;&nbsp;&nbsp;上传数据结果： "+ result.msg + "  <br/>&nbsp;&nbsp;&nbsp;&nbsp;上传数据次数： " + (countData[result.id]++));
			}
		}
	});
}
var upDataTimer = new Array();
function submitForm(formName){
	var formObj = $('#'+formName);//alert(formObj.serialize())
	var deviceId = formObj.find("input[name=id]").val();
	$.ajax({
		cache: true,
		type: "POST",
		url: formObj.attr("action"),
		data: formObj.serialize(),// 你的formid
		async: true,
		error: function(request){
			$('#msg'+formName).html("提交数据失败！");
		},
		success: function(result){
			if(result.code == "11"){
				$('#msg'+formName).html(formName+"  "+result.code + "  "+ result.msg);
				var deviceId = formObj.find("input[name=id]").val();
				var interval = result.data.interval;
				upDataTimer[deviceId] = setInterval(function() { 
					submitMonitroData(deviceId);
			    }, 
			    interval); 
			}else if(result.code=="12"){
				clearInterval(upDataTimer[deviceId]);
				$('#msg'+formName).html(" <br/>&nbsp;&nbsp;&nbsp;&nbsp;报告状态结果： "+ result.msg + "  <br/>&nbsp;&nbsp;&nbsp;&nbsp;报告状态次数： " + (countStatus[result.id]++));
			}else{
				$('#msg'+formName).html(" <br/>&nbsp;&nbsp;&nbsp;&nbsp;报告状态结果： "+ result.msg + "  <br/>&nbsp;&nbsp;&nbsp;&nbsp;报告状态次数： " + (countStatus[result.id]++));
			}
		}
	});
}

function initData(deviceId){
	var sum = countPoint[deviceId];
	var yRange = yMax[deviceId];
	var data = '', time = (new Date()).getTime(), i; 
    for (i = 0; i < sum; i++) { 
    	if(i>0) data += ',';
    	//data += '{"x":' + fomatFloat((i+Math.random())*10,2)+  ',';
    	//data += '"y":' + fomatFloat(Math.random()*10,2) + '}';
    	data += fomatFloat(Math.random()*yRange-60,2);
    } 
    data += '';
    console.log(data);
    return data; 
}

function initData_(sum){
	var data = '[', time = (new Date()).getTime(), i; 
    for (i = 0; i < sum; i++) { 
    	if(i>0) data += ',';
    	data += '{"x":' + fomatFloat((i+Math.random())*10,2)+  ',';
    	data += '"y":' + fomatFloat(Math.random()*10,2) + '}';
    	
    } 
    data += ']';
    console.log(data);
    return data; 
}


function initData__(){
	var data = [], 
    time = (new Date()).getTime(), i; 
    for (i = 0; i <= 10; i++) { 
        data.push({ 
            x: (i+Math.random())*10,  
            y: Math.random()*10 
        }); 
    } 
    console.log(data);
    return data; 
}
	
	
function fomatFloat(src,pos){     
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);     
} 

var moniterTimer = null;
function beginAll(){
	if(moniterTimer != null)
		return;
	moniterTimer = setInterval(function() { 
		submitForm("device01Form");
		submitForm("device02Form");
		submitForm("P19887Form");
    }, 
    1000); //太快了后，影响数据上报
}
function stopAll(){
	clearInterval(moniterTimer);
	moniterTimer = null;
	clearInterval(upDataTimer["device01"]);
	clearInterval(upDataTimer["device02"]);
	clearInterval(upDataTimer["P19887"]);
}
</script>
<style type="text/css">
table { width:98%; border-collapse:collapse;}
table caption{height:80px; font-size:30px;}
table thead tr{height:30px; }
table th,td{border:1px solid #ff0000;}
table .formrow{height:60px}
table .messagerow{height:100px}
</style>
</head>
<body>
<table>
<caption>模拟设备上传监控数据  <br/>
&nbsp;&nbsp;&nbsp;klmlklj<input type="button" onclick="beginAll()" value="开始报告所有设备状态（间隔0.2秒）">
&nbsp;&nbsp;&nbsp;<input type="button" onclick="stopAll()" value="停止上传所有设备状态">
</caption>
<thead><tr><th>设备device01</th><th>设备device02</th><th>设备P19887</th></tr></thead>
<tbody>
<tr class="formrow">
<td>
<form id="device01Form" action="client/device/reportStatus">
<input type="text" name="id" value="device01">
<input type="text" name="code" value="10">
</form>
</td>
<td>
<form id="device02Form" action="client/device/reportStatus">
<input type="text" name="id" value="device02">
<input type="text" name="code" value="10">
</form>
</td>
<td>
<form id="P19887Form" action="client/device/reportStatus">
<input type="text" name="id" value="P19887">
<input type="text" name="code" value="10">
</form>
</td>
</tr>
<tr class="messagerow">
<td>状态报告：<span id="msgdevice01Form"></span><br/><br/>数据上传：<span id="msgdevice01"></span></td>
<td>状态报告：<span id="msgdevice02Form"></span><br/><br/>数据上传：<span id="msgdevice02"></span></td>
<td>状态报告：<span id="msgP19887Form"></span><br/><br/>数据上传：<span id="msgP19887"></span></td>
</tr>
</tbody>
</table>

</body>
</html>