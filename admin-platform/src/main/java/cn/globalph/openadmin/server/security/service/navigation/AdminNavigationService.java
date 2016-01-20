package cn.globalph.openadmin.server.security.service.navigation;

import cn.globalph.openadmin.server.security.domain.AdminMenu;
import cn.globalph.openadmin.server.security.domain.AdminModule;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.server.security.domain.AdminUser;

import java.util.List;

public interface AdminNavigationService {

    public AdminMenu buildMenu(AdminUser adminUser);

    public boolean isUserAuthorizedToViewSection(AdminUser adminUser, AdminSection section);

    public boolean isUserAuthorizedToViewModule(AdminUser adminUser, AdminModule module);

    public AdminSection findAdminSectionByURI(String uri);

    public AdminSection findAdminSectionBySectionKey(String sectionKey);

    public AdminSection findAdminSectionByClass(String className);
    public AdminSection findAdminSectionByClass(Class<?> clazz);

    /**
     * Gets all of the {@link AdminSection}s in the system, sorted by the {@link AdminSection#getDisplayOrder()}
     * @return the list of all {@link AdminSection}s sorted by {@link AdminSection#getDisplayOrder()}
     */
    public List<AdminSection> findAllAdminSections();

}
