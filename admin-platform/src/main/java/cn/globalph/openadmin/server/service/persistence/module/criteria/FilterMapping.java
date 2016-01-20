package cn.globalph.openadmin.server.service.persistence.module.criteria;

import org.apache.commons.collections.CollectionUtils;

import cn.globalph.openadmin.dto.SortDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查询过滤条件
 * @author felix.wu
 */
public class FilterMapping {

    public static final String RANGE_SPECIFIER_REGEX = "->";

    protected String fullPropertyName;      
    protected List<String> filterValues = new ArrayList<String>();
    protected List directFilterValues = new ArrayList();
    protected SortDirection sortDirection;
    protected Restriction restriction;//约束
    protected FieldPath fieldPath;    //约束路径
    protected Class<?> inheritedFromClass;
    protected Boolean nullsLast = true;

    public FilterMapping withFullPropertyName(String fullPropertyName) {
        setFullPropertyName(fullPropertyName);
        return this;
    }

    public FilterMapping withFilterValues(List<String> filterValues) {
        setFilterValues(filterValues);
        return this;
    }
    
    public FilterMapping withDirectFilterValues(List directFilterValues) {
        setDirectFilterValues(directFilterValues);
        return this;
    }

    public FilterMapping withSortDirection(SortDirection sortDirection) {
        setSortDirection(sortDirection);
        return this;
    }

    public FilterMapping withRestriction(Restriction restriction) {
        setRestriction(restriction);
        return this;
    }

    public FilterMapping withFieldPath(FieldPath fieldPath) {
        setFieldPath(fieldPath);
        return this;
    }
    
    public FilterMapping withInheritedFromClass(Class<?> inheritedFromClass) {
        setInheritedFromClass(inheritedFromClass);
        return this;
    }

    public String getFullPropertyName() {
        return fullPropertyName;
    }

    public void setFullPropertyName(String fullPropertyName) {
        this.fullPropertyName = fullPropertyName;
    }

    public List<String> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(List<String> filterValues) {
        if (CollectionUtils.isNotEmpty(directFilterValues)) {
            throw new IllegalArgumentException("Cannot set both filter values and direct filter values");
        }
        
        List<String> parsedValues = new ArrayList<String>();
        for (String unfiltered : filterValues) {
            parsedValues.addAll(Arrays.asList(parseFilterValue(unfiltered)));
        }
        this.filterValues.addAll(parsedValues);
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }

    public FieldPath getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(FieldPath fieldPath) {
        this.fieldPath = fieldPath;
    }
    
    public List getDirectFilterValues() {
        return directFilterValues;
    }
    
    public void setDirectFilterValues(List directFilterValues) {
        if (CollectionUtils.isNotEmpty(filterValues)) {
            throw new IllegalArgumentException("Cannot set both filter values and direct filter values");
        }
        this.directFilterValues = directFilterValues;
    }
    
    public Class<?> getInheritedFromClass() {
        return inheritedFromClass;
    }

    public void setInheritedFromClass(Class<?> inheritedFromClass) {
        this.inheritedFromClass = inheritedFromClass;
    }

    protected String[] parseFilterValue(String filterValue) {
        //We do it this way because the String.split() method will return only a single array member
        //when there is nothing on one side of the delimiter. We want to have two array members (one empty)
        //in this case.
        String[] vals;
        if (filterValue.contains(RANGE_SPECIFIER_REGEX)) {
            vals = new String[]{filterValue.substring(0, filterValue.indexOf(RANGE_SPECIFIER_REGEX)),
                filterValue.substring(filterValue.indexOf(RANGE_SPECIFIER_REGEX) + RANGE_SPECIFIER_REGEX.length(),
                filterValue.length())};
        } else {
            vals = new String[]{filterValue};
        }
        for (int j=0;j<vals.length;j++) {
            vals[j] = vals[j].trim();
        }
        return vals;
    }

    public Boolean getNullsLast() {
        return nullsLast;
    }

    public void setNullsLast(Boolean nullsLast) {
        this.nullsLast = nullsLast;
    }
}
