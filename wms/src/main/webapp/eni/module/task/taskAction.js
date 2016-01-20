var showTaskMessageWin = function(btn, e) {
	var windowObj = {};
	if (typeof task_msg_window_config != 'undefined')
		windowObj['config'] = task_msg_window_config;
	if (typeof task_msg_approve_window_buttons != 'undefined')
		windowObj['approveButtons'] = task_msg_approve_window_buttons;
	if (typeof task_msg_modify_window_buttons != 'undefined')
		windowObj['modifyButtons'] = task_msg_modify_window_buttons;
	
	windowObj['items']=[{},{}];
	if (typeof task_msg_tabPanel_config != 'undefined')
		windowObj['items'][0]['config'] = task_msg_tabPanel_config;
	if (typeof task_msg_tabPanel_items != 'undefined')
		windowObj['items'][0]['items'] = task_msg_tabPanel_items;
	
	if (typeof task_comments_form_config != 'undefined')
		windowObj['items'][1]['config'] = task_comments_form_config;
	if (typeof task_comments_form_buttons != 'undefined')
		windowObj['items'][1]['buttons'] = task_comments_form_buttons;
	if (typeof task_comments_form_items != 'undefined')
		windowObj['items'][1]['items'] = task_comments_form_items;
	
	var record = Ecp.components.taskGrid.grid.getSelectedRecord();
	if(!record)
		return;
	if(record['messageType']=='MT')
	{
		var tag=record['messageTag'].substring(1);
		if(tag=='99')
		{		
			var buttonName = 2;
			if(record['status']=='W'){
				buttonName = 1;
			}else if(record['status']=='F'){
				buttonName = 3;
			}else if(record['status']=='P'){
				buttonName = 4;
			}
			if(record['templateType']&&record['status']=='M'
				&&!(record['noticeTimes']&&record['noticeTimes']>0)){
				showRefundMessageLayout(REFUND_ID,"template",record['messageId'],null,record['caseId']);
			}else{
				var params={cmd:'message',action:'getFinMessage',messageId:record['messageId'],needComment:true};
				if(record['status']!='W')
					params['flag']='yes';
				Ecp.Ajax.request(params, function(result) {
					result['comments']='';
					taskMessageViewWindow.show();
					taskMessageViewWindow.window.getItem(0).setValues(result);
					taskMessageViewWindow.window.getItem(1).setValues(result);
					taskMessageViewWindow.dataBus={
						type:'MT',
						mid:record['messageId']
					};
				});
				var taskMessageViewWindow=Ecp.MessageFinContentWin.createMsgWinInTaskProc({
					cusCommentForm:{},
					cusMsgContentForm:{},
					cusMsgContentWin:{},
					code:record['status']=buttonName
				},[Ecp.components.taskGrid,Ecp.components.taskCaseGrid],record);
				Ecp.components.taskGrid.reloadUpdate('reload');
			}
			delete Ecp.components['msgWinInTaskProc'];
		}else{
			var msgConfig={
				id:record['messageId'],
				tag:record['messageTag'],
				status:record['status'],
				fin:'1',
				messageType:'MT'+record['messageTag']
			};
			var win=Ecp.TaskMessageViewWindow.createWindow(windowObj,msgConfig,[Ecp.components.taskGrid]);
			Ecp.Ajax.request({cmd:'message',action:'getLastTaskFromXmlMsg',id:record['messageId']}, function(result) {
				win.getItem(1).setValues(result)
				win['ecpOwner'].show();
			});
		}
	}else{
		var msgConfig={
				id:record['messageId'],
				tag:record['messageTag'],
				status:record['status']
			};
		var win=Ecp.TaskMessageViewWindow.createWindow(windowObj,msgConfig,[Ecp.components.taskGrid]);
		Ecp.Ajax.request({cmd:'message',action:'getLastTaskFromXmlMsg',id:record['messageId']}, function(result) {
				win.getItem(1).setValues(result)
				win['ecpOwner'].show();
		});
	}
};

