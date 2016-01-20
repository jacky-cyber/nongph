package cn.globalph.openadmin.web.form.component;


/**
 * View class for the icon that can potentially appear on list grid rows.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class ListGridRecordIcon {
    
    protected String cssClass;
    protected String message;
    
    public ListGridRecordIcon withCssClass(String cssClass) {
        setCssClass(cssClass);
        return this;
    }

    public ListGridRecordIcon withMessage(String message) {
        setMessage(message);
        return this;
    }

    public String getCssClass() {
        return cssClass;
    }
    
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
