<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html;charset=utf-8"
    pageEncoding="utf-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="/Tsme/js/jQuery/jquery-1.7.2.js"></script>
<script src="/Tsme/js/spectra/jquery.sparkline.js"></script>
<script src="/Tsme/js/tsme/showSignal.js"></script>
<script type="text/javascript" src="/Tsme/js/spectra/jquery.flot.js"></script>
<title>信息管理</title>
</head>
<body onload="initSignal()">
	<div style="width:100%;height:50%;margin-left:25%">
		<div id="placeholder" style="width:600px;height:300px;"></div>
	</div>
	<div style="width:100%;height:50%;margin-left:25%" id="info">
	<form style="height: 500px; width: 100%">
		<div style="height: 20px; font-size: 12px; width: 120px; letter-spacing: normal; text-align: right; display: inline">设备编号：</div>
		<input style="position: relative; height: 30px; width: 154px; display: inline" id="id" value="${deviceId}">
		<div>
		
		</div>
		<fieldset style="width:500px;margin-top:30px">
		<legend>设备信号模板信息</legend>
		<div style=" display: block; margin-top:10px">
		<div style="width: 250px; display: inline">
		<div style="position: relative; height: 24px; width: 111px; font-size: 12px; text-align: right; display: inline">信号频率：</div>
		<input style="display: inline; position: relative; width: 106px" id="value" name="frequence">
		<label style="line-height: 20px; height: 20px; text-align: left; display: inline">kHz</label>
		</div>
			<div style="position: relative; width:250px; display: inline">
					<div style="position: relative; height: 24px; width: 111px; font-size: 12px; text-align: right; display: inline">信号电平值：</div>
		<input style="display: inline; position: relative; width: 98px" id="value" name="RxLev">
		<label style="line-height: 20px; height: 20px; text-align: left; display: inline" >dBm</label></div>
		</div>
	
		<div style=" display: block;margin-top:10px">
		<div style="width: 250px; display: inline">
		<div style="position: relative; height: 24px; width: 111px; font-size: 12px; text-align: right; display: inline">信道号：</div>
		<input style="display: inline; position: relative; width: 106px" id="value">
		
		
		<div style="width: 250px; display: inline">
		<div style="position: relative; height: 24px; width: 111px; font-size: 12px; text-align: right; display: inline;margin-left:56px">信号类型：</div>
		<select style="width: 91px; ">
		<option>临频</option> 
		<option>同频</option>
		 </select> 
		 </div>
		</div>
		</div>
		<div style="width: 500px; display: inline; margin-top:10px">
		<div style="position: relative; height: 24px; width: 111px; font-size: 12px; text-align: right; display: inline">信号误差范围：</div>
		<input style="display: inline; position: relative; width: 46px" id="value">
		<label style="line-height: 20px; height: 20px; text-align: left; display: inline">dBm ~</label><input style="display: inline; position: relative; width: 48px" id="value"><label style="line-height: 20px; height: 20px; text-align: left; display: inline">dBm</label><br><br><input
					type="submit" value="提交" style="width: 130px;margin-left: 90px;" onclick="addSignal()"><input
					type="reset" value="重置" style="width: 130px; position: relative; margin-left: 100px; display: inline">
			</div>
		
		
		
		</fieldset>
	</form>
	
	</div>
</body>
</html>