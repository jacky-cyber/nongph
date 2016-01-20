/**
 * MessageViewTabPanel
 */
com.felix.eni.module.message.MessageViewTabPanel = function() {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.TabPanel );
	
	var suffix = new Date().getTime();
	var errorJson={
			'ERR_CODE_NEED_OrgnlInstrId':ERR_CODE_NEED_OrgnlInstrId,
			'ERR_CODE_OrgnlInstrId_error':ERR_CODE_OrgnlInstrId_error,
			'ERR_CODE_InstdAmtCcy_error':ERR_CODE_InstdAmtCcy_error,
			'ERR_CODE_InstdAmt_error':ERR_CODE_InstdAmt_error,
			'ERR_CODE_InstdAmt_increase':ERR_CODE_InstdAmt_increase,
			'ERR_CODE_NEED_ASSIGNEE':ERR_CODE_NEED_ASSIGNEE,
			'ERR_CODE_Invalid_ASSIGNEE_Name':ERR_CODE_Invalid_ASSIGNEE_Name,
			'ERR_CODE_ASSIGNEE_Length':ERR_CODE_ASSIGNEE_Length,
			'ERR_CODE_InstdAmt_Length_error':ERR_CODE_InstdAmt_Length_error,
			'ERR_Xml_Validate_Error':ERR_Xml_Validate_Error
	};
	
	this.setMsgSrc = function(srcJson) {
		var src = Ext.urlEncode( srcJson );
		function addStr( orgStr, addStr ){
			var part1=orgStr.substring(0,orgStr.lastIndexOf("'")+1);
			var part2=orgStr.substring(orgStr.lastIndexOf("'")+1);
			return part1+' '+addStr+' '+part2;
		}
	
		this.items[0].html=addStr(this.items[0].html,'src=\''+ECP_MESSAGE_SERVLET_URL+'&'+src+'\'');
	}
	
	this.setIframeSrc = function(srcJson){
		document.getElementById('caseFormFrame'+this.suffix).src=ECP_MESSAGE_SERVLET_URL+'&'+Ext.urlEncode(srcJson);
	}

	this.getMessageBody = function(isReplace) {
		var case_form_win = document.getElementById('caseFormFrame' + this.suffix).contentWindow;
		case_form_win.writeXml();
		var sxml = case_form_win.document.getElementById("xml").value;
		if( isReplace ) {
			sxml = sxml.replace(/\n/g, '');
			sxml = sxml.replace(/>/g, '>\n');
		}
		return sxml;
	}

	this.getReceiverForFinTemplate=function(){
		var case_form_win = document.getElementById('caseFormFrame' + this.suffix).contentWindow;
		var receiver=case_form_win.document.getElementById("msgReceiver").value;
		return receiver;
	}

	this.validate = function(tag, validateSuccessFun) {
		var params={
				cmd:'message',
				action:'messageSchemaValidator',
				messageTypeTag:tag,
				messageBody:this.getMessageBody()
		};
		com.felix.nsf.Ajax.request(params, function(result) {
			if (result.result != 'success') {
				if (result.message == 'senderBicError')
					Ecp.MessageBox.alert(TXT.message_xml_sender_error);
				else if (result.message == 'receiverBicError')
					Ecp.MessageBox.alert(TXT.message_xml_receiver_error);
				else {
					var errorMsg = "";
					if(result.message.length>0)
					{
						errorMsg += errorJson[result.message[0].error];
						for (i = 1; i < result.message.length; i++)
							errorMsg +="<br>"+errorJson[result.message[i].error];
					}
					Ecp.MessageBox.alert(errorMsg);
				}
			} else {
				validateSuccessFun(result);
			}
		});
	}

	this.validateFin = function(messageType, sender, receiver, validateSuccessFun) {
		var messageBody = replaceSuffix( this.getMessageBody() );
		var params={
				cmd:'message',
				action:'finMessageValidate',
				type:'y',
				messageType:messageType,
				body:messageBody,
				sender:sender,
				receiver:receiver
		};
		com.felix.nsf.Ajax.request(params, function(result) {
			if (result.result != 'success') {
				if(result.msg=='' || result.msg==null)
					Ecp.MessageBox.alert(TXT.message_transform_error);
				else if(result.msg=='formatErr')
					Ecp.MessageBox.alert(TXT.message_format_err);
				else
					Ecp.MessageBox.alert(result.msg);
			} else {
				validateSuccessFun(result);
			}
		});
	}

	var replaceSuffix=function(messageBody){
		if(Ext.isIE){	
			while(messageBody.indexOf("&#10;")!=-1){
				messageBody = messageBody.replace("&#10;", "..ABC..");
			}
		}else{
			while(messageBody.indexOf("\\n")!=-1){
				messageBody = messageBody.replace("\\n", " ..ABC..");
			}
		}
		return messageBody;
	}

	this.setConfig( { id : 'messageViewTabPanel' + this.suffix,
					  activeTab : 0,
					  region : 'center',
					  defaults : { autoScroll :false } } );
	
	this.setItems( [ { id :'msgForm' + this.suffix,
					   title :TXT.message_form,
					   html :"<iframe name='caseFormFrame" + this.suffix + "' id='caseFormFrame" + this.suffix + "' frameborder='0' width='100%' height='100%'></iframe>"
					 },
					 { id :'msgSrc' + this.suffix,
					   title :TXT.message_src,
					   xtype:'form',
					   items : [ { x :0,
								   y :60,
								   xtype :'textarea',
							       name :'msgText'+this.suffix,
							       id :'msgText'+this.suffix,
							       hideLabel :true,
							       readOnly :true,
							       anchor :'100% 100%'} ]
					  } ] );
	this.getExtTabPanel().on( 'beforetabchange', function(p, n, c) {
		if ( n.id == 'msgSrc' + suffix )
			getItem('msgSrc' + suffix).findById('msgText'+suffix).setValue(renderTabPanel['ecpOwner']['ecpOwner'].getMessageBody(true));
	});
}