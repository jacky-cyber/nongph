package cn.globalph.openadmin.dto.visitor;

import cn.globalph.openadmin.dto.AdornedTargetList;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MapStructure;
import cn.globalph.openadmin.dto.SimpleValueMapStructure;


public interface PersistenceAssociationItemVisitor {

    public void visit(AdornedTargetList adornedTargetList);
    
    public void visit(MapStructure mapStructure);
    
    public void visit(SimpleValueMapStructure simpleValueMapStructure);
    
    public void visit(ForeignKey foreignKey);
    
}
