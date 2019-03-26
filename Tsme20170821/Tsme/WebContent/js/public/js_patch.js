/**
 * js bug：解决 javascript 的 小数截取的bug。<br>
 * bug： alert(parseFloat(0.006).toFixed(2)); 显示 0.00 修改后显示 0.01
 */
Number.prototype.toFixed = function(d) {
	var s = this + "";
	if (!d)
		d = 0;
	if (s.indexOf(".") == -1)
		s += ".";
	s += new Array(d + 1).join("0");
	if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0," + (d + 1) + "})?)\\d*$").test(s)) {
		var s = "0" + RegExp.$2, pm = RegExp.$1, a = RegExp.$3.length, b = true;
		if (a == d + 2) {
			a = s.match(/\d/g);
			if (parseInt(a[a.length - 1]) > 4) {
				for ( var i = a.length - 2; i >= 0; i--) {
					a[i] = parseInt(a[i]) + 1;
					if (a[i] == 10) {
						a[i] = 0;
						b = i != 1;
					} else
						break;
				}
			}
			s = a.join("").replace(new RegExp("(\\d+)(\\d{" + d + "})\\d$"),
					"$1.$2");
		}
		if (b)
			s = s.substr(1);
		return (pm + s).replace(/\.$/, "");
	}
	return this + "";
};

// 给Number类型增加四则运算
/**
 * js bug：js四则运算小数精度丢失的bug修复。例如：1.5451+0.34133
 * 按理来说应该是等于1.88643,结果JS给计算时居然算成1.88629999999998了 调用方法
 */
/**
 * 加法：1.5451+0.34133 的调用方法为:var s = (1.5451).add(0.34133).toFixed(2);
 * alert((7.1).add(12.00027)+"="+(7.1+12.00027));
 */
Number.prototype.add = function(arg) {
	var bit1, bit2, m;
	try {
		bit1 = arg.toString().split(".")[1].length;
	} catch (e) {
		bit1 = 0;
	}
	try {
		bit2 = this.toString().split(".")[1].length;
	} catch (e) {
		bit2 = 0;
	}
	m = Math.pow(10, Math.max(bit1, bit2));
	return (this * m + arg * m) / m;
};
/**
 * 减法: 1.5451-0.34133 的调用方法为:var s = (1.5451).sub(0.34133);
 */
Number.prototype.sub = function(arg) {
	var bit1, bit2;
	try {
		bit1 = arg.toString().split(".")[1].length;
	} catch (e) {
		bit1 = 0;
	}
	try {
		bit2 = this.toString().split(".")[1].length;
	} catch (e) {
		bit2 = 0;
	}
	var n = Math.max(bit1, bit2);
	var m = Math.pow(10, n);
	return Number(((this * m - arg * m) / m).toFixed(n));
};
/**
 * 乘法: 1.5451*0.34133 的调用方法为:var s = (1.5451).mul(0.34133).toFixed(3);
 */
Number.prototype.mul = function(arg) {
	var bit1, bit2;
	try {
		bit1 = arg.toString().split(".")[1].length;
	} catch (e) {
		bit1 = 0;
	}
	try {
		bit2 = this.toString().split(".")[1].length;
	} catch (e) {
		bit2 = 0;
	}
	var m = bit1 + bit2;
	// var n = (bit1 > bit2) ? bit1 : bit2;
	return (Number(this.toString().replace(".", ""))
			* Number(arg.toString().replace(".", "")) / Math.pow(10, m));// .toFixed(n);
};
/**
 * 除法: 1.5451/0.34133 的调用方法为:var s = (1.5451).div(0.34133).toFixed(3);
 */
Number.prototype.div = function(arg) {
	var bit1, bit2;
	try {
		bit1 = arg.toString().split(".")[1].length;
	} catch (e) {
		bit1 = 0;
	}
	try {
		bit2 = this.toString().split(".")[1].length;
	} catch (e) {
		bit2 = 0;
	}
	var n = Math.max(bit1, bit2);
	var m = Math.pow(10, n);
	// return (Number(this.toString().replace(".", ""))*m) /
	// (Number(arg.toString().replace(".", ""))*m);
	return ((this * m) / (arg * m));
};

