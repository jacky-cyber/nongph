var loadCaseData=function()
{
	Ecp.components.caseGrid.grid.selectFirstRow();
}

var ASSIGN_XML_TYPE={'camt.027.001.02':'CNR','camt.008.002.02':'RTCP','camt.007.002.02':'RTMP','camt.026.001.02':'UTA','camt.028.001.02':'API','camt.037.001.02':'DAReq','CAMT_032_CCA':'CCA','CAMT_038_CSRReq':'CSRReq'};

// what's this???
function searchCase(){
	//alert('a');
	var params={
		cmd:'case',
		action:'find'
	};
	var json=null;
	var caseGrid=null;
	if(this instanceof Ecp.LinkCaseAndPaymenForm){
		if(!this.form.isValid())
			return;
		json=this.form.getValues();
		caseGrid=this['targetSearchGrid'];
	}
	if(json['amountFrom']==''||json['amountTo']==''){
			json['amountFrom']=0;
			json['amountTo']=0;
		}
	if(json['spendTime']==''){
		json['spendTime']=0;
	}
	//alert('');
	params['json']=Ext.util.JSON.encode(json);
	caseGrid.search(params);
}

var showCreateCaseWith007InCaseListWin=function(btn,e)
{
	showCreateInCaseListWin(btn,e,swift_eni007_tag);
}

var showCreateCaseWith008InCaseListWin=function(btn,e)
{
	showCreateInCaseListWin(btn,e,swift_eni008_tag);
}

var showCreateCaseWith026InCaseListWin=function(btn,e)
{
	showCreateInCaseListWin(btn,e,swift_eni026_tag);
}

var showCreateCaseWith027InCaseListWin=function(btn,e)
{
	showCreateInCaseListWin(btn,e,swift_eni027_tag);
}

var showCreateCaseWith028InCaseListWin=function(btn,e)
{
	showCreateInCaseListWin(btn,e,swift_eni028_tag);
}

var showCreateCaseWith037InCaseListWin=function(btn,e)
{
	showCreateInCaseListWin(btn,e,swift_eni037_tag);
}

var showCreateCaseWith192InCaseListWin=function(btn,e)
{
	showCreateFinInCaseListWin(btn,e,swift_mt_192);
}

var showCreateCaseWith195InCaseListWin=function(btn,e)
{
	showCreateFinInCaseListWin(btn,e,swift_mt_195);
}

var showCreateCaseWith196InCaseListWin=function(btn,e)
{
	showCreateFinInCaseListWin(btn,e,swift_mt_196);
}

var showCreateCaseWith292InCaseListWin=function(btn,e)
{
	showCreateFinInCaseListWin(btn,e,swift_mt_292);
}

var showCreateCaseWith295InCaseListWin=function(btn,e)
{
	showCreateFinInCaseListWin(btn,e,swift_mt_295);
}

var showCreateCaseWith296InCaseListWin=function(btn,e)
{
	showCreateFinInCaseListWin(btn,e,swift_mt_296);
}

