package cn.globalph.openadmin.server.security.service.navigation;

import org.apache.commons.lang.StringUtils;

import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.server.security.domain.AdminUser;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component("blPolymorphicEntityCheckSectionAuthorization")
public class PolymorphicEntitySectionAuthorizationImpl implements SectionAuthorization {

    @Resource(name="blDynamicEntityDao")
    protected DynamicEntityDao dynamicEntityDao;

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @PostConstruct
    public void init() {
        dynamicEntityDao.setStandardEntityManager(em);
    }

    @Override
    public boolean isUserAuthorizedToViewSection(AdminUser adminUser, AdminSection section) {
        if (StringUtils.isBlank(section.getCeilingEntity())) {
            return true;
        }

        try {
            //Only display this section if there are 1 or more entities relative to the ceiling 
            //for this section that are qualified to be created by the admin
            return dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(
                    Class.forName(section.getCeilingEntity()), false).length > 0;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