// 给String对象增加四则运算
/**
 * 加法：1.5451+0.34133 的调用方法为:var s = (1.5451).add(0.34133).toFixed(2);
 * alert((7.1).add(12.00027)+"="+(7.1+12.00027));
 */
String.prototype.add = function(arg) {
	return Number(this).add(arg);
};
/**
 * 减法: 1.5451-0.34133 的调用方法为:var s = (1.5451).sub(0.34133);
 */
String.prototype.sub = function(arg) {
	return Number(this).sub(arg);
};
/**
 * 乘法: 1.5451*0.34133 的调用方法为:var s = (1.5451).mul(0.34133).toFixed(3);
 */
String.prototype.mul = function(arg) {
	return Number(this).mul(arg);
};
/**
 * 除法: 1.5451/0.34133 的调用方法为:var s = (1.5451).div(0.34133).toFixed(3);
 */
String.prototype.div = function(arg) {
	return Number(this).div(arg);
};

// 等比例缩放图片
//var flag = false;
/**
 * ImgD：原图 maxWidth：允许的最大宽度 maxHeight：允许的最大高度
 */
function resizeimg(ImgD, maxWidth, maxHeight) {
	var image = new Image();
	var iwidth = maxWidth; // 定义允许图片宽度
	var iheight = maxHeight; // 定义允许图片高度
	image.src = ImgD.src;
	if (image.width > 0 && image.height > 0) {
		//flag = true;
		if (image.width / image.height >= iwidth / iheight) {
			if (image.width > iwidth) {
				ImgD.width = iwidth;
				ImgD.height = (image.height * iwidth) / image.width;
			} else {
				ImgD.width = image.width;
				ImgD.height = image.height;
			}
			//ImgD.alt = image.width + "×" + image.height;
		} else {
			if (image.height > iheight) {
				ImgD.height = iheight;
				ImgD.width = (image.width * iheight) / image.height;
			} else {
				ImgD.width = image.width;
				ImgD.height = image.height;
			}
			//ImgD.alt = image.width + "×" + image.height;
		}
	}
	centerImage(ImgD, maxWidth, maxHeight);
}

function centerImage(imgD, maxWidth, maxHeight) {
	// var div = imgD.parentNode;// 获取包含本图片的div不用这个
	if (imgD.height < maxHeight) {
		var top = (maxHeight - imgD.height) / 2-2;
		//ie6不支持这么获取高度，所以ie6下不执行
		if(imgD.height!=0){
			imgD.style.marginTop = top + "px";
		}
	}
	if (imgD.width < maxWidth) {
		var left = (maxWidth - imgD.width) / 2;
		//ie6不支持这么获取宽度，所以ie6下不执行
		if(imgD.width!=0){
			imgD.style.marginLeft = left + "px";
		}
	}
}

function CurentTime()
{ 
    var now = new Date();
   
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
   
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
   
    var clock = year + "-";
   
    if(month < 10)
        clock += "0";
   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day + " ";
   
    if(hh < 10)
        clock += "0";
       
    clock += hh + ":";
    if (mm < 10) clock += '0'; 
    clock += mm; 
    return(clock); 
} 


function debug_showAttr(obj)
{
	var str="<b>begin:the attribute of "+obj+"</b><br/>";
	for(var i in obj)
	{
		str+="<b>"+i+"</b>=="+obj[i]+"<br>";
	}
	document.body.innerHTML+=str;
}
function debug_showMsg(str)
{
	document.body.innerHTML+=str;
}
function debug_showMsgSrc(str)
{
	document.body.innerHTML+=str.replace(/</g,"&lt").replace(/>/g,"&gt");
}

