package cn.globalph.openadmin.dto;

import cn.globalph.common.presentation.client.OperationScope;

import java.io.Serializable;

/**
 * @author felix.wu
 */
public class PersistenceOperationType implements Serializable {

    private static final long serialVersionUID = 1L;

    private OperationScope fetchType = OperationScope.BASIC;
    private OperationScope removeType = OperationScope.BASIC;
    private OperationScope addType = OperationScope.BASIC;
    private OperationScope updateType = OperationScope.BASIC;
    private OperationScope inspectType = OperationScope.BASIC;

    public PersistenceOperationType() {
        //do nothing
    }

    public PersistenceOperationType(OperationScope fetchType, OperationScope removeType, OperationScope addType, OperationScope updateType, OperationScope inspectType) {
        this.removeType = removeType;
        this.addType = addType;
        this.updateType = updateType;
        this.fetchType = fetchType;
        this.inspectType = inspectType;
    }

    /**
     * How should the system execute a removal of this item.
     * <p/>
     * OperationType BASIC will result in the item being removed based on its primary key
     * OperationType NONDESTRUCTIVEREMOVE will result in the item being removed from the containing list in the containing entity. This
     * is useful when you don't want the item to actually be deleted, but simply removed from the parent collection.
     * OperationType ADORNEDTARGETLIST will result in a join structure being deleted (not either of the associated entities).
     * cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl is an example of a join structure entity.
     * OperationType MAP will result in the item being removed from the requisite map in the containing entity.
     *
     * @return the type of remove operation
     */
    public OperationScope getRemoveType() {
        return removeType;
    }

    /**
     * How should the system execute a removal of this item.
     * <p/>
     * OperationType BASIC will result in the item being removed based on its primary key
     * OperationType NONDESTRUCTIVEREMOVE will result in the item being removed from the containing list in the containing entity. This
     * is useful when you don't want the item to be removed to actually be deleted, but simply removed from the parent collection.
     * OperationType ADORNEDTARGETLIST will result in a join structure being deleted (not either of the associated entities).
     * cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl is an example of a join structure entity.
     * OperationType MAP will result in the item being removed from the requisite map in the containing entity.
     *
     * @param removeType
     */
    public void setRemoveType(OperationScope removeType) {
        this.removeType = removeType;
    }

    /**
     * How should the system execute an addition for this item
     * <p/>
     * OperationType BASIC will result in the item being inserted
     * OperationType NONDESTRUCTIVEREMOVE is not supported and will result in the same behavior as BASIC. Note, any foreign key associations in the
     * persistence perspective (@see PersistencePerspective) will be honored during the BASIC based add.
     * OperationType ADORNEDTARGETLIST will result in a join structure entity being added (not either of the associated entities).
     * cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl is an example of a join structure entity.
     * OperationType MAP will result in the item being added to the requisite map in the containing entity.
     *
     * @return the type of the add operation
     */
    public OperationScope getAddType() {
        return addType;
    }

    /**
     * How should the system execute an addition for this item
     * <p/>
     * OperationType BASIC will result in the item being inserted
     * OperationType NONDESTRUCTIVEREMOVE is not supported and will result in the same behavior as BASIC. Note, any foreign key associations in the
     * persistence perspective (@see PersistencePerspective) will be honored during the BASIC based add.
     * OperationType ADORNEDTARGETLIST will result in a join structure entity being added (not either of the associated entities).
     * cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl is an example of a join structure entity.
     * OperationType MAP will result in the item being added to the requisite map in the containing entity.
     *
     * @param addType
     */
    public void setAddType(OperationScope addType) {
        this.addType = addType;
    }

    /**
     * How should the system execute an update for this item
     * <p/>
     * OperationType BASIC will result in the item being updated based on it's primary key
     * OperationType NONDESTRUCTIVEREMOVE is not supported and will result in the same behavior as BASIC. Note, any foreign key associations in the
     * persistence perspective (@see PersistencePerspective) will be honored during the BASIC based update.
     * OperationType ADORNEDTARGETLIST will result in a join structure entity being updated (not either of the associated entities).
     * cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl is an example of a join structure entity.
     * OperationType MAP will result in the item being updated to the requisite map in the containing entity.
     *
     * @return the type of the update operation
     */
    public OperationScope getUpdateType() {
        return updateType;
    }