function showRefundMessageLayout(templateId, templateFlag, pid, templateWindow,
		caseId){
	var autoform;
	var autowin;
	var fieldNameList = new Array();
	var values = {};
	var typeList = new Array();
	var isOut = '';
	var duplicate = '';
	var templateName='';
	var needHQCheck;
	displayForm();
	function displayForm() {
		var uid = templateId;
		var prefix='';
		autoform = createForm();
		autowin = Ecp.RefundMessageWin.createWindow({
						"winFun":function (obj) {
							    Ecp.components.refundMessageWin = obj;
						}
					},autoform);		
			
		autowin['dataBus']={tempId:uid,tempFlag:templateFlag,pid:pid,cid:caseId,tempListWin:templateWindow};	
		function createOnSuccess(result) {
			var fields = result.fields;
			autowin['dataBus']['existTName'] = result.tName;
			isOut = result.isOut;
			duplicate = result.duplicate;
			type = result.type;
			autowin['dataBus']['templateName']=result.templateName;
			needHQCheck=result.needHQCheck;
			if(caseId!='')
				prefix=new Date().getTime();
			parserJson(fields, autoform, false, fieldNameList, typeList, prefix);// ,comboValue);
			autowin['dataBus']['fieldNameList']=fieldNameList;
			autowin['dataBus']['typeList']=typeList;
			autowin['dataBus']['values']=values;
			autowin['dataBus']['prefix']=prefix;
			autowin['dataBus']['duplicate']=duplicate;
			autowin.buttons[0].setHandler(function(){
				clickTaskModifyForRefundBtn(this,autowin);
			});
			autowin.show();
			var params={cmd:'message',action:'getFinMessage',messageId:pid,needComment:true};
				Ecp.Ajax.request(params, function(result) {
					var formTemplate = Ext.getCmp('reasonLable'+prefix).getValue();
					if(result['lastComments']!=null)
						Ecp.components.refundMessageWin.getItem(1).setValues(result);
			});
		}
		var params = 'cmd=messageTemplate&action=getTemplateLayoutForAudit&templateId='
					+ uid + '&pid=' + pid + '&caseId=' + caseId;
		Ecp.Ajax.request(params, createOnSuccess);
	}
}
var showTaskDetailWin = function(btn, e) {
	var windowObj = {};
	if (typeof task_history_window_config != 'undefined')
		windowObj['config'] = task_history_window_config;
	if (typeof task_history_window_buttons != 'undefined')
		windowObj['buttons'] = task_history_window_buttons;
	
	windowObj['items']=[{}];
	var cmtStoreObj={};
	typeof cmt_taskDetail_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_taskDetail_dsConfig;
	typeof cmt_taskDetail_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_taskDetail_dsEventConfig;
	windowObj['items'][0]['store'] = cmtStoreObj;
	
	var cmtGridObj={};
	typeof cmt_taskDetail_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_taskDetail_gridConfig;
	typeof cmt_taskDetail_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_taskDetail_cmConfig;
	typeof cmt_taskDetail_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_taskDetail_gridEventConfig;
	windowObj['items'][0]['grid'] = cmtGridObj;
	
	var win=Ecp.TaskHistoryWindow.createSingleWindow(windowObj);
	win.getItem(0)['ecpOwner'].searchByMessageId(Ecp.components.taskGrid.grid.getSelectedRecord()['messageId']);
	win.show();
}
var clickTaskApproveBtn = function(btn, e) {
	var params=btn['ecpOwner'].getItem(1).getValues();
	params['cmd']='task';
	params['action']='handle';
	params['id']=Ecp.components.taskGrid.grid.getSelectedId();
	params['transitionName']='A';
	if(this['dataBus']!=undefined&&this['dataBus']['type']=='MT'){
		Ecp.Ajax.request({cmd:'task',action:'checkHasPayment',messageId:this['dataBus']['mid']},function(result){
			var vMsg = TXT.task_is_approval;
			if (result.payment != ''){
				if(Ecp.userInfomation.isInstHQ==true)
					vMsg = TXT.task_is_approval_withNoPayment;
				else{
					if(result.needHqCheck!='')
						vMsg = TXT.task_is_approval_withNoPayment_toSuper;
					else
						vMsg = TXT.task_is_approval_withNoPayment;
				}
			}
			var record=Ecp.components.taskGrid.grid.getSelectedRecord();
			if(record['templateType']!=undefined && record['templateType']
				&&!(record['noticeTimes']&&record['noticeTimes']>0)
				&&record['simulateMessage']!=undefined && record['simulateMessage']){
				Ecp.Ajax.request({cmd:'task',action:'checkPaymentDealStatus',messageId:this['dataBus']['mid']},function(status){
					if(status.result=='success'){// it is refund 
						Ext.MessageBox.confirm(TXT.common_hint,TXT.task_is_approval_send_simulate, function(btnValue){
							if(btnValue=='yes'){
								params['isSendSimulate']='Y';
								approveTaskAjaxToCommand(params,btn.ecpOwner);
							}else{
								params['isSendSimulate']='N';
								Ecp.MessageBox.confirm(TXT.task_is_approval_send_message, function(){
									approveTaskAjaxToCommand(params,btn.ecpOwner);
								},this);
							}
						},this);
					}else{
						Ecp.MessageBox.alert(TXT.task_is_refund_dealstatus_change);
					}
				});
			}else{
				Ecp.MessageBox.confirm(vMsg,function(){
					approveTaskAjaxToCommand(params,this);
				},this);
			}
		},this);
	}else{
		Ecp.MessageBox.confirm(TXT.task_is_approval,function(){
			approveTaskAjaxToCommand(params,btn['ecpOwner']);
		});
	}
}
var approveTaskAjaxToCommand = function(params,win){
	Ecp.Ajax.request(params, function(result) {
	if (result.result == 'failure') {
			if(result.message=='senderBicError')
				Ecp.MessageBox.alert(TXT.message_xml_sender_error);
			else if(result.message=='receiverBicError')
				Ecp.MessageBox.alert(TXT.message_xml_receiver_error);
			else
				Ecp.MessageBox.alert(TXT.task_operate_error);
			return;
		}
		win.window.close();
		Ecp.components.taskGrid.reloadUpdate('reload');
	});
}
var clickTaskRejectBtn = function(btn, e) {
	Ecp.MessageBox.confirm(TXT.task_is_reject,function(){
		var params=btn['ecpOwner'].getItem(1).getValues();
		params['cmd']='task';
		params['action']='handle';
		params['id']=Ecp.components.taskGrid.grid.getSelectedId();
		params['transitionName']='R';
		Ecp.Ajax.request(params, function(result) {
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT.task_operate_error);
				return;
			}
			var win=btn['ecpOwner'];
			if(Ecp.components.taskGrid.grid.getSelectedRecord()['messageTag'].substring(1)=='99'){
				win.window.hide();
				this.notifyAll('reload');
			}
			else{	
				win.window.close();
				win['ecpOwner'].notifyAll('reload');
			}	
		},this);
	},this);
}

