package cn.globalph.openadmin.web.form.entity;

/**
 * @author Andre Azzolini (apazzolini)
 */
public class CodeField extends Field {

    protected String fileType;
    protected String fetchUrlSuffix;

    /* ************************** */
    /* STANDARD GETTERS / SETTERS */
    /* ************************** */
    
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public String getFetchUrlSuffix() {
        return fetchUrlSuffix;
    }
    
    public void setFetchUrlSuffix(String fetchUrlSuffix) {
        this.fetchUrlSuffix = fetchUrlSuffix;
    }

}
