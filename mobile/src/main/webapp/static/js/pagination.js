$(function() {
	$(window).on(
			"scroll",
			loadMore
	);
})

$(function() {
	$(window).on(
			"load",
			loadMore
	);
})

var loadMore = function() {
	if (($("#loadMoreItems").offset().top) <= ($(document)
			.scrollTop() + 100 + $(window).height())) {
		var q = $("#loadMoreItems .q").text();
		var cat = $("#loadMoreItems .cat").text();
		var page = parseInt($("#loadMoreItems .page").text()) + 1;
		$("#loadMoreItems .page").text(page);
		var totalPage = parseInt($("#loadMoreItems .totalPage").text());
		if(page <= totalPage){
			$(window).unbind("scroll",loadMore);
			var url = "";
			if(cat==""){
				url = "/ajaxSearch?q=" + q + "&page=" + page;
			}else{
				url = cat + "/ajaxCategory?page=" + page;
			}
			// 加载更多查询结果
			BLC.ajax({
				url : url,
				type : "GET",
				dataType : "json"
			}, function(data, extraData) {
				if (data) {
					for (var i = 0; i < data.length; i++) {
						var part1 = "<li class='product_container'>"
								+ "<a class='sku_img_lk' href='"
								+ data[i].href + "'>" + "<img alt='"
								+ data[i].alt + "' src='"
								+ data[i].media + "'></img>" + "</a>"
								+ "<div class='sku_info'>"
								+ "<p class='sku_name'>" + "<a herf='"
								+ data[i].href + "'>" + data[i].alt
								+ "</a>" + "</p>"
								+ "<p class='sku_desc'>"
								+ data[i].description + "</p>";
						var part2 = "";
						if (data[i].onSale) {
							part2 = "<p class='sku_salePrice'>"
									+ "<span class='has-sale'>" + " ￥"
									+ data[i].retailPrice + "</span>"
									+ "<span class='sale'" > +" ￥"
									+ data[i].salePrice + "</span>"
									+ "</p>";
						} else {
							part2 = "<p class='sku_salePrice'>"
									+ "<span>" + " ￥"
									+ data[i].retailPrice + "</span>"
									+ "</p>";
						}

						var part3 = "</div>" + "</li>";
						$("#products").append(part1 + part2 + part3);
					}
					$(window).bind("scroll",loadMore);
				}
			});
		}
	}
}
