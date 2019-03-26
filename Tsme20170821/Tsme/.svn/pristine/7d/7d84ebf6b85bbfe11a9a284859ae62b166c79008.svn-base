<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>频谱图</title> 
<style type="text/css">
#canvas1 { 
background-color: cornflowerblue; 
} 
#canvas3, #canvas1, #canvas4{
position:absolute;
left:300px;
}
.spectraWrap, .configWrap{
border:1px solid #000;
width:100%;
height:400px;
}
</style> 
<script type="text/javascript"> 
var colorArr = new Array();
colorArr = ["red", "orange", "yellow", "green", "blue", "cyanine", "purple","black"];

function draw(){ 
var my_canvas1 = document.getElementById( "canvas1" );
var my_canvas3 = document.getElementById( "canvas3" ); 
var my_canvas4 = document.getElementById( "canvas4" ); 
var content1 = my_canvas1.getContext( "2d" );
var content3 = my_canvas3.getContext( "2d" ); 
var content4 = my_canvas4.getContext( "2d" ); 
content1.strokeStyle = colorArr[7];
content1.moveTo(10, 400 );//原点
content1.lineTo(400,400);//横坐标
content1.moveTo(10, 400 );//原点
content1.lineTo(10,0);//纵坐标
content3.moveTo(10, 100 ); //原点
content4.moveTo(10, 100 ); //原点

//第一条曲线
for( var i = 1; i < 200; i += 0.1 ){
var x = i * 10; 
var y = Math.sin(i ) * 100 +100; 
content3.strokeStyle = colorArr[0];
content3.lineTo( x, y ); 
}
//第二条曲线
for( var i = 1; i < 200; i += 0.1 ){ 
var x = i * 10; 
var y = Math.cos(i ) * 100 +100; 
content4.strokeStyle = colorArr[1];
content4.lineTo( x, y ); 
}
content1.stroke(); 
content1.closePath(); 
content3.stroke(); 
content3.closePath(); 
content4.stroke(); 
content4.closePath(); 
} 
</script>
</head> 
<body onload="draw()">
<div class="spectraWrap">该区域为频谱图显示区(待完善)
<canvas id = "canvas1" width="400" height="400"></canvas> 
<canvas id = "canvas3" width="400" height="400" ></canvas>
<canvas id = "canvas4" width="400" height="400"></canvas>
</div>
<script>
var objList = [];
var obj1={};
obj1.fre = 9264;
obj2.rx = 940;
objList.add(obj1);
var obj2 = {};
obj2.fre = 9048;
obj2.rx = 941;
objList.add(obj2);
var obj3 = {};
obj3.fre = 8840;
obj3.rx = 942;
objList.add(obj3);
</script>
//正常的为绿色

<br/><h1>实时监控：</h1>请选择监控点
<form action="">
<select name="siteId"><option value="site01">第一个点</option><option value="site02">第二个点</option></select>
<button>查看                                      </button>
</form>
<div class="configWrap">
该区域为配置信息区域(待完善)
</div>
</body>
</html> 