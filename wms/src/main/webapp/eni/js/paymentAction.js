//change defaultItemsConfig to items
function showExplicitSearhPaymentWin(){
	var formObj={};
	
	var winObj={};
	
	var win=Ecp.PaymentMessageExplicitlySearchWin.createWindow({
		cusExplicitSearchForm:formObj,
		cusExplicitSearchWin:winObj,
		targetSearchPanel:Ecp.components['paymentGrid']
	});
	
	win.show();
}

function showSearchPaymentWin(){
	var formObj={};
	
	var winObj={};
	
	var win=Ecp.PaymentMessageSearchWin.createWindow({
		cusSearchForm:formObj,
		cusSearchWin:winObj,
		targetSearchPanel:Ecp.components['paymentGrid']
	});
	
	win.show();
}

function showPaymentMessageKeyWordsSearchWin(){
	var formObj={};
	
	var winObj={};
	
	var win=Ecp.PaymentMessageKeyWordsSearchWin.createWindow({
		cusKeyWordsSearchForm:formObj,
		cusKeyWordsSearchWin:winObj,
		targetSearchPanel:Ecp.components['paymentGrid']
	});
	
	win.show();
}

function explicitSearchPayment(){
	var byForm=false;
	if(this instanceof Ecp.PaymentMessageExplicitlySearchWin){
		var ecpForm=this.defaultItemsConfig[0]['ecpOwner'];
	}
	else{
		var ecpForm=this.form;
		byForm=true;
	}
	if(ecpForm.isValid()){
		this.targetSearchPanel.search(ecpForm.getValues());
		if(!byForm)
			this.window.window.hide();
	}
}

function keyWordsSearchPayment(){
	var byForm=false;
	if(this instanceof Ecp.PaymentMessageKeyWordsSearchWin){
		var ecpForm=this.defaultItemsConfig[0]['ecpOwner'];
	}
	else{
		var ecpForm=this.form;
		byForm=true;
	}
	if(ecpForm.isValid()){
		var baseParams={
			cmd:'payment',
			action:'searchByKeywords'
		};
		baseParams['json']=Ext.util.JSON.encode(ecpForm.getValues());
		this.targetSearchPanel.search(baseParams);
		if(!byForm)
			this.window.window.hide();
	}
}

function searchPayment(){
	var byForm=false;
	if(this instanceof Ecp.PaymentMessageSearchWin){
		var ecpForm=this.defaultItemsConfig[0]['ecpOwner'];
	}
	else{
		var ecpForm=this.form;
		byForm=true;
	}
	if(ecpForm.isValid()){
		var baseParams={
			cmd:'payment',
			action:'find'
		};
		//alert(baseParams['cmd']);
		var json=ecpForm.getValues();
		//alert(json['currency']);
		if(json['amountMin']==''||json['amountMax']==''){
			json['amountMin']=0;
			json['amountMax']=0;
		}
		baseParams['json']=Ext.util.JSON.encode(json);
		this.targetSearchPanel.search(baseParams);
		if(!byForm){
			this.window.window.hide();
		}
	}
}

function additionalSearchForBOCOMM(referenceNum){
	this.targetSearchPanel.search({
		cmd:'payment',
		action:'find',
		json :Ext.util.JSON.encode({
			referenceNum :referenceNum,
			dateFrom :'',
			dateTo :'',
			amountMin :'0',
			amountMax :'0',
			ioFlag :'',
			currency :'',
			sender:'',
			receiver:'',
			msgType:''
		})
	});
}

