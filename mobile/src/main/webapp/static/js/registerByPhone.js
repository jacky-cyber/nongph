$(function() {
	$("#register .item .vc-btn:not(.btnResend-disabled)").on("click", function() {
		var mob = $.trim($("#register .item .item-phone input").val());
		// 前端js验证手机号码不能为空
		if (mob == "") {
			alert("手机号码不能为空！");
			return;
		}
		// 前端js验证手机号码格式
		var reg = /^1[3|4|5|7|8]\d{9}$/;
		if (!reg.test(mob)) {
			alert("手机号码格式不对！");
			return;
		}
		// 获取短信验证码
		BLC.ajax({
			url : "/sms/" + mob,
			type : "POST",
			dataType : "json"
		}, function(data, extraData) {
			if (data.success) {
				alert(data.success);
			}
			if (data.error) {
				alert(data.error);
			}
		});
	})
	
		$("#resetPasswordByphone .item .vc-btn").on("click", function() {
		var mob = $.trim($("#resetPasswordByphone .item .item-phone input").val());

		// 前端js验证手机号码不能为空
		if (mob == "") {
			alert("手机号码不能为空！");
			return;
		}
		// 前端js验证手机号码格式
		var reg = /^1[3|4|5|8]\d{9}$/;
		if (!reg.test(mob)) {
			alert("手机号码格式不对！");
			return;
		}
		// 获取短信验证码
		BLC.ajax({
			url : "/sms/forgetPassword/" + mob,
			type : "POST",
			dataType : "json"
		}, function(data, extraData) {
			if (data.success) {
				alert(data.success);
			}
			if (data.error) {
				alert(data.error);
			}
		});
	})

	$("#content .manage-account-form-wrapper .item .vc-btn").on("click", function() {
		var newMob = $.trim($("#content .manage-account-form-wrapper #newPhone").val());
		var oldMob = $.trim($("#content .manage-account-form-wrapper #oldPhone").val());
		// 前端js验证手机号码不能为空
		if (oldMob == "") {
			alert("旧手机号码不能为空！");
			return;
		}
		// 前端js验证手机号码格式
		var reg = /^1[3|4|5|8]\d{9}$/;
		if (!reg.test(oldMob)) {
			alert("旧手机号码格式不对！");
			return;
		}
		
		if (newMob == "") {
			alert("新手机号码不能为空！");
			return;
		}
		// 获取短信验证码
		BLC.ajax({
			url : "/sms/resetPhone/" + oldMob + "/" + newMob,
			type : "GET",
			dataType : "json"
		}, function(data, extraData) {
			if (data.success) {
				alert(data.success);
			}
			if (data.error) {
				alert(data.error);
			}
		});
	})
	
//	$("#content .manage-account-form-wrapper #validationCodeSumbmit").on("click", function() {
//		var mob = $.trim($("#content .manage-account-form-wrapper .item .item-phone input").val());
//		var validationCode = $.trim($("#content .manage-account-form-wrapper #validationCode input").val());
//
//		// 前端js验证手机号码不能为空
//		if (mob == "") {
//			alert("手机号码不能为空！");
//			return;
//		}
//		// 前端js验证手机号码格式
//		var reg = /^1[3|4|5|8]\d{9}$/;
//		if (!reg.test(mob)) {
//			alert("手机号码格式不对！");
//			return;
//		}
//		//验证码不能为空
//		if(validationCode == ""){
//			alert("验证码不能为空");
//			return;
//		}
//		var reg1 = /^\d{6}$/;
//		if (!reg1.test(validationCode)) {
//			alert("验证码格式不对！");
//			return;
//		}
//		// 手机认证
//		BLC.ajax({
//			url : "/sms/" + mob+"/"+validationCode,
//			type : "POST",
//			dataType : "json"
//		}, function(data, extraData) {
//			if (data.success) {
//				alert("手机认证成功，5秒后将跳转到账号信息页面");
//				setTimeout(function(){window.location="/account";},5000)
//			}
//			if (data.error) {
//				alert(data.error);
//			}
//		});
//	})
})