package cn.globalph.common.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jeff Fischer
 */
public class ExceptionHelper {

    private static final Log LOG = LogFactory.getLog(ExceptionHelper.class);

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Class<G> refineType, Class<J> wrapType, String message, Throwable e) {
        if (refineType.isAssignableFrom(e.getClass())) {
            return wrapException(e, wrapType, message);
        }
        if (e.getCause() != null) {
            return refineException(refineType, wrapType, message, e.getCause());
        }
        if (e instanceof UndeclaredThrowableException) {
            return refineException(refineType, wrapType, message, ((UndeclaredThrowableException) e).getUndeclaredThrowable());
        }
        if (e instanceof InvocationTargetException) {
            return refineException(refineType, wrapType, message, ((InvocationTargetException) e).getTargetException());
        }
        return wrapException(e, wrapType, message);
    }

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Class<G> refineType, Class<J> wrapType, Throwable e) {
        return refineException(refineType, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Class<J> wrapType, Throwable e) {
        return refineException(RuntimeException.class, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> RuntimeException refineException(Throwable e) {
        return refineException(RuntimeException.class, RuntimeException.class, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Class<G> refineType, Class<J> wrapType, String message, Throwable e) throws G {
        if (refineType.isAssignableFrom(e.getClass())) {
            throw (G) e;
        }
        if (e.getCause() != null) {
            processException(refineType, wrapType, message, e.getCause());
        }
        if (e instanceof UndeclaredThrowableException) {
            processException(refineType, wrapType, message, ((UndeclaredThrowableException) e).getUndeclaredThrowable());
        }
        if (e instanceof InvocationTargetException) {
            processException(refineType, wrapType, message, ((InvocationTargetException) e).getTargetException());
        }
        throw wrapException(e, wrapType, message);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Class<G> refineType, Class<J> wrapType, Throwable e) throws G {
        processException(refineType, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Class<J> wrapType, Throwable e) throws G {
        processException(RuntimeException.class, wrapType, null, e);
    }

    public static <G extends Throwable, J extends RuntimeException> void processException(Throwable e) throws G {
        processException(RuntimeException.class, RuntimeException.class, null, e);
    }

    private static <J extends RuntimeException> RuntimeException wrapException(Throwable e, Class<J> wrapType, String message) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        try {
            if (StringUtils.isEmpty(message)) {
                return wrapType.getConstructor(Throwable.class).newInstance(e);
            } else {
                return wrapType.getConstructor(String.class, Throwable.class).newInstance(message, e);
            }
        } catch (Exception e1) {
            LOG.error("Could not wrap exception", e1);
            throw new RuntimeException(e);
        }
    }

}
