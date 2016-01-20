package cn.globalph.common.cache.engine;

import java.lang.reflect.Method;

/**
 * 
 * @felix.wu
 *
 */
public class HydrationItemDescriptor {
    
    private String factoryMethod;
    private Method[] mutators;
    
    public String getFactoryMethod() {
        return factoryMethod;
    }
    
    public void setFactoryMethod(String factoryMethod) {
        this.factoryMethod = factoryMethod;
    }
    
    public Method[] getMutators() {
        return mutators;
    }
    
    public void setMutators(Method[] mutators) {
        this.mutators = mutators;
    }

}
