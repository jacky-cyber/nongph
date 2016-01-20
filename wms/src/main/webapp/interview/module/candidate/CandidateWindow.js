com.felix.interview.module.candidate.CandidateWindow = function( candidate ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var form = new com.felix.interview.module.candidate.CandidateFormPanel(candidate);

	var clickOkBtn = function() {
		if( form.isValid() ){
			form.getExtFormPanel().getForm().submit({
                url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+'/candidateAction/saveInterviewer',
                waitTitle:TXT.common_hint,
                waitMsg: TXT.common_wait_access,
                failure: function(f, o){
                	var result = o.result;
                    if( result.state=='SUCCESS' ) {
    	        		thoj.setCloseConfirm( false );
    	     			thoj.close();
    	     			thoj.getParent().getParent().reload();
    				} else {
    					var message = TXT['interview_candidate_error_'+result.errorDesc.code];
    					if( !message )
    						message = result.errorDesc.desc;
    					com.felix.nsf.MessageBox.alert( message );
    				}
                }
            });
		}
	}
	
	var clickCloseBtn = function() {
		thoj.close();
	}
	
	this.setConfig( { title :candidate?TXT.interview_candidate_win_title_edit:TXT.interview_candidate_win_title_add,
					  width :500,
					  height:500,
					  autoScroll :true,
					  border :true,
					  resizable :true,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  shadow: false,
					  closeAction:'close',
					  buttonAlign :'right',
					  layout: 'fit'
					} );
	
	this.setItems( [form] );
	
	this.setButtons( [ { text :TXT.common_btnOK, handler : clickOkBtn }, 
					   { text :TXT.common_btnClose, handler : clickCloseBtn } 
					 ] );
}