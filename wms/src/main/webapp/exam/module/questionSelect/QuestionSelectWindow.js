com.felix.exam.module.questionSelect.QuestionSelectWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	var form = new com.felix.exam.module.questionSelect.QuestionSelectFormPanel();
	var grid = new com.felix.exam.module.questionSelect.QuestionSelectGridPanel(); 
	
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
	
	this.setConfig( { title :TXT.exam_question_select_title,
					  width :700,
					  height :700,
					  autoScroll :true,
					  layout  :'border',
					  border :false,
					  resizable :true,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  shadow:false,
					  closeAction:'close',
					  buttonAlign :'center'
					} );
	
	this.setItems( [form, grid] );
	
	var clickCloseBtn = function(){
		thoj.close();
	};
	
	this.setCloseConfirm(false);
	
	this.setButtons( [ { text :TXT.common_btnClose,handler : clickCloseBtn } ] );
}