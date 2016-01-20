var cld;
// var CLD=document.getElementById("CLD");
var Today = new Date();
var tY = Today.getFullYear();
var tM = Today.getMonth();
var tD = Today.getDate();

var solarMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
var nStr1 = new Array('日', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十');
var monthName = new Array("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
		"11", "12");

function init() {
	// 第一次加载，选中'中国'
				var result = '';
				var gNum;
				for ( var i = 0; i < 6; i++) {
					result += '<tr align=center>';
					for (j = 0; j < 7; j++) {
						gNum = i * 7 + j;
						result += '<td id="GD' + gNum + '" nowrap ';
						if (j == 0 || j == 6) {
							result += '><font id="SD' + gNum
									+ '" size=2 face="Arial Black" ';
						} else {
							result += '><font id="SD' + gNum
									+ '" size=2 face="Arial Black" ';
						}
						result += ' TITLE=""> </font><br></td>';
					}
					result += '</tr>';
				}
				// firefox
				//var tbody1 = document.getElementById("tbody1");
				//tbody1.innerHTML = result;
				// IE
				tbody1.parentNode.outerHTML = tbody1.parentNode.outerHTML
						.replace("</TBODY>", result + "</TBODY>");

				var str = "";
				str += "<SELECT class=input1 onchange=changeCld() name=SY>";
				for (i = 2010; i < 2050; i++)

					str += "<option value='1'>" + i + "</option>";
				str += '</select>';

				document.getElementById("year").innerHTML = str;

				str = "";
				str += "<SELECT class=input1 onchange=changeCld() name=SM>";
				for (i = 1; i < 13; i++)
					str += "<option value='1'>" + i + "</option>";
				str += '</select>';
				document.getElementById("month").innerHTML = str;
				initial();

}

function solarDays(y, m) {
	if (m == 1)
		return (((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0)) ? 29 : 28);
	else
		return (solarMonth[m]);
}

function calElement(sYear, sMonth, sDay, week) {

	this.isToday = false;
	this.sYear = sYear;
	this.sMonth = sMonth;
	this.sDay = sDay;
	this.week = week;

	this.color = '';
}

function calendar(y, m) {

	var sDObj, lDObj, lY, lM, lD = 1, lL, lX = 0, tmp1, tmp2;
	var lDPOS = new Array(3);
	var n = 0;
	var firstLM = 0;

	sDObj = new Date(y, m, 1);// 当月一日日期

	this.length = solarDays(y, m);// 国历当月天数
	this.firstWeek = sDObj.getDay(); // 国历当月1日星期几

	for ( var i = 0; i < this.length; i++) {
		// sYear,sMonth,sDay,week
		this[i] = new calElement(y, m + 1, i + 1,
				nStr1[(i + this.firstWeek) % 7]);

	}
	// 今日
	if (y == tY && m == tM)
		this[tD - 1].isToday = true;

}

function drawCld(SY, SM, resultFormDB) {
	var resultArray = resultFormDB.split(",");
	var i, sD, s, size;
	cld = new calendar(SY, SM);
	var rendered;
	var isWeekend=false;//默认周末为红色
	document.getElementById("YMBG").innerHTML = SY + "年" + monthName[SM] + "月";

	for (i = 0; i < 42; i++) {
		rendered = false;
		sObj = eval('SD' + i);

		sObj.className = '';

		sD = i - cld.firstWeek;
		// alert("sD:"+sD+";cld.firstWeek:"+cld.firstWeek+";cld.length:"+cld.length+";i:"+i);
		if (sD > -1 && sD < cld.length) {
			// d-->日
			var d = sD + 1;
			var sObjId = 'SD' + i;
			var tempD = (d) < 10 ? "0" + d : d;//day:1->01
			if(i == 0 || i % 7 == 0 || (i + 1) % 7 == 0){//默认周末为红色
				sObj.innerHTML = '<span style="color: red;CURSOR: hand" onclick="checkDay('
					+ i + ',' + d + ')">' + d + '</span>';
				isWeekend=true;
				
			}
			for ( var m = 0; m < resultArray.length; m++) {
				if (tempD == resultArray[m].substring(0, 2)) {
					if (resultArray[m].substring(2, 3) == '2') {
						sObj.innerHTML = '<span style="color: red;CURSOR: hand" onclick="checkDay('
								+ i + ',' + d + ')">' + d + '</span>';
						rendered = true;
						break;
					}
					else if(resultArray[m].substring(2, 3) == '1' && isWeekend){//是周末并且sign为1时，设置周末为工作日
						sObj.innerHTML = '<span style="color: black;CURSOR: hand" onclick="checkDay('
							+ i + ',' + d + ')">' + d + '</span>';
						isWeekend=false;
						rendered = true;
						break;
					}
				}
			}
			//style="COLOR: red" style="COLOR: black"
			if (cld[sD].isToday)
				sObj.className = 'todyaColor';//今天
			if (rendered == false && isWeekend==false) {//不是周末，并且没有被设置过

				sObj.innerHTML = '<span style="color: black;CURSOR: hand" onclick="checkDay('
						+ i + ',' + d + ')">' + d + '</span>';
			} 
			
		} else { // 非日期
			sObj.innerHTML = '';
		}
		isWeekend=false;

	}
}

function changeCld() {
	var y, m;
	y = CLD.SY.selectedIndex + 2010;
	m = CLD.SM.selectedIndex;
	// window.location.href = ""
	var year = y;
	var month = (m) < 10 ? "0" + parseInt(m + 1) : m + 1;
	var para = {
		cmd : 'calendarMaintain',
		action : 'queryByMonthAndCountry',
		savedDate : year + '-' + month + '-01 00:00:00'

	}

	Ecp.Ajax.request(para, function(r) {
		var resultFormDB = "";
		for ( var i = 0; i < r.length; i++) {
			if (i < r.length - 1)
				resultFormDB += r[i].savedDate.substring(8, 10) + r[i].sign
						+ ',';
			else
				resultFormDB += r[i].savedDate.substring(8, 10) + r[i].sign;
		}
		drawCld(y, m, resultFormDB);
		tick();
	});

}

function pushBtm(K) {
	switch (K) {
	case 'YU':
		if (CLD.SY.selectedIndex > 0)
			CLD.SY.selectedIndex--;
		break;
	case 'YD':
		if (CLD.SY.selectedIndex < 149)
			CLD.SY.selectedIndex++;
		break;
	case 'MU':
		if (CLD.SM.selectedIndex > 0) {
			CLD.SM.selectedIndex--;
		} else {
			CLD.SM.selectedIndex = 11;
			if (CLD.SY.selectedIndex > 0)
				CLD.SY.selectedIndex--;
		}
		break;
	case 'MD':
		if (CLD.SM.selectedIndex < 11) {
			CLD.SM.selectedIndex++;
		} else {
			CLD.SM.selectedIndex = 0;
			if (CLD.SY.selectedIndex < 149)
				CLD.SY.selectedIndex++;
		}
		break;
	default:
		CLD.SY.selectedIndex = tY - 1900;
		CLD.SM.selectedIndex = tM;
	}
	changeCld();
}

function tick() {
	var today;
	today = new Date();
	document.getElementById("nowTime").innerHTML = today.toLocaleString()
			.replace(/(年|月)/g, "/").replace(/日/, "");
	window.setTimeout("tick()", 1000);
}

// ///////////////////////////////////////////////////////
function checkDay(i, d) {
	var title;
	var flag;
	var sObjId = 'SD' + i;
	var sObj = document.getElementById(sObjId).lastChild;
	var month = (CLD.SM.selectedIndex + 1) < 10 ? "0"
			+ parseInt(CLD.SM.selectedIndex + 1) : CLD.SM.selectedIndex + 1;
	var day = d < 10 ? "0" + d : d;
	var calendarDate = CLD.SY.selectedIndex + 2010 + "-" + month + '-' + day;
	if (sObj.style.cssText == 'COLOR: black; CURSOR: hand') {
		title = "是否设置为休息日?";
		flag = 2;
	} else if (sObj.style.cssText == 'COLOR: red; CURSOR: hand') {
		title = "是否设置为工作日?";
		flag = 1;
	}
	var para = {
		cmd : 'calendarMaintain',
		action : 'updateSign',
		savedDate : calendarDate + ' 00:00:00',
		sign : flag

	}
	Ecp.MessageBox.confirm(title, function() {

		Ecp.Ajax.request(para, function(r) {

		});

		if (flag == 2) {
			sObj.style.cssText = 'COLOR: red; CURSOR: hand';
			sObj.style.color = 'red';
		} else {
			sObj.style.cssText = 'COLOR: black; CURSOR: hand';
			sObj.style.color = 'black';
		}

	}

	);
}
function initial() {
	CLD.SY.selectedIndex = tY - 2010;
	CLD.SM.selectedIndex = tM;
	var year = tY;
	var month = (tM + 1) < 10 ? "0" + parseInt(tM + 1) : tM + 1;
	var para = {
		cmd : 'calendarMaintain',
		action : 'queryByMonthAndCountry',
		savedDate : year + '-' + month + '-01 00:00:00'

	}

	Ecp.Ajax.request(para, function(r) {
		var resultFormDB = "";
		for ( var i = 0; i < r.length; i++) {
			if (i < r.length - 1)
				resultFormDB += r[i].savedDate.substring(8, 10) + r[i].sign
						+ ',';
			else
				resultFormDB += r[i].savedDate.substring(8, 10) + r[i].sign;
		}
		drawCld(tY, tM, resultFormDB);
		tick();
	});

}