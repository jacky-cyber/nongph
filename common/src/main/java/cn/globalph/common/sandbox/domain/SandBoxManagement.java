package cn.globalph.common.sandbox.domain;

import java.io.Serializable;

/**
 * @author Jeff Fischer
 */
public interface SandBoxManagement extends Serializable {

    SandBox getSandBox();

    void setSandBox(SandBox sandBox);
}
