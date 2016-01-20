/**
 * TaskHistoryGridWidget
 */
com.felix.eni.module.task.TaskHistoryGridPanel = function() {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.getDataStore().setConfig( { url:com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL,
							root:'taskTraces',
							idProperty:'id',
							fields:[ { name :'id'}, 
									 { name :'assigner' }, 
									 { name :'assignee' }, 
									 { name :'messageId'}, 
									 { name :'status'	}, 
									 { name :'action'	}, 
									 { name :'createTime'}, 
									 { name :'comments'	}, 
									 { name :'messageNum'}, 
									 { name :'messageType'}, 
									 { name :'createBy'	}, 
									 { name :'createByInst'} ],
							baseParams: { cmd:'task', action:'findTaskByMessage' }
							});
	var render1 = function(value) {
					if (value == 'A') {
						return TXT.task_action_approval;
					} else if (value == 'R') {
						return TXT.task_status_modify;
					} else if (value == 'N') {
						return TXT.case_createXmlMsgInCase;
					} else if (value == 'C')
						return TXT.task_action_cancel;
					else if (value == 'M')
						return TXT.task_detail_modify;
					else if (value == 'F')
						return TXT.task_detail_account_fail;
					else if (value == 'S')
						return TXT.task_detail_account_success;
					else if (value == 'P')
						return TXT.task_detail_account_P_run;
					}
	
	this.searchByMessageId = function(messageId){
		dataStore.store['baseParams'] = { 'cmd' :'task', 'action' :'findTaskByMessage',	'id' :messageId };
		dataStore.store.load();
	}
	
	this.setColumnModelConfig( [ { header: TXT.task_history_operation_time,dataIndex: 'createTime',  menuDisabled: true, width:150	}, 
								 { header: TXT.task_action,                dataIndex: 'action',      menuDisabled: true, width:100, renderer: render1}, 
								 { header :TXT.task_comments,		       dataIndex: 'comments',    menuDisabled: true, width :200}, 
								 { header :TXT.institution_name,   		   dataIndex: 'createByInst',menuDisabled: true, width :150}, 
								 { header :TXT.task_history_operation_by,  dataIndex: 'createBy',    menuDisabled: true, width :150} ] );
	this.setGridConfig( { id:'taskHistoryGridId',
						  loadMask:true,
						  region:'center'
						} );	
	this.setSelectModel( 0 );
}