var showCreateFinInCaseListWin=function(btn,e,typeTag)
{
	var windowObj = {};
	if (typeof create_fin_in_case_list_window_config != 'undefined')
		windowObj['config'] = create_fin_in_case_list_window_config;
	if (typeof create_fin_in_case_list_window_buttons != 'undefined')
		windowObj['buttons'] = create_fin_in_case_list_window_buttons;
	
	windowObj['items']=[{}];
	if (typeof create_fin_in_case_list_tabPanel_config != 'undefined')
		windowObj['items'][0]['config'] = create_fin_in_case_list_tabPanel_config;
	if (typeof create_fin_in_case_list_tabPanel_items != 'undefined')
		windowObj['items'][0]['items'] = create_fin_in_case_list_tabPanel_items;
	
	var params={
			cmd:'message',
			action:'getFinMessageLayout',
			messageType:typeTag
	};	
	Ecp.Ajax.requestNoDecode(params, function(result){	
		messageBody=result;
		var msgConfig={
				messageType:typeTag,
				tag:typeTag,
				editMode:'editable',
				IBPSeqNum:'Y',
				fin:'fin',
				currentSender:Ecp.userInfomation['instBic']
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingleFinWindow(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler : createCaseWithCaseListForFin
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[Ecp.components.caseGrid]);
		messageViewWindow.show();
	});
}

var showCreateInCaseListWin=function(btn,e,typeTag)
{
	var windowObj = {};
	if (typeof create_in_case_list_window_config != 'undefined')
		windowObj['config'] = create_in_case_list_window_config;
	if (typeof create_in_case_list_window_buttons != 'undefined')
		windowObj['buttons'] = create_in_case_list_window_buttons;
	
	windowObj['items']=[{}];
	if (typeof create_in_case_list_tabPanel_config != 'undefined')
		windowObj['items'][0]['config'] = create_in_case_list_tabPanel_config;
	if (typeof create_in_case_list_tabPanel_items != 'undefined')
		windowObj['items'][0]['items'] = create_in_case_list_tabPanel_items;
	
	var params={
			cmd:'message',
			action:'getMessageLayout',
			messageTypeTag:typeTag,
			caseList:'1'
	};
	Ecp.Ajax.requestNoDecode(params, function(result) {
		// global variable defined in case.htm
		messageBody=result;	
		
		var msgConfig={
				messageType:typeTag,
				tag:typeTag,
				editMode:'editable',
				IBPSeqNum:'Y'
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingleWindow(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler : createCaseWithCaseListForXml
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[Ecp.components.caseGrid]);
		messageViewWindow.show();
	});
}

/*var clickCreateCaseWithPaymentSearchBtn=function(btn,e)
{
	var msgViewWin=btn['ecpOwner']['ecpOwner'];
	var tag=msgViewWin['msgConfig']['tag'];
	msgViewWin.validate(tag,function(){
		var msgBody=msgViewWin.getMessageBody();
		var params={
				cmd:'message',
				action:'getXmlLayout',
				messageTypeTag:tag,
				messageBody:msgBody
		};
		Ecp.Ajax.request(params, function(result) {
			// old coding
			var fieldList = new Array();
			var valueList = new Array();
			fieldList.push("sendBic");
			fieldList.push("receiverBic");
			fieldList.push("F21");
			fieldList.push("currency");
			fieldList.push("amount");
			fieldList.push("valueDate");
			valueList.push(result.sendBic);
			valueList.push(result.receiverBic);
			valueList.push(result.F21);
			valueList.push(result.currency);
			valueList.push(result.amount);
			valueList.push(result.valueDate);
			// create payment window
			
			var s='';
			for(var i in result)
				s+=i+':'+result[i]+'\n';
			alert(s);
		});
	});
}*/

function showCaseDetailWindow(){
	var cid=this['ecpOwner'].getSelectedId();
	showCaseDetailByIdWin(cid,[Ecp.components.caseGrid]);
//	Ecp.Ajax.request({cmd:'case',action:'get',id:cid},function(result){
//		win.dataBus={
//			cid:cid,
//			status:result.status
//		};
//		Ecp.showWaitingWin();
//		win.window.window.show();
//		win.changeLayoutByCaseStatus(result.status);
//		win.window.window.maximize();
//		Ext.MessageBox.hide();
//		win.setValues(result);
//	});
//	var win=Ecp.CaseDetailWindow.createWindow({
//		cusCaseDetailForm:{},
//		cusCaseMessageGrid:{},
//		cusCasePaymentGrid:{},
//		cusCaseHistoryGrid:{},
//		cusCaseCommentsGrid:{},
//		cusCaseAccompanyInfoTab:{},
//		cusCaseDetailWindow:{},
//		cusMessageGridTbar:{},
//		cusPaymentGridTbar:{},
//		cusCommentGridTbar:{}
//	},[Ecp.components.caseGrid]);
}

function showCaseDetailByIdWin(cid,observers,isComplex,hideBelowWinFun){
	if(!cid || cid=='')
		return null;
	Ecp.Ajax.request({cmd:'case',action:'get',id:cid},function(result){
		win.dataBus={
			cid:cid,
			status:result.status
		};
		Ecp.showWaitingWin();
		win.window.window.show();
		win.changeLayoutByCaseStatus(result.status);
		win.window.window.maximize();
		Ext.MessageBox.hide();
		// urge out or in 
		var urgeDealOutland= Ext.getCmp("caseAccompanyMsgToolBar").findById('urgeDealOutland');
		var urgeDealInland= Ext.getCmp("caseAccompanyMsgToolBar").findById('urgeDealInland');
		
		if(urgeDealOutland!=null){
			if(result.statusForUrgeObj &&result.statusForUrgeObj.outStatus){
				urgeDealOutland.show();
			}else{
				urgeDealOutland.hide();
			}
		}
		if(urgeDealInland!=null){
			if(result.statusForUrgeObj && result.statusForUrgeObj.inStatus){
				urgeDealInland.show();
			}else{
				urgeDealInland.hide();
			}
		}
		win.setValues(result);
		//typeName
	});
	var obj={
		cusCaseDetailForm:{},
		cusCaseMessageGrid:{},
		cusCasePaymentGrid:{},
		cusCaseHistoryGrid:{},
		cusCaseCommentsGrid:{},
		cusCaseAccompanyInfoTab:{},
		cusCaseDetailWindow:{},
		cusMessageGridTbar:{},
		cusPaymentGridTbar:{},
		cusCommentGridTbar:{},
		cusCaseHisIBPsnGrid:{}
	};
	if(isComplex===undefined)
		var win=Ecp.CaseDetailWindow.createNoSubDetailWin(obj,observers);
	else
		var win=Ecp.CaseDetailWindow.createComplexDetailWin(obj,observers,hideBelowWinFun);
	return win;
}

function setTbarStatusInMsgGrid(sm,index,record){
	var data=record.data;
	caseStatus=Ecp.components['caseDeatilWin'].dataBus.status;
	var status=data['status'];
	if(caseStatus!='C'){
		var toolBar=sm.grid.getTopToolbar();
		if (data['isRead'] == true)
			toolBar.items.itemAt(4).setText(TXT.message_notRead);
		else
			toolBar.items.itemAt(4).setText(TXT.message_read);
		// modified by wujc on 2013.1.15
		//if (data['ioFlag'] == 'O'&& (data['type'] == 'MT' || data['type'] == 'MX')&& Ecp.userInfomation.sendToSwift&&status=='A')
		if (data['ioFlag'] == 'O'&& (data['type'] == 'MT' && data['swiftStatus']=='NAK' || data['type'] == 'MX' && data['swiftStatus']!='ACK')&& Ecp.userInfomation.sendToSwift&&status=='A')
		// ---------------------------------
			toolBar.items.itemAt(14).setDisabled(false);
		else
			toolBar.items.itemAt(14).setDisabled(true);
		if(data['swiftStatus']=='NAK')
			toolBar.items.itemAt(18).setDisabled(false);
		else
			toolBar.items.itemAt(18).setDisabled(true);
		if ((data['ioFlag']=='I'&&Ecp.userInfomation.sendToSwift == true&&data['type'] == 'MX'))		
			toolBar.items.itemAt(12).setDisabled(false);
		else
			toolBar.items.itemAt(12).setDisabled(true);	
		if(data['ioFlag']=='O')
			toolBar.items.itemAt(16).setDisabled(false);
		else
			toolBar.items.itemAt(16).setDisabled(true);
	}
}

function getPaymentMessageInCaseDetail(){
	var formObj={};
	//customization for paymentMessageShowWindow
	var winObj={};
	//customization for paymentMessageShowWindow
	
	var record=this['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	var winId='paymentShowInCase'+record.id;	
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	var win=new Ecp.MessageFinContentWin();
	win.initForm({
		msgConf:{
				needValidate:false
			},
		cusObj:formObj
	});
	win.setButtons([
			{
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
	win.handleWidgetConfig(function(win){
		win['config']['id']=winId;
		win['config']['title']=TXT.payment_messageId+record.referenceNum;
	});
	win.customization(winObj);
	win.render();
	Ecp.Ajax.request({cmd:'payment',action:'getPaymentMessage',id:record.id}, function(result) {
			win.window.getItem(0).setValues(result);
			win.window.window.setPosition(undefined,undefined);
			win.show();
			win['dataBus']={
				mid:record.id,
				isPayment:true
			};
	});
}

function getMessageInCaseDetail(){
	var record=this['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	if(record['type']=='MT'){
		if(record['messageType'].indexOf('99')!=-1||record['ioFlag']=='I')
			getMTMessageInCaseDetail(record);
		else
			getFinMessageInCaseDetail(record);
	}else if(record['type']=='MX'){
		getXmlMessageInCaseDetail(record);
	}
}

function getMTMessageInCaseDetail(record){
	var formObj={};
	//customization for paymentMessageShowWindow
	var winObj={};
	//customization for paymentMessageShowWindow
	var winId='mesageShowInCase'+record.id;	
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	var win=new Ecp.MessageFinContentWin();
	win.initForm({
		msgConf:{
				needValidate:false
			},
		cusObj:formObj
	});
	win.setButtons([
			{
				text:TXT.message_transform,
				handler:function(bt){
					var body = win.items[0].items.items[5].value;
					transformFinContent(body);
				},
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
	win.handleWidgetConfig(function(win){
		win['config']['id']=winId;
		win['config']['title']=TXT.message+' '+record.messageId;
		// added by wujc on 2013.1.14 for notice_count
		if(record.citicNoticeCount>0)
		{
			win['config']['title']+=' ('+TXT.case_urge_deal_outland+': '+TXT.case_urge_deal_outland_notice_count1+record.citicNoticeCount+TXT.case_urge_deal_outland_notice_count2+')';
			win['config']['modal']=true;
			win.setButtons([
			{
				text:TXT.case_urge_deal_outland,
				handler:clickUrgeDealOutlandBtn,
				scope:win
			},
			{
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
		}
		// -------------------------------------------
	});
	win.customization(winObj);
	if(record['ioFlag']=='I'&&record['stmtmiscNum']>=1){
		win.buttons.splice(0 ,0,{
				text :TXT.message_forware_cls,
				handler:function(e){
					forwareClsForMessage(e, record);
				},
				scope:win
			});
	}
	win.render();
	Ecp.Ajax.request({cmd:'message',action:'getFinMessage',messageId:record.id,messageNoticeCount:record.citicNoticeCount}, function(result) {
			win.window.getItem(0).setValues(result);
			win.window.window.setPosition(undefined,undefined);
			win.show();
			win['dataBus']={
				mid:record.id
			};
	});
}

function getXmlMessageInCaseDetail(record){
	var windowObj={};
	var winId='xmlMesageShowInCase'+record.id;
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	
	var msgConfig={
			id:record['id'],
			tag:record['messageTypeTag'],
			editMode:'readOnly'
	};
	var messageViewWindow=Ecp.MessageViewWindow.createMessageViewWindow(windowObj,msgConfig,function(obj){
		obj['config']['id']=winId;
		obj.buttons=[{
			text :TXT.message_print,
			handler : printMsg,
			scope:obj
			},{
			text :TXT.common_btnClose,
				handler : function() {
					var win=this['ecpOwner'];
					win.window.close();
				}
			}];
			
		// added by wujc on 2013.1.14 for notice_count
		if(record.citicNoticeCount>0)
		{
			//obj['config']['title']+=' ('+TXT.case_urge_deal_outland+': '+TXT.case_urge_deal_outland_notice_count1+record.citicNoticeCount+TXT.case_urge_deal_outland_notice_count2+')';
			obj.setNoticeCount(record.citicNoticeCount);
			obj['config']['modal']=true;
			obj.buttons=[
			{
				text:TXT.case_urge_deal_outland,
				handler:clickUrgeDealOutlandBtn,
				scope:obj
			},
			{
				text:TXT.message_print,
				handler:printMsg,
				scope:obj
			},{
				text:TXT.common_btnClose,
				handler:function(){
					this['ecpOwner'].window.close();
				}
			}
		];
		}
		// -------------------------------------
	});
	messageViewWindow.show();
	messageViewWindow['dataBus']={
				mid:record.id
			};
	
//	var messageViewWindow = new Ecp.MessageViewWindow();
//	messageViewWindow.handleWidgetConfig(function(obj){
//	obj['config']['id']=winId;
//	obj.msgConfig={
//			id:record['id'],
//			tag:record['messageTypeTag'],
//			editMode:'readOnly'
//	};
//	obj.buttons=[{
//			text :TXT.message_print,
//			handler : printMsg,
//			scope:messageViewWindow
//			},{
//			text :TXT.common_btnClose,
//				handler : function() {
//					var win=this['ecpOwner'];
//					win.window.close();
//				}
//			}];
//	});
//	messageViewWindow.customization(windowObj);
//	messageViewWindow.render();
//	messageViewWindow.show();
//	messageViewWindow['dataBus']={
//				mid:record.id
//			};
}

function getFinMessageInCaseDetail(record){
	var windowObj={};
	var winId='finMesageShowInCase'+record.id;
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	
	var msgConfig={
			id:record['id'],
			tag:record['messageType'],
			editMode:'readOnly',
			fin:'fin'
	};
	var messageViewWindow=Ecp.MessageViewWindow.createMessageViewWindow(windowObj,msgConfig,function(obj){
		obj['config']['id']=winId;
		obj.buttons=[{
			text :TXT.message_print,
			handler : printMsg,
			scope:obj
			},{
			text :TXT.common_btnClose,
				handler : function() {
					var win=this['ecpOwner'];
					win.window.close();
				}
			}];
		// add by sunyue on 2013.01.17 for forwardClsForMessage
		if(record['ioFlag']=='I'&&record['stmtmiscNum']>=1){
			obj.buttons.splice(0 ,0,{
					text :TXT.message_forware_cls,
					handler:function(e){
						forwareClsForMessage(e, record);
					},
					scope:messageViewWindow
				});
		}
		// added by wujc on 2013.1.14 for notice_count
		if(record.citicNoticeCount>0)
		{
			//obj['config']['title']+=' ('+TXT.case_urge_deal_outland+': '+TXT.case_urge_deal_outland_notice_count1+record.citicNoticeCount+TXT.case_urge_deal_outland_notice_count2+')';
			obj.setNoticeCount(record.citicNoticeCount);
			obj['config']['modal']=true;
			obj.setButtons([
			{
				text:TXT.case_urge_deal_outland,
				handler:clickUrgeDealOutlandBtn,
				scope:obj
			},
			{
				text:TXT.message_print,
				handler:printMsg,
				scope:obj
			},{
				text:TXT.common_btnClose,
				handler:function(){
					this['ecpOwner'].window.close();
				}
			}
		]);
		}
		// -----------------------------------------------
	});
	messageViewWindow.show();
	messageViewWindow['dataBus']={
				mid:record.id
			};
}

function cancelPaymentRelationShipWithCase(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var paymentGrid=caseDetailWin.caseAccompanyInfoTab.getTab(1);
	var record=paymentGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	Ecp.MessageBox.confirm(TXT.case_message_sure_relatedP,function(){
		Ecp.Ajax.request({cmd:'case',action:'checkCaseOwner',cid:caseDetailWin['dataBus']['cid']},function(result){
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT[result.message]);
				return;
			}else{
				Ecp.Ajax.request({cmd:'payment',action:'deleteRelated',pid:record.id},function(result){
					if (result.result == 'failure') {
						Ecp.MessageBox.alert(TXT.payment_deleted);
						return;
					} else {
						reloadPartCaseDetail('payments',caseDetailWin.caseAccompanyInfoTab.getServiceComponent('paymentMessages'));
					}
				});
			}
		});
	});	
}
//refund messageId and caseId
function applyRefundIncomeWithCase(){
	var messageRecord= Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('paymentMessages').grid.getSelectedRecord();
	if(!messageRecord){
		return;
	}
	var pid = messageRecord.id;
	var cid = Ecp.components['caseDeatilWin']['dataBus']['cid'];
	if(Ecp.userInfomation.isInstHQ){
		Ecp.Ajax.request({cmd:"messageTemplate", action:"checkBackType", templateId:REFUND_ID, messageId:pid,caseId:cid}, function (result) {
			if (result.result == "success") {
				showMessageLayout(REFUND_ID,"template",pid,null,cid);
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
}
function showAddCommentsWin(){
	var formObj={};
	
	var winObj={};

	var win=Ecp.CaseCommentsWindow.createWindow({
		cusCommentForm:formObj,
		cusCommentWin:winObj,
		usage:'add'
	},[Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('comments')]);
	win.window.window.show();
}

function saveComment(){
	if(!this.defaultItemsConfig[0]['ecpOwner'].isValid())
		return;
	var contentParam=this.defaultItemsConfig[0]['ecpOwner'].getValues();
	Ecp.Ajax.request({cmd:'case',action:'createCaseComments',id:Ecp.components['caseDeatilWin']['dataBus']['cid'],content:contentParam['content']},function(result){
		if (result.result == 'success') {
			this.window.window.hide();
			this.notifyAll(this,'commentsAdded');
		} else 
			Ecp.MessageBox.alert(TXT.case_comment_need);
	},this);
}

function showCommentsWin(){
	var formObj={};
	
	var winObj={};
	
	var data=this['ecpOwner'].getSelectedRecord();
	Ecp.Ajax.request({cmd:'case',action:'getCaseComments',id:data.id},function(result){
		win.window.window.show();
		win.window.getItem(0).setValues(result);
	});
	var win=Ecp.CaseCommentsWindow.createWindow({
		cusCommentForm:formObj,
		cusCommentWin:winObj,
		usage:'show'
	},[Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('comments')]);
}

function saveExtrenalCaseInfo(){
	if (Ecp.components['caseDeatilWin']['dataBus']['status'] == 'C') {
		Ecp.MessageBox.alert(TXT.case_save_error);
		return;
	}
	var form=this.form;
	var param={
		cmd:'case',
		action:'editCase',
		cid:Ecp.components['caseDeatilWin']['dataBus']['cid'],
		//remittance:form.findById('remittance'),
		mergeNumber:form.findById('mergeNumber'),
		//account:form.findById('account'),
		caseId:form.findById('caseId'),
		//isSubAccount:form.findById('isSubAccount'),
		caseType:form.findById('caseType'),
		careFlag:form.findById('careFlag')
	};
	Ecp.Ajax.request(param,function(result){
		if (result.result == 'failure')
			Ecp.MessageBox.alert(TXT.case_deleted);
		else{
			form.setValues(result.careInfo[0]);
		}
	});
}

function closeOrReopenCase(){
	var cid=Ecp.components['caseDeatilWin']['dataBus']['cid'];
	if(Ecp.components['caseDeatilWin']['dataBus']['status']=='C'){
		Ecp.MessageBox.confirm(TXT.case_reopen_confirm,function(){
				Ecp.Ajax.request({cmd:'case',action:'reopenCase',id:cid},function(result) {
						Ecp.components['caseDeatilWin'].window.window.hide();
						Ecp.components['caseDeatilWin'].notifyAll('reload');
				});
		});
	}else{
		Ecp.MessageBox.confirm(TXT.case_close_confirm,function(){
			Ecp.Ajax.request({cmd:'message',action:'hasLastRoi',caseId:cid},function(result){
				if(result.isRoi!='yes'){
					Ext.MessageBox.confirm(TXT.common_hint,TXT.case_create_ROI,function(btn){
						if(btn=='yes'){
							showXmlMsgWinInCaseDetail({messageTypeTag:swift_eni029_tag,caseId:cid,roiToClose:true});
						}else
							closeCase('');
					});
				}else{
					closeCase('');
				}
			});
		});
	}
}
function urgeDealInland(){
	Ecp.MessageBox.confirm(TXT.case_message_sure_urgeInland,function() {
		var cid=Ecp.components['caseDeatilWin']['dataBus']['cid'];
		var caseDetailWin=Ecp.components['caseDeatilWin'];
		Ecp.Ajax.request({cmd:'case',action:'urgeDealInland',id:cid},function(result){
			var message=result.message;
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT[common_access_fail]);
				return ;
			}
			reloadPartCaseDetail('messages',caseDetailWin.caseAccompanyInfoTab.getServiceComponent('messages'));
			Ecp.components['caseDeatilWin'].notifyAll('reload');
		});
	});
}
function clickUrgeDealOutlandBtn(){
	Ecp.MessageBox.confirm(TXT.case_message_sure_urgeOutLand,function() {
		var caseDetailWin=Ecp.components['caseDeatilWin'];
		var cid=Ecp.components['caseDeatilWin']['dataBus']['cid'];
		Ecp.Ajax.request({cmd:'case',action:'urgeDealOutland',id:cid},function(result){
			var message=result.message;
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT[message]);
				return ;
			}
			reloadPartCaseDetail('messages',caseDetailWin.caseAccompanyInfoTab.getServiceComponent('messages'));
			Ecp.components['caseDeatilWin'].notifyAll('reload');
		});
	});
}

// added by wujc 2013.1.14
function urgeDealOutland(){
	var msgStore=Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages').grid.grid.getStore();
	var record=msgStore.getAt(msgStore.getCount()-1).data;
	var noticeCount=0;
	if(record.noticeTimes)
		noticeCount=record.noticeTimes;
	record['citicNoticeCount']=noticeCount+1;
	if(record['type']=='MT'){
		if(record['messageType'].indexOf('99')!=-1||record['ioFlag']=='I')
			getMTMessageInCaseDetail(record);
		else
			getFinMessageInCaseDetail(record);
	}else if(record['type']=='MX'){
		getXmlMessageInCaseDetail(record);
	}
}
// ------------------------

function closeCase(messageBody){
	var cid=Ecp.components['caseDeatilWin']['dataBus']['cid'];
	Ecp.Ajax.request({cmd:'case',action:'closeCase',id:cid,body:messageBody},function(result){
		if (result.result == 'failure'&&result.message=='sendFail') {
			Ecp.MessageBox.alert(TXT.common_access_fail);
			return;
		}else if (result.result == 'failure'){
			Ecp.MessageBox.alert(TXT.case_existTask);
			return ;
		}
		Ecp.components['caseDeatilWin'].window.window.hide();
		Ecp.components['caseDeatilWin'].notifyAll('reload');
	});
}

function setMessageReadedFlag(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	if (Ecp.userInfomation.instName != record['assignee']) {
		Ecp.MessageBox.alert(TXT.case_setIsReadError);
		return;
	}
	Ecp.Ajax.request({cmd:'message',action:'setNewFlag',id:record['id']}, function(result) {
			if (result.result == 'failure')
				Ecp.MessageBox.alert(TXT.case_setIsReadError);
			else {
				reloadPartCaseDetail('messages',caseDetailWin.caseAccompanyInfoTab.getServiceComponent('messages'));
			}
		});
}

function cancelMsgRelationShipFromCase(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	if (record['ioFlag'] == 'O') {
		Ecp.MessageBox.alert(TXT.case_message_canotDelRelated);
		return;
	} else if(record['assignee']!=Ecp.userInfomation.instName)
	{
		Ecp.MessageBox.alert(TXT.message_error_assignee);
		return;
	}else {
		var cid=caseDetailWin['dataBus']['cid'];
		Ecp.MessageBox.confirm(TXT.case_message_sure_relatedM,function() {
			Ecp.Ajax.request({cmd:'case',action:'checkCaseOwner',cid:cid},function(result) {
			if (result.result == 'failure') {
				Ecp.MessageBox.alert(TXT[result.message]);
				return;
			} else {
				Ecp.Ajax.request({cmd:'message',action:'delMsgFromCase',cid:cid,mid:record['id']},function() {
					if (result.result == 'failure') {
						Ecp.MessageBox.alert(TXT[result.message]);
					} else {																			
						reloadPartCaseDetail('messages',caseDetailWin.caseAccompanyInfoTab.getServiceComponent('messages'));
					}
				});
			}
		});							
	});
	}
}

function sendNotificationMail(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	Ecp.Ajax.request({cmd:'message',action:'sendEmail',messageId:record['id'],caseId:caseDetailWin['dataBus']['cid']}, function(result) {
		if (result.result=='failure')
			Ecp.MessageBox.alert(TXT.case_canNotSendEmail);
			if (result.result == 'success') {
				Ecp.MessageBox.alert(TXT.case_sendEmailSuccess);
			}
		});
}

function assignCaseToInstInDetail(){
	var id=Ecp.components['caseDeatilWin']['dataBus']['cid'];
	var observers=[];
	if(Ecp.components.caseGrid!==undefined)
		observers=[Ecp.components.caseGrid];
	if(Ecp.components.taskCaseGrid!==undefined)
		observers=[Ecp.components.taskCaseGrid];
	assignCase(id,function(treeWin){
		Ecp.components['caseDeatilWin'].window.window.hide();
		treeWin['ecpOwner'].notifyAll('reload');
	},observers);
}

function showAuditMessageRecordWinInCaseDetail(){
	var windowObj = {};
	if (typeof task_history_window_config != 'undefined')
		windowObj['config'] = task_history_window_config;
	if (typeof task_history_window_buttons != 'undefined')
		windowObj['buttons'] = task_history_window_buttons;
	if (typeof task_history_grid_store != 'undefined')
		windowObj['storeObj'] = task_history_grid_store;
	if (typeof task_history_grid_obj != 'undefined')
		windowObj['gridObj'] = task_history_grid_obj;
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var id=messageGrid['ecpOwner'].getSelectedId();
	if(id)
	{
		var win=Ecp.TaskHistoryWindow.createSingleWindow(windowObj);
		win.getItem(0)['ecpOwner'].searchByMessageId(id);
		win.show();
	}
}

function showAssignXmlMsgSelector(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	if (Ecp.userInfomation.sendToSwift == false){
		Ecp.MessageBox.alert(TXT.message_reassign_no_power);
		return;
	}
	if (record['type']=='MT'||record['ioFlag']=='O'){
		Ecp.MessageBox.alert(TXT.message_reassign_xml_error);
		return;
	}
	messageType = record["messageTypeTag"];
	//messageTypeName=Ecp.getMessageTypeText(messageType);
	if(ASSIGN_XML_TYPE[''+messageType]!=null){
		Ext.MessageBox.confirm(TXT.common_hint,TXT.case_xml_create_payment,
			function(btn) {
				if(btn=='yes'){
					Ecp.Ajax.request({cmd:'message',action:'getOrgnInstId',messageId:record['id']},function(result){
						var win=Ecp.PaymentMessageIntegrateWindow.createSelectWinForAssignXml({
							cusExplicitForm:{},
							cusMutilSearchPanel:{},
							cusSelectGridPanel:{},
							cusTargetGridPanel:{},
							cusSearchWin:{},
							cusKeyWordsForm:{},
							searchForm:{},
							handlerFunction:showReassignXmlWinWithPayment
						});
						win.show();
						win['dataBus']={msgId:record['id'],cid:caseDetailWin['dataBus']['cid'],messageTypeTag:messageType};
						win.setValues({referenceNum:result.instID});
						additionalSearchForBOCOMM.call(win.mutilSearchPanel.getTab(0)['ecpOwner']['ecpOwner'],result.instID);
					});
					}else{
						showReassignXmlWin({messageTypeTag:messageType,msgId:record['id'],caseId:caseDetailWin['dataBus']['cid']});
					}
			});
		}
		else{
			showReassignXmlWin({messageTypeTag:messageType,msgId:record['id'],caseId:caseDetailWin['dataBus']['cid']});
		}
}

function showReassignXmlWinWithPayment(btn,e){
	var record=this['selectGridPanel'].grid.getSelectedRecord();
	if(!record)
		return;
	var cid=record['cid'];
	if(cid!=''&&cid!=Ecp.components['caseDeatilWin']['dataBus']['cid']){
		Ecp.MessageBox.alert(TXT.case_xml_isNotCase);
		return;
	}
	showReassignXmlWin({messageTypeTag:this['dataBus']['messageTypeTag'],msgId:this['dataBus']['msgId'],caseId:this['dataBus']['cid'],paymentId:record['id']});
	this.window.window.hide();
}

function showReassignXmlWin(obj){
	var params={
		cmd:'message',
		action:'getReassignXmlLayout',
		messageId:obj['msgId'],
		caseId:obj['caseId']
	};
	if(obj['paymentId']!=undefined)
		params['paymentId']=obj['paymentId'];
	var windowObj = {};
	if (typeof create_in_payment_list_window_config != 'undefined')
		windowObj['config'] = create_in_payment_list_window_config;
	if (typeof create_in_payment_list_window_buttons != 'undefined')
		windowObj['buttons'] = create_in_payment_list_window_buttons;
	if (typeof create_in_payment_list_window_title != 'undefined')
		windowObj['title'] = create_in_payment_list_window_title;
	if (typeof create_in_payment_list_tabPanel_config != 'undefined')
		windowObj['tabPanelConfig'] = create_in_payment_list_tabPanel_config;
	
	
	Ecp.Ajax.requestNoDecode(params, function(result) {
		if(result=='[NO_ASSIGNEE]'){
			Ecp.MessageBox.alert(TXT.case_canNot_create_message);
			return;
		}
		messageBody=result;
		var msgConfig={
				messageType:obj['messageTypeTag'],
				tag:obj['messageTypeTag'],
				editMode:'editable'
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingleWindow(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler :reassignXmlMessage
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[]);
		messageViewWindow['dataBus']={paymentId:obj['paymentId']};
		messageViewWindow.show();
	});
}

function reassignXmlMessage(btn,e){
	var msgViewWin=btn['ecpOwner']['ecpOwner'];
	var tag=msgViewWin['msgConfig']['tag'];
	msgViewWin.validate(tag,function(){
		var msgBody=msgViewWin.getMessageBody();
		var params={
				cmd:'message',
				action:'createXmlMessageInCase',
				caseId:Ecp.components['caseDeatilWin']['dataBus']['cid'],
				messageTypeTag:tag,
				messageBody:msgBody,
				actionType:'R'
		};
		if(msgViewWin['dataBus']['paymentId']!=undefined)
			params['pid']=msgViewWin['dataBus']['paymentId'];
		Ecp.Ajax.request(params, function(result) {
			msgViewWin.window.window.hide();
			msgViewWin['dataBus']=null;
			reloadPartCaseDetail('messagesAndPayments',[Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'),Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('paymentMessages')]);
		});
	});
}

function createXmlMsgWithSelectedPayment(messageTypeTag){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	Ext.MessageBox.confirm(TXT.common_hint,TXT.case_xml_create_payment,
		function(btn) {
			if(btn=='yes'){
				var win=Ecp.PaymentMessageIntegrateWindow.createSelectWinForAssignXml({
							cusExplicitForm:{},
							cusMutilSearchPanel:{},
							cusSelectGridPanel:{},
							cusTargetGridPanel:{},
							cusSearchWin:{},
							cusKeyWordsForm:{},
							searchForm:{},
							handlerFunction:createXmlInCaseWithSelectedPayment
				});
				win.show();
				win['dataBus']={cid:caseDetailWin['dataBus']['cid'],messageTypeTag:messageTypeTag};
				win.setValues({referenceNum:caseDetailWin.caseDetailForm.form.findById('referenceNum')});
				additionalSearchForBOCOMM.call(win.mutilSearchPanel.getTab(0)['ecpOwner']['ecpOwner'],caseDetailWin.caseDetailForm.form.findById('referenceNum'));
			}else{
				autoSendNotification({messageTypeTag:messageTypeTag});
			}
		});
}

function autoSendNotification(obj){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var allMsgRecord=messageGrid.getStore().getRange();
	var existFinMsgFlag=false;
	for(var i=0;i<allMsgRecord.length;i++){
		if(allMsgRecord[i].get('type')=='MT'){
			existFinMsgFlag=true;
			continue;
		}
	}
	obj['caseId']=caseDetailWin['dataBus']['cid'];
	if(caseDetailWin.caseDetailForm.form.findById('creatorBic')!=Ecp.userInfomation.instBic){
		Ext.MessageBox.confirm(TXT.common_hint,TXT.message_auto_create_noca,function(btn){
			if(btn=='yes'){
				if(existFinMsgFlag==true){
					Ext.MessageBox.alert(TXT.common_hint,TXT.message_not_create_noca);
					return;
				}
				obj['actionType']='R';
		 		showXmlMsgWinInCaseDetail(obj);
			}
			else{
				showXmlMsgWinInCaseDetail(obj);
			} 		
	});
	}else{
		showXmlMsgWinInCaseDetail(obj);
	}
}

function createXmlInCaseWithSelectedPayment(){
	var record=this['selectGridPanel'].grid.getSelectedRecord();
	if(!record)
		return;
	var cid=record['cid'];
	if(cid!=''&&cid!=Ecp.components['caseDeatilWin']['dataBus']['cid']){
		Ecp.MessageBox.alert(TXT.case_xml_isNotCase);
		return;
	}
	autoSendNotification({messageTypeTag:this['dataBus']['messageTypeTag'],pid:record['id']});
	this.window.window.hide();
}

function createCaseWith008InDetail(){
	createXmlMsgWithSelectedPayment(swift_eni008_tag);
}

function createCaseWith007InDetail(){
	createXmlMsgWithSelectedPayment(swift_eni007_tag);
}

function createCaseWith027InDetail(){
	createXmlMsgWithSelectedPayment(swift_eni027_tag);
}

function createCaseWith026InDetail(){
	createXmlMsgWithSelectedPayment(swift_eni026_tag);
}

function createCaseWith028InDetail(){
	createXmlMsgWithSelectedPayment(swift_eni028_tag);
}

function createCaseWith037InDetail(){
	createXmlMsgWithSelectedPayment(swift_eni037_tag);
}

function createCaseWith032InDetail(){
	autoSendNotification({messageTypeTag:swift_eni032_tag});
}

function createCaseWith038InDetail(){
	autoSendNotification({messageTypeTag:swift_eni038_tag});
}

function createCaseWith033InDetail(){
	showXmlMsgWinInCaseDetail({messageTypeTag:swift_eni033_tag,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function createCaseWith035InDetail(){
	showXmlMsgWinInCaseDetail({messageTypeTag:swift_eni035_tag,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}


function showXmlMsgWinInCaseDetail(obj){
	var params={
		cmd:'message',
		action:'getMessageLayout',
		messageTypeTag:obj['messageTypeTag'],
		caseId:obj['caseId']
	};
	if(obj['pid']!=undefined)
		params['paymentId']=obj['pid'];
	var windowObj = {};
	if (typeof create_in_payment_list_window_config != 'undefined')
		windowObj['config'] = create_in_payment_list_window_config;
	if (typeof create_in_payment_list_window_buttons != 'undefined')
		windowObj['buttons'] = create_in_payment_list_window_buttons;
	if (typeof create_in_payment_list_window_title != 'undefined')
		windowObj['title'] = create_in_payment_list_window_title;
	if (typeof create_in_payment_list_tabPanel_config != 'undefined')
		windowObj['tabPanelConfig'] = create_in_payment_list_tabPanel_config;
	
	
	Ecp.Ajax.requestNoDecode(params, function(result) {
		if(result=='[NO_ASSIGNEE]'){
			Ecp.MessageBox.alert(TXT.case_canNot_create_message);
			return;
		}
		messageBody=result;
		var msgConfig={
				messageType:obj['messageTypeTag'],
				tag:obj['messageTypeTag'],
				editMode:'editable'
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingelWinToCreateMsg(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler :createXmlInCaseDetail
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[]);
		messageViewWindow['dataBus']={paymentId:obj['pid'],actionType:obj['actionType'],roiToClose:obj['roiToClose'],caseId:obj['caseId']};
		messageViewWindow.show();
	});
}

function createCaseWith192InDetail(){
	showFinMsgWinInCaseDetail({messageType:swift_mt_192,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function createCaseWith195InDetail(){
	showFinMsgWinInCaseDetail({messageType:swift_mt_195,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function createCaseWith196InDetail(){
	showFinMsgWinInCaseDetail({messageType:swift_mt_196,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function createCaseWith292InDetail(){
	showFinMsgWinInCaseDetail({messageType:swift_mt_292,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function createCaseWith295InDetail(){
	showFinMsgWinInCaseDetail({messageType:swift_mt_295,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function createCaseWith296InDetail(){
	showFinMsgWinInCaseDetail({messageType:swift_mt_296,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function showFinMsgWinInCaseDetail(obj){
	var params={
		cmd:'message',
		action:'getFinMessageLayout',
		messageType:obj['messageType'],
		caseId:obj['caseId'],
		f21:Ecp.components['caseDeatilWin']['caseDetailForm'].form.findById('referenceNum')
	};
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
	
	Ecp.Ajax.requestNoDecode(params, function(result) {
		if(result=='[NO_ASSIGNEE]'){
			Ecp.MessageBox.alert(TXT.case_canNot_create_message);
			return;
		}
		messageBody=result;
		var msgConfig={
				messageType:obj['messageType'],
				editMode:'editable',
				tag:obj['messageType'],
				fin:'fin',
				currentSender:Ecp.userInfomation['instBic']
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingleFinInCaseWindow(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler :createFinInCaseDetail
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[]);
		messageViewWindow['dataBus']={caseId:obj['caseId']};
		messageViewWindow.show();
	});
}

function replyMT192InCaseDetail(){
	showReplyFinMsgWinInCaseDetail({messageType:swift_mt_192,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function replyMT195InCaseDetail(){
	showReplyFinMsgWinInCaseDetail({messageType:swift_mt_195,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function replyMT196InCaseDetail(){
	showReplyFinMsgWinInCaseDetail({messageType:swift_mt_196,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function replyMT292InCaseDetail(){
	showReplyFinMsgWinInCaseDetail({messageType:swift_mt_292,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function replyMT295InCaseDetail(){
	showReplyFinMsgWinInCaseDetail({messageType:swift_mt_295,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function replyMT296InCaseDetail(){
	showReplyFinMsgWinInCaseDetail({messageType:swift_mt_296,caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']});
}

function showReplyFinMsgWinInCaseDetail(obj){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(record['ioFlag']=='I'&&record['type']=='MT'){	
		var params={
			cmd:'message',
			action:'getReplyFinMessageLayout',
			messageType:obj['messageType'],
			messageId:record['id']
		};
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
	
		Ecp.Ajax.requestNoDecode(params, function(result) {
			if(result=='[NO_ASSIGNEE]'){
				Ecp.MessageBox.alert(TXT.case_canNot_create_message);
				return;
			}
			messageBody=result;
			var msgConfig={
				messageType:obj['messageType'],
				editMode:'editable',
				tag:obj['messageType'],
				fin:'fin',
				currentSender:Ecp.userInfomation['instBic']
			};
			var messageViewWindow=Ecp.MessageViewWindow.createSingleFinInCaseWindow(windowObj,msgConfig,function(obj){
				obj.buttons=[{
					text :TXT.commom_btn_add,
					handler : createFinInCaseDetail
				},{
					text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
					}
				}];
			},[]);
			messageViewWindow['dataBus']={caseId:obj['caseId']};
			messageViewWindow.show();
		});
	}else
		Ecp.MessageBox.alert(TXT.message_reply_mt_message_error);
}

function createXmlInCaseDetail(){
	var msgViewWin=this['ecpOwner']['ecpOwner'];
	var tag=msgViewWin['msgConfig']['tag'];
	if(msgViewWin['dataBus']['roiToClose']==undefined){
		msgViewWin.validate(tag,function(){
		var msgBody=msgViewWin.getMessageBody();
		var params={
				cmd:'message',
				action:'createXmlMessageInCase',
				caseId:msgViewWin['dataBus']['caseId'],
				messageTypeTag:tag,
				messageBody:msgBody
		};
		if(msgViewWin['dataBus']['paymentId']!=undefined)
			params['pid']=msgViewWin['dataBus']['paymentId'];
		if(msgViewWin['dataBus']['actionType']!=undefined)
			params['actionType']=msgViewWin['dataBus']['actionType'];	
		Ecp.Ajax.request(params, function(result) {
			msgViewWin.window.window.hide();
			msgViewWin['dataBus']=null;
			reloadPartCaseDetail('messagesAndPayments',[Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'),Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('paymentMessages')]);
		});
	});
	}else{
		msgViewWin.validate(tag,function(){
			closeCase(msgViewWin.getMessageBody());
			msgViewWin.window.window.hide();
		});
	}
}

function createFinInCaseDetail(){
	var msgViewWin=this['ecpOwner']['ecpOwner'];
	var tag=msgViewWin['msgConfig']['tag'];
	var receiver=msgViewWin.items[0]['ecpOwner']['ecpOwner'].getReceiverForFinTemplate();
	msgViewWin.validateFin(tag,Ecp.userInfomation['instBic'],receiver,function(result){
		if(result.message=='isEniUser'){
			Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
				createFinInCaseDetailInner(msgViewWin,tag,receiver);
			});
		}
		else
			createFinInCaseDetailInner(msgViewWin,tag,receiver);
	});
}

function createFinInCaseDetailInner(msgViewWin,tag,receiver){
	var msgBody=Ecp.MessageViewTabPanel.replaceSuffix(msgViewWin.getMessageBody());
		var params={
				cmd:'message',
				action:'replyEIMessageTypeFin',
				caseId:msgViewWin['dataBus']['caseId'],
				messageType:tag,
				body:msgBody,
				receiver:receiver
		};
		Ecp.Ajax.request(params, function(result) {
			msgViewWin.window.window.hide();
			msgViewWin['dataBus']=null;
			reloadPartCaseDetail('messages',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'));
		});
}

function getErrorCode(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	//showXmlNakErrorCodeWin(messageGrid['ecpOwner'].getSelectedId());
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(record)
	{
		var id = record['id'];
		if(record['swiftStatus']=='NAK'){
		   if(record['type']=='MX')
				showXmlNakErrorCodeWin(id);
		   else if(record['type']=='MT')
				showFinNakErrorCodeWin(id);
		   }
		else
			Ecp.MessageBox.alert(TXT.message_not_nak_error);
	}
}

function showReplyMsgListWin(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	if(record['ioFlag']=='I'&&record['type']=='MX'){	
		var win=Ecp.ReplyMsgListWindow.createSingleWindow({
			cusRecommendGrid:{},
			cusAllTypeGrid:{},
			cusReplyTab:{},
			cusRplyMsgListWin:{},
			messageId:record['id']
		});	
		win['dataBus']={
			messageId:record['id'],
			caseId:caseDetailWin['dataBus']['cid']
		};
		win.window.window.show();
	}else{
		Ecp.MessageBox.alert(TXT.message_reply_xml_message_error);
	}
}

function showReplyXml(){
	var selectedGrid=this.replyMsgTab.getActiveTab();
	var record=selectedGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	var params={
		cmd:'message',
		action:'getReplyXmlMessageLayout',
		messageTypeTag:record['tag'],
		caseId:this['dataBus']['caseId'],
		messageId:this['dataBus']['messageId']
	};
	var windowObj = {};
	if (typeof create_in_payment_list_window_config != 'undefined')
		windowObj['config'] = create_in_payment_list_window_config;
	if (typeof create_in_payment_list_window_buttons != 'undefined')
		windowObj['buttons'] = create_in_payment_list_window_buttons;
	if (typeof create_in_payment_list_window_title != 'undefined')
		windowObj['title'] = create_in_payment_list_window_title;
	if (typeof create_in_payment_list_tabPanel_config != 'undefined')
		windowObj['tabPanelConfig'] = create_in_payment_list_tabPanel_config;
	Ecp.Ajax.requestNoDecode(params, function(result) {
		messageBody=result;
		var msgConfig={
				messageType:record['tag'],
				tag:record['tag'],
				editMode:'editable'
			};
		var messageViewWindow=Ecp.MessageViewWindow.createSingelWinToReplyMsg(windowObj,msgConfig,function(obj){
			obj.buttons=[{
				text :TXT.commom_btn_add,
				handler :createReplyXmlMsgInCase,
				scope:this
			},{
				text :TXT.common_btnClose,
					handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
		},[]);
		messageViewWindow['dataBus']={caseId:this['dataBus']['caseId']};
		messageViewWindow.show();
	},this);
	this.window.window.hide();
}

function createReplyXmlMsgInCase(btn,e){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var msgViewWin=btn['ecpOwner']['ecpOwner'];
	var tag=msgViewWin['msgConfig']['tag'];
	msgViewWin.validate(tag,function(){
		var msgBody=msgViewWin.getMessageBody();
		var params={
				cmd:'message',
				action:'replyXmlMessage',
				caseId:msgViewWin['dataBus']['caseId'],
				messageTypeTag:tag,
				messageBody:msgBody
		};
		Ecp.Ajax.request(params, function(result) {
			msgViewWin.window.window.hide();
			msgViewWin['dataBus']=null;
			reloadPartCaseDetail('messages',caseDetailWin.caseAccompanyInfoTab.getServiceComponent('messages'));
		});
	});
}

function reloadPartCaseDetail(type,reloadComponent){
	var cid=Ecp.components['caseDeatilWin']['dataBus']['cid'];
	Ecp.Ajax.request({cmd:'case',action:'get',id:cid},function(result){
		if(type=='payments'){
			var ecpForm=Ecp.components['caseDeatilWin']['caseDetailForm']['form'];
			ecpForm.findFieldById('referenceNum').setValue(result.referenceNum);
			ecpForm.findFieldById('relatedReferenceNum').setValue(result.relatedReferenceNum);
			ecpForm.findFieldById('currency').setValue(result.currency);
			ecpForm.findFieldById('amount').setValue(result.amount);
			ecpForm.findFieldById('valueDate').setValue(result.valueDate);
			//ecpForm.findFieldById('account').setValue(result.account);
			reloadComponent.loadLocalData({payments:result.paymentMessages});
		}
		if(type=='comments'){
			reloadComponent.loadLocalData({comments:result.comments});
		}
		if(type=='messages'){
			var ecpForm=Ecp.components['caseDeatilWin']['caseDetailForm']['form'];
			ecpForm.findFieldById('caseId').setValue(result.caseId);
			ecpForm.findFieldById('creatorBic').setValue(result.creatorBic);
			reloadComponent.loadLocalData({messages:result.messages});
		}
		if(type=='messagesAndPayments'){
			var ecpForm=Ecp.components['caseDeatilWin']['caseDetailForm']['form'];
			ecpForm.findFieldById('caseId').setValue(result.caseId);
			ecpForm.findFieldById('creatorBic').setValue(result.creatorBic);
			reloadComponent[0].loadLocalData({messages:result.messages});
			reloadComponent[1].loadLocalData({payments:result.paymentMessages});
		}
		if(type=='hisIBPsns'){
			reloadComponent.loadLocalData({hisIBPsns:result.hisIBPsns});
		}
		if(type=='refereshMessages'){
			reloadComponent.loadLocalData({messages:result.messages});
		}
	});
}

var showQueryCaseWin=function(btn, e) {
	var windowObj={};
	if(typeof case_query_window_config!='undefined')
		windowObj['config']=case_query_window_config;
	if(typeof case_query_window_buttons!='undefined')
		windowObj['buttons']=case_query_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof case_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = case_search_form_config;
	if (typeof case_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = case_search_form_buttons;
	if (typeof case_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = case_search_form_items;
	
	// window
	var caseSearchWin = Ecp.CaseSearchWindow.createSingleSearchWindow(windowObj);
	//liaozhiling modify 2011-05-03
	//caseSearchWin.getItem(0).reset();
	//caseSearchWin.getItem(0).setValues({isSubAccount:'',remittance:'',status:''});
	caseSearchWin.show();
}

var clickQueryCaseBtn=function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		
		values['amountFrom']=setNumberFieldDefaultValue(values['amountFrom']);
		values['amountTo']=setNumberFieldDefaultValue(values['amountTo']);
		//values['spendTime']=setNumberFieldDefaultValue(values['spendTime']);
		
		var params={};
		params['cmd']='case';
		params['action']='find';
		params['json']=Ext.util.JSON.encode(values);
		
		Ecp.components.caseGrid.search(params);
		win.window.hide();
	}
}

var clickResetQueryCaseBtn=function(btn, e) {
	Ecp.components.caseSearchForm.reset();
}

var showAssignCaseWin=function(btn, e) {
	var data=Ecp.components.caseGrid.grid.getSelectedRecord();
	if(data) {
		var id=Ecp.components.caseGrid.grid.getSelectedId();
		assignCase(id,function(treeWin){
			treeWin['ecpOwner'].notifyAll('reload');
		},[Ecp.components.caseGrid]);
	}
}

function assignCase(id,afterAssignAction,observers,listeners){
	Ecp.Ajax.request({cmd:'case',action:'checkCaseOwner',cid:id},function(result){
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT[result.message]);
			return;
		} else {
			Ecp.Ajax.request({cmd:'case',action:'get',id:id},function(result){
				var isSubAccountValue = result.isSubAccount;
				
				// customization json
				var obj={caseId:id};
				var win=Ecp.InternalCodeSearchWindow.createSingleSearchWithSubAccountWindow(obj,function(treeRecord,treeWin){
					if(treeRecord==null){
						Ecp.MessageBox.alert(TXT.case_institution_choice);
						return;
					}
					Ecp.Ajax.request({cmd:'case',action:'reassign',id:id,institutionid:treeRecord.attributes.id,
									isSubAccount:treeWin.getItem(0).findById('accountAssigne')}, function(result) {
								if (result.result == 'failure') {
									if (result.message == 'self')
										Ecp.MessageBox.alert(TXT.case_self);
									else if (result.message == 'existTask')
										Ecp.MessageBox.alert(TXT.case_existTask);
									return;
								}
								afterAssignAction(treeWin);
							});
					},observers,listeners);
				win.getItem(0).setValue('accountAssigne',isSubAccountValue);
				win.show();
			});
		}
	});	
}

//var showAssignCaseWindow=function(id) {
//	var params=
//	{
//		cmd:'case',
//		action:'get',
//		id:id
//	};
//	
//	Ecp.Ajax.request(params,function(result){
//		var isSubAccountValue = result.isSubAccount;
//		showInstitutionTreeWithSubAccountWin(function(treeRecord,treeWin){
//			alert(treeRecord.attributes.internalCode);
//			alert(treeRecord.attributes.name);
//		});
//		Ecp.components.institutionSelectWithSubAccountWin.getItem(0).setValue('accountAssigne',isSubAccountValue);
//	});
//	showInstitutionTreeWin(function(obj)
//	{ 
//		var newFiledObj=[
//        {
//        	columnWidth :.5,
//			layout :'form',
//			defaultType :'textfield',
//			defaults : {
//				anchor :'85%'
//			},
//			items:[
//	        {
//				xtype:'trigger',
//	        	fieldLabel:TXT.branch_internalCode,
//	        	id: 'internalCode',
//	        	name: 'internalCode',
//	          	triggerClass:'x-form-search-trigger',
//	            allowBlank:true,
//	            onTriggerClick : function() {
//					clickSearchInstitBtn(this);
//				}
//	        }]
//		},
//		{
//        	columnWidth :.5,
//			layout :'form',
//			defaultType :'textfield',
//			defaults : {
//				anchor :'85%'
//			},
//			items: [
//			{
//				xtype :'combo',
//				fieldLabel :TXT.case_subAccount,
//				id :'isSubAccount',
//				name :'isSubAccount',
//				value :'N',
//				store :new Ext.data.SimpleStore(
//				{
//					fields : [ 'label',
//							'value' ],
//					data : [
//					[
//							TXT.case_isSubAccount,
//							'Y' ],
//					[
//							TXT.case_isNotSubAccount,
//							'N' ] ]
//				}),
//				forceSelection :true,
//				displayField :'label',
//				valueField :'value',
//				typeAhead :true,
//				mode :'local',
//				value:isSubAccountValue,
//				triggerAction :'all',
//				selectOnFocus :true,
//				editable :true
//			}]
//		}];
//		var config=
//		{
//			labelAlign: 'left',
//			region: 'north',
//		    labelWidth: 100,
//		    layout:'column',
//			margins:'0 0 2 0',
//			cmargins:'0 0 2 0',
//		    height: 45,
//		    frame:true
//		};
//		var buttons=[{
//			text: TXT.common_btnOK,
//			handler: function(){
//				var win=this['ecpOwner'];
//				var form=win.getItem(0);
//				var treeRecord=win.getItem(1).tree.getSelectionModel().getSelectedNode();
//				var params={
//						cmd:'case',
//						action:'reassign',
//						id:id,
//						institutionid:treeRecord.attributes.id,
//						isSubAccount:form.getValues()['isSubAccount']
//					};
//					
//					Ecp.Ajax.request(params,function(result){
//						if (result.result == 'failure') {
//							if (result.message == 'self')
//								Ext.MessageBox.alert(TXT.common_hint,
//										TXT.case_self);
//							if (result.message == 'existTask')
//								Ext.MessageBox.alert(TXT.common_hint,
//										TXT.case_existTask);
//							return;
//						}
//						else {
//							win.window.hide();
//							form.reset();
//							Ecp.components.caseGrid.notifyAll('assignCaseReload');
//						}
//					});
//			}	
//		}];
//		obj.formJson['items']=newFiledObj;
//		obj.formJson['config']=config;
//		obj.buttons=buttons;
//	})
//}

/*
 * The method is used to set number field's defult value as 0
 */
var setNumberFieldDefaultValue=function(value) {
	if(value == '' || value == null) {
		return 0;
	}
	return value;
}

function ensureWinType(win){
	if(win['ecpOwner']!==undefined&&win['id']!='caseDetailWin'){
		return true;
	}
	else
		return false;
}

function ensureMtWinInTask(win){
	if(win['id']=='mtMsgInTaskList')
		return true;
	else
		return false;
}

function ensureXmlWinInTask(win){
	if(win['id'].indexOf('xmlMsgInTaskList')!=-1)
		return true;
	else
		return false;
}

function ensureMtWinInReceiverProc(win){
	if(win['id']=='mtMsgReceiverWin'||win['id']=='caseSelectInReceiveProcWin')
		return true;
	else
		return false;
}

function ensureXmlWinInReceiverProc(win){
	if(win['id']=='xmlMsgReceiverWin'||win['id']=='caseSelectInReceiveProcWin')
		return true;
	else
		return false;
}

/*function ensureMsgWinInMsgList(win){
	if(win['id']=='xmlMsgReceiverWin'||win['id']=='caseSelectInReceiveProcWin')
		return true;
	else
		return false;
}
*/
/*function ensurePaymentInPaymentList(win){
	if(win['id'].indexOf('xmlMsgInTaskList')!=-1)
		return true;
	else
		return false;
}
*/
function caseDetailBeforeHide(ensureFun){
	//var wins=Ext.WindowMgr.getBy(ensureFun);
	var wins=Ecp.CaseDetailWindow.hiddenRelatedWindow;
	for(var i=wins.length-1;i>=0;i--){
		wins[i].resumeEvents();
		wins[i].show();
	}
	Ecp.CaseDetailWindow.hiddenRelatedWindow=[];
}

function caseDetailBeforeShow(ensureFun){
	var wins=Ext.WindowMgr.getBy(ensureFun);
	var j=0;
	for(var i=0;i<wins.length;i++){
		if(wins[i].isVisible()==false)
			continue;
		wins[i].suspendEvents(false);
		wins[i].hide();
		Ecp.CaseDetailWindow.hiddenRelatedWindow[j++]=wins[i];
	}
}

function showMsgTmpWinWithCaseList(){
	if(Ecp.components['messageTemplateWin']){
		delete Ecp.components['messageTemplateWin'];
	}
	var type='fin';
	var win=Ecp.MessageTemplateListWin.createWindow({
		cusMsgTmpGrid:{},
		cusMsgTmpWin:{},
		type:type,
		caseId:'0'
	});
	win.grid.show();
	win['dataBus']={
		pid:'',
		cid:''
	};
	win.window.window.show();
}

function showPrivateMsgTmpWinWithCaseList(){
	var win=Ecp.MessageTemplateListWin.createPrivateMsgListWindow({
		cusPrivaeMsgTmpGrid:{},
		cusPrivaeMsgTmpWin:{},
		caseId:'0'
	});
	win.grid.show();
	win['dataBus']={
		pid:'',
		cid:''
	};
	win.window.window.show();
}

function showMsgTmpWinWithCaseDetail(){
	//just temprorary
	//var a =  Ecp.components[''].findById('msgGridInCaseDetail');
	//alert('asdf');
	//var messageRecord= Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages').grid.getSelectedRecord();
	//if(!messageRecord){
	//	return;
	//}
	if(Ecp.components['messageTemplateWin'])
		delete Ecp.components['messageTemplateWin'];
	var type='fin';
	var win=Ecp.MessageTemplateListWin.createWindow({
		cusMsgTmpGrid:{},
		cusMsgTmpWin:{},
		type:type,
		caseId:Ecp.components['caseDeatilWin']['dataBus']['cid']
	});
	win.grid.show();
	win['dataBus']={
		pid:'',
		cid:Ecp.components['caseDeatilWin']['dataBus']['cid']
	};
	win.window.window.show();
}

function showPrivateMsgTmpWinWithCaseDetail(){
	var win=Ecp.MessageTemplateListWin.createPrivateMsgListWindow({
		cusPrivaeMsgTmpGrid:{},
		cusPrivaeMsgTmpWin:{}
	});
	win.grid.show();
	win['dataBus']={
		pid:'',
		cid:Ecp.components['caseDeatilWin']['dataBus']['cid']
	};
	win.window.window.show();
}

function showReplyMsgTmpWinWithCaseDetail(){
	//just temprorary
	var type='fin';
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	if(record['ioFlag'] == 'I' && record['type'] == 'MT'){	
		if(Ecp.components['messageTemplateWin']){
			delete Ecp.components['messageTemplateWin'];
		}
		var win=Ecp.MessageTemplateListWin.createWindow({
			cusMsgTmpGrid:{},
			cusMsgTmpWin:{},
			type:type,
			caseId:caseDetailWin['dataBus']['cid']
		});
		win.grid.show();
		win['dataBus']={
			messageId:record['id'],
			cid:caseDetailWin['dataBus']['cid']
		};
		win.window.window.show();
	}else
		Ecp.MessageBox.alert(TXT.message_reply_mt_message_error);
}

function showReplyPrivateMsgTmpWinWithCaseDetail(){
	var caseDetailWin=Ecp.components['caseDeatilWin'];
	var messageGrid=caseDetailWin.caseAccompanyInfoTab.getTab(0);
	var record=messageGrid['ecpOwner'].getSelectedRecord();
	if(!record)
		return;
	if(record['ioFlag'] == 'I' && record['type'] == 'MT'){		
		var win=Ecp.MessageTemplateListWin.createPrivateMsgListWindow({
			cusPrivaeMsgTmpGrid:{},
			cusPrivaeMsgTmpWin:{}
		});
		win.grid.show();
		win['dataBus']={
			messageId:record['id'],
			cid:caseDetailWin['dataBus']['cid']
		};
		win.window.window.show();
	}else
		Ecp.MessageBox.alert(TXT.message_reply_mt_message_error);
}

function createCaseWithCaseListForXml(){
	var msgViewWin=this['ecpOwner']['ecpOwner'];
	var tag=msgViewWin['msgConfig']['tag'];
	var IBPSeqNumValue=msgViewWin.items[1]['ecpOwner']['ecpOwner'].getIBPSeqNumValue();
	msgViewWin.validate(tag,function(){
		var msgBody=msgViewWin.getMessageBody();
		var params={
				cmd:'message',
				action:'getXmlLayout',
				messageTypeTag:tag,
				messageBody:msgBody
		};
		Ecp.Ajax.request(params, function(result) {
			var selectDataMap={referenceNum:result.F21,sendBic:result.sendBic,receiverBic:result.receiverBic,dateFrom:result.valueDate,dateTo:result.valueDate,amountMax:result.amount,amountMin:result.amount,currency:result.currency};
			var win=Ecp.PaymentMessageIntegrateWindow.createWindow({
				cusExplicitForm:{},
				cusMutilSearchPanel:{},
				cusSelectGridPanel:{},
				cusTargetGridPanel:{},
				cusSearchWin:{},
				cusKeyWordsForm:{},
				searchForm:{}
			},[Ecp.components.caseGrid],[msgViewWin]);
			win.show();
			win['dataBus']={msgTag:tag,params:{cmd:'message',action:'createCaseByXmlMessage',messageTypeTag:tag,messageBody:msgBody,IBPSeqNum:IBPSeqNumValue}};
			win.setValues(selectDataMap);
		});
	});
}

function createCaseWithCaseListForFin(){
	var msgViewWin=this['ecpOwner']['ecpOwner'];
	var messageType=msgViewWin['msgConfig']['tag'];
	var IBPSeqNumForm=msgViewWin.items[1]['ecpOwner']['ecpOwner'].getIBPSeqNumValue();
	var receiver=msgViewWin.items[0]['ecpOwner']['ecpOwner'].getReceiverForFinTemplate();
	msgViewWin.validateFin(messageType,Ecp.userInfomation['instBic'],receiver,function(result){
		if(result.message=='isEniUser'){
			Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
				createCaseWithCaseListForFinInner(result,msgViewWin,messageType,IBPSeqNumForm,receiver);
			});
		}
		else
			createCaseWithCaseListForFinInner(result,msgViewWin,messageType,IBPSeqNumForm,receiver);
	});
}

function createCaseWithCaseListForFinInner(result,msgViewWin,messageType,IBPSeqNumForm,receiver){
	var msgBody=Ecp.MessageViewTabPanel.replaceSuffix(msgViewWin.getMessageBody());
		var selectDataMap={referenceNum:result.F21,sendBic:result.sender,receiverBic:result.receiver,dateFrom:result.valueDate,dateTo:result.valueDate,amountMax:result.amount,amountMin:result.amount,currency:result.currency};
		var win=Ecp.PaymentMessageIntegrateWindow.createWindow({
			cusExplicitForm:{},
			cusMutilSearchPanel:{},
			cusSelectGridPanel:{},
			cusTargetGridPanel:{},
			cusSearchWin:{},
			cusKeyWordsForm:{},
			searchForm:{}
		},[Ecp.components.caseGrid],[msgViewWin]);
		win.show();
		win['dataBus']={msgTag:messageType,params:{cmd:'message',action:'createEIMessageTypeFin',messageType:messageType,body:msgBody,IBPSeqNum:IBPSeqNumForm,receiver:receiver,fin:'Y'}};
		win.setValues(selectDataMap);
}

function listGeCloseTimeCases()
{
	self.location='needHandleCase.jsp?locale='+locale+'&closeTime=1';
}

function editIBPSeqNum(){
	if (Ecp.components['caseDeatilWin']['dataBus']['status'] == 'C') {
		Ecp.MessageBox.alert(TXT.case_save_error);
		return;
	}
	var form=this.form.formPanel;
	var columnForm=form.items.itemAt(0);
	var firstColumn=columnForm.items.itemAt(0);
	//var IBPSeqNumFormField=firstColumn.items.itemAt(7).items.itemAt(0);
	var IBPSeqNumFormField=firstColumn.items.itemAt(5).items.itemAt(0);
	if(!(IBPSeqNumFormField.isValid()))
		return;
	var param={
		cmd:'case',
		action:'editIBPSeqNum',
		cid:Ecp.components['caseDeatilWin']['dataBus']['cid'],
		IBPSeqNum:IBPSeqNumFormField.getValue()
	};
	Ecp.MessageBox.confirm(TXT.case_IBP_mod_conf,function(){
		Ecp.Ajax.request(param,function(result){
			if (result.result == 'failure'){
				Ecp.MessageBox.alert(TXT[result.message]);
			}
			else{
				reloadPartCaseDetail('hisIBPsns',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('hisIBPsns'));
				Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.activeTab(4);
			}
		});
	});
}

function refereshMessagesInCase(){
	reloadPartCaseDetail('refereshMessages',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'));
}