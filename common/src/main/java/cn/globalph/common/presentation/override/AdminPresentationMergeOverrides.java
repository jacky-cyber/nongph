package cn.globalph.common.presentation.override;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a non-comprehensive override(部分覆写) of admin presentation annotation
 * property values for a target entity field.
 * 
 * @see cn.globalph.common.presentation.AdminPresentation
 * @see cn.globalph.common.presentation.AdminPresentationToOneLookup
 * @see cn.globalph.common.presentation.AdminPresentationDataDrivenEnumeration
 * @see cn.globalph.common.presentation.AdminPresentationCollection
 * @see cn.globalph.common.presentation.AdminPresentationAdornedTargetCollection
 * @see cn.globalph.common.presentation.AdminPresentationMap
 * @author felix.wu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdminPresentationMergeOverrides {

    /**
     * The new configurations for each field targeted for override
     *
     * @return field specific overrides
     */
    AdminPresentationMergeOverride[] value();

}
