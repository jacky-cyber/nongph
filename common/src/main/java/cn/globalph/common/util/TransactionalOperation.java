package cn.globalph.common.util;

/**
 * @author Jeff Fischer
 */
public interface TransactionalOperation {

    void execute() throws Throwable;

}
