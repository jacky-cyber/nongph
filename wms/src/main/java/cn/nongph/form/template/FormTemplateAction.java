package com.felix.form.template;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.felix.form.model.FormTemplate;
import com.felix.form.model.FormTemplateField;
import com.felix.form.model.FormTemplateFieldAction;
import com.felix.form.model.FormTemplateFieldOption;
import com.felix.nsf.Pagination;
import com.felix.nsf.response.CommonResult;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named
public class FormTemplateAction {
	private int start;
	private int limit;
	private String pattern;
	private String id;
	private String name;
	private String layout;
	private String fields;
	
	@Inject
	private FormTemplateService service;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	
	public Pagination search(){
		Pagination page = new Pagination();
		page.setStart( start );
		page.setLimit( limit );
		if( pattern==null ) {
			pattern = "%";
		} else {
			pattern = "%" + pattern + "%";
		}
		service.search( page, pattern );
		return page;
	}
	
	public CommonResult save(){
		FormTemplate ft;
		if( StringUtils.isEmpty(id) ) {
			ft = new FormTemplate();
		} else {
			ft = service.get( id );
		}
		ft.setName( name );
		ft.setLayout( layout );
		List<FormTemplateField> ftfs = new ArrayList<FormTemplateField>();
		try{
			JSONArray a = new JSONArray(fields);
			for( int i=0; i<a.length(); i++ ) {
				JSONObject o = a.getJSONObject(i);
				
				FormTemplateField field = new FormTemplateField();
				field.setName( o.getString( "name") );
				field.setLabel( o.getString("label") );
				field.setType( FormTemplateField.TYPE.valueOf( o.getString("type" ) ) );
				field.setReadOnly( o.getBoolean( "readOnly" ) );
				field.setMandatory( o.getBoolean( "mandatory" ) );
				field.setInitValue( o.getString("initValue") );
				
				JSONArray aa = o.getJSONArray( "actions" );
				if( aa!=null && aa.length()>0 ) {
					for( int j1=0; j1<aa.length(); j1++ ) {
						JSONObject action = aa.getJSONObject( j1 );
						FormTemplateFieldAction ftfa = new FormTemplateFieldAction();
						ftfa.setOnEvent( action.getString("onEvent") );
						ftfa.setScriptType( action.getString( "scriptType") );
						ftfa.setScript( action.getString( "script" ) );
						ftfa.setField( field );
						
						field.getActions().add( ftfa );
					}
				}
				
				if( field.getType()==FormTemplateField.TYPE.text ){
					field.setMaxSize( o.getString("maxSize").isEmpty()?null: o.getInt( "maxSize" ) );
					field.setRegex( o.getString("regex") );
					field.setRegexText( o.getString("regexText") );
				} else if( field.getType()==FormTemplateField.TYPE.comboBox 
						    || field.getType()==FormTemplateField.TYPE.checkBox ) {
					JSONArray oa = o.getJSONArray( "options" );
					if( oa!=null && oa.length()>0 ) {
						for( int j2=0; j2<oa.length(); j2++ ) {
							JSONObject option = oa.getJSONObject(j2);
							FormTemplateFieldOption ftfo = new FormTemplateFieldOption();
							ftfo.setLabel( option.getString( "label" ) );
							ftfo.setValue( option.getString( "value" ) );
							ftfo.setField( field );
							
							field.getOptions().add( ftfo );
						}
					}
				}
				
				field.setFormTemplate( ft );
				ftfs.add( field );
			}
		} catch (JSONException e ) {
                    return CommonResult.createFailureResult(null, e.getMessage());
		}
		
		for( FormTemplateField f : ft.getFields() )
                    service.delete( f );
		ft.setFields( ftfs );
		service.persist( ft );
		return CommonResult.createSuccessResult();
	}
	
	public FormTemplate get(){
		FormTemplate template = service.get(id);
		for( FormTemplateField field : template.getFields() ) {
			for( FormTemplateFieldAction action : field.getActions() ) 
				action.getOnEvent();
			
			for( FormTemplateFieldOption option : field.getOptions() )
				option.getLabel();
		}
		return template;
	}
	
	public CommonResult delete(){
		FormTemplate template = service.get(id);
		service.delete( template );
		return CommonResult.createSuccessResult();
	}
        
        public List<FormTemplate> getAll(){
            return service.getAll();
        }
}
