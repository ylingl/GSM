var map;
var ControlShowState = false;
var customLayer;
var currentPoint;
var deviceLayer;
//加载map.js
function loadMapJs(){
	init();
}


//初始化
function init(){
	map = new BMap.Map("map");            // 创建Map实例
	var point = new BMap.Point(116.404, 39.915); // 创建点坐标
	map.centerAndZoom(point,15);                 
	map.enableScrollWheelZoom();                 //启用滚轮放大缩小
	map.enableKeyboard();					//启动键盘放大缩小
	drawStations();
	drawDevices();
	/*addShowControlBtn();
	addLocationSelectBar();
	initToolBar();*/
	
}

//绘制基站
function drawStations(){	

	if (customLayer) {
		map.removeTileLayer(customLayer);
	}
	customLayer=new BMap.CustomLayer({
		geotableId: 122022,
		q: '', //检索关键字
		tags: '', //空格分隔的多字符串
		filter: '' //过滤条件,参考http://developer.baidu.com/map/lbs-geosearch.htm#.search.nearby
	});
	map.addTileLayer(customLayer);
	customLayer.addEventListener('hotspotclick',showBSDetail);
}

function addShowControlBtn(){
	// 定义一个控件类,即function
	function OpenControl(){
	  // 默认停靠位置和偏移量
	  this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
	  this.defaultOffset = new BMap.Size(0, map.getSize().height / 2 - 30);
	}

	// 通过JavaScript的prototype属性继承于BMap.Control
	OpenControl.prototype = new BMap.Control();

	// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
	// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
	OpenControl.prototype.initialize = function(map){
	  // 创建一个DOM元素
	  var div = document.createElement("div");
	  // 添加文字说明
	  div.appendChild(document.createTextNode(""));
	  // 设置样式
	  div.setAttribute("id","openControl");
	  div.style.cursor = "pointer";
	  div.style.border = "1px solid gray";
	  div.style.background = "url(/Tsme/images/map/openControl.png)";
	  div.style.width= "18px";
	  div.style.height = "60px";
	
	  div.onclick = showFunctionArea;
	  // 添加DOM元素到地图中
	  map.getContainer().appendChild(div);
	  // 将DOM元素返回
	  return div;
	};

	var openControl = new OpenControl();
	map.addControl(openControl);
}

function addLocationSelectBar(){
	// 定义一个控件类,即function
	function LocationSelectBar(){
	  // 默认停靠位置和偏移量
	  this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
	  this.defaultOffset = new BMap.Size(10, 22);
	}

	// 通过JavaScript的prototype属性继承于BMap.Control
	LocationSelectBar.prototype = new BMap.Control();

	// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
	// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
	LocationSelectBar.prototype.initialize = function(map){
	  // 创建一个DOM元素
	  var div = document.createElement("div");

	  var province = document.createElement("select");
	  province.setAttribute("id","s_province");
	  province.setAttribute("name","s_province");
	  province.style.width = "80px";
	  province.onchange = showProvinceArea;
	  var city = document.createElement("select");
	  city.setAttribute("id","s_city");
	  city.setAttribute("name","s_city");
	  city.style.width = "90px";
	  city.style.marginLeft = "10px";
	  var county = document.createElement("select");
	  county.setAttribute("id","s_county");
	  county.setAttribute("name","s_county");
	  county.style.width = "100px";
	  county.style.marginLeft = "10px";
	  div.appendChild(province);
	  div.appendChild(city);
	  div.appendChild(county);
	  // 设置样式
	  div.setAttribute("id","openControl");
	  div.style.cursor = "pointer";
	  div.style.border = "1px solid gray";
	  div.style.width= "290px";
	  div.style.height = "18px";
	  // 添加DOM元素到地图中
	  map.getContainer().appendChild(div);
	  // 将DOM元素返回
	  return div;
	};

	var locationSelectBar = new LocationSelectBar();
	map.addControl(locationSelectBar);
	init_area();
	document.getElementById("s_province").setAttribute('onchange','showProvinceArea()');
	document.getElementById("s_city").setAttribute('onchange','showCityArea()');
	document.getElementById("s_county").setAttribute('onchange','showCountyArea()');
}

