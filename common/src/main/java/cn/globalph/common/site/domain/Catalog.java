package cn.globalph.common.site.domain;

import java.io.Serializable;
import java.util.List;

public interface Catalog extends Serializable {

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    List<Site> getSites();

    void setSites(List<Site> sites);

}
