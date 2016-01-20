/**
 * MessageViewWindow
 */
com.felix.eni.module.message.MessageViewWindow = function(form, mc) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	// additional attributes
	var msgConfig = { id:'',
					  editMode:'readOnly',
					  tag:'',
					  fin:'fin' };
	if( mc ) {
		msgConfig = mc;
	} 
	
	var get 						  
	this.setConfig( {
					width :700,
					height :500,
					autoScroll :false,
					layout :'border',
					border :false,
					modal:false,
					minimizable :false,
					maximizable :true,
					layoutConfig : {animate :false},
					resizable :false,
					buttonAlign :'right' } );
	
	this.setButtons( [ { text :TXT.common_btnOK,
						 handler : function() {	win.window.close(); } 
					    } ] );
	
	this.setItems( [form] );
	
	var titleJson={
			'camt.008':TXT.eni_008,
			'camt.007':TXT.eni_007,
			'camt.026':TXT.eni_026,
			'camt.027':TXT.eni_027,
			'camt.028':TXT.eni_028,
			'camt.029':TXT.eni_029,
			'camt.030':TXT.eni_030,
			'camt.031':TXT.eni_031,
			'camt.032':TXT.eni_032,
			'camt.033':TXT.eni_033,
			'camt.034':TXT.eni_034,
			'camt.035':TXT.eni_035,
			'camt.036':TXT.eni_036,
			'camt.037':TXT.eni_037,
			'camt.038':TXT.eni_038,
			'camt.039':TXT.eni_039
	};
	
	var noticeCount=0;
	
	this.setNoticeCount = function( noticeCount ) {
		this.noticeCount=noticeCount;
	}
	
	this.initMessageBody = function(){
		if( typeof messageBody!='undefined' && messageBody!='')
			messageBody='';
	}
	
	this.getMessageBody = function(){
		return getItem(0)['ecpOwner'].getMessageBody();
	}
	
	this.validate = function(tag, validateSuccessFun){
		return getItem(0)['ecpOwner'].validate(tag,validateSuccessFun);
	}
	
	this.validateFin=function(messageType,sender,receiver,validateSuccessFun){
		return getItem(0)['ecpOwner'].validateFin(messageType,sender,receiver,validateSuccessFun);
	}
	
	this.setIframeSrc=function(srcJson){
		return getItem(0)['ecpOwner'].setIframeSrc(srcJson);
	}
	
	this.print=function(type){
		var url = ECP_MESSAGE_SERVLET_URL+"&printMode=y&id="+this.msgConfig.id;
		if (type && type == "P")
			url += "&msgType=P";
		window.open(url);
	}
}