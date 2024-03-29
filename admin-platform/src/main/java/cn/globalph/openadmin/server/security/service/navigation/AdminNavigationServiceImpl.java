package cn.globalph.openadmin.server.security.service.navigation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.openadmin.server.security.dao.AdminNavigationDao;
import cn.globalph.openadmin.server.security.domain.AdminMenu;
import cn.globalph.openadmin.server.security.domain.AdminModule;
import cn.globalph.openadmin.server.security.domain.AdminModuleDTO;
import cn.globalph.openadmin.server.security.domain.AdminModuleImpl;
import cn.globalph.openadmin.server.security.domain.AdminPermission;
import cn.globalph.openadmin.server.security.domain.AdminRole;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.server.security.domain.AdminUser;
import cn.globalph.openadmin.server.security.service.AdminSecurityService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

/**
 * This service is used to build the left hand navigation for the admin
 * @author elbertbautista
 */
@Service("blAdminNavigationService")
public class AdminNavigationServiceImpl implements AdminNavigationService {

    private static final Log LOG = LogFactory.getLog(AdminNavigationServiceImpl.class);
    private static final String PATTERN = "_";

    @Resource(name = "blAdminNavigationDao")
    protected AdminNavigationDao adminNavigationDao;

    @Resource(name="blAdditionalSectionAuthorizations")
    protected List<SectionAuthorization> additionalSectionAuthorizations = new ArrayList<SectionAuthorization>();

    @Override
    public AdminMenu buildMenu(AdminUser adminUser) {
        AdminMenu adminMenu = new AdminMenu();
        List<AdminModule> modules = adminNavigationDao.readAllAdminModules();
        populateAdminMenu(adminUser, adminMenu, modules);
        return adminMenu;
    }

    protected void populateAdminMenu(AdminUser adminUser, AdminMenu adminMenu, List<AdminModule> modules) {
        for (AdminModule module : modules) {
            List<AdminSection> authorizedSections = buildAuthorizedSectionsList(adminUser, module);
            if (authorizedSections != null && authorizedSections.size() > 0) {
                AdminModuleDTO adminModuleDto = ((AdminModuleImpl) module).getAdminModuleDTO();
                adminMenu.getAdminModules().add(adminModuleDto);
                adminModuleDto.setSections(authorizedSections);
            }
        }

        // Sort the authorized modules
        BeanComparator displayComparator = new BeanComparator("displayOrder");
        Collections.sort(adminMenu.getAdminModules(), displayComparator);
    }

    protected List<AdminSection> buildAuthorizedSectionsList(AdminUser adminUser, AdminModule module) {
        List<AdminSection> authorizedSections = new ArrayList<AdminSection>();
        for (AdminSection section : module.getSections()) {
            if (isUserAuthorizedToViewSection(adminUser, section)) {
                authorizedSections.add(section);
            }
        }

        Collections.sort(authorizedSections, SECTION_COMPARATOR);
        return authorizedSections;
    }

    @Override
    public boolean isUserAuthorizedToViewModule(AdminUser adminUser, AdminModule module) {
        List<AdminSection> moduleSections = module.getSections();
        if (moduleSections != null && !moduleSections.isEmpty()) {
            for (AdminSection section : moduleSections) {
                if (isUserAuthorizedToViewSection(adminUser, section)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public AdminSection findAdminSectionByURI(String uri) {
        return adminNavigationDao.readAdminSectionByURI(uri);
    }
    
    @Override
    public AdminSection findAdminSectionByClass(String className) {
        try {
            return findAdminSectionByClass(Class.forName(className));
        } catch (ClassNotFoundException e) {
            LOG.warn("Invalid classname received. This likely points to a configuration error.");
            return null;
        }
    }
    
    @Override
    public AdminSection findAdminSectionByClass(Class<?> clazz) {
        return adminNavigationDao.readAdminSectionByClass(clazz);
    }

    @Override
    public AdminSection findAdminSectionBySectionKey(String sectionKey) {
        return adminNavigationDao.readAdminSectionBySectionKey(sectionKey);
    }

    @Override
    public boolean isUserAuthorizedToViewSection(AdminUser adminUser, AdminSection section) {
        boolean response = false;
        List<AdminPermission> authorizedPermissions = section.getPermissions();
        checkAuth: {
            if (!CollectionUtils.isEmpty(adminUser.getAllRoles())) {
                for (AdminRole role : adminUser.getAllRoles()) {
                    for (AdminPermission permission : role.getAllPermissions()){
                        if (checkPermissions(authorizedPermissions, permission)) {
                            response = true;
                            break checkAuth;
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(adminUser.getAllPermissions())) {
                for (AdminPermission permission : adminUser.getAllPermissions()){
                    if (checkPermissions(authorizedPermissions, permission)) {
                        response = true;
                        break checkAuth;
                    }
                }
            }
            for (String defaultPermission : AdminSecurityService.DEFAULT_PERMISSIONS) {
                for (AdminPermission authorizedPermission : authorizedPermissions) {
                    if (authorizedPermission.getName().equals(defaultPermission)) {
                        response = true;
                        break checkAuth;
                    }
                }
            }
        }

        if (response) {
            for (SectionAuthorization sectionAuthorization : additionalSectionAuthorizations) {
                if (!sectionAuthorization.isUserAuthorizedToViewSection(adminUser, section)) {
                    response = false;
                    break;
                }
            }
        }

        return response;
    }
    
    @Override
    public List<AdminSection> findAllAdminSections() {
        List<AdminSection> sections = adminNavigationDao.readAllAdminSections();
        Collections.sort(sections, SECTION_COMPARATOR);
        return sections;
    }

    protected boolean checkPermissions(List<AdminPermission> authorizedPermissions, AdminPermission permission) {
        if (authorizedPermissions != null) {
            if (authorizedPermissions.contains(permission)){
                return true;
            }

            for (AdminPermission authorizedPermission : authorizedPermissions) {
                if (permission.getName().equals(parseForAllPermission(authorizedPermission.getName()))) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String parseForAllPermission(String currentPermission) {
        String[] pieces = currentPermission.split(PATTERN);
        StringBuilder builder = new StringBuilder(50);
        builder.append(pieces[0]);
        builder.append("_ALL_");
        for (int j = 2; j<pieces.length; j++) {
            builder.append(pieces[j]);
            if (j < pieces.length - 1) {
                builder.append('_');
            }
        }
        return builder.toString();
    }

    private static SectionComparator SECTION_COMPARATOR = new SectionComparator();

    private static class SectionComparator implements Comparator<AdminSection> {

        @Override
        public int compare(AdminSection section, AdminSection section2) {
            if (section.getDisplayOrder() != null) {
                if (section2.getDisplayOrder() != null) {
                    return section.getDisplayOrder().compareTo(section2.getDisplayOrder());
                }
                else
                    return -1;
            } else if (section2.getDisplayOrder() != null) {
                return 1;
            }

            return section.getId().compareTo(section2.getId());
        }

    }

    public List<SectionAuthorization> getAdditionalSectionAuthorizations() {
        return additionalSectionAuthorizations;
    }

    public void setAdditionalSectionAuthorizations(List<SectionAuthorization> additionalSectionAuthorizations) {
        this.additionalSectionAuthorizations = additionalSectionAuthorizations;
    }
}
