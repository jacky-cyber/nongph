(function($, BLCAdmin) {
    
    BLCAdmin.translations = {
        getProperties : function($container) {
            return {
                ceilingEntity : $container.find('.translation-ceiling').text(),
                entityId      : $container.find('.translation-id').text(),
                propertyName  : $container.find('.translation-property').text(),
                isRte         : $container.find('.translation-rte').text()
            };
        }
    };
    
})(jQuery, BLCAdmin);

$(document).ready(function() {
    
    $('body').on('click', 'a.show-translations', function() {
        if ($(this).data('disabled') != 'disabled') {
            BLCAdmin.showLinkAsModal($(this).attr('href'));
        }
    	return false;
    });
    
    $('body').on('click', 'button.translation-submit-button', function() {
	    var $form = $(this).closest('.modal').find('.modal-body form');
		
		BLC.ajax({
			url: $form.attr('action'),
			type: "POST",
			data: $form.serialize()
		}, function(data) {
            BLCAdmin.listGrid.replaceRelatedListGrid($(data));
            BLCAdmin.hideCurrentModal();
	    });
		
		return false;
    });
    
    $('body').on('click', 'button.translation-grid-add', function() {
        var $container = $(this).closest('.listgrid-container');
        var baseUrl = $container.find('.listgrid-header-wrapper table').data('currenturl');
        var properties = BLCAdmin.translations.getProperties($container);
        
        BLCAdmin.showLinkAsModal(baseUrl + '/add?' + $.param(properties));
        return false;
    });
    
    $('body').on('click', 'button.translation-grid-update', function() {
        var $container = $(this).closest('.listgrid-container');
        var $selectedRows = $container.find('table tr.selected');
        var baseUrl = $container.find('.listgrid-header-wrapper table').data('currenturl');
        var rowFields = BLCAdmin.listGrid.getRowFields($selectedRows);
        var properties = BLCAdmin.translations.getProperties($container);
        
        properties.localeCode = rowFields.localeCode;
        properties.translationId = rowFields.id;
        
        BLCAdmin.showLinkAsModal(baseUrl + '/update?' + $.param(properties));
        return false;
    });
    
    $('body').on('click', 'button.translation-grid-remove', function() {
        var $container = $(this).closest('.listgrid-container');
        var $selectedRows = $container.find('table tr.selected');
        var baseUrl = $container.find('.listgrid-header-wrapper table').data('currenturl');
        var rowFields = BLCAdmin.listGrid.getRowFields($selectedRows);
        var properties = BLCAdmin.translations.getProperties($container);
        
        properties.translationId = rowFields.id;
        
        BLC.ajax({
            url: baseUrl + '/delete',
            data: properties,
            type: "POST"
        }, function(data) {
            BLCAdmin.listGrid.replaceRelatedListGrid($(data));
        });
        
        return false;
    });

});
