package cn.globalph.openadmin.dto;

import org.apache.commons.lang.ArrayUtils;

import cn.globalph.common.presentation.client.PersistenceAssociationItemType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 持久操作
 * @author felix.wu
 */
public class PersistenceAssociation {

	protected String[] additionalNonPersistentProperties = new String[]{};
    protected ForeignKey[] additionalForeignKeys = new ForeignKey[]{};
    /*关联项*/
    protected Map<PersistenceAssociationItemType, PersistenceAssociationItem> persistenceAssociationItems = new HashMap<PersistenceAssociationItemType, PersistenceAssociationItem>();
    /*持久操作类型*/
    protected PersistenceOperationType persistenceOperationType = new PersistenceOperationType();
    protected Boolean populateToOneFields = false;
    protected String[] excludeFields = new String[]{};
    protected String[] includeFields = new String[]{};
    protected String configurationKey;
    protected Boolean showArchivedFields = false;
    protected Boolean useServerSideInspectionCache = true;
    
    public PersistenceAssociation() {
    }
    
    public PersistenceAssociation(PersistenceOperationType operationTypes, String[] additionalNonPersistentProperties, ForeignKey[] additionalForeignKeys) {
        setAdditionalNonPersistentProperties(additionalNonPersistentProperties);
        setAdditionalForeignKeys(additionalForeignKeys);
        this.persistenceOperationType = operationTypes;
    }

    public String[] getAdditionalNonPersistentProperties() {
        return additionalNonPersistentProperties;
    }

    public void setAdditionalNonPersistentProperties(String[] additionalNonPersistentProperties) {
        this.additionalNonPersistentProperties = additionalNonPersistentProperties;
        if (!ArrayUtils.isEmpty(this.additionalNonPersistentProperties)) {
            Arrays.sort(this.additionalNonPersistentProperties);
        }
    }

    public ForeignKey[] getAdditionalForeignKeys() {
        return additionalForeignKeys;
    }

    public void setAdditionalForeignKeys(ForeignKey[] additionalForeignKeys) {
        this.additionalForeignKeys = additionalForeignKeys;
        if (!ArrayUtils.isEmpty(this.additionalForeignKeys)) {
            Arrays.sort(this.additionalForeignKeys, new Comparator<ForeignKey>() {
                public int compare(ForeignKey o1, ForeignKey o2) {
                    return o1.getManyToField().compareTo(o2.getManyToField());
                }
            });
        }
    }

    public PersistenceOperationType getPersistenceOperationType() {
        return persistenceOperationType;
    }

    public void setPersistenceOperationType(PersistenceOperationType operationTypes) {
        this.persistenceOperationType = operationTypes;
    }
    
    public void addPersistenceAssociationItem(PersistenceAssociationItemType type, PersistenceAssociationItem item) {
        persistenceAssociationItems.put(type, item);
    }

    public Map<PersistenceAssociationItemType, PersistenceAssociationItem> getPersistenceAssociationItems() {
        return persistenceAssociationItems;
    }

    public void setPersistenceAssociationItems(Map<PersistenceAssociationItemType, PersistenceAssociationItem> persistencePerspectiveItems) {
        this.persistenceAssociationItems = persistencePerspectiveItems;
    }

    /**
     * Retrieves whether or not ManyToOne and OneToOne field boundaries
     * will be traversed when retrieving and populating entity fields.
     * Implementation should use the @AdminPresentationClass annotation
     * instead.
     *
     * @return Whether or not ManyToOne and OneToOne field boundaries will be crossed.
     */
    @Deprecated
    public Boolean getPopulateToOneFields() {
        return populateToOneFields;
    }

    /**
     * Sets whether or not ManyToOne and OneToOne field boundaries
     * will be traversed when retrieving and populating entity fields.
     * Implementation should use the @AdminPresentationClass annotation
     * instead.
     *
     * @return Whether or not ManyToOne and OneToOne field boundaries will be crossed.
     */
    @Deprecated
    public void setPopulateToOneFields(Boolean populateToOneFields) {
        this.populateToOneFields = populateToOneFields;
    }

    /**
     * Retrieve the list of fields to exclude from the admin presentation.
     * Implementations should use the excluded property of the AdminPresentation
     * annotation instead, or use an AdminPresentationOverride if re-enabling a
     * Broadleaf field is desired. If multiple datasources point to the same
     * entity, but different exclusion behavior is required, a custom persistence
     * handler may be employed with different inspect method implementations to
     * account for the variations.
     *
     * @return list of fields to exclude from the admin
     */
    @Deprecated
    public String[] getExcludeFields() {
        return excludeFields;
    }

    /**
     * Set the list of fields to exclude from the admin presentation.
     * Implementations should use the excluded property of the AdminPresentation
     * annotation instead, or use an AdminPresentationOverride if re-enabling a
     * Broadleaf field is desired. If multiple datasources point to the same
     * entity, but different exclusion behavior is required, a custom persistence
     * handler may be employed with different inspect method implementations to
     * account for the variations.
     *
     * @param excludeManyToOneFields
     */
    @Deprecated
    public void setExcludeFields(String[] excludeManyToOneFields) {
        this.excludeFields = excludeManyToOneFields;
        if (!ArrayUtils.isEmpty(this.excludeFields)) {
            Arrays.sort(this.excludeFields);
        }
    }

    /**
     * Get the list of fields to include in the admin presentation.
     * Implementations should use excludeFields instead.
     *
     * @return list of fields to include in the admin
     */
    @Deprecated
    public String[] getIncludeFields() {
        return includeFields;
    }

