com.felix.interview.module.interviewView.InterviewFormPanel = function( iv ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var thoj = this;
	var afterGetExamRoundName = function(btn, text){
        if( btn =='ok' ) {
            text = text.trim();
            if( text ) { 
               var round = new com.felix.interview.module.interview.ExamRoundPanel();
               round.setTitle(text +' - ' + round.getTitle());
               round.getForm().getButtons().push( { xtype: 'button',
														          text : TXT.commom_btnSave,
														          width: 100,
														          iconCls: 'icoOk',
														          scope: thoj,
														          handler: function(){
														        	  			     this.getParent().addRound(round);
															           			  }
				         											} );
               thoj.getParent().addItem( round );
               thoj.getParent().refresh();
            }
        }
    };    
        
    var afterGetViewRoundName = function(btn, text){
         if( btn =='ok' ) {
            text = text.trim();
            if( text ) { 
                var round = new com.felix.interview.module.interview.ViewRoundPanel();
                round.setTitle(text +' - ' + round.getTitle());
                round.getForm().getButtons().push( { xtype: 'button',
														           text : TXT.commom_btnSave,
														           width: 100,
														           iconCls: 'icoOk',
														           scope: thoj,
														           handler: function(){
														        	  			     this.getParent().addRound(round);
															           			  }
																	 } );
                thoj.getParent().addItem( round );
                thoj.getParent().refresh();
            }
        }
    };   
    
    this.setConfig( { labelAlign :'left',
                          layout:'form',
                          labelWidth :60,
                          margins:'0 0 5 0',
                          frame :true
		         } );

	this.setItems( [ { id:'id',
					   name: 'id',
					   xtype:"hidden",
					   value:iv.id
					 },      
					 { id:'candidateId',
					   name: 'candidateId',
					   xtype:"hidden"
					 },
					 { id:'positionId',
					   name: 'positionId',
					   xtype:"hidden",
					   value:iv.applyPosition.id
					 },
	                 { id :'candidateName',
					   name :'candidateName',
					   xtype :'displayfield',
					   fieldLabel :TXT.interview_interview_candidate,
					   width :300,
					   tabIndex:1,
					   value:iv.candidate.name,
					   readOnly: true
	                 },
	                 { id :'positionName',
					   name :'positionName',
					   xtype :'displayfield',
					   fieldLabel :TXT.interview_interview_position,
					   width :300,
					   tabIndex:2,
					   value:iv.applyPosition.name,
					   readOnly: true
	                 },
	                 { id :'loginName',
					   name :'loginName',
					   xtype :'textfield',
					   fieldLabel :TXT.interview_interview_loginName,
					   width :300,
					   tabIndex:3,
					   allowBlank:false,
					   value:iv.loginName,
					   readOnly: true
	                 },
	                 { id :'loginPassword',
					   name :'loginPassword',
					   xtype :'textfield',
					   fieldLabel :TXT.interview_interview_loginPassword,
					   width :300,
					   tabIndex:4,
					   allowBlank:false,
					   value:iv.loginPassword,
					   readOnly: true
	                 },
	                 {id :'status',
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
					    value:iv.status,
					    readOnly:true
	                 } ]
		            );
		
		if( iv.conversationId != '' ) {
	    	this.getButtons().push( { xtype: 'button',
									  text : TXT.interview_interviewView_clean_conversation,
									  width: 100,
									  iconCls: 'icoAdd',
									  scope: this,
									  handler:function(){
										  this.getParent().cleanConversation();
									  }
									} );
		}
		
		this.getButtons().push( { xtype: 'button',
					              text : TXT.interview_interview_round_add_exam,
					              width: 100,
					              iconCls: 'icoAdd',
					              scope: this,
					              handler: function(){
					                    Ext.MessageBox.prompt(TXT.interview_interview_round_add_exam, 
					                                          TXT.interview_interview_round_set_name, 
					                                          afterGetExamRoundName);
					                    
					              }
					            } );
		this.getButtons().push( { xtype: 'button',
					              text : TXT.interview_interview_round_add_view,
						          width: 100,
						          iconCls: 'icoAdd',
						          scope: this,
					              handler: function(){
					            	  Ext.MessageBox.prompt(TXT.interview_interview_round_add_view, 
					                                          TXT.interview_interview_round_set_name, 
					                                          afterGetViewRoundName);
					              } 
								} );
}