$(function(){
	$('body').on('click', 'header .main_bar .bar_left', function() {
		var ccid = $("#ccid").val();
		location.href="/category/all?ccid=" + ccid;
	});
});