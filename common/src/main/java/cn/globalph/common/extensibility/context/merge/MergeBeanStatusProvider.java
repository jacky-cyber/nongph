package cn.globalph.common.extensibility.context.merge;

import org.springframework.context.ApplicationContext;


public interface MergeBeanStatusProvider {
    
    /**
     * Typically used by the {@link AbstractMergeBeanPostProcessor} class to determine whether or not certain
     * lists should be processed or if they can be safely ignored.
     * 
     * @param bean
     * @param beanName
     * @param appCtx
     * @return whether or not processing should be triggered
     */
    public boolean isProcessingEnabled(Object bean, String beanName, ApplicationContext appCtx);

}