var clickTaskModifyBtn = function(btn, e) {
	var record=Ecp.components.taskGrid.grid.getSelectedRecord();
	if(record['messageType']=='MT' && record['messageTag'].substring(1)=='99' && !btn['ecpOwner'].getItem(0).isValid())
		return;
	var params=btn['ecpOwner'].getItem(1).getValues();
	params['cmd']='task';
	params['action']='handle';
	params['id']=record['id'];
	params['transitionName']='M';
	if(this['dataBus']!=undefined&&this['dataBus']['type']=='MT'){
		Ecp.MessageBox.confirm(TXT.task_is_modify,function(){
			var finParam=btn['ecpOwner'].getItem(0).getValues();
			var validateParams={};
			validateParams['finSender']=finParam['sender'];
			validateParams['finReceiver']=finParam['receiver'];
			validateParams['finRelatedReference']=finParam['relatedReference'];
			validateParams['internalCode']=finParam['reference'];
			validateParams['body']=finParam['body'];
			validateParams['cmd']='message';
			validateParams['action']='vaildatorText';
			Ecp.Ajax.request(validateParams,function(result){
				if (result.result == 'failure') {
					Ext.MessageBox.alert(TXT.common_hint,TXT.message_transform_error);
					return;
				}
				params['sender']=finParam['sender'];
				params['receiver']=finParam['receiver'];
				params['finRelatedReference']=finParam['relatedReference'];
				params['body']=finParam['body'];
				delete params['relatedReference'];
				Ecp.Ajax.request(params,function(result){
					if (result.result == 'failure') {
						Ecp.MessageBox.alert(TXT.task_operate_error);
						return;
					}
					this.notifyAll('reload');
					this.window.window.hide();
				},this);
			},this);
		},this);
	}else if(record['messageTag'].length==3){
		Ecp.MessageBox.confirm(TXT.task_is_modify,function(){
			var receiver=btn['ecpOwner']['ecpOwner'].items[0]['ecpOwner']['ecpOwner'].getReceiverForFinTemplate();
			var sender=Ecp.userInfomation['instBic'];
			btn['ecpOwner']['ecpOwner'].validateFin(record['messageName'],sender,receiver,function(){
				params['body']=Ecp.MessageViewTabPanel.replaceSuffix(btn['ecpOwner']['ecpOwner'].getMessageBody());
				params['sender']=sender;
				params['receiver']=receiver;
				Ecp.Ajax.request(params, function(result) {
					if (result.result == 'failure') {
						Ecp.MessageBox.alert(TXT.task_operate_error);
						return;
					}
					var win=btn['ecpOwner'];
					win.window.close();
					win['ecpOwner'].notifyAll('reload');
					});
				});
		});
	}
	else
	{
		Ecp.MessageBox.confirm(TXT.task_is_modify,function(){
			btn['ecpOwner']['ecpOwner'].validate(record['messageTag'],function(){
				params['messageBody']=btn['ecpOwner']['ecpOwner'].getMessageBody();
				Ecp.Ajax.request(params, function(result) {
					if (result.result == 'failure') {
						Ecp.MessageBox.alert(TXT.task_operate_error);
						return;
					}
					var win=btn['ecpOwner'];
					win.window.close();
					win['ecpOwner'].notifyAll('reload');
					});
				});
		});
	}
}
var clickTaskModifyForRefundBtn = function(btn, autowin) {
	var record=Ecp.components.taskGrid.grid.getSelectedRecord();
	if(!(record['messageType']=='MT' && record['messageTag'].substring(1)=='99'))
		return;
	Ecp.Ajax.request({cmd:'task',action:'checkPaymentDealStatus',messageId:record['messageId']},function(result){
		if(result.result=='success'){// it is refund 
			var fieldNameList=autowin['dataBus']['fieldNameList'];
			var typeList=autowin['dataBus']['typeList'];
			var values=autowin['dataBus']['values'];
			var prefix=autowin['dataBus']['prefix'];
			var params={cmd:'task',action:'handle'};
			var count = 0;
			var cbCount = 0;
			for ( var i = 0; i < fieldNameList.length; i++) {
				if (typeList[i] == 'date')
					values[fieldNameList[i]] = transFormDate(autowin.findById(fieldNameList[i]+prefix).getValue());
				else if (typeList[i] == 'checkBox') {
					cbCount++;
					values[fieldNameList[i]] = autowin.findById(fieldNameList[i]+prefix).getValue();
					if (values[fieldNameList[i]] == '')
						count++;
				}
				else
					values[fieldNameList[i]] = autowin.findById(fieldNameList[i]+prefix).getValue();
				params[fieldNameList[i]]=values[fieldNameList[i]];
			}	
			if(params['reason']=='OTHER'){
				params['reason']=params['reasonContent'];
			}
			if (count == cbCount && cbCount != 0&&tempForm.verifyCheckBox==true) {
				Ecp.MessageBox.alert(TXT.privateTemplate_checkBoxNull);
				return;
			}
			Ecp.MessageBox.confirm(TXT.task_is_modify,function(){
				params['cmd']='message';
				params['action']='tmpToFin';
				params['tId']=REFUND_ID;
			/*	if (tempForm.selectedTag!='')
				params ['comboTextArea']='yes';
			*/
				params ['msgIdForSd']=record['messageId'];
				Ecp.Ajax.request(params, function(finContent) {
					if (finContent.result == 'failure') {
						Ecp.MessageBox.alert(TXT.message_transform_error);
						return;
					}else{
						var refundFinInfoWin = RefundFinInfoWin(record,finContent,params);
						refundFinInfoWin.show();
						refundFinInfoWin['ecpOwner'].getItem(0).setValues(finContent);
					}
				});
			});
		}else{
			Ecp.MessageBox.alert(TXT.task_is_refund_dealstatus_change_to_cencel);
		}
	});
}
var submitModifyRefundInfo = function(record,finContent,params){
	var finDataMap={finSender:'sender',finReceiver:'receiver',internalCode:'reference',finRelatedReference:'relatedReference',body:'body'};
	var validateFinInfo={cmd:'message',action:'vaildatorText'};
	Ext.iterate(finDataMap,function(key,value){
		validateFinInfo[key]=finContent[value];
		params[key]=finContent[value];
	});
	Ecp.Ajax.request(validateFinInfo,function(validateResult){
		if (validateResult.result == 'failure') {
			Ecp.MessageBox.alert(TXT.message_transform_error);
			return;
		}
		params['id']=record['id'];
		params['transitionName']='M';
		params['cmd']='task';
		params['action']='handle';
		params['comments']=Ecp.components.refundMessageWin.getItem(1).getValues()['comments'];
		Ecp.Ajax.request(params, function(result) {
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT.task_operate_error);
				return;
			}
			Ecp.components.refundMessageWin.window.window.hide();
			Ecp.components.taskGrid.reloadUpdate('reload');
		});
	},this);
}
var clickTaskCancelBtn = function(btn, e) {
	Ecp.MessageBox.confirm(TXT.task_is_cancel,function(){
		var params=btn['ecpOwner'].getItem(1).getValues();
		params['cmd']='task';
		params['action']='handle';
		params['id']=Ecp.components.taskGrid.grid.getSelectedId();
		params['transitionName']='C';
		Ecp.Ajax.request(params, function(result) {
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT.task_operate_error);
				return;
			}
			var win=btn['ecpOwner'];
			if(Ecp.components.taskGrid.grid.getSelectedRecord()['messageTag'].substring(1)=='99'){
				win.window.hide();
				this.notifyAll('reload');
			}else{
				win.window.hide();
				win['ecpOwner'].notifyAll('reload');
			}
		},this);
	},this);
}
var clickTaskRunBtn = function(btn, e) {
	var params=btn['ecpOwner'].getItem(1).getValues();
	var record=Ecp.components.taskGrid.grid.getSelectedRecord();
	params['cmd']='task';
	params['action']='handle';
	params['id']=record['id'];
	params['transitionName']='P';
	Ecp.MessageBox.confirm(TXT.task_is_release,function(){
		Ecp.Ajax.request(params, function(result) {
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT.task_operate_error);
				return;
			}
			Ecp.components.msgWinInTaskProc.window.window.hide();
			Ecp.components.taskGrid.reloadUpdate('reload');
		},this);
	},this);
}

