var time = 60, phoneAction = "";

function initPage(){
	$('.backgroundDiv').css('height', $(window).height() - 80);
	if($("#state").val()=="register"){
		$("#navTitle").html("用户注册");
		$("#navLoginText").show();
		$("#navRegistText").hide();
		loadPage("/Tsme/account/userRegister");
	}
	else if($("#state").val()=="retrieve"){
		$("#navTitle").html("密码找回");
		$("#navLoginText").show();
		$("#navRegistText").hide();
		loadPage("/Tsme/security/retrieve/userRetrieve");
	}
	else{
		$("#navTitle").html("系统登录");
		$("#navLoginText").hide();
		$("#navRegistText").show();
		loadPage("/Tsme/security/login/userLogin");
	}
}

function navTurn(){
	var aA = $(".navTitlePos a");
	aA.live("click",function(){
		if($(this).html() == '点击注册'){
			$("#navTitle").html("用户注册");
			$("#navLoginText").show();
			$("#navRegistText").hide();
			loadPage("/Tsme/account/userRegister");
		}
		if($(this).html() == '在此登录'){
			$("#navTitle").html("系统登录");
			$("#navLoginText").hide();
			$("#navRegistText").show();
			loadPage("/Tsme/security/login/userLogin");
		}
		if($(this).html() == "忘记密码?"){
			$("#navTitle").html("密码找回");
			$("#navLoginText").show();
			$("#navRegistText").hide();
			loadPage("/Tsme/security/retrieve/userRetrieve");
		}
	});
}

