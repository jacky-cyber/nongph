com.felix.eni.module.message.MessageFinContentWindow = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.Window);
		
	this.setConfig( {
		title :TXT.eni_message,
		width : 580,
		height :460,
		autoScroll :false,
		layout :'border',
		border :false,
		closeAction :'close',
		minimizable :false,
		maximizable :false,
		resizable :false,
		modal :false,
		shadow :false,
		layoutConfig : { animate :false },
		buttonAlign :'center'
	} );
	
	var form = new com.felix.eni.module.message.MessageFinContentFormPanel();
	this.setItems( [form] );
	
	this.dataBus=null;
	
	this.setTaskFormValidator = function(code){
		var textArea = form.items.itemAt(5)
		if( code==1 || code==3 ) {
			form.items.itemAt(1).setReadOnly(true);
			form.items.itemAt(3).setReadOnly(true);
			textArea.setReadOnly(true);
			textArea['regex']=undefined;
			textArea['regexText']=undefined;
		}else{
			form.items.itemAt(1).setReadOnly(false);
			form.items.itemAt(3).setReadOnly(false);
			textArea.setReadOnly(false);
			if( Ext.isIE )
				textArea['regex']=/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{1,46}(\r\n[0-9a-zA-Z/\-\?\(\)\.,'\+ ][0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,49}){0,33}$/;
			else
				textArea['regex']=/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{1,46}(\n[0-9a-zA-Z/\-\?\(\)\.,'\+ ][0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,49}){0,33}$/;
			textArea['regexText']=TXT.template_format_error;
		}
	}
	
	this.update = function(src,eventName){
		if(eventName=='integrateMsgIntoCase')
			this.getExtWindow().hide();
		if(eventName=='receiveMsgProEnd')	
			this.getExtWindow().hide();
		if(eventName=='afterCreateCase')	
			this.getExtWindow().hide();
	}
}

Ecp.MessageFinContentWin.prototype.initForm=function(obj){
	this.items[0].init();
	if(obj['handler']!=undefined)
		this.items[0].handlerWidgetConfig(obj['handler']);
	this.items[0].setMessageFinContentFormConf(obj['msgConf']);
	this.items[0].customization(obj['cusObj']);
	this.items[0]=this.items[0].render();
}

Ecp.MessageFinContentWin.prototype.customization=function(obj){
	this.cusObj=obj;
}

//static method
com.felix.eni.module.message.MessageFinContentWindow.createMsgWinInReceiveProc = function( obj, observer, careTxt ) {
	if( !Ecp.components['msgWinInReceiveProc'] ) {
		var win=new Ecp.MessageFinContentWin();
		win.initForm({
			msgConf:{
				//readOnlyFlag:false,
				needValidate:false
			},
			cusObj:obj['cusMsgContentForm'],
			handler:obj['msgContentFormHandler']
		});
		win.setMessageFinContentConf({
			closeAction:'hide',
			modal:true
		});
		win.setButtons([{
				text:careTxt,
				handler:setCareForTaskWin,
				scope:win
			},{
				text:TXT.common_search,
				handler:showCaseSelectWinAction,
				scope:win
			},{
				text:TXT.case_search_case,
				handler:showPaymentSearchWin,
				scope:win
			},{
				text:TXT.message_setUnrelated,
				handler:setMsgUnrelated,
				scope:win
			},{
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
					this['ecpOwner'].window.hide();
				}
			}
		]);
		/*if(obj['msgContentWinHandler']!=undefined)
			win.handleWidgetConfig(obj['msgContentWinHandler']);*/
		win.handleWidgetConfig(function(obj){
			obj['config']['id']='mtMsgReceiverWin';
			obj['config']['title']=TXT.eni_message_fin;
		});
		win.eventConfig={
			'beforeshow':function(){
				this['ecpOwner']['items'][0]['ecpOwner'].reset();
				win.dataBus=null;
			}
		};
		win.customization(obj['cusMsgContentWin']);
		win.addObservers(observer);
		win.render();
		Ecp.components['msgWinInReceiveProc']=win;
	}
	return Ecp.components['msgWinInReceiveProc'];
}

com.felix.eni.module.message.MessageFinContentWindow.createMsgWinInTaskProc = function(obj,observer,record){
	if(!Ecp.components['msgWinInTaskProc']){
		var taskCommentForm=new Ecp.TaskCommentForm();
		taskCommentForm.customization(obj['cusCommentForm']);
		var win=new Ecp.MessageFinContentWin();
		var msgConf=null;
		var buttons=[
			{
				text:TXT.task_detail,
				handler:showTaskDetailWin,
				scope:win
			},{
				text:TXT.task_findCase,
				handler:showCaseInTaskDetailWin,
				scope:win
			},{text:TXT.message_transform,handler:function(bt){
												var body = win.items[0].items.items[5].value;
												transformFinContent(body);
											},
						scope:win
					},{
				text:TXT.common_btnClose,
				handler:function(){
					this['ecpOwner'].window.close();
				}
			}	
		];
		if(obj['code']==1){
			buttons.splice(0,0,{
				text :TXT.task_reject,
				handler : clickTaskRejectBtn,
				scope:win
			});
			buttons.splice(0,0,{
				text :TXT.task_approval,
				handler : clickTaskApproveBtn,
				scope:win
			});
			msgConf={
				needValidate:false
			};
		}else if(obj['code']==2){
			buttons.splice(0,0,{
				text :TXT.task_cancel,
				handler : clickTaskCancelBtn,
				scope:win
			});
			buttons.splice(0,0,{
				text :TXT.task_modify,
				handler : clickTaskModifyBtn,
				scope:win
			});
			//var renderedFormItems=win['items'][0]['items'];
			msgConf={
				readOnlyFlag:false,
				needValidate:true
			};
			/*renderedFormItems.itemAt(1).setReadOnly(false);
			renderedFormItems.itemAt(3).setReadOnly(false);
			renderedFormItems.itemAt(5).setReadOnly(false);*/
		}else if(obj['code']==3){
			buttons.splice(0,0,{
				text :TXT.task_cancel,
				handler : clickTaskCancelBtn,
				scope:win
			});
			buttons.splice(0,0,{
				text :TXT.task_run,
				handler : clickTaskRunBtn,
				scope:win
			});
			msgConf={
				readOnlyFlag:true
			};
		}else if(obj['code']==4){
			buttons.splice(0,0,{
				text :TXT.task_run,
				handler : clickTaskRunBtn,
				scope:win
			});
			msgConf={
				readOnlyFlag:true
			};
		}
		win.initForm({
			msgConf:msgConf,
			cusObj:obj['cusMsgContentForm'],
			handler:obj['msgContentFormHandler']
		});
		win.setMessageFinContentConf({
			closeAction:'close',
			modal:true,
			height:580
		});
		win.handleWidgetConfig(function(obj){
			obj['config']['id']='mtMsgInTaskList';
			obj['config']['title']=TXT.eni_message_fin;
			if(record.noticeTimes>0){
				obj['config']['title']+=' ('+TXT.case_urge_deal_outland+': '+TXT.case_urge_deal_outland_notice_count1+record.noticeTimes+TXT.case_urge_deal_outland_notice_count2+')';
				obj['config']['modal']=true;
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
			}
			obj['items'].push(taskCommentForm.render());
		});
		win.setButtons(buttons);
		
		win.eventConfig={
			'beforeshow':function(){
				/*this['ecpOwner']['items'][0]['ecpOwner'].reset();
				this['ecpOwner']['items'][1]['ecpOwner'].reset();*/
				win.dataBus=null;
			}
		};
		win.customization(obj['cusMsgContentWin']);
		win.addObservers(observer);
	/*		
	var finForm =win.items[0].form.items.items;
		finForm.splice(0,0,{
			fieldLabel :TXT.message_20,
			id :'references',
			name :'reference',
			allowBlank :false,
			readOnly :true
		});
		if(record['noticeTimes']&&record['noticeTimes']>0){
		}
		*/
		win.render();
		Ecp.components['msgWinInTaskProc']=win;
	}else{
		var window=Ecp.components['msgWinInTaskProc'].window.window;
		//var renderedFormItems=window['items'].itemAt(0)['items'];
		if(obj['code']==1){
			window['buttons'][0].setText(TXT.task_approval);
			window['buttons'][0].handler=clickTaskApproveBtn;
			window['buttons'][1].setText(TXT.task_reject);
			window['buttons'][1].handler=clickTaskRejectBtn;
			/*renderedFormItems.itemAt(1).setReadOnly(true);
			renderedFormItems.itemAt(3).setReadOnly(true);
			renderedFormItems.itemAt(5).setReadOnly(true);*/
		}else if(obj['code']==2){
			window['buttons'][0].setText(TXT.task_modify);
			window['buttons'][0].handler=clickTaskModifyBtn;
			window['buttons'][1].setText(TXT.task_cancel);
			window['buttons'][1].handler=clickTaskCancelBtn;
			/*renderedFormItems.itemAt(1).setReadOnly(false);
			renderedFormItems.itemAt(3).setReadOnly(false);
			renderedFormItems.itemAt(5).setReadOnly(false);*/
		}else if(obj['code']==3){
			window['buttons'][0].setText(TXT.task_run);
			window['buttons'][0].handler=clickTaskRunBtn;
			window['buttons'][1].setText(TXT.task_cancel);
			window['buttons'][1].handler=clickTaskCancelBtn;
		}else if(obj['code']==4){
			window['buttons'][0].setText(TXT.task_run);
			window['buttons'][0].handler=clickTaskRunBtn;
		}
		Ecp.components['msgWinInTaskProc'].setTaskFormValidator(obj['code']);
	}
	return Ecp.components['msgWinInTaskProc'];
}

com.felix.eni.module.message.MessageFinContentWindow.createSinglePaymentMessageWin=function(obj){
	if(!Ecp.components['paymentInReceiveProc']){
		var win=new Ecp.MessageFinContentWin();
		win.initForm({
			msgConf:{
					needValidate:false
				},
			cusObj:obj['cusForm']
		});
		win.setMessageFinContentConf({
				closeAction:'hide',
				modal:true
			});
		win.setButtons([
			{
				text:TXT.message_print,
				handler:printMsg,
				scope:win
			},{
				text:TXT.common_btnClose,
				handler:function(){
					this['ecpOwner'].window.hide();
				}
			}
		]);
		
		win.handleWidgetConfig(function(win){
			win['config']['id']='paymentInReceiveProc';
			win['config']['title']=TXT.payment_msg;
		});
		win.eventConfig={
			'beforeshow':function(){
				this['ecpOwner']['items'][0]['ecpOwner'].reset();
			}
		};
		win.customization(obj['cusWin']);
		win.render();
		Ecp.components['paymentInReceiveProc']=win;
	}
	return Ecp.components['paymentInReceiveProc'];
}

com.felix.eni.module.message.MessageFinContentWindow.createSingleFinMsgWinToGenCase=function(obj,observers){
	if(!Ecp.components['finWinToGenCaseInPayment']){
		var win=new Ecp.MessageFinContentWin();
		win.initForm({
			msgConf:{
				readOnlyFlag:obj['templateNameIsRefunds']===undefined?false:true,
				needValidate:true,
				needValidateVtype:obj['isFree']===undefined?true:undefined
			},
			cusObj:obj['cusMsgContentForm'],
			handler:obj['msgContentFormHandler']
		});
		win.setMessageFinContentConf({
			closeAction:'hide',
			modal:true
		});
		win.setButtons([{
				text:TXT.common_btnCreate,
				handler:obj['createFunction'],
				scope:win
			},{
				text:TXT.common_btnClose,
				handler:function(){
					this['ecpOwner'].window.hide();
				}
			}
		]);
		win.handleWidgetConfig(function(obj){
			obj['config']['id']='mtMsgToGenCase';
			obj['config']['title']=TXT.eni_message_fin;
		});
		win.eventConfig={
			'beforeshow':function(){
				this['ecpOwner']['items'][0]['ecpOwner'].reset();
				win.dataBus=null;
			}
		};
		win.customization(obj['cusMsgContentWin']);
		win.addObservers(observers);
		win.render();
		Ecp.components['finWinToGenCaseInPayment']=win;
	}else{
		//change observers
		//change buttonHandler
		var win=Ecp.components['finWinToGenCaseInPayment'];
		//alert(win.window.window.constructor);
		var createButton=win.window.window.buttons[0];
		if(createButton.handler!=obj['createFunction'])
			createButton.handler=obj['createFunction'];
		if(obj['isFree']!==undefined)
			win.items[0].items.itemAt(5)['vtype']=undefined;
		else
			win.items[0].items.itemAt(5)['vtype']='vailAreaStringFin';
		//if is freeTemplate change needValidate
	}
	return Ecp.components['finWinToGenCaseInPayment'];
}