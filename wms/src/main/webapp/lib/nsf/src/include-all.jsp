<%
String contextPath = request.getContextPath();
String locale = request.getParameter("locale");
if( locale==null || locale.isEmpty() ) 
	locale = "zh_CN";
%>
var contextPath = "<%=contextPath%>";
var locale = "<%=locale%>";

String.prototype.replaceAll = function(s1,s2) {
    return this.replace(new RegExp(s1,"gm"),s2); 
}

String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.strContains = function(substr, isIgnoreCase){
	var string = this;
	if( isIgnoreCase ) {
		string = string.toLowerCase();
		substr = substr.toLowerCase();
	}
	var startChar = substr.substring(0,1);
	var strLen=substr.length;
	for( var j=0; j<string.length-strLen+1; j++ ) {
		if( string.charAt(j)==startChar ) {
			if(string.substring(j, j+strLen)==substr)
				return true;
		}
	}
	return false;
}

String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
}

String.prototype.endWith = function( str ){
	if( str==null || str=="" || this.length==0 || str.length>this.length )
	  return false;
	if( this.substring( this.length-str.length )==str )
	  return true;
	else
	  return false;
	return true;
}

document.write("<script type='text/javascript' src='<%=contextPath%>/lib/extjs/adapter/ext/ext-base-debug.js'></script>");
document.write("<script type='text/javascript' src='<%=contextPath%>/lib/extjs/ext-all-debug.js'></script>");
document.write("<script type='text/javascript' src='<%=contextPath%>/lib/extjs/src/locale/ext-lang-<%=locale%>.js'></script>");
document.write("<script type='text/javascript' src='<%=contextPath%>/lib/extjs/ux/fileuploadfield/FileUploadField.js'></script>");
document.write("<script type='text/javascript' src='<%=contextPath%>/lib/extjs/ux/RowEditor.js'></script>");

document.write("<script type='text/javascript' src='<%=contextPath%>/lib/nsf/src/Package.js'></script>");
document.write("<script type='text/javascript' src='<%=contextPath%>/lib/nsf/src/locale/lang_<%=locale%>.js'></script>");
document.write("<script type='text/javascript' src='<%=contextPath%>/lib/nsf/src/Ext-expand.js'></script>");

document.write("<script type='text/javascript' src='<%=contextPath%>/std/baseWidget/menu.js'></script>");
