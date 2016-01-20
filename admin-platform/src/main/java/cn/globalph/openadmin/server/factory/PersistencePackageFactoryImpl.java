package cn.globalph.openadmin.server.factory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.exception.ExceptionHelper;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.common.util.dao.DynamicDaoHelper;
import cn.globalph.common.util.dao.DynamicDaoHelperImpl;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistenceOperationType;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.dto.SectionCrumb;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.server.security.service.navigation.AdminNavigationService;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author felix.wu
 */
@Service("blPersistencePackageFactory")
public class PersistencePackageFactoryImpl implements PersistencePackageFactory {

    @Resource(name = "blAdminNavigationService")
    protected AdminNavigationService adminNavigationService;

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    protected DynamicDaoHelper dynamicDaoHelper = new DynamicDaoHelperImpl();

    @Override
    public PersistencePackage create(PersistencePackageRequest request) {
        PersistenceAssociation persistencePerspective = new PersistenceAssociation();

        persistencePerspective.setAdditionalForeignKeys(request.getAdditionalForeignKeys());
        persistencePerspective.setAdditionalNonPersistentProperties(new String[] {});
        
        if (request.getForeignKey() != null) {
            persistencePerspective.addPersistenceAssociationItem(PersistenceAssociationItemType.FOREIGNKEY, 
                    request.getForeignKey());
        }

        switch ( request.getType() ) {
            case STANDARD:
                persistencePerspective.setPersistenceOperationType(getDefaultOperationTypes());
                break;

            case ADORNED:
                if (request.getAdornedList() == null) {
                    throw new IllegalArgumentException("ADORNED type requires the adornedList to be set");
                }

                persistencePerspective.setPersistenceOperationType(getOperationTypes(OperationScope.ADORNEDTARGETLIST));
                persistencePerspective.addPersistenceAssociationItem(PersistenceAssociationItemType.ADORNEDTARGETLIST,
                        request.getAdornedList());
                break;

            case MAP:
                if (request.getMapStructure() == null) {
                    throw new IllegalArgumentException("MAP type requires the mapStructure to be set");
                }

                persistencePerspective.setPersistenceOperationType(getOperationTypes(OperationScope.MAP));
                persistencePerspective.addPersistenceAssociationItem(PersistenceAssociationItemType.MAPSTRUCTURE,
                        request.getMapStructure());
                break;
        }

        if (request.getOperationTypesOverride() != null) {
            persistencePerspective.setPersistenceOperationType( request.getOperationTypesOverride() );
        }

        PersistencePackage pp = new PersistencePackage();
        pp.setRootEntityClassname(request.getRootEntityClassname());
        if (!StringUtils.isEmpty(request.getSecurityRootEntityClassname())) {
            pp.setSecurityRootEntityClassname(request.getSecurityRootEntityClassname());
        }
        if (!ArrayUtils.isEmpty(request.getSectionCrumbs())) {
            SectionCrumb[] converted = new SectionCrumb[request.getSectionCrumbs().length];
            int index = 0;
            for (SectionCrumb crumb : request.getSectionCrumbs()) {
                SectionCrumb temp = new SectionCrumb();
                try {
                    temp.setSectionIdentifier(getClassNameForSection(crumb.getSectionIdentifier()));
                } catch (Exception e) {
                    temp.setSectionIdentifier(request.getRootEntityClassname());
                }
                temp.setSectionId(crumb.getSectionId());
                converted[index] = temp;
                index++;
            }
            pp.setSectionCrumbs(converted);
        }
        pp.setSectionEntityField(request.getSectionEntityField());
        pp.setFetchTypeClassname(null);
        pp.setPersistenceAssociation(persistencePerspective);
        pp.setCustomCriteria(request.getCustomCriteria());
        pp.setCsrfToken(null);
        pp.setRequestingEntityName(request.getRequestingEntityName());
        pp.setValidateUnsubmittedProperties(request.isValidateUnsubmittedProperties());

        if (request.getEntity() != null) {
            pp.setEntity(request.getEntity());
        }

        for (Map.Entry<String, PersistencePackageRequest> subRequest : request.getSubRequests().entrySet()) {
            pp.getSubPackages().put(subRequest.getKey(), create(subRequest.getValue()));
        }

        return pp;
    }

    protected PersistenceOperationType getDefaultOperationTypes() {
        PersistenceOperationType operationTypes = new PersistenceOperationType();
        operationTypes.setFetchType(OperationScope.BASIC);
        operationTypes.setRemoveType(OperationScope.BASIC);
        operationTypes.setAddType(OperationScope.BASIC);
        operationTypes.setUpdateType(OperationScope.BASIC);
        operationTypes.setInspectType(OperationScope.BASIC);
        return operationTypes;
    }
    
    protected PersistenceOperationType getOperationTypes(OperationScope nonInspectOperationType) {
        PersistenceOperationType operationTypes = new PersistenceOperationType();
        operationTypes.setFetchType(nonInspectOperationType);
        operationTypes.setRemoveType(nonInspectOperationType);
        operationTypes.setAddType(nonInspectOperationType);
        operationTypes.setUpdateType(nonInspectOperationType);
        operationTypes.setInspectType(OperationScope.BASIC);
        return operationTypes;
    }

    protected String getClassNameForSection(String sectionKey) {
        try {
            AdminSection section = adminNavigationService.findAdminSectionByURI("/" + sectionKey);
            String className = (section == null) ? sectionKey : section.getCeilingEntity();
            Class<?>[] entities = dynamicDaoHelper.getAllPolymorphicEntitiesFromCeiling(Class.forName(className), em.unwrap(Session.class).getSessionFactory(), true, true);
            return entities[entities.length - 1].getName();
        } catch (ClassNotFoundException e) {
            throw ExceptionHelper.refineException(RuntimeException.class, RuntimeException.class, e);
        }
    }
}
