/*var privateMessageTemplateDs;
var MessageTemplate = new Object();*/
var MessageTemplate = new Object();
Ext.apply(Ext.form.VTypes, {
	vailNumber : function(value, field) {
		if (field.initialPassField) {
			var number = Ext.getCmp(field.initialPassField);
			return (number.getValue() >= field.getValue());
		}
		return true;
	},
	vailNumberText :TXT.message_vType_number_error
});

Ext.apply(Ext.form.VTypes,{
	vailString: function(value,field){
		if(field.id=='internalCode'+field.initialPassField)
			return true;
		var index=value.search('ENIidentify');
		if(index==-1)
			return true;
		else 
			return false;
	},
	vailStringText : TXT.message_vType_string_error
	}
);

Ext.apply(Ext.form.VTypes,{
	vailAreaStringTmp : function(value,field){
		var index=value.search('ENIidentify');
		if(index==-1)
			return true;
		else 
			return false;
	},
	vailAreaStringTmpText : TXT.message_vType_areaInTmp_error
});

Ext.apply(Ext.form.VTypes,{
	vailBackCur:function(value,field){
		var orgCur=Ext.getCmp('currency'+field.exterLabel).getValue();
		var backCur=Ext.getCmp('backCurrency'+field.exterLabel).getValue();
		var cutCur=Ext.getCmp('cutCurrency'+field.exterLabel).getRawValue();
		if((orgCur!=backCur)||((cutCur!=''&&orgCur!=cutCur))||((cutCur!=''&&orgCur!=cutCur)))
			return false;
		else{
			Ext.getCmp('currency'+field.exterLabel).clearInvalid();
			Ext.getCmp('backCurrency'+field.exterLabel).clearInvalid();
			Ext.getCmp('cutCurrency'+field.exterLabel).clearInvalid();
		}
		return true;	
	},
	vailBackCurText:TXT.template_back_cur
});


MessageTemplate.createStringText = function(field, includeForm, readOnlyFlag,
		exterLabel) {
	var text = new Ext.form.TextField( {
		id :field.name + exterLabel,
		name :field.name + exterLabel,
		fieldLabel :TXT[field.label]?TXT[field.label]:field.label,
		maxLength :field.maxSize,
		vtype : 'vailString',
		initialPassField : exterLabel,
		width :215
	// allowBlank : false
			});
	if (field.isReadOnly != null)
		text.readOnly = true;
	if (field.mandatory != null)
		text.allowBlank = false;
	if (field.regex != null) {
		var reg = new RegExp(field.regex);
		text.regex = reg;
		text.regexText = TXT[field.regexText];
	}
	if (readOnlyFlag == true)
		text.readOnly = true;
	//for testing back temp
	if (field.firstMandatory!=null)
		text.firstMandatory='yes';
	if (field.secondMandatory!=null)
		text.secondMandatory='yes';
	if (field.action!=null){
		eval(field.action);
	}
	includeForm.add(text);
	if (field.value != '')
		includeForm.findById(field.name + exterLabel).setValue(field.value);
}

MessageTemplate.createNumberText = function(field, flag, includeForm,
		readOnlyFlag, exterLabel) {
	var text = new Ext.form.NumberField( {
		id :field.name + exterLabel,
		name :field.name + exterLabel,
		fieldLabel :TXT[field.label]?TXT[field.label]:field.label,
		decimalPrecision :2,
		width :215
	// allowBlank : false
			});
	if (flag == 1)
		text.allowDecimals = false;
	else
		text.allowDecimals = true;
	if (field.relatedVtype != null) {
		text.vtype = 'vailNumber';
		text.initialPassField = field.relatedVtype+exterLabel;
	}
	if (field.isReadOnly != null)
		text.readOnly = true;
	if (field.mandatory != null)
		text.allowBlank = false;
	if (readOnlyFlag == true)
		text.readOnly = true;
	if (field.action!=null){
		eval(field.action);
		//text.validationEvent='blur';
	}
	includeForm.add(text);
	if (field.value != '')
		includeForm.findById(field.name + exterLabel).setValue(field.value);
}

