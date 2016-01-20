<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf8">
  <style type="text/css">
body {
	font-size:12px;
}
#innerWrapper {
	padding:8px;
}
.MainContainer {
	padding:8px;
	width:100%; 
}

.header {
	padding:8px;
}
</style>
<link href="styles/global.css" rel="stylesheet" type="text/css" />
<link href="styles/messageContainer.css" rel="stylesheet" type="text/css" />
</head>

<%
	String finMsgType=((String)request.getAttribute("messageType"));
	if(!finMsgType.startsWith("MT"))
		 request.setAttribute("messageType","MT"+finMsgType);
 %>
<script type="text/javascript">
	function writeXml() {
		showXML(this);
	}
</script>

<form id="caseForm">
<div class="header"><b>
</b><input type="text" id="msgId" value="<%=request.getAttribute("messageType")%>" style="display:none;"/>
<input type="text" id="msgType" value="fin" style="display:none;"/>

<input type="button" value="Load" onclick="loadMessage();" style="display:none;"/><input type="button" value="hide" onclick="showOptional(this);" style="display:none;"/><input type="button" value="toXML" onclick="showXML(this);" style="display:none;"/> <input type="text" id="search" onkeyup="searchContainer(this);" style="display:none;"/>
<input type="text" id="creator" style="display:none;"/>
<input type="text" id="assnge" style="display:none;"/>
<input type="hidden" id="editMode" value="<%=request.getAttribute("editMode") %>" />
<input type="hidden" id="msgLang" value="<%=request.getAttribute("msgLang") %>" />
</div>
<div class="header">
<div class="MainContainer">
<div class="patternContainer">
	<div class="patternContainerLabel">Sender: </div>
	<div class="patternField mandatoryField"><input type="text" class="field" id="sender" readOnly="readOnly" value='<%=request.getAttribute("sender")==null?"":request.getAttribute("sender") %>'/></div>
</div>
<textarea rows="1" cols="100" id="xml" name="xml" style="display:none;"><%=request.getAttribute("messageBody") %></textarea>
<div class="patternContainer">
	<div class="patternContainerLabel">Receiver:</div>
	<div class="patternField mandatoryField">
		<input id="msgReceiver" type="text" class="field" id="receiver" value='<%=request.getAttribute("receiver")==null?"":request.getAttribute("receiver") %>'/>
		<script type="text/javascript">
			if('readOnly'=='<%=request.getAttribute("editMode") %>')
				document.getElementById('msgReceiver').readOnly='readOnly';
			if(parent.messageBody)
				document.getElementById("xml").value = parent.messageBody;
		</script>
	</div>
</div>
</div>
</div>
<div id="innerWrapper"></div>

<!--<textarea rows="1" cols="100" id="xml" name="xml" style="display:none;"><%=request.getAttribute("messageBody") %></textarea>-->
<!--  style="width:850px; height:400px; overflow-y:auto; overflow-x:hidden;" -->

<div id="messageContainerTemplates" style="display:none;">
	<div class="sequenceContainer" type="SequenceContainer">
		<div class="sequenceHeader">
			<div class="checkBoxWrapper"><input type="checkbox" onclick="_$f(event)" /></div>
			<div class="headerInfo">
				<div class="headerLabel"></div>
				<div class="headerDescription">&nbsp;&nbsp;</div>
			</div>
			<div class="headerUtils">
				<a href="javascript:void(0);" class="collapseBtn" title="Expand" onclick="_$f(event)">&nbsp;</a>
				<a href="javascript:void(0);" class="newOccurBtn" onclick="_$f(event)">New Occ..</a>
			</div>
		</div>
	</div>
	<div class="sequenceBody" type="SequenceBody"></div>
	<div class="sequenceItemContainer" type="SequenceItemContainer">
		<div class="sequenceItemHeader">
			<div class="headerLabel"></div>
			<div class="headerUtils"><a href="javascript:void(0);" class="removeBtn" onclick="_$f(event)">Remove</a>
			</div>
		</div>
		<div class="sequenceItemBody"></div>
	</div>
	<div class="compositeContainer" type="CompositeContainer">
		<div class="compositeHeader">
			<div class="checkBoxWrapper"><input type="checkbox" onclick="_$f(event)" /></div>
			<div class="headerInfo">
				<div class="headerLabel"></div>
				<div class="headerDescription"></div>
			</div>
		</div>
		<div class="compositeBody"></div>
	</div>
	<div class="compositeContainer" type="OptionContainer">
		<div class="compositeHeader">
			<div class="checkBoxWrapper"><input type="checkbox" onclick="_$f(event)" /></div>
			<div class="headerInfo">
				<div class="headerLabel"></div>
				<div class="headerDescription"></div>
			</div>
		</div>
		<div class="compositeBody"></div>
	</div>
	<div class="fieldOptionSelector" type="OptionContainerSelector"><select onchange="_$f(event)"></select></div>
	<div class="patternContainer" type="PatternContainer">
		<div class="checkBoxWrapper"><input type="checkbox" onclick="_$f(event)" /></div>
		<div class="patternContainerLabel"></div>
	</div>
	<div class="patternField" type="ListPatternField">
		<select class="field" onchange="_$f(event)"></select>
		<span class="format" title="Format"></span>
		<span class="fieldDescription"></span>
	</div>
	<div class="patternField" type="TextAreaPatternField">
		<textarea class="field" wrap="hard" onfocus="_$f(event)" onblur="_$f(event)"></textarea>
		<span class="format" title="Format"></span>
		<span class="fieldDescription"></span>
	</div>
	<div class="patternField" type="TextPatternField">
		<input type="text" class="field" onkeypress="_$f(event)" onkeyup="_$f(event)" onfocus="_$f(event)" onblur="_$f(event)" />
		<span class="format" title="Format"></span>
		<span class="fieldDescription"></span>
	</div>
	<div class="patternField" type="BooleanPatternField">
		<label class="labeledField"><input type="checkbox"/>&nbsp;&nbsp;true</label>
		<span class="format" title="Format"></span>
		<span class="fieldDescription"></span>
	</div>
	<div class="patternField" type="CheckListPatternField">
		<div class="multiField"></div>
		<span class="format" title="Format"></span>
		<span class="fieldDescription"></span>
	</div>
	<div class="patternField" type="ComplexTextPatternField">
		<div class="complexField"><input type="text" class="field" onkeypress="_$f(event)" onkeyup="_$f(event)" onfocus="_$f(event)" onblur="_$f(event)"  /></div>
		<span class="format" title="Format"></span>
		<span class="fieldDescription"></span>
	</div>
	<div class="bicExpansionContainer" type="BicExpansionContainer">
		<div class="checkBoxWrapper"></div>
		<table class="bicExpansionForm" cellpadding="4" cellspacing="0">
			<tr><td class="bicExpansionLabel">Institution</td><td></td></tr>
			<tr><td class="bicExpansionLabel">Branch</td><td></td></tr>
			<tr><td class="bicExpansionLabel">City</td><td></td></tr>
			<tr><td class="bicExpansionLabel">Country</td><td></td></tr>
		</table>
	</div>
</div>
<iframe frameborder="0" width="0" height="0" src="UIGenerator.htm"></iframe>
</form>

</html>
