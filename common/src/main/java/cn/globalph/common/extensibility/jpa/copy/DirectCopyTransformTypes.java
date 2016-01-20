package cn.globalph.common.extensibility.jpa.copy;

/**
 * Constants for tagging an entity class to receive byte code transformation upon
 * being loaded by the classloader
 *
 * @author Jeff Fischer
 */
public class DirectCopyTransformTypes {

    //Class should receive sandbox related fields
    public static final String SANDBOX = "sandbox";
    //Class should receive fields that describe the original entity from which a sandbox clone was derived
    public static final String SANDBOX_PRECLONE_INFORMATION = "sandboxPreCloneInformation";
    //Class should receive fields describing the site to which the entity belongs
    public static final String MULTITENANT_SITE = "multiTenantSite";
    //Class should receive fields describing the catalog to which the entity belongs
    public static final String MULTITENANT_CATALOG = "multiTenantCatalog";
    //Class should receive the Discriminatable marker interface alone
    public static final String MULTITENANT_SITEMARKER = "multiTenantSiteMarker";
    //Class should receive fields describing whether or not the entity was created during a site preview
    public static final String PREVIEW = "preview";
    //Class should receive fields describing site context information for the permission
    public static final String MULTITENANT_ADMINPERMISSION = "multiTenantAdminPermission";
    //Class should receive fields describing site context information for the role
    public static final String MULTITENANT_ADMINROLE = "multiTenantAdminRole";
    //Class should receive fields describing site context information for the user
    public static final String MULTITENANT_ADMINUSER = "multiTenantAdminUser";

}
