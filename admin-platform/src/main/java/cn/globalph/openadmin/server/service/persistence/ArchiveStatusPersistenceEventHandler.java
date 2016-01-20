package cn.globalph.openadmin.server.service.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.persistence.Status;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.service.persistence.module.EmptyFilterValues;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPath;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FilterMapping;
import cn.globalph.openadmin.server.service.persistence.module.criteria.Restriction;
import cn.globalph.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;

import org.springframework.stereotype.Component;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Adds {@link FilterMapping} to the {@link CriteriaTransferObject}'s {@link CriteriaTransferObject#getAdditionalFilterMappings()}
 * in order to exclude by default any entities that are archived.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("blArchiveStatusPersistenceEventHandler")
public class ArchiveStatusPersistenceEventHandler extends PersistenceManagerEventHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(ArchiveStatusPersistenceEventHandler.class);
    
    @Override
    public PersistenceOperationEventHandlerResponse preFetch(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        try {
            Class<?>[] entityClasses = persistenceManager.getDynamicEntityDao()
                    .getAllPolymorphicEntitiesFromCeiling(Class.forName(persistencePackage.getRootEntityClassname()));
            boolean isArchivable = false;
            for (Class<?> entity : entityClasses) {
                if (Status.class.isAssignableFrom(entity)) {
                    isArchivable = true;
                    break;
                }
            }
            if (isArchivable && !persistencePackage.getPersistenceAssociation().getShowArchivedFields()) {
                FilterMapping filterMapping = new FilterMapping()
                    .withFieldPath(new FieldPath().withTargetProperty("archiveStatus.archived"))
                    .withDirectFilterValues(new EmptyFilterValues())
                    .withRestriction(new Restriction()
                            .withPredicateProvider(new PredicateProvider<Character, Character>() {
                                @Override
                                public Predicate buildPredicate(CriteriaBuilder builder,
                                                                FieldPathBuilder fieldPathBuilder,
                                                                From root, String ceilingEntity,
                                                                String fullPropertyName, Path<Character> explicitPath,
                                                                List<Character> directValues) {
                                    return builder.or(builder.equal(explicitPath, 'N'), builder.isNull(explicitPath));
                                }
                            })
                    );
                cto.getAdditionalFilterMappings().add(filterMapping);
            }
            return new PersistenceOperationEventHandlerResponse().
                    withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED);
        } catch (ClassNotFoundException e) {
            LOG.error("Could not find the class " + persistencePackage.getRootEntityClassname() + " to "
                    + "compute polymorphic entity types for. Assuming that the entity is not archivable");
            return new PersistenceOperationEventHandlerResponse().
                    withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
        }
    }
    
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
    
}
