/**
 * Created by steven on 7/22/15.
 */

var isWechatBrowser = function () {
    var ua = window.navigator.userAgent.toLowerCase();
    return ua.match(/MicroMessenger/i) == 'micromessenger';
};

var toWechatPay = function () {
    var appid = $('#appid').val();
    var url = $('#url').val();
    var ext = (url == "") ? "" : ("?url=" + encodeURIComponent(url));
    var redirectUri = "http://" + location.host + "/wechat/getOpenid" + ext;
    location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + appid
        + '&redirect_uri=' + encodeURIComponent(redirectUri)
        + '&response_type=code&scope=snsapi_base&state=123#wechat_redirect';
};

if (isWechatBrowser()) {
    toWechatPay();
} else {
    location.href = "/";
}