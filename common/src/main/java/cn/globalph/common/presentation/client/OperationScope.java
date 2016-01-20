package cn.globalph.common.presentation.client;

/**
 * Describes the target of an admin operation. This can be a regular entity itself, or a collection
 * or map property.
 *
 * @author felix.wu
 *
 */
public enum OperationScope {
    NONDESTRUCTIVEREMOVE, //非破坏性删除
    BASIC,                //对象
    ADORNEDTARGETLIST,    //对象以及关联的集合
    MAP                   //对象以及管理的Map
}