/*
function setCareForTaskWin(bt){
	var	careTxt = bt.getText();
	if(careTxt == TXT.message_notCare){
		bt.setText(TXT.message_care);
	}else{
		bt.setText(TXT.message_notCare);
	}
	var params = {cmd:"message", action:"setCareFlag", id:this['dataBus']['mid']};
	Ecp.Ajax.request(params, function (result) {
		if (result.result == "success") {
			Ecp.components.taskMsgGrid.show();
		} else {
			Ecp.MessageBox.alert(TXT.message_NoPower);
		}
	});
}
var showCaseSelectWinAction=function(){
	var cmtStoreObj={};
	typeof cmt_case_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_case_dsConfig;
	typeof cmt_case_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_case_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_case_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_case_gridConfig;
	typeof cmt_case_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_case_cmConfig;
	typeof cmt_case_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_case_gridEventConfig;
	
	var windowObj = {};
	if (typeof cmt_case_config != 'undefined')
		windowObj['config'] = cmt_case_config;
	if (typeof cmt_case_buttons != 'undefined')
		windowObj['buttons'] = cmt_case_buttons;

	var fromObj = {};
	if (typeof cmt_case_formConfig != 'undefined')
		fromObj['config'] = cmt_case_formConfig;
	if (typeof cmt_case_formButtons != 'undefined')
		fromObj['buttons'] = cmt_case_formButtons;
	if (typeof cmt_case_formItems != 'undefined')
		fromObj['items'] = cmt_case_formItems;
	
	var win=Ecp.CaseSelectWindow.createSelectWin({
		cusCaseGrid:{
			store:cmtStoreObj,
			grid:cmtGridObj
		},
		cusCaseTrigger:fromObj,
		cusCaseSelectWin:windowObj
	},[this,Ecp.components.taskMsgGrid,Ecp.components.taskCaseGrid]);
	win.dataBus=this.dataBus;
	//alert(msgWin.update);
	/*if(msgWin.update==undefined){
		msgWin.update=function(src,eventName){
			msgWin.window.window.hide();
		}
		win.addObserver(msgWin);
		win.addObserver(Ecp.components.taskMsgGrid);
		win.addObserver(Ecp.components.taskCaseGrid);
	}*/
	win.show();
}

