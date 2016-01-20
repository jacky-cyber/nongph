package cn.globalph.b2c.workflow;



/**
 * A Do-nothing activity used to test proper merge ordering in workflows
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public class PassThroughActivity extends BaseActivity<ProcessContext<? extends Object>> {

    @Override
    public ProcessContext<? extends Object> execute(ProcessContext<? extends Object> context) throws Exception {
        return context;
    }

}
