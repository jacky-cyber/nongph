package cn.globalph.openadmin.dto;

import java.io.Serializable;

/**
 * 
 * @felix.wu
 *
 */
public class DynamicResultSet implements Serializable {

    private static final long serialVersionUID = 1L;

    private ClassMetadata classMetaData;
    private Entity[] records;
    private Integer pageSize;
    private Integer startIndex;
    private Integer totalRecords;
    private Integer batchId;

    public DynamicResultSet() {
        //do nothing
    }

    public DynamicResultSet(ClassMetadata classMetaData, Entity[] records, Integer totalRecords) {
        this.records = records;
        this.classMetaData = classMetaData;
        this.totalRecords = totalRecords;
    }

    public DynamicResultSet(Entity[] records, Integer totalRecords) {
        this.records = records;
        this.totalRecords = totalRecords;
    }

    public DynamicResultSet(ClassMetadata classMetaData) {
        this.classMetaData = classMetaData;
    }

    public ClassMetadata getClassMetaData() {
        return classMetaData;
    }

    public void setClassMetaData(ClassMetadata classMetaData) {
        this.classMetaData = classMetaData;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Entity[] getRecords() {
        return records;
    }

    public void setRecords(Entity[] records) {
        this.records = records;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
