$(function () {

    var isWechatBrowser = function () {
        var ua = window.navigator.userAgent.toLowerCase();
        return ua.match(/MicroMessenger/i) == 'micromessenger';
    };

    var toCompletePay = function(){
        var orderNumber = $('#orderNumber').val();
        location.href = "/pay/complete/" + orderNumber;
    };

    var toAliPay = function(){
        var orderNumber = $('#orderNumber').val();
        location.href = "/alipay/prepare/" + orderNumber;
    };
    
    var toWechatPay = function(){
        var orderNumber = $('#orderNumber').val();
        var appid = $('#appid').val();
        var redirectUri = "http://" + location.host + "/wechatpay/checkout?showwxpaytitle=1&orderNumber=" + orderNumber;
        location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + appid
            + '&redirect_uri=' + encodeURIComponent(redirectUri)
            + '&response_type=code&scope=snsapi_base&state=123#wechat_redirect';
    };
    
    var wechatPayCheckout = function() {
        $(".wechat a").on("click", toWechatPay);
    	$(".ali a").on("click", toAliPay);
    };

    if (isWechatBrowser()) {
        wechatPayCheckout();
    } else {
    	$(".wechat").hide();
    	$(".ali a").on("click", toAliPay);
    }
    $(".complete a").on("click", toCompletePay);
});