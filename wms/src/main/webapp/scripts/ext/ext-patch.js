Ext.override(Ext.menu.DateMenu,{
	render: function(){
		Ext.menu.DateMenu.superclass.render.call(this);
		if(Ext.isGecko){
			this.picker.el.dom.childNodes[0].style.width = '178px';
			this.picker.el.dom.style.width = '178px';
		}
	}
});  