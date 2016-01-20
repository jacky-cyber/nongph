com.felix.exam.module.positionSelect.SelectPositionWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	var form = new com.felix.exam.module.positionSelect.SelectPositionFormPanel();
	
	var clickNextBtn = function() {
		if ( form.isValid() ) {
			var positionId = form.getValues(false).positionId;
			thoj.close();
			thoj.getParent().onSelectPosition(positionId);
		}
	}
	
	var clickCanelBtn = function() {
		thoj.close();
	}
	
	this.setConfig( { title :TXT.exam_positionSelect_title,
					  width :350,
					  height:150,
					  autoScroll :true,
					  border :true,
					  resizable :true,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  shadow: false,
					  closeAction:'close',
					  buttonAlign :'center',
					  layout: 'fit'
					} );
	
	this.setItems( [form] );
	
	this.setButtons( [ { text :TXT.common_next, handler : clickNextBtn }, 
					   { text :TXT.common_cancel, handler : clickCanelBtn } 
					 ] );
	this.setCloseConfirm(false);
}