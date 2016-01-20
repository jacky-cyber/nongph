package cn.globalph.b2c.search.domain;

import java.io.Serializable;

/**
 * @author Jeff Fischer
 */
public interface RequiredFacet extends Serializable {

    Long getId();

    void setId(Long id);

    SearchFacet getRequiredFacet();

    void setRequiredFacet(SearchFacet requiredFacet);

    SearchFacet getSearchFacet();

    void setSearchFacet(SearchFacet searchFacet);

}
