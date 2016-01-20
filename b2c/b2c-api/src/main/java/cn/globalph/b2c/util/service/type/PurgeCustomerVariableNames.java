package cn.globalph.b2c.util.service.type;

/**
 * @author Jeff Fischer
 */
public enum PurgeCustomerVariableNames {
    IS_REGISTERED //looking for registered or anonymous customers
    ,IS_DEACTIVATED //looking for active or inactive customers
    ,SECONDS_OLD //looking for customers older than this
    ,IS_PREVIEW //looking for customers that are marked as preview (generally only meaningful in an enterprise context)
    ,SITE //looking for customers that belong to a particular site (generally only meaningful in an multi-tenant context)
}
