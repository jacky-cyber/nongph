function showTemplateLayout(){
	var grid=this.grid;
	var templateId=grid.grid.getSelectedId();
	if(!templateId)
		return;
	var tempFlag='';	
	if(grid instanceof Ecp.MessageTemplateGridWidget)
		tempFlag='template';
	else
		tempFlag='privateTemplate';
	showTemplateLayoutWin(this,templateId,tempFlag);
}
//for db click grid   to template and privateTemplate
function showTemplateLayoutByDBClickGrid(){
	var grid=this['ecpOwner'].ecpOwner;
	var templateId=grid.grid.getSelectedId();
	if(!templateId)
		return;
	var tempFlag='';	
	var win  =Ecp.components['messageTemplateWin'];
	if(grid instanceof Ecp.MessageTemplateGridWidget)
		tempFlag='template';
	else{
		win=Ecp.components['privateMessageTemplateWin'];
		tempFlag='privateTemplate';
	}
	//var privateWin = Ecp.components.privateMessageTemplateWin.grid.grid.getSelectedRecord();
	
	showTemplateLayoutWin(win,templateId,tempFlag);
}
function showTemplateLayoutWin(win,templateId,tempFlag){
	var templateRecordId ='';
	if(tempFlag=='privateTemplate'){
		var record=Ecp.components['privateMessageTemplateWin'].grid.grid.getSelectedRecord();
		templateRecordId=record['templateId'];
	}
	if(REFUND_ID==templateId||REFUND_ID==templateRecordId){
		if(win['dataBus']['pid']!=undefined && ''!=win['dataBus']['pid'] ){//in payment
			checkRefundPaymentMessageIsleg(win,templateId,tempFlag,win['dataBus']['pid']);
		}else{// in case 
			var caseId = win['dataBus']['cid'];
			if(caseId!=''){
				var paymentNum = Ecp.components.caseDeatilWin.caseDetailForm.form.findById("paymentNum");
				if(paymentNum){
					if(paymentNum==1){
						var paymentId = Ecp.components.caseDeatilWin.caseDetailForm.form.findById("paymentId");
						checkRefundPaymentMessageIsleg(win,templateId,tempFlag,paymentId);
					}else if(paymentNum>1){
						Ecp.ForwaredToClsWin.createWindow({"createRefundBySelectPaymentFun":function (obj) {
							obj.buttons[0].handler=function(){
								createRefundBySelectPayment(win,templateId,tempFlag);
							}
						},"createRefundBySelectPaymentGridFun":function (obj) {
							obj.defaultStoreConfig.baseParams['payment']="";
						}},caseId,'').show();
					}else{
						Ecp.MessageBox.alert(TXT.case_not_with_payment_or_stmtmisc);
					}
				}else{
					Ecp.MessageBox.alert(TXT.case_not_with_payment_or_stmtmisc);
				}
			}else{// create case
				if(win['dataBus']['messageId']!==undefined)
					showMessageLayoutByCase(win['dataBus']['messageId'], win['dataBus']['cid'], tempFlag, templateId,win.window.window);
				else{
					showMessageLayout(templateId,tempFlag,win['dataBus']['pid'],win.window.window,win['dataBus']['cid']);
				}
			}
		}
	}else{
		if(win['dataBus']['messageId']!==undefined)
			showMessageLayoutByCase(win['dataBus']['messageId'], win['dataBus']['cid'], tempFlag, templateId,win.window.window);
		else{
			showMessageLayout(templateId,tempFlag,win['dataBus']['pid'],win.window.window,win['dataBus']['cid']);
		}
	}
}
function checkRefundPaymentMessageIsleg(win,templateId,tempFlag,paymentId){
	Ecp.Ajax.request({cmd:"messageTemplate", action:"checkBackType", templateId:templateId, messageId:paymentId,caseId:win['dataBus']['cid'],tempFlag:tempFlag}, function (result) {
		if (result.result == "success" ) {
			if (result['message']&&result['message']>0) {
				Ecp.MessageBox.confirm(TXT.had_refund_for_sub_insitution,function(){
					showFinallyTemplateLayoutWin(tempFlag,templateId,paymentId);
				},win);
			}else {
					showFinallyTemplateLayoutWin(tempFlag,templateId,paymentId);
			}
		}else{
			showErrorInfoBox(result.message);
			return ;
		}
	});
}
function showErrorInfoBox(info){
	if(info=='messageStatusChange'){
		Ecp.MessageBox.alert(TXT.message_no_back_change_status);
	}else if (info=='message_no_input'){
		Ecp.MessageBox.alert(TXT.message_no_input);
	}else if (info=='withoutNotPayment'){
		Ecp.MessageBox.alert(TXT.withoutNotPayment);
	}else if (info=='message_NoPower'){
		Ecp.MessageBox.alert(TXT.message_NoPower);
	}else if (info=='withoutNotClearOrPayment'){
		Ecp.MessageBox.alert(TXT.withoutNotClearOrPayment);
	}else if (info=='centerbankNotToSubRefund'){
		Ecp.MessageBox.alert(TXT.centerbank_not_to_sub_refund);
	}else if (info=='messageOnlyCOrRDAppyly'){
		Ecp.MessageBox.alert(TXT.message_only_C_RD_appyly);
	}else if (info=='notMainPayment'){ 
		Ecp.MessageBox.alert(TXT.notMainPayment);	
	}else{
		Ecp.MessageBox.alert(TXT.message_back_error);
	}
}
function createRefundBySelectPayment(win,templateId,tempFlag){	
	var recordId=Ecp.components.ForwaredToClsPaymentGrid.grid.getSelectedId();
	if(!recordId){
		return ;
	}
	checkRefundPaymentMessageIsleg(win,templateId,tempFlag,recordId)
	Ecp.components.ForwaredToClsWin.window.close();
}
function showFinallyTemplateLayoutWin(tempFlag,templateId,paymentId){
	var messageTemplateWin = Ecp.components.messageTemplateWin;
	if(tempFlag=='privateTemplate'){
		messageTemplateWin=Ecp.components.privateMessageTemplateWin;
	}
	var pid = messageTemplateWin['dataBus']['pid'];
	if(paymentId!=null&&paymentId!=null){
		pid=paymentId;
	}
	if(messageTemplateWin['dataBus']['messageId']!==undefined)
		showMessageLayoutByCase(messageTemplateWin['dataBus']['messageId'], messageTemplateWin['dataBus']['cid'], tempFlag, templateId,messageTemplateWin.window.window);
	else{
		showMessageLayout(templateId,tempFlag,pid,messageTemplateWin.window.window,messageTemplateWin['dataBus']['cid']);
	}
}
function showSaveDraftTemplateWin(){
	var isClose=false;
	if(this['dataBus']['cid']!='')
		isClose=true;
	if(this['dataBus']['tempFlag']!='template'){	
		var record=Ecp.components['privateMessageTemplateWin'].grid.grid.getSelectedRecord();
		if(record['ownerId']!=Ecp.userInfomation.id){
			Ecp.MessageBox.alert(TXT.private_message_can_not_edit);
			return;	
		}else{
			var win=Ecp.DraftNameSetWindow.createWindow({
				cusNameForm:{},
				cusNameWin:{}
			},[Ecp.components['privateMessageTemplateWin'].grid]);
		}
	}else
		var win=Ecp.DraftNameSetWindow.createWindow({
			cusNameForm:{},
			cusNameWin:{}
		},[]);
	win['dataBus']=this['dataBus'];
	win['dataBus']['isClose']=isClose;
	win['dataBus']['tempWin']=this;
	win.window.window.show();
	if(win['dataBus']['existTName']!==undefined&&win['dataBus']['existTName']!='')
		win.form.form.setValues({tName:win['dataBus']['existTName']});
}

