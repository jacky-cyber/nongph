com.felix.exam.module.question.QuestionWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	var form = new com.felix.exam.module.question.QuestionFormPanel();
	var choiceGridPanel = new com.felix.exam.module.question.QuestionChoiceGridPanel();
	
	var clickCreateBtn = function() {
		if ( form.isValid() ) {
			var values = form.getValues();	 
			var type  = values.questionTypeId;
			var answer= values.rightAnswer;
            if( type == '1' || type == '2' ) {
            	var cs = choiceGridPanel.getExtGridPanel().store;
            	if( cs.getCount()==0 ) {
            		com.felix.nsf.MessageBox.alert( TXT.exam_question_set_choice ); 
            		return false;
            	}
            	
            	var allLabel = '';
            	var choiceItems = [];
            	for( var i=0; i<cs.getCount(); i++) {
            		var cr = cs.getAt(i);
            		if( cr.data.labelName=='' ) {
            			com.felix.nsf.MessageBox.alert( TXT.exam_question_set_label ); 
                		return false;
            		}
            		allLabel = allLabel+cr.data.labelName;
            		if( cr.data.content=='' ) {
            			com.felix.nsf.MessageBox.alert( TXT.exam_question_set_content ); 
                		return false;
            		}
            		choiceItems.push(cr.data);
            	}
            	
				
				if( type == '1' ) {
					if( answer.length>1 ) {
						com.felix.nsf.MessageBox.alert( TXT.exam_question_answer_should_one_char ); 
						return false;
					}
					
					if( allLabel.indexOf(answer)==-1 ) {
						com.felix.nsf.MessageBox.alert( '"'+answer+'"' + TXT.exam_question_answer_is_not_label ); 
						return false;
					}
					
				} 
				
				if( type == '2' ) {
					if( answer.length==1 ) {
						com.felix.nsf.MessageBox.alert( TXT.exam_question_answer_should_muti_char ); 
						return false;
					}
					for( var i=0; i<answer.length; i++ ) {
						var l = answer.charAt(i);
						if( allLabel.indexOf(l)==-1 ) {
							com.felix.nsf.MessageBox.alert( '"'+l+'"' + TXT.exam_question_answer_is_not_label ); 
							return false;	
						}
					}
				}
            }
            values.choiceItems = Ext.encode(choiceItems);
            values.positionId = Ext.encode(values.positionId);
            com.felix.nsf.Ajax.requestWithMessageBox("questionAction", "save", function(result){
            	if( result.state=='SUCCESS' ) {
            		thoj.setCloseConfirm( false );
         			thoj.close();
         			thoj.getParent().getParent().reload();
				} else {
					com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
				}
            }, values);
           
		}
	}
	
	var clickResetBtn = function() {
		form.reset();
	}
	
	this.setConfig( { title :TXT.question_win_title,
					  width :650,
					  height:650,
					  autoScroll :false,
					  border :false,
					  resizable :false,
					  minimizable :false,
					  maximizable :false,
					  shadow: false,
					  closeAction:'close',
					  buttonAlign :'center'
					} );
	
	this.setItems( [form, choiceGridPanel] );
	
	this.setButtons( [ { text :TXT.question_position_create_btn, handler : clickCreateBtn }, 
					   { text :TXT.common_reset, handler : clickResetBtn } 
					 ] );
	
	this.show = function(){
		com.felix.nsf.Ajax.requestWithMessageBox("positionAction", "getAll", function(result){
			form.setPositions( result );
			thoj.super.show();
		} );
	}
	
	var initPosition = function(id){
		com.felix.nsf.Ajax.requestWithMessageBox("questionAction", "getQuestionPosition", function(result){
			var position = [];
			for( var i=0; i<result.length; i++)
				position.push( result[i].id );
			form.setValue("positionId",position);
		}, {id: id});
	};
	
	var initChoiceItem = function( choiceItems ){
		for(var i=0; i<choiceItems.length; i++)
			choiceGridPanel.addChoiceItem( choiceItems[i].labelName, choiceItems[i].content );
	};
	
	var initForm = function(id, isCopy){
		com.felix.nsf.Ajax.requestWithMessageBox("questionAction", "get", function(result){
			if( isCopy ){
				result.id='';
				result.name = TXT.exam_question_copy_from + result.name;
			}
			form.setValues( result );
			form.setValue( "questionTypeId", result.questionType.id );
			initChoiceItem( result.choiceItems );
		}, {id: id});
	};
	
	this.edit = function( id ) {
		com.felix.nsf.Ajax.requestWithMessageBox("positionAction", "getAll", function(result){
			form.setPositions( result );
			thoj.super.show();
			initForm( id );
			initPosition( id );
		} );
	}
	
	this.copy = function( id ) {
		com.felix.nsf.Ajax.requestWithMessageBox("positionAction", "getAll", function(result){
			form.setPositions( result );
			thoj.super.show();
			initForm( id, true );
			initPosition( id );
		} );
	}
}