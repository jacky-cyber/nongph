com.felix.exam.module.examinationSelect.ExaminationSelectWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	var form = new com.felix.exam.module.examinationSelect.ExaminationSelectFormPanel();
	var grid = new com.felix.exam.module.examinationSelect.ExaminationSelectGridPanel(); 
	
	var clickQueryBtn = function() {
		if ( form.isValid() ) {
			var values = form.getValues();			
			thoj.getParent().getParent().search(values); 
			thoj.hide();
		}
	}
	
	var clickResetBtn = function() {
		form.reset();
	}
	
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
}