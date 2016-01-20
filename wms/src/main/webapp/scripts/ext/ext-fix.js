Ext.form.BasicForm.prototype.setValues = function(values)...{   
      if(values instanceof Array)...{                
          for(var i = 0, len = values.length; i < len; i++)...{   
              var v = values[i];   
              var f = this.findField(v.id);   
              if(f)...{   
                  if ( f.getEl().dom.type == 'radio' ) ...{   
                      var group = this.el.dom.elements[f.getName()];   
                      for (var i=0; i < group.length; i++ ) ...{   
                          if(group[i].__ext_field) ...{   
                              group[i].__ext_field.setValue(group[i].value == v);   
                              if(this.trackResetOnLoad)...{   
                                  group[i].__ext_field.originalValue = group[i].__ext_field.getValue();   
                              }   
                          }   
                      }   
               }   
                else  
                 ...{   
                    f.setValue(v.value);   
                  if(this.trackResetOnLoad)...{   
                        f.originalValue = f.getValue();   
                   }   
               }   
            }   
        }   
    }else...{   
       var field, id;   
       for(id in values)...{   
            if(typeof values[id] != 'function' && (field = this.findField(id)))...{   
               if( field.getEl().dom.type == 'radio' ) ...{   
                   var group = this.el.dom.elements[field.getName()];   
                    for (var i=0; i < group.length; i++ ) ...{   
                        if(group[i].__ext_field) ...{   
                            group[i].__ext_field.setValue(group[i].value == values[id]);   
                            if(this.trackResetOnLoad)...{   
                               group[i].__ext_field.originalValue = group[i].__ext_field.getValue();   
                            }   
                       }   
                    }   
               }   
                else  
                ...{   
                    field.setValue(values[id]);   
                   if(this.trackResetOnLoad)...{   
                         field.originalValue = field.getValue();   
                    }   
                }   
            }   
         }   
    }   
    return this;   
 }   
  
   
  
 Ext.form.Radio.prototype.onRender = function(ct, position) ...{   
  Ext.form.Radio.superclass.onRender.call(this, ct, position);   
   this.el.dom.__ext_field = this;   
 }   
  
 Ext.form.Radio.prototype.setValue = function(v) ...{   
     if(v === true || v === 'true' || v == '1' || v === false || v === 'false' || v == '0') ...{   
   
        // Select all radios of this group   
        var radios = this.el.up('form').select('input[type=radio]');   
  
        // Uncheck all other values   
        for(var i = 0; i < radios.elements.length; i++) ...{   
            if(radios.elements[i].__ext_field && radios.elements[i].__ext_field != this && radios.elements[i].name == this.el.dom.name)   
            ...{   
               radios.elements[i].__ext_field.checked = false;   
                   
                // DOM checked is set by the browser   
  
                radios.elements[i].__ext_field.fireEvent("check", this, this.checked);   
           }   
         }   
            
        this.checked = (v === true || v === 'true' || v == '1');   
        if(this.el && this.el.dom) ...{   
            this.el.dom.checked = this.checked;   
        }   
         this.fireEvent("check", this, this.checked);   
     }   
 }  