var integrateMsgIntoCase=function(){
	var id=this.items[1]['ecpOwner'].getSelectedId();
	if(!id)
		return;
	var param={
		cmd:'message',
		action:'handlerCreateCaseOrImpMsg',
		messageId:this['dataBus']['mid'],
		caseId:id
	};
	Ecp.Ajax.request(param, function(result) {
		if(result.result=='failure'){
			Ext.MessageBox.alert(TXT.common_hint,TXT[result.message]);
		}
		this.notifyAll('integrateMsgIntoCase');
		this.window.window.hide();
	},this);
}
 
var setMsgUnrelated=function(){
	var param={
		cmd:'message',
		action:'setUnrelated',
		messageId:this['dataBus']['mid']
	};
	Ext.MessageBox.confirm(TXT.common_hint, TXT.message_unrelated,
		function(btn) {
			if (btn == 'yes') {
				Ecp.Ajax.request(param, function(result) {
					this.notifyAll('reload');
					this.window.window.hide();
				},this);
			}
	},this);
}

var printMsg=function(){
	var url = "MessageServlet?printMode=y&id="+this['dataBus']['mid'];
	if(this['dataBus']['isPayment']!=undefined)
		url+='&msgType=P';
	window.open(url);
}

function showPaymentSearchWin(){
	//don't forget to add customization process
	var finDataMap={
		sender:'sendBic',
		receiver:'receiverBic',
		relatedReference:'referenceNum'
	};
	var dataBus=this['dataBus'];
	var msgWin=null;
	if(dataBus['type']=='MT'){
		Ext.iterate(finDataMap,function(key,value){
			dataBus[value]=dataBus[key];
		});
		msgWin=Ecp.components['msgWinInReceiveProc'];
	}else
		msgWin=Ecp.MessageViewWindow.singleMessageView[0];
	var win=Ecp.PaymentMessageIntegrateWindow.createWindow({
		cusExplicitForm:{},
		cusMutilSearchPanel:{},
		cusSelectGridPanel:{},
		cusTargetGridPanel:{},
		cusSearchWin:{},
		cusKeyWordsForm:{},
		searchForm:{}
	},[Ecp.components.taskMsgGrid,Ecp.components.taskCaseGrid],[msgWin]);//Ecp.components['caseSelectWin']],[msgWin]);
	win.show();
	win['dataBus']=dataBus;
	win.setValues(dataBus);
	//alert(win.mutilSearchPanel.getTab(0)['ecpOwner']['ecpOwner']['defaultFormConfig']);
	additionalSearchForBOCOMM.call(win.mutilSearchPanel.getTab(0)['ecpOwner']['ecpOwner'],dataBus['referenceNum']);
}

