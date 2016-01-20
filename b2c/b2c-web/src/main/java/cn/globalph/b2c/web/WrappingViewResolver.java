package cn.globalph.b2c.web;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @felix.wu
 *
 */
public class WrappingViewResolver implements ViewResolver {
    
    private View view;
    
    public WrappingViewResolver(View view) {
        this.view = view;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.ViewResolver#resolveViewName(java.lang.String, java.util.Locale)
     */
    public View resolveViewName(String arg0, Locale arg1) throws Exception {
        return view;
    }

}
