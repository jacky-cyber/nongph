
package cn.globalph.openadmin.web.form.entity;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FieldGroup {

    protected String title;
    protected Integer order;
    protected Set<Field> alternateOrderedFields = new HashSet<Field>();
    protected Set<Field> fields = new HashSet<Field>();

    public Boolean getIsVisible() {
        for (Field f : getFields()) {
            if (f.getIsVisible()) {
                return true;
            }
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public FieldGroup withTitle(String title) {
        setTitle(title);
        return this;
    }
    
    public FieldGroup withOrder(Integer order) {
        setOrder(order);
        return this;
    }

    public boolean addField(Field field) {
        if (field.getAlternateOrdering()) {
            return alternateOrderedFields.add(field);
        } else {
            return fields.add(field);
        }
    }

    public boolean removeField(Field field) {
        if (field.getAlternateOrdering()) {
            return alternateOrderedFields.remove(field);
        } else {
            return fields.remove(field);
        }
    }

    public Set<Field> getFields() {
        List<Field> myFields = new ArrayList<Field>();
        myFields.addAll(fields);
        Collections.sort(myFields, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return new CompareToBuilder()
                    .append(o1.getOrder(), o2.getOrder())
                    .append(o1.getFriendlyName(), o2.getFriendlyName())
                    .append(o1.getName(), o2.getName())
                    .toComparison();
            }
        });
        if (!alternateOrderedFields.isEmpty()) {
            List<Field> mapFieldsList = new ArrayList<Field>(alternateOrderedFields);
            Collections.sort(mapFieldsList, new Comparator<Field>() {
                @Override
                public int compare(Field o1, Field o2) {
                    return new CompareToBuilder()
                        .append(o1.getOrder(), o2.getOrder())
                        .append(o1.getFriendlyName(), o2.getFriendlyName())
                        .append(o1.getName(), o2.getName())
                        .toComparison();
                }
            });
            /*
            alternate ordered fields whose order is less or equal to zero appear first and are
            prepended to the response list in order
             */
            List<Field> smallOrderFields = new ArrayList<Field>();
            for (Field mapField : mapFieldsList) {
                if (mapField.getOrder() <= 0) {
                    smallOrderFields.add(mapField);
                }
            }
            myFields.addAll(0, smallOrderFields);
            /*
            Alternate ordered fields (specifically custom fields) have a different ordering rule than regular fields. For example,
            if a user enters 3 for the field order value for a custom field, that custom field should be the third
            on the form. Regular BLC AdminPresentation fields tends to have orders like 1000, 2000, etc..., so this
            distinction is necessary.
             */
            for (Field mapField : mapFieldsList) {
                if (mapField.getOrder() <= 0) {
                    continue;
                }
                if (mapField.getOrder() < myFields.size() + 1) {
                    myFields.add(mapField.getOrder() - 1, mapField);
                    continue;
                }
                myFields.add(mapField);
            }
        }

        //don't allow any modification of the fields
        return Collections.unmodifiableSet(new LinkedHashSet<Field>(myFields));
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

}
