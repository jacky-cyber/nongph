/* Events to trigger on every page load */
$(function(){
    
    // options for mobile image swiping
    var $imgs = $('#product_thumbs');
    var IMG_WIDTH = 320;
    var currentImg = 0;
	var fromImg = 0;
    var maxImages = $imgs.find('li').length;
    var speed = 250;
    var swipeOptions = {
            triggerOnTouchEnd   : false,    
            swipeStatus         : swipeStatus,
            allowPageScroll     : "vertical",
            threshold           : 40            
    }

    // Update the locale that has been selected
    HC.updateLocaleSelection();

    // Show the JavaScript version of product options if the user has JavaScript enabled
    $('.product-options').removeClass('hidden');
    $('.product-option-nonjs').remove();

	//Product page tab
	$('#product_description .tabs .tab').on('click',function(){
		if(!$(this).is('.currentTab')){
			$('#product_description .currentTab').removeClass('currentTab');
			$('#product_description .tab_content').hide();
			$(this).addClass('currentTab');
			var selected = $(this).attr('id');
			$("#product_description ."+selected).show();

		}
	});
	//当滚动条向下滚动时，tab一直置顶
	//$('#product_description .clearfix').on(''
	$(window).scroll(function(){
		if($(window).scrollTop()>$('#tab_separate').offset().top){
			$('#select_tab').addClass('put_top');
		}else{
			$('#select_tab').removeClass('put_top');
		}
	})
	
	$('#product_description #select_tab .tabs .buy .detailTabBuy').on('click',function(){
		$("form[name='cart']").submit();
	})

    //所有的sku和对应的选项值
    var allSkus = $('#product-option-data').data('product-option-pricing');
    //产品所有选项信息
    var productOptions = $('#product-option-data').data('product-options');
    //当前页面的sku选项信息
    var currentOption;
    //当前页面skuId
    var curSkuId =  $('#product-option-data').data('current-sku');
   
    //得到当前页面sku选项信息
	if(allSkus){
    for (var i = 0; i < allSkus.length; i++) {
        if (allSkus[i].skuId === curSkuId) {
            currentOption = allSkus[i].selectedOptions;
            break;
        }
    }
	}
   
    for (var i=0;i<$('.product-option-group').length;i++){
    	for(var j=0;j<$('.product-option-group').eq(i).find('ul li').length;j++){
    		var value = $('.product-option-group').eq(i).find('ul li').eq(j).data('product-option-value');
    		if(value.valueId===currentOption[i]){
    			$('.product-option-group').eq(i).find('ul li').eq(j).addClass('active');
    			$('.product-option-group').find('span.option-value').text(value.valueName);
    			break;
    		}
    	}
    }
    
    // Bind the JavaScript product option boxes to execute on click
    $('body').on('click', '.product-option-group li:not(.unavailable)', function() {
        HC.changeProductOption($(this));
        return false;
    });

    if (Modernizr.touch) {
        $imgs.swipe(swipeOptions);
    } else {
        $('.jqzoom').jqzoom({
            zoomType: 'innerzoom',
            title: false,
            zoomWidth: 402,
            zoomHeight: 402
        });
    }
    
    $('ul#products li .content').dotdotdot(); // trim product descriptions in the small layout
    $('ul#featured_products li .content').dotdotdot(); // trim product descriptions in the small layout

	if(maxImages==1){
		$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
	}

	/*****产品页面图片展示切换*******/
	
	/**初始化**/
	//当只有一张图片时，next按钮显灰
	if(maxImages==1){
		$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
	}
	//默认选中第一张图片
	$('#product_thumbs li').addClass("unselected");
	$('#product_thumbs li').first().removeClass('unselected').addClass("selected");

	//next按钮事件
	$('.gonext').on('click',function(){
			gonext();
		});
	//prev按钮事件
	$('.goprev').on('click',function(){
			goprev();
		});
	//图片点击时按钮
	$('#product_thumbs li').on('click',function(){
		for(var i=0;i<maxImages;i++){
			if($imgs.find('li').eq(i).find("a img").attr("src")==$(this).find("a img").attr("src")){
				currentImg = i;
				break;
			}
		}
		$('#product_thumbs .selected').removeClass('selected').addClass('unselected');
		$(this).removeClass('unselected').addClass("selected");
		$("#product_main_image a img").attr('src',$(this).find('a img').attr('src'));

		if(maxImages!=1){
			if(currentImg==maxImages-1){
				$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
			}else{
				$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right_light.gif)");
			}
			if(currentImg!=0){
				$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left.gif)");
			}else{
				$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left_gray.gif)");
			}
		}else{
			$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
			$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left_gray.gif)");
		}
	});
	function gonext() {

		//下一个图片index
		var next = currentImg + 1;
		//图片总数少于4
		if(maxImages<=4){
			if(currentImg==maxImages-1) return;
			else{
				//css改变边框，显示选中
				$('#product_thumbs li').eq(currentImg).removeClass('selected').addClass('unselected');
				$('#product_thumbs li').eq(next).removeClass('unselected').addClass("selected");

				$("#product_main_image a img").attr('src',$('#product_thumbs li').eq(next).find('a img').attr('src'));

				currentImg++;
			}
		}
		//图片总数大于4
		else{
			if(next==maxImages) return;
			else{
				if(next>=4){
					fromImg = next + 1 - 4;
					$imgs.css("left","-"+94*fromImg+"px");
					//css改变边框，显示选中
					$('#product_thumbs li').eq(currentImg).removeClass('selected').addClass('unselected');
					$('#product_thumbs li').eq(next).removeClass('unselected').addClass("selected");

					$("#product_main_image a img").attr('src',$('#product_thumbs li').eq(next).find('a img').attr('src'));

					currentImg++;
				}else{
					//css改变边框，显示选中
					$('#product_thumbs li').eq(currentImg).removeClass('selected').addClass('unselected');
					$('#product_thumbs li').eq(next).removeClass('unselected').addClass("selected");
					$("#product_main_image a img").attr('src',$('#product_thumbs li').eq(next).find('a img').attr('src'));

					currentImg++;
				}
			}
		}

		if(maxImages!=1){
			if(currentImg==maxImages-1){
				$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
			}else{
				$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right_light.gif)");
			}
			if(currentImg!=0){
				$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left.gif)");
			}else{
				$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left_gray.gif)");
			}
		}else{
			$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
			$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left_gray.gif)");
		}
	}

	function goprev() {
		if(currentImg==0) return;
		var pre = currentImg-1;
		if(pre<fromImg){
			fromImg--;
			$imgs.css("left","-"+94*fromImg+"px");
			$('#product_thumbs li').eq(currentImg).removeClass('selected').addClass('unselected');
			$('#product_thumbs li').eq(pre).removeClass('unselected').addClass("selected");

			$("#product_main_image a img").attr('src',$('#product_thumbs li').eq(pre).find('a img').attr('src'));
			currentImg--;
		}else{
			$('#product_thumbs li').eq(currentImg).removeClass('selected').addClass('unselected');
			$('#product_thumbs li').eq(pre).removeClass('unselected').addClass("selected");

			$("#product_main_image a img").attr('src',$('#product_thumbs li').eq(pre).find('a img').attr('src'));
			currentImg--;
		}

		if(maxImages!=1){
			if(currentImg==maxImages-1){
				$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
			}else{
				$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right_light.gif)");
			}
			if(currentImg!=0){
				$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left.gif)");
			}else{
				$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left_gray.gif)");
			}
		}else{
			$("#product_thumbs_container .gonext").css("background","url(/static/img/to_right.gif)");
			$("#product_thumbs_container .goprev").css("background","url(/static/img/to_left_gray.gif)");
		}
	}
    /**
     * Catch each phase of the swipe.
     * move : we drag the div.
     * cancel : we animate back to where we were
     * end : we animate to the next image
     */         
    function swipeStatus(event, phase, direction, distance) {
        
        //If we are moving before swipe, and we are going Lor R in X mode, or U or D in Y mode then drag.
        if (phase == "move" && (direction == "left" || direction == "right")) {
            var duration = 0;
            if (direction == "left") {
                scrollImages((IMG_WIDTH * currentImg) + distance, duration);
            } else if (direction == "right") {
                scrollImages((IMG_WIDTH * currentImg) - distance, duration);
            }
        } else if ( phase == "cancel") {
            scrollImages(IMG_WIDTH * currentImg, speed);
        } else if ( phase =="end" ) {
            if (direction == "right") {
                previousImage();
            } else if (direction == "left") {           
                nextImage();
            }
        }
        
    }

    function previousImage() {
        currentImg = Math.max(currentImg-1, 0);
        scrollImages( IMG_WIDTH * currentImg, speed);
    }
    
    function nextImage() {
        currentImg = Math.min(currentImg+1, maxImages-1);
        scrollImages( IMG_WIDTH * currentImg, speed);
    }
    
    /**
     * Manually update the position of the images on drag
     */
    function scrollImages(distance, duration) {
        //$imgs.css("-webkit-transition-duration", (duration/1000).toFixed(1) + "s");
        //inverse the number we set in the css
        var value = (distance<0 ? "" : "-") + Math.abs(distance).toString();
      //  $imgs.css("-webkit-transform", "translate3d("+value +"px,0px,0px)");
	  $imgs.css("left",value+"px");
    }
    
});

$(function(){			
   $(".jqzoom").jqueryzoom({
		xzoom:400,
		yzoom:400,
		offset:10,
		position:"right",
		preload:1,
		lens:1
	});
	$("#spec-list").marquee({
		deriction:"left",
		width:350,
		height:56,
		step:2,
		speed:4,
		delay:10,
		control:true,
		_front:"#spec-right",
		_back:"#spec-left"
	});
	$("#spec-list img").bind("mouseover",function(){
		var src=$(this).attr("src");
		$("#spec-n1 img").eq(0).attr({
			src:src.replace("\/n5\/","\/n1\/"),
			jqimg:src.replace("\/n5\/","\/n0\/")
		});
		$(this).css({
			"border":"2px solid #ff6600",
			"padding":"1px"
		});
	}).bind("mouseout",function(){
		$(this).css({
			"border":"1px solid #ccc",
			"padding":"2px"
		});
	});				
})