function addReceivePayment(){
	var record=this.selectGridPanel.grid.getSelected();
	if(!record)
		return;
	var id=record.id;
	if(this['targetRecordCollections'][id]!=undefined){
		Ext.MessageBox.alert(TXT.common_hint,TXT.case_payment_canOntSel);
		return;
	}
	var t = this.targetGridPanel.dataStore.store.getCount();
	if(this.targetRecordIndex >= 1 && REFUND_ID==this.dataBus.tempId){
		Ext.MessageBox.alert(TXT.common_hint,TXT.case_only_one_payment_for_fundId);
		return ;
	}
	/*if(REFUND_ID==this.dataBus.tempId){
		if(this.targetRecordIndex >= 1){
			Ext.MessageBox.alert(TXT.common_hint,TXT.case_only_one_payment_for_fundId);
			return ;
		}else{
			Ecp.Ajax.request({cmd:"messageTemplate", action:"checkBackType", templateId:REFUND_ID, messageId:id,caseId:''}, function (result) {
				if (result.result == "success" ) {
					if (result['message']&&result['message']>0) {
						Ecp.MessageBox.confirm(TXT.had_refund_for_sub_insitution,function(){
							
						},win);
					}else {
					
					}
				}else{
					if(result.message=='messageStatusChange'){
						Ecp.MessageBox.alert(TXT.message_no_back_change_status);
					}else if (result.message=='message_no_input'){
						Ecp.MessageBox.alert(TXT.message_no_input);
					}else if (result.message=='withoutNotPayment'){
						Ecp.MessageBox.alert(TXT.withoutNotPayment);
					}else if (result.message=='message_NoPower'){
						Ecp.MessageBox.alert(TXT.message_NoPower);
					}else if (result.message=='withoutNotClearPayment'){
						Ecp.MessageBox.alert(TXT.withoutNotClearPayment);
					}else{
						Ecp.MessageBox.alert(TXT.message_back_error);
					}
					return ;
				}
			});
		}
	}*/
	this.targetGridPanel.insertRecord(this.targetRecordIndex,record);
	this.targetRecordIndex++;
	this.targetRecordCollections[record.id]=record;
}

function removeReceivePayment(){
	var record=this.targetGridPanel.grid.getSelected();
	if(!record)
		return;
	this.targetGridPanel.remove(record);
	this.targetRecordIndex--;
	delete this.targetRecordCollections[record.id];
}

function showPaymentInRceiveProc(){
	var formObj={};
	
	var windowObj={};
	
	var id=this['ecpOwner'].getSelectedId();
	if(!id)
		return;
	Ecp.Ajax.request({cmd:'payment',action:'getPaymentMessage',id:id}, function(result) {
		win.show();
		win.window.getItem(0).setValues(result);
	});
	var win=Ecp.MessageFinContentWin.createSinglePaymentMessageWin({
		cusForm:formObj,
		cusWin:windowObj
	});
}

