package cn.globalph.common.web;

import org.springframework.web.servlet.handler.AbstractHandlerMapping;

/**
 * Adds some convenience methods to the Spring AbstractHandlerMapping for
 * BLC specific HandlerMappings.
 * 
 * Always returns null from defaultHandlerMapping 
 */
public abstract class BLCAbstractHandlerMapping extends AbstractHandlerMapping {
    protected String controllerName;
    protected String ajaxControllerName;

    @Override
    /**
     * This handler mapping does not provide a default handler.   This method
     * has been coded to always return null.
     */
    public Object getDefaultHandler() {
        return null;        
    }
    
    /**
     * Returns the controllerName if set or "blPageController" by default.
     * @return
     */
    public String getControllerName() {
        return controllerName;
    }

    /**
     * Sets the name of the bean to use as the Handler.  Typically the name of
     * a controller bean.
     * 
     * @param controllerName
     */
    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

	public String getAjaxControllerName() {
		return ajaxControllerName;
	}

	public void setAjaxControllerName(String ajaxControllerName) {
		this.ajaxControllerName = ajaxControllerName;
	}
    
    
}
