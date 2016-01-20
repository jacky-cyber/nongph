com.felix.interview.module.interviewer.InterviewInfoWindow = function( iv ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var form = new com.felix.interview.module.interviewer.InterviewFormPanel( iv );
	
	var clickCloseBtn = function() {
		thoj.close();
	};
	
	this.setConfig( {title :TXT.interview_interviewer_interview_info,
					     width :600,
						  height:600,
						  autoScroll :true,
						  border :false,
						  resizable :true,
						  modal:false,
						  minimizable :false,
						  maximizable :true,
						  shadow: false,
						  closeAction:'close',
						  buttonAlign :'right',
						  layout: { type:'vbox',
	                                padding:'5, 20, 5, 5',
	                                align:'stretch'
	                                }
						 } );
	
	this.setItems( [form] );
	
	this.setButtons( [ { text :TXT.common_btnClose, handler : clickCloseBtn } 
					 ] );
	
	this.setCloseConfirm( false );
	
	for(var i=0; i<iv.rounds.length; i++){
		  var roundPanel;
		  var round = iv.rounds[i];
        if( round.state=='PASS' || round.state=='REJECT' ) {
        		if( round.type==='EXAM' ) {
                roundPanel = new com.felix.interview.module.interviewer.ExamGridPanel( round );
            } else {
                roundPanel = new com.felix.interview.module.interviewer.ViewGridPanel( round );
            	}
        		this.addItem( roundPanel );
           }
    }
}