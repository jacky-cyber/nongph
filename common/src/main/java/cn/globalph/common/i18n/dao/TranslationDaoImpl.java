package cn.globalph.common.i18n.dao;

import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.i18n.domain.TranslatedEntity;
import cn.globalph.common.i18n.domain.Translation;
import cn.globalph.common.i18n.domain.TranslationImpl;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.util.dao.DynamicDaoHelper;
import cn.globalph.common.util.dao.DynamicDaoHelperImpl;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository("blTranslationDao")
public class TranslationDaoImpl implements TranslationDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name = "blTranslationDaoExtensionManager")
    protected TranslationDaoExtensionManager extensionManager;

    protected DynamicDaoHelper dynamicDaoHelper = new DynamicDaoHelperImpl();
    
    @Override
    public Translation save(Translation translation) {
        return em.merge(translation);
    }
    
    @Override
    public Translation create() {
        return (Translation) entityConfiguration.createEntityInstance(Translation.class.getName());
    }
    
    @Override
    public void delete(Translation translation) {
        em.remove(translation);
    }
    
    @Override
    public Map<String, Object> getIdPropertyMetadata(TranslatedEntity entity) {
        Class<?> implClass = entityConfiguration.lookupEntityClass(entity.getType());
        return dynamicDaoHelper.getIdMetadata(implClass, (HibernateEntityManager) em);
    }
    
    @Override
    public Translation readTranslationById(Long translationId) {
        return em.find(TranslationImpl.class, translationId);
    }
    
    @Override
    public List<Translation> readTranslations(TranslatedEntity entity, String entityId, String fieldName) {
        entityId = getUpdatedEntityId(entity, entityId);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Translation> criteria = builder.createQuery(Translation.class);
        Root<TranslationImpl> translation = criteria.from(TranslationImpl.class);

        criteria.select(translation);
        criteria.where(builder.equal(translation.get("entityType"), entity.getFriendlyType()),
            builder.equal(translation.get("entityId"), entityId),
            builder.equal(translation.get("fieldName"), fieldName)
        );

        TypedQuery<Translation> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);

        return query.getResultList();
    }

    @Override
    public Translation readTranslation(TranslatedEntity entity, String entityId, String fieldName, String localeCode) {
        entityId = getUpdatedEntityId(entity, entityId);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Translation> criteria = builder.createQuery(Translation.class);
        Root<TranslationImpl> translation = criteria.from(TranslationImpl.class);

        criteria.select(translation);
        criteria.where(builder.equal(translation.get("entityType"), entity.getFriendlyType()),
            builder.equal(translation.get("entityId"), entityId),
            builder.equal(translation.get("fieldName"), fieldName),
            builder.equal(translation.get("localeCode"), localeCode)
        );
        TypedQuery<Translation> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<Translation> translations = query.getResultList();
        if (translations.size() > 1) {
            throw new IllegalStateException("Found multiple translations for: " + entity.getFriendlyType() + "|" + entityId + "|" + fieldName + "|" + localeCode);
        }
        if (!translations.isEmpty()) {
            return translations.get(0);
        }
        return null;
    }
    
    protected String getUpdatedEntityId(TranslatedEntity entity, String entityId) {
        Class<?> clazz = entityConfiguration.lookupEntityClass(entity.getType());

        ExtensionResultHolder erh = new ExtensionResultHolder();
        try {
            Long id = Long.parseLong(entityId);
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().overrideRequestedId(erh, em, clazz, id);
            if (result.equals(ExtensionResultStatusType.HANDLED)) {
                return String.valueOf((Long) erh.getResult());
            }
        } catch (NumberFormatException e) {
            // The ID was not a Long, which means we can't override it. We'll return the default value below.
        }

        return entityId;
    }

    public DynamicDaoHelper getDynamicDaoHelper() {
        return dynamicDaoHelper;
    }

    public void setDynamicDaoHelper(DynamicDaoHelper dynamicDaoHelper) {
        this.dynamicDaoHelper = dynamicDaoHelper;
    }

}
