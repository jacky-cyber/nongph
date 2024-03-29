(function($, BLCAdmin) {
    
    // Add utility functions for offers to the BLCAdmin object
    BLCAdmin.offer = {
    
        addOnChangeTriggers : function($form) {
            $form.find('#field-type').on('change', function() {
                BLCAdmin.offer.initializeOfferTypeField($form);
            });
            
            $form.find('#field-deliveryType').on('change', function() {
                BLCAdmin.offer.initializeDeliveryTypeField($form);
            });
        },
        
        /**
         * Show / hide certain fields on the offer screen based on currently selected values
         */
        initializeOfferFormFields : function($form) {
            this.initializeOfferTypeField($form);
            this.initializeDeliveryTypeField($form);
        },
        
        initializeOfferTypeField : function($form) {
            var $offerType = $form.find('#field-type');
            var offerTypeValue;
            if ($offerType.find('select').length > 0) {
                offerTypeValue = $offerType.find('select').val();
            } else {
                offerTypeValue = $offerType.find('input[type="radio"]:checked').val();
            }
            
            var $fgCriteria = $form.find('#field-offerMatchRules---FULFILLMENT-GROUP');
            var $itemTarget = $form.find('#field-targetItemCriteria');
            var $itemTargetFieldset = $itemTarget.closest('fieldset');
            
            if (offerTypeValue == "ORDER") {
                $fgCriteria.addClass('hidden');
                $itemTarget.addClass('hidden');
                $itemTargetFieldset.addClass('hidden');
            } else if (offerTypeValue == 'ORDER_ITEM') {
                $fgCriteria.addClass('hidden');
                $itemTarget.removeClass('hidden');
                $itemTargetFieldset.removeClass('hidden');
            } else if (offerTypeValue == 'FULFILLMENT_GROUP') {
                $fgCriteria.removeClass('hidden');
                $itemTarget.addClass('hidden');
                $itemTargetFieldset.addClass('hidden');
            } else {
                $fgCriteria.addClass('hidden');
                $itemTarget.addClass('hidden');
                $itemTargetFieldset.addClass('hidden');
            }
        },
        
        initializeDeliveryTypeField : function($form) {
            var $deliveryType = $form.find('#field-deliveryType');
            var $offerCode = $form.find('#field-offerCode-offerCode');
            
            if ($deliveryType.find('select').val() == "CODE") {
                $offerCode.removeClass('hidden');
            } else {
                $offerCode.addClass('hidden');
            }
        }
        
    };

    BLCAdmin.addInitializationHandler(function($container) {
        var $form = $container.closest('form.offer-form');
        BLCAdmin.offer.addOnChangeTriggers($form);
        BLCAdmin.offer.initializeOfferFormFields($form);
    });
    
})(jQuery, BLCAdmin);
