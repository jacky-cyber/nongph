(function($, BLCAdmin) {
    
    var adminFormats = {
        blcDateFormat : "yy.mm.dd",
        blcTimeFormat : "HH:mm:ss",
        displayDateFormat : 'mm/dd/yy',
        displayTimeFormat : 'HH:mm:ss',
        displayDateDelimiter : '/'
    };

    // Add utility functions for dates to the BLCAdmin object
    BLCAdmin.dates = {
        /**
         * This function should be called for any element that wants to be a rulebuilder
         */
        initialize : function($element) {
            // Set the value of this datepicker to be the appropriately formatted one
            $element.val($element.val().indexOf(adminFormats.displayDateDelimiter)>=0?this.getDisplayDate(this.getServerDate($element.val())):this.getDisplayDate($element.val()));
            // Make it a date-time picker
            $element.datetimepicker({
                showSecond: true,
                timeFormat: 'HH:mm:ss'
            });
        },
        
        /**
         * serverDate should be in the Broadleaf datetime format, "yyyy.MM.dd HH:mm:ss" (Java spec)
         * returns the display format, "mm/dd/yy HH:mm:ss" (JavaScript spec)
         */
        getDisplayDate : function(serverDate) {
            var display = BLC.dates.getDisplayDate(serverDate, adminFormats);
            return display == null ? null : display.displayDate + " " + display.displayTime;
        },
        
        /**
         * displayDate should be in the format "mm/dd/yy HH:mm" (JavaScript spec)
         * returns the server-expected format, "yyyy.MM.dd HH:mm:ss Z" (Java spec)
         */
        getServerDate : function(displayDate) {
            var server = BLC.dates.getServerDate(displayDate, adminFormats);
            return server == null ? null : server.serverDate + " " + server.serverTime;
        },
        
        initializationHandler : function($container) {
            $container.find('.datepicker').each(function(index, element) {
                BLCAdmin.dates.initialize($(element));
            });
        },
        
        postValidationSubmitHandler : function($form) {
            $form.find('.datepicker').each(function(index, element) {
                var $this = $(this);
                if ($this.closest('.entityFormTab').data('initialized') != 'true') {
                    BLCAdmin.dates.initialize($this);
                }

                var name = $this.attr('name');

                var $hiddenClone = $('<input>', {
                    type: 'hidden',
                    name: name,
                    value: BLCAdmin.dates.getServerDate($this.val()),
                    'class': 'datepicker-clone'
                });
              
                $(this).data('previous-name', name).removeAttr('name').after($hiddenClone);
            });
        }
    };
    
    BLCAdmin.addInitializationHandler(BLCAdmin.dates.initializationHandler);
    BLCAdmin.addPostValidationSubmitHandler(BLCAdmin.dates.postValidationSubmitHandler);
            
})(jQuery, BLCAdmin);

$(document).ready(function() {
  
    $('body').on('click', 'div.datepicker-container', function(event) {
        $(this).find('input').datepicker('show');
    });
    
});
