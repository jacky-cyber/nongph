com.felix.std.module.userSelect.UserSelectWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
        var selectUserCallBack;
	var form = new com.felix.std.module.userSelect.UserSelectFormPanel();
	var grid = new com.felix.std.module.userSelect.UserSelectGridPanel(); 
	
	var clickQueryBtn = function() {
		if ( form.isValid() ) {
			var values = form.getValues();			
			thoj.getParent().getParent().search(values); 
			thoj.hide();
		}
	};
	
	var clickResetBtn = function() {
		form.reset();
	};
	
	this.setConfig( { title :TXT.exam_examination_select_title,
                          width :550,
                          height :500,
                          autoScroll :true,
                          layout  :'border',
                          border :false,
                          resizable :true,
                          modal:true,
                          minimizable :false,
                          maximizable :true,
                          shadow:false,
                          closeAction:'close',
                          buttonAlign :'right'
			  } );
	
	this.setItems( [form, grid] );
	
	var clickCloseBtn = function(){
		thoj.close();
	};
	
	this.setCloseConfirm(false);
	
	this.setButtons( [ { text :TXT.common_btnClose,handler : clickCloseBtn } ] );
        
        this.setSelectUserCallBack = function( callback ) {
            selectUserCallBack = callback;
        };
        
        this.getSelectUserCallBack = function(){
            return selectUserCallBack;
        };
}