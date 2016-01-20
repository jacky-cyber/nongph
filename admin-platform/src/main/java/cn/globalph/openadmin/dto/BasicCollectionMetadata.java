package cn.globalph.openadmin.dto;

import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.openadmin.dto.visitor.MetadataVisitor;

/**
 * @author felix.wu
 */
public class BasicCollectionMetadata extends CollectionMetadata {

    private AddMethodType addMethodType;

    public AddMethodType getAddMethodType() {
        return addMethodType;
    }

    public void setAddMethodType(AddMethodType addMethodType) {
        this.addMethodType = addMethodType;
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    protected FieldMetadata populate(FieldMetadata metadata) {
        ((BasicCollectionMetadata) metadata).addMethodType = addMethodType;
        return super.populate(metadata);
    }

    @Override
    public FieldMetadata cloneFieldMetadata() {
        BasicCollectionMetadata basicCollectionMetadata = new BasicCollectionMetadata();
        return populate(basicCollectionMetadata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicCollectionMetadata)) return false;
        if (!super.equals(o)) return false;

        BasicCollectionMetadata that = (BasicCollectionMetadata) o;

        if (addMethodType != that.addMethodType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (addMethodType != null ? addMethodType.hashCode() : 0);
        return result;
    }
}
