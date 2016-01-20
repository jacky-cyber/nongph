package cn.globalph.openadmin.web.rulebuilder.dto;

import cn.globalph.common.presentation.client.SupportedFieldType;

/**
 * @author Elbert Bautista (elbertbautista)
 *
 * A temporary container object used to load the data into a RuleBuilderFieldService
 * @see cn.globalph.openadmin.web.rulebuilder.service.RuleBuilderFieldService
 * @see cn.globalph.openadmin.web.rulebuilder.service.OrderItemFieldServiceImpl
 *
 *
 */
public class FieldData {

    protected String fieldLabel;
    protected String fieldName;
    protected String operators;
    protected String options;
    protected SupportedFieldType fieldType;
    protected SupportedFieldType secondaryFieldType;

    private FieldData(Builder builder) {
        this.fieldLabel = builder.fieldLabel;
        this.fieldName = builder.fieldName;
        this.operators = builder.operators;
        this.options = builder.options;
        this.fieldType = builder.fieldType;
        this.secondaryFieldType = builder.secondaryFieldType;
    }

    public static class Builder {
        protected String fieldLabel = null;
        protected String fieldName = null;
        protected String operators = null;
        protected String options = null;
        protected SupportedFieldType fieldType = null;
        protected SupportedFieldType secondaryFieldType = null;

        public FieldData build() {
            return new FieldData(this);
        }

        public Builder() {}

        public Builder label(String fieldLabel) {
            this.fieldLabel = fieldLabel;
            return this;
        }

        public Builder name(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder operators(String operators) {
            this.operators = operators;
            return this;
        }

        public Builder options(String options) {
            this.options = options;
            return this;
        }

        public Builder type(SupportedFieldType fieldType) {
            this.fieldType = fieldType;
            return this;
        }

        public Builder secondaryType(SupportedFieldType fieldType) {
            this.secondaryFieldType = fieldType;
            return this;
        }
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getOperators() {
        return operators;
    }

    public String getOptions() {
        return options;
    }

    public SupportedFieldType getFieldType() {
        return fieldType;
    }

    public SupportedFieldType getSecondaryFieldType() {
        return secondaryFieldType;
    }
}
