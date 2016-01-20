com.felix.form.module.render.FormRenderer = (function (thoj){
	thoj.render = function(ft){
		var form = new Ext.FormPanel( {
			labelAlign :'left',
			region :'center',
			labelWidth :80,
			autoScroll :true,
			frame :true,
			bodyStyle :'padding:5px 5px 5px 5px',
			layout :'form',
			layoutConfig:{
				trackLabels:true
			}
		} );
		
		for( var i=0; i<ft.fields.length; i++ ) {
			var field = ft.fields[i];
			var rendererClass = com.felix.form.module.render.Renderer.getRenderer(field.type);
			var renderer = new rendererClass();
                        renderer.constructor( field );
			form.add( renderer.render() );
		}
		
		return form;
	}
	
	return thoj;
})({});