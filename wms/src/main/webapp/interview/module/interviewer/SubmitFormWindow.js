com.felix.interview.module.interviewer.SubmitFormWindow = function( iru ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var clickCloseBtn = function() {
		thoj.close();
	};
	
	var pass = function(){
		var form = thoj.getItem(0);
		var formFields = form.getForm().getValues(false);
		com.felix.nsf.Ajax.requestWithMessageBox("interviewerAction", "passInterviewRoundUser", function(result){
			 if( result.state==='SUCCESS' ) {
             this.setCloseConfirm( false );
             this.close();
          } else {
             var message = TXT['interview_interview_error_'+result.errorDesc.code];
             if( !message )
            	 message = result.errorDesc.desc;
             com.felix.nsf.MessageBox.alert( message );
             }
		}, {id: iru.id, formFields: Ext.encode( formFields ) }, thoj );
	};
	
	var reject = function(){
		var form = thoj.getItem(0);
		var formFields = form.getForm().getValues(false);
		com.felix.nsf.Ajax.requestWithMessageBox("interviewerAction", "rejectInterviewRoundUser", function(result){
			 if( result.state==='SUCCESS' ) {
             this.setCloseConfirm( false );
             this.close();
          } else {
             var message = TXT['interview_interview_error_'+result.errorDesc.code];
             if( !message )
            	 message = result.errorDesc.desc;
             com.felix.nsf.MessageBox.alert( message );
             }
		}, {id: iru.id, formFields: Ext.encode( formFields )}, thoj );
	};
	
	this.setConfig( { width :500,
							height :450,
							autoScroll :false,
							layout :'border',
							border :false,
							minimizable :false,
							maximizable :false,
							resizable :true,
							shadow:false,
							resizable :false,
							modal :false,
							layoutConfig : {
								animate :false
							},
							buttonAlign :'right'
						} );
	
	this.setButtons( [ { text :TXT.interview_interviewer_pass, handler : pass },
							 { text :TXT.interview_interviewer_reject, handler : reject },
	                   { text :TXT.common_btnClose, handler : clickCloseBtn } 
	 					   ] );
	
	this.show = function(){
		com.felix.nsf.Ajax.requestWithMessageBox("formTemplateAction", "get", function(ft){
			var form = com.felix.form.module.render.FormRenderer.render(ft);
			this.getConfig().title = ft.name;
			this.addItem( form );
			this.getExtWindow().show(this.getParent(), function(){
                                form.getForm().clearInvalid();
                            }); 
		}, {id: iru['form.formTemplate.id']}, this );
	}
}	