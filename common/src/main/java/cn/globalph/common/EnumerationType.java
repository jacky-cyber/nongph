package cn.globalph.common;

/**
 * In order for the Broadleaf Administration to display enumerated values with meaningful labels,
 * enumerations should implement this interface.
 */
public interface EnumerationType {

    public String getType();
    public String getFriendlyType();
    
}
