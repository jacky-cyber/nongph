package cn.globalph.common.enumeration.dao;

import cn.globalph.common.enumeration.domain.DataDrivenEnumeration;
import cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue;

public interface DataDrivenEnumerationDao {

    public DataDrivenEnumeration readEnumByKey(String enumKey);

    public DataDrivenEnumerationValue readEnumValueByKey(String enumKey, String enumValueKey);

}
