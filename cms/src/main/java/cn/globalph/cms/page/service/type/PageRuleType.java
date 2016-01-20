package cn.globalph.cms.page.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of offer rule types.<BR>
 * REQUEST - indicates a rule based on the incoming http request.<BR>
 * TIME - indicates a rule based on {@link cn.globalph.common.TimeDTO time}<br>
 * PRODUCT - indicates a rule based on {@link cn.globalph.b2c.product.domain.Product product}
 * CUSTOMER - indicates a rule based on {@link cn.globalph.passport.domain}
 */
public class PageRuleType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, PageRuleType> TYPES = new LinkedHashMap<String, PageRuleType>();

    public static final PageRuleType REQUEST = new PageRuleType("REQUEST", "Request");
    public static final PageRuleType TIME = new PageRuleType("TIME", "Time");
    public static final PageRuleType PRODUCT = new PageRuleType("PRODUCT", "Product");
    public static final PageRuleType CUSTOMER = new PageRuleType("CUSTOMER", "Customer");

    /**
     * Allows translation from the passed in String to a <code>PageRuleType</code>
     * @param type
     * @return The matching rule type
     */
    public static PageRuleType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public PageRuleType() {
        //do nothing
    }

    /**
     * Initialize the type and friendlyType
     * @param <code>type</code>
     * @param <code>friendlyType</code>
     */
    public PageRuleType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    /**
     * Sets the type
     * @param type
     */
    public void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    /**
     * Gets the type
     * @return
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Gets the name of the type
     * @return
     */
    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
        return true;
    }
        if (obj == null) {
        return false;
    }
        if (getClass() != obj.getClass()) {
        return false;
    }
        PageRuleType other = (PageRuleType) obj;
        if (type == null) {
            if (other.type != null) {
        return false;
        }
        } else if (!type.equals(other.type)) {
        return false;
    }
        return true;
    }

}
