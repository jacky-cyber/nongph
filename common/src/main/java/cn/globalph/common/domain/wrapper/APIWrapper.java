package cn.globalph.common.domain.wrapper;

/**
 * This interface is the super interface for all classes that will provide a JAXB wrapper
 * around classes.  Any class that will be exposed via JAXB annotations to the JAXRS API
 * may implement this as a convenience to provide a standard method to populate data objects.
 *
 * This is not a requirement as objects will not generally be passed using a reference to this
 * interface.
 * @param <T>
 */
public interface APIWrapper<T, W> {

    public W wrapDetails(T model);

    public W wrapSummary(T model);

}
