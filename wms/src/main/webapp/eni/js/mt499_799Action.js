/* change by sunyue 20121212*/
var loadMT499_799Data = function () {
	//Ecp.components.mt499_799Grid.grid.selectFirstRow();
};
var showFinMT499_799Win = function () {
	Ecp.MT499799MessageFinSearchWin.createWindow({
		'searchFormFun':function(obj){
			Ecp.components.mt499799MessageSearchForm=obj;
		},
		'winFun':function(obj){
			Ecp.components.mt499799SearchWin=obj;
		}
	}).show();
};
var clickQueryMT499799MessageBtn = function(){
	var form = Ecp.components.mt499799MessageSearchForm;
	var values = form.form.getValues();
	if ( (values.createTimeFrom).length > 0 && (values.creatTimeTo).length > 0) {
	   	   if( values.createTimeFrom > values.creatTimeTo){
		   		Ecp.MessageBox.alert(TXT.common_time_startEndError); 
		   		return false;
	   	   }
    } 
	values['cmd']='message';
	values['action']='findMT499799';
	Ecp.components.mt499_799Grid.search(values);
	Ecp.components.mt499799SearchWin.window.close();
};
var clickAbolishMT499_799Btn = function () {
	var records = Ecp.components.mt499_799Grid.grid.getSelectedRecordDatas();
	if(!records){
		return;
	}
	Ecp.MessageBox.confirm(TXT.mt499_799_is_abolish,function(){
		var values={};
		values['cmd']='message';
		values['action']='abolishMT499799Message';
		var ids = [];
		for (var i = 0; i < records.length; i++){
			ids[i] = records[i].id;
		}
		values['id']= ids;
		Ecp.Ajax.request(values,function(result){
			if(result.result=='success'){
				Ecp.components.mt499_799Grid.show();
			}
		});
	});
	
};
var showMT499_799ContentWin = function (btn, e) {
	var grid = Ecp.components.mt499_799Grid.grid;
	if (grid.getDataCount() == 0) {
		return ;
	}
	var record = Ecp.components.mt499_799Grid.grid.getSelectedRecord();
	var formObj = {};
	var winObj = {};
	var winId = "mesageShowInMsg" + record.id;
	if (Ext.WindowMgr.get(winId) != undefined) {
		return;
	}
	var win = new Ecp.MessageFinContentWin();
	win.initForm({msgConf:{needValidate:false}, cusObj:formObj});
	win.setButtons([{text:TXT.message_transform,handler:function(bt){
												var body = win.items[0].items.items[5].value;
												transformFinContent(body);
											},
						scope:win
					},{text:TXT.message_print, handler:printMsg, scope:win}, {text:TXT.common_btnClose, handler:function () {
		this["ecpOwner"].window.close();
	}}]);
	win.handleWidgetConfig(function (win) {
		win["config"]["id"] = winId;
		win["config"]["title"] = TXT.message + " " + record.messageId;
	});
	win.customization(winObj);
	win.render();
	Ecp.Ajax.request({cmd:"message", action:"getFinMessage", messageId:record.id}, function (result) {
		win.window.getItem(0).setValues(result);
		win.window.window.setPosition(undefined, undefined);
		win.show();
		win["dataBus"] = {mid:record.id};
	});
};

