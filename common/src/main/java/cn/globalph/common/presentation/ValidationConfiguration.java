package cn.globalph.common.presentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @felix.wu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidationConfiguration {
    
    /**
     * <p>The fully qualified classname of the cn.globalph.openadmin.server.service.persistence.validation.PropertyValidator
     * instance to use for validation</p>
     * 
     * <p>If you need to do dependency injection, this can also correspond to a bean ID that implements the 
     * cn.globalph.openadmin.server.service.persistence.validation.PropertyValidator interface.</p>
     * 
     * @return the validator implementation. This implementation should be an instance of
     * cn.globalph.openadmin.server.service.persistence.validation.PropertyValidator and could be either a bean
     * name or fully-qualified class name
     */
    String validationImplementation();
    
    /**
     * <p>Optional configuration items that can be used to setup the validator</p>. Most validators should have at least
     * a single configuration item with {@link ConfigurationItem#ERROR_MESSAGE}.
     * 
     * @return validator configuration attributes
     */
    ConfigurationItem[] configurationItems() default {};
}
