package cn.globalph.b2c.workflow;

public interface ProcessContextFactory<U, T> {

    public ProcessContext<U> createContext(T preSeedData) throws WorkflowException;

}