function getPaymentMessage(){
	var formObj={};
	//customization for paymentMessageShowWindow
	var winObj={};
	//customization for paymentMessageShowWindow
	
	var record=this['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	var winId='paymentShow'+record.id;	
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	var win=new Ecp.MessageFinContentWin();
	win.setButtons([{
				text:TXT.payment_related,
				handler:showLinkCaseWin,
				scope:win
			},{
				text:TXT.message_transform,
				handler:showTransformFinContentWin,
				scope:win
			},{
				text:TXT.message_print,
				handler:printMsg,
				scope:win
			},{
				text:TXT.common_btnClose,
				handler:function(){
					this['ecpOwner'].window.close();
				}
			}
	]);
	win['config']['id']=winId;
	win['config']['title']=TXT.payment_messageId+record.referenceNum;
	if(record['busiType']==1 ||record['busiType']==2||record['busiType']==3||record['busiType']==4){
		var messageFinContentFormForCLSForm = new Ecp.MessageFinContentFormForCLSForm();
		Ecp.components.messageFinContentFormForCLSForm=messageFinContentFormForCLSForm;
		win.items.splice(0,1);
		win.handleWidgetConfig(function(win){
			win['config']['layout']='column';
			if(record['busiType']==2){
				var paymentMessageInfoForm = new Ecp.paymentMessageInfoForm();
				Ecp.components.paymentMessageInfoForm=paymentMessageInfoForm;
				win['items'].push({columnWidth :.5,defaults : {anchor :'95%'},items : [messageFinContentFormForCLSForm.render()]});
				win['items'].push({columnWidth :.5,defaults : {anchor :'95%'},items : [paymentMessageInfoForm.render()]});
				win.setMessageFinContentConf({
					modal:true,
					width:800
				});
			}else if(record['busiType']=1||record['busiType']==3||record['busiType']==4){
				var paymentMessageCLSInfoForm = new Ecp.paymentMessageCLSInfoForm();
				Ecp.components.paymentMessageCLSInfoForm=paymentMessageCLSInfoForm;
				win['items'].push({columnWidth :.4,defaults : {anchor :'95%'},items : [messageFinContentFormForCLSForm.render()]});
				win['items'].push({columnWidth :.6,defaults : {anchor :'95%'},items : [paymentMessageCLSInfoForm.render()]});
				win.setMessageFinContentConf({
					modal:true,
					width:1000,
					height:500
				});
			}
		});
		if(record['clsId']&&record['clsId'].trim()!=''){
			win.buttons.splice(0,0,{
				text :TXT.common_btnSynchro,
				handler :function(){
					synchroPaymentWin(record['id'],record['busiType']);
				},
				scope:win
			});
		}
	}else{
		win.initForm({
			msgConf:{
					needValidate:false
				},
			cusObj:formObj
		});
	}
	win.customization(winObj);
	win.render();
	Ecp.Ajax.request({cmd:'payment',action:'getPaymentMessage',id:record.id}, function(result) {
			var paymentJson = convertPayMessageText(result.paymentMessage);
			if(record['busiType']==1||record['busiType']==3||record['busiType']==4){
				Ecp.components.messageFinContentFormForCLSForm.form.setValues(result);
				Ecp.components.paymentMessageCLSInfoForm.form.setValues(paymentJson);
			}else if(record['busiType']==2){
				Ecp.components.messageFinContentFormForCLSForm.form.setValues(result);
				Ecp.components.paymentMessageInfoForm.form.setValues(paymentJson);
			}else{
				win.window.getItem(0).setValues(result);
			}
			win.window.window.setPosition(undefined,undefined);
			win.show();
			delete result['result'];
			result['mid']=record.id;
			result['related']=record.related;
			win.dataBus=result;
			win.dataBus['isPayment']=true;
	});
}
function convertPayMessageText(paymentInfo){
	var pm=paymentInfo;
	pm.msgFlag = TXT.payment_msgFlag[paymentInfo.msgFlag];//add by jinlijun @2013/2/19
	pm.clrFlag = TXT.payment_clrFlag[paymentInfo.clrFlag];
	pm.reconcileType = TXT.payment_reconcileType[paymentInfo.reconcileType];
	pm.cancFlag = TXT.payment_cancFlag[paymentInfo.cancFlag];
	pm.dbCr = TXT.payment_dbCr[paymentInfo.dbCr];
	pm.settleFlag=TXT.payment_settleFlag[paymentInfo.settleFlag];
	pm.dealStatus=TXT.payment_dealStatus[paymentInfo.dealStatus];
	pm.dealType=TXT.payment_dealType['_'+paymentInfo.dealType];
	var txStatus = '_'+paymentInfo.txStatus;
	if (paymentInfo.ioFlag=='O') {
		pm.txStatus = TXT.payment_txStatus_O[txStatus];
	} else if (paymentInfo.ioFlag=='I') {
		pm.txStatus = TXT.payment_txStatus_I[txStatus];
	}
	var sysFlag = paymentInfo.sysFlag;
	if (paymentInfo.ioFlag=='O') {
		pm.sysFlag = TXT.payment_sysFlag_O[sysFlag];
	} else if (paymentInfo.ioFlag=='I') {
		pm.sysFlag = TXT.payment_sysFlag_I[sysFlag];
	}
	var dccSendFlag="";
	if (paymentInfo.dccSendFlag=='Y'||paymentInfo.dccSendFlag=='y') {
		paymentInfo.dccSendFlag = TXT.common_yes_desc;
	} else if (paymentInfo.dccSendFlag=='N'||paymentInfo.dccSendFlag=='n') {
		paymentInfo.dccSendFlag = TXT.common_no_desc;
	}
	return pm;
}
function synchroPaymentWin(recordId,busiType){
	var param={};
	param['id']=recordId;
	param['cmd']='payment';
	param['action']='synchroPaymentInfo';
	Ecp.Ajax.request(param, function(result) {
		if (result.result &&result.result == 'failure'){
			Ext.MessageBox.alert(TXT.common_hint,TXT.payment_synchro_failure);
			return;
		}else{
			var paymentJson = convertPayMessageText(result.paymentMessage);
			if(busiType==1||busiType==3||busiType==4){
				Ecp.components.messageFinContentFormForCLSForm.form.setValues(result);
				Ecp.components.paymentMessageCLSInfoForm.form.setValues(paymentJson);
			}else if(busiType==2){
				Ecp.components.messageFinContentFormForCLSForm.form.setValues(result);
				Ecp.components.paymentMessageInfoForm.form.setValues(paymentJson);
			}
		}
	});
}
function showLinkCaseWin(){
	var title='';
	var id='';
	var related='';
	var recordDataBus={};
	if(this instanceof Ecp.MessageFinContentWin){
		title=TXT.payment_messageId+this['dataBus']['reference'];
		id='linkPaymentShow'+this['dataBus']['mid'];
		related=this['dataBus']['related'];
	}else{
		var record=Ecp.components['paymentGrid'].grid.getSelectedRecord();
		if(!record)
			return;
		title=TXT.payment_messageId+record['referenceNum'];
		id='linkPaymentShow'+record['id'];
		related=record['related'];
		recordDataBus['mid']=record['id'];
		recordDataBus['reference']=record['referenceNum'];
		recordDataBus['related']=record['related'];
	}
	if(related!=''){
		Ext.MessageBox.alert(TXT.common_hint, TXT.payment_alreadyRelated);
		return;
	}
	if(Ext.WindowMgr.get(id)!=undefined)
		return;
	var formObj={};
	//customization for paymentLinkWindow
	var winObj={};
	//customization for paymentLinkWindow
	var cusGridObj={};
	var win=new Ecp.LinkCaseAndPaymenWin();
	win.handleWidgetConfig(function(win){
		win['defaultConfig']['id']=id;
		win['defaultConfig']['title']=title;
	});
	win.initForm({
		cusFormConfig:formObj,
		searchCaseHandler:searchCase
	});
	win.initCaseGrid({
		gridEventConfig:{
			grid:{
				//show caseDetail
				dblclick:showCaseDetailInPaymentLink
			}
		},
		cusGridPanel:cusGridObj
	});
	win.setButtonHandler(linkCase);
	if(this instanceof Ecp.MessageFinContentWin){
		win['dataBus']=this['dataBus'];
	}else{
		win['dataBus']=recordDataBus;
	}
	win.addObservers([Ecp.components.paymentGrid]);
	win.customization(winObj);
	win.render();
	win.window.window.setPosition(undefined,undefined);
	win.show();
	
	// register case grid
	//Ecp.components.caseInPaymentLinkGrid=win.window.getItem(1)['ecpOwner'];
}

function showCaseDetailInPaymentLink()
{
	var cid=this['ecpOwner'].getSelectedId();
	showCaseDetailByIdWin(cid,[this['ecpOwner']['ecpOwner']],true,ensureWinType);
}

function linkCase(){
	if(this['dataBus']['related']!=''){
		Ext.MessageBox.alert(TXT.common_hint, TXT.payment_alreadyRelated);
		return;
	}
	//alert(this.grid.grid.constructor);
	var cid=this.grid.grid.getSelectedId();
	if(!cid)
		return;
	var msg=TXT.payment_importMessageStart + this['dataBus']['reference']
				+ TXT.payment_importMessageEnd;
	var param={
		cmd:'payment',
		action:'relatedTo',
		pid:this['dataBus']['mid'],
		cid:cid
	};			
	Ext.MessageBox.confirm(TXT.common_hint, msg, function(btn) {
			if (btn == 'yes') {
				Ecp.Ajax.request(param, function(result) {
					if (result.result == 'failure'){
						Ext.MessageBox.alert(TXT.common_hint,
								TXT[result.message]);
						this.window.window.close();
						return;
					}
					this.notifyAll('afterRelated');
					this.window.window.close();
				},this);
			}
		},this);
}

function importPayment(){
	var dialog = new Ext.ux.UploadDialog.Dialog({
			url: DISPATCH_SERVLET_URL+'?cmd=payment&action=importPayment&type=y',
			reset_on_hide: false,
			allow_close_on_upload: true,
			upload_autostart: false,
			permitted_extensions: ['csv','CSV']
	});
	dialog.on('uploadsuccess',function(){
		Ecp.components['paymentGrid'].show();
		});
	dialog.show('show-button');
}

function initXmlMenuWhenLoadData(){
	var grid=Ecp.components.paymentGrid.grid;
	if(grid.grid.getTopToolbar().get(2)['menu'].items.length>3){
		var createCaseMenu=grid.grid.getTopToolbar().get(2)['menu'].get(3)['menu'];
		var btn007=createCaseMenu.get(0);
		var btn008=createCaseMenu.get(1);
		var btn026=createCaseMenu.get(2);
		btn007.hide();
		btn008.hide();
		btn026.hide();
		grid.selectFirstRow();
		if(Ecp.userInfomation.sendToSwift==false){
			btn026.hide();
		}
		if(grid.getSelectedCount()!=0){
			var record=grid.getSelectedRecord();
			var type=record['type'];
			if(record['ioFlag']=='O'){
				if(type=='103'||type=='202'){
					btn007.show();
					btn008.show();
				}
			}else{
				btn026.show();
			}
		}
	//alert('123');
	}
}

function checkXmlMenuWhenClick(){
	if(this['ecpOwner'].getSelectedCount()!=0){
		var grid=Ecp.components.paymentGrid.grid;
		if(grid.grid.getTopToolbar().get(2)['menu'].items.length>3){
		var createCaseMenu=this.getTopToolbar().get(2)['menu'].get(3)['menu'];
		var btn007=createCaseMenu.get(0);
		var btn008=createCaseMenu.get(1);
		var btn026=createCaseMenu.get(2);
		var record=this['ecpOwner'].getSelectedRecord();
		var type=record['type'];
		if(record['ioFlag']=='O'){
			btn026.hide();
		if(type=='103'||type=='202'){
			btn007.show();
			btn008.show();
		}else{
			btn007.hide();
			btn008.hide();	
		}
		}else{
			btn026.show();
			btn007.hide();
			btn008.hide();
		}
		}
	}
}

//just temp should transfer to a suitable plcae!!!!!!!!!
var printMsg=function(){
	var url = "MessageServlet?printMode=y&id="+this['dataBus']['mid'];
	if(this['dataBus']['isPayment']!=undefined)
		url+='&msgType=P';
	window.open(url);
}

/**
 * create case
 */
function showCreateCaseWith007InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWin(btn,e,swift_eni007_tag);
}

