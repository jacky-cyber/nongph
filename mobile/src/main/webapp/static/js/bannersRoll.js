$(function(){
	var count, current, hg, wd; 
	count = $('#banners_outer div ul li').length;
	current = 0;
	$("#banners_triggers a").eq(current).addClass("cur");
	
	wd = $("#banners_outer").width();
	hg = wd / 2.6;
	$("#banners_outer").css("height",hg+"px");
	$("#banners_outer div ul").css("width",count * 100+"%");
	$("#banners_outer div ul li").css("width",100/count + "%");
	
	$(window).resize(function() {
		  wd = $("#banners_outer").width();
		  hg = wd / 2.6;
		  $("#banners_outer").css("height",hg+"px");
	});

	if(count<=1) {
		$("#banners_outer #banners_triggers").css("display","none");
	}
	
	function turnTo(){
		var next = (current + 1) % count;
		$("#banners_triggers a").removeClass("cur");
		$("#banners_triggers a").eq(next).addClass("cur");
		var left = next * 100 + "%";
		$("#banners_outer div ul").animate(
			{left:"-" + left},
			500,
			function(){
				current = next;
			}
		);
		
	};
	function changeTo(n){
		var next = n % count;
		$("#banners_triggers a").removeClass("cur");
		$("#banners_triggers a").eq(next).addClass("cur");
		var left = next * 100 + "%";
		$("#banners_outer div ul").animate(
			{left:"-" + left},
			500,
			function(){
				current = next;
			}
		);
		
	};

	var interval = setInterval(turnTo,6000);
//
	$("#banners_outer #banners_triggers a").bind("mouseover",function(){
		clearInterval(interval);
		changeTo($(this).attr("data-item"));
		interval = setInterval(turnTo,6000);
	});
	
	
	$("#banners_outer div ul").on("touchstart", function(e){
		clearInterval(interval);
		e.preventDefault();
		var  touches = e.originalEvent.touches[0];
		$(this).attr("touchstartx", touches.pageX);
		$(this).attr("touchstarty", touches.pageY);
	});
	$("#banners_outer div ul").on("touchmove", function(e){
		var  touches = e.originalEvent.touches[0];
		var x = touches.pageX - $(this).attr("touchstartx");
		var y = touches.pageY - $(this).attr("touchstarty");
		var left = x - wd * current;
		$("#banners_outer div ul").css("left",left+"px");
	});
	
	$("#banners_outer div ul").on("touchend", function(e){
		var touches = e.originalEvent.changedTouches[0];
		var x = touches.pageX - $(this).attr("touchstartx");
		var y = touches.pageY - $(this).attr("touchstarty");
		//right
		if(x < 0) {
			changeTo(current + 1);
			$(this).removeAttr("touchstartx");
			$(this).removeAttr("touchstarty");
			
		}
		//left
		else{
			changeTo(current + count - 1)
			$(this).removeAttr("touchstartx");
			$(this).removeAttr("touchstarty");
		}
		interval = setInterval(turnTo,6000);
	});

});
