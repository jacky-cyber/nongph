package cn.globalph.common.enumeration.service;

import cn.globalph.common.enumeration.domain.DataDrivenEnumeration;
import cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue;

public interface DataDrivenEnumerationService {

    public DataDrivenEnumeration findEnumByKey(String enumKey);

    public DataDrivenEnumerationValue findEnumValueByKey(String enumKey, String enumValueKey);

}