function showCreateCaseWith008InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWin(btn,e,swift_eni008_tag);
}

function showCreateCaseWith026InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWin(btn,e,swift_eni026_tag);
}

function showCreateCaseWith027InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWin(btn,e,swift_eni027_tag);
}

function showCreateCaseWith028InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWin(btn,e,swift_eni028_tag);
}

function showCreateCaseWith037InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWin(btn,e,swift_eni037_tag);
}

function showCreateCaseInPaymentListWin(btn,e,typeTag)
{
	if(Ecp.components.paymentGrid.grid.getSelectedRecord()['related']!='')
	{
		Ecp.MessageBox.alert(TXT.payment_canNotCreateCase);
		return;
	}
	
	var windowObj = {};
	if (typeof create_in_payment_list_window_config != 'undefined')
		windowObj['config'] = create_in_payment_list_window_config;
	if (typeof create_in_payment_list_window_buttons != 'undefined')
		windowObj['buttons'] = create_in_payment_list_window_buttons;
	
	windowObj['items']=[{}];
	if (typeof create_in_payment_list_tabPanel_config != 'undefined')
		windowObj['items'][0]['config'] = create_in_payment_list_tabPanel_config;
	if (typeof create_in_payment_list_tabPanel_items != 'undefined')
		windowObj['items'][0]['items'] = create_in_payment_list_tabPanel_items;
	
	var params={
			cmd:'message',
			action:'getMessageLayout',
			messageTypeTag:typeTag,
			paymentId:Ecp.components.paymentGrid.grid.getSelectedId()
	};
	Ecp.Ajax.requestNoDecode(params, function(result) {
		// global variable defined in payment.htm
		messageBody=result;
		//alert(messageBody);
		var msgConfig={
				messageType:typeTag,
				tag:typeTag,
				//IBPSeqNum:'Y',
				editMode:'editable'
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingleWindow(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler : function() {
					clickCreateCasehBtn(this,e);
				}
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[Ecp.components.paymentGrid]);
		messageViewWindow.show();
	});
}

function showCreateCaseWithMT192InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWinByFin(btn,e,swift_mt_192);
}

