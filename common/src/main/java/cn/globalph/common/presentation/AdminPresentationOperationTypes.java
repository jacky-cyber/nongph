package cn.globalph.common.presentation;

import cn.globalph.common.presentation.client.OperationScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for defining how CRUD operations are performed on an advanced collection in Broadleaf Commerce.
 * This is an advanced configuration, as the default operation settings are appropriate in most cases.
 *
 * @author Jeff Fischer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AdminPresentationOperationTypes {

    /**
     * <p>How should the system execute an addition for this item</p>
     * 
     * <p>OperationType BASIC will result in the item being inserted<BR>
     * OperationType ADORNEDTARGETLIST will result in a adorned target entity being added (not either of the associated entities).<BR>
     * CrossSaleProductImpl is an example of an adorned target entity, since it adds additional fields around the target Product entity.<BR>
     * OperationType MAP will result in the item being added to the requisite map in the containing entity.</p>
     *
     * @return the type of the add operation
     */
    OperationScope addType() default OperationScope.BASIC;

    /**
     * <p>How should the system execute an update for this item</p>
     * 
     * <p>OperationType BASIC will result in the item being updated based on it's primary key<BR>
     * OperationType ADORNEDTARGETLIST will result in a join structure entity being updated (not either of the associated entities).<BR>
     * CrossSaleProductImpl is an example of an adorned target entity, since it adds additional fields around the target Product entity.<BR>
     * OperationType MAP will result in the item being updated to the requisite map in the containing entity.</p>
     *
     * @return the type of the update operation
     */
    OperationScope updateType() default OperationScope.BASIC;

    /**
     * <p>How should the system execute a removal of this item.</p>
     * 
     * <p>OperationType BASIC will result in the item being removed based on its primary key<BR>
     * OperationType NONDESTRUCTIVEREMOVE will result in the item being removed from the containing list in the containing entity. This
     * is useful when you don't want the item to actually be deleted, but simply removed from the parent collection.<BR>
     * OperationType ADORNEDTARGETLIST will result in a join structure being deleted (not either of the associated entities).<BR>
     * CrossSaleProductImpl is an example of an adorned target entity, since it adds additional fields around the target Product entity.<BR>
     * OperationType MAP will result in the item being removed from the requisite map in the containing entity.</p>
     *
     * @return the type of remove operation
     */
    OperationScope removeType() default OperationScope.BASIC;

    /**
     * <p>How should the system execute a fetch</p>
     * 
     * <p>OperationType BASIC will result in a search for items having one or more basic properties matches<BR>
     * OperationType ADORNEDTARGETLIST will result in search for target items that match the parent association in the adorned target entity.<BR>
     * CrossSaleProductImpl is an example of an adorned target entity, since it adds additional fields around the target Product entity.<BR>
     * OperationType MAP will result retrieval of all map entries for the requisite map in the containing entity.</p>
     *
     * @return the type of the fetch operation
     */
    OperationScope fetchType() default OperationScope.BASIC;

    /**
     * <p>OperationType values are generally ignored for inspect and should be defined as BASIC for consistency in most circumstances.
     * This API is meant to support future persistence modules where specialized inspect phase management may be required.</p>
     *
     * @return the type of the inspect operation
     */
    OperationScope inspectType() default OperationScope.BASIC;

}
