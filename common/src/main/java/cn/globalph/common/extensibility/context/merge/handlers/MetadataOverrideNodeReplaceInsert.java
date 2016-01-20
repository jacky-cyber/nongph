package cn.globalph.common.extensibility.context.merge.handlers;

import org.w3c.dom.Node;

import java.util.List;

/**
 * This handler is responsible for replacing nodes in the source document
 * with the same nodes from the patch document. Note, additional nodes
 * from the patch document that are not present in the source document
 * are simply appended to the source document. This is specialized for the
 * mo:overrideItem element.
 * 
 * @felix.wu
 *
 */
public class MetadataOverrideNodeReplaceInsert extends NodeReplaceInsert {

    protected boolean checkNode(List<Node> usedNodes, Node[] primaryNodes, Node node) {
        //find matching nodes based on id
        if (replaceNode(primaryNodes, node, "configurationKey", usedNodes)) {
            return true;
        }
        //find matching nodes based on name
        if (replaceNode(primaryNodes, node, "ceilingEntity", usedNodes)) {
            return true;
        }
        //check if this same node already exists
        if (exactNodeExists(primaryNodes, node, usedNodes)) {
            return true;
        }
        return false;
    }

}
