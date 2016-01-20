com.felix.interview.module.candidate.CandidateViewWindow = function( candidate ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	var cdt = candidate;
	
	var form = new com.felix.interview.module.candidate.CandidateFormPanel(candidate);
	
	
	var viewResume = function(){
		window.location.href = com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+'/candidateAction/downloadResume?id='+cdt.id;
	}
	
	var clickCloseBtn = function() {
		thoj.close();
	}
	
	this.setConfig( { title :TXT.interview_candidate_win_title_view,
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
	
	this.setButtons( [ { text :TXT.interview_candidate_view_resume, handler : clickCloseBtn, disable:candidate.resumeName?true:false },
	                   { text :TXT.common_btnClose, handler : clickCloseBtn } ] );
	
	this.setCloseConfirm( false );
	
	this.setListeners( {'show':function(){
		form.setReadOnly();
	}} );
}