function showCreateCaseWithMT195InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWinByFin(btn,e,swift_mt_195);
}

function showCreateCaseWithMT196InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWinByFin(btn,e,swift_mt_196);
}

function showCreateCaseWithMT292InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWinByFin(btn,e,swift_mt_292);
}

function showCreateCaseWithMT295InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWinByFin(btn,e,swift_mt_295);
}

function showCreateCaseWithMT296InPaymentListWin(btn,e)
{
	showCreateCaseInPaymentListWinByFin(btn,e,swift_mt_296);
}

function showCreateCaseInPaymentListWinByFin(btn,e,typeTag){
	var paymentRecord=Ecp.components.paymentGrid.grid.getSelectedRecord();
	if(paymentRecord['related']!='')
	{
		Ecp.MessageBox.alert(TXT.payment_canNotCreateCase);
		return;
	}
	
	var windowObj = {};
	if (typeof create_fin_in_payment_list_window_config != 'undefined')
		windowObj['config'] = create_in_payment_list_window_config;
	if (typeof create_fin_in_payment_list_window_buttons != 'undefined')
		windowObj['buttons'] = create_in_payment_list_window_buttons;
	
	windowObj['items']=[{}];
	if (typeof create_fin_in_payment_list_tabPanel_config != 'undefined')
		windowObj['items'][0]['config'] = create_in_payment_list_tabPanel_config;
	if (typeof create_fin_in_payment_list_tabPanel_items != 'undefined')
		windowObj['items'][0]['items'] = create_in_payment_list_tabPanel_items;
		
	var params={
			cmd:'message',
			action:'getFinMessageLayout',
			messageType:typeTag,
			f21:paymentRecord['referenceNum']
	};
	Ecp.Ajax.requestNoDecode(params, function(result) {
		// global variable defined in payment.htm
		messageBody=result;
		var msgConfig={
				messageType:typeTag,
				editMode:'editable',
				tag:typeTag,
				//IBPSeqNum:'Y',
				fin:'fin',
				currentSender:Ecp.userInfomation['instBic']
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingleFinWindow(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler : function() {
					clickCreateCasehBtnByFin(this,e);
				}
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[Ecp.components.paymentGrid]);
		messageViewWindow.show();
	});
}

function clickCreateCasehBtn(btn,e)
{
	var msgViewWin=btn['ecpOwner']['ecpOwner'];
	var tag=msgViewWin['msgConfig']['tag'];
	//var 
	msgViewWin.validate(tag,function(){
		var msgBody=msgViewWin.getMessageBody();
		var params={
				cmd:'message',
				action:'createCaseByXmlMessage',
				paymentId:Ecp.components.paymentGrid.grid.getSelectedId(),
				messageTypeTag:tag,
				messageBody:msgBody
		};
		Ecp.Ajax.request(params, function(result) {
			if (result.result == 'failure') {
				var msg;
				if (result.message == 'paymentDeleted')
					msg = TXT.case_message_paymentDeleted;
				else if (result.message == 'hasRelated')
					msg = TXT.payment_containCase;
				else
					msg=TXT[result.message];
				Ecp.MessageBox.alert(msg);
				return;
			}
			msgViewWin.window.window.hide();
			msgViewWin.notifyAll('afterRelated');
		});
	});
}

function clickCreateCasehBtnByFin(btn,e){
	var msgViewWin=btn['ecpOwner']['ecpOwner'];
	var messageType=msgViewWin['msgConfig']['tag'];
	var receiver=msgViewWin.items[0]['ecpOwner']['ecpOwner'].getReceiverForFinTemplate();
	msgViewWin.validateFin(messageType,Ecp.userInfomation['instBic'],receiver,function(result){
		if(result.message=='isEniUser'){
			Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
				clickCreateCasehBtnByFinInner(messageType,receiver,msgViewWin);
			});
		}
		else
				clickCreateCasehBtnByFinInner(messageType,receiver,msgViewWin);
	});
}