function saveOrEditDraftTemplate(){
	var tName = this.form.form.getValues()['tName'];
	var tempForm=this['dataBus']['tempWin'].items.itemAt(0);
	var fieldNameList=this['dataBus']['fieldNameList'];
	var typeList=this['dataBus']['typeList'];
	var prefix=this['dataBus']['prefix'];
	tempForm.findById(fieldNameList[0]+prefix).focus();
	var tParams = {cmd:'messageTemplate',action:'savePrivateTemplate',tName:tName};;
	if(this['dataBus']['tempFlag'] == 'template')
		tParams['templateId']=this['dataBus']['tempId'];
	else
		tParams['privateTemplateId']=this['dataBus']['tempId'];
	for ( var i = 0; i < fieldNameList.length; i++) {
		if (typeList[i] == 'date')
			tParams[fieldNameList[i]]=transFormDate(tempForm.findById(fieldNameList[i]+prefix).getValue());
		else
			tParams[fieldNameList[i]]=tempForm.findById(fieldNameList[i]+prefix).getValue();
	}
	if(tempForm.selectedTag!='')
		tParams['comboTextArea']='yes';
	function saveTemplateOnSuccess(result) {
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT[result.message]);
		} else {
			if(this['dataBus']['isClose']==false){
					if (this['dataBus']['tempFlag'] != 'template') {
						this.notifyAll('reload');
					}
				}
				this.window.window.hide();
				this['dataBus']['tempWin'].close();
			}
		}
	Ecp.Ajax.request(tParams,saveTemplateOnSuccess,this);				
}

