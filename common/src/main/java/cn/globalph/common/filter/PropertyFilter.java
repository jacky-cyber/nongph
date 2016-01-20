package cn.globalph.common.filter;

/**
 * @author Jeff Fischer
 */
public class PropertyFilter extends Filter {

    protected boolean isJoinTableFilter = false;
    protected String propertyName;

    public Boolean getJoinTableFilter() {
        return isJoinTableFilter;
    }

    public void setJoinTableFilter(Boolean joinTableFilter) {
        isJoinTableFilter = joinTableFilter;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
