/* Operations that deal with checkout */

$(document).ready(function () {

    $('.chosePickupAddress').click(function () {
        $(this).next().slideToggle();
    });

    $('#commit').click(function () {
    	if($("#selectAddress").length > 0){
    		HC.showNotification("请选择地址", 4000);
    		return ;
    	}
        var $orderForm = $('#orderForm');
        $orderForm.attr("action", "/checkout/submitOrder");
        $orderForm.submit();
    })
});
