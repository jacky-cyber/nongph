package cn.globalph.openadmin.server.service.persistence.module.criteria.converter;

import cn.globalph.common.util.BLCSystemProperty;

import org.springframework.stereotype.Component;

/**
 * @author felix.wu
 */
@Component("blStringLikeFilterValueConverter")
public class StringLikeFilterValueConverter implements FilterValueConverter<String> {
    
    protected boolean getOnlyStartsWith() {
        return BLCSystemProperty.resolveBooleanSystemProperty("admin.search.string.onlyStartsWith");
    }

    @Override
    public String convert(String stringValue) {
        return getOnlyStartsWith() ? stringValue.toLowerCase() + "%" : "%" + stringValue.toLowerCase() + "%";
    }

}
