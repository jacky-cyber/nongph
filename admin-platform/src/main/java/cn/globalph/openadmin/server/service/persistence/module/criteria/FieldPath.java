package cn.globalph.openadmin.server.service.persistence.module.criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * @author felix.wu
 */
public class FieldPath {

    protected List<String> associationPath = new ArrayList<String>();     //关联路径，用于join，支持多表join
    protected List<String> targetPropertyPieces = new ArrayList<String>();//目标属性
    protected String targetProperty;                                      //目标属性，用户覆写上述两个属性

    public FieldPath withAssociationPath(List<String> associationPath) {
        setAssociationPath(associationPath);
        return this;
    }

    public FieldPath withTargetPropertyPieces(List<String> targetPropertyPieces) {
        setTargetPropertyPieces(targetPropertyPieces);
        return this;
    }

    public FieldPath withTargetProperty(String targetProperty) {
        setTargetProperty(targetProperty);
        return this;
    }

    public List<String> getAssociationPath() {
        return associationPath;
    }

    public void setAssociationPath(List<String> associationPath) {
        this.associationPath = associationPath;
    }

    public List<String> getTargetPropertyPieces() {
        return targetPropertyPieces;
    }

    public void setTargetPropertyPieces(List<String> targetPropertyPieces) {
        this.targetPropertyPieces = targetPropertyPieces;
    }

    public String getTargetProperty() {
        return targetProperty;
    }

    public void setTargetProperty(String targetProperty) {
        this.targetProperty = targetProperty;
    }
}
