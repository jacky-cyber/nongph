com.felix.eni.module.message.MessageFinContentFormPanel = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.FormPanel);
	
	this.setConfig( {
		labelAlign : 'center',
		region     : 'center',
		labelWidth : 60,
		bodyStyle  : 'padding:10px 10px 10px 10px',
		frame      : true,
		defaultType: 'textfield',
		defaults   : { anchor :'95%' } } );
	
	this.setItems( [ { id :'sender',          name :'sender',          fieldLabel :TXT.message_assigner, allowBlank :false, minLength :8, maxLength :11, readOnly:true },
				     { id :'receiver',        name :'receiver',        fieldLabel :TXT.message_assignee, allowBlank :false, minLength :8, maxLength :11, readOnly:true },
				     { id :'reference',       name :'reference',       fieldLabel :TXT.message_20,       allowBlank :false,                              readOnly:true },
			         { id :'relatedReference',name :'relatedReference',fieldLabel :TXT.message_21,                                        maxLength :16, readOnly:true },
			         { id :'type',  	      name :'type'		       xtype:'hidden' },
			         { id :'body',            name :'body',            fieldLabel :TXT.message_body, xtype :'textarea', height :250, allowBlank :false, readOnly:true }
					] );
	
}

Ecp.MessageFinContentForm.prototype.setMessageFinContentFormConf=function(obj){
	if(obj['readOnlyFlag']!=undefined){
		var readOnlyFlag=obj['readOnlyFlag'];
		this.items[1]['readOnly']=readOnlyFlag;
		this.items[3]['readOnly']=readOnlyFlag;
		this.items[5]['readOnly']=readOnlyFlag;
	}
	if(obj['needValidate']!=undefined&&obj['needValidate']!=false){	
		Ext.isIE==true?this.items[5]['regex']=/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{1,46}(\r\n[0-9a-zA-Z/\-\?\(\)\.,'\+ ][0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,49}){0,33}$/:this.items[5]['regex']=/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{1,46}(\n[0-9a-zA-Z/\-\?\(\)\.,'\+ ][0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,49}){0,33}$/;
		this.items[5]['regexText']=TXT.template_format_error;
	}
	if(obj['needValidateVtype']!=undefined&&obj['needValidateVtype']!=false){	
		this.items[5]['vtype']='vailAreaStringFin';
	}
}