function checkSelectedPayment(){
	//alert(this['dataBus']);
	//alert(this.constructor);
	var paymentRecord=this['targetRecordCollections'];
	var ids=[];
	var caseIds=[];
	var caseType=null;
	Ext.iterate(paymentRecord,function(key,value){
		ids.push(key);
		var record=paymentRecord[key];
		if(record.get('cid')!=''){
			caseIds.push(record.get('related'));
			caseType=record.get('caseType');
		}
	});
	if(caseIds.length>1){
		Ext.MessageBox.alert(TXT.common_hint,TXT.payment_selectFailure);
		return;
	}
	var paramObj={
			ids:ids,
			caseId:caseIds.length==0?'':caseIds[0],
			caseType:caseType,
			msgType:this['dataBus']['type'],
			messageId:this['dataBus']['mid'],
			messageTypeCheck:this['dataBus']['messageTypeCheck'],
			win:this
	};
	integratePaymentWithCase(paramObj);
}

function getIBPSeqNum(){
	if(!this.IBPSeqNumForm.form.isValid())
		return;
	var IBPSeqNumValue=this.IBPSeqNumForm.getIBPSeqNumValue();
	if(IBPSeqNumValue!='')
		this.dataBus['IBPSeqNum']=IBPSeqNumValue;
	integratePaymentWithCase(this.dataBus);	
	this.window.window.hide();
}

