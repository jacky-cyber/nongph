package cn.globalph.openadmin.dto.visitor;

import cn.globalph.openadmin.dto.AdornedTargetList;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MapStructure;
import cn.globalph.openadmin.dto.SimpleValueMapStructure;


public class PersistencePerspectiveItemVisitorAdapter implements PersistenceAssociationItemVisitor {

    @Override
    public void visit(AdornedTargetList adornedTargetList) {
        //do nothing
    }

    @Override
    public void visit(MapStructure mapStructure) {
        //do nothing
    }

    @Override
    public void visit(SimpleValueMapStructure simpleValueMapStructure) {
        //do nothing
    }

    @Override
    public void visit(ForeignKey foreignKey) {
        //do nothing
    }
}
