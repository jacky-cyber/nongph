package cn.globalph.openadmin.dto;

import java.util.Arrays;

/**
 * @author felix.wu
 */
public abstract class CollectionMetadata extends FieldMetadata {
	
	private PersistenceAssociation persistenceAssociation;
	private String collectionRootEntity;//集合元素根类型
	private boolean mutable = true;     //是否可修改
    private String[] customCriteria;

    public PersistenceAssociation getPersistenceAssociation() {
        return persistenceAssociation;
    }

    public void setPersistenceAssociation(PersistenceAssociation persistenceAssociation) {
        this.persistenceAssociation = persistenceAssociation;
    }

    public String getCollectionRootEntity() {
        return collectionRootEntity;
    }

    public void setCollectionRootEntity(String collectionCeilingEntity) {
        this.collectionRootEntity = collectionCeilingEntity;
    }

    public boolean isMutable() {
        return mutable;
    }

    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    public String[] getCustomCriteria() {
        return customCriteria;
    }

    public void setCustomCriteria(String[] customCriteria) {
        this.customCriteria = customCriteria;
    }

    @Override
    protected FieldMetadata populate(FieldMetadata metadata) {
        super.populate(metadata);
        ((CollectionMetadata) metadata).setPersistenceAssociation(persistenceAssociation.clonePersistencePerspective());
        ((CollectionMetadata) metadata).setCollectionRootEntity(collectionRootEntity);
        ((CollectionMetadata) metadata).setMutable(mutable);
        ((CollectionMetadata) metadata).setCustomCriteria(customCriteria);
        ((CollectionMetadata) metadata).setTab(getTab());
        ((CollectionMetadata) metadata).setTabOrder(getTabOrder());
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionMetadata)) return false;

        CollectionMetadata metadata = (CollectionMetadata) o;

        if (mutable != metadata.mutable) return false;
        if (collectionRootEntity != null ? !collectionRootEntity.equals(metadata.collectionRootEntity) : metadata.collectionRootEntity != null)
            return false;
        if (!Arrays.equals(customCriteria, metadata.customCriteria)) return false;
        if (persistenceAssociation != null ? !persistenceAssociation.equals(metadata.persistenceAssociation) : metadata.persistenceAssociation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = persistenceAssociation != null ? persistenceAssociation.hashCode() : 0;
        result = 31 * result + (collectionRootEntity != null ? collectionRootEntity.hashCode() : 0);
        result = 31 * result + (mutable ? 1 : 0);
        result = 31 * result + (customCriteria != null ? Arrays.hashCode(customCriteria) : 0);
        return result;
    }
}
