<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/Tsme/css/account/manage.css"/>
<script type="text/javascript" src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/Tsme/js/config/manage.js"></script>
<title>管理中心</title>
</head>
<body>
<div class="title">
	<h1>GSM-R网络固定干扰监测平台管理系统</h1>
</div>
<div class="wrap">
	<div class="navigator">
		<ul>
			<li class="leftList"><a class="userA">员工管理</a></li>
			<li class="leftList"><a class=deviceA>设备管理</a></li>
		</ul>
	</div>
	<div class="rightDiv supervisorDiv">
		<h class="infoTitle">员工信息</h>
			<ul>
				<li class="userLi">
					<span>员工编号</span>
					<span>姓名</span>
					<span>性别</span>
					<span>密码</span>
				</li>
				<c:forEach var="item" items="${userItems}" varStatus="status">
				<li>
					<span>${item.empno}</span>
					<span>${item.name}</span>
					<span>${item.gender}</span>
					<span>${item.password}</span>
				</li>
				</c:forEach>
				<li class="userLi addLi">
					<span>添加用户</span>
					<span style="width:49%;">
						<label for="way1">
							<input type="radio" checked="checked" name="addWay" id="way1"/>方式一
						</label>
						<input type="file" id="upfile"/>
						<input onclick="addBatchUser();" type="button" value="批量导入">
					</span>
					<span class="last">
						<label for="way2">
							<input type="radio" name="addWay" id="way2"/>方式二
						</label>
						<input value="逐一添加" onclick="addSingleUser();" type="button">
					</span>
					<!-- <textarea id="txtArea" cols=50 rows=10></textarea> -->
					
				
				</li>
			</ul>
	</div>
	<div class="rightDiv DeviceDiv">
		<h>设备信息</h>
		<ul>
			<li>
				<span>设备编号</span>
				<span>经度</span>
				<span>纬度</span>
				<span>方位角</span>
				<span>距离</span>
			</li>
			<c:forEach var="item" items="${deviceItems}" varStatus="status">
			<li>
				<span>${item.name}</span>
				<span>${item.LNG}</span>
				<span>${item.LAT}</span>
				<span>${item.azimuth}</span>
				<span>${item.distance}</span>
			</li>
			</c:forEach>
			<li>
				<input type="file" id="upfile"/>
				<input onclick="ReadExcel();" type="button" value="批量导入">
				<textarea id="txtArea" cols=50 rows=10></textarea>
			</li>
			<li><input class="addDevice" type="button" value="增加设备"></li>
		</ul>
		<div class="addDeviceDiv">
				<ul>
					<li>
						<span>设备编号</span><input type="text">
					</li>
					<li>
						<span>经度</span><input type="password">
					</li>
					<li>
						<span>纬度</span><input type="text">
					</li>
					<li>
						<span>方位角</span><input type="text">
					</li>
					<li>
						<span>距离</span><input type="text">
					</li>
					<li><input type="button" value="确定"></li>
				</ul>
			</div>
	</div>
</div>
<!-- <div class="maskDiv"></div> -->
<div class="conDiv">
	<div class="addUserDiv">
		<h class="addTitle">添加成员信息</h>
		<ul>
			<li>
				<span>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span><input type="text">
			</li>
			<li>
				<span>密&nbsp;&nbsp;&nbsp;&nbsp;码：</span><input type="password">
			</li>
			<li>
				<span>员工编号：</span><input type="text">
			</li>
			<li>
				<span>性&nbsp;&nbsp;&nbsp;&nbsp;别：</span>
				<label for="male">
					<input id="male" type="radio" checked="checked" name="sex" value="true" />男
				</label>
				<label for="female">
					<input id="female" type="radio" name="sex" value="false" />女
				</label>
			</li>
			<li><a class="save">确&nbsp;&nbsp;定</a></li>
		</ul>
	</div>
</div>
</body>
</html>