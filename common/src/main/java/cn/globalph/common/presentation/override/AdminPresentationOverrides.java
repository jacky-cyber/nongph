package cn.globalph.common.presentation.override;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author pverheyden
 * @deprecated use {@link AdminPresentationMergeOverrides} instead
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deprecated
public @interface AdminPresentationOverrides {

    AdminPresentationOverride[] value() default {};

    AdminPresentationCollectionOverride[] collections() default {};

    AdminPresentationAdornedTargetCollectionOverride[] adornedTargetCollections() default {};

    AdminPresentationMapOverride[] maps() default {};

    AdminPresentationToOneLookupOverride[] toOneLookups() default{};

    AdminPresentationDataDrivenEnumerationOverride[] dataDrivenEnums() default{};
}
