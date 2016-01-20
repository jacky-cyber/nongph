package cn.globalph.common.extensibility.context.merge.handlers;

import org.w3c.dom.Node;

import java.util.List;

/**
 * This adapter class allows the developer to create a merge handler
 * instance and only override a subset of the functionality, instead of
 * having to provide an independent, full implementation of the MergeHandler
 * interface.
 * 
 * @felix.wu
 *
 */
public class MergeHandlerAdapter implements MergeHandler {

    public MergeHandler[] getChildren() {
        return null;
    }

    public String getName() {
        return null;
    }

    public int getPriority() {
        return 0;
    }

    public String getXPath() {
        return null;
    }

    public Node[] merge(List<Node> nodeList1, List<Node> nodeList2,
            List<Node> exhaustedNodes) {
        return null;
    }

    public void setChildren(MergeHandler[] children) {
        //do nothing
    }

    public void setName(String name) {
        //do nothing
    }

    public void setPriority(int priority) {
        //do nothing
    }

    public void setXPath(String xpath) {
        //do nothing
    }

}
