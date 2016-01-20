Ext.BLANK_IMAGE_URL =       contextPath+"/lib/extjs/resources/images/default/s.gif";
Ext.chart.Chart.CHART_URL = contextPath+"/lib/extjs/resources/charts.swf";
Ext.form.Field.prototype.msgTarget = 'side';
Ext.QuickTips.init();
Ext.Ajax.timeout = 2*60*1000;

// additional ext function
Ext.grid.CheckboxSelectionModel.prototype.reset=function(){
	Ext.grid.CheckboxSelectionModel.superclass.clearSelections.call(this);
	Ext.fly(this.grid.view.innerHd).child('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
};

Ext.override(Ext.form.NumberField, {
    setValue : function(v){
            v = typeof v == 'number' ? v : String(v).replace(this.decimalSeparator, ".");
        v = isNaN(v) ? '' : String(v).replace(".", this.decimalSeparator);
        return Ext.form.NumberField.superclass.setValue.call(this, v);
    },
    fixPrecision : function(value){
        var nan = isNaN(value);
        if(!this.allowDecimals || this.decimalPrecision == -1 || nan || !value){
           return nan ? '' : value;
        }
        return parseFloat(value).toFixed(this.decimalPrecision);
    }
});

Ext.apply(
		Ext.form.VTypes, { password : function( value, field ){
											if( field.name ) {
												var password = Ext.getCmp(field.name);
												return (password.getValue()==value);
											}
											return true;
									  },
						   passwordText : TXT.user_confirmPwdError }
		);

Ext.override(Ext.layout.BoxLayout, {
    renderAll : function(ct, target){
        if(!this.innerCt){
            this.innerCt = target.createChild({cls:this.innerCls,style:"overflow: auto;"});
            this.padding = this.parseMargins(this.padding);
        }
        Ext.layout.BoxLayout.superclass.renderAll.call(this, ct, this.innerCt);
    },
});
