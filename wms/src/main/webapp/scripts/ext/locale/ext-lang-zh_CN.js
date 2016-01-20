/*
 * Simplified Chinese translation By DavidHu 09 April 2007
 */

Ext.UpdateManager.defaults.indicatorText = '<div class="loading-indicator">加载中...</div>';

if (Ext.View) {
	Ext.View.prototype.emptyText = '';
}

if (Ext.grid.Grid) {
	Ext.grid.Grid.prototype.ddText = '{0} 选择行';
}

if (Ext.TabPanelItem) {
	Ext.TabPanelItem.prototype.closeText = "关闭";
}

if (Ext.form.Field) {
	Ext.form.Field.prototype.invalidText = "输入值非法";
}

Date.monthNames = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
		"十一月", "十二月"];

Date.dayNames = ["日", "一", "二", "三", "四", "五", "六"];

if (Ext.MessageBox) {
	Ext.MessageBox.buttonText = {
		ok : "确定",
		cancel : "取消",
		yes : "是",
		no : "否"
	};
}

if (Ext.util.Format) {
	Ext.util.Format.date = function(v, format) {
		if (!v)
			return "";
		if (!(v instanceof Date))
			v = new Date(Date.parse(v));
		return v.dateFormat(format || "y年m月d日");
	};
}

if (Ext.DatePicker) {
	Ext.apply(Ext.DatePicker.prototype, {
		todayText : "今天",
		minText : "日期在最小日期之前",
		maxText : "日期在最大日期之后",
		disabledDaysText : "",
		disabledDatesText : "",
		monthNames : Date.monthNames,
		dayNames : Date.dayNames,
		nextText : '下月 (Control+Right)',
		prevText : '上月 (Control+Left)',
		monthYearText : '选择一个月 (Control+Up/Down 来改变年)',
		todayTip : "{0} (空格键选择)",
		format : "y年m月d日",
		okText : "确定",
		cancelText : "取消"
	});
}

if (Ext.PagingToolbar) {
	Ext.apply(Ext.PagingToolbar.prototype, {
		beforePageText : "页",
		afterPageText : "页共 {0} 页",
		firstText : "第一页",
		prevText : "前一页",
		nextText : "下一页",
		lastText : "最后页",
		refreshText : "刷新",
		displayMsg : "显示 {0} - {1}，共 {2} 条",
		emptyMsg : '没有数据需要显示'
	});
}

if (Ext.form.TextField) {
	Ext.apply(Ext.form.TextField.prototype, {
		minLengthText : "该输入项的最小长度是 {0}",
		maxLengthText : "该输入项的最大长度是 {0}",
		blankText : "该输入项为必输项",
		regexText : "",
		emptyText : null
	});
}