function deleteDraft(){
	var grid=this.grid;
	var record=grid.grid.getSelectedRecord();
	if(!record)
		return;
	if(record['ownerId']!=Ecp.userInfomation.id){
		Ecp.MessageBox.alert(TXT.private_message_can_not_remove);
			return;
	}
	Ecp.MessageBox.confirm(TXT.privateTemplate_delete_confirmation,function(){
		var params ={cmd:'messageTemplate',action:'removePrivateTemplate',templateId:record['id']};
		Ecp.Ajax.request(params,function(result) {
			var msg = '';
			if (result.result == 'success') {
				if (result.message == 'deleteSuccess') {
					msg = TXT.privateTemplate_deleteSuccess;
					this.grid.dataStore.store.reload();
				}
			} else if (result.result == 'failure') {
				msg = TXT.private_message_can_not_remove;
			}
			Ecp.MessageBox.alert(msg);
		},this);
	},this);				
}

function transferTempToFin(){
	var tempForm=this.items.itemAt(0);
	var fieldNameList=this['dataBus']['fieldNameList'];
	var typeList=this['dataBus']['typeList'];
	var values=this['dataBus']['values'];
	var prefix=this['dataBus']['prefix'];
	if (!tempForm.form.isValid()){
		return;
	}
	var params={cmd:'message',action:'tmpToFin'};
	var count = 0;
	var cbCount = 0;
	for ( var i = 0; i < fieldNameList.length; i++) {
		if (typeList[i] == 'date')
			values[fieldNameList[i]] = transFormDate(tempForm.findById(fieldNameList[i]+prefix).getValue());
		else if (typeList[i] == 'checkBox') {
			cbCount++;
			values[fieldNameList[i]] = tempForm.findById(fieldNameList[i]+prefix).getValue();
			if (values[fieldNameList[i]] == '')
				count++;
		}
		else
			values[fieldNameList[i]] = tempForm.findById(fieldNameList[i]+prefix).getValue();
		params[fieldNameList[i]]=values[fieldNameList[i]];
	}
	if (count == cbCount && cbCount != 0&&tempForm.verifyCheckBox==true) {
		Ecp.MessageBox.alert(TXT.privateTemplate_checkBoxNull);
		return;
	}
	if (this['dataBus']['tempFlag'] == 'template')
		params['tId']=this['dataBus']['tempId'];
	else
		params['pId']=this['dataBus']['tempId'];
	if (tempForm.selectedTag!='')
		params ['comboTextArea']='yes';
	if (this['dataBus']['messageId']!==undefined)
		params ['msgIdForSd']=this['dataBus']['messageId'];
	var dataBus=this['dataBus'];
	Ecp.Ajax.request(params, function(result) {
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT.message_transform_error);
			return;
		}
		win.show();
		win['dataBus']=dataBus;
		win.window.getItem(0).setValues(result);
	});
	var winObj={
		cusMsgContentForm:{},
		cusMsgContentWin:{},
		isFree:autowin['dataBus']['isFree']
	};
	if(this['dataBus']['cid']!==undefined&&this['dataBus']['cid']!=''
		&&this['dataBus']['pid']!==undefined&&this['dataBus']['pid']!=''){
		winObj['createFunction']=createAndIntegrateMsgWithCase;
		winObj['pid']=this['dataBus']['pid'];
		winObj['isFree']='isFree';
	}else if(this['dataBus']['cid']!==undefined&&this['dataBus']['cid']!=''){
		winObj['createFunction']=createAndIntegrateMsgWithCase;
		winObj['isFree']='isFree';
	}else if(this['dataBus']['messageId']!==undefined){
		winObj['createFunction']=createAndIntegrateMsgWithCase;
		winObj['isFree']='isFree';
	}
	else if(this['dataBus']['pid']!==undefined&&this['dataBus']['pid']!='')
		winObj['createFunction']=createCaseInPaymentList;
	else
		winObj['createFunction']=createCaseWithCaseList;
	//modify by sunyue 2013/01/21  for refund , it cannot edit message
	if(this['dataBus']['templateName']==REFUND_NAME){
		delete Ecp.components['finWinToGenCaseInPayment'];
		winObj["templateNameIsRefunds"]=true;
	}
	//---------------------------------------------
	var win=Ecp.MessageFinContentWin.createSingleFinMsgWinToGenCase(winObj,[]);
	win['tmpWin']=this;
						/*var body;
						var sendBic;
						var receiverBic;
						var internalCode;
						var f21;
						Ecp
											.ajaxRequest(
													DISPATCH_SERVLET_URL,
													'POST',
													params,
													function(result) {
														if (result.result == 'failure') {
															Ext.MessageBox
																	.alert(
																			TXT_common_hint,
																			TXT_message_transform_error);
															return;
														}
														sendBic = result.sendBic;
														receiverBic = result.receiverBic;
														internalCode = result.internalCode;
														f21 = result.f21;
														Ecp
																.ajaxRequestNoJson(
																		DISPATCH_SERVLET_URL,
																		'POST',
																		params + '&isString=yes',
																		function(
																				result) {
																			body = result;
																			var json = {};
																			json.body = body;
																			json.sender = sendBic;
																			json.receiver = receiverBic;
																			json.pid = pid;
																			json.tmpField = fieldNameList;
																			json.tmpValue = valueArray;
																			json.reference = internalCode;
																			json.relatedReference = f21;
																			json.autoWin = autowin;
																			json.duplicate = duplicate;
																			json.caseId = caseId;
																			json.autoForm = autoform;
																			json.prefix=prefix;
																			json.isFree=null;
																			//add by liusien add templateName
																			json.templateName=templateName;
																			if(autoform.findById('isFree'+prefix)!=undefined||(autoform.findById('isFree'+prefix)!=null))
																				json.isFree=autoform.findById('isFree'+prefix).getValue();
																			showFinMessageWin(json);
																		});
													});*/
								
}

