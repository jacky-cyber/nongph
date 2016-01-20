/**
 * Report Columns for report-by-business-amount   add by jinlijun 2013/1/28
 */
var column4ReportByBusiAmount=[
	{header:TXT.report_institution_name,dataIndex:'institutionName',width:350},
	{header :TXT.report_remittance,dataIndex :'remittance',menuDisabled :true,width :100},
	{header :TXT.report_UTA_name,dataIndex :'utaCount',menuDisabled :true,width :100},
	{header :TXT.report_RTM_name,dataIndex :'rtmCount',menuDisabled :true,width :100},
	{header :TXT.report_RFD_name,dataIndex :'rfdCount',menuDisabled :true,width :100},
	{header :TXT.report_RTC_name,dataIndex :'rtcCount',menuDisabled :true,width :100},
	{header :TXT.report_CNR_name,dataIndex :'cnrCount',menuDisabled :true,width :100},
	{header :TXT.report_OTH_name,dataIndex :'othCount',menuDisabled :true,width :100},
	{header :TXT.report_sum,dataIndex :'sum',menuDisabled :true,width :100},
	{header :TXT.report_percent,dataIndex :'percent',menuDisabled :true,width :100},
	{header :TXT.report_replay_case_amount,dataIndex :'replayCaseAmount',menuDisabled :true,width :100},
	{header :TXT.report_replay_case_percent,dataIndex :'replayCasePercent',menuDisabled :true,width :100}
];

var column4ReportByOverTime=[
	{header:TXT.report_institution_name,dataIndex:'institutionName',width:350},
	{header :TXT.report_UTA_name,dataIndex :'utaCount',menuDisabled :true,width :100},
	{header :TXT.report_RTM_name,dataIndex :'rtmCount',menuDisabled :true,width :100},
	{header :TXT.report_RFD_name,dataIndex :'rfdCount',menuDisabled :true,width :100},
	{header :TXT.report_RTC_name,dataIndex :'rtcCount',menuDisabled :true,width :100},
	{header :TXT.report_CNR_name,dataIndex :'cnrCount',menuDisabled :true,width :100},
	{header :TXT.report_OTH_name,dataIndex :'othCount',menuDisabled :true,width :100},
	{header :TXT.report_sum,dataIndex :'sum',menuDisabled :true,width :100},
	{header :TXT.report_percent,dataIndex :'percent',menuDisabled :true,width :100}
];

var fields4ReportByCaseType=[
	{name :'case_type_name'}, 
	{name :'remittance_in'}, 
	{name :'remittance_out'},
	{name :'remittance_undefined' },
	{name :'sum' },
	{name :'percent' }
];

var columns4ReportByCaseType=[
	{header :TXT.report_case_type, dataIndex :'case_type_name', menuDisabled :true, width :200}, 
	{header :TXT.report_remittance_in, dataIndex :'remittance_in', menuDisabled :true, width :120}, 
	{header :TXT.report_remittance_out, dataIndex :'remittance_out', menuDisabled :true, width :120}, 
	{header :TXT.report_remittance_undefined, dataIndex :'remittance_undefined', menuDisabled :true, width :120}, 
	{header :TXT.report_sum, dataIndex :'sum', menuDisabled :true, width :120}, 
	{header :TXT.report_percent, dataIndex :'percent', menuDisabled :true, width :120}
];		

var fields4ReportByCountry=[
	{name :'country'}, 
	{name :'remittance'}, 
	{name :'utaCount'},
	{name :'rtmCount' },
	{name :'rfdCount' },
	{name :'rtcCount' },
	{name :'cnrCount' },
	{name :'othCount' },
	{name :'sum' },
	{name :'percent' }
];

var columns4ReportByCountry=[
	{header :TXT.report_country, dataIndex :'country', menuDisabled :true, width :120}, 
	{header :TXT.report_remittance,dataIndex :'remittance',menuDisabled :true,width :100},
	{header :TXT.report_UTA_name,dataIndex :'utaCount',menuDisabled :true,width :100},
	{header :TXT.report_RTM_name,dataIndex :'rtmCount',menuDisabled :true,width :100},
	{header :TXT.report_RFD_name,dataIndex :'rfdCount',menuDisabled :true,width :100},
	{header :TXT.report_RTC_name,dataIndex :'rtcCount',menuDisabled :true,width :100},
	{header :TXT.report_CNR_name,dataIndex :'cnrCount',menuDisabled :true,width :100},
	{header :TXT.report_OTH_name,dataIndex :'othCount',menuDisabled :true,width :100},
	{header :TXT.report_sum,dataIndex :'sum',menuDisabled :true,width :100},
	{header :TXT.report_percent,dataIndex :'percent',menuDisabled :true,width :100}
];		    

