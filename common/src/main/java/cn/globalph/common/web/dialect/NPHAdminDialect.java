package cn.globalph.common.web.dialect;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

public class NPHAdminDialect extends AbstractDialect {

    private Set<IProcessor> processors = new HashSet<IProcessor>();

    @Override
    public String getPrefix() {
        return "nph_admin";
    }

    @Override
    public boolean isLenient() {
        return true;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(Set<IProcessor> processors) {
        this.processors = processors;
    }

}
