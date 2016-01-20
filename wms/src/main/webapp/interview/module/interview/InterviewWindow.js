com.felix.interview.module.interview.InterviewWindow = function() {
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var form = new com.felix.interview.module.interview.InterviewFormPanel();

	var clickOkBtn = function() {
            var items = thoj.getItems();
            if( items.length===1 ) {
                com.felix.nsf.MessageBox.alert(TXT.interview_interview_no_round);
                return false;
            } 

            for(var i=0; i<items.length; i++){
                if( !items[i].isValid() ){
                    return false;
                }
            }
           
            var rounds = [];
            for(var i=1; i<items.length; i++){
                rounds.push( items[i].toJSON() );
            }
            var reqParam = form.getValues();
            reqParam.rounds =  Ext.encode( rounds );

            com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'save',function(result){
                    if( result.state==='SUCCESS' ) {
                        thoj.setCloseConfirm( false );
                        thoj.close();
                        thoj.getParent().getParent().reload();
                    } else {
                            var message = TXT['interview_interview_error_'+result.errorDesc.code];
                            if( !message )
                                    message = result.errorDesc.desc;
                            com.felix.nsf.MessageBox.alert( message );
                    }
            }, reqParam);
	};
	
	var clickCloseBtn = function() {
		thoj.close();
	};
	
	this.setConfig( { title :TXT.interview_interview_new,
					  width :450,
					  height:600,
					  autoScroll :true,
					  border :false,
					  resizable :true,
					  modal:true,
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
	
	this.setButtons( [ { text :TXT.common_btnOK, handler : clickOkBtn }, 
					   { text :TXT.common_btnClose, handler : clickCloseBtn } 
					 ] );
	
	this.edit = function( interviewId ) {
		this.getConfig().title = TXT.interview_interview_edit;
		
		com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", "get", function(interview){
			form.getItems()[0].value = interview.id;
			form.getItems()[3].value = interview.candidate.name;
			form.getItems()[4].value = interview.applyPosition.name;
			form.getItems()[5].value = interview.loginName;
			form.getItems()[6].value = interview.loginPassword;
			form.getItems().push( {id :'status',
								   name :'status',
								   xtype :'combo',
								   fieldLabel :TXT.common_status,
								   width :300,
								   tabIndex:7,
								   mode: 'local',
								   store: new Ext.data.ArrayStore( {id: 0,
															        fields: ['value', 'label'],
															        data: [ ['PENDING', '等待面试'], 
															                ['GOING',   '正在面试'],
															                ['PASS',    '面试通过'],
                                                                            ['REJECT',  '面试失败'],
															                ['ABSENT',  '缺席面试']
															                ]
															       } ),
								    valueField: 'value',
								    displayField: 'label',
								    triggerAction:'all',
								    editable:false,
								    value:interview.status,
                                    readOnly:true
				                  } );
            for(var i=0; i<interview.rounds.length; i++){
                var round = interview.rounds[i];
                var roundPanel;
                if( round.type==='EXAM' ) {
                    roundPanel = new com.felix.interview.module.interview.ExamRoundPanel();
                    roundPanel.setTitle( round.name +' - ' + TXT.interview_interview_round_type_exam);
                } else {
                    roundPanel = new com.felix.interview.module.interview.ViewRoundPanel();
                    roundPanel.setTitle( round.name +' - ' + TXT.interview_interview_round_type_view);
                }

                this.addItem( roundPanel );
            }
                
			this.getExtWindow().show( this.getParent().getExtToolBar(), function(){
                for(var i=0; i<interview.rounds.length; i++){
                    var round = interview.rounds[i];
                    this.getItem(i+1).init( round );
                }
            }, this );
		}, {id: interviewId}, this );
	};
};