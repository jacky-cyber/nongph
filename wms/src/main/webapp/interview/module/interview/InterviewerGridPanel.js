com.felix.interview.module.interview.InterviewerGridPanel = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	var thoj = this;
	
    var storeLoaded = false;
    var store = new Ext.data.JsonStore( {
                                        autoDestroy: false,
                                        url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/formTemplateAction/getAll",
                                        fields: ['id', 'name']
                                    } );
    store.addListener('load', function(){ 
                                   storeLoaded = true;
                              } );
    store.load();
  
	var isInterviewerExist = function( userId ){
            if( thoj.getDataCount()===0 )
                return false;

            var recordIndex = thoj.getDataStore().getExtStore().find("id", userId);
            if( recordIndex>=0 ) {
                var record = thoj.getDataStore().getExtStore().getAt(recordIndex);
                alert( record.get("name") + TXT.interview_interview_exam_exist);
                    return true;
            } else {
                    return false;
            }
	};
	
    this.isReady = function(){
      return storeLoaded;  
    }; 
    
	this.getDataStore().setConfig({
		fields: ['id', 'name', 'formTemplate']
	});
	
	this.setColumnModelConfig( [{ header :TXT.interview_interview_interviewer, dataIndex :'name',          menuDisabled :true,	width :150,  align :'left'},
	                	        { header :TXT.interview_interview_template,    dataIndex :'formTemplate',  menuDisabled :true,	width :200,   align :'left',
	                			  editor : new Ext.form.ComboBox( { allowBank:false,
                                                                    typeAhead: true,
                                                                    triggerAction: 'all',
                                                                    editable: false,
                                                                    lazyRender:true,
                                                                    mode: 'local',
                                                                    store: store,
                                                                    valueField: 'id',
                                                                    displayField: 'name'
                                                                  } ),
								  renderer: function(value){
                                                if( value ) { 
                                                    return store.getById(value).get('name');
                                                } else {
                                                    return value;
                                                }
                                             } 
                                 } ] );
	
	this.setGridConfig({
		border :true,
		margins: '0 0 0 0',
		cmargins:'0 0 4 0',
		height:150,
		loadMask:true
	});
	
	this.setEditable( true );
	this.setPagination( false );
	this.setTopToolBar( new com.felix.interview.module.interview.InterviewerToolbar() );
	this.setSelectModel( 2 );
        
	this.addInterviewer = function( id, name, template ){
        if( !isInterviewerExist(id) ) {
            var store = this.getExtGridPanel().getStore();
            var Choice = store.recordType;
            var c = new Choice( {id:id, name:name, formTemplate: template?template:''} );
            store.insert( store.getCount(), c );
        }
	};

	this.setReadOnly = function(){
		this.getTopToolBar().disable();
	};
        
    this.setGridEventConfig( {'destroy': function(){
                                                   store.destroy();
                                          } 
                             } );
       
    this.isValid = function(){
         var store = this.getExtGridPanel().getStore();
         for(var i=0; i<store.getCount(); i++){
             var tmp = store.getAt(i).data['formTemplate'];
             if( tmp==undefined || tmp=='') {
                 com.felix.nsf.MessageBox.alert( "没有为面试官选择模板在" + this.getParent().getExtPanel().title );
                 return false;
             }
         }
         return true;
	};
};