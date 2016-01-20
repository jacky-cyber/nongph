package cn.globalph.common.extensibility.context.merge.handlers;

/**
 * @author Jeff Fischer
 */
public class CommaDelimitedNodeValueMerge extends NodeValueMerge {

    @Override
    public String getDelimiter() {
        return ",";
    }

    @Override
    public String getRegEx() {
        return getDelimiter();
    }
}