    /**
     * Set the list of fields to include in the admin presentation.
     * Implementations should use excludeFields instead.
     *
     * @param includeManyToOneFields
     */
    @Deprecated
    public void setIncludeFields(String[] includeManyToOneFields) {
        this.includeFields = includeManyToOneFields;
        if (!ArrayUtils.isEmpty(this.includeFields)) {
            Arrays.sort(this.includeFields);
        }
    }

    public String getConfigurationKey() {
        return configurationKey;
    }

    public void setConfigurationKey(String configurationKey) {
        this.configurationKey = configurationKey;
    }

    public Boolean getShowArchivedFields() {
        return showArchivedFields;
    }

    public void setShowArchivedFields(Boolean showArchivedFields) {
        this.showArchivedFields = showArchivedFields;
    }

    public Boolean getUseServerSideInspectionCache() {
        return useServerSideInspectionCache;
    }

    public void setUseServerSideInspectionCache(Boolean useServerSideInspectionCache) {
        this.useServerSideInspectionCache = useServerSideInspectionCache;
    }

    public PersistenceAssociation clonePersistencePerspective() {
        PersistenceAssociation persistencePerspective = new PersistenceAssociation();
        persistencePerspective.persistenceOperationType = persistenceOperationType.cloneOperationTypes();

        if (additionalNonPersistentProperties != null) {
            persistencePerspective.additionalNonPersistentProperties = new String[additionalNonPersistentProperties.length];
            System.arraycopy(additionalNonPersistentProperties, 0, persistencePerspective.additionalNonPersistentProperties, 0, additionalNonPersistentProperties.length);
        }

        if (additionalForeignKeys != null) {
            persistencePerspective.additionalForeignKeys = new ForeignKey[additionalForeignKeys.length];
            for (int j=0; j<additionalForeignKeys.length;j++){
                persistencePerspective.additionalForeignKeys[j] = additionalForeignKeys[j].cloneForeignKey();
            }
        }

        if (this.persistenceAssociationItems != null) {
            Map<PersistenceAssociationItemType, PersistenceAssociationItem> persistencePerspectiveItems = new HashMap<PersistenceAssociationItemType, PersistenceAssociationItem>(this.persistenceAssociationItems.size());
            for (Map.Entry<PersistenceAssociationItemType, PersistenceAssociationItem> entry : this.persistenceAssociationItems.entrySet()) {
                persistencePerspectiveItems.put(entry.getKey(), entry.getValue().clonePersistenceAssociationItem());
            }
            persistencePerspective.persistenceAssociationItems = persistencePerspectiveItems;
        }

        persistencePerspective.populateToOneFields = populateToOneFields;
        persistencePerspective.configurationKey = configurationKey;
        persistencePerspective.showArchivedFields = showArchivedFields;
        persistencePerspective.useServerSideInspectionCache = useServerSideInspectionCache;

        if (excludeFields != null) {
            persistencePerspective.excludeFields = new String[excludeFields.length];
            System.arraycopy(excludeFields, 0, persistencePerspective.excludeFields, 0, excludeFields.length);
        }

        if (includeFields != null) {
            persistencePerspective.includeFields = new String[includeFields.length];
            System.arraycopy(includeFields, 0, persistencePerspective.includeFields, 0, includeFields.length);
        }

        return persistencePerspective;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistencePerspective{");
        sb.append("persistencePerspectiveItems=").append(persistenceAssociationItems);
        sb.append(", configurationKey='").append(configurationKey).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistenceAssociation)) return false;

        PersistenceAssociation that = (PersistenceAssociation) o;

        if (!Arrays.equals(additionalForeignKeys, that.additionalForeignKeys)) return false;
        if (!Arrays.equals(additionalNonPersistentProperties, that.additionalNonPersistentProperties))
            return false;
        if (configurationKey != null ? !configurationKey.equals(that.configurationKey) : that.configurationKey != null)
            return false;
        if (!Arrays.equals(excludeFields, that.excludeFields)) return false;
        if (!Arrays.equals(includeFields, that.includeFields)) return false;
        if (persistenceOperationType != null ? !persistenceOperationType.equals(that.persistenceOperationType) : that.persistenceOperationType != null)
            return false;
        if (persistenceAssociationItems != null ? !persistenceAssociationItems.equals(that.persistenceAssociationItems) : that.persistenceAssociationItems != null)
            return false;
        if (populateToOneFields != null ? !populateToOneFields.equals(that.populateToOneFields) : that.populateToOneFields != null)
            return false;
        if (showArchivedFields != null ? !showArchivedFields.equals(that.showArchivedFields) : that.showArchivedFields != null)
            return false;
        if (useServerSideInspectionCache != null ? !useServerSideInspectionCache.equals(that.useServerSideInspectionCache) : that.useServerSideInspectionCache != null)
                    return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = additionalNonPersistentProperties != null ? Arrays.hashCode(additionalNonPersistentProperties) : 0;
        result = 31 * result + (additionalForeignKeys != null ? Arrays.hashCode(additionalForeignKeys) : 0);
        result = 31 * result + (persistenceAssociationItems != null ? persistenceAssociationItems.hashCode() : 0);
        result = 31 * result + (persistenceOperationType != null ? persistenceOperationType.hashCode() : 0);
        result = 31 * result + (populateToOneFields != null ? populateToOneFields.hashCode() : 0);
        result = 31 * result + (excludeFields != null ? Arrays.hashCode(excludeFields) : 0);
        result = 31 * result + (includeFields != null ? Arrays.hashCode(includeFields) : 0);
        result = 31 * result + (configurationKey != null ? configurationKey.hashCode() : 0);
        result = 31 * result + (showArchivedFields != null ? showArchivedFields.hashCode() : 0);
        result = 31 * result + (useServerSideInspectionCache != null ? useServerSideInspectionCache.hashCode() : 0);
        return result;
    }
}
