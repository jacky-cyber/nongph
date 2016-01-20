com.felix.interview.module.interviewView.ExamToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var markPending = function(){
		var data = thoj.getParent().getSelectedRecordData(true);
		if( data ) {
			thoj.getParent().getParent().getParent().markPending(data.id);
		}
	};
	
	var markGoing = function(){
		var data = thoj.getParent().getSelectedRecordData(true);
		if( data ) {
			thoj.getParent().getParent().getParent().markGoing(data.id);
		}
	};
	
	this.setToolBarConfig( {} );
   this.setToolBarItemsConfig( [ {id:'interview_interviewView_exam_tbar_mp', type:'button', text:TXT.interview_interviewView_mark_pending, handler:'markPending',iconCls:'icoAdd'},
                                 {id:'interview_interviewView_exam_tbar_mg', type:'button', text:TXT.interview_interviewView_mark_going,   handler:'markGoing',iconCls:'icoDel'}
   										  ] ); 	
	
	
	this.setToolBarEvent( { markPending: markPending,
									markGoing  : markGoing} );
}