function integratePaymentWithCase(obj){
	var hintMsg='';
	var xmlLogicalMsg='';
	var xmlParam={};
	var param={
		cmd:'message',
		action:'handlerCreateCaseOrImpMsg',
		code:'1',
		pid:obj['ids'],
		messageId:obj['messageId']
	};
	
	if(obj['caseId']==''){
		if(obj['ids'].length==0){
			hintMsg=TXT.case_createCaseNoPayment;
			delete param['pid'];
		}
		else
			hintMsg=TXT.case_createCase;
	}else{
		if(obj['msgType']=='MX'){
			if(obj['messageTypeCheck'].substr(4)==obj['caseType']){
				hintMsg=TXT.case_duplicateCase;
				xmlParam['action']='closeDuplicateCase';
			}
			else if(obj['caseType']=='UTA'){
				if(obj['messageTypeCheck']=='008 RTCP'||obj['messageTypeCheck']=='007 RTMP'){
					hintMsg=TXT.case_closeUTAcaseFirst;
					xmlParam['action']='createFollowCase';
				}
			}else{
				Ext.MessageBox.alert(TXT.common_hint,TXT.case_cannotImport);
				return;
			}
			xmlParam['cmd']='message';
			xmlParam['caseId']=obj['caseId'];
			xmlParam['messageId']=obj['messageId'];
		}else{
			//hintMsg = TXT.case_confirmImportCase_start + obj['caseId']+TXT.case_confirmImportCase_end;
			hintMsg='';
		}
	}
	if(hintMsg!=''){
		Ext.MessageBox.confirm(TXT.common_hint, hintMsg, function(btn) {
			if (btn == 'yes') {
				requestServerToHandle(obj,xmlParam,param);
			}
		});
	}else
		requestServerToHandle(obj,xmlParam,param);
}

function requestServerToHandle(obj,xmlParam,param){
	if(xmlParam['cmd']!=undefined){
		Ecp.Ajax.request(xmlParam,function(result){
		obj['win'].notifyAll('receiveMsgProEnd');
		obj['win'].notifyAll('assignCaseReload');
		obj['win'].window.window.hide();
	});
	}else{
		Ecp.Ajax.request(param, function(result){
		if(result.result=='failure'){
			Ext.MessageBox.alert(TXT.common_hint,TXT[result.message]);
			obj['win'].notifyAll('assignCaseReload');
		}else{
			if (result.isNew != ''&&result.autoAssign=='') {
				Ext.MessageBox.confirm(TXT.common_hint, TXT.case_isAssignNewCase,
				function(btn) {
					if (btn == 'yes') {
						showAssignWindowInReceiveWin(result.cid);
					}else{
						obj['win'].notifyAll('assignCaseReload');
					}
				});
			}
			else{
				obj['win'].notifyAll('assignCaseReload');
			}
				obj['win'].notifyAll('receiveMsgProEnd');
				obj['win'].window.window.hide();
			}
		});	
	}
}

