package cn.globalph.common.presentation.override;

import cn.globalph.common.presentation.AdminPresentation;

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
public @interface AdminPresentationOverride {
    
    /**
     * The name of the property whose {@link AdminPresentation} annotation should be overwritten
     * 
     * @return the name of the property that should be overwritten
     */
    String name();
    
    /**
     * The {@link AdminPresentation} to overwrite the property with. This is a comprehensive override,
     * meaning whatever was declared on the target property previously will be completely replaced
     * with what is defined in this {@link AdminPresentation}.
     * 
     * @return the {@link AdminPresentation} being mapped to the attribute
     */
    AdminPresentation value() default @AdminPresentation();

}
