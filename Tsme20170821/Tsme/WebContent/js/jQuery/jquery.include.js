$.extend({
    includePath: '',
    include: function(file)
    {
        var files = typeof file == "string" ? [file] : file;
        for (var i = 0; i < files.length; i++)
        {
        	try{
	            var name = files[i].replace(/^\s|\s$/g, "");
	            var att = name.split('.');
	            var ext = att[att.length - 1].toLowerCase();
	            var isCSS = ext == "css";
	            var tag = isCSS ? "link" : "script";
	            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " language='javascript' type='text/javascript' ";
	            var link = (isCSS ? "href" : "src") + "='" + $.includePath + name + "'";
	            if ($(tag + "[" + link + "]").length == 0) document.write("<" + tag + attr + link + "></" + tag + ">");
        	}catch(e){
        		console.log("loading file failed " + files[i]);
        	}
        }
    }
});

//ʹ�÷���
// var temp = "192.168.85.99:8080";
//$.includePath = "http://" + domain + "/javascript/";
//$.include(['http://image.esunny.com/script/jquery.divbox.js','/css/pop_win.css']);