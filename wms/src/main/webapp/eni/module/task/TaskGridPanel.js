/**
 * TaskGridWidget
 */
com.felix.eni.module.task.TaskGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.getDataStore().setConfig( { url : com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL,
							root:'tasks',
							idProperty:'id',
							fields:[{name :'id'}, 
									{name :'messageId'}, 
									{name :'assigner' }, 
									{name :'assignee'}, 
									{name :'status'}, 
									{name :'createTime'}, 
									{name :'messageNum'}, 
									{name :'messageType'}, 
									{name :'caseInternalCode'}, 
									{name :'caseId'}, 
									{name :'messageTag'}, 
									{name :'messageCreateBy'}, 
									{name :'createBy'} ,
									{name :'messageName'},
									{name :'sender'},
									{name :'receiver'},
									{name :'templateType'},
									{name :'paymentMessageId'},
									{name :'noticeTimes'},
									{name :'simulateMessage'}],
							baseParams: { cmd:'task', action:'find'}
							} );
	
	var renderer1 = function(value) {
  		var rendererJson={'TMP':TXT.message_type_tmp,
  						  'MT':TXT.message_type_mt,
  				          'MX':TXT.message_type_mx,
  				          'RPL':TXT.message_type_rpl};
        return rendererJson[value];
    };
	
	var renderer2 = function(value, metaData, record) {
	   	var showStatus='';
	   	if(record.data['templateType']){
	   		showStatus = TXT.task_type_refund;
	   	}
        if (value == "W") {
          showStatus = showStatus + TXT.task_status_wait;
        } else if (value == "P") {
           showStatus = showStatus +  TXT.task_status_wait_accout;
        } else if (value == "F") {
           showStatus = showStatus + TXT.task_status_accout_fail;
        } else {
           showStatus = showStatus + TXT.task_status_modify;
        }
        return showStatus;
    };
    
	this.setColumnModelConfig([{ header :TXT.task_messageType,       dataIndex :'messageType',     menuDisabled: true, width :80, renderer : renderer1}, 
		            { header :TXT.message_name,           dataIndex :'messageName',     menuDisabled: true, width :80},  
		            { header :TXT.message_assigner,       dataIndex :'sender',          menuDisabled: true, width :120},  
		            { header :TXT.message_assignee,       dataIndex :'receiver',        menuDisabled: true, width :120},
					{ header :TXT.task_messageId,         dataIndex :'messageNum',      menuDisabled: true, width :150},  
					{ header :TXT.case_internal_code,     dataIndex :'caseInternalCode',menuDisabled: true, width :150}, 
					{ header :TXT.task_message_create_by, dataIndex :'messageCreateBy', menuDisabled: true, width :80}, 
					{ header :TXT.task_createTime,        dataIndex :'createTime',      menuDisabled: true, width :130}, 
					{ header :TXT.task_status,            dataIndex :'status',          menuDisabled: true, width :100, renderer : renderer2}
				   ]);
	
	this.setGridConfig({title:   TXT.task,
						id:      'taskGridId',
						region:  'center',
						loadMask:true,
						margins: '0 0 4 0',
						cmargins:'0 0 4 0'
						});
	this.setSelectModel(0);
	this.setGridEventConfig({dblclick: showTaskMessageWin })	
}