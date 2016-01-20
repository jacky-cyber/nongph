com.felix.interview.module.interviewView.ExamRoundPanel = function( ir ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
    
	var config = {
			collapsible:false,
			closable:false,
			border  :true,
			margins:'0 0 5 0',
			title: ir.name + ' - ' +  TXT.interview_interviewView_round_type_exam + ' - ' + ir.state
		};
	
	 var form = new com.felix.interview.module.interviewView.ExamFormPanel(ir);
	 var grid = new com.felix.interview.module.interviewView.ExamGridPanel(ir); 
	 grid.setParent( this );
	 var extPanel;
	    
	 this.getExtPanel = function(){
        if( !extPanel ) {
            config["items"] = this.getExtComponent( [form,grid] );
            extPanel = new Ext.Panel( config );
        }
        return extPanel;
	 };  
	 
     this.init = function(){
        var interviewRoundExaminations = ir.interviewRoundExaminations;
        for(var i=0; i<interviewRoundExaminations.length; i++){
            var ire = interviewRoundExaminations[i];
            grid.addExamination( ire.id, ire.exam.name, ire.exam.position.name, ire.examConfuse, ire.status, ire.startTime, ire.endTime );
        }
     };
};