if (Ext.form.NumberField) {
	Ext.apply(Ext.form.NumberField.prototype, {
		minText : "该输入项的最小值是 {0}",
		maxText : "该输入项的最大值是 {0}",
		nanText : "{0} 不是有效数值"
	});
	Ext.apply(Ext.form.NumberField.prototype, {
		decimalPrecision : 2,
		// allowNegative :false,
		allowDecimals : true,
		// allowBlank : false,
		cls : 'numReadOnly',
		// private
		FormatComma : true,
		initEvents : function() {
			Ext.form.NumberField.superclass.initEvents.call(this);
			var allowed = this.baseChars + '';
			if (this.allowDecimals) {
				allowed += this.decimalSeparator;
			}
			if (this.FormatComma) {
				allowed += ",";
			}
			if (this.allowNegative) {
				allowed += "-";
			}
			this.stripCharsRe = new RegExp('[^' + allowed + ']', 'gi');
			var keyPress = function(e) {
				var k = e.getKey();
				if (!Ext.isIE
						&& (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
					return;
				}
				var c = e.getCharCode();
				if (allowed.indexOf(String.fromCharCode(c)) === -1) {
					e.stopEvent();
				}
			};
			this.el.on("keypress", keyPress, this);
		},
		// private
		validateValue : function(value) {
			if (!Ext.form.NumberField.superclass.validateValue
					.call(this, value)) {
				return false;
			}
			if (value.length < 1) { // if it's blank
				// and textfield
				// didn't flag
				// it then it's
				// valid
				return true;
			}
			if (this.FormatComma) {
				value = this.removeCommas(String(value));
			}
			value = String(value).replace(this.decimalSeparator, ".");
			if (isNaN(value)) {
				this.markInvalid(String.format(this.nanText, value));
				return false;
			}
			var num = this.parseValue(value);
			if (num < this.minValue) {
				this.markInvalid(String.format(this.minText, this.minValue));
				return false;
			}
			if (num > this.maxValue) {
				this.markInvalid(String.format(this.maxText, this.maxValue));
				return false;
			}
			return true;
		},
		fixPrecision : function(value) {
			var nan = isNaN(value);
			if (!this.allowDecimals || this.decimalPrecision == -1 || nan
					|| !value) {
				return nan ? '' : value;
			}
			return parseFloat(parseFloat(value).toFixed(this.decimalPrecision));
		},
		setValue : function(v) {
			v = typeof v == 'number' ? v : (String(this.removeCommas(v))
					.replace(this.decimalSeparator, "."));
			v = isNaN(v) ? '' : String(v).replace(".", this.decimalSeparator);
			if (String(v).length > 0)
				v = parseFloat(v).toFixed(this.decimalPrecision);
			// if(this.FormatComma)
			// v=this.formatCommaStyle(v);
			Ext.form.NumberField.superclass.setValue.call(this, v);
			if (this.FormatComma && String(v).length > 0) {
				v = this.addCommas(v);
				Ext.form.NumberField.superclass.setValue.call(this, v);
			}
		},
		parseValue : function(value) {
			if (this.FormatComma)
				value = this.removeCommas(String(value));
			value = parseFloat(String(value)
					.replace(this.decimalSeparator, "."));

			return isNaN(value) ? '' : value;
		},
		beforeBlur : function() {
			var v = this.parseValue(this.getRawValue());
			if (String(v).trim().length > 0) {
				this.setValue(this.fixPrecision(v));

			}
		},
		addCommas : function(nStr) {
			nStr += '';
			if (nStr.length == 0)
				return '';
			x = nStr.split('.');
			x1 = x[0];
			x2 = x.length > 1 ? '.' + x[1] : '';
			var rgx = /(\d+)(\d{3})/;
			while (rgx.test(x1)) {
				x1 = x1.replace(rgx, '$1' + ',' + '$2');
			}
			return x1 + x2;
		},
		removeCommas : function(nStr) {
			nStr = nStr + '';
			var r = /(\,)/;
			while (r.test(nStr)) {
				nStr = nStr.replace(r, '');
			}
			return nStr;
		}
	});
}

if (Ext.form.DateField) {
	Ext.apply(Ext.form.DateField.prototype, {
		disabledDaysText : "禁用",
		disabledDatesText : "禁用",
		minText : "该输入项的日期必须在 {0} 之后",
		maxText : "该输入项的日期必须在 {0} 之前",
		invalidText : "{0} 是无效的日期 - 必须符合格式： {1}",
		format : "y年m月d日"
	});
}

if (Ext.form.ComboBox) {
	Ext.apply(Ext.form.ComboBox.prototype, {
		loadingText : "加载...",
		valueNotFoundText : undefined
	});
}

if (Ext.form.VTypes) {
	Ext.apply(Ext.form.VTypes, {
		emailText : '该输入项必须是电子邮件地址，格式如： "user@domain.com"',
		urlText : '该输入项必须是URL地址，格式如： "http:/' + '/www.domain.com"',
		alphaText : '该输入项只能包含字符和_',
		alphanumText : '该输入项只能包含字符,数字和_'
	});
}

if (Ext.grid.GridView) {
	Ext.apply(Ext.grid.GridView.prototype, {
		sortAscText : "正序",
		sortDescText : "逆序",
		lockText : "锁列",
		unlockText : "解锁列",
		columnsText : "列"
	});
}

if (Ext.grid.PropertyColumnModel) {
	Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
		nameText : "名称",
		valueText : "值",
		dateFormat : "y年m月d日"
	});
}

if (Ext.layout.BorderLayout.SplitRegion) {
	Ext.apply(Ext.layout.BorderLayout.SplitRegion.prototype, {
		splitTip : "拖动来改变尺寸.",
		collapsibleSplitTip : "拖动来改变尺寸. 双击隐藏."
	});
}
