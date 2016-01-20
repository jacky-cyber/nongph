com.felix.exam.module.examinationView.ExaminationViewWindow = function(examination, answer){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	if( answer ) {
		var an = {};
		for( var i=0; i<answer.length; i++ ) {
			var eqa = answer[i];
			an[eqa.examQuestion.id] = [eqa.answer, eqa.result];
		}
		answer = an;
	}
	
	var thoj = this;

	var form = new com.felix.exam.module.examinationView.ExaminationViewFormPanel(examination, an);
	
	var clickCloseBtn = function() {
		thoj.close();
	}
	
	this.setConfig( { title :TXT.exam_examinationView_win_title,
					  width :900,
					  height:700,
					  autoScroll :true,
					  border :true,
					  resizable :true,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  shadow: false,
					  closeAction:'close',
					  buttonAlign :'right'
					} );
	
	this.setItems( [form] );
	
	this.setButtons( [ { text :TXT.common_btnClose, handler : clickCloseBtn } ] );
	
	this.setCloseConfirm( false );
}