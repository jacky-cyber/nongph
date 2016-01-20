package cn.globalph.common.presentation.override;

import cn.globalph.common.presentation.AdminPresentationDataDrivenEnumeration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jeff Fischer
 * @deprecated use {@link AdminPresentationMergeOverrides} instead
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deprecated
public @interface AdminPresentationDataDrivenEnumerationOverride {

    /**
     * The name of the property whose AdminPresentationDataDrivenEnumeration annotation should be overwritten
     *
     * @return the name of the property that should be overwritten
     */
    String name();

    /**
     * The AdminPresentationDataDrivenEnumeration to overwrite the property with
     *
     * @return the AdminPresentation being mapped to the attribute
     */
    AdminPresentationDataDrivenEnumeration value();

}
