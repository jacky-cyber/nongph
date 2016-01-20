package cn.globalph.openadmin.dto;

import org.apache.commons.collections.CollectionUtils;

import cn.globalph.common.util.BLCCollectionUtils;
import cn.globalph.common.util.TypedPredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 属性值过滤和排序条件
 * @author felix.wu
 *
 */
public class FilterAndSortCriteria {

    public static final String SORT_PROPERTY_PARAMETER = "sortProperty";
    public static final String SORT_DIRECTION_PARAMETER = "sortDirection";
    public static final String START_INDEX_PARAMETER = "startIndex";
    public static final String MAX_INDEX_PARAMETER = "maxIndex";
    
    public static final String IS_NULL_FILTER_VALUE = new String("BLC_SPECIAL_FILTER_VALUE:NULL").intern();
    public static final String IS_NOT_NULL_FILTER_VALUE = new String("BLC_SPECIAL_FILTER_VALUE:NOT_NULL").intern();

    protected String propertyId;
    protected List<String> filterValues = new ArrayList<String>();

    protected SortDirection sortDirection;

    public FilterAndSortCriteria(String propertyId) {
        this.propertyId = propertyId;
    }
    
    public FilterAndSortCriteria(String propertyId, String filterValue) {
        this.propertyId = propertyId;
        setFilterValue(filterValue);
    }
    
    public FilterAndSortCriteria(String propertyId, List<String> filterValues) {
        setPropertyId(propertyId);
        setFilterValues(filterValues);
    }
    
    public FilterAndSortCriteria(String propertyId, List<String> filterValues, SortDirection sortDirection) {
        setPropertyId(propertyId);
        setFilterValues(filterValues);
        setSortDirection(sortDirection);
    }
    
    public FilterAndSortCriteria(String propertyId, String[] filterValues) {
        this.propertyId = propertyId;
        setFilterValues(Arrays.asList(filterValues));
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public void clearFilterValues() {
        filterValues.clear();
    }

    public void setFilterValue(String value) {
        clearFilterValues();
        addFilterValue(value);
    }

    public void addFilterValue(String value) {
        filterValues.add(value);
    }

    public List<String> getFilterValues() {
        // We want values that are NOT special
        return BLCCollectionUtils.selectList(filterValues, getPredicateForSpecialValues(false));
    }

    public List<String> getSpecialFilterValues() {
        // We want values that ARE special
        return BLCCollectionUtils.selectList(filterValues, getPredicateForSpecialValues(true));
    }

    public void setFilterValues(List<String> filterValues) {
        this.filterValues = filterValues;
    }

    public Boolean getSortAscending() {
        return (sortDirection == null) ? null : SortDirection.ASCENDING.equals(sortDirection);
    }

    public void setSortAscending(Boolean sortAscending) {
        this.sortDirection = (sortAscending) ? SortDirection.ASCENDING : SortDirection.DESCENDING;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public boolean hasSpecialFilterValue() {
        // We want values that ARE special
        return CollectionUtils.exists(filterValues, getPredicateForSpecialValues(true));
    }
    
    protected TypedPredicate<String> getPredicateForSpecialValues(final boolean inclusive) {
        return new TypedPredicate<String>() {
            @Override
            public boolean eval(String value) {
                // Note that this static String is the result of a call to String.intern(). This means that we are
                // safe to compare with == while still allowing the user to specify a filter for the actual value of this
                // string.
                if (inclusive) {
                    return IS_NULL_FILTER_VALUE == value || IS_NOT_NULL_FILTER_VALUE == value;
                } else {
                    return IS_NULL_FILTER_VALUE != value && IS_NOT_NULL_FILTER_VALUE != value;
                }
            }
        };
    }
}
