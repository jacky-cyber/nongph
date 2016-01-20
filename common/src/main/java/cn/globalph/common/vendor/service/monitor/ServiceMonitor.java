package cn.globalph.common.vendor.service.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import cn.globalph.common.vendor.service.monitor.handler.LogStatusHandler;
import cn.globalph.common.vendor.service.type.ServiceStatusType;

import java.util.HashMap;
import java.util.Map;

public class ServiceMonitor {

    private static final Log LOG = LogFactory.getLog(ServiceMonitor.class);

    protected Map<ServiceStatusDetectable, StatusHandler> serviceHandlers = new HashMap<ServiceStatusDetectable, StatusHandler>();
    protected StatusHandler defaultHandler = new LogStatusHandler();
    protected Map<ServiceStatusDetectable, ServiceStatusType> statusMap = new HashMap<ServiceStatusDetectable, ServiceStatusType>();

    public synchronized void init() {
        for (ServiceStatusDetectable statusDetectable : serviceHandlers.keySet()) {
            checkService(statusDetectable);
        }
    }

    public Object checkServiceAOP(ProceedingJoinPoint call) throws Throwable {
        try {
            checkService((ServiceStatusDetectable) call.getThis());
        } catch (Throwable e) {
            LOG.error("Could not check service status", e);
        }
        return call.proceed();
    }

    public void checkService(ServiceStatusDetectable statusDetectable) {
        ServiceStatusType type = statusDetectable.getServiceStatus();
        if (!statusMap.containsKey(statusDetectable)) {
            statusMap.put(statusDetectable, type);
            if (type.equals(ServiceStatusType.DOWN)) {
                handleStatusChange(statusDetectable, type);
            }
        }
        if (!statusMap.get(statusDetectable).equals(type)) {
            handleStatusChange(statusDetectable, type);
            statusMap.put(statusDetectable, type);
        }
    }

    protected void handleStatusChange(ServiceStatusDetectable serviceStatus, ServiceStatusType serviceStatusType) {
        if (serviceHandlers.containsKey(serviceStatus)) {
            serviceHandlers.get(serviceStatus).handleStatus(serviceStatus.getServiceName(), serviceStatusType);
        } else {
            defaultHandler.handleStatus(serviceStatus.getServiceName(), serviceStatusType);
        }
    }

    public Map<ServiceStatusDetectable, StatusHandler> getServiceHandlers() {
        return serviceHandlers;
    }

    public void setServiceHandlers(Map<ServiceStatusDetectable, StatusHandler> serviceHandlers) {
        this.serviceHandlers = serviceHandlers;
    }

    public StatusHandler getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(StatusHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }
}