//展示基站信息
function showBSDetail(e){
	console.log(e);
	var id = e.content.PRECISION;
	var lng = e.content.location[0];
	var lat = e.content.location[1];
	var address = e.content.address;
	var date = e.content.DAY;
	var customPoi = e.customPoi;//poi的默认字段
	var content = '<p style="width:280px;margin:0;line-height:20px;">基站编号:' + id
	+ '<br/>&nbsp;&nbsp;经&nbsp;&nbsp;&nbsp;度&nbsp;&nbsp;:'+lng +"°"
	+ '<br/>&nbsp;&nbsp;纬&nbsp;&nbsp;&nbsp;度&nbsp;&nbsp;:'+lat +"°"
	+ '<br/>建立时间:'+date
	+ '<br/>&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址&nbsp;&nbsp;:'+address
	+ '<br/>&nbsp;公&nbsp;里&nbsp;标:3km'
	+ '</p>';
	var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
		title: "基站信息", //标题
		width: 300, //宽度
		height: 130, //高度
		panel : "panel", //检索结果面板
		enableAutoPan : true, //自动平移
		enableSendToPhone:false,
		searchTypes :[
			//BMAPLIB_TAB_SEARCH,   //周边检索
		]
	});
	currentPoint = new BMap.Point(lng, lat);
	searchInfoWindow.open(currentPoint);
}

// 展开左侧功能区
function showFunctionArea(){
	var div = document.getElementById("openControl");
	if(!ControlShowState){
		div.style.background = "url(/Tsme/images/map/closeControl.png)";
		$(".funcArea").show();
		$(".map").animate({left:"300px"}, 300);
		$(".funcArea").animate({left:"0px"}, 300);
		ControlShowState = !ControlShowState;
	}else{
		div.style.background = "url(/Tsme/images/map/openControl.png)";
		$(".map").animate({left:"0px"}, 300);
		$(".funcArea").animate({left:"-300px"}, 300);
		ControlShowState = !ControlShowState;
	}
}

function initToolBar(){
	$(".searchBtn").bind("click", function(){
		$(".searchBtn").removeClass("disselected").addClass("selected");
		$(".detailPage").load("/Tsme/map/loadSearch");
	});
}

function showProvinceArea(){
	change(1);
	var province = document.getElementById("s_province").value;
	map.centerAndZoom(province, 9);
}

function showCityArea(){
	change(2);
	var city = document.getElementById("s_city").value;
	map.centerAndZoom(city, 11);
}

function showCountyArea(){
	var county = document.getElementById("s_county").value;
	map.centerAndZoom(county, 14);
}

function drawDevices() {

	if (deviceLayer) {
		map.removeTileLayer(deviceLayer);
	}
	deviceLayer=new BMap.CustomLayer({
		geotableId: 122629,
		q: '', //检索关键字
		tags: '', //空格分隔的多字符串
		filter: '' //过滤条件,参考http://developer.baidu.com/map/lbs-geosearch.htm#.search.nearby
	});
	map.addTileLayer(deviceLayer);
	deviceLayer.addEventListener('hotspotclick',showDeviceDetail);
}

function showDeviceDetail(e){
	
	console.log(e);
	var id = e.content.device_id;
	var lng = e.content.location[0];
	var lat = e.content.location[1];
	var address = e.content.address;
	var date = e.content.device_create_time;
	var customPoi = e.customPoi;//poi的默认字段
	var content = '<p style="width:280px;margin:0;line-height:20px;">设备编号:' + id
	+ '<br/>&nbsp;&nbsp;经&nbsp;&nbsp;&nbsp;度&nbsp;&nbsp;:'+lng +"°"
	+ '<br/>&nbsp;&nbsp;纬&nbsp;&nbsp;&nbsp;度&nbsp;&nbsp;:'+lat +"°"
	+ '<br/>建立时间:'+date
	+ '<br/><div style=\"margin-top:10px\"><button onclick=\"initDevice()\">设备配置</button><button style=\"margin-left:20px\">信号查看</button><div>'
	/*+ '<br/>&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址&nbsp;&nbsp;:'+address
	+ '<br/>&nbsp;公&nbsp;里&nbsp;标:3km'*/
	+ '</p>';	
	var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
		title: customPoi.title, //标题
		width: 300, //宽度
		height: 130, //高度
		panel : "panel", //检索结果面板
		enableAutoPan : true, //自动平移
		enableSendToPhone:false,
		searchTypes :[
			//BMAPLIB_TAB_SEARCH,   //周边检索
		]
	});
	currentPoint = new BMap.Point(lng, lat);
	searchInfoWindow.open(currentPoint);
}

function initDevice() {
	window.open("/Tsme/tsme/iterator?deviceId=123456");
}