var fields4ReportByUser=[
	{name :'institution_and_user'}, 
	{name :'newCount'},
	{name :'applyCount' },
	{name :'modifyCount' },
	{name :'cancelCount' },
	{name :'rollbackCount' },
	{name :'passCount' },
	{name :'sum' },
	{name :'percent' }
];

var columns4ReportByUser=[
	{header :TXT.report_institution_user_name, dataIndex :'institution_and_user', menuDisabled :true, width :300}, 
	{header :TXT.report_new_name,dataIndex :'newCount',menuDisabled :true,width :100},
	{header :TXT.report_apply_name,dataIndex :'applyCount',menuDisabled :true,width :100},
	{header :TXT.report_modify_name,dataIndex :'modifyCount',menuDisabled :true,width :100},
	{header :TXT.report_cancel_name,dataIndex :'cancelCount',menuDisabled :true,width :100},
	{header :TXT.report_rollback_name,dataIndex :'rollbackCount',menuDisabled :true,width :100},
	{header :TXT.report_pass_name,dataIndex :'passCount',menuDisabled :true,width :100},
	{header :TXT.report_sum,dataIndex :'sum',menuDisabled :true,width :100},
	{header :TXT.report_percent,dataIndex :'percent',menuDisabled :true,width :100}
];		   


var fields4ReportByYear=[
	{name :'year'}, 
	{name :'remittance'}, 
	{name :'utaCount'},
	{name :'rtmCount' },
	{name :'rfdCount' },
	{name :'rtcCount' },
	{name :'cnrCount' },
	{name :'othCount' },
	{name :'sum' },
	{name :'percent' }
];

var columns4ReportByYear=[
	{header :TXT.report_year, dataIndex :'year', menuDisabled :true, width :120}, 
	{header :TXT.report_remittance,dataIndex :'remittance',menuDisabled :true,width :100},
	{header :TXT.report_UTA_name,dataIndex :'utaCount',menuDisabled :true,width :100},
	{header :TXT.report_RTM_name,dataIndex :'rtmCount',menuDisabled :true,width :100},
	{header :TXT.report_RFD_name,dataIndex :'rfdCount',menuDisabled :true,width :100},
	{header :TXT.report_RTC_name,dataIndex :'rtcCount',menuDisabled :true,width :100},
	{header :TXT.report_CNR_name,dataIndex :'cnrCount',menuDisabled :true,width :100},
	{header :TXT.report_OTH_name,dataIndex :'othCount',menuDisabled :true,width :100},
	{header :TXT.report_sum,dataIndex :'sum',menuDisabled :true,width :100},
	{header :TXT.report_percent,dataIndex :'percent',menuDisabled :true,width :100}
];	

var fields4ReportByNotice=[
	{name :'institutionName'}, 
	{name :'caseNum'}, 
	{name :'busiNum'},
	{name :'currAmount' },
	{name :'valueDate' },
	{name :'queryDate' },
	{name :'noticeTimesOuter' },
	{name :'noticeTimesDepart' }
];

var columns4ReportByNotice=[
	{header :TXT.report_institution_name, dataIndex :'institutionName', menuDisabled :true, width :350}, 
	{header :TXT.report_case_num,dataIndex :'caseNum',menuDisabled :true,width :150},
	{header :TXT.report_busi_num,dataIndex :'busiNum',menuDisabled :true,width :150},
	{header :TXT.report_curr_amount,dataIndex :'currAmount',menuDisabled :true,width :150},
	{header :TXT.report_value_date,dataIndex :'valueDate',menuDisabled :true,width :100},
	{header :TXT.report_query_date,dataIndex :'queryDate',menuDisabled :true,width :100},
	{header :TXT.report_notice_times_outer,dataIndex :'noticeTimesOuter',menuDisabled :true,width :100},
	{header :TXT.report_notice_times_depart,dataIndex :'noticeTimesDepart',menuDisabled :true,width :100}
];	
