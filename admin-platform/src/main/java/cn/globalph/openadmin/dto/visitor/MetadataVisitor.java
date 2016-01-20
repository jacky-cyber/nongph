package cn.globalph.openadmin.dto.visitor;

import cn.globalph.openadmin.dto.AdornedTargetCollectionMetadata;
import cn.globalph.openadmin.dto.BasicCollectionMetadata;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.MapMetadata;

/**
 * @author Jeff Fischer
 */
public interface MetadataVisitor {

    public void visit(BasicFieldMetadata metadata);

    public void visit(BasicCollectionMetadata metadata);

    public void visit(AdornedTargetCollectionMetadata metadata);

    public void visit(MapMetadata metadata);
}
