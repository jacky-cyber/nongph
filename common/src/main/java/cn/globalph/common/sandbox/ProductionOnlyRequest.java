package cn.globalph.common.sandbox;

import cn.globalph.common.presentation.AdminPresentationAdornedTargetCollection;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;

/**
 * Holder for the {@link #CRITERIA} constant 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface ProductionOnlyRequest {
    
    /**
     * <p>
     * Determines that all admin persistence request should be in production and skip any workflow/sandbox processing
     *  This can be added as customCriteria to any
     * {@link PersistencePackage#getCustomCriteria()} or any of the {@link AdminPresentationCollection#customCriteria()},
     * {@link AdminPresentationAdornedTargetCollection#customCriteria()}, {@link AdminPresentationToOneLookup#customCriteria()}
     * annotations.
     * 
     * <p>
     * One use case for this is for all inventory requests in the advanced inventory module. When executing Sku lookups
     * or actually displaying the list of inventory in the admin, all of those requests should be made against production
     * records
     * 
     * <p>
     * This criteria is only utilized within the enterprise module.
     */
    public static final String CRITERIA = "PRODUCTION_ONLY_REQUEST";

}
