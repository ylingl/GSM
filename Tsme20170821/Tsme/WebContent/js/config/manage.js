$(function(){
	if(window != top){
		top.location.href = location.href;
	}
})

function addSingleUser(){
	var html = "<div class='addUserDiv'>" +
					"<h class='addTitle'>添加成员信息</h>" + 
					"<ul>" + 
						"<li>" +
							"<span>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span><input type='text' name='name'>" + 
						"</li>"+
						"<li>" +
							"<span>密&nbsp;&nbsp;&nbsp;&nbsp;码：</span><input type='password' name='password'>" + 
						"</li>"+
						"<li>" +
							"<span>员工编号：</span><input type='text' name='code'>" + 
						"</li>"+
						"<li>" +
						"<span>性&nbsp;&nbsp;&nbsp;&nbsp;别：</span>" + 
						"<label for='male'>" +
							"<input id='male' type='radio' name='sex' value='true' checked='checked' />" +
						"</label>" +
						"<label for='female'>" + 
							"<input id='female'  type='radio' name='sex' value='false' />" +
						"</label>" +
					"</li>" +
					"<li><a class='save'>确&nbsp;&nbsp;定</a></li>" +
				"</ul>" + 
			"</div>";
	var mask = "<div class='maskDiv'></div>"
	$("body").append(mask);
	$(".maskDiv").append(html);
	$(".save").click(function(){
		var name = $("input[name=name]").val();
		var psd = $("input[name=password]").val();
		var code = $("input[name=code]").val();
		var sex = $("input[name=sex]").val();
		var obj = {};
		obj.name = name;
		obj.psd = psd;
		obj.code = code;
		obj.sex = sex;
		$.ajax({
			async : false,
			cache : false,
	        type: "POST",
	        url: "/Tsme/account/saveUser",
	        contentType: "application/json; charset=utf-8",
	        data: JSON.stringify(obj),
	        dataType: "text",
	        success: function (message) {
	        	console.log(message);
	        },
	        error: function (message) {
	        	console.log(message);
	        }
	    });
	});
}

function addSingleDevice(){
	var html = "<div class='addDeviceDiv'>" +
					"<ul>" +
						"<li>" +
							"<span>设备编号</span><input type='text'>" + 
						"</li>" +
						"<li>" +
							"<span>经度</span><input type='password'>" + 
					"</li>" + 
					"<li>" + 
						"<span>纬度</span><input type='text'>" + 
					"</li>" + 
					"<li>" + 
						"<span>方位角</span><input type='text'>" + 
					"</li>" + 
					"<li>" + 
						"<span>距离</span><input type='text'>" + 
					"</li>" + 
					"<li><input type='button' value='确定'></li>" + 
				"</ul>" + 
			"</div>";
	var mask = "<div class='maskDiv'></div>"
	$("body").append(mask);
	$(".maskDiv").append(html);
	$(".save").click(function(){
		var name = $("");
		$(".maskDiv").hide();
		$(".conDiv").hide();
	});
}

function addBatchUser(){
	function ReadExcel() {
        var tempStr = "";
        //得到文件路径的值
        var filePath = document.getElementById("upfile").value;
        //创建操作EXCEL应用程序的实例
        var oXL = new ActiveXObject("Excel.application");
         //打开指定路径的excel文件
        var oWB = oXL.Workbooks.open(filePath);
        //操作第一个sheet(从一开始，而非零)
        oWB.worksheets(1).select();
        var oSheet = oWB.ActiveSheet;
        //使用的行数
      var rows =  oSheet .usedrange.rows.count; 
        try {
           for (var i = 2; i <= rows; i++) {
              if (oSheet.Cells(i, 2).value == "null" || oSheet.Cells(i, 3).value == "null") break;
              var a = oSheet.Cells(i, 2).value.toString() == "undefined" ? "": oSheet.Cells(i, 2).value;
              tempStr += (" " + oSheet.Cells(i, 2).value + " " + oSheet.Cells(i, 3).value + " " + oSheet.Cells(i, 4).value + " " + oSheet.Cells(i, 5).value + " " + oSheet.Cells(i, 6).value + "\n");
           }
        } catch(e) {
           document.getElementById("txtArea").value = tempStr;
        }
        document.getElementById("txtArea").value = tempStr;
        //退出操作excel的实例对象
        oXL.Application.Quit();
         //手动调用垃圾收集器
        CollectGarbage();
     }
}