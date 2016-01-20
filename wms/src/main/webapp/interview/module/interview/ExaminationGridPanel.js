com.felix.interview.module.interview.ExaminationGridPanel = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	var thoj = this;
	
	var isExaminationExist = function( examId ){
		if( thoj.getDataCount()==0 )
			return false;
			
		var recordIndex = thoj.getDataStore().getExtStore().find("id", examId);
		if( recordIndex>=0 ) {
			var record = thoj.getDataStore().getExtStore().getAt(recordIndex);
			alert( record.get("examinationName") + TXT.interview_interview_exam_exist);
			return true;
		} else {
			return false;
		}
	}
	
	this.getDataStore().setConfig({
            fields: ['id', 'examinationName', 'positionName', ' confuse']
	});
	
	this.setColumnModelConfig( [{ header :TXT.interview_interview_examinationName, dataIndex :'examinationName',menuDisabled :true,	width :150,  align :'left'},
	                            { header :TXT.interview_interview_position,        dataIndex :'positionName',   menuDisabled :true,	width :150,  align :'left'},
	                			{ header :TXT.interview_interview_confuse,         dataIndex :'confuse',        menuDisabled :true,	width :70,	 align :'left',
	                			  editor : new Ext.form.ComboBox( {
								                				    typeAhead: true,
								                				    triggerAction: 'all',
								                				    lazyRender:true,
								                				    mode: 'local',
								                				    store: new Ext.data.ArrayStore({
								                				        id: 0,
								                				        fields: ['id', 'label'],
								                				        data: [ [0, 'NO'], [1, 'YES'] ]
								                				    }),
								                				    valueField: 'id',
								                				    displayField: 'label'
								                				  } ),
								  renderer:function(value){
									  return value==0?'NO':'YES';
								  }} ] );
	
	this.setGridConfig({
		border :true,
		margins: '0 0 0 0',
		cmargins:'0 0 4 0',
		height:150,
		loadMask:true
	});
	
	this.setEditable( true );
	this.setPagination( false );
	this.setTopToolBar( new com.felix.interview.module.interview.ExaminationToolbar() );
	this.setSelectModel( 2 );
	
	this.addExamination = function( id, name, positioName, confuse ){
		if( !isExaminationExist(id) ) {
			var store = this.getExtGridPanel().getStore();
			var Choice = store.recordType;
			var c = new Choice( {id:id, examinationName:name, positionName:positioName, confuse: confuse?confuse:0} );
			store.insert( store.getCount(), c );
		}
	}
	
	var viewExamination = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("examinationAction", "get4View", function(exam){
				var win = new com.felix.exam.module.examinationView.ExaminationViewWindow(exam);
				win.show();
			}, {id: recordData.id} );
		}
	};

	this.setGridEventConfig( {rowdblclick:viewExamination} );
	
	this.setReadOnly = function(){
		this.getTopToolBar().disable();
		
	}
}