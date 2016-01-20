package cn.globalph.openadmin.web.form.component;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.util.BLCMessageUtils;
import cn.globalph.openadmin.web.form.entity.Field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * grid row record
 * @author felix.wu
 *
 */
public class ListGridRecord {

    protected ListGrid listGrid; //父容器
    protected String id;
    protected String altId;
    protected List<Field> fields = new ArrayList<Field>();
    protected List<Field> hiddenFields = new ArrayList<Field>();
    protected Boolean isDirty;
    protected Boolean isError;
    protected String errorKey;
    protected ListGridRecordIcon icon;

    /**
     * Convenience map keyed by the field name. Used to guarantee field ordering with header fields within a ListGrid
     */
    protected Map<String, Field> fieldMap;
    
    public String getPath() {
        return listGrid.getPath() + "/" + id;
    }
    
    public boolean getCanLinkToExternalEntity() {
        return StringUtils.isNotBlank(listGrid.getExternalEntitySectionKey());
    }
    
    public String getExternalEntityPath() {
        return listGrid.getExternalEntitySectionKey() + "/" + id;
    }

    public ListGrid getListGrid() {
        return listGrid;
    }
    
    public void setListGrid(ListGrid listGrid) {
        this.listGrid = listGrid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public int getIndex() {
        return listGrid.getStartIndex() + listGrid.getRecords().indexOf(this);
    }

    /**
     * Normally you should not be looping through these fields. In order to preserve proper field ordering, instead you
     * should loop through {@link ListGrid#getHeaderFields()} and then invoke the {@link #getField(String)} method
     * with that header field name.
     * 
     * @return
     */
    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(List<Field> hiddenFields) {
        this.hiddenFields = hiddenFields;
    }

    /**
     * Returns a {@link Field} in this record for a particular field name. Used when displaying a {@link ListGrid} in order
     * to guarantee proper field ordering
     * 
     * @param fieldName
     * @return
     */
    public Field getField(String fieldName) {
        if (fieldMap == null) {
            fieldMap = new LinkedHashMap<String, Field>();
            for (Field field : fields) {
                fieldMap.put(field.getName(), field);
            }
            for (Field hiddenField : hiddenFields) {
                fieldMap.put(hiddenField.getName(), hiddenField);
            }
        }
        Field field = fieldMap.get(fieldName);
        
        // We'll return a null field is this particular record doesn't have this polymorphic property.
        // This prevents NPEs in list grids
        if (field == null) {
            field = new Field();
        }
        
        return field;
    }
    
    public void clearFieldMap() {
        fieldMap = null;
    }

    public String getHiddenFieldsJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"hiddenFields\":[");
        for (int j=0;j<hiddenFields.size();j++) {
            sb.append("{\"name\":\"");
            sb.append(hiddenFields.get(j).getName());
            sb.append("\",\"val\":\"");
            sb.append(hiddenFields.get(j).getValue());
            sb.append("\"}");
            if (j < hiddenFields.size()-1) {
                sb.append(",");
            }
        }
        sb.append("]}");

        return sb.toString();
    }

    public Boolean getIsDirty() {
        return isDirty == null ? false : isDirty;
    }

    public void setDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public Boolean getIsError() {
        return isError == null ? false : isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }
    
    public ListGridRecordIcon getIcon() {
        if (icon != null) {
            return icon;
        }
        
        if (getIsError()) {
            return new ListGridRecordIcon()
                .withCssClass("icon-exclamation-sign")
                .withMessage(BLCMessageUtils.getMessage(getErrorKey()));
        }

        if (getIsDirty()) {
            return new ListGridRecordIcon()
                .withCssClass("icon-pencil")
                .withMessage(BLCMessageUtils.getMessage("listgrid.record.edited"));
        }
        
        return null;
    }
    
    public void setIcon(ListGridRecordIcon icon) {
        this.icon = icon;
    }
    
    public Boolean getHasIcon() {
        return icon != null || getIsError() || getIsDirty();
    }
    
    public String getAltId() {
        return altId;
    }

    public void setAltId(String altId) {
        this.altId = altId;
    }
    
}
