package cn.globalph.openadmin.server.security.service.navigation;

import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.server.security.domain.AdminUser;

/**
 * @author Jeff Fischer
 */
public interface SectionAuthorization {
    boolean isUserAuthorizedToViewSection(AdminUser adminUser, AdminSection section);
}
