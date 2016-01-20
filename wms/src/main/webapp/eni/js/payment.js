Ecp.components.globalView=new Ecp.GlobalView('payment');
function paymentLayout(){
	var cmtStoreObj={};
	typeof cmt_payment_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_payment_dsConfig;
	typeof cmt_payment_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_payment_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_payment_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_payment_gridConfig;
	typeof cmt_payment_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_payment_cmConfig;
	typeof cmt_payment_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_payment_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_payment_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_payment_toolBarFn;
	typeof cmt_payment_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_payment_toolBarConfigs;
	
	var paymentToolBar=new Ecp.PaymentToolBar();
	paymentToolBar.setWidgetEvent({
		explicitSearchPayment:showExplicitSearhPaymentWin,
		searchPayment:showSearchPaymentWin,
		keyWordsSearchPayment:showPaymentMessageKeyWordsSearchWin,
		showLinkCaseWin:showLinkCaseWin,
		//importPayment:importPayment,
		paymentRelateCase:showPaymentRelateCaseWin,
		createCaseWith007:showCreateCaseWith007InPaymentListWin,
		createCaseWith008:showCreateCaseWith008InPaymentListWin,
		createCaseWith026:showCreateCaseWith026InPaymentListWin,
		createCaseWith027:showCreateCaseWith027InPaymentListWin,
		createCaseWith028:showCreateCaseWith028InPaymentListWin,
		createCaseWith037:showCreateCaseWith037InPaymentListWin,
		createCaseWithFinTemplate:showMsgTmpWinWithPaymentList,
		createCaseWithDraft:showPrivateMsgTmpWinWithPaymentList,
		createCaseWith192:showCreateCaseWithMT192InPaymentListWin,
		createCaseWith195:showCreateCaseWithMT195InPaymentListWin,
		createCaseWith196:showCreateCaseWithMT196InPaymentListWin,
		createCaseWith292:showCreateCaseWithMT292InPaymentListWin,
		createCaseWith295:showCreateCaseWithMT295InPaymentListWin,
		createCaseWith296:showCreateCaseWithMT296InPaymentListWin
	});
	paymentToolBar.setPremission(Ecp.userInfomation.menu);
	paymentToolBar.customization(toolBarCusObj);
	
	var paymentGrid=new Ecp.PaymentMessageGrid();
	paymentGrid.setToolBar(paymentToolBar.render());
	
	paymentGrid.setWidgetEvent({
		grid:{
			dblclick:getPaymentMessage,
			click:checkXmlMenuWhenClick
		},
		store:{
			load:initXmlMenuWhenLoadData
		}
	});
	
	paymentGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	Ecp.components.globalView.addModuleComp(paymentGrid.render());
	Ecp.components.globalView.render(TXT.payment);
	
	paymentGrid.show();
	
	Ecp.components.paymentGrid=paymentGrid;
}

Ecp.onReady(paymentLayout);