function clickCreateCasehBtnByFinInner(messageType,receiver,msgViewWin){
	var msgBody=Ecp.MessageViewTabPanel.replaceSuffix(msgViewWin.getMessageBody());
	var params={
			cmd:'message',
			action:'createEIMessageTypeFin',
			pid:Ecp.components.paymentGrid.grid.getSelectedId(),
			byPayments:'Y',
			messageType:messageType,
			body:msgBody,
			receiver:receiver
	};
	Ecp.Ajax.request(params, function(result) {
	if (result.result == 'failure') {
			var msg;
			if (result.message == 'paymentDeleted')
				msg = TXT.case_message_paymentDeleted;
			else if (result.message == 'hasRelated')
				msg = TXT.payment_containCase;
			else
				msg=TXT[result.message];
			Ecp.MessageBox.alert(msg);
			return;
		}
	msgViewWin.window.window.hide();
		msgViewWin.notifyAll('afterRelated');
	});
}

function showMsgTmpWinWithPaymentList(){
	//just temprorary
	var type='fin';
	var record=Ecp.components.paymentGrid.grid.getSelectedRecord();
	if(!record)
		return;
	if(record['related']!=''){
		Ecp.MessageBox.alert(TXT.payment_alreadyRelated);
		return;
	}
	if(Ecp.components['messageTemplateWin'])
		delete Ecp.components['messageTemplateWin'];
	var win=Ecp.MessageTemplateListWin.createWindow({
		cusMsgTmpGrid:{},
		cusMsgTmpWin:{},
		type:type
	});
	win.grid.show();
	win['dataBus']={
		pid:record['id'],
		cid:''
	};
	win.window.window.show();
}

