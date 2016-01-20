package cn.globalph.common.presentation.override;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a non-comprehensive override of admin presentation annotation
 * property values for a target entity field.
 *
 * @see cn.globalph.common.presentation.AdminPresentation
 * @see cn.globalph.common.presentation.AdminPresentationToOneLookup
 * @see cn.globalph.common.presentation.AdminPresentationDataDrivenEnumeration
 * @see cn.globalph.common.presentation.AdminPresentationCollection
 * @see cn.globalph.common.presentation.AdminPresentationAdornedTargetCollection
 * @see cn.globalph.common.presentation.AdminPresentationMap
 * @author Jeff Fischer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdminPresentationMergeOverride {

    /**
     * The name of the property whose admin presentation annotation should be overwritten
     *
     * @return the name of the property that should be overwritten
     */
    String name();

    /**
     * The array of override configuration values. Each entry correlates to a property on
     * {@link cn.globalph.common.presentation.AdminPresentation},
     * {@link cn.globalph.common.presentation.AdminPresentationToOneLookup},
     * {@link cn.globalph.common.presentation.AdminPresentationDataDrivenEnumeration},
     * {@link cn.globalph.common.presentation.AdminPresentationAdornedTargetCollection},
     * {@link cn.globalph.common.presentation.AdminPresentationCollection} or
     * {@link cn.globalph.common.presentation.AdminPresentationMap}
     *
     * @return The array of override configuration values.
     */
    AdminPresentationMergeEntry[] mergeEntries();
}
