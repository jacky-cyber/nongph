com.felix.exam.module.questionView.QuestionViewWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	var form = new com.felix.exam.module.questionView.QuestionViewFormPanel();
	
	var clickResetBtn = function() {
		form.reset();
	}
	
	this.setConfig( { title :TXT.questionView_win_title,
					  width :650,
					  height:600,
					  autoScroll :false,
					  border :false,
					  resizable :false,
					  minimizable :false,
					  maximizable :false,
					  shadow: false,
					  closeAction:'close',
					  layoutConfig : { animate :false },
					  buttonAlign :'center'
					} );
	
	this.setItems( [form] );
	
	var clickCloseBtn = function(){
		thoj.close();
	};
	
	this.setButtons( [ { text :TXT.common_btnClose,handler : clickCloseBtn } ] );
	
	this.show = function( id ){
		com.felix.nsf.Ajax.requestWithMessageBox("questionViewAction", "getQuestionInfo", function(result){
			if( result.choiceItems.length>0 ) {
				form.getItems().push( {
									 	id :'choiceItems',
										name :'choiceItems',
										xtype:'textarea',
										fieldLabel: TXT.exam_question_choice,
										width :500,
										height:180,
										tabIndex:5
									 } );
			}
			thoj.super.show();
			form.setValues( result );
			form.setReadOnly();
		}, {questionId:id} );
	}
	this.setCloseConfirm( false );
}