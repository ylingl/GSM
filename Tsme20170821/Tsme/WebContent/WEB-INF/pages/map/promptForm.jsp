<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<style>
ul,li{list-style:none; font-family:microsoft yahei; padding:0px; margin:0px; width:100%;}
.wrap{
    position: absolute;
    bottom: 0px;
    right: 0px;
    width: 300px;
    height: 300px;
    display: block;
    border-radius: 10px 10px 0px 0px;
    border: 1px solid black;
    background: -webkit-gradient(linear, 0% 0%, 0% 100%,from(#b8c4cb), to(#f6f6f8));
}
.title{
    margin: auto;
    display: block;
    text-align: center;
    font-size: 21px;
    font-family: microsoft Yahei;
    color: #fff;
    background-color: red;
    border-radius: 10px 10px 0px 0px;
    height: 35px;
    line-height: 35px;
}
.title img{
	position: absolute;
    top: 5px;
    right: 10px;
}
.info{
	height: 30px;
    text-align: left;
    line-height: 30px;
    color: #000;
    font-size: 16px;
}

li span{
	text-align:center;
	display:inline-block;
}
.name{
	width:70px;
}
.time{
	width:80px;
}
.place{
	width:50px;
}
.type{
	width:50px;
}
.view{
	width: 44px;
    text-decoration: none;
    display: inline-block;
    text-align: center;
}
</style>
<script type="text/javascript">
function closeDialog(){
	$(".wrap").remove();
}
function add(){
	var liHtml = "<li></li>"
}
</script>
</head>
<body>
<!-- <a onclick="add()"></a> -->
<div class="wrap">
	<h class="title">监测到异常信号<img onclick="closeDialog()" src=""/Tsme/images/dialog/close.png""></h>
	<ul>
		<li class="info"><span class="name">设备名称</span><span class="time">时间</span><span class="place">地点</span><span class="type">类型</span><span class="view">操作</span></li>
		<li class="info"><span class="name">abc</span><span class="time">2015-10-7</span><span class="place">唐山</span><span class="type">干扰波</span><a class="view" href="http://localhost:8080/Tsme" target="_blank">查看</a></li>
	</ul>
</div>
</body>
</html>