package cn.globalph.openadmin.server.service.persistence.module.criteria.converter;

/**
 * @author felix.wu
 */
public interface FilterValueConverter<T> {

    T convert(String stringValue);

}
