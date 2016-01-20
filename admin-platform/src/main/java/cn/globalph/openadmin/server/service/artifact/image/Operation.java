package cn.globalph.openadmin.server.service.artifact.image;

import cn.globalph.openadmin.server.service.artifact.image.effects.chain.UnmarshalledParameter;

public class Operation {

    protected String name;
    protected Double factor;
    protected UnmarshalledParameter[] parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public UnmarshalledParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(UnmarshalledParameter[] parameters) {
        this.parameters = parameters;
    }
}