MessageTemplate.createDataText = function(field, includeForm, readOnlyFlag,
		exterLabel) {
	var text = new Ext.form.DateField( {
		id :field.name + exterLabel,
		name :field.name + exterLabel,
		fieldLabel :TXT[field.label]?TXT[field.label]:field.label,
		width :215,
		format :'Y-m-d'
	});
	if (field.isReadOnly != null)
		text.readOnly = true;
	if (field.mandatory != null)
		text.allowBlank = false;
	if (readOnlyFlag == true) {
		text.readOnly = true;
		text.hideTrigger = true;
	}
	includeForm.add(text);
	if (field.value != '')
		includeForm.findById(field.name + exterLabel).setValue(field.value);
}

MessageTemplate.createComboCox = function(field, includeForm, readOnlyFlag,
		exterLabel) {
	var options = field.options;
	var array = new Array();
	var val=new Array();
	var index1 = 0;
	for ( var i = 0; i < options.length; i++) {
		var option = options[i];
		var subArray = new Array();
		subArray[0] = TXT[option.label];
		if(!subArray[0])
			subArray[0]=option.label;
		subArray[1] = option.value;
		val[i]=option.label;
		array[index1] = subArray;
		index1++;
	}
	var store = new Ext.data.SimpleStore( {
		fields : [ 'label', 'value' ],
		data :array
	});
	var combo = new Ext.form.ComboBox(
			{
				fieldLabel :TXT[field.label]?TXT[field.label]:field.label,
				id :field.name + exterLabel,
				name :field.name + exterLabel,
				tpl :'<tpl for="."><div ext:qtip="{label}" class="x-combo-list-item">{label}</div></tpl>',
				forceSelection :true,
				store :store,
				displayField :'label',
				valueField :'value',
				mode :'local',
				triggerAction :'all',
				selectOnFocus :true,
				editable :false,
				validator : function(value){
					if(Ext.getCmp(field.name + exterLabel)!=null||Ext.getCmp(field.name + exterLabel)!=undefined){
						var rawValue=Ext.getCmp(field.name + exterLabel).getRawValue();
						var index=rawValue.search('ENIidentify');
						if(index==-1)
							return true;
						else
							return false;
					}else{
						return true;
					}
				},
				invalidText:TXT.message_vType_string_error,
				width :215
			});
	if (field.isReadOnly != null)
		combo.editable = false;
	if (field.mandatory != null)
		combo.allowBlank = false;
	if (field.isCombo != null) {
		combo.readOnly = false;
		combo.editable = true;
		combo.addListener('render', function(f) {
			f.el.on('keyup', function(e) {
				var raw=f.getRawValue();
				for(var i=0;i<val.length;i++){
					if(val[i]==raw){
						return;
					}	
				}
				f.setValue(raw);
			});
		});
	}
	//
	if (readOnlyFlag == true) {
		/*combo.editable = false;
		combo.triggerAction = 'query';
		// if(isSelect==null)
		combo.hideTrigger = true;*/
		combo.readOnly=true;
	}
	includeForm.add(combo);
	//for testing back temp
	//this two attrbutes should add to form's attr
	includeForm.selectedTag='';
	includeForm.valued=false;
	if (field.action!=null){
		eval(field.action);
	}	
	//should be modified
	if (field.value != ''&&includeForm.valued==false){
			includeForm.findById(field.name + exterLabel).setValue(field.value);
	}
}

MessageTemplate.createTextArea = function(field, includeForm, readOnlyFlag,
		exterLabel) {
	var area = new Ext.form.TextArea( {
		id :field.name + exterLabel,
		name :field.name + exterLabel,
		fieldLabel :TXT[field.label]?TXT[field.label]:field.label,
		width :410,
		height :180,
		vtype : 'vailAreaStringTmp'
	// allowBlank : false
			});
	/*area.regex = new RegExp(field.regex);
	area.regexText = field.regexText;*/
	if (field.isReadOnly != null)
		text.readOnly = true;
	if (field.mandatory != null)
		area.allowBlank = false;
	if (field.regex != null) {
		var regText='';
		regText=field.regex;
		if(!Ext.isIE){
			regText=field.regex.replace(/\\r\\n/g,'\\n');	
		}
		var reg = new RegExp(regText);
		area.regex=reg;
		area.regexText=TXT.template_format_error;
	}
	if (readOnlyFlag == true)
		area.readOnly = true;
	includeForm.add(area);
	if (field.value != '')
		includeForm.findById(field.name + exterLabel).setValue(field.value);
}

