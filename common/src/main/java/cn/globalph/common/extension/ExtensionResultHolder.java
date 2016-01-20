package cn.globalph.common.extension;

import java.util.HashMap;
import java.util.Map;

/**
 * If a service extension using the {@link ExtensionManager} pattern expects a result from the extension, it should
 * pass in an instance of this class into the method call.   
 * 
 * The extension points can examine or update this class with response information and set a single return value with
 * {@link #setResult(Object)} or add values via the contextMap provided with {@link #getContextMap()}
 *
 */
public class ExtensionResultHolder<T> {

    protected T result;
    protected Throwable throwable;
    protected Map<String, Object> contextMap = new HashMap<String, Object>();

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Map<String, Object> getContextMap() {
        return contextMap;
    }
}
