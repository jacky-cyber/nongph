com.felix.interview.module.candidate.CandidateSearchWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	var form = new com.felix.interview.module.candidate.CandidateSearchFormPanel();
	
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
	
	this.setConfig( { title :TXT.interview_candidate_query,
					  width :500,
					  height :200,
					  autoScroll :false,
					  layout :'fit',
					  border :false,
					  resizable :false,
					  modal:true,
					  minimizable :false,
					  maximizable :false,
					  shadow:false,
					  closeAction:'hide',
					  layoutConfig : { animate :false },
					  buttonAlign :'center'
					} );
	
	this.setItems( [form] );
	
	this.setButtons( [ { text :TXT.common_btnOK, handler : clickQueryBtn }, 
					   { text :TXT.common_reset, handler : clickResetBtn } 
					 ] );
}