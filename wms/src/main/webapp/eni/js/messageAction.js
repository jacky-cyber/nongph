var showSearchMessageWin = function(btn, e) {
	var windowObj = {};
	if (typeof message_search_window_config != 'undefined')
		windowObj['config'] = message_search_window_config;
	if (typeof message_search_window_buttons != 'undefined')
		windowObj['buttons'] = message_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof message_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = message_search_form_config;
	if (typeof message_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = message_search_form_buttons;
	if (typeof message_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = message_search_form_items;

	// window
	var messageSearchWin = Ecp.MessageSearchWindow.createSingleSearchWindow(windowObj);	
	//messageSearchWin.getItem(0).reset();
	messageSearchWin.getItem(0).setValues({type:'',ioFlag:'',isRead:'',swiftStatus:''});
//	messageSearchWin.getItem(0).setValues({ioFlag:''});
//	messageSearchWin.getItem(0).setValues({isRead:''});
//	messageSearchWin.getItem(0).setValues({swiftStatus:''});
	messageSearchWin.show();
}

var showSearchNotwithMessageWin = function(btn, e) {
	var windowObj = {};
	if (typeof message_search_window_config != 'undefined')
		windowObj['config'] = message_search_window_config;
	if (typeof message_search_window_buttons != 'undefined')
		windowObj['buttons'] = message_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof message_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = message_search_form_config;
	if (typeof message_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = message_search_form_buttons;
	if (typeof message_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = message_search_form_items;

	// window
	var Win = Ecp.DeleteMessageSearchWindow.createSingleSearchWindow(windowObj);
	//Win.getItem(0).reset();
	Win.show();
}

var showCaseInMessageListWin = function(btn, e) {
	var cid=Ecp.components.messageGrid.grid.getSelectedRecord()['cId'];
	showCaseDetailByIdWin(cid,[Ecp.components.messageGrid],true,ensureWinType);
}

// ---------------------------------------------------
var showCopyMessageWinInMessageList = function(btn, e) {
	var record=Ecp.components.messageGrid.grid.getSelectedRecord();
	if(!record)
		return;
	if(record['swiftStatus']!=='W' && record['swiftStatus']!=='NAK'){
		Ecp.MessageBox.alert(TXT.message_can_not_copy);
		return;
	}
	if(record['type']=='MT'){
		if(record['messageType'].indexOf('99')!=-1||record['ioFlag']=='I')
			showMTMessageWinForCopy(record,'inMList',Ecp.components.messageGrid);
		else
			showFinMessageWinForCopy(record,'inMList',Ecp.components.messageGrid);
	}else if(record['type']=='MX'){
		showMXMessageWinForCopy(record,'inMList',Ecp.components.messageGrid);
	}
}

function showCopyMessageWinInMessageListInCD(){
	var record=Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages').grid.getSelectedRecord();
	if(!record)
		return;
	if(record['swiftStatus']!=='W' && record['swiftStatus']!=='NAK'){
		Ecp.MessageBox.alert(TXT.message_can_not_copy);
		return;
	}
	if(record['type']=='MT'){
		if(record['messageType'].indexOf('99')!=-1||record['ioFlag']=='I')
			showMTMessageWinForCopy(record,'inMList',reloadPartCaseDetail,true);
		else
			showFinMessageWinForCopy(record,'inMList',reloadPartCaseDetail,true);
	}else if(record['type']=='MX'){
		showMXMessageWinForCopy(record,'inMList',reloadPartCaseDetail,true);
	}
}
// -------------------------------------------------

var showMTMessageWinForCopy=function(record,extraId,component,isFunction){
	var formObj={};
	//customization for paymentMessageShowWindow
	var winObj={};
	//customization for paymentMessageShowWindow
	var winId='mesageShowForCopy'+extraId+record.id;	
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	var win=new Ecp.MessageFinContentWin();
	win.initForm({
		msgConf:{
				readOnlyFlag:false,
				needValidate:true
				//needValidateVtype:obj['isFree']===undefined?true:undefined
			},
		cusObj:formObj
	});
	win.setButtons([
			{
				text:TXT.message_copy,
				handler:copyMTmessage,
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
	});
	win.customization(winObj);
	win.render();
	Ecp.Ajax.request({cmd:'message',action:'getFinMessage',messageId:record.id,flag:'yes'}, function(result) {
			win.window.getItem(0).setValues(result);
			win.window.window.setPosition(undefined,undefined);
			win.show();
			win['dataBus']={
				caseId:record['cId'],
				type:record['messageTypeTag']
			};
			if(isFunction==true)
				win['reloadFunction']=component;
			else	
				win['reloadComponent']=component;
	});
}

function showMXMessageWinForCopy(record,extraId,component,isFunction){
	var windowObj={};
	var winId='xmlMesageShowForCopy'+extraId+record.id;
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	
	var msgConfig={
			id:record['id'],
			tag:record['messageTypeTag'],
			editMode:'editable',
			copyMsg:'copyMsg'
	};
	var messageViewWindow=Ecp.MessageViewWindow.createMessageViewWindow(windowObj,msgConfig,function(obj){
		obj['config']['id']=winId;
		obj.buttons=[{
			text :TXT.message_copy,
			handler : copyMXmessage,
			scope:obj
			},{
			text :TXT.common_btnClose,
				handler : function() {
					var win=this['ecpOwner'];
					win.window.close();
				}
			}];
	});
	messageViewWindow.show();
	messageViewWindow['dataBus']={
		caseId:record['cId']
	};
	if(isFunction==true)
		messageViewWindow['reloadFunction']=component;
	else	
		messageViewWindow['reloadComponent']=component;
}

function showFinMessageWinForCopy(record,extraId,component,isFunction){
	var windowObj={};
	var winId='finMesageShowInCase'+extraId+record.id;
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	
	var msgConfig={
			id:record['id'],
			tag:record['messageType'],
			editMode:'editable',
			fin:'fin'
	};
	var messageViewWindow=Ecp.MessageViewWindow.createMessageViewWindow(windowObj,msgConfig,function(obj){
		obj['config']['id']=winId;
		obj.buttons=[{
			text :TXT.message_copy,
			handler : copyOrgFinMessage,
			scope:obj
			},{
			text :TXT.common_btnClose,
				handler : function() {
					var win=this['ecpOwner'];
					win.window.close();
				}
			}];
	});
	messageViewWindow.show();
	messageViewWindow['dataBus']={
		caseId:record['cId']
	};
	if(isFunction==true)
		messageViewWindow['reloadFunction']=component;
	else	
		messageViewWindow['reloadComponent']=component;
}

function copyMTmessage(){
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
		params['action']='createEIMessageByFinMessage';
		params['type']=this['dataBus']['type'];
		params['isCopy']=true;
		Ecp.Ajax.request(checkIsActiveEniParam,function(eniResult){
			if(eniResult.message=='isEniUser'){
				Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
					Ecp.Ajax.request(params,function(result){
						if(this.reloadComponent!==undefined)
							this.reloadComponent.reloadUpdate();
						else
							this.reloadFunction.call(this,'messages',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'));
						this.window.window.close();
					},this);
				},this);
			}
			else
				Ecp.Ajax.request(params,function(result){
					if(this.reloadComponent!==undefined)
						this.reloadComponent.reloadUpdate();
					else
						this.reloadFunction.call(this,'messages',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'));
					this.window.window.close();
				},this);
		},this);													
	},this);
}

function copyMXmessage(){
	var msgBody=this.getMessageBody();
	var tag=this['msgConfig']['tag'];
	var params={
			cmd:'message',
			action:'createXmlMessageInCase',
			caseId:this['dataBus']['caseId'],
			messageTypeTag:tag,
			messageBody:msgBody
	};
	var msgViewWin=this;
	this.validate(tag,function(){
		Ecp.Ajax.request(params, function(result) {
			msgViewWin.window.window.close();
			if(msgViewWin.reloadComponent!==undefined)
				msgViewWin.reloadComponent.reloadUpdate();
			else
				msgViewWin.reloadFunction.call(msgViewWin,'messages',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'));
		});
	});
}

function copyOrgFinMessage(){
	var msgViewWin=this;
	var tag=msgViewWin['msgConfig']['tag'];
	var receiver=msgViewWin.items[0]['ecpOwner']['ecpOwner'].getReceiverForFinTemplate();
	msgViewWin.validateFin(tag,Ecp.userInfomation['instBic'],receiver,function(result){
		if(result.message=='isEniUser'){
			Ecp.MessageBox.confirm(TXT.payment_isEniUser,function(){
				createOrgFinForCopy(msgViewWin,tag,receiver);
			});
		}
		else
			createOrgFinForCopy(msgViewWin,tag,receiver);
	});
}

function createOrgFinForCopy(msgViewWin,tag,receiver){
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
		msgViewWin.window.window.close();
		if(msgViewWin.reloadComponent!==undefined)
			msgViewWin.reloadComponent.reloadUpdate();
		else
			msgViewWin.reloadFunction.call(msgViewWin,'messages',Ecp.components['caseDeatilWin'].caseAccompanyInfoTab.getServiceComponent('messages'));
	});
}

var showAuditMessageRecordWin = function(btn, e) {
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
	win.getItem(0)['ecpOwner'].searchByMessageId(Ecp.components.messageGrid.grid.getSelectedId());
	win.show();
}

var clickRelateMessageBtn = function(btn, e) {
	var record=Ecp.components.messageGrid.grid.getSelectedRecord();
	if(!record['deleted']){
		Ecp.MessageBox.alert(TXT.message_undeleted);
		return;
	}
	var params={
			cmd:'message',
			action:'setRelated',
			messageId:record['id']
	};
	Ecp.Ajax.request(params, function(result) {
		Ecp.components.messageGrid.dataStore.store.reload();
	});
}

// -------------------------------------------------------
// show error code window
function showXmlNakErrorCodeWin (id) {
		var windowObj = {};
		if (typeof message_info_xmlnak_window_config != 'undefined')
			windowObj['config'] = message_info_xmlnak_window_config;
		if (typeof message_info_xmlnak_window_buttons != 'undefined')
			windowObj['buttons'] = message_info_xmlnak_window_buttons;

		// store object in grid
		windowObj['items']=[{}];
		var cmtStoreObj = {};
	   if (typeof message_info_xmlnak_form_dsConfig != 'undefined')
		cmtStoreObj['cmt_dsConfig'] = message_info_xmlnak_form_dsConfig;
	   if (typeof message_info_xmlnak_form_dsEventConfig != 'undefined')
		cmtStoreObj['cmt_ds_eventConfig'] = message_info_xmlnak_form_dsEventConfig;
	   windowObj['items'][0]['store'] = cmtStoreObj;

	   // grid object in grid
	   var cmtGridObj = {};
	   if (typeof message_info_xmlnak_form_gridConfig != 'undefined')
		cmtGridObj['cmt_gridConfig'] = message_info_xmlnak_form_gridConfig;
	   if (typeof message_info_xmlnak_form_gridCmConfig != 'undefined')
		cmtGridObj['cmt_cmConfig'] = message_info_xmlnak_form_gridCmConfig;
	   if (typeof message_info_xmlnak_form_gridEventConfig != 'undefined')
		cmtGridObj['cmt_grid_eventConfig'] = message_info_xmlnak_form_gridEventConfig;
	   windowObj['items'][0]['grid'] = cmtGridObj;

		var win = Ecp.XmlNakMessageWindow.createSingleWindow(windowObj);
		win.show();
		win.getItem(0)['ecpOwner'].search({
			cmd :'message',
			action :'getNakErrorCode',
			id :id
		});
}

function showFinNakErrorCodeWin (id) {
		var windowObj = {};
		if (typeof message_info_finnak_window_config != 'undefined')
			windowObj['config'] = message_info_finnak_window_config;
		if (typeof message_info_finnak_window_buttons != 'undefined')
			windowObj['buttons'] = message_info_finnak_window_buttons;

		// item's config
		windowObj['items']=[{}];
		if (typeof message_info_finnak_form_config != 'undefined')
			windowObj['items'][0]['config'] = message_info_finnak_form_config;
		if (typeof message_info_finnak_form_buttons != 'undefined')
			windowObj['items'][0]['buttons'] = message_info_finnak_form_buttons;
		if (typeof message_info_finnak_form_items != 'undefined')
			windowObj['items'][0]['items'] = message_info_finnak_form_items;

		var params = {
			cmd :'message',
			action :'getNakErrorCode',
			id :id
		};
		Ecp.Ajax.requestNoDecode(params, function(result) {
			var win = Ecp.FinNakMessageWindow.createSingleWindow(windowObj);
			win.show();
			win.getItem(0).setValue('errorContent',result);
		});
}

var showNakErrorCodeWin = function(btn, e) {
	var record=Ecp.components.messageGrid.grid.getSelectedRecord();
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
// --------------------------------------------------------------------

var clickMessageReadWin = function(btn, e) {
	var record = Ecp.components.messageGrid.grid.getSelectedRecord();
	if(!record){
		return;
	}
	
	if(record['deleted']){
	   Ecp.MessageBox.alert(TXT.message_is_deleted);
	   return;
	}
	
	var params = {
			cmd :'message',
			action :'setNewFlag',
			id :record['id']
	};
	Ecp.Ajax.request(params,function(result){
			if(result.result=='success') {
				Ecp.components.messageGrid.show();
			} else {
				Ecp.MessageBox.alert(TXT.message_NoPower);
			}
		});
}

var selectMessageRecord = function(sm,index,recordData) {
	//var record = Ecp.components.messageGrid.grid.getSelectedRecord();
	var record=recordData.data;
	var status=record['status'];
	Ecp.components.messageToolBar.toolBar.toolBar.findById('messageRead').setDisabled(false);
	if (record['isRead'] == true)
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('messageRead').setText(TXT.message_notRead);
	else
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('messageRead').setText(TXT.message_read);
	// modified by sunyue on 2013.1.15    
	/*if (record['caseStatus'] == 'C')
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('setCare').hide();
	else{
		if(record['careFlag']){
		   Ecp.components.messageToolBar.toolBar.toolBar.findById('setCare').setText(TXT.message_notCare);
		}else{
			Ecp.components.messageToolBar.toolBar.toolBar.findById('setCare').setText(TXT.message_care);
		}
	}*/
	  
	// modified by wujc on 2013.1.15 
	if (record['ioFlag'] == 'O'&& (record['type'] == 'MT' && record['swiftStatus']=='NAK' || record['type'] == 'MX' && record['swiftStatus']!='ACK')&& Ecp.userInfomation.sendToSwift&&status=='A')
	//if (record['ioFlag'] == 'O'&& status=='A' && (record['type'] == 'MT' || record['type'] == 'MX')&&record['internalCode']!='')
	// -------------------------------
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('copyMessage').setDisabled(false);
	else
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('copyMessage').setDisabled(true);		
	   
	if(record['swiftStatus']=='NAK')
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('nakErrorCode').setDisabled(false);
	else
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('nakErrorCode').setDisabled(true);	
	   
	if(record['deleted'])
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('relateMessage').setDisabled(false);
	else
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('relateMessage').setDisabled(true);	
	 
	 if(record['deleted'])
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('messageCase').setDisabled(true);
	else
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('messageCase').setDisabled(false);
	if(record['ioFlag']=='O'&&record['internalCode']!='')
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('auditMessageRecord').setDisabled(false);
	else
	   Ecp.components.messageToolBar.toolBar.toolBar.findById('auditMessageRecord').setDisabled(true);
	if(record['internalCode']==''&&record['ioFlag'] == 'O'){
		Ecp.components.messageToolBar.toolBar.toolBar.findById('messageCase').setDisabled(true);
		Ecp.components.messageToolBar.toolBar.toolBar.findById('messageRead').setDisabled(true);
	}
}

// ----------------------------------------------------
// show message window
var showMessageWin = function(btn, e) {		
	var record=Ecp.components.messageGrid.grid.getSelectedRecord();
	if(record['type']=='TMP')
		showTmptMessageWin(record,btn,e);
	else if(record['type']=='MT'){
		if(record['messageType'].indexOf('99')!=-1||record['ioFlag']=='I')
			showMtMessageWin(record,btn,e);
		else
			showFinMessageWin(record,btn,e);
	}
	else
		showXmlMessageWin(record,btn,e);
}

var showTmptMessageWin=function(record,btn,e){
	alert('tmp');
}

var showMtMessageWin=function(record,btn,e){
	var formObj={};
	var winObj={};
	var winId='mesageShowInMsg'+record.id;	
	if(Ext.WindowMgr.get(winId)!=undefined)
		return;
	var win=new Ecp.MessageFinContentWin();
	win.initForm({
		msgConf:{
				needValidate:false
			},
		cusObj:formObj
	});
	win.setButtons([{
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
	if(record['ioFlag']=='I'&&record['stmtmiscNum']>=1){
		win.buttons.splice(0 ,0,{
				text :TXT.message_forware_cls,
				handler:function(e){
					forwareClsForMessage(e, record);
				},
				scope:win
			});
	}
	win.handleWidgetConfig(function(win){
		win['config']['id']=winId;
		win['config']['title']=TXT.message+' '+record.messageId;
	});
	win.customization(winObj);
	win.render();
	Ecp.Ajax.request({cmd:'message',action:'getFinMessage',messageId:record.id}, function(result) {
			win.window.getItem(0).setValues(result);
			win.window.window.setPosition(undefined,undefined);
			win.show();
			win['dataBus']={
				mid:record.id
			};
	});
}

var showXmlMessageWin = function(record,btn, e){
	var windowObj = {};
	if (typeof view_msg_window_config != 'undefined')
		windowObj['config'] = view_msg_window_config;
	if (typeof view_msg_window_buttons != 'undefined')
		windowObj['buttons'] = view_msg_window_buttons;
	
	windowObj['items']=[{}];
	if (typeof view_msg_tabPanel_config != 'undefined')
		windowObj['items'][0]['config'] = view_msg_tabPanel_config;
	if (typeof view_msg_tabPanel_items != 'undefined')
		windowObj['items'][0]['items'] = view_msg_tabPanel_items;
	
	var msgConfig={
			id:record['id'],
			tag:record['messageTypeTag'],
			editMode:'readOnly'
		};
	var messageViewWindow = Ecp.MessageViewWindow.createMessageViewWindow(windowObj,msgConfig,function(obj){
		obj.buttons=[{
			text :TXT.message_print,
			handler : function() {
				this['ecpOwner']['ecpOwner'].print();
			}
		},{
		text :TXT.common_btnClose,
			handler : function() {
				var win=this['ecpOwner'];
				win.window.close();
			}
		}];
	});
	messageViewWindow.show();
}

var showFinMessageWin = function(record,btn, e){
	var windowObj = {};
	if (typeof view_fin_msg_window_config != 'undefined')
		windowObj['config'] = view_msg_window_config;
	if (typeof view_fin_msg_window_buttons != 'undefined')
		windowObj['buttons'] = view_msg_window_buttons;
	
	windowObj['items']=[{}];
	if (typeof view_fin_msg_tabPanel_config != 'undefined')
		windowObj['items'][0]['config'] = view_msg_tabPanel_config;
	if (typeof view_fin_msg_tabPanel_items != 'undefined')
		windowObj['items'][0]['items'] = view_msg_tabPanel_items;
	
	var msgConfig={
			id:record['id'],
			tag:record['messageType'],
			editMode:'readOnly',
			fin:'fin'
		};
	var messageViewWindow = Ecp.MessageViewWindow.createMessageViewWindow(windowObj,msgConfig,function(obj){
		obj.buttons=[{
			text :TXT.message_print,
			handler : function() {
				this['ecpOwner']['ecpOwner'].print();
			}
		},{
		text :TXT.common_btnClose,
			handler : function() {
				var win=this['ecpOwner'];
				win.window.close();
			}
		}];
		if(record['ioFlag']=='I'&&record['stmtmiscNum']>=1){
			obj.buttons.splice(0 ,0,{
				text :TXT.message_forware_cls,
				handler:function(e){
					forwareClsForMessage(e, record);
				}
			});
		}
	});
	messageViewWindow.show();
}

// ------------------------------------------------------

var clickQueryMessageBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values['cmd']='message';
		values['action']='find';
		var columnModel=Ecp.components.messageGrid.grid.grid.getColumnModel();
		if(columnModel.isHidden(12)){
			columnModel.setHidden(12,false);
			columnModel.setHidden(13,false);
		}
		Ecp.components.messageGrid.search(values);
		win.window.hide();
	}
}

var messageSearchFormReset = function(btn,e){
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	form.reset();
}

var clickQueryDeleteMessageBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values['cmd']='message';
		values['action']='findDeletedIncomingMsg';
		var columnModel=Ecp.components.messageGrid.grid.grid.getColumnModel();
		if(!columnModel.isHidden(12)){
			columnModel.setHidden(12,true);
			columnModel.setHidden(13,true);
		}
		Ecp.components.messageGrid.searchDeletedMsg(values);
		win.window.hide();
	}
}

function searchOutcomingMessage(){
	/*var columnModel=Ecp.components.messageGrid.grid.grid.getColumnModel();
	if(!columnModel.isHidden(12)){
		columnModel.setHidden(12,true);
		columnModel.setHidden(13,true);
	}
	Ecp.components.messageGrid.searchOutcomingMsg({});*/
	var searchWindow=Ecp.DeleteMessageSearchWindow.createSingleSearchWindowFoIBP();
	searchWindow.window.window.show();
}

function relateOutcomingMessage(){
	var record = Ecp.components.messageGrid.grid.getSelectedRecord();
	if(!record)
		return;
	var obj={
		'searchCase':searchCase,
		'showCaseDetailInPaymentLink':showCaseInMessageListWinForIBP,
		'linkCaseInMessage':linkCaseInMessage
	};
	obj['dataBus']={
		id:record.id
	};
	var win=Ecp.LinkCaseAndPaymenWin.createSingleWin(obj);
	win.show();
}

function showCaseInMessageListWinForIBP()
{
	var cid=this['ecpOwner'].getSelectedId();
	showCaseDetailByIdWin(cid,[this['ecpOwner']['ecpOwner']],true,ensureWinType);
	//Ecp.components.linkCaseWinForMessage.hide();
}

function linkCaseInMessage(){
	var cid=this.grid.grid.getSelectedId();
	if(!cid)
		return;
	Ecp.Ajax.request({cmd:'message',action:'relateToCase',id:this['dataBus']['id'],cid:cid}, function(result) {
		if(result['result']=='failure')
			Ecp.MessageBox.alert(TXT[result.message]);
		else{
			Ecp.components.messageGrid.reloadUpdate();
			this.window.window.close();
		}
	},this);
}

function queryMessageForIBP(){
	var searchForm=this.searchMessageFormForIBP;
	var value=searchForm.form.getValues();
	var columnModel=Ecp.components.messageGrid.grid.grid.getColumnModel();
	if(!columnModel.isHidden(12)){
		columnModel.setHidden(12,true);
		columnModel.setHidden(13,true);
	}
	Ecp.components.messageGrid.searchOutcomingMsg(value);
	this.window.window.hide();
}
function forwareClsForMessage(bt,record){
	var stmtmiscNum = record['stmtmiscNum'];
	if(stmtmiscNum){
		if(stmtmiscNum==1){
			var params = {cmd:"message", action:"forwareClsForMessage", messageId:record["id"]};
			Ecp.Ajax.request(params, function (result) {
				if (result.result == "success") {
					Ecp.components.messageGrid.show();
				} else {
					Ecp.MessageBox.alert(TXT.message_NoPower);
				}
			});
		}else if(record['stmtmiscNum']>1){
			Ecp.ForwaredToClsWin.createWindow({"ForwaredToClsFun":function (obj) {
			}},record['cId'],record['id']).show();
		}else{
			alert(TXT.message_to_in_case_no_stmtmisc);
		}
	}else{
		alert(TXT.message_to_in_case_no_stmtmisc);
	}
}
function selectPaymentToCls(messageId){
	var record = Ecp.components.ForwaredToClsPaymentGrid.grid.getSelectedRecord();
	if (!record) {
		return;
	}
	var params = {cmd:"message", action:"forwareClsForMessage", messageId:messageId,paymentId:record['id']};
			Ecp.Ajax.request(params, function (result) {
				if (result.result == "success") {
					Ecp.components.messageGrid.show();
				} else {
					Ecp.MessageBox.alert(TXT.message_NoPower);
				}
	});
}