package cn.globalph.openadmin.dto;

import java.io.Serializable;

/**
 * @author Jeff Fischer
 */
public class BatchDynamicResultSet implements Serializable {

    protected DynamicResultSet[] dynamicResultSets;

    public DynamicResultSet[] getDynamicResultSets() {
        return dynamicResultSets;
    }

    public void setDynamicResultSets(DynamicResultSet[] dynamicResultSets) {
        this.dynamicResultSets = dynamicResultSets;
    }
}
