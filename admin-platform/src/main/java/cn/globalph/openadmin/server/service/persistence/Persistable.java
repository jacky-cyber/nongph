package cn.globalph.openadmin.server.service.persistence;

/**
 * @author felix.wu
 */
public interface Persistable<T, G extends Throwable> {

    public T execute() throws G;

}
