package cn.globalph.common.enumeration.service;

import javax.annotation.Resource;

import cn.globalph.common.enumeration.dao.DataDrivenEnumerationDao;
import cn.globalph.common.enumeration.domain.DataDrivenEnumeration;
import cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue;

import org.springframework.stereotype.Service;


@Service("blDataDrivenEnumerationService")
public class DataDrivenEnumerationServiceImpl implements DataDrivenEnumerationService {

    @Resource(name = "blDataDrivenEnumerationDao")
    protected DataDrivenEnumerationDao dao;

    @Override
    public DataDrivenEnumeration findEnumByKey(String enumKey) {
        return dao.readEnumByKey(enumKey);
    }
    
    @Override
    public DataDrivenEnumerationValue findEnumValueByKey(String enumKey, String enumValueKey) {
        return dao.readEnumValueByKey(enumKey, enumValueKey);
    }

}
