com.felix.interview.module.candidate.CandidateGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	var thoj = this;
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/candidateAction/search",
		root :'items',
		totalProperty :'totalCount',
		id :'id',
		fields : [ 'id', 
				   'name',
				   'idCode',
				   'phone',
				   'age' ]
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.common_human_name,        dataIndex :'name', 	menuDisabled :true,	width :200, align :'left' },
			{ header :TXT.interview_candidate_id,   dataIndex :'idCode',  menuDisabled :true,	width :200,	align :'left' },
			{ header :TXT.interview_candidate_phone,dataIndex :'phone', menuDisabled :true,	width :200,	align :'left' },
			{ header :TXT.interview_candidate_age,  dataIndex :'age',   menuDisabled :true,	width :200,	align :'left' }] );
					
	this.setGridConfig({
		title:TXT.interview_candidate,
		id:'interview_candidate_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );		
	
	var toolbar = new com.felix.interview.module.candidate.CandidateToolbar();
	
	this.setTopToolBar( toolbar );
		
	var viewCandidate = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecord(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("candidateAction", "get", function(candidate){
				var win = new com.felix.interview.module.candidate.CandidateViewWindow(candidate);
				win.show();
			}, {id: recordData.id} );
		}
	};
	
	this.setGridEventConfig( {rowdblclick:viewCandidate} );
}