package cn.globalph.openadmin.web.rulebuilder.dto;

import java.io.Serializable;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class FieldDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String label;

    protected String name;

    protected String operators;

    protected String options;

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

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