function createCaseInPaymentList(){
	var finDataMap={finSender:'sender',finReceiver:'receiver',internalCode:'reference',finRelatedReference:'relatedReference',body:'body'};
	if(!this.items[0]['ecpOwner'].isValid())
		return;
		//TODO
	var values=this.items[0]['ecpOwner'].getValues();
	var params={cmd:'message',action:'vaildatorText'};
	Ext.iterate(finDataMap,function(key,value){
		params[key]=values[value];
	});
	Ecp.Ajax.request(params,function(result){
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT.message_transform_error);
			return;
		}
		var checkIsActiveEniParam={cmd:'message',action:'isEniUser',receiver:params['finReceiver']};
		Ecp.Ajax.request(checkIsActiveEniParam,function(result){
			if(result.message=='isEniUser'){
				Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
					createCaseInPaymentListInner(this,params);
				},this);
			}
			else
				createCaseInPaymentListInner(this,params);
		},this);						
	},this);
}

function createCaseInPaymentListInner(msgWin,params){
	var vMsg = TXT.case_createCaseInPaymentList;
	if (msgWin['tmpWin']['isSecondCreate']!==undefined)
		vMsg = TXT.case_createCaseInPaymentListForBack;
	Ecp.MessageBox.confirm(vMsg,function(){
		params['action']='createEIMessageByFinMessage';
		params['pid']=msgWin['dataBus']['pid'];
		params['byPayment']='yes';
		params['templateName']=msgWin['dataBus']['templateName'];
		var tempVlues=msgWin['dataBus']['values'];
		Ext.iterate(tempVlues,function(key,value){
			params[key]=value;
		});
		Ecp.Ajax.request(params,function(result) {
			if (result.result == 'failure') {
				if(result.message!==undefined&&result.message!='')
					Ecp.MessageBox.alert(TXT[result.message]);
				else
					Ecp.MessageBox.alert(TXT.message_transform_error);
			} else {
				if (msgWin['dataBus']['duplicate'] == '')
					msgWin['tmpWin'].close();
				else if (this['dataBus'] && this['dataBus']['duplicate'] != ''&& this['tmpWin']['isSecondCreate']!==undefined) {
					msgWin['tmpWin'].close();
				} else {
					msgWin['tmpWin']['isSecondCreate'] = 'yes';
					msgWin['tmpWin'].items.itemAt(0).findById('hiddenInternalCode').setValue(result.createdIntCode);
				}
				msgWin.window.window.hide();
				msgWin['dataBus']['tempListWin'].hide();
				Ecp.components.paymentGrid.reload();
			}
		},this);															
	},this);	
}

