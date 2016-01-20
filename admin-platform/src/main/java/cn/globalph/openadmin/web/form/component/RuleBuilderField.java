package cn.globalph.openadmin.web.form.component;

import cn.globalph.openadmin.web.form.entity.Field;
import cn.globalph.openadmin.web.rulebuilder.dto.DataWrapper;

/**
 * @author felix.wu
 */
public class RuleBuilderField extends Field {

    protected String fieldBuilder;
    protected String styleClass;
    protected DataWrapper dataWrapper;
    protected String json;
    protected String jsonFieldName;

    public String getFieldBuilder() {
        return fieldBuilder;
    }

    public void setFieldBuilder(String fieldBuilder) {
        this.fieldBuilder = fieldBuilder;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    public void setDataWrapper(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getJsonFieldName() {
        return jsonFieldName;
    }

    public void setJsonFieldName(String jsonFieldName) {
        this.jsonFieldName = jsonFieldName;
    }
}
