com.felix.exam.module.examination.ExaminationWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var su = new com.felix.nsf.SyncUnit();
	su.setEventCount(2);
	com.felix.nsf.Context.currentComponents.setPositionSyncUnit = su;
	
	var form = new com.felix.exam.module.examination.ExaminationFormPanel();

	var clickOkBtn = function() {
		var items = thoj.getItems();
		if( items.length==1 ) {
			com.felix.nsf.MessageBox.alert(TXT.exam_examination_no_catalog);
			return false;
		} 
			
		for( var i=0; i<items.length; i++ ) {
			if( !items[i].isValid() ){
				return false;
			}
		}
		
		var reqParam = form.getValues();
		
		var catalogsXML = ['<catalogs>']
		for( var i=1; i<items.length; i++ ) {
			catalogsXML.push( items[i].asXML() );
		}
		catalogsXML.push('</catalogs>');
		reqParam.catalogsXML = catalogsXML.join('\n');
		
		com.felix.nsf.Ajax.requestWithMessageBox("examinationAction", 'saveExamination',function(result){
			if( result.state=='SUCCESS' ) {
        		thoj.setCloseConfirm( false );
     			thoj.close();
     			thoj.getParent().getParent().reload();
			} else {
				com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
			}
		}, reqParam);
	}
	
	var clickCloseBtn = function() {
		thoj.close();
	}
	
	this.setConfig( { title :TXT.exam_examination_win_title,
					  width :800,
					  height:700,
					  autoScroll :true,
					  border :true,
					  resizable :true,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  shadow: false,
					  closeAction:'close',
					  buttonAlign :'right',
					  layout: {
						  type:'vbox',
                          padding:'5, 20, 5, 5',
                          align:'stretch'
					  }
					} );
	
	this.setItems( [form] );
	
	this.setButtons( [ { text :TXT.common_btnOK, handler : clickOkBtn }, 
					   { text :TXT.common_btnClose, handler : clickCloseBtn } 
					 ] );
		
	var init= function(exam, isCopy){
		if( isCopy ){
			exam.id='';
			exam.name = TXT.exam_examination_copy_from + exam.name;
		}
		exam.positionId = exam.position.id;
		form.setValues( exam );
		for( var i=0; i<exam.catalogs.length; i++)
			thoj.getItem( i+1 ).init( exam.catalogs[i] );
	};
	
	this.edit = function( id ) {
		com.felix.nsf.Ajax.requestWithMessageBox("examinationAction", "get", function(exam){
			su.setHandler( function(){
				init(exam);
			} );
			thoj.setListeners( {'show':su.addEvent()} );
			
			for( var i=0; i<exam.catalogs.length; i++) {
				var catalog = new com.felix.exam.module.examination.CatalogPanel();
				thoj.addItem( catalog );
			}
			thoj.show();
		}, {id: id} );
	}
	
	this.copy = function( id ) {
		com.felix.nsf.Ajax.requestWithMessageBox("examinationAction", "get", function(exam){
			su.setHandler( function(){
				init(exam, true);
			} );
			thoj.setListeners( {'show':su.addEvent()} );
			
			for( var i=0; i<exam.catalogs.length; i++) {
				var catalog = new com.felix.exam.module.examination.CatalogPanel();
				thoj.addItem( catalog );
			}
			thoj.show();
		}, {id: id} );
	}
	
	this.isQuestionExist = function( questionId ){
		var items = this.getItems();
		for( var i=0; i<items.length; i++ ) {
			var item = items[i];
			if( item instanceof com.felix.exam.module.examination.CatalogPanel ) {
				if( item.isQuestionExist( questionId ) )
					return true;
			}
		}
		return false;
	}
}