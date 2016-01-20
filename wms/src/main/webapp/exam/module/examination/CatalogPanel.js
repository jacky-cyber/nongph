com.felix.exam.module.examination.CatalogPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	var thoj = this;
	
	var extPanel;
	
	var config = {
		title: 'Catalog',
		collapsible:false,
		closable:true,
		border  :true,
		margins:'0 0 5 0',
		tools:[ {id:'close', 
			     text:'Close', 
			     handler:function(event, toolE1, panel){
			    	 panel.destroy();
			    	 thoj.getParent().removeItem(thoj);
			    	 thoj.getParent().refresh();
			     } } ]
	};
	
	var form  = new com.felix.exam.module.examination.CatalogFormPanel();
	var grid = new com.felix.exam.module.examination.CatalogQuestionGridPanel();
	
	form.setParent( this );
	grid.setParent( this );
	
	this.getExtPanel = function(){
            if( !extPanel ) {
                config["items"] = this.getExtComponent( [form, grid] );
                extPanel = new Ext.Panel( config );
            }
            return extPanel;
	};
	
	this.isQuestionExist = function( questionId ){
		if( grid.getDataCount()==0 )
			return false;
			
		var recordIndex = grid.getDataStore().getExtStore().find("id", questionId);
		if( recordIndex>=0 ) {
			var record = grid.getDataStore().getExtStore().getAt(recordIndex);
			alert( record.data.questionName + '('+ record.data.questionType +')' 
					                      + TXT.exam_examination_catalog_in + form.getValues().catalogName + TXT.exam_examination_catalog_question_exist);
			return true;
		} else {
			return false;
		}
	}
	
	this.isValid = function(){
		if( form.isValid() ) {
			if( grid.getDataCount()==0 ) {
				com.felix.nsf.MessageBox.alert( "not select questions in catalog " + form.getValues().catalogName );
				return false;
			} else {
				if( !grid.isValid() ) {
					com.felix.nsf.MessageBox.alert( "not set score in catalog " + form.getValues().catalogName );
					return false;
				}
			}
		}
		return true;
	}
	
	this.asXML = function(){
		var formValues = form.getValues();
		var store = grid.getExtGridPanel().getStore();
		
		var xml = ['<catalog>'];
		xml.push('<name>' + formValues.catalogName + '</name>');
		xml.push('<desc>' + formValues.catalogDesc + '</desc>');
		xml.push('<questions>');
		for(var i=0; i<store.getCount(); i++){
			var rd = store.getAt( i ).data;
			xml.push('<question>');
			xml.push('<id>' + rd.id + '</id>');
			xml.push('<score>' + rd.score + '</score>');
			xml.push('</question>');
		}
		xml.push('</questions>');
		xml.push('</catalog>')
		return xml.join('\n');		
	}
	
	this.init = function( examCatalog ) {
		form.setValues({catalogName:examCatalog.name, catalogDesc:examCatalog.description});
		for( var i=0; i<examCatalog.questions.length; i++){
			var question = examCatalog.questions[i].question;
			grid.addQuestion( question.id, question.name, question.questionType.name, examCatalog.questions[i].score);
		}
	}
}