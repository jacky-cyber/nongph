package cn.globalph.openadmin.web.rulebuilder.dto;

import java.io.Serializable;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class OperatorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String label;

    protected String name;

    protected String fieldType;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
