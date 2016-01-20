package cn.globalph.b2c.workflow;

public abstract class BaseExtensionActivity<T extends ProcessContext<? extends Object>> extends BaseActivity<T> {
    
    @Override
    public T execute(T context) throws Exception {
        return context;
    }

}
