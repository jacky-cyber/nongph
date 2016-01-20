com.felix.nsf.GlobalConstants = ( function(thoj){
	thoj.DISPATCH_SERVLET_URL = contextPath+"/RequestDispatchServlet";
	thoj.CSP_SOO_SERVLET_URL      = contextPath+"/CspSsoServlet";
	thoj.ECP_MESSAGE_SERVLET_URL  = contextPath+"/eni/MessageServlet?r="+new Date().getTime();
	thoj.DATA_INIT_SERVLET_URL    = contextPath+"/DataInitServlet";
	thoj.PAGE_SIZE = 25;
	
	thoj.URL_LOGIN = contextPath + "/std/module/login/login.html?locale="+locale;
	thoj.URL_LOGOUT = contextPath + "/singleLogout";
	thoj.ENI_INDEX_URL= contextPath +'/std/module/user/user.html';

	thoj.APP_MENU_TYPE = 1;

	thoj.APP_VERSION = 'v3.0.build.21207';
	thoj.APP_COPYRIGHT = "Copyright Â© 2008-2013 Felix, All Rights Reserved";

	thoj.USING_SKIN = false;
	return thoj;
})({});