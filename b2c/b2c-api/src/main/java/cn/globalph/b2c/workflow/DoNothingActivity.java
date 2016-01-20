package cn.globalph.b2c.workflow;

import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

public class DoNothingActivity<T extends ProcessContext<?>> extends BaseActivity<ProcessContext<T>> {
    @Override
    public ProcessContext<T> execute(ProcessContext<T> context) throws Exception {
        return context;
    }
}
