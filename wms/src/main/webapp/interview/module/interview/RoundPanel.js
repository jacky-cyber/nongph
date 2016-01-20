com.felix.interview.module.interview.RoundPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	var thoj = this;
   var form = new com.felix.interview.module.interview.DateTimeFormPanel();
	var grid; 
	var extPanel;
	
	var config = {
		collapsible:false,
		closable:true,
		border  :true,
		margins:'0 0 5 0',
		tools:[ { id:'close', 
			  text:'Close', 
			  handler:function(event, toolE1, panel){
			    	 panel.destroy();
			    	 thoj.getParent().removeItem(thoj);
			    	 thoj.getParent().refresh();
			  } } ]
	};
	
    this.setTitle = function( title ) {
      config.title= title;  
    };

    this.getTitle = function(){
        return config.title;
    };
    
   this.getForm = function(){
	   return form;
    }
    
   this.setGrid = function(g){
        grid = g;
        grid.setParent( this );
    };
    
   this.getGrid = function(){
        return grid;
    };
    
   this.getConfig = function(){
        return config;
    };
    
	this.getExtPanel = function(){
        if( !extPanel ) {
            config["items"] = this.getExtComponent( [form,grid] );
            extPanel = new Ext.Panel( config );
           }
        return extPanel;
	};
	
	this.init = function(round) {
        form.setValue("interviewDate", Date.parseDate(round.planStartTime.substring(0,10), "Y-m-d") );
		form.setValue("interviewTime", round.planStartTime.substring(11,16) );
	};
        
   this.isValid = function(){
        	if( !form.isValid() )
        		return false;
        	
            if( grid.getDataCount()===0 ) {
                com.felix.nsf.MessageBox.alert( "没有设置内容在" + this.getExtPanel().title );
                return false;
            } else {
                if( grid.isValid!==undefined )
                    return grid.isValid();
                else
                    return true;
            }
        };
        
      this.toJSON = function(){
        	var json = {};
        	
            var title = this.getExtPanel().title;
            var ts = title.split("-");
            json.name = ts[0].trim();
        
        	var formValues = form.getValues();
        	json.planStartTime = formValues.interviewDate + ' ' +  formValues.interviewTime + ':00';
            
        	json.gridDatas = [];
            var store = grid.getExtGridPanel().getStore();
            for(var i=0; i<store.getCount(); i++){
                var rd = store.getAt( i );
                json.gridDatas.push( rd.data );
            }
            return json;
        };    
};