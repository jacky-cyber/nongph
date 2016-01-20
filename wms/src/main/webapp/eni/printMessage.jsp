<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
request.setCharacterEncoding("utf-8");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=request.getAttribute("messageType") %></title>
<style>
	body {
		margin-top:20px;
		margin-right:20px;
		margin-bottom:20px;
		margin-left:20px;
	}
</style>

<style media="print" type="text/css">
　　.Noprint{display:none;}
</style>

</head>
<body>

<div class="Noprint"> 
<input type="button" value="打印"  onclick="window.print()"/>
<input type="button" value="关闭"  onclick="window.close()"/>
</div>

<%
	String message = request.getAttribute("messageBody").toString(); 
	
	//String xmlMessage = request.getAttribute("messageHtmlBody").toString();
	
/*
	if (message.indexOf("<?xml") >= 0) {
		message = message.replaceAll("[\\n]","");
		message = message.replaceAll("<","&lt");
		message = message.replaceAll(">","&gt<br>");
		message = message.replaceAll("&lt/","<br>&lt/");
		message = message.replaceAll("<br><br>&lt/","<br>&lt/");
		//message = message.replaceAll("[\\n]","<br>");
	}else {
		message = message.replaceAll("<","&lt");
		message = message.replaceAll(">","&gt");
		message = message.replaceAll("[\\n]","<br>");
	}
*/
%>
<%=message %>

</body>
</html>