    /**
     * How should the system execute an update for this item
     * <p/>
     * OperationType BASIC will result in the item being updated based on it's primary key
     * OperationType NONDESTRUCTIVEREMOVE is not supported and will result in the same behavior as BASIC. Note, any foreign key associations in the
     * persistence perspective (@see PersistencePerspective) will be honored during the BASIC based update.
     * OperationType ADORNEDTARGETLIST will result in a join structure entity being updated (not either of the associated entities).
     * cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl is an example of a join structure entity.
     * OperationType MAP will result in the item being updated to the requisite map in the containing entity.
     *
     * @param updateType
     */
    public void setUpdateType(OperationScope updateType) {
        this.updateType = updateType;
    }

    /**
     * How should the system execute a fetch
     * <p/>
     * OperationType BASIC will result in a search for items having one or more basic properties matches
     * OperationType FOREINKEY is not support and will result in the same behavior as BASIC. Note, any foreign key associations will be included
     * as part of the query.
     * OperationType ADORNEDTARGETLIST will result in search for items that match one of the associations in a join structure. For example, CategoryProductXrefImpl
     * is used in a AdornedTargetList fetch to retrieve all products for a particular category.
     * OperationType MAP will result retrieval of all map entries for the requisite map in the containing entity.
     *
     * @return the type of the fetch operation
     */
    public OperationScope getFetchType() {
        return fetchType;
    }

    /**
     * How should the system execute a fetch
     * <p/>
     * OperationType BASIC will result in a search for items having one or more basic properties matches
     * OperationType FOREINKEY is not support and will result in the same behavior as BASIC. Note, any foreign key associations will be included
     * as part of the query.
     * OperationType ADORNEDTARGETLIST will result in search for items that match one of the associations in a join structure. For example, CategoryProductXrefImpl
     * is used in a AdornedTargetList fetch to retrieve all products for a particular category.
     * OperationType MAP will result retrieval of all map entries for the requisite map in the containing entity.
     *
     * @param fetchType
     */
    public void setFetchType(OperationScope fetchType) {
        this.fetchType = fetchType;
    }

    /**
     * OperationType values are generally ignored for inspect and should be defined as BASIC for consistency in most circumstances.
     * This API is meant to support future persistence modules where specialized inspect phase management may be required.
     *
     * @return the type of the inspect operation
     */
    public OperationScope getInspectType() {
        return inspectType;
    }

    /**
     * OperationType values are generally ignored for inspect and should be defined as BASIC for consistency in most circumstances.
     * This API is meant to support future persistence modules where specialized inspect phase management may be required.
     *
     * @param inspectType
     */
    public void setInspectType(OperationScope inspectType) {
        this.inspectType = inspectType;
    }

    public PersistenceOperationType cloneOperationTypes() {
        PersistenceOperationType operationTypes = new PersistenceOperationType();
        operationTypes.setAddType(addType);
        operationTypes.setFetchType(fetchType);
        operationTypes.setInspectType(inspectType);
        operationTypes.setRemoveType(removeType);
        operationTypes.setUpdateType(updateType);

        return  operationTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistenceOperationType)) return false;

        PersistenceOperationType that = (PersistenceOperationType) o;

        if (addType != that.addType) return false;
        if (fetchType != that.fetchType) return false;
        if (inspectType != that.inspectType) return false;
        if (removeType != that.removeType) return false;
        if (updateType != that.updateType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fetchType != null ? fetchType.hashCode() : 0;
        result = 31 * result + (removeType != null ? removeType.hashCode() : 0);
        result = 31 * result + (addType != null ? addType.hashCode() : 0);
        result = 31 * result + (updateType != null ? updateType.hashCode() : 0);
        result = 31 * result + (inspectType != null ? inspectType.hashCode() : 0);
        return result;
    }
}
