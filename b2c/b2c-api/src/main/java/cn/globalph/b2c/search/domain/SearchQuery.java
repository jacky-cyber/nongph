package cn.globalph.b2c.search.domain;

public class SearchQuery {

    private String queryString;
    
    public SearchQuery() { }
    public SearchQuery(String queryString) {
        setQueryString(queryString);
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
