package cn.globalph.openadmin.dto.visitor;

import cn.globalph.openadmin.dto.AdornedTargetCollectionMetadata;
import cn.globalph.openadmin.dto.BasicCollectionMetadata;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.MapMetadata;

import java.io.Serializable;

/**
 * @author Jeff Fischer
 */
public class MetadataVisitorAdapter implements MetadataVisitor, Serializable {

    @Override
    public void visit(AdornedTargetCollectionMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(BasicFieldMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(BasicCollectionMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(MapMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }
}
