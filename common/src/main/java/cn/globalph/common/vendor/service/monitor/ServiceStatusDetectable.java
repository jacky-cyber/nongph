package cn.globalph.common.vendor.service.monitor;

import cn.globalph.common.vendor.service.type.ServiceStatusType;

import java.io.Serializable;

public interface ServiceStatusDetectable<T> {

    public ServiceStatusType getServiceStatus();

    public String getServiceName();
    
    public Object process(T arg) throws Exception;

}