function userSubmit() {	
	var username = $("#username").val();
	$("#username").val(trim(username));
	if($("#username").val() ==""){
		$("#loginAccountDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("请填写用户名");
		$("#userrorLog").show();
		return;
	}
	else if($("#password").val() == ""){
		$("#passwordDiv").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("请填写密码");
		$("#pwderrorLog").show();
		return;
	}
	else{
		if($("#userInfoInputValidate").is(":visible")){
			if($("#validateCode").val() == ""){
				$("#validateDiv").css('border','1px solid #ff0000');
				$("#validateCodeErrorLog").html("请填写验证码");
				$("#validateCodeErrorLog").show();
				return;
			}
		}	
	}
	
	$.ajax({
		async: false,
		cache: false,
		data: { "username":$("#username").val(), "password":$("#password").val(), "validateCode":$("#validateCode").val()},
		type: 'POST', 
		dataType: 'text',
		url: "/Tsme/security/login/checkloginInfo?tempid=" + Math.random(),
		success: function(val){
			if(val == "right"){
				var form = $("#userLoginForm")[0];
				form.submit();
			} else if(val == "usernameNotExisted"){	
				$("#loginAccountDiv").css('border','1px solid #ff0000');
				$("#userrorLog").html("该用户名未被注册");
				$("#userrorLog").show();
				$('.userInfoInputValidate').show();
				return;
			} else if(val == "passwordFalse"){
				$("#passwordDiv").css('border','1px solid #ff0000');
				$("#pwderrorLog").html("密码输入错误");
				$("#pwderrorLog").show();
				$('.userInfoInputValidate').show();
				return;
			} else if(val == "validateCodeFalse"){
				$("#validateDiv").css('border','1px solid #ff0000');
				$("#validateCodeErrorLog").html("验证码输入错误");
				$("#validateCodeErrorLog").show();
				$('.userInfoInputValidate').show();
			}
		},
		error: function(){
			console.log("error");
		}
	});
}

function trim(str){
	return str.replace(/(^[\s\n\f\r\t\v]*)|([\s\n\f\r\t\v]*$)/g, ""); 
}

function initInput(){
	$("#username").live("focus",function(){
		InputOnFocus(this);
	});
	$("#password").live("focus",function(){
		InputOnFocus(this);
	});
	$("#validateCode").live("focus",function(){
		InputOnFocus(this);
	});
	$("#username").live("blur",function(){
		InputOnBlur(this);
	});
	$("#password").live("blur",function(){
		InputOnBlur(this);
	});
	$("#validateCode").live("blur",function(){
		InputOnBlur(this);
	});
	$("#sec_password").live("focus",function(){
		InputOnFocus(this);
	})
	$("#sec_password").live("blur",function(){
		InputOnBlur(this);
	})
}

function loadPage(url){
	$.ajax({
		async: false,
		url: url,
		type: "POST",
		success: function(data){
			$(".userInfoDetail").html(data);
	    }
	});
}

function InputOnFocus(obj){
	$(obj).siblings(".errorLog").hide();
	var parentDiv = obj.parentNode;
	parentDiv.style.border = "1px solid #4fc1e9";
}

function InputOnBlur(obj){
	var parentDiv = obj.parentNode; 
	parentDiv.style.border="1px solid #a1a4a6";

}

function emailValidate(){
	window.location.href = "/Tsme/account/userInfoRegister/registerValidate";
}

function changeCheckNum(){
    var img = document.getElementById("checkNumImage");
    img.src="/Tsme/security/login/checkNumberShow?timeStamp=" + new Date().getTime();
}

function checkState(){
	$("#username").val("");
	$("#password").val("");
	if($("#username").val().length >0)
		$("#AccountPlaceholder").hide();
	if($("#password").val().length >0)
		$("#PwdPlaceholder").hide();
	if($("#sec_password").length > 0 && $("#sec_password").val().length >0)
		$("#PwdTwicePlaceholder").hide();
	if($("#validateCode").val().length >0)
		$("#validatePlaceholder").hide();
}

$(function () {
	$('#username').on('valuechange', function (e, previous) {
		if($('#username').val().length!=0)
			$('#AccountPlaceholder').hide();
		else
			$('#AccountPlaceholder').show();
	    })
	$('#password').on('valuechange', function (e, previous) {
	    if($('#password').val().length!=0)
			$('#PwdPlaceholder').hide();
		else
			$('#PwdPlaceholder').show();
	    })
	 $('#validateCode').on('valuechange', function (e, previous) {
	    if($('#validateCode').val().length!=0)
			$('#validatePlaceholder').hide();
		else
			$('#validatePlaceholder').show();
	    })
	 $('#sec_password').on('valuechange', function (e, previous) {
	    if($('#sec_password').val().length!=0)
			$('#PwdTwicePlaceholder').hide();
		else
			$('#PwdTwicePlaceholder').show();
	    })
})

$.event.special.valuechange = {
	 teardown: function (namespaces) {
	 $(this).unbind('.valuechange');
	 },
	 handler: function (e) {
		 $.event.special.valuechange.triggerChanged($(this));
	    },
	    add: function (obj) {
	    $(this).on('keyup.valuechange cut.valuechange paste.valuechange input.valuechange', obj.selector, $.event.special.valuechange.handler)
	    },
	    triggerChanged: function (element) {
	    var current = element[0].contentEditable === 'true' ? element.html() : element.val()
	    , previous = typeof element.data('previous') === 'undefined' ? element[0].defaultValue : element.data('previous')
	    if (current !== previous) {
	    element.trigger('valuechange', [element.data('previous')])
	    element.data('previous', current)
	    }
	}
}

function checkInfo() {
	if ( $("#username").val().length == 0){
		$("#registerAccountDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("请输入用户名");
		$("#userrorLog").show();
		return ;
	}else if ( $("#username").val().length < 6 && $("#username").val().length > 0){
		$("#registerAccountDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("用户名长度过短");
		$("#userrorLog").show();
		return ;
	}else if ( $("#username").val().length > 150){
		$("#registerAccountDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("用户名长度过长");
		$("#userrorLog").show();
		return ;
	}else if (!emailPattern.test($("#username").val())&&!telphonePattern.test($("#username").val())){
		$("#registerAccountDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("用户名格式不正确");
		$("#userrorLog").show();
		return ;
	}else if($("#validateCode").val().length == 0){
		$("#validateDiv").css('border','1px solid #ff0000');
		$("#validateCodeErrorLog").html("请输入验证码");
		$("#validateCodeErrorLog").show();
		return;
	}
	$.ajax({
		async: false,
		cache: false,
		data: { "username":$("#username").val(), "validateCode":$("#validateCode").val()},
		type: 'get', 
		dataType: 'text',
		url: "/Tsme/account/checkInfo?tempid=" + Math.random(),
		success: function(val){
			if(val == "right" && emailPattern.test($("#username").val())){
				var form = $("#userRegisterForm")[0];
				form.action="/Tsme/account/sendValidate";
				form.submit();
			}/* else if(val == "right" && telphonePattern.test($("#username").val())){
				$.ajax({
					async: false,
					cache: false,
					data: { "phone":$("#username").val()},
					type: 'get', 
					dataType: 'text',
					url: "/Tsme/account/userInfoRegister/phoneCheck?tempid=" + Math.random(),
					success: function(val){
						loadPage("/Tsme/account/userInfoRegister/phoneRegister?phone=" + $("#username").val());
					}
				});
			}*/
			else if(val == "usernameExisted"){	
				$("#registerAccountDiv").css('border','1px solid #ff0000');
				$("#userrorLog").html("该用户名已被注册");
				$("#userrorLog").show();
			}else if(val == "validateCodeFalse"){	
				$("#validateDiv").css('border','1px solid #ff0000');
				$("#validateCodeErrorLog").html("验证码输入错误");
				$("#validateCodeErrorLog").show();
			}
		},
		error: function(){
			console.log("error");
		}
	});
}

function retrieveInfo() {
	if ( $("#username").val().length == 0){
		$("#accountInputDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("请输入账户名");
		$("#userrorLog").show();
		return ;
	}else if ( $("#username").val().length < 6 && $("#username").val().length > 0){
		$("#accountInputDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("账户名长度过短");
		$("#userrorLog").show();
		return ;
	}else if ( $("#username").val().length > 150){
		$("#accountInputDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("账户名度过长");
		$("#userrorLog").show();
		return ;
	}else if (!emailPattern.test($("#username").val()) && !phoneNumPattern.test($("#username").val())){
		$("#accountInputDiv").css('border','1px solid #ff0000');
		$("#userrorLog").html("账户名格式不正确");
		$("#userrorLog").show();
		return ;
	}else if($("#validateCode").val().length == 0){
		$("#validateCodeDiv").css('border','1px solid #ff0000');
		$("#validateCodeErrorLog").html("请输入验证码");
		$("#validateCodeErrorLog").show();
		return;
	}
	$.ajax({
		async: false,
		cache: false,
		data: { "username":$("#username").val(), "validateCode":$("#validateCode").val()},
		type: 'get', 
		dataType: 'text',
		url: "/Tsme/security/login/checkInfo?tempid=" + Math.random(),
		success: function(val){
			if(val == "right"){
				if (emailPattern.test($("#username").val())){
					var form = $("#userRetreiveForm")[0];
					form.action="/Tsme/security/retrieve/retrieveEmailSend";
					form.submit();
				} /*else if (phoneNumPattern.test($("#username").val())){
					$.ajax({
						async: false,
						cache: false,
						data: { "phone":$("#username").val()},
						type: 'get', 
						dataType: 'text',
						url: "/Tsme/security/retrieve/phoneCheck?tempid=" + Math.random(),
						success: function(val){
							if (val == "right")
								loadPage("/Tsme/security/retrieve/phoneRetrieve?phone=" + $("#username").val());
						}
					});
				}*/
				
			} else if(val == "accountNotExisted"){
				$("#accountInputDiv").css('border','1px solid #ff0000');
				$("#userrorLog").html("该账号未被注册");
				$("#userrorLog").show();
				return ;
			} else if(val == "validateCodeFalse"){
				$("#validateCodeDiv").css('border','1px solid #ff0000');
				$("#validateCodeErrorLog").html("验证码输入错误");
				$("#validateCodeErrorLog").show();
				return ;
			}
		},
		error: function(){
			console.log("error");
		}
	});
}

function retrieveAndLogin(){
	if($("#password").val().length==0){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("请输入密码");
		$("#pwderrorLog").show();
		return;
	}
	else if ($("#password").val().length<6 && $("#password").val().length>0){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过短");
		$("#pwderrorLog").show();
		return;
	}else if ($("#password").val().length >20){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过长");
		$("#pwderrorLog").show();
		return;
	}else if (ilcode.test($("#password").val()) ){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码中含有非法字符");
		$("#pwderrorLog").show();
		return;
	}else if ($("#password").val() != $("#sec_password").val()){
		$("#EnsurePassword").css('border','1px solid #ff0000');
		$("#ensurePwdErrorLog").html("两次密码输入不一致");
		$("#ensurePwdErrorLog").show();
		return;
	}
	$("#userRetrieveForm").submit();
}

function registerAndLogin(){
	if($("#password").val().length==0){
		$("#validatePwdDiv").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("请输入密码");
		$("#pwderrorLog").show();
		return;
	} else if($("#password").val().length < 6 && $("#password").val().length > 0){
		$("#validatePwdDiv").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过短");
		$("#pwderrorLog").show();
		return;
	} else if($("#password").val().length > 20){
		$("#validatePwdDiv").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过长");
		$("#pwderrorLog").show();
		return;
	} else if(ilcode.test($("#password").val()) ){
		$("#validatePwdDiv").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("含有非法字符");
		$("#pwderrorLog").show();
		return;
	} else if($("#password").val() != $("#sec_password").val()){
		$("#sec_validatePwdDiv").css('border','1px solid #ff0000');
		$("#ensurePwdErrorLog").html("两次密码输入不一致");
		$("#ensurePwdErrorLog").show();
		return;
	}
	
	$("#userRegisterForm").submit();
}

function phoneRegisterAndLogin(){
	$("#EnsurePassword").css('border','1px solid #a1a4a6');
	$("#ensurePwdErrorLog").html("");
	$("#InputPassword").css('border','1px solid #a1a4a6');
	$("#pwderrorLog").html("");
	$("#phoneValidateCode").css('border','1px solid #a1a4a6');
	$("#validateCodeErrorLog").html("");
	
	if($("#validateCode").val().length == 0){
		$("#validateDiv").css('border','1px solid #ff0000');
		$("#validateCodeErrorLog").html("请输入验证码");
		$("#validateCodeErrorLog").show();
		return;
	}
	else if($("#password").val().length==0){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("请输入密码");
		$("#pwderrorLog").show();
		return;
	}
	else if ($("#password").val().length<6 && $("#password").val().length>0){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过短");
		$("#pwderrorLog").show();
		return;
	}else if ($("#password").val().length >20){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过长");
		$("#pwderrorLog").show();
		return;
	}else if (ilcode.test($("#password").val()) ){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码中含有非法字符");
		$("#pwderrorLog").show();
		return;
	}else if ($("#password").val() != $("#sec_password").val()){
		$("#EnsurePassword").css('border','1px solid #ff0000');
		$("#ensurePwdErrorLog").html("两次密码输入不一致");
		$("#ensurePwdErrorLog").show();
		return;
	}
	
	$.ajax({
		async: false,
		cache: false,
		data: { "phone":$("#username").val(),"stamp":$("#validateCode").val()},
		type: 'get', 
		dataType: 'text',
		url: "/Tsme/account/userInfoRegister/checkPhoneStamp?tempid=" + Math.random(),
		success: function(val){
			if(val == "right"){
				$("#userRegisterForm").submit();
			}else{
				$("#phoneValidateCode").css('border','1px solid #ff0000');
				$("#validateCodeErrorLog").html("验证码失效或错误");
				$("#validateCodeErrorLog").show();
			}
		},
		error: function(){
			console.log("error");
		}
	});
}

function phoneRetrieveAndLogin(){
	if($("#validateCode").val().length == 0){
		$("#validateDiv").css('border','1px solid #ff0000');
		$("#validateCodeErrorLog").html("请输入验证码");
		$("#validateCodeErrorLog").show();
		return;
	}
	else if($("#password").val().length==0){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("请输入密码");
		$("#pwderrorLog").show();
		return;
	}
	else if ($("#password").val().length<6 && $("#password").val().length>0){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过短");
		$("#pwderrorLog").show();
		return;
	}else if ($("#password").val().length >20){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码长度过长");
		$("#pwderrorLog").show();
		return;
	}else if (ilcode.test($("#password").val()) ){
		$("#InputPassword").css('border','1px solid #ff0000');
		$("#pwderrorLog").html("密码中含有非法字符");
		$("#pwderrorLog").show();
		return;
	}else if ($("#password").val() != $("#sec_password").val()){
		$("#EnsurePassword").css('border','1px solid #ff0000');
		$("#ensurePwdErrorLog").html("两次密码输入不一致");
		$("#ensurePwdErrorLog").show();
		return;
	}
	
	$.ajax({
		async: false,
		cache: false,
		data: { "phone":$("#username").val(),"stamp":$("#validateCode").val()},
		type: 'get', 
		dataType: 'text',
		url: "/Tsme/security/retrieve/checkPhoneStamp?tempid=" + Math.random(),
		success: function(val){
			if(val == "right"){
				$("#userRetrieveForm").submit();
			}else{
				$("#phoneValidateCode").css('border','1px solid #ff0000');
				$("#validateCodeErrorLog").html("验证码失效或错误");
				$("#validateCodeErrorLog").show();
			}
		},
		error: function(){
			console.log("error");
		}
	});
}


function initRegisterRequest(){
	phoneAction = setTimeout('resendRegisterRequest()', 1000);  
}

function resendRegisterRequest(){ 
	$("#resendRequst").show();
	$(".getCode").css("color","#a1a4a6");
    time = time - 1;  
    $("#time").html(time); 
    
    if (time <= 0) {
    	clearTimeout(phoneAction);
    	$("#resendRequst").hide();
    	$(".getCode").css("color","#252626");
    	$(".getCode").bind("click", function(){
    		if (time > 0)
    			return false;
    		$.ajax({
    			async: false,
    			cache: false,
    			data: { "phone":$("#username").val(),},
    			type: 'get', 
    			dataType: 'text',
    			url: "/Tsme/account/userInfoRegister/phoneCheck?tempid=" + Math.random(),
    			success: function(val){
    				if(val == "right"){
    					time = 60;
    					initRegisterRequest();
    				}
    			},
    			error: function(){
    				console.log("error");
    			}
    		});
    	});
    	return ;
    }
    initRegisterRequest();
 } 

function initRetrieveRequest(){
	phoneAction = setTimeout('resendRetrieveRequest()', 1000);  
}

function resendRetrieveRequest(){ 
	$("#resendRequst").show();
	$(".getCode").css("color","#a1a4a6");
    time = time - 1;  
    $("#time").html(time); 
    
    if (time <= 0) {
    	clearTimeout(phoneAction);
    	$("#resendRequst").hide();
    	$(".getCode").css("color","#252626");
    	$(".getCode").bind("click", function(){
    		if (time > 0)
    			return false;
    		$.ajax({
    			async: false,
    			cache: false,
    			data: { "phone":$("#username").val(),},
    			type: 'get', 
    			dataType: 'text',
    			url: "/Tsme/security/retrieve/phoneCheck?tempid=" + Math.random(),
    			success: function(val){
    				if(val == "right"){
    					time = 60;
    					initRetrieveRequest();
    				}
    			},
    			error: function(){
    				console.log("error");
    			}
    		});
    	});
    	return ;
    }
    initRetrieveRequest();
 } 
