package cn.globalph.common.extension;

import org.apache.commons.beanutils.BeanComparator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ExtensionManager模式用于查找开箱即用的被扩展的组件。具体的组件由modules扩展.
 * 
 * 定义一个继承ExtensionHandler的组件接口，在modules中实现该组件接口.
 * 
 * 具体的ExtensionManager类使用组件接口作为类型参数.   
 * 
 * 抽象的ExtensionManager类遍列所以的handlers并且检查其{@link ExtensionResultStatusType} 
 * 来决定是否继续其他handlers.
 * 
 * @author felix.wu
 *
 * @param <T> 组件接口
 */
public abstract class ExtensionManager<T extends ExtensionHandler> implements InvocationHandler {

    protected boolean handlersSorted = false;
    protected static String LOCK_OBJECT = new String("EM_LOCK");
    
    protected T extensionHandler;
    protected List<T> handlers = new ArrayList<T>();

    /**
     * Should take in a className that matches the ExtensionHandler interface being managed.
     * @param className
     */
    @SuppressWarnings("unchecked")
    public ExtensionManager(Class<T> _clazz) {
        extensionHandler = (T) Proxy.newProxyInstance(_clazz.getClassLoader(),
                new Class[] { _clazz },
                this);
     }
    
    public T getHandlerProxy() {
        return extensionHandler;
     }

    /**
     * If you are attempting to register a handler with this manager and are invoking this outside of an {@link ExtensionManager}
     * subclass, consider using {@link #registerHandler(ExtensionHandler)} instead.
     * 
     * While the sorting of the handlers prior to their return is thread safe, adding directly to this list is not.
     *   
     * @return a list of handlers sorted by their priority
     * @see {@link #registerHandler(ExtensionHandler)}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> getHandlers() {
        synchronized (LOCK_OBJECT) {
            if (!handlersSorted) {
                if (!handlersSorted) {
                    Comparator fieldCompare = new BeanComparator("priority");
                    Collections.sort(handlers, fieldCompare);
                    handlersSorted = true;
                	 }
            	}
            return handlers;
           }
     }
    
    /**
     * Intended to be invoked from the extension handlers themselves. This will add the given handler to this manager's list of
     * handlers. This also checks to ensure that the handler has not been already registered with this {@link ExtensionManager}
     * by checking the class names of the already-added handlers.
       * 
     * This method is thread safe.
       * 
     * @param handler the handler to register with this extension manager
     * @return true if the handler was successfully registered, false if this handler was already contained in the list of
     * handlers for this manager
     */
    public boolean registerHandler(T handler) {
        synchronized (LOCK_OBJECT) {
            boolean add = true;
            for (T item : this.handlers) {
                if (item.getClass().equals(handler.getClass())) {
                    add = false;
                }
            }
            if (add) {
                this.handlers.add(handler);
                handlersSorted = false;
            }
            
            return add;
        }
    }

    public void setHandlers(List<T> handlers) {
        this.handlers = handlers;
    }
    
    /**
     * Utility method that is useful for determining whether or not an ExtensionManager implementation
     * should continue after processing a ExtensionHandler call.
     * 
     * By default, returns true for CONTINUE
     * 
     * @return
     */
    public boolean shouldContinue(ExtensionResultStatusType result, ExtensionHandler handler,
            Method method, Object[] args) {
        if (result != null) {
            if (ExtensionResultStatusType.HANDLED_STOP.equals(result)) {
                return false;
            }
            
            if (ExtensionResultStatusType.HANDLED.equals(result) && ! continueOnHandled()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns whether or not this extension manager continues on {@link ExtensionResultStatusType}.HANDLED.   
     * 
     * @return
     */
    public boolean continueOnHandled() {
        return false;
    }

    /**
     * {@link ExtensionManager}s don't really need a priority but they pick up this property due to the 
     * fact that we want them to implement the same interface <T> as the handlers they are managing.   
     * 
     * @return
     */
    public int getPriority() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean notHandled = true;
        for (ExtensionHandler handler : getHandlers()) {
            try {
                ExtensionResultStatusType result = (ExtensionResultStatusType) method.invoke(handler, args);
                if (!ExtensionResultStatusType.NOT_HANDLED.equals(result)) {
                    notHandled = false;
                	 }
                if (!shouldContinue(result, handler, method, args)) {
                    break;
                     }
            } catch (InvocationTargetException e) {
                throw e.getCause();
                }
          }
        if (notHandled) {
            return ExtensionResultStatusType.NOT_HANDLED;
        } else {
            return ExtensionResultStatusType.HANDLED;
          }
    }
       
}
