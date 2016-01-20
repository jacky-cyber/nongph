com.felix.eni.module.task.TaskNewMessageGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.eni.module.message.MessageGridPanel );
	
	this.getGridConfig().title = TXT.task_new_message;
	this.getGridConfig().border  = false;
	
	this.setColumnModelConfig([
		       { header :'', dataIndex :'io',  width :25, menuDisabled: true, renderer : function(value) {
																	                        if (value == 'I')
																	                            return "<img src='../images/in.png' align='absmiddle'/>";
																	                        else
																	                            return "<img src='../images/out.png' align='absmiddle'/>";
																	                    } },
               { header :'', dataIndex :'type', width :30, menuDisabled: true, renderer : function(value) {
																	                         if (value == 'MT')
																	                            return "fin";
																	                         else if (value == 'MX')
																	                            return 'xml';
																	                         else
																	                            return value;
																	                      } },
               { header :TXT.case_num, dataIndex :'cId',  menuDisabled: true,  hidden :true }, 
               { header :TXT.message_type, dataIndex :'messageType', width :150, menuDisabled: true, renderer : function(value, cellmeta, record, rowIndex, columnIndex, store) {
											                        function getValue(value) {
												                		var rendererJson={
												                				'008 RTCP':TXT.eni_008,
												                				'007 RTMP':TXT.eni_007,
												                				'026 UTA':TXT.eni_026,
												                				'027 CNR':TXT.eni_027,
												                				'028 API':TXT.eni_028,
												                				'029 ROI':TXT.eni_029,
												                				'030 NOCA':TXT.eni_030,
												                				'031 RCA':TXT.eni_031,
												                				'032 CCA':TXT.eni_032,
												                				'033 RFD':TXT.eni_033,
												                				'034 D':TXT.eni_034,
												                				'035 PFI':TXT.eni_035,
												                				'036 DAResp':TXT.eni_036,
												                				'037 DAReq':TXT.eni_037,
												                				'038 CSRR':TXT.eni_038,
												                				'039 CSR':TXT.eni_039};
												                		var result= rendererJson[value];
												                		return result==null?value:result;
											                		}
											                	
											                       if (record.data["careFlag"]) {
																			return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:green;'>"
																					+ getValue(value) + "</span>";
																	}else if (!record.data["isRead"]) {
											                            return "<img src='../images/new.png' align='absmiddle'/> <span style='font-weight:bold;'>"
											                                    + getValue(value) + "</span>";
											                        } else
											                            return getValue(value);
																} },
				{ header :TXT.message_assigner, dataIndex :'sendBic', menuDisabled: true, width :130 },
                { header :TXT.message_assignee, dataIndex :'receiverBic', menuDisabled: true, width :130 }, 
                { header :TXT.message_type, dataIndex :'messageTypeTag', menuDisabled: true, hidden :true }, 
                { header :TXT.message_num, dataIndex :'messageId', width :200, menuDisabled: true },
                { header :TXT.message_create_time, dataIndex :'createTime', menuDisabled: true, width :180 },
                { header :TXT.message_careFlag,	dataIndex :'careFlag', menuDisabled :true, width :50, renderer : function(value) {
																														if (value)
																															return TXT.common_yes_desc;
																														else
																															return TXT.common_no_desc;
																													} }, 
				{ header :TXT.common_last_careBy, dataIndex :'careBy', menuDisabled: true, width :180 },
                { header :TXT.common_last_careTime, dataIndex :'careTime', menuDisabled: true, width :180}
                ]);
	
	this.setGridEventConfig( { dblclick: showReceiveMsgWin } );
	this.getDataStore().setEventConfig(	{ load: function(){
													setTitle( this.getTotalCount() );
												}
							});
	
	var thob = this;
	var setTitle = function(totalCount){
		thob.getExtGridPanel().setTitle( TXT.task_new_message + '('+ totalCount + ')' );
	}
	
	var showReceiveMsgWin = function(e) {
		var record = thob.getSelectedRecord();
		if( !record )
			return;
		if( record['type']=='MT' ) {	
			var	careTxt = TXT.message_care;
			if( record['careFlag'] )
				careTxt = TXT.message_notCare;
			var win = Ecp.MessageFinContentWin.createMsgWinInReceiveProc( [thob], careTxt );
			
			com.felix.nsf.Util.Ajax.request({cmd:'message',action:'getFinMessage',messageId:record.id}, function(result) {
				win.show();
				win.window.getItem(0).setValues(result);
				delete result['result'];
				result['type']=record['type'];
				result['selectCaseType']='baseParamsMt';
				result['mid']=record.id;
				win.dataBus=result;
			});
		} else {
			var msgConfig={
					id:record['id'],
					tag:record['messageTypeTag'],
					editMode:'readOnly'
			};
			var	careTxt = TXT.message_care;
			if(record['careFlag']){
				careTxt = TXT.message_notCare;
			}
			var messageViewWindow = Ecp.MessageViewWindow.createSingleWindow(xmlWinObj,msgConfig,function(obj){
				obj.config.id='xmlMsgReceiverWin';
				obj.buttons=[{ text:careTxt,                  handler:setCareForTaskWin,       scope:obj },
							 { text:TXT.common_search,        handler:showCaseSelectWinAction, scope:obj },
							 { text:TXT.case_search_case,     handler:showPaymentSearchWin,    scope:obj },
							 { text:TXT.message_setUnrelated, handler:setMsgUnrelated,  	   scope:obj },
							 { text:TXT.message_print,  	  handler:printMsg, 			   scope:obj },
							 { text:TXT.common_btnClose, 	  handler:function(){ this['ecpOwner'].window.hide(); } } ];
			},[Ecp.components.taskMsgGrid]);
	
			messageViewWindow['dataBus']={
				mid:record.id,
				referenceNum:record['relatedReferenceNum'],
				sendBic:record['sendBic'],
				receiverBic:record['receiverBic'],
				type:record['type'],
				selectCaseType:'baseParamsMx',
				messageTypeCheck:record['messageType'],
				sender:record['sendBic'],
				receiver:record['receiverBic']
			};
			messageViewWindow.show();
		}
	}
}