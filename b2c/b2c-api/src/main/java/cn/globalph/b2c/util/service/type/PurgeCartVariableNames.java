package cn.globalph.b2c.util.service.type;

/**
 * @author Jeff Fischer
 */
public enum PurgeCartVariableNames {
    STATUS //looking for order with this status(es)
    ,NAME //looking for order with this name(s)
    ,SECONDS_OLD //looking for orders older than this
    ,IS_PREVIEW //looking for orders that are marked as preview (generally only meaningful in an enterprise context)
    ,SITE //looking for orders that belong to a particular site (generally only meaningful in an multi-tenant context)
}
