package cn.globalph.openadmin.server.security.dao;


import cn.globalph.openadmin.server.security.domain.AdminModule;
import cn.globalph.openadmin.server.security.domain.AdminSection;

import java.util.List;

/**
 *
 * @author elbertbautista
 *
 */
public interface AdminNavigationDao {

    public List<AdminModule> readAllAdminModules();

    public List<AdminSection> readAllAdminSections();
    
    public AdminSection readAdminSectionByClass(Class<?> clazz);

    public AdminSection readAdminSectionByURI(String uri);

    public AdminSection readAdminSectionBySectionKey(String sectionKey);


}
