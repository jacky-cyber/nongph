$(function(){
	var bkColors = ["#E91F1F","#FD8121","lightblue","#FD8121"];
	var count = $('.banner_image').length;
	var current = 0;
	$("#banner_image"+current).css("z-index",1);
	$("#banner_link"+current).css("z-index",2);
	$("#banner_image"+current).css("left","0");
	$("#banner_link"+current).css("left","0");
	$("#banner_trigger"+current).addClass("cur");
	$("#banner_outer_outer").css("background-color",bkColors[current]);

	function trigger(){
		var next = (current+1)%count;
		$("#banner_image"+next).css("left","0");
		$("#banner_link"+next).css("left","0");
		$("#banner_outer_outer").css("background-color",bkColors[next]);
		
		$("#banner_image"+current).animate(
			{left:"-12000px"},
			1000,
			function(){
				$("#banner_link"+current).css("left","-12000px");
				
				$("#banner_image"+current).css("z-index",0);
				$("#banner_link"+current).css("z-index",0);
				$("#banner_trigger"+current).removeClass("cur");
				
				$("#banner_image"+next).css("z-index",1);
				$("#banner_link"+next).css("z-index",2);
				$("#banner_trigger"+next).addClass("cur");
				
				current = next;
			}
		);
		
	};
	var interval = setInterval(trigger,6000);

	$("#banners_outer #banners_triggers a").bind("mouseover",function(){
		clearInterval(interval);
		var to = $(this).attr("id").charAt(14);
		$("#banner_image"+to).css("left","0");
		$("#banner_link"+to).css("left","0");
		$("#banner_link"+current).css("left","-12000px");
		
		$("#banner_image"+current).css("z-index",0);
		$("#banner_link"+current).css("z-index",0);
		$("#banner_trigger"+current).removeClass("cur");
		
		$("#banner_image"+to).css("z-index",1);
		$("#banner_link"+to).css("z-index",2);
		$("#banner_trigger"+to).addClass("cur");
		$("#banner_outer_outer").css("background-color",bkColors[to]);
		current = to;
		interval = setInterval(trigger,6000);
	});

});

$(function(){
	var num=$('input:radio[name="address.id"]:checked').val();
	if(num==0) $("#shipping_address").show();
	else $("#shipping_address").hide();
	$('input:radio[name="address.id"]:checked').parent().addClass("address_selected");
	$('input[name="address.id"]').bind("click",function(){
			var val=$('input:radio[name="address.id"]:checked').val();
			if(val==0){
				$("#shipping_address").show();
			}else{
				$("#shipping_address").hide();
			}

			$('input:radio[name="address.id"]').parent().removeClass("address_selected");
			$('input:radio[name="address.id"]:checked').parent().addClass("address_selected");
		});
});