function createAndIntegrateMsgWithCase(){
	var finDataMap={finSender:'sender',finReceiver:'receiver',internalCode:'reference',finRelatedReference:'relatedReference',body:'body'};
	if(!this.items[0]['ecpOwner'].isValid())
		return;
	var values=this.items[0]['ecpOwner'].getValues();
	var params={cmd:'message',action:'vaildatorText'};
	Ext.iterate(finDataMap,function(key,value){
		params[key]=values[value];
	});
	Ecp.Ajax.request(params,function(result){
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT.message_transform_error);
			return;
		}
		var checkIsActiveEniParam={cmd:'message',action:'isEniUser',receiver:params['finReceiver']};
		Ecp.Ajax.request(checkIsActiveEniParam,function(eniResult){
			if(eniResult.message=='isEniUser'){
				Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
					createAndIntegrateMsgWithCaseInner(this,params,result);
				},this);
			}
			else
				createAndIntegrateMsgWithCaseInner(this,params,result);
		},this);													
	},this);
}

function createAndIntegrateMsgWithCaseInner(msgWin,params,result){
	var vMsg = TXT.case_importCase;
	if (result.payment != '')
		vMsg = TXT.message_createWithNoPayment;
	Ecp.MessageBox.confirm(vMsg,function(){
		params['action']='createEIMessageByFinMessage';
		params['templateName']=msgWin['dataBus']['templateName'];
		delete params['internalCode'];
		var tempVlues=msgWin['dataBus']['values'];
		Ext.iterate(tempVlues,function(key,value){
			params[key]=value;
		});
		if(params['templateName']==REFUND_NAME && msgWin['dataBus']['pid']!=''){
			params['pid']=msgWin['dataBus']['pid'];
			msgWin['dataBus']['duplicate'] = '';
		}
		Ecp.Ajax.request(params,function(result) {
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT.message_transform_error);
			} else {
				if (msgWin['dataBus']['duplicate'] == '')
					msgWin['tmpWin'].close();
				else if (msgWin['dataBus']['duplicate'] != ''&& msgWin['tmpWin']['isSecondCreate']!==undefined) {
					msgWin['tmpWin'].close();
				} else {
					msgWin['tmpWin']['isSecondCreate'] = 'yes';
				}
				msgWin.window.window.hide();
				if(msgWin['dataBus']['tempListWin']!=null){
					msgWin['dataBus']['tempListWin'].hide();
				}
				reloadPartCaseDetail('messages',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'));
				reloadPartCaseDetail('payments',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('paymentMessages'));
			}
		},msgWin);															
	},msgWin);
}

function createCaseWithCaseList(){
	var finDataMap={finSender:'sender',finReceiver:'receiver',internalCode:'reference',finRelatedReference:'relatedReference',body:'body'};
	if(!this.items[0]['ecpOwner'].isValid())
		return;
	var values=this.items[0]['ecpOwner'].getValues();
	var params={cmd:'message',action:'vaildatorText'};
	Ext.iterate(finDataMap,function(key,value){
		params[key]=values[value];
	});
	Ecp.Ajax.request(params,function(result){
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT.message_transform_error);
			return;
		}
		var checkIsActiveEniParam={cmd:'message',action:'isEniUser',receiver:params['finReceiver']};
		Ecp.Ajax.request(checkIsActiveEniParam,function(eniResult){
			if(eniResult.message=='isEniUser'){
				Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
					createCaseWithCaseListInner(this,values,params);
				},this);
			}
			else
				createCaseWithCaseListInner(this,values,params);
		},this);	
	},this);
}

function createCaseWithCaseListInner(autoWin,values,params){
	var tmpValues=autoWin['dataBus']['values'];
		if (autoWin['tmpWin']['isSecondCreate']===undefined){
			var selectDataMap={referenceNum:values['relatedReference'],sendBic:values['sender'],receiverBic:values['receiver'],dateFrom:tmpValues['valueDate'],dateTo:tmpValues['valueDate'],amountMax:tmpValues['amount'],amountMin:tmpValues['amount'],currency:tmpValues['currency']};
			var win=Ecp.PaymentMessageIntegrateWindow.createWindow({
				cusExplicitForm:{},
				cusMutilSearchPanel:{},
				cusSelectGridPanel:{},
				cusTargetGridPanel:{},
				cusSearchWin:{},
				cusKeyWordsForm:{},
				searchForm:{}
			},[Ecp.components.caseGrid],[autoWin]);
			win.show();
			win['dataBus']=autoWin['dataBus'];
			win['dataBus']['params']=params;
			win['tmpWin']=autoWin['tmpWin'];
			win.setValues(selectDataMap);
			additionalSearchForBOCOMM.call(win.mutilSearchPanel.getTab(0)['ecpOwner']['ecpOwner'],selectDataMap['referenceNum']);
		}else{
			var msg = TXT.message_confirmImportCase_start+autoWin['tmpWin'].items.itemAt(0).findById('hiddenInternalCode').getValue()+ TXT.message_confirmImportCase_end;
			Ecp.MessageBox.confirm(msg,function(){
				params['action']='createEIMessageByFinMessage';
				params['templateName']=autoWin['dataBus']['templateName'];
				delete params['internalCode'];
				var tempVlues=autoWin['dataBus']['values'];
				Ext.iterate(tempVlues,function(key,value){
					params[key]=value;
				});
				Ecp.Ajax.request(params,function(result) {
					if (result.result == 'failure') {
						Ecp.MessageBox.alert(TXT.message_transform_error);
					} else {
						autoWin['tmpWin'].close();
						autoWin.window.window.hide();
						autoWin['dataBus']['tempListWin'].hide();
						Ecp.components.caseGrid.reloadUpdate();
					}
				},autoWin);															
			},autoWin);
		}
}

function checkSelectedPayment(){
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
	createOrImportMsgToCase({
		ids:ids,
		caseId:caseIds.length==0?'':caseIds[0],
		win:this
	});
}

function createOrImportMsgToCase(obj){
	var params=obj['win']['dataBus']['params'];
	if(obj['caseId']==''){
		if(obj['ids'].length==0){
			hintMsg=TXT.case_createCaseNoPayment;
		}
		else{
			hintMsg=TXT.case_createCase;	
		}
	}else{
		hintMsg = TXT.case_confirmImportCase_start + obj['caseId']+TXT.case_confirmImportCase_end;
	}
	obj['ids'].length==0?1:params['pid']=obj['ids'];
	if(obj['win']['dataBus']['msgTag']===undefined){
		Ecp.MessageBox.confirm(hintMsg,function(){
			params['action']='createEIMessageByFinMessage';
			params['templateName']=this['dataBus']['templateName'];
			delete params['internalCode'];
			var tempVlues=this['dataBus']['values'];
			Ext.iterate(tempVlues,function(key,value){
				params[key]=value;
			});
			Ecp.Ajax.request(params,function(result) {
				if (result.result == 'failure') {
					Ecp.MessageBox.alert(TXT[result.message]);
				} else {
					if (this['dataBus']['duplicate'] == '')
						this['tmpWin'].close();
					else if (this['dataBus']['duplicate'] != ''&& this['tmpWin']['isSecondCreate']!==undefined) {
						this['tmpWin'].close();
					} else {
						this['tmpWin']['isSecondCreate'] = 'yes';
						this['tmpWin'].items.itemAt(0).findById('hiddenInternalCode').setValue(result.createdIntCode);
					}
					this.window.window.hide();
					this['dataBus']['tempListWin'].hide();
					this.notifyAll('afterCreateCase');
				}
			},this);
		},obj['win']);
	}else{
		if(params['fin']===undefined&&params['pid']!==undefined){
			params['paymentId']=params['pid'];
			delete params['pid'];
		}
		Ecp.MessageBox.confirm(hintMsg,function(){
			Ecp.Ajax.request(params,function(result) {
				if (result.result == 'failure') {
					Ecp.MessageBox.alert(TXT[result.message]);
				} else {
					this.window.window.hide();
					this.notifyAll('afterCreateCase');
				}
			},this);
		},obj['win']);
	}
}