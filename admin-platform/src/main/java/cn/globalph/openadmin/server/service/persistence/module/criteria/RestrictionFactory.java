package cn.globalph.openadmin.server.service.persistence.module.criteria;

/**
 * @author Jeff Fischer
 */
public interface RestrictionFactory {

    Restriction getRestriction(String type, String propertyId);

}
