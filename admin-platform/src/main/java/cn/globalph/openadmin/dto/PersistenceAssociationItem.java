package cn.globalph.openadmin.dto;

import cn.globalph.openadmin.dto.visitor.PersistenceAssociationItemVisitor;

/**
 * Simple marker interface for persistence perspective members
 */
public interface PersistenceAssociationItem {

    public void accept(PersistenceAssociationItemVisitor visitor);

    public PersistenceAssociationItem clonePersistenceAssociationItem();

}
