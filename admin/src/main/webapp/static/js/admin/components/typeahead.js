(function($, BLCAdmin) {
    
    var typeaheadMatches = {};
    
    BLCAdmin.typeahead = {
            
        setToOneSelectionId : function setToOneSelectionId($input) {
            var $fieldBox = $input.closest('.field-box');
            var $extLink = $fieldBox.find('.external-link-container a');
            var val = $input.val();
            var valId = null;

            if (val != null && val != "") {
                var matches = typeaheadMatches[$fieldBox.attr('id')];
    
                if (typeof matches != "undefined") {
                    for (var i = 0; i < matches.length; i++) {
                        if (matches[i].displayKey.toUpperCase() == val.toUpperCase()) {
                            valId = matches[i].id;
                        }
                    }
                }
            }
            
            $fieldBox.find('input[type="hidden"]').val(valId);
            $extLink.attr('href', $extLink.data('foreign-key-link') + '/' + valId);

            return true;
        },

        initializeForToOne : function initializeForToOne($input) {
            var bh = new Bloodhound({
                datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.value); },
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                limit: 10,
                remote: {
                    url: $input.data('typeahead-url') + '&query=%QUERY',
                    filter: function(response) {
                        // Store the response locally so we can leverage it later
                        var fieldName = $input.closest('.field-box').attr('id');
                        typeaheadMatches[fieldName] = response;

                        return response;
                    }
                }
            });
               
            bh.initialize();

            $input.typeahead(null, {
                autoselect: false,
                displayKey: 'displayKey',
                source: bh.ttAdapter()
            });
            
            $input.on('blur', function() {
                BLCAdmin.typeahead.setToOneSelectionId($input);
            });
        },
        
    };
    
    BLCAdmin.addInitializationHandler(function($container) {
        $container.find('.to-one-typeahead').each(function(index, element) {
            BLCAdmin.typeahead.initializeForToOne($(element));
        });
    });
    
})(jQuery, BLCAdmin);