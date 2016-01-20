package cn.globalph.common.vendor.service.monitor;

import cn.globalph.common.vendor.service.type.ServiceStatusType;

public interface StatusHandler {

    public void handleStatus(String serviceName, ServiceStatusType status);

}
