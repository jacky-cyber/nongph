package cn.globalph.b2c.search.domain;

public interface SearchSynonym {
    public String getTerm();
    public void setTerm(String term);
    public String[] getSynonyms();
    public void setSynonyms(String[] synonyms);
}
