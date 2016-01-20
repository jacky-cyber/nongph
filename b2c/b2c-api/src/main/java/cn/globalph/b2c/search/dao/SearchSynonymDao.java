package cn.globalph.b2c.search.dao;

import cn.globalph.b2c.search.domain.SearchSynonym;

import java.util.List;

public interface SearchSynonymDao {
    public List<SearchSynonym> getAllSynonyms();
    public void createSynonym(SearchSynonym synonym);
    public void updateSynonym(SearchSynonym synonym);
    public void deleteSynonym(SearchSynonym synonym);
}