MessageTemplate.createCheckBox = function(field, includeForm, readOnlyFlag,
		exterLabel) {
	if (field.isCheckBoxOnly != null) {
		var checkBox = new Ext.form.Checkbox({
			id :field.name + exterLabel,
			name :field.name + exterLabel,
			fieldLabel :TXT[field.label]?TXT[field.label]:field.label
		});
		includeForm.add(checkBox);
		if (field.value != '')
			includeForm.findById(field.name + exterLabel).setValue(field.value);
		if (field.action!=null){
			eval(field.action);
		}		
		if (readOnlyFlag == true)
			includeForm.findById(field.name + exterLabel).disable();
	} else {
		var textWitnCheck;
		if(field.subType==null){
		 	textWitnCheck= new Ext.form.TextField( {
				id :field.name + exterLabel,
				name :field.name + exterLabel,
				labelSeparator :'',
				vtype : 'vailString',
				initialPassField : exterLabel,
				width :215
			});
		}else{
			textWitnCheck= new Ext.form.TextArea( {
				id :field.name + exterLabel,
				name :field.name + exterLabel,
				labelSeparator :'',
				vtype : 'vailAreaStringTmp',
				validationEvent:'blur',
				//validationDelay:750,
				width :280,
				//regex:/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{1,35}(\r\n[0-9a-zA-Z/\-\?\(\)\.,'\+ ][0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,34}){0,3}$/,
				regexText:TXT.template_format_error_c
			});
			if(Ext.isIE)
				textWitnCheck.regex=/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{1,35}(\r\n[0-9a-zA-Z/\-\?\(\)\.,'\+ ][0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,34}){0,3}$/;
			else
				textWitnCheck.regex=/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{1,35}(\n[0-9a-zA-Z/\-\?\(\)\.,'\+ ][0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,34}){0,3}$/;
		}
		textWitnCheck.hide();
		// textWitnCheck.disable();
		var checkBox = new Ext.form.Checkbox( {
			id :field.name + exterLabel + 'C',
			name :field.name + exterLabel + 'C',
			fieldLabel :TXT[field.label]?TXT[field.label]:field.label,
			listeners : {
				'check' : function(hidden, checked) {
					if (checked == true) {
						textWitnCheck.show();
					}
					if (checked == false) {
						textWitnCheck.hide();
						textWitnCheck.setValue('');
					}
				}
			}
		});
		if (readOnlyFlag == true) {
			// checkBox.readOnly = true;
			textWitnCheck.readOnly = true;
			includeForm.add(checkBox);
			includeForm.add(textWitnCheck);
			if (field.value != '') {
				includeForm.findById(field.name + exterLabel + 'C').setValue(
						true);
				includeForm.findById(field.name + exterLabel).setValue(
						field.value);
			}
			includeForm.findById(field.name + exterLabel + 'C').disable();

		} else {
			includeForm.add(checkBox);
			includeForm.add(textWitnCheck);
			if (field.value != '') {
				includeForm.findById(field.name + exterLabel + 'C').setValue(
						true);
				includeForm.findById(field.name + exterLabel).setValue(
						field.value);
			}
			if(field.action!=null){
				eval(field.action);
			}
		}
	}
}

MessageTemplate.createHidden = function(field, includeForm, readOnlyFlag,
		exterLabel) {
	var hiddenText = new Ext.form.Hidden( {
		id :field.name + exterLabel,
		name :field.name + exterLabel,
		labelSeparator :'',
		width :215
	});
	includeForm.add(hiddenText);
	if (field.value != '')
		includeForm.findById(field.name + exterLabel).setValue(field.value);
}

function transFormDate(date) {
	if (date == '')
		return '';
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if (month < 10)
		month = '0' + month;
	var day = date.getDate();
	if (day < 10)
		day = '0' + day;
	return year + '-' + month + '-' + day;
}

function parserJson(fields, autoform, readOnlyFlag, fieldNameList, typeList,
		exterLabel) {// ,comboValue) {
	for ( var i = 0; i < fields.length; i++) {
		var field = fields[i];
		if (fieldNameList != null && typeList != null) {
			fieldNameList[i] = field.name;
			typeList[i] = field.type;
		}
		if (field.type == 'string') {
			if (field.options == null)
				MessageTemplate.createStringText(field, autoform, readOnlyFlag,
						exterLabel);
			else
				MessageTemplate.createComboCox(field, autoform, readOnlyFlag,
						exterLabel);
		}
		if (field.type == 'int') {
			var flag = 1
			MessageTemplate.createNumberText(field, flag, autoform,
					readOnlyFlag, exterLabel);
		}
		if (field.type == 'float') {
			var flag = 0;
			MessageTemplate.createNumberText(field, flag, autoform,
					readOnlyFlag, exterLabel);
		}
		if (field.type == 'date') {
			MessageTemplate.createDataText(field, autoform, readOnlyFlag,
					exterLabel);
		}
		if (field.type == 'area') {
			MessageTemplate.createTextArea(field, autoform, readOnlyFlag,
					exterLabel);
		}
		if (field.type == 'checkBox') {
			MessageTemplate.createCheckBox(field, autoform, readOnlyFlag,
					exterLabel)
		}
		if (field.type == 'hidden') {
			MessageTemplate.createHidden(field, autoform, readOnlyFlag,
					exterLabel)
		}
	}
}

/*function createNameWin(autoform, existTName, templateId, fieldNameList,
		templateFlag, typeList, autowin,prefix,tmpWin,isClose) {
	var nameForm = new Ext.FormPanel( {
		labelAlign :'left',
		region :'center',
		labelWidth :60,
		frame :true,
		autoScroll :false,
		bodyStyle :'padding:10px 10px 10px 10px',
		layout :'form',
		items : [ {
			xtype :'textfield',
			fieldLabel :TXT_privateTemplate_entername,
			width :280,
			id :'tName',
			name :'tName',
			allowBlank :false
		} ]
	});
	var nameWin;
	if (!nameWin) {
		var nameWin = new Ext.Window(
				{
					title :TXT_privateTemplate_name,
					width :420,
					height :130,
					autoScroll :false,
					layout :'border',
					border :false,
					minimizable :false,
					maximizable :false,
					resizable :false,
					modal :true,
					shadow:false,
					layoutConfig : {
						animate :false
					},
					items : [ nameForm ],
					buttonAlign :'center',
					buttons : [ {
						text :TXT_privateTemplate_saveEnd,
						handler : function() {
							var tName = nameForm.findById('tName').getValue();
							autoform.findById(fieldNameList[0]+prefix).focus();
							var tParams = '';
							for ( var i = 0; i < fieldNameList.length; i++) {
								if (typeList[i] == 'date')
									tParams += ('&' + fieldNameList[i] + '=' + transFormDate(autoform
											.findById(fieldNameList[i]+prefix).getValue()));
								else
									tParams += ('&' + fieldNameList[i] + '=' + autoform
											.findById(fieldNameList[i]+prefix).getValue());
							}
							if(autoform.selectedTag!='')
								tParams += '&comboTextArea=yes';
							if (templateFlag == 'template')
								tParams = 'cmd=messageTemplate&action=savePrivateTemplate&templateId='
										+ templateId
										+ '&tName='
										+ tName
										+ tParams;
							else
								tParams = 'cmd=messageTemplate&action=savePrivateTemplate&privateTemplateId='
										+ templateId
										+ '&tName='
										+ tName
										+ tParams;
							function saveTemplateOnSuccess(result) {
								if (result.result == 'failure') {
									if (result.message == 'tName') {
										Ext.MessageBox.alert(TXT_common_hint,
												TXT_privateTemplate_needName);
										return;
									}
									if (result.message == 'nameExist') {
										Ext.MessageBox.alert(TXT_common_hint,
												TXT_privateTemplate_nameExists);
										return;
									}
									if(result.message=='notOwner'){
										Ext.MessageBox.alert(TXT_common_hint,
												TXT_private_message_can_not_edit);
										return;
									}
								} else {
									if(isClose==false){
										if (templateFlag != 'template') {
											privateMessageTemplateDs.reload();
										}
									}else{
										//tmpWin.close();
									}
									nameWin.close();
									autowin.close();
								}
							}
							showWaitingWin();
							Ecp.ajaxRequest(url, method, tParams,
									saveTemplateOnSuccess);
						}
					} ]
				});
	}
	if (existTName != '')
		nameForm.findById('tName').setValue(existTName);
	nameWin.show();
}*/

function createForm() {
	var autoform = new Ext.FormPanel( {
		labelAlign :'left',
		region :'center',
		labelWidth :122,
		autoScroll :true,
		frame :true,
		bodyStyle :'padding:10px 10px 10px 10px',
		layout :'form',
		layoutConfig:{
			trackLabels:true
		}
	});
	autoform.verifyCheckBox=true;
	return autoform;
}

function createFormWin(autoform,isModal) {
	autowin = new Ext.Window( {
		// title :TXT_privateTemplate_templateDetail,
		width :610,
		height :450,
		autoScroll :false,
		layout :'border',
		border :false,
		minimizable :false,
		maximizable :false,
		resizable :true,
		shadow:false,
		resizable :false,
		//modal :true,
		layoutConfig : {
			animate :false
		},
		items : [ autoform ],
		buttonAlign :'center'
	});
	if(isModal==true)
		autowin.modal=true;
	else
		autowin.modal=false;
	return autowin;
}

function showMessageLayout(templateId, templateFlag, pid, templateWindow,
		caseId) {
	// var params;
//	if(caseId!='')
		//templateWindow.hide();
	var autoform;
	var autowin;
	var fieldNameList = new Array();
	var values = {};
	var typeList = new Array();
	// var comboValue=new Array();
	//var existTName = '';
	var isOut = '';
	var duplicate = '';
	var type = '';
	//add by liusien 2009-12-14
	var templateName='';
	//add by liusien 2009-12-14
	var needHQCheck;
	displayForm();
	// add a value attr
	function displayForm() {
		var uid = templateId;
		var prefix='';
		autoform = createForm();
		if(caseId!='')
			autowin = createFormWin(autoform,false);
		else
			autowin = createFormWin(autoform,true);
		autowin['dataBus']={tempId:uid,tempFlag:templateFlag,pid:pid,cid:caseId,tempListWin:templateWindow};	
		function createOnSuccess(result) {
			var fields = result.fields;
			autowin['dataBus']['existTName'] = result.tName;
			isOut = result.isOut;
			duplicate = result.duplicate;
			type = result.type;
			autowin['dataBus']['templateName']=result.templateName;
			needHQCheck=result.needHQCheck;
			autowin.setTitle(result.templateDes);
			if(caseId!='')
				prefix=new Date().getTime();
			parserJson(fields, autoform, false, fieldNameList, typeList, prefix);// ,comboValue);
			/*autoform.findById('IBPSeqNum'+prefix).setVisible(false);
			if(pid!=''||(pid==''&&caseId==''))
				autoform.findById('IBPSeqNum'+prefix).setVisible(true);*/
			autowin['dataBus']['fieldNameList']=fieldNameList;
			autowin['dataBus']['typeList']=typeList;
			autowin['dataBus']['values']=values;
			autowin['dataBus']['prefix']=prefix;
			autowin['dataBus']['duplicate']=duplicate;
			if(autoform.findById('isFree'+prefix)!=undefined||(autoform.findById('isFree'+prefix)!=null))
				autowin['dataBus']['isFree']='yes';
			autowin.addButton(TXT.common_btnCreate, transferTempToFin,autowin);

			autowin.addButton(TXT.privateTemplate_save,showSaveDraftTemplateWin,autowin);

			autowin.addButton(TXT.common_btnClose, function() {
				autowin.close();
			}, autowin);
			autowin.show();
		}

		if (templateFlag == 'template')
			var params = 'cmd=messageTemplate&action=getTemplateLayout&templateId='
					+ uid + '&pid=' + pid + '&caseId=' + caseId;
		else
			var params = 'cmd=messageTemplate&action=getTemplateLayout&privateTemplateId='
					+ uid + '&pid=' + pid + '&caseId=' + caseId;
		Ecp.Ajax.request(params, createOnSuccess);
	}
}

function showMessageLayoutByCase(messageId, caseId, templateFlag, templateId,templateWindow) {
	// var params;
//	if(caseId!='')
		//templateWindow.hide();
	var autoform;
	var autowin;
	var fieldNameList = new Array();
	var values = {};
	var typeList = new Array();
	// var comboValue=new Array();
	//var existTName = '';
	var isOut = '';
	var duplicate = '';
	var type = '';
	//add by liusien 2009-12-14
	var templateName='';
	//add by liusien 2009-12-14
	var needHQCheck;
	displayForm();
	// add a value attr
	function displayForm() {
		var uid = templateId;
		var prefix='';
		autoform = createForm();
		if(caseId!='')
			autowin = createFormWin(autoform,false);
		else
			autowin = createFormWin(autoform,true);
		autowin['dataBus']={tempId:uid,tempFlag:templateFlag,messageId:messageId,cid:caseId,tempListWin:templateWindow};	
		function createOnSuccess(result) {
			var fields = result.fields;
			autowin['dataBus']['existTName'] = result.tName;
			isOut = result.isOut;
			duplicate = result.duplicate;
			type = result.type;
			autowin['dataBus']['templateName']=result.templateName;
			needHQCheck=result.needHQCheck;
			autowin.setTitle(result.templateDes);
			if(caseId!='')
				prefix=new Date().getTime();
			parserJson(fields, autoform, false, fieldNameList, typeList, prefix);// ,comboValue);
			autowin['dataBus']['fieldNameList']=fieldNameList;
			autowin['dataBus']['typeList']=typeList;
			autowin['dataBus']['values']=values;
			autowin['dataBus']['prefix']=prefix;
			autowin['dataBus']['duplicate']=duplicate;
			/*if(autoform.findById('isFree'+prefix)!=undefined||(autoform.findById('isFree'+prefix)!=null))
				autowin['dataBus']['isFree']='yes';*/
			//add a attr to make sure that the current inst hasn't right to send message to swift
			/*if (Ecp.user.isInstHQ == false && needHQCheck == true) {
				var count = 0;
				var cbCount = 0;
				autowin.addButton(TXT_case_sendInternalInst, function() {
					// isSwift = false;
						if (!autoform.form.isValid())
							return;
						for ( var i = 0; i < fieldNameList.length; i++) {
							if (typeList[i] == 'date')
								valueArray[i] = transFormDate(autoform
										.findById(fieldNameList[i]+prefix).getValue());
							else if (typeList[i] == 'checkBox') {
								cbCount++;
								valueArray[i] = autoform.findById(fieldNameList[i]+prefix)
										.getValue();
								if (valueArray[i] == '')
									count++;
							}
								 * else if (typeList[i] == 'combo'){ var flag=0;
								 * var rawValue =
								 * autoform.findById(fieldNameList[i])
								 * .getRawValue(); for(var j=0;j<comboValue.length;j++){
								 * if(rawValue==comboValue[j]){ flag=1 break; } }
								 * if(flag==1) valueArray[i] =
								 * autoform.findById(fieldNameList[i]) .getValue();
								 * else valueArray[i] = rawValue; }
								 
							else
								valueArray[i] = autoform.findById(fieldNameList[i]+prefix)
										.getValue();
						}
						if (count == cbCount && cbCount != 0) {
							Ext.MessageBox.alert(TXT_common_hint,
									TXT_privateTemplate_checkBoxNull);
							return;
						}
						var checkParams='cmd=message&action=vaildatorTmp';
						if (templateFlag == 'template')
							checkParams += '&templateId=' + templateId;
						else
							checkParams += '&pTemplateId=' + templateId;
						for ( var i = 0; i < fieldNameList.length; i++) 
							checkParams += '&' + fieldNameList[i] + '=' + valueArray[i];
						Ecp.ajaxRequest(url, method, checkParams,function(result){
							if(result.result=='failure'){
								Ext.MessageBox.alert(TXT_common_hint,TXT_message_transform_error);
								return;
							}
							if (caseId != '') {
								createEIMessageStep(fieldNameList, valueArray,
										templateFlag, templateId, '',
										null, autowin, caseId);
							} else if (pid == '') {
								createEIMessageStep(fieldNameList, valueArray,
										templateFlag, templateId, '',
										null, autowin, '');
							} else {
								createEIMessageStep(fieldNameList, valueArray,
										templateFlag, templateId, pid,
										null, autowin, '');
							}
						});
					}, autowin);
			}*///modified by liusien 2009-12-14
			/*var msg='';
				if (needHQCheck==false||Ecp.user.isInstHQ == true) 
					msg=TXT_message_sendToSwift;
				if (Ecp.user.isInstHQ == false && needHQCheck == true)
					msg=TXT_case_sendInternalInst;*/
			autowin.addButton(TXT.common_btnCreate, transferTempToFin,autowin);

			autowin.addButton(TXT.privateTemplate_save,showSaveDraftTemplateWin,autowin);

			autowin.addButton(TXT.common_btnClose, function() {
				autowin.close();
			}, autowin);
			autowin.show();
		}

		if (templateFlag == 'template')
			var params = 'cmd=messageTemplate&action=getReplyTemplateLayout&templateId='
					+ uid + '&messageId=' + messageId + '&caseId=' + caseId;
		else
			var params = 'cmd=messageTemplate&action=getReplyTemplateLayout&privateTemplateId='
					+ uid + '&messageId=' + messageId + '&caseId=' + caseId;
		Ecp.Ajax.request(params, createOnSuccess);
	}
}