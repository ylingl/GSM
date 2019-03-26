$.ajax({
	async: false,
	success: function(response){
		if (document.getElementsByName("roleOrder")[0].value == 3) {
			document.getElementById("adminLeftBar").innerHTML = response;
			
			var regDetectJs = /<script(.|\n)*?>(.|\n|\r\n)*?<\/script>/ig;
			var jsContained = response.match(regDetectJs);
	
			// 第二步：如果包含js，则一段一段的取出js再加载执行
			if(jsContained) {
				// 分段取出js正则
				var regGetJS = /<script(.|\n)*?>((.|\n|\r\n)*)?<\/script>/im;
	
				// 按顺序分段执行js
				var jsNums = jsContained.length;
				for (var i=0; i<jsNums; i++) {
					var jsSection = jsContained[i].match(regGetJS);
	
					if(jsSection[2]) {
						if(window.execScript) {
							// 给IE的特殊待遇
							window.execScript(jsSection[2]);
						} else {
							// 给其他大部分浏览器用的
							window.eval(jsSection[2]);
						}
					}
				}
			}
		}
	},
	url: "/Tsme/navigate/leftTools/adminLeftBar.jsp",
	type: "get"
});