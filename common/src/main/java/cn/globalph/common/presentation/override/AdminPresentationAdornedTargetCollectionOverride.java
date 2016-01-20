package cn.globalph.common.presentation.override;

import cn.globalph.common.presentation.AdminPresentationAdornedTargetCollection;

/**
 * @author Jeff Fischer
 * @deprecated use {@link AdminPresentationMergeOverrides} instead
 */
@Deprecated
public @interface AdminPresentationAdornedTargetCollectionOverride {

    /**
     * The name of the property whose AdminPresentation annotation should be overwritten
     *
     * @return the name of the property that should be overwritten
     */
    String name();

    /**
     * The AdminPresentation to overwrite the property with
     *
     * @return the AdminPresentation being mapped to the attribute
     */
    AdminPresentationAdornedTargetCollection value();

}
