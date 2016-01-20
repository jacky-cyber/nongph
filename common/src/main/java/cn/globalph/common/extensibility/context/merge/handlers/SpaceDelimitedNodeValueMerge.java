package cn.globalph.common.extensibility.context.merge.handlers;

/**
 * @author Jeff Fischer
 */
public class SpaceDelimitedNodeValueMerge extends NodeValueMerge {

    @Override
    public String getDelimiter() {
        return " ";
    }
}