function reloadForEvent(){
	Ecp.components.taskCaseGrid.reloadUpdate();
}

//caution! still has bug
function showAssignWindowInReceiveWin(cid){
		assignCase(cid,function(treeWin){
			treeWin['ecpOwner'].notifyAll('reload');
		},[Ecp.components.taskCaseGrid],{hide:reloadForEvent});
}

var showCaseInNewCaseListWin = function() {
	var cid=Ecp.components.taskCaseGrid.grid.getSelectedId();
	showCaseDetailByIdWin(cid,[Ecp.components.taskMsgGrid,Ecp.components.taskCaseGrid],true);
}

var showCaseInTaskDetailWin = function(btn, e) {
	var record=Ecp.components.taskGrid.grid.getSelectedRecord();
	var ensureFun=null;
	if(record['messageTag'].substring(1)=='99')
		ensureFun=ensureMtWinInTask;
	else
		ensureFun=ensureXmlWinInTask;
	showCaseDetailByIdWin(record['caseId'],[],true,ensureFun);
		
}

function showCaseDetailIncomingMsgWin() {
	var cid=Ecp.components.caseIncomingMsgGrid.grid.getSelectedId();
	var ensureFun=null;
	var record=Ecp.components.taskMsgGrid.grid.getSelectedRecord();
	if(record['type']=='MT')
		ensureFun=ensureMtWinInReceiverProc;
	else
		ensureFun=ensureXmlWinInReceiverProc;
	showCaseDetailByIdWin(cid,[Ecp.components.caseIncomingMsgGrid],true,ensureFun);
}

function messageSearchMenuForHandle(){
	var win=Ecp.MessageSearchWindow.createSingleWindowForHandle({'queryMethod':queryMessageForHandle});
	win.window.window.show();
}

var queryMessageForHandle = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values['cmd']='message';
		values['action']='findNewIncomingMessage';
		Ecp.components.taskMsgGrid.search(values);
		Ecp.components.taskMsgGrid.grid.grid.setTitle(TXT.task_new_message + '('
		                    + Ecp.components.taskMsgGrid.grid.getDataCount() + ')');
		win.window.hide();
	}
}

function caseSearchMenuForHandle(){
	var windowObj={};
	windowObj['items']=[{}];
	var win=Ecp.CaseSearchWindow.createSingleSearchWindowForHandle(windowObj);
	win.window.show();
}

function searchCasesForHandle(){
	var win = Ecp.components['CaseSearchWindowForHandle'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values['amountFrom']=setNumberFieldDefaultValue(values['amountFrom']);
		values['amountTo']=setNumberFieldDefaultValue(values['amountTo']);
		//values['spendTime']=setNumberFieldDefaultValue(values['spendTime']);
		
		var params={};
		params['cmd']='case';
		params['action']='listAllCaseAsssignment';
		params['json']=Ext.util.JSON.encode(values);
		
		Ecp.components.taskCaseGrid.search(params);
		Ecp.components.taskCaseGrid.grid.grid.setTitle(TXT.task_new_case + '('
		                    + Ecp.components.taskCaseGrid.grid.getDataCount() + ')');
		win.window.window.hide();
	}
}
function RefundFinInfoWin(record,finContent,params){
		var win=new Ecp.MessageFinContentWin();
		var msgConf=null;
		var buttons=[
			{
				text:TXT.common_btnOK,
				handler:function(){
					this['ecpOwner'].window.close();
					submitModifyRefundInfo(record,finContent,params);
				}
			},
			{
				text:TXT.common_btnClose,
				handler:function(){
					this['ecpOwner'].window.close();
				}
			}	
		];
		msgConf={
			readOnlyFlag:true
		};
		win.initForm({
			msgConf:msgConf,
			cusObj:{}
		});
		win.setMessageFinContentConf({
			modal:true,
			height:480
		});
		win.handleWidgetConfig(function(obj){
			obj['config']['id']='mtMsgInTaskList';
			obj['config']['title']=TXT.eni_message_fin;
		});
		win.setButtons(buttons);
		win.customization({});
		return win.render();
}