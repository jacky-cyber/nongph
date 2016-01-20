// window 1
var showTestWin1=function(btn,e)
{
	// only create a window widget and show it
	// sometimes it should get data from backend
	var win=new Ecp.testWin1();
	win.show();
}

var clickWin1Btn1=function(btn,e)
{
	Ecp.ajaxRequest('test.jsp','POST','',function(result){
		alert("result when clicking btn1 in win1");
		showTestWin2();
	})
}

var clickWin1Btn2=function(btn,e)
{
	Ecp.ajaxRequest('test.jsp','POST','',function(result){
		alert("result when clicking btn2 in win1");
	})
}

//window2
var showTestWin2=function(btn,e)
{
	// only create a window widget and show it
	// sometimes it should get data from backend
	var win=new Ecp.testWin2();
	win.show();
}

var clickWin2Btn1=function(btn,e)
{
	Ecp.ajaxRequest('test.jsp','POST','',function(result){
		alert("result when clicking btn1 in win2");
	})
}

var clickWin2Btn2=function(btn,e)
{
	Ecp.ajaxRequest('test.jsp','POST','',function(result){
		alert("result when clicking btn2 in win2");
	})
}

// define win1
Ecp.testWin1=function()
{
	
}

Ecp.testWin1.prototype.show=function()
{
	Ext.MessageBox.confirm(TXT.common_hint, 'test window1', function(btn){
		Ecp.showWaitingWin();
		if (btn == 'yes') {
			clickWin1Btn1(btn);
		}else{
			clickWin1Btn2(btn);
		}
	});
}

// define win2
Ecp.testWin2=function()
{
	
}

Ecp.testWin2.prototype.show=function()
{
	Ext.MessageBox.confirm(TXT.common_hint, 'test window2', function(btn){
		Ecp.showWaitingWin();
		if (btn == 'yes') {
			clickWin2Btn1(btn);
		}else{
			clickWin2Btn2(btn);
		}
	});
}