function showPrivateMsgTmpWinWithPaymentList(){
	var record=Ecp.components.paymentGrid.grid.getSelectedRecord();
	if(!record)
		return;
	if(record['related']!=''){
		Ecp.MessageBox.alert(TXT.payment_alreadyRelated);
		return;
	}
	var win=Ecp.MessageTemplateListWin.createPrivateMsgListWindow({
		cusPrivaeMsgTmpGrid:{},
		cusPrivaeMsgTmpWin:{}
	});
	win.grid.show();
	win['dataBus']={
		pid:record['id'],
		cid:''
	};
	win.window.window.show();
}

function showPaymentRelateCaseWin()
{
	var record=Ecp.components.paymentGrid.grid.getSelectedRecord();
	if(record['cid']!='')
		showCaseDetailByIdWin(record['cid'],[Ecp.components.paymentGrid],true,ensureWinType);
	else
		Ext.MessageBox.alert(TXT.common_hint,TXT.payment_noRelated);
}
function showTransformFinContentWin(bt, e){
	transformFinContent(this.dataBus.body);
}
function transformFinContent(body){
	if(body==null||body.trim()==""){
		Ecp.MessageBox.alert(TXT.message_fin_content_is_not_empty);
		return 
	}
	var params={
			cmd:'telegraphCode',
			action:'translateFromCodeToChinese',
			content:body
	};
	Ecp.Ajax.request(params, function(result) {
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT.message_fin_content_is_not_empty);
			return;
		}else{
			Ecp.MessageFinContentTransWin.createWindow({"formFun":function (obj) {
				Ecp.components.messageFinContentTransformForm = obj;
				obj.items[0].value=result.content;
				obj.items[1].value=result.simpleChinese;
				obj.items[2].value=result.traditionalChinese;
			}, "winFun":function (obj) {
				Ecp.components.messageFinContentTransWin = obj;
			}}).show();
		}
	});
}