package cn.globalph.openadmin.web.compatibility;


import cn.globalph.openadmin.server.service.JSCompatibilityHelper;

import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.savedrequest.Enumerator;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jeff Fischer
 */
public class JSCompatibilityRequestWrapper extends FirewalledRequest {

    public JSCompatibilityRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public void reset() {
        //do nothing
    }

    @Override
    public String getContextPath() {
        return JSCompatibilityHelper.unencode(super.getContextPath());
    }

    @Override
    public String getPathTranslated() {
        return JSCompatibilityHelper.unencode(super.getPathTranslated());
    }

    @Override
    public String getQueryString() {
        return JSCompatibilityHelper.unencode(super.getQueryString());
    }

    @Override
    public String getRequestURI() {
        return JSCompatibilityHelper.unencode(super.getRequestURI());
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(JSCompatibilityHelper.unencode(super.getRequestURL().toString()));
    }

    @Override
    public String getServletPath() {
        return JSCompatibilityHelper.unencode(super.getServletPath());
    }

    @Override
    public String getParameter(String name) {
        return JSCompatibilityHelper.unencode(super.getParameter(name));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Enumeration getParameterNames() {
        List<String> names = new ArrayList<String>();
        Enumeration enumeration = super.getParameterNames();
        while (enumeration.hasMoreElements()) {
            names.add(JSCompatibilityHelper.unencode((String) enumeration.nextElement()));
        }

        return new Enumerator<String>(names);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map getParameterMap() {
        Map params = super.getParameterMap();
        Map temp = new LinkedHashMap();
        for (Object key : params.keySet()) {
            Object value = params.get(key);
            if (value instanceof String) {
                temp.put(JSCompatibilityHelper.unencode((String) key), JSCompatibilityHelper.unencode((String) value));
            } else {
                String[] vals = (String[]) value;
                String[] tempVals = new String[vals.length];
                int j = 0;
                for (String val : vals) {
                    tempVals[j] = JSCompatibilityHelper.unencode(val);
                    j++;
                }
                temp.put(JSCompatibilityHelper.unencode((String) key), tempVals);
            }
        }
        return temp;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Map<String, String> getParameterNameConversionMap() {
        if (getAttribute("requestParameterConversionMap") == null) {
            Map<String, String> map = new HashMap<String, String>();
            Enumeration enumeration = super.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String temp = (String) enumeration.nextElement();
                map.put(JSCompatibilityHelper.unencode(temp), temp);
            }
            setAttribute("requestParameterConversionMap", map);
        }

        return (Map<String, String>) getAttribute("requestParameterConversionMap");
    }
    
    @Override
    public String[] getParameterValues(String name) {
        String convertedParameterName = getParameterNameConversionMap().get(name);
        return convertedParameterName == null ? super.getParameterValues(name) : super.getParameterValues(convertedParameterName);
    }

}
