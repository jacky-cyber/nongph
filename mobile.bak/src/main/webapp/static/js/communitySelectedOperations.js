$(function(){
    $('body').on('change', '#firstLevelCommunity', function() {
    	$('#secondLevelCommunity').empty();
    	$('#netNode').empty();
    	$('#secondLevelCommunity').append($("<option>").val('').text('--'));
    	$('#netNode').append($("<option>").val('').text('--'));
    	
    	var value = $(this).children('option:selected').val();
    	if(value=='') return;
    	var requestUrl = '/account/addresses/communities/' + value;
        BLC.ajax( { url:requestUrl, 
            type: "POST",
            dataType: "json"
         }, function(data, extraData) {
        	 	var communities = data.secondLevelCommunities
        	 	for(i=0;i<communities.length;i++){
        	 		$("#secondLevelCommunity").append($("<option>").val(communities[i].id).text(communities[i].communityName)); 
        	 	}
            }
       );
   });
    
    $('body').on('change', '#secondLevelCommunity', function() {
    	$('#netNode').empty();
    	$('#netNode').append($("<option>").val('').text('--'));
    	
    	var value = $(this).children('option:selected').val();
    	if(value=='') return;
    	var requestUrl = '/account/addresses/netNodes/'+value;
        BLC.ajax( { url:requestUrl, 
            type: "POST",
            dataType: "json"
         }, function(data, extraData) {
        	 	var netNodes = data.netNodes;
        	 	for(i=0;i<netNodes.length;i++){
        	 		$("#netNode").append($("<option>").val(netNodes[i].id).text(netNodes[i].netNodeName)); 
        	 	}
            }
       );
   });
    
});