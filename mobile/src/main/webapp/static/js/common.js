/* Events to trigger on every page load */
$(function(){
    // options for mobile image swiping
    var $imgs = $('#product_thumbs');
    var IMG_WIDTH = 320;
    var currentImg = 0;
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
        $imgs.css("-webkit-transition-duration", (duration/1000).toFixed(1) + "s");
        //inverse the number we set in the css
        var value = (distance<0 ? "" : "-") + Math.abs(distance).toString();
        $imgs.css("-webkit-transform", "translate3d("+value +"px,0px,0px)");
    }
    
});
$(function(){
	$("#searchbtn").on("click",function(){
		$("#doSearch").submit();
	});
});

$(function() {
  $('#slides').slidesjs({
	width: 320,
	height: 320,
	navigation: false,
	play: {
		active: false,
		auto: false,
		interval: 4000,
		swap: true
	}
  });
});