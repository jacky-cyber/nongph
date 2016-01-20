package cn.globalph.common.presentation.client;

/**
 * 关联类型
 * @author felix.wu
 */
public enum PersistenceAssociationItemType {
    FOREIGNKEY,           //to one
    ADORNEDTARGETLIST,    //to many with additional data
    MAPSTRUCTURE          //to many with key
}
