package cn.globalph.b2c.workflow;

import java.util.Map;

import cn.globalph.b2c.workflow.state.RollbackHandler;

import org.springframework.core.Ordered;

public abstract class BaseActivity<T extends ProcessContext<?>> implements Activity<T> {
    
    protected ErrorHandler errorHandler;
    protected String beanName;

    protected RollbackHandler<T> rollbackHandler;
    protected String rollbackRegion;
    protected Map<String, Object> stateConfiguration;
    protected boolean automaticallyRegisterRollbackHandler = false;
    protected int order = Ordered.LOWEST_PRECEDENCE;
    
    @Override
    public boolean shouldExecute(T context) {
        return true;
    }
    
    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public void setBeanName(final String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setErrorHandler(final ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public RollbackHandler<T> getRollbackHandler() {
        return rollbackHandler;
    }

    @Override
    public void setRollbackHandler(RollbackHandler<T> rollbackHandler) {
        this.rollbackHandler = rollbackHandler;
    }

    @Override
    public String getRollbackRegion() {
        return rollbackRegion;
    }

    @Override
    public void setRollbackRegion(String rollbackRegion) {
        this.rollbackRegion = rollbackRegion;
    }

    @Override
    public Map<String, Object> getStateConfiguration() {
        return stateConfiguration;
    }

    @Override
    public void setStateConfiguration(Map<String, Object> stateConfiguration) {
        this.stateConfiguration = stateConfiguration;
    }

    @Override
    public boolean getAutomaticallyRegisterRollbackHandler() {
        return automaticallyRegisterRollbackHandler;
    }

    @Override
    public void setAutomaticallyRegisterRollbackHandler(boolean automaticallyRegisterRollbackHandler) {
        this.automaticallyRegisterRollbackHandler = automaticallyRegisterRollbackHandler